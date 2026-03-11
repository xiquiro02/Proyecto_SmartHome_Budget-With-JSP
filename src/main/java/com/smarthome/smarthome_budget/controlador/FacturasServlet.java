package com.smarthome.smarthome_budget.controlador;

import com.smarthome.smarthome_budget.dao.CategoriaEgresoDao;
import com.smarthome.smarthome_budget.dao.MetodoPagoDao;
import com.smarthome.smarthome_budget.dao.RegistroEgresoDao;
import com.smarthome.smarthome_budget.modelo.CategoriaEgreso;
import com.smarthome.smarthome_budget.modelo.MetodoPago;
import com.smarthome.smarthome_budget.modelo.RegistroEgreso;
import com.smarthome.smarthome_budget.modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Servlet central del módulo de Facturas.
 * Maneja todas las operaciones CRUD mediante el parámetro "accion".
 *
 * GET  /Facturas              → dashboard (01_facturas.jsp)
 * GET  /Facturas?accion=form  → formulario de registro
 * GET  /Facturas?accion=lista → consultar todas las facturas
 * GET  /Facturas?accion=filtroEstado&estado=X
 * GET  /Facturas?accion=filtroCategoria&idCategoria=X
 * GET  /Facturas?accion=filtroFecha&mes=X&anio=X
 * GET  /Facturas?accion=editar&id=X
 * GET  /Facturas?accion=confirmarEliminar&id=X
 * GET  /Facturas?accion=historial
 * GET  /Facturas?accion=historialCategoria&idCategoria=X
 * GET  /Facturas?accion=historialFecha&mes=X&anio=X
 * GET  /Facturas?accion=historialMonto&rango=X
 * POST /Facturas?accion=registrar
 * POST /Facturas?accion=actualizar
 * POST /Facturas?accion=eliminar
 * POST /Facturas?accion=marcarPagada
 */
@WebServlet("/Facturas")
public class FacturasServlet extends HttpServlet {

    private static final String BASE_JSP = "/public/modules/02_Gestion_facturas-recordatorios/";

