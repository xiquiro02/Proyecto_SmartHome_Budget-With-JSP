package com.smarthome.smarthome_budget.controlador;

import com.smarthome.smarthome_budget.dao.*;
import com.smarthome.smarthome_budget.modelo.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

/**
 * Servlet central del módulo Mi Inventario.
 *
 * GET  /Inventario                              → dashboard (01_MiInventario.jsp)
 * GET  /Inventario?accion=registrar             → formulario nuevo producto
 * GET  /Inventario?accion=consultar             → lista completa (09)
 * GET  /Inventario?accion=filtroTipo&tipo=X     → filtro por categoría (10)
 * GET  /Inventario?accion=filtroCantidad&orden=X→ filtro por cantidad (11)
 * GET  /Inventario?accion=editar&id=X           → formulario editar (04)
 * GET  /Inventario?accion=confirmarEliminar&id=X→ confirmar eliminar (06)
 * GET  /Inventario?accion=agotados              → productos agotados (12)
 * GET  /Inventario?accion=moverALista&id=X      → seleccionar lista destino (13)
 *
 * POST /Inventario?accion=guardar               → registrar producto
 * POST /Inventario?accion=actualizar            → guardar cambios
 * POST /Inventario?accion=eliminar              → eliminar producto
 * POST /Inventario?accion=mover                 → mover agotado a lista de compras
 */
@WebServlet("/Inventario")
public class InventarioServlet extends HttpServlet {

    private static final String BASE = "/public/modules/04_ProductosDisponiblesCasa/";

