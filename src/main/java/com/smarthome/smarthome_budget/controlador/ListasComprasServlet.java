package com.smarthome.smarthome_budget.controlador;

import com.smarthome.smarthome_budget.dao.*;
import com.smarthome.smarthome_budget.modelo.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

/**
 * Servlet central del módulo Listas de Compras.
 *
 * GET  /Listas                              → dashboard (01_listasCompras.jsp)
 * GET  /Listas?accion=crear                 → formulario nueva lista
 * GET  /Listas?accion=editar&id=X           → editar lista + sus productos
 * GET  /Listas?accion=verDetalle&id=X       → ver detalle con checkboxes
 * GET  /Listas?accion=confirmarEliminar&id=X
 * GET  /Listas?accion=agregarProducto&id=X  → formulario agregar producto
 * GET  /Listas?accion=editarProducto&idDetalle=X&idLista=X
 * GET  /Listas?accion=confirmarEliminarProd&idDetalle=X&idLista=X
 *
 * POST /Listas?accion=registrar             → crear lista
 * POST /Listas?accion=actualizar            → guardar cambios lista
 * POST /Listas?accion=eliminar              → eliminar lista
 * POST /Listas?accion=guardarProducto       → agregar producto a lista
 * POST /Listas?accion=actualizarProducto    → editar cantidad/nombre producto
 * POST /Listas?accion=eliminarProducto      → quitar producto de lista
 * POST /Listas?accion=toggleComprado        → marcar/desmarcar un producto
 * POST /Listas?accion=marcarTodos           → marcar/desmarcar todos
 */
@WebServlet("/Listas")
public class ListasComprasServlet extends HttpServlet {

    private static final String BASE = "/public/modules/03_ListasCompras/";

