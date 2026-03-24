package com.smarthome.smarthome_budget.controlador;

// Importación de todas las clases DAO y modelo necesarias para el módulo de listas de compras
import com.smarthome.smarthome_budget.dao.*;
import com.smarthome.smarthome_budget.modelo.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;

/* Clase: ListasComprasServlet
   Propósito: Gestionar todas las operaciones del módulo Listas de Compras.
   Permite crear, editar, eliminar y consultar listas, así como agregar, editar,
   eliminar y marcar como comprados los productos dentro de cada lista.
   El rol 3 (Invitado) puede ver las listas pero no puede gestionarlas.
   URL mapeada: /Listas
*/
@WebServlet("/Listas")
public class ListasComprasServlet extends HttpServlet {

    // Constante de tipo texto con la ruta base de las vistas del módulo de listas
    private static final String BASE = "/public/modules/03_ListasCompras/";

    // Patrón de validación para nombres de lista: letras (con tildes/ñ), números, espacios, . - _
    private static final Pattern P_NOMBRE_LISTA    = Pattern.compile("^[\\p{L}\\p{N} .\\-_]+$");
    // Patrón de validación para nombres de producto: letras, números, espacios y: . , # -
    private static final Pattern P_NOMBRE_PRODUCTO = Pattern.compile("^[\\p{L}\\p{N} .,#\\-]+$");

    /* Método: doGet
       Propósito: Mostrar las vistas del módulo según la acción solicitada por URL.
       Gestiona el dashboard, formularios de creación/edición y vistas de detalle.
       @param req  → Objeto con el parámetro "accion" en la URL
       @param resp → Objeto para hacer forward a la vista correspondiente
    */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Verifica que haya sesión activa con usuario autenticado
        if (!sesionValida(req, resp)) return;

        // Variable entera que almacena el ID del hogar del usuario en sesión
        int idHogar = idHogar(req);
        // Variable de tipo texto que almacena la acción solicitada; "dashboard" por defecto
        String accion = nvl(req.getParameter("accion"));
        if (accion.isEmpty()) accion = "dashboard";

