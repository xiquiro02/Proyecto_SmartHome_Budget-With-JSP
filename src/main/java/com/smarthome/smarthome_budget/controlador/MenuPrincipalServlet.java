package com.smarthome.smarthome_budget.controlador;

// Importación de clases DAO y modelos necesarios para cargar los datos del dashboard
import com.smarthome.smarthome_budget.dao.RegistroEgresoDao;
import com.smarthome.smarthome_budget.dao.PresupuestoMensualDao;
import com.smarthome.smarthome_budget.dao.InventarioCasaDao;
import com.smarthome.smarthome_budget.dao.ListaComprasDao;
import com.smarthome.smarthome_budget.modelo.ListaCompras;
import com.smarthome.smarthome_budget.modelo.PresupuestoMensual;
import com.smarthome.smarthome_budget.modelo.RegistroEgreso;
import com.smarthome.smarthome_budget.modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/* Clase: MenuPrincipalServlet
   Propósito: Cargar los datos dinámicos del panel principal (dashboard) de la aplicación.
   Obtiene el próximo pago pendiente, el presupuesto disponible del mes, el número de
   productos con stock bajo y las listas de compras con ítems pendientes.
   También gestiona la visualización del código de invitación recién generado.
   URL mapeada: /Menu
*/
@WebServlet("/Menu")
public class MenuPrincipalServlet extends HttpServlet {

    // Constante de tipo texto con la ruta base de las vistas del módulo menú principal
    private static final String BASE = "/public/modules/MenuPrincipal/";

    /* Método: doGet
       Propósito: Consultar todas las fuentes de datos necesarias para el dashboard,
       asignarlas como atributos del request y hacer forward a la vista JSP del menú.
       @param req  → Objeto con los parámetros de la petición HTTP GET
       @param resp → Objeto para hacer forward a la vista del menú principal
    */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Verifica que haya una sesión activa con usuario autenticado
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            // Sin sesión, redirige al formulario de inicio de sesión
            resp.sendRedirect(req.getContextPath() +
                    "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_requerida");
            return;
        }

        // Objeto Usuario recuperado de la sesión (datos del usuario autenticado)
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        // Variable entera que almacena el ID del hogar del usuario en sesión
        int idHogar     = (Integer) session.getAttribute("idHogar");
        // Variable entera que almacena el ID del rol del usuario (1=Admin, 2=Cotitular, 3=Invitado)
        int idRol       = (Integer) session.getAttribute("idRol");

        // ── Próximo pago pendiente ────────────────────────────────────────────
        // Instancia del DAO de egresos para consultar pagos pendientes
        RegistroEgresoDao egresoDao = new RegistroEgresoDao();
        // Actualiza las facturas cuya fecha de vencimiento ya pasó antes de mostrar
        egresoDao.actualizarVencidas();

        // Lista de objetos RegistroEgreso con estado "Pendiente", ordenados por fecha ASC
        List<RegistroEgreso> pendientes = egresoDao.listarPorEstado(idHogar, "Pendiente");
        // Objeto RegistroEgreso que representa el próximo pago; null si no hay pendientes
        RegistroEgreso proximoPago = pendientes.isEmpty() ? null : pendientes.get(0);

        // ── Presupuesto disponible del mes ────────────────────────────────────
        // Instancia del DAO de presupuesto mensual para obtener datos del mes actual
        PresupuestoMensualDao presDao = new PresupuestoMensualDao();
        // Objeto PresupuestoMensual con los datos del presupuesto del mes en curso; null si no existe
        PresupuestoMensual presupuesto = presDao.obtenerMesActual(idHogar);
        // Variable de tipo decimal que almacena el total de egresos del mes actual
        BigDecimal totalEgresos       = presDao.totalEgresosMesActual(idHogar);
        // Variable de tipo decimal que almacena el monto disponible; inicia en cero
        BigDecimal disponible         = BigDecimal.ZERO;

        if (presupuesto != null) {
            // Calcula el disponible: monto máximo del presupuesto menos el total gastado
            disponible = presupuesto.getMontoMax().subtract(totalEgresos);
            // Si el resultado es negativo (gastó más de lo presupuestado), se deja en cero
            if (disponible.compareTo(BigDecimal.ZERO) < 0) disponible = BigDecimal.ZERO;
        }

        // ── Productos con stock bajo o agotados ───────────────────────────────
        // Instancia del DAO de inventario para consultar el estado del stock
        InventarioCasaDao invDao = new InventarioCasaDao();
        // Variable entera que almacena la cantidad total de productos con stock bajo o agotados
        int productosAgotados = invDao.listarStockBajo(idHogar).size()
                              + invDao.listarAgotados(idHogar).size();

        // ── Listas de compras con ítems pendientes ────────────────────────────
        // Instancia del DAO de listas de compras para filtrar las que tienen pendientes
        ListaComprasDao listasDao = new ListaComprasDao();
        // Variable de tipo long que almacena cuántas listas activas tienen ítems sin comprar
        long listasConPendientes = listasDao.listarPorHogar(idHogar).stream()
                .filter(l -> ("Pendiente".equals(l.getEstadoLista())
                           || "En progreso".equals(l.getEstadoLista()))
                           && l.getTotalPendientes() > 0)
                .count();

        // ── Pasar atributos a la vista ────────────────────────────────────────
        // Envía el próximo pago (RegistroEgreso o null) al JSP
        req.setAttribute("proximoPago",        proximoPago);
        // Envía el objeto de presupuesto mensual (PresupuestoMensual o null) al JSP
        req.setAttribute("presupuesto",        presupuesto);
        // Envía el total de egresos del mes (BigDecimal) al JSP
        req.setAttribute("totalEgresos",       totalEgresos);
        // Envía el monto disponible del presupuesto (BigDecimal) al JSP
        req.setAttribute("disponible",         disponible);
        // Envía el ID del rol (entero) al JSP para controlar visibilidad de secciones
        req.setAttribute("idRol",              idRol);
        // Envía la cantidad de productos agotados o con stock bajo (entero) al JSP
        req.setAttribute("productosAgotados",  productosAgotados);
        // Envía la cantidad de listas con ítems pendientes (entero) al JSP
        req.setAttribute("listasConPendientes", (int) listasConPendientes);

        // Verifica si se debe mostrar el código de invitación recién generado
        String accion = req.getParameter("accion");
        if ("codigoGenerado".equals(accion)) {
            // Recupera el código generado almacenado temporalmente en sesión
            String codigoGenerado = (String) session.getAttribute("codigo_generado");
            // Recupera el nombre del rol asignado al código (texto)
            String rolAsignado    = (String) session.getAttribute("rol_asignado");
            if (codigoGenerado != null) {
                // Pasa el código y el nombre del rol a la vista para mostrarlos
                req.setAttribute("codigoGenerado", codigoGenerado);
                req.setAttribute("rolAsignado", rolAsignado);
                // Limpia de la sesión para que no se vuelva a mostrar si el usuario recarga
                session.removeAttribute("codigo_generado");
                session.removeAttribute("rol_asignado");
            }
        }

        // Hace forward a la vista JSP del menú principal con todos los datos cargados
        req.getRequestDispatcher(BASE + "01_menuPrincipal.jsp").forward(req, resp);
    }
}