    // ─── GET ─────────────────────────────────────────────────────────────────

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!sesionValida(req, resp)) return;
        // ROL 3 (INVITADO) solo puede ver el inventario, no modificar
        int idHogar = idHogar(req);
        String accion = nvl(req.getParameter("accion"));

        switch (accion) {
            case "registrar":       mostrarFormRegistrar(req, resp);              break;
            case "consultar":       mostrarConsulta(req, resp, idHogar, null);    break;
            case "filtroTipo":      mostrarFiltroTipo(req, resp, idHogar);        break;
            case "filtroCantidad":  mostrarFiltroCantidad(req, resp, idHogar);    break;
            case "editar":          mostrarFormEditar(req, resp, idHogar);        break;
            case "confirmarEliminar": mostrarConfirmarEliminar(req, resp, idHogar); break;
            case "agotados":        mostrarAgotados(req, resp, idHogar);          break;
            case "moverALista":     mostrarMoverALista(req, resp, idHogar);       break;
            default:                mostrarDashboard(req, resp, idHogar);
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
        String accion = nvl(req.getParameter("accion"));

        // INVITADO no puede modificar inventario
        if (idRol == 3) {
            resp.sendRedirect(req.getContextPath() + "/Inventario?error=sin_permiso"); return;
        }

        switch (accion) {
            case "guardar":     guardarProducto(req, resp, idHogar);   break;
            case "actualizar":  actualizarProducto(req, resp, idHogar); break;
            case "eliminar":    eliminarProducto(req, resp, idHogar);  break;
            case "mover":       moverAgotadoALista(req, resp, idHogar); break;
            default: resp.sendRedirect(req.getContextPath() + "/Inventario");
        }
    }

    // ─── Vistas GET ───────────────────────────────────────────────────────────

    private void mostrarDashboard(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {
        int[] resumen = new InventarioCasaDao().obtenerResumen(idHogar);
        req.setAttribute("disponibles", resumen[0]);
        req.setAttribute("porAgotar",   resumen[1]);
        req.setAttribute("categorias",  resumen[2]);
        forward(req, resp, "01_MiInventario.jsp");
    }

    private void mostrarFormRegistrar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("tiposProducto", new ProductoDao().listarTipos());
        forward(req, resp, "02_RegistrarProductoDisponibles.jsp");
    }

    private void mostrarConsulta(HttpServletRequest req, HttpServletResponse resp,
                                 int idHogar, List<InventarioCasa> inventarioFiltrado)
            throws ServletException, IOException {
        List<InventarioCasa> inventario = inventarioFiltrado != null
            ? inventarioFiltrado
            : new InventarioCasaDao().listarPorHogar(idHogar);

        if (inventario.isEmpty()) {
            forward(req, resp, "08_ConsultarInventario-Vacio.jsp");
        } else {
            req.setAttribute("inventario", inventario);
            req.setAttribute("tiposProducto", new ProductoDao().listarTipos());
            forward(req, resp, "09_ConsultarInventario.jsp");
        }
    }

    private void mostrarFiltroTipo(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {
        String tipoStr = req.getParameter("tipo");
        List<InventarioCasa> inventario;
        if (tipoStr != null && !tipoStr.isEmpty()) {
            inventario = new InventarioCasaDao().listarPorTipo(idHogar, parseInt(tipoStr));
        } else {
            inventario = new InventarioCasaDao().listarPorHogar(idHogar);
        }
        req.setAttribute("inventario", inventario);
        req.setAttribute("tiposProducto", new ProductoDao().listarTipos());
        req.setAttribute("filtroActivo", "tipo");
        req.setAttribute("tipoSeleccionado", tipoStr);
        forward(req, resp, "10_FiltroCategorias-ConsultarInventario.jsp");
    }

    private void mostrarFiltroCantidad(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {
        String orden = nvl(req.getParameter("orden"));
        InventarioCasaDao dao = new InventarioCasaDao();
        List<InventarioCasa> inventario;
        switch (orden) {
            case "menorIgual2": inventario = dao.listarStockBajo(idHogar);             break;
            case "mayorIgual10":
                // filtrar >= 10
                inventario = dao.listarOrdenadoCantidadDesc(idHogar);
                inventario.removeIf(i -> i.getCantidad() < 10);
                break;
            case "asc":  inventario = dao.listarOrdenadoCantidadAsc(idHogar);          break;
            case "desc": inventario = dao.listarOrdenadoCantidadDesc(idHogar);         break;
            default:     inventario = dao.listarPorHogar(idHogar);
        }
        req.setAttribute("inventario", inventario);
        req.setAttribute("tiposProducto", new ProductoDao().listarTipos());
        req.setAttribute("filtroActivo", "cantidad");
        req.setAttribute("ordenSeleccionado", orden);
        forward(req, resp, "11_FiltroCantidad-ConsultarInventario.jsp");
    }

    private void mostrarFormEditar(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {
        int id = parseInt(req.getParameter("id"));
        InventarioCasa inv = new InventarioCasaDao().obtenerPorId(id, idHogar);
        if (inv == null) { resp.sendRedirect(req.getContextPath() + "/Inventario?accion=consultar"); return; }
        req.setAttribute("inv", inv);
        req.setAttribute("tiposProducto", new ProductoDao().listarTipos());
        forward(req, resp, "04_EditarProductoCasa.jsp");
    }

    private void mostrarConfirmarEliminar(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {
        int id = parseInt(req.getParameter("id"));
        InventarioCasa inv = new InventarioCasaDao().obtenerPorId(id, idHogar);
        if (inv == null) { resp.sendRedirect(req.getContextPath() + "/Inventario?accion=consultar"); return; }
        req.setAttribute("inv", inv);
        forward(req, resp, "06_EliminarProducto-Casa.jsp");
    }

    private void mostrarAgotados(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {
        List<InventarioCasa> agotados = new InventarioCasaDao().listarAgotados(idHogar);
        req.setAttribute("agotados", agotados);
        forward(req, resp, "12_MoverProductosAgotados.jsp");
    }

    private void mostrarMoverALista(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {
        int id = parseInt(req.getParameter("id"));
        InventarioCasa inv = new InventarioCasaDao().obtenerPorId(id, idHogar);
        if (inv == null) { resp.sendRedirect(req.getContextPath() + "/Inventario?accion=agotados"); return; }
        List<ListaCompras> listas = new ListaComprasDao().listarPorHogar(idHogar);
        req.setAttribute("inv", inv);
        req.setAttribute("listas", listas);
        forward(req, resp, "13_MoverA-Lista.jsp");
    }

    // ─── Acciones POST ────────────────────────────────────────────────────────

    private void guardarProducto(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {

        String nombre     = req.getParameter("nombreProducto");
        int cantidad      = parseInt(req.getParameter("cantidad"));
        int idTipo        = parseInt(req.getParameter("idTipoProducto"));
        String descripcion = nvl(req.getParameter("descripcion"));

        if (vacio(nombre) || cantidad <= 0) {
            req.setAttribute("error", "Nombre y cantidad (> 0) son obligatorios.");
            req.setAttribute("tiposProducto", new ProductoDao().listarTipos());
            forward(req, resp, "02_RegistrarProductoDisponibles.jsp"); return;
        }
        if (idTipo <= 0) idTipo = 5; // Otros

        // Actualizar descripción si se ingresó
        int idProducto = new ProductoDao().obtenerOCrearProducto(nombre, idTipo);
        if (!descripcion.isBlank()) {
            try (java.sql.Connection con = com.smarthome.smarthome_budget.basedatos.claseConexion.MetodoConectar();
                 java.sql.PreparedStatement ps = con.prepareStatement(
                     "UPDATE Producto SET Descripcion=? WHERE IDProducto=?")) {
                ps.setString(1, descripcion.trim());
                ps.setInt(2, idProducto);
                ps.executeUpdate();
            } catch (java.sql.SQLException e) { /* ignorar */ }
        }

        InventarioCasa inv = new InventarioCasa();
        inv.setIdHogar(idHogar);
        inv.setIdProducto(idProducto);
        inv.setCantidad(cantidad);

        int res = new InventarioCasaDao().registrar(inv);
        if (res >= 1) {
            req.setAttribute("nombreProducto", nombre.trim());
            req.setAttribute("cantidad", cantidad);
            req.setAttribute("fueActualizado", res == 2);
            forward(req, resp, "03_ConfirmarRegistroProductosDisponibles.jsp");
        } else {
            req.setAttribute("error", "Error al registrar el producto.");
            req.setAttribute("tiposProducto", new ProductoDao().listarTipos());
            forward(req, resp, "02_RegistrarProductoDisponibles.jsp");
        }
    }

    private void actualizarProducto(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {

        int id       = parseInt(req.getParameter("idInventario"));
        int cantidad = parseInt(req.getParameter("cantidad"));

        if (cantidad < 0) {
            resp.sendRedirect(req.getContextPath() + "/Inventario?accion=editar&id=" + id + "&error=cantidad_invalida");
            return;
        }

        InventarioCasa inv = new InventarioCasa();
        inv.setIdInventario(id);
        inv.setIdHogar(idHogar);
        inv.setCantidad(cantidad);

        boolean ok = new InventarioCasaDao().actualizar(inv);
        if (ok) {
            InventarioCasa updated = new InventarioCasaDao().obtenerPorId(id, idHogar);
            req.setAttribute("nombreProducto", updated != null ? updated.getNombreProducto() : "");
            forward(req, resp, "05_CambiosGuardados.jsp");
        } else {
            resp.sendRedirect(req.getContextPath() + "/Inventario?accion=editar&id=" + id + "&error=db");
        }
    }

    private void eliminarProducto(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {

        int id = parseInt(req.getParameter("idInventario"));
        // Recuperar nombre antes de eliminar
        InventarioCasa inv = new InventarioCasaDao().obtenerPorId(id, idHogar);
        String nombre = inv != null ? inv.getNombreProducto() : "";

        new InventarioCasaDao().eliminar(id, idHogar);
        req.setAttribute("nombreProducto", nombre);
        forward(req, resp, "07_ProductoEliminado.jsp");
    }

    private void moverAgotadoALista(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {

        int idInventario = parseInt(req.getParameter("idInventario"));
        int idLista      = parseInt(req.getParameter("idLista"));
        int cantidadMover = parseInt(req.getParameter("cantidad"));
        if (cantidadMover <= 0) cantidadMover = 1;

        InventarioCasa inv = new InventarioCasaDao().obtenerPorId(idInventario, idHogar);
        if (inv == null || idLista <= 0) {
            resp.sendRedirect(req.getContextPath() + "/Inventario?accion=agotados&error=datos_invalidos");
            return;
        }

        // Agregar a la lista de compras seleccionada
        DetalleListaComprasDao detalleDao = new DetalleListaComprasDao();
        detalleDao.agregarProducto(idLista, inv.getIdProducto(), cantidadMover);
        new ListaComprasDao().recalcularEstado(idLista);

        // Obtener nombre de la lista para confirmación
        ListaCompras lista = new ListaComprasDao().obtenerPorId(idLista, idHogar);
        req.setAttribute("nombreProducto", inv.getNombreProducto());
        req.setAttribute("nombreLista", lista != null ? lista.getNombreLista() : "");
        forward(req, resp, "14_ConfirmacionMovimiento.jsp");
    }

    // ─── Utilidades ──────────────────────────────────────────────────────────

    private boolean sesionValida(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() +
                "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_requerida");
            return false;
        }
        return true;
    }

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