        // Enruta a la vista o acción correspondiente según el parámetro recibido
        switch (accion) {
            case "dashboard":               mostrarDashboard(req, resp, idHogar);           break;
            case "crear":                   mostrarFormCrear(req, resp);                     break;
            case "editar":                  mostrarFormEditar(req, resp, idHogar);           break;
            case "verDetalle":              mostrarDetalle(req, resp, idHogar);              break;
            case "confirmarEliminar":       mostrarConfirmarEliminar(req, resp, idHogar);    break;
            case "agregarProducto":         mostrarFormAgregarProducto(req, resp, idHogar);  break;
            case "editarProducto":          mostrarFormEditarProducto(req, resp);            break;
            case "confirmarEliminarProd":   mostrarConfirmarEliminarProd(req, resp);         break;
            default:                        mostrarDashboard(req, resp, idHogar);
        }
    }

    /* Método: doPost
       Propósito: Procesar las acciones que modifican datos: crear/editar/eliminar listas,
       agregar/editar/eliminar productos y marcar ítems como comprados.
       @param req  → Objeto con los datos del formulario enviado
       @param resp → Objeto para hacer forward o redirigir al resultado
    */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Establece la codificación UTF-8 para aceptar caracteres especiales del formulario
        req.setCharacterEncoding("UTF-8");
        // Verifica que haya sesión activa con usuario autenticado
        if (!sesionValida(req, resp)) return;

        // Variable entera que almacena el ID del hogar del usuario en sesión
        int idHogar = idHogar(req);
        // Variable entera que almacena el ID del rol del usuario (1=Admin, 2=Cotitular, 3=Invitado)
        int idRol   = idRol(req);
        // Objeto Usuario recuperado de la sesión
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");
        // Variable de tipo texto que almacena la acción enviada por el formulario
        String accion   = nvl(req.getParameter("accion"));

        // Enruta a la acción correspondiente según el parámetro enviado
        switch (accion) {
            case "registrar":           crearLista(req, resp, usuario, idHogar, idRol);      break;
            case "actualizar":          actualizarLista(req, resp, idHogar, idRol);          break;
            case "eliminar":            eliminarLista(req, resp, idHogar, idRol);            break;
            case "guardarProducto":     guardarProducto(req, resp, idHogar, idRol);          break;
            case "actualizarProducto":  actualizarProducto(req, resp, idHogar, idRol);       break;
            case "eliminarProducto":    eliminarProducto(req, resp, idHogar, idRol);         break;
            case "toggleComprado":      toggleComprado(req, resp, idHogar);                  break;
            case "marcarTodos":         marcarTodos(req, resp, idHogar);                     break;
            default: resp.sendRedirect(req.getContextPath() + "/Listas");
        }
    }

    // ─── Vistas GET ───────────────────────────────────────────────────────────

    /* Método: mostrarDashboard
       Propósito: Cargar todas las listas del hogar y mostrar la vista del dashboard.
       @param req     → Objeto de la petición HTTP
       @param resp    → Objeto para hacer forward
       @param idHogar → Entero con el ID del hogar del usuario
    */
    private void mostrarDashboard(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {
        // Asigna la lista de listas del hogar al atributo "listas" del request
        req.setAttribute("listas", new ListaComprasDao().listarPorHogar(idHogar));
        forward(req, resp, "01_listasCompras.jsp");
    }

    /* Método: mostrarFormCrear
       Propósito: Mostrar el formulario vacío para crear una nueva lista de compras.
    */
    private void mostrarFormCrear(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        forward(req, resp, "02_CrearLista.jsp");
    }

    /* Método: mostrarFormEditar
       Propósito: Buscar la lista por ID y mostrar el formulario de edición con sus datos.
       @param idHogar → Entero con el ID del hogar para verificar que la lista pertenezca al hogar
    */
    private void mostrarFormEditar(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {
        // Variable entera que almacena el ID de la lista a editar
        int id = parseInt(req.getParameter("id"));
        if (id <= 0) { resp.sendRedirect(req.getContextPath() + "/Listas"); return; }
        // Objeto ListaCompras con los datos de la lista; null si no pertenece al hogar
        ListaCompras lista = new ListaComprasDao().obtenerPorId(id, idHogar);
        if (lista == null) { resp.sendRedirect(req.getContextPath() + "/Listas?error=no_encontrada"); return; }
        req.setAttribute("lista", lista);
        // Asigna la lista de productos de la lista al request
        req.setAttribute("detalles", new DetalleListaComprasDao().listarPorLista(id));
        forward(req, resp, "04_EditarListaCompras.jsp");
    }

    /* Método: mostrarDetalle
       Propósito: Buscar la lista por ID y mostrar la vista con el detalle completo
       incluyendo todos sus productos y estados.
    */
    private void mostrarDetalle(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {
        // Variable entera que almacena el ID de la lista a consultar
        int id = parseInt(req.getParameter("id"));
        if (id <= 0) { resp.sendRedirect(req.getContextPath() + "/Listas"); return; }
        // Objeto ListaCompras con los datos de la lista; null si no existe o no pertenece al hogar
        ListaCompras lista = new ListaComprasDao().obtenerPorId(id, idHogar);
        if (lista == null) { resp.sendRedirect(req.getContextPath() + "/Listas?error=no_encontrada"); return; }
        req.setAttribute("lista", lista);
        req.setAttribute("detalles", new DetalleListaComprasDao().listarPorLista(id));
        forward(req, resp, "14_VerDetallesLista.jsp");
    }

    /* Método: mostrarConfirmarEliminar
       Propósito: Buscar la lista y mostrar la vista de confirmación antes de eliminarla.
    */
    private void mostrarConfirmarEliminar(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {
        // Variable entera que almacena el ID de la lista a eliminar
        int id = parseInt(req.getParameter("id"));
        ListaCompras lista = new ListaComprasDao().obtenerPorId(id, idHogar);
        if (lista == null) { resp.sendRedirect(req.getContextPath() + "/Listas"); return; }
        req.setAttribute("lista", lista);
        forward(req, resp, "06_EliminarLista.jsp");
    }

    /* Método: mostrarFormAgregarProducto
       Propósito: Cargar la lista y los tipos de producto disponibles para mostrar
       el formulario de agregar un producto a la lista.
    */
    private void mostrarFormAgregarProducto(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {
        // Variable entera que almacena el ID de la lista a la que se agregará el producto
        int idLista = parseInt(req.getParameter("id"));
        ListaCompras lista = new ListaComprasDao().obtenerPorId(idLista, idHogar);
        if (lista == null) { resp.sendRedirect(req.getContextPath() + "/Listas"); return; }
        req.setAttribute("lista", lista);
        // Asigna la lista de tipos de producto disponibles para el selector del formulario
        req.setAttribute("tiposProducto", new ProductoDao().listarTipos());
        forward(req, resp, "08_AgregarProductos.jsp");
    }

    /* Método: mostrarFormEditarProducto
       Propósito: Cargar el detalle del producto a editar y mostrar el formulario de edición.
    */
    private void mostrarFormEditarProducto(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Variable entera que almacena el ID del detalle (ítem de lista) a editar
        int idDetalle = parseInt(req.getParameter("idDetalle"));
        // Variable entera que almacena el ID de la lista a la que pertenece el detalle
        int idLista   = parseInt(req.getParameter("idLista"));
        // Objeto DetalleListaCompras con los datos del ítem; null si no existe
        DetalleListaCompras detalle = new DetalleListaComprasDao().obtenerPorId(idDetalle);
        if (detalle == null) { resp.sendRedirect(req.getContextPath() + "/Listas"); return; }
        req.setAttribute("detalle", detalle);
        req.setAttribute("idLista", idLista);
        req.setAttribute("tiposProducto", new ProductoDao().listarTipos());
        forward(req, resp, "10_EditarProducto.jsp");
    }

    /* Método: mostrarConfirmarEliminarProd
       Propósito: Cargar el detalle del producto y mostrar la confirmación antes de eliminarlo.
    */
    private void mostrarConfirmarEliminarProd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Variable entera que almacena el ID del detalle a eliminar
        int idDetalle = parseInt(req.getParameter("idDetalle"));
        // Variable entera que almacena el ID de la lista propietaria del detalle
        int idLista   = parseInt(req.getParameter("idLista"));
        DetalleListaCompras detalle = new DetalleListaComprasDao().obtenerPorId(idDetalle);
        if (detalle == null) { resp.sendRedirect(req.getContextPath() + "/Listas"); return; }
        req.setAttribute("detalle", detalle);
        req.setAttribute("idLista", idLista);
        forward(req, resp, "12_EliminarProducto.jsp");
    }

    // ─── Acciones POST ────────────────────────────────────────────────────────

    /* Método: crearLista
       Propósito: Validar el nombre de la nueva lista, verificar que no esté duplicado
       en el hogar, crear el objeto ListaCompras, persistirlo y mostrar la confirmación.
    */
    private void crearLista(HttpServletRequest req, HttpServletResponse resp,
                            Usuario usuario, int idHogar, int idRol)
            throws ServletException, IOException {

        // Solo roles 1 y 2 pueden crear listas
        if (!puedeGestionar(idRol)) {
            resp.sendRedirect(req.getContextPath() + "/Listas?error=sin_permiso"); return;
        }

        // Variable de tipo texto que almacena el nombre ingresado para la nueva lista
        String nombre = nvl(req.getParameter("nombreLista")).trim();
        // Variable de tipo texto con el mensaje de error de validación, o null si es válido
        String err = validarNombreLista(nombre);
        if (err != null) {
            req.setAttribute("error", err);
            req.setAttribute("valorNombre", nombre);
            forward(req, resp, "02_CrearLista.jsp"); return;
        }

        // Instancia del DAO de listas para verificar duplicados y persistir
        ListaComprasDao dao = new ListaComprasDao();
        if (dao.existeNombre(idHogar, nombre)) {
            // No puede haber dos listas con el mismo nombre en el mismo hogar
            req.setAttribute("error", "Ya existe una lista con ese nombre. Elige un nombre diferente.");
            req.setAttribute("valorNombre", nombre);
            forward(req, resp, "02_CrearLista.jsp"); return;
        }

        // Construye el objeto ListaCompras con el hogar y nombre
        ListaCompras lista = new ListaCompras();
        lista.setIdHogar(idHogar);
        lista.setNombreLista(nombre);

        // Variable entera con el ID generado para la nueva lista; 0 o negativo si falló
        int id = dao.insertar(lista);
        if (id > 0) {
            req.setAttribute("nombreLista", nombre);
            req.setAttribute("idLista", id);
            forward(req, resp, "03_ConfirmarCreacionLista.jsp");
        } else {
            req.setAttribute("error", "Error al crear la lista. Intenta nuevamente.");
            req.setAttribute("valorNombre", nombre);
            forward(req, resp, "02_CrearLista.jsp");
        }
    }

    /* Método: actualizarLista
       Propósito: Validar los datos editados de la lista y persistir los cambios en la BD.
    */
    private void actualizarLista(HttpServletRequest req, HttpServletResponse resp,
                                 int idHogar, int idRol)
            throws ServletException, IOException {
        if (!puedeGestionar(idRol)) {
            resp.sendRedirect(req.getContextPath() + "/Listas?error=sin_permiso"); return;
        }
        // Variable entera que almacena el ID de la lista a actualizar
        int id = parseInt(req.getParameter("idLista"));
        // Variable de tipo texto que almacena el nuevo nombre de la lista
        String nombre = nvl(req.getParameter("nombreLista")).trim();
        // Variable de tipo texto que almacena el nuevo estado de la lista
        String estado = req.getParameter("estadoLista");

        String err = validarNombreLista(nombre);
        if (id <= 0 || err != null) {
            resp.sendRedirect(req.getContextPath() + "/Listas?accion=editar&id=" + id +
                "&error=" + (err != null ? "nombre_invalido" : "campos_vacios"));
            return;
        }

        // Construye el objeto con los datos actualizados
        ListaCompras lista = new ListaCompras();
        lista.setIdListaCompras(id);
        lista.setIdHogar(idHogar);
        lista.setNombreLista(nombre);
        // Si el estado es null, se asigna "Pendiente" por defecto
        lista.setEstadoLista(estado != null ? estado : "Pendiente");

        if (new ListaComprasDao().actualizar(lista)) {
            req.setAttribute("nombreLista", nombre);
            forward(req, resp, "05_ConfirmacionEdicionLista.jsp");
        } else {
            resp.sendRedirect(req.getContextPath() + "/Listas?accion=editar&id=" + id + "&error=error_db");
        }
    }

    /* Método: eliminarLista
       Propósito: Eliminar la lista y todos sus detalles de la base de datos.
    */
    private void eliminarLista(HttpServletRequest req, HttpServletResponse resp,
                               int idHogar, int idRol) throws IOException {
        if (!puedeGestionar(idRol)) {
            resp.sendRedirect(req.getContextPath() + "/Listas?error=sin_permiso"); return;
        }
        // Variable entera que almacena el ID de la lista a eliminar
        int id = parseInt(req.getParameter("idLista"));
        // Elimina la lista y sus detalles de la base de datos
        new ListaComprasDao().eliminar(id, idHogar);
        resp.sendRedirect(req.getContextPath() + "/Listas?exito=lista_eliminada");
    }

    /* Método: guardarProducto
       Propósito: Validar, crear o actualizar (sumar cantidad) un producto en la lista.
    */
    private void guardarProducto(HttpServletRequest req, HttpServletResponse resp,
                                 int idHogar, int idRol)
            throws ServletException, IOException {

        // Variable entera que almacena el ID de la lista donde se agrega el producto
        int idLista       = parseInt(req.getParameter("idLista"));
        // Variable de tipo texto que almacena el nombre del producto a agregar
        String nombreProd = nvl(req.getParameter("nombreProducto")).trim();
        // Variable de tipo texto con la cantidad; se reemplaza la coma por punto para decimales
        String cantStr    = nvl(req.getParameter("cantidad")).replace(",", ".");
        // Variable entera que almacena el ID del tipo de producto seleccionado
        int idTipo        = parseInt(req.getParameter("idTipoProducto"));

        ListaCompras lista = new ListaComprasDao().obtenerPorId(idLista, idHogar);

        String errN = validarNombreProducto(nombreProd);
        if (errN != null) {
            req.setAttribute("error", errN);
            req.setAttribute("lista", lista);
            req.setAttribute("tiposProducto", new ProductoDao().listarTipos());
            forward(req, resp, "08_AgregarProductos.jsp"); return;
        }

        // Variable de tipo BigDecimal que almacena la cantidad del producto; ZERO si no se puede parsear
        BigDecimal cantidad;
        try { cantidad = new BigDecimal(cantStr); }
        catch (Exception e) { cantidad = BigDecimal.ZERO; }

        if (cantidad.compareTo(BigDecimal.ZERO) <= 0 || cantidad.compareTo(new BigDecimal("999")) > 0) {
            req.setAttribute("error", "La cantidad debe ser mayor a 0 y máximo 999.");
            req.setAttribute("lista", lista);
            req.setAttribute("tiposProducto", new ProductoDao().listarTipos());
            forward(req, resp, "08_AgregarProductos.jsp"); return;
        }

        // Si no se seleccionó tipo, se usa el tipo por defecto (5 = General)
        if (idTipo <= 0) idTipo = 5;

        // Obtiene o crea el producto en la tabla Producto y retorna su ID
        int idProducto = new ProductoDao().obtenerOCrearProducto(nombreProd, idTipo);
        if (idProducto <= 0) {
            req.setAttribute("error", "Error al registrar el producto.");
            req.setAttribute("lista", lista);
            req.setAttribute("tiposProducto", new ProductoDao().listarTipos());
            forward(req, resp, "08_AgregarProductos.jsp"); return;
        }

        // Agrega el producto a la lista (si ya existe suma la cantidad)
        // Variable entera: 1 si se creó nuevo, 2 si se sumó cantidad a uno existente
        int resultado = new DetalleListaComprasDao().agregarProducto(idLista, idProducto, cantidad);
        // Recalcula el estado de la lista (Pendiente, En progreso o Completada)
        new ListaComprasDao().recalcularEstado(idLista);

        if (resultado >= 1) {
            req.setAttribute("nombreProducto", nombreProd);
            req.setAttribute("cantidad", cantidad.toPlainString());
            req.setAttribute("idLista", idLista);
            // Variable booleana que indica si el producto ya existía y se sumó su cantidad
            req.setAttribute("fueActualizado", resultado == 2);
            forward(req, resp, "09_ConfirmarProducto.jsp");
        } else {
            req.setAttribute("error", "No se pudo agregar el producto.");
            req.setAttribute("lista", lista);
            req.setAttribute("tiposProducto", new ProductoDao().listarTipos());
            forward(req, resp, "08_AgregarProductos.jsp");
        }
    }

    /* Método: actualizarProducto
       Propósito: Actualizar la cantidad de un ítem de lista y recalcular el estado.
    */
    private void actualizarProducto(HttpServletRequest req, HttpServletResponse resp,
                                    int idHogar, int idRol)
            throws ServletException, IOException {
        // Variable entera que almacena el ID del detalle (ítem) a actualizar
        int idDetalle = parseInt(req.getParameter("idDetalle"));
        // Variable entera que almacena el ID de la lista propietaria del ítem
        int idLista   = parseInt(req.getParameter("idLista"));
        // Variable de tipo texto con la nueva cantidad; se reemplaza coma por punto
        String cantStr = nvl(req.getParameter("cantidad")).replace(",", ".");

        // Variable de tipo BigDecimal que almacena la nueva cantidad; ZERO si no se puede parsear
        BigDecimal cantidad;
        try { cantidad = new BigDecimal(cantStr); }
        catch (Exception e) { cantidad = BigDecimal.ZERO; }

        if (cantidad.compareTo(BigDecimal.ZERO) <= 0 || cantidad.compareTo(new BigDecimal("999")) > 0) {
            resp.sendRedirect(req.getContextPath() +
                "/Listas?accion=editarProducto&idDetalle=" + idDetalle +
                "&idLista=" + idLista + "&error=cantidad_invalida");
            return;
        }

        // Actualiza solo la cantidad del ítem en la base de datos
        new DetalleListaComprasDao().actualizarCantidad(idDetalle, cantidad);
        // Recalcula el estado de la lista después del cambio
        new ListaComprasDao().recalcularEstado(idLista);

        req.setAttribute("idLista", idLista);
        forward(req, resp, "11_ConfirmacionProducto.jsp");
    }

    /* Método: eliminarProducto
       Propósito: Eliminar un ítem de la lista y recalcular el estado de la lista.
    */
    private void eliminarProducto(HttpServletRequest req, HttpServletResponse resp,
                                  int idHogar, int idRol) throws IOException {
        // Variable entera que almacena el ID del detalle a eliminar
        int idDetalle = parseInt(req.getParameter("idDetalle"));
        // Variable entera que almacena el ID de la lista propietaria
        int idLista   = parseInt(req.getParameter("idLista"));
        // Elimina el ítem del detalle de la lista
        new DetalleListaComprasDao().eliminar(idDetalle);
        // Recalcula el estado de la lista después de eliminar el ítem
        new ListaComprasDao().recalcularEstado(idLista);
        resp.sendRedirect(req.getContextPath() + "/Listas?accion=verDetalle&id=" + idLista + "&exito=prod_eliminado");
    }

    /* Método: toggleComprado
       Propósito: Alternar el estado comprado/pendiente de un ítem individual de la lista.
       Si el nuevo estado es "comprado", agrega automáticamente el producto al inventario
       del hogar con la cantidad registrada en la lista (si ya existía, suma el stock).
    */
    private void toggleComprado(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws IOException {
        // Variable entera que almacena el ID del detalle a actualizar
        int idDetalle = parseInt(req.getParameter("idDetalle"));
        // Variable entera que almacena el ID de la lista propietaria
        int idLista   = parseInt(req.getParameter("idLista"));
        // Variable booleana que almacena el nuevo estado: true = comprado, false = pendiente
        boolean comprado = "true".equals(req.getParameter("comprado"));

        // Si se marca como comprado, el producto pasa automáticamente al inventario del hogar
        if (comprado) {
            // Variable entera con el ID del producto enviado directamente desde el formulario
            int idProducto = parseInt(req.getParameter("idProducto"));
            // Variable de tipo texto con la cantidad enviada desde el formulario; coma → punto para decimales
            String cantStr = nvl(req.getParameter("cantidadProd")).replace(",", ".");
            // Variable de tipo BigDecimal que almacena la cantidad parseada; 1 si no se puede convertir
            BigDecimal cantidad;
            try { cantidad = new BigDecimal(cantStr); }
            catch (Exception e) { cantidad = BigDecimal.ONE; }
            if (cantidad.compareTo(BigDecimal.ZERO) <= 0) cantidad = BigDecimal.ONE;
            if (idProducto > 0) {
                // Construye el objeto de inventario con el producto y la cantidad de la lista
                InventarioCasa inv = new InventarioCasa();
                inv.setIdHogar(idHogar);
                inv.setIdProducto(idProducto);
                inv.setCantidad(cantidad);
                // Registra en inventario: crea nuevo si no existe, suma stock si ya existe
                new InventarioCasaDao().registrar(inv);
            }
        }

        // Cambia el estado del ítem en la base de datos
        new DetalleListaComprasDao().toggleComprado(idDetalle, comprado);
        // Recalcula el estado de la lista (puede pasar a "Completada" si todos están comprados)
        new ListaComprasDao().recalcularEstado(idLista);
        resp.sendRedirect(req.getContextPath() + "/Listas?accion=verDetalle&id=" + idLista);
    }

    /* Método: marcarTodos
       Propósito: Marcar o desmarcar todos los ítems de la lista como comprados o pendientes.
       Si se marcan todos como comprados, agrega al inventario del hogar únicamente los
       ítems que aún no estaban marcados (para no duplicar stock). Usa redirect en lugar
       de forward para que la vista recargue los datos actualizados.
    */
    private void marcarTodos(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws IOException {
        // Variable entera que almacena el ID de la lista cuyos ítems se van a marcar
        int idLista = parseInt(req.getParameter("idLista"));
        // Variable booleana: true = marcar todos como comprados, false = desmarcar todos
        boolean todos = "true".equals(req.getParameter("comprado"));

        // Si se marcan todos como comprados, pasa al inventario solo los que aún no lo estaban
        if (todos) {
            // Obtiene todos los ítems actuales de la lista antes de cambiar su estado
            List<DetalleListaCompras> items = new DetalleListaComprasDao().listarPorLista(idLista);
            // Instancia del DAO de inventario reutilizable en el ciclo
            InventarioCasaDao invDao = new InventarioCasaDao();
            for (DetalleListaCompras item : items) {
                // Solo procesa los que NO estaban comprados, para no sumar doble el stock
                if (!item.isComprado()) {
                    // Construye el objeto de inventario con el producto y cantidad del ítem
                    InventarioCasa inv = new InventarioCasa();
                    inv.setIdHogar(idHogar);
                    inv.setIdProducto(item.getIdProducto());
                    inv.setCantidad(item.getCantidadDecimal());
                    // Registra en inventario: crea nuevo si no existe, suma stock si ya existe
                    invDao.registrar(inv);
                }
            }
        }

        // Actualiza todos los ítems de la lista al mismo tiempo en la base de datos
        new DetalleListaComprasDao().marcarTodos(idLista, todos);
        // Recalcula el estado general de la lista
        new ListaComprasDao().recalcularEstado(idLista);
        resp.sendRedirect(req.getContextPath() + "/Listas?accion=verDetalle&id=" + idLista +
            (todos ? "&info=todos_marcados" : "&info=todos_desmarcados"));
    }

    // ─── Validaciones ─────────────────────────────────────────────────────────

    /* Método: validarNombreLista
       Propósito: Verificar que el nombre de la lista cumpla las reglas: no vacío,
       entre 5 y 50 caracteres, y que solo contenga caracteres permitidos.
       @param nombre → Texto con el nombre a validar
       @return String → Mensaje de error en texto, o null si el nombre es válido
    */
    private String validarNombreLista(String nombre) {
        if (nombre == null || nombre.isEmpty()) return "El nombre de la lista es obligatorio.";
        if (nombre.length() < 5) return "El nombre debe tener al menos 5 caracteres.";
        if (nombre.length() > 50) return "El nombre no puede superar 50 caracteres.";
        if (!P_NOMBRE_LISTA.matcher(nombre).matches())
            return "Solo se permiten letras, números, espacios, puntos, guiones y guiones bajos.";
        return null;
    }

    /* Método: validarNombreProducto
       Propósito: Verificar que el nombre del producto cumpla las reglas: no vacío,
       entre 5 y 50 caracteres, y que solo contenga caracteres permitidos.
       @param nombre → Texto con el nombre del producto a validar
       @return String → Mensaje de error en texto, o null si el nombre es válido
    */
    private String validarNombreProducto(String nombre) {
        if (nombre == null || nombre.isEmpty()) return "El nombre del producto es obligatorio.";
        if (nombre.length() < 5) return "El nombre del producto debe tener al menos 5 caracteres.";
        if (nombre.length() > 50) return "El nombre del producto no puede superar 50 caracteres.";
        if (!P_NOMBRE_PRODUCTO.matcher(nombre).matches())
            return "Solo se permiten letras, números, espacios y los símbolos: . , # -";
        return null;
    }

    // ─── Utilidades ───────────────────────────────────────────────────────────

    /* Método: sesionValida
       Propósito: Verificar que exista una sesión HTTP activa con usuario autenticado.
       @return boolean → true si la sesión es válida, false si redirigió al login
    */
    private boolean sesionValida(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() +
                "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_requerida");
            return false;
        }
        return true;
    }

    /* Método: puedeGestionar
       Propósito: Verificar si el rol del usuario le permite crear, editar o eliminar listas.
       Solo los roles 1 (Administrador) y 2 (Cotitular) pueden gestionar.
       @param idRol → Entero con el ID del rol del usuario
       @return boolean → true si puede gestionar, false si es solo lectura
    */
    private boolean puedeGestionar(int idRol) { return idRol == 1 || idRol == 2; }

    /* Método: forward
       Propósito: Hacer forward a una vista JSP del módulo de listas de compras.
       @param vista → Nombre del archivo JSP dentro de la carpeta BASE
    */
    private void forward(HttpServletRequest req, HttpServletResponse resp, String vista)
            throws ServletException, IOException {
        req.getRequestDispatcher(BASE + vista).forward(req, resp);
    }

    /* Método: idHogar
       Propósito: Obtener el ID del hogar del usuario desde la sesión activa.
       @return int → Entero con el ID del hogar
    */
    private int idHogar(HttpServletRequest req) { return (Integer) req.getSession().getAttribute("idHogar"); }

    /* Método: idRol
       Propósito: Obtener el ID del rol del usuario desde la sesión activa.
       @return int → Entero con el ID del rol
    */
    private int idRol(HttpServletRequest req)   { return (Integer) req.getSession().getAttribute("idRol"); }

    /* Método: parseInt
       Propósito: Convertir texto a entero de forma segura, retornando 0 si falla.
       @param s → Texto a convertir
       @return int → Entero resultante, o 0 si la conversión falla
    */
    private int parseInt(String s) { try { return Integer.parseInt(s); } catch (Exception e) { return 0; } }

    /* Método: nvl
       Propósito: Retornar la cadena recibida o una cadena vacía si es null.
       @param s → Texto a evaluar
       @return String → El mismo texto, o "" si era null
    */
    private String nvl(String s)   { return s == null ? "" : s; }
}