    // ─── GET ─────────────────────────────────────────────────────────────────

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!sesionValida(req, resp)) return;

        int idHogar = idHogar(req);
        String accion = req.getParameter("accion");
        if (accion == null) accion = "dashboard";

        switch (accion) {
            case "dashboard":   mostrarDashboard(req, resp, idHogar);              break;
            case "crear":       mostrarFormCrear(req, resp);                        break;
            case "editar":      mostrarFormEditar(req, resp, idHogar);             break;
            case "verDetalle":  mostrarDetalle(req, resp, idHogar);                break;
            case "confirmarEliminar": mostrarConfirmarEliminar(req, resp, idHogar); break;
            case "agregarProducto":   mostrarFormAgregarProducto(req, resp, idHogar); break;
            case "editarProducto":    mostrarFormEditarProducto(req, resp);         break;
            case "confirmarEliminarProd": mostrarConfirmarEliminarProd(req, resp); break;
            default: mostrarDashboard(req, resp, idHogar);
        }
    }

    // ─── POST ────────────────────────────────────────────────────────────────

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        if (!sesionValida(req, resp)) return;

        int idHogar = idHogar(req);
        int idRol   = idRol(req);
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");
        String accion = nvl(req.getParameter("accion"));

        switch (accion) {
            case "registrar":         crearLista(req, resp, usuario, idHogar, idRol);        break;
            case "actualizar":        actualizarLista(req, resp, idHogar, idRol);            break;
            case "eliminar":          eliminarLista(req, resp, idHogar, idRol);              break;
            case "guardarProducto":   guardarProducto(req, resp, idHogar, idRol);            break;
            case "actualizarProducto":actualizarProducto(req, resp, idHogar, idRol);        break;
            case "eliminarProducto":  eliminarProducto(req, resp, idHogar, idRol);          break;
            case "toggleComprado":    toggleComprado(req, resp, idHogar);                   break;
            case "marcarTodos":       marcarTodos(req, resp, idHogar);                      break;
            default: resp.sendRedirect(req.getContextPath() + "/Listas");
        }
    }

    // ─── Vistas GET ───────────────────────────────────────────────────────────

    private void mostrarDashboard(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {
        List<ListaCompras> listas = new ListaComprasDao().listarPorHogar(idHogar);
        req.setAttribute("listas", listas);
        forward(req, resp, "01_listasCompras.jsp");
    }

    private void mostrarFormCrear(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        forward(req, resp, "02_CrearLista.jsp");
    }

    private void mostrarFormEditar(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {
        int id = parseInt(req.getParameter("id"));
        if (id <= 0) { resp.sendRedirect(req.getContextPath() + "/Listas"); return; }

        ListaCompras lista = new ListaComprasDao().obtenerPorId(id, idHogar);
        if (lista == null) { resp.sendRedirect(req.getContextPath() + "/Listas?error=no_encontrada"); return; }

        List<DetalleListaCompras> detalles = new DetalleListaComprasDao().listarPorLista(id);
        req.setAttribute("lista", lista);
        req.setAttribute("detalles", detalles);
        forward(req, resp, "04_EditarListaCompras.jsp");
    }

    private void mostrarDetalle(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {
        int id = parseInt(req.getParameter("id"));
        if (id <= 0) { resp.sendRedirect(req.getContextPath() + "/Listas"); return; }

        ListaCompras lista = new ListaComprasDao().obtenerPorId(id, idHogar);
        if (lista == null) { resp.sendRedirect(req.getContextPath() + "/Listas?error=no_encontrada"); return; }

        List<DetalleListaCompras> detalles = new DetalleListaComprasDao().listarPorLista(id);
        req.setAttribute("lista", lista);
        req.setAttribute("detalles", detalles);
        // Vista unificada para todos los tipos de lista
        forward(req, resp, "14_VerDetalleLista.jsp");
    }

    private void mostrarConfirmarEliminar(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {
        int id = parseInt(req.getParameter("id"));
        ListaCompras lista = new ListaComprasDao().obtenerPorId(id, idHogar);
        if (lista == null) { resp.sendRedirect(req.getContextPath() + "/Listas"); return; }
        req.setAttribute("lista", lista);
        forward(req, resp, "06_EliminarLista.jsp");
    }

    private void mostrarFormAgregarProducto(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {
        int idLista = parseInt(req.getParameter("id"));
        ListaCompras lista = new ListaComprasDao().obtenerPorId(idLista, idHogar);
        if (lista == null) { resp.sendRedirect(req.getContextPath() + "/Listas"); return; }
        req.setAttribute("lista", lista);
        req.setAttribute("tiposProducto", new ProductoDao().listarTipos());
        forward(req, resp, "08_AgregarProductos.jsp");
    }

    private void mostrarFormEditarProducto(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int idDetalle = parseInt(req.getParameter("idDetalle"));
        int idLista   = parseInt(req.getParameter("idLista"));
        DetalleListaCompras detalle = new DetalleListaComprasDao().obtenerPorId(idDetalle);
        if (detalle == null) { resp.sendRedirect(req.getContextPath() + "/Listas"); return; }
        req.setAttribute("detalle", detalle);
        req.setAttribute("idLista", idLista);
        req.setAttribute("tiposProducto", new ProductoDao().listarTipos());
        forward(req, resp, "10_EditarProducto.jsp");
    }

    private void mostrarConfirmarEliminarProd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int idDetalle = parseInt(req.getParameter("idDetalle"));
        int idLista   = parseInt(req.getParameter("idLista"));
        DetalleListaCompras detalle = new DetalleListaComprasDao().obtenerPorId(idDetalle);
        if (detalle == null) { resp.sendRedirect(req.getContextPath() + "/Listas"); return; }
        req.setAttribute("detalle", detalle);
        req.setAttribute("idLista", idLista);
        forward(req, resp, "12_EliminarProducto.jsp");
    }

    // ─── Acciones POST ────────────────────────────────────────────────────────

    private void crearLista(HttpServletRequest req, HttpServletResponse resp,
                            Usuario usuario, int idHogar, int idRol)
            throws ServletException, IOException {

        if (!puedeGestionar(idRol)) {
            resp.sendRedirect(req.getContextPath() + "/Listas?error=sin_permiso"); return;
        }

        String nombre = req.getParameter("nombreLista");
        if (vacio(nombre)) {
            req.setAttribute("error", "El nombre de la lista es obligatorio.");
            forward(req, resp, "02_CrearLista.jsp"); return;
        }

        ListaCompras lista = new ListaCompras();
        lista.setIdHogar(idHogar);
        lista.setIdUsuario(usuario.getIDUsuario());
        lista.setNombreLista(nombre.trim());

        int id = new ListaComprasDao().insertar(lista);
        if (id > 0) {
            // Mostrar confirmación pasando los datos
            req.setAttribute("nombreLista", nombre.trim());
            forward(req, resp, "03_ConfirmarCreacionLista.jsp");
        } else {
            req.setAttribute("error", "Error al crear la lista. Intenta nuevamente.");
            forward(req, resp, "02_CrearLista.jsp");
        }
    }

    private void actualizarLista(HttpServletRequest req, HttpServletResponse resp,
                                 int idHogar, int idRol)
            throws ServletException, IOException {

        if (!puedeGestionar(idRol)) {
            resp.sendRedirect(req.getContextPath() + "/Listas?error=sin_permiso"); return;
        }

        int id = parseInt(req.getParameter("idLista"));
        String nombre = req.getParameter("nombreLista");
        String estado = req.getParameter("estadoLista");

        if (id <= 0 || vacio(nombre)) {
            resp.sendRedirect(req.getContextPath() + "/Listas?accion=editar&id=" + id + "&error=campos_vacios");
            return;
        }

        ListaCompras lista = new ListaCompras();
        lista.setIdListaCompras(id);
        lista.setIdHogar(idHogar);
        lista.setNombreLista(nombre.trim());
        lista.setEstadoLista(estado != null ? estado : "Pendiente");

        boolean ok = new ListaComprasDao().actualizar(lista);
        if (ok) {
            req.setAttribute("nombreLista", nombre.trim());
            forward(req, resp, "05_ConfirmacionEdicionLista.jsp");
        } else {
            resp.sendRedirect(req.getContextPath() + "/Listas?accion=editar&id=" + id + "&error=error_db");
        }
    }

    private void eliminarLista(HttpServletRequest req, HttpServletResponse resp,
                               int idHogar, int idRol) throws IOException {
        if (!puedeGestionar(idRol)) {
            resp.sendRedirect(req.getContextPath() + "/Listas?error=sin_permiso"); return;
        }
        int id = parseInt(req.getParameter("idLista"));
        new ListaComprasDao().eliminar(id, idHogar);
        resp.sendRedirect(req.getContextPath() + "/Listas?exito=lista_eliminada");
    }

    private void guardarProducto(HttpServletRequest req, HttpServletResponse resp,
                                 int idHogar, int idRol)
            throws ServletException, IOException {

        // RF: ADMINISTRADOR, COTITULAR e INVITADO pueden agregar productos a listas
        // (todos los roles tienen permiso ver_y_agregar_compras o gestionar_lista_compras)

        int idLista        = parseInt(req.getParameter("idLista"));
        String nombreProd  = req.getParameter("nombreProducto");
        int cantidad       = parseInt(req.getParameter("cantidad"));
        int idTipo         = parseInt(req.getParameter("idTipoProducto"));

        if (vacio(nombreProd) || cantidad <= 0) {
            req.setAttribute("error", "Nombre y cantidad (>0) son obligatorios.");
            req.setAttribute("idLista", idLista);
            req.setAttribute("tiposProducto", new ProductoDao().listarTipos());
            // Recargar la lista para mantener el contexto
            ListaCompras lista = new ListaComprasDao().obtenerPorId(idLista, idHogar);
            req.setAttribute("lista", lista);
            forward(req, resp, "08_AgregarProductos.jsp"); return;
        }

        if (idTipo <= 0) idTipo = 5; // default "Otros"

        // Obtener o crear el producto
        int idProducto = new ProductoDao().obtenerOCrearProducto(nombreProd, idTipo);
        if (idProducto <= 0) {
            req.setAttribute("error", "Error al registrar el producto.");
            forward(req, resp, "08_AgregarProductos.jsp"); return;
        }

        DetalleListaComprasDao detalleDao = new DetalleListaComprasDao();
        int resultado = detalleDao.agregarProducto(idLista, idProducto, cantidad);

        // Recalcular estado de la lista
        new ListaComprasDao().recalcularEstado(idLista);

        if (resultado >= 1) {
            // Mostrar confirmación
            req.setAttribute("nombreProducto", nombreProd.trim());
            req.setAttribute("cantidad", cantidad);
            req.setAttribute("idLista", idLista);
            req.setAttribute("fueActualizado", resultado == 2);
            forward(req, resp, "09_ConfirmarProducto.jsp");
        } else {
            req.setAttribute("error", "No se pudo agregar el producto.");
            forward(req, resp, "08_AgregarProductos.jsp");
        }
    }

    private void actualizarProducto(HttpServletRequest req, HttpServletResponse resp,
                                    int idHogar, int idRol)
            throws ServletException, IOException {

        int idDetalle = parseInt(req.getParameter("idDetalle"));
        int idLista   = parseInt(req.getParameter("idLista"));
        int cantidad  = parseInt(req.getParameter("cantidad"));

        if (cantidad <= 0) {
            resp.sendRedirect(req.getContextPath() +
                "/Listas?accion=editarProducto&idDetalle=" + idDetalle + "&idLista=" + idLista + "&error=cantidad_invalida");
            return;
        }

        new DetalleListaComprasDao().actualizarCantidad(idDetalle, cantidad);
        new ListaComprasDao().recalcularEstado(idLista);

        req.setAttribute("idLista", idLista);
        forward(req, resp, "11_ConfirmacionProducto.jsp");
    }

    private void eliminarProducto(HttpServletRequest req, HttpServletResponse resp,
                                  int idHogar, int idRol) throws IOException {
        int idDetalle = parseInt(req.getParameter("idDetalle"));
        int idLista   = parseInt(req.getParameter("idLista"));
        new DetalleListaComprasDao().eliminar(idDetalle);
        new ListaComprasDao().recalcularEstado(idLista);
        resp.sendRedirect(req.getContextPath() + "/Listas?accion=verDetalle&id=" + idLista + "&exito=prod_eliminado");
    }

    private void toggleComprado(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws IOException {
        int idDetalle = parseInt(req.getParameter("idDetalle"));
        int idLista   = parseInt(req.getParameter("idLista"));
        boolean comprado = "true".equals(req.getParameter("comprado"));
        new DetalleListaComprasDao().toggleComprado(idDetalle, comprado);
        new ListaComprasDao().recalcularEstado(idLista);
        resp.sendRedirect(req.getContextPath() + "/Listas?accion=verDetalle&id=" + idLista);
    }

    private void marcarTodos(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {
        int idLista   = parseInt(req.getParameter("idLista"));
        boolean todos = "true".equals(req.getParameter("comprado"));
        new DetalleListaComprasDao().marcarTodos(idLista, todos);
        new ListaComprasDao().recalcularEstado(idLista);

        req.setAttribute("todosComprados", todos);
        req.setAttribute("idLista", idLista);
        if (todos) {
            forward(req, resp, "17_ProductosComprado-Todos.jsp");
        } else {
            forward(req, resp, "18_ProductosPendientes-Todos.jsp");
        }
    }

    // ─── Utilidades ──────────────────────────────────────────────────────────

    private boolean sesionValida(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() +
                "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_requerida");
            return false;
        }
        return true;
    }

    /** ADMINISTRADOR y COTITULAR gestionan. INVITADO solo agrega productos. */
    private boolean puedeGestionar(int idRol) { return idRol == 1 || idRol == 2; }

    private void forward(HttpServletRequest req, HttpServletResponse resp, String vista)
            throws ServletException, IOException {
        req.getRequestDispatcher(BASE + vista).forward(req, resp);
    }

    private int idHogar(HttpServletRequest req) {
        return (Integer) req.getSession().getAttribute("idHogar");
    }
    private int idRol(HttpServletRequest req) {
        return (Integer) req.getSession().getAttribute("idRol");
    }
    private int parseInt(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }
    private boolean vacio(String s) { return s == null || s.trim().isEmpty(); }
    private String nvl(String s) { return s == null ? "" : s; }
}