    // ─── GET ─────────────────────────────────────────────────────────────────

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_requerida");
            return;
        }

        int idHogar = (Integer) session.getAttribute("idHogar");
        String accion = req.getParameter("accion");
        if (accion == null) accion = "dashboard";

        // Actualizar vencidas automáticamente antes de mostrar cualquier listado
        RegistroEgresoDao dao = new RegistroEgresoDao();
        dao.actualizarVencidas();

        switch (accion) {
            case "dashboard":
                mostrarDashboard(req, resp, dao, idHogar);
                break;
            case "form":
                mostrarFormularioRegistro(req, resp, idHogar);
                break;
            case "lista":
                mostrarLista(req, resp, dao, idHogar, null, null, null, null);
                break;
            case "filtroEstado":
                String estado = req.getParameter("estado");
                mostrarLista(req, resp, dao, idHogar, estado, null, null, null);
                break;
            case "filtroCategoria":
                String catParam = req.getParameter("idCategoria");
                mostrarLista(req, resp, dao, idHogar, null, parsearInt(catParam), null, null);
                break;
            case "filtroFecha":
                mostrarLista(req, resp, dao, idHogar, null, null,
                        parsearInt(req.getParameter("mes")),
                        parsearInt(req.getParameter("anio")));
                break;
            case "editar":
                mostrarFormularioEditar(req, resp, dao, idHogar);
                break;
            case "confirmarEliminar":
                mostrarConfirmarEliminar(req, resp, dao, idHogar);
                break;
            case "historial":
                mostrarHistorial(req, resp, dao, idHogar, null, null, null, null);
                break;
            case "historialCategoria":
                mostrarHistorial(req, resp, dao, idHogar,
                        parsearInt(req.getParameter("idCategoria")), null, null, null);
                break;
            case "historialFecha":
                mostrarHistorial(req, resp, dao, idHogar, null,
                        parsearInt(req.getParameter("mes")),
                        parsearInt(req.getParameter("anio")), null);
                break;
            case "historialMonto":
                mostrarHistorial(req, resp, dao, idHogar, null, null, null,
                        req.getParameter("rango"));
                break;
            default:
                mostrarDashboard(req, resp, dao, idHogar);
        }
    }

    // ─── POST ────────────────────────────────────────────────────────────────

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_requerida");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        int idHogar = (Integer) session.getAttribute("idHogar");
        int idRol   = (Integer) session.getAttribute("idRol");
        String accion = req.getParameter("accion");
        if (accion == null) accion = "";

        switch (accion) {
            case "registrar":
                procesarRegistro(req, resp, usuario, idHogar, idRol);
                break;
            case "actualizar":
                procesarActualizacion(req, resp, usuario, idHogar, idRol);
                break;
            case "eliminar":
                procesarEliminacion(req, resp, idHogar, idRol);
                break;
            case "marcarPagada":
                procesarMarcarPagada(req, resp, idHogar, idRol);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/Facturas");
        }
    }

    // ─── Métodos de visualización ────────────────────────────────────────────

    private void mostrarDashboard(HttpServletRequest req, HttpServletResponse resp,
                                  RegistroEgresoDao dao, int idHogar)
            throws ServletException, IOException {
        int[] resumen = dao.obtenerResumen(idHogar);
        req.setAttribute("pendientes", resumen[0]);
        req.setAttribute("pagadas",    resumen[1]);
        req.setAttribute("vencidas",   resumen[2]);
        forward(req, resp, "01_facturas.jsp");
    }

    private void mostrarFormularioRegistro(HttpServletRequest req, HttpServletResponse resp,
                                           int idHogar)
            throws ServletException, IOException {
        cargarCatalogos(req);
        forward(req, resp, "02_formularioRegistrar-facturas.jsp");
    }

    private void mostrarLista(HttpServletRequest req, HttpServletResponse resp,
                              RegistroEgresoDao dao, int idHogar,
                              String estado, Integer idCategoria, Integer mes, Integer anio)
            throws ServletException, IOException {

        List<RegistroEgreso> facturas;
        String filtroActivo = "ninguno";

        if (estado != null && !estado.isEmpty()) {
            facturas = dao.listarPorEstado(idHogar, estado);
            filtroActivo = "estado";
            req.setAttribute("estadoFiltro", estado);
        } else if (idCategoria != null) {
            facturas = dao.listarPorCategoria(idHogar, idCategoria);
            filtroActivo = "categoria";
            req.setAttribute("categoriaFiltro", idCategoria);
        } else if (mes != null && anio != null) {
            facturas = dao.listarPorMes(idHogar, mes, anio);
            filtroActivo = "fecha";
            req.setAttribute("mesFiltro", mes);
            req.setAttribute("anioFiltro", anio);
        } else {
            facturas = dao.listarPorHogar(idHogar);
        }

        cargarCatalogos(req);
        req.setAttribute("facturas", facturas);
        req.setAttribute("filtroActivo", filtroActivo);

        // Elegir la vista según el filtro
        if ("estado".equals(filtroActivo)) {
            forward(req, resp, "06_Consultar-Facturas.-Filtro-Estado.jsp");
        } else if ("categoria".equals(filtroActivo)) {
            forward(req, resp, "04_Consultar-Facturas.-Filtro-Categoria..jsp");
        } else if ("fecha".equals(filtroActivo)) {
            forward(req, resp, "05_Consultar-Facturas.-Filtro-Fecha.jsp");
        } else {
            forward(req, resp, "03_Consultar-Facturas.jsp");
        }
    }

    private void mostrarFormularioEditar(HttpServletRequest req, HttpServletResponse resp,
                                         RegistroEgresoDao dao, int idHogar)
            throws ServletException, IOException {
        int id = parsearInt(req.getParameter("id"));
        if (id <= 0) {
            resp.sendRedirect(req.getContextPath() + "/Facturas?accion=lista&error=id_invalido");
            return;
        }
        RegistroEgreso factura = dao.obtenerPorId(id, idHogar);
        if (factura == null) {
            resp.sendRedirect(req.getContextPath() + "/Facturas?accion=lista&error=no_encontrada");
            return;
        }
        cargarCatalogos(req);
        req.setAttribute("factura", factura);
        forward(req, resp, "07_EditarFactura.jsp");
    }

    private void mostrarConfirmarEliminar(HttpServletRequest req, HttpServletResponse resp,
                                          RegistroEgresoDao dao, int idHogar)
            throws ServletException, IOException {
        int id = parsearInt(req.getParameter("id"));
        if (id <= 0) {
            resp.sendRedirect(req.getContextPath() + "/Facturas?accion=lista&error=id_invalido");
            return;
        }
        RegistroEgreso factura = dao.obtenerPorId(id, idHogar);
        if (factura == null) {
            resp.sendRedirect(req.getContextPath() + "/Facturas?accion=lista&error=no_encontrada");
            return;
        }
        req.setAttribute("factura", factura);
        forward(req, resp, "09_EliminarFactura.jsp");
    }

    private void mostrarHistorial(HttpServletRequest req, HttpServletResponse resp,
                                  RegistroEgresoDao dao, int idHogar,
                                  Integer idCategoria, Integer mes, Integer anio, String rango)
            throws ServletException, IOException {

        List<RegistroEgreso> historial;
        String filtroActivo = "ninguno";

        if (idCategoria != null) {
            historial = dao.listarPagadasPorCategoria(idHogar, idCategoria);
            filtroActivo = "categoria";
            req.setAttribute("categoriaFiltro", idCategoria);
        } else if (mes != null && anio != null) {
            historial = dao.listarPagadasPorMes(idHogar, mes, anio);
            filtroActivo = "fecha";
            req.setAttribute("mesFiltro", mes);
            req.setAttribute("anioFiltro", anio);
        } else if (rango != null) {
            BigDecimal min = BigDecimal.ZERO;
            BigDecimal max = new BigDecimal("999999999");
            switch (rango) {
                case "menor50":  max = new BigDecimal("50000"); break;
                case "50a150":   min = new BigDecimal("50000"); max = new BigDecimal("150000"); break;
                case "mayor200": min = new BigDecimal("200000"); break;
            }
            historial = dao.listarPagadasPorMonto(idHogar, min, max);
            filtroActivo = "monto";
            req.setAttribute("rangoFiltro", rango);
        } else {
            historial = dao.listarPagadas(idHogar);
        }

        BigDecimal[] totales = dao.obtenerTotalPagado(idHogar);
        cargarCatalogos(req);
        req.setAttribute("historial", historial);
        req.setAttribute("totalPagado", totales[0]);
        req.setAttribute("cantidadPagadas", totales[1].intValue());
        req.setAttribute("filtroActivo", filtroActivo);

        if ("categoria".equals(filtroActivo)) {
            forward(req, resp, "12_HistorialPagos-Filtrar-categoria.jsp");
        } else if ("fecha".equals(filtroActivo)) {
            forward(req, resp, "13_HistorialPagos-Filtrar-Fecha.jsp");
        } else if ("monto".equals(filtroActivo)) {
            forward(req, resp, "14_HistorialPagos-Filtrar-Monto.jsp");
        } else {
            forward(req, resp, "11_HistorialPagos.jsp");
        }
    }

    // ─── Métodos de procesamiento ─────────────────────────────────────────────

    private void procesarRegistro(HttpServletRequest req, HttpServletResponse resp,
                                  Usuario usuario, int idHogar, int idRol)
            throws ServletException, IOException {

        // Solo ADMINISTRADOR (1) y COTITULAR (2) pueden registrar facturas
        if (idRol != 1 && idRol != 2) {
            resp.sendRedirect(req.getContextPath() + "/Facturas?error=sin_permiso");
            return;
        }

        String nombreFactura = req.getParameter("nombreFactura");
        String montoStr      = req.getParameter("monto");
        String catStr        = req.getParameter("idCategoriaEgreso");
        String metStr        = req.getParameter("idMetodoPago");
        String fechaStr      = req.getParameter("fechaVencimiento");
        String descripcion   = req.getParameter("descripcion");
        String estadoPago    = req.getParameter("estadoPago");

        // Validaciones
        if (estaVacio(nombreFactura) || estaVacio(montoStr) || estaVacio(catStr)
                || estaVacio(metStr) || estaVacio(fechaStr) || estaVacio(estadoPago)) {
            req.setAttribute("error", "Todos los campos obligatorios deben completarse.");
            cargarCatalogos(req);
            forward(req, resp, "02_formularioRegistrar-facturas.jsp");
            return;
        }

        BigDecimal monto;
        try {
            monto = new BigDecimal(montoStr.replace(",", "."));
            if (monto.compareTo(BigDecimal.ZERO) <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            req.setAttribute("error", "El monto debe ser un número positivo.");
            cargarCatalogos(req);
            forward(req, resp, "02_formularioRegistrar-facturas.jsp");
            return;
        }

        LocalDateTime fechaVencimiento;
        try {
            fechaVencimiento = LocalDate.parse(fechaStr).atStartOfDay();
        } catch (DateTimeParseException e) {
            req.setAttribute("error", "La fecha de vencimiento no es válida.");
            cargarCatalogos(req);
            forward(req, resp, "02_formularioRegistrar-facturas.jsp");
            return;
        }

        // RF07: la fecha de vencimiento no puede ser pasada al registrar
        if (fechaVencimiento.isBefore(LocalDateTime.now()) && !"Pagada".equals(estadoPago)) {
            req.setAttribute("error", "La fecha de vencimiento no puede ser una fecha pasada.");
            cargarCatalogos(req);
            forward(req, resp, "02_formularioRegistrar-facturas.jsp");
            return;
        }

        RegistroEgreso egreso = new RegistroEgreso();
        egreso.setIdHogar(idHogar);
        egreso.setIdUsuario(usuario.getIDUsuario());
        egreso.setNombreFactura(nombreFactura.trim());
        egreso.setMonto(monto);
        egreso.setIdCategoriaEgreso(Integer.parseInt(catStr));
        egreso.setIdMetodoPago(Integer.parseInt(metStr));
        egreso.setFechaVencimiento(fechaVencimiento);
        egreso.setDescripcion(descripcion != null ? descripcion.trim() : "");
        egreso.setEstadoPago(estadoPago);

        RegistroEgresoDao dao = new RegistroEgresoDao();
        int idGenerado = dao.insertarEgreso(egreso);

        if (idGenerado > 0) {
            resp.sendRedirect(req.getContextPath() + "/Facturas?accion=lista&exito=registrada");
        } else {
            req.setAttribute("error", "Error al guardar la factura. Intenta nuevamente.");
            cargarCatalogos(req);
            forward(req, resp, "02_formularioRegistrar-facturas.jsp");
        }
    }

    private void procesarActualizacion(HttpServletRequest req, HttpServletResponse resp,
                                       Usuario usuario, int idHogar, int idRol)
            throws ServletException, IOException {

        if (idRol != 1 && idRol != 2) {
            resp.sendRedirect(req.getContextPath() + "/Facturas?error=sin_permiso");
            return;
        }

        int id = parsearInt(req.getParameter("idEgreso"));
        String nombreFactura = req.getParameter("nombreFactura");
        String montoStr      = req.getParameter("monto");
        String catStr        = req.getParameter("idCategoriaEgreso");
        String metStr        = req.getParameter("idMetodoPago");
        String fechaStr      = req.getParameter("fechaVencimiento");
        String descripcion   = req.getParameter("descripcion");
        String estadoPago    = req.getParameter("estadoPago");

        if (id <= 0 || estaVacio(nombreFactura) || estaVacio(montoStr)
                || estaVacio(catStr) || estaVacio(metStr)
                || estaVacio(fechaStr) || estaVacio(estadoPago)) {
            resp.sendRedirect(req.getContextPath() + "/Facturas?accion=editar&id=" + id + "&error=campos_vacios");
            return;
        }

        BigDecimal monto;
        try {
            monto = new BigDecimal(montoStr.replace(",", "."));
            if (monto.compareTo(BigDecimal.ZERO) <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/Facturas?accion=editar&id=" + id + "&error=monto_invalido");
            return;
        }

        LocalDateTime fechaVencimiento;
        try {
            fechaVencimiento = LocalDate.parse(fechaStr).atStartOfDay();
        } catch (DateTimeParseException e) {
            resp.sendRedirect(req.getContextPath() + "/Facturas?accion=editar&id=" + id + "&error=fecha_invalida");
            return;
        }

        RegistroEgreso egreso = new RegistroEgreso();
        egreso.setIdEgresos(id);
        egreso.setIdHogar(idHogar);
        egreso.setIdUsuario(usuario.getIDUsuario());
        egreso.setNombreFactura(nombreFactura.trim());
        egreso.setMonto(monto);
        egreso.setIdCategoriaEgreso(Integer.parseInt(catStr));
        egreso.setIdMetodoPago(Integer.parseInt(metStr));
        egreso.setFechaVencimiento(fechaVencimiento);
        egreso.setDescripcion(descripcion != null ? descripcion.trim() : "");
        egreso.setEstadoPago(estadoPago);
        // Si se marcó como Pagada en edición, establecer FechaPago
        if ("Pagada".equals(estadoPago)) {
            egreso.setFechaPago(LocalDateTime.now());
        }

        RegistroEgresoDao dao = new RegistroEgresoDao();
        boolean ok = dao.actualizar(egreso);

        if (ok) {
            forward(req, resp, "08_ConfirmacionEdicion.jsp");
        } else {
            resp.sendRedirect(req.getContextPath() + "/Facturas?accion=editar&id=" + id + "&error=error_db");
        }
    }

    private void procesarEliminacion(HttpServletRequest req, HttpServletResponse resp,
                                     int idHogar, int idRol)
            throws IOException {

        if (idRol != 1 && idRol != 2) {
            resp.sendRedirect(req.getContextPath() + "/Facturas?error=sin_permiso");
            return;
        }

        int id = parsearInt(req.getParameter("idEgreso"));
        if (id <= 0) {
            resp.sendRedirect(req.getContextPath() + "/Facturas?accion=lista&error=id_invalido");
            return;
        }

        RegistroEgresoDao dao = new RegistroEgresoDao();
        // Eliminar también los recordatorios asociados
        new com.smarthome.smarthome_budget.dao.RecordatorioEgresoDao().eliminarPorEgreso(id);
        boolean ok = dao.eliminar(id, idHogar);

        if (ok) {
            resp.sendRedirect(req.getContextPath() + "/Facturas?accion=lista&exito=eliminada");
        } else {
            resp.sendRedirect(req.getContextPath() + "/Facturas?accion=lista&error=error_eliminar");
        }
    }

    private void procesarMarcarPagada(HttpServletRequest req, HttpServletResponse resp,
                                      int idHogar, int idRol)
            throws IOException {

        if (idRol != 1 && idRol != 2) {
            resp.sendRedirect(req.getContextPath() + "/Facturas?error=sin_permiso");
            return;
        }

        int id = parsearInt(req.getParameter("idEgreso"));
        RegistroEgresoDao dao = new RegistroEgresoDao();
        dao.marcarComoPagada(id, idHogar);
        resp.sendRedirect(req.getContextPath() + "/Facturas?accion=lista&exito=pagada");
    }

    // ─── Utilidades ──────────────────────────────────────────────────────────

    private void cargarCatalogos(HttpServletRequest req) {
        req.setAttribute("categorias",   new CategoriaEgresoDao().listarCategorias());
        req.setAttribute("metodosPago",  new MetodoPagoDao().listarMetodosPago());
    }

    private void forward(HttpServletRequest req, HttpServletResponse resp, String vista)
            throws ServletException, IOException {
        req.getRequestDispatcher(BASE_JSP + vista).forward(req, resp);
    }

    private boolean estaVacio(String s) {
        return s == null || s.trim().isEmpty();
    }

    private int parsearInt(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }
}
