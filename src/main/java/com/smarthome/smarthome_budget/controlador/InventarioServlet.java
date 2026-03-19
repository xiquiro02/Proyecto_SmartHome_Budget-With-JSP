package com.smarthome.smarthome_budget.controlador;

import com.smarthome.smarthome_budget.dao.*;
import com.smarthome.smarthome_budget.modelo.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Servlet central del módulo Mi Inventario.
 *
 * GET  /Inventario                               → dashboard (01_MiInventario.jsp)
 * GET  /Inventario?accion=registrar              → formulario nuevo producto
 * GET  /Inventario?accion=consultar              → lista completa (09)
 * GET  /Inventario?accion=filtroTipo&tipo=X      → filtro por categoría (10)
 * GET  /Inventario?accion=filtroCantidad&orden=X → filtro por cantidad (11)
 * GET  /Inventario?accion=editar&id=X            → formulario editar (04)
 * GET  /Inventario?accion=confirmarEliminar&id=X → confirmar eliminar (06)
 * GET  /Inventario?accion=autoAnadirALista&idProducto=X → añade a lista automáticamente
 *
 * POST /Inventario?accion=guardar     → registrar producto
 * POST /Inventario?accion=actualizar  → guardar cambios
 * POST /Inventario?accion=eliminar    → eliminar producto
 *
 * NOTA: Las vistas 12_MoverProductosAgotados y 13_MoverA-Lista han sido eliminadas
 * según los nuevos requerimientos. Se usa autoAnadirALista desde la tarjeta de inventario.
 */
@WebServlet("/Inventario")
public class InventarioServlet extends HttpServlet {

    private static final String BASE = "/public/modules/04_ProductosDisponiblesCasa/";

    // Mismas reglas que en ListasComprasServlet
    private static final Pattern P_NOMBRE_PRODUCTO = Pattern.compile("^[\\p{L}\\p{N} .,#\\-]+$");

    // ─── GET ──────────────────────────────────────────────────────────────────

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!sesionValida(req, resp)) return;
        int idHogar = idHogar(req);
        String accion = nvl(req.getParameter("accion"));

        switch (accion) {
            case "registrar":           mostrarFormRegistrar(req, resp);                    break;
            case "consultar":           mostrarConsulta(req, resp, idHogar, null);          break;
            case "filtroTipo":          mostrarFiltroTipo(req, resp, idHogar);              break;
            case "filtroCantidad":      mostrarFiltroCantidad(req, resp, idHogar);          break;
            case "editar":              mostrarFormEditar(req, resp, idHogar);              break;
            case "confirmarEliminar":   mostrarConfirmarEliminar(req, resp, idHogar);       break;
            case "autoAnadirALista":    autoAnadirALista(req, resp, idHogar);               break;
            default:                    mostrarDashboard(req, resp, idHogar);
        }
    }

    // ─── POST ─────────────────────────────────────────────────────────────────

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        if (!sesionValida(req, resp)) return;

        int idHogar = idHogar(req);
        int idRol   = idRol(req);
        String accion = nvl(req.getParameter("accion"));

        if (idRol == 3) {
            resp.sendRedirect(req.getContextPath() + "/Inventario?error=sin_permiso"); return;
        }

        switch (accion) {
            case "guardar":     guardarProducto(req, resp, idHogar);    break;
            case "actualizar":  actualizarProducto(req, resp, idHogar); break;
            case "eliminar":    eliminarProducto(req, resp, idHogar);   break;
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
            case "menorIgual2": inventario = dao.listarStockBajo(idHogar); break;
            case "mayorIgual10":
                inventario = dao.listarOrdenadoCantidadDesc(idHogar);
                BigDecimal diez = new BigDecimal("10");
                inventario.removeIf(i -> i.getCantidad().compareTo(diez) < 0);
                break;
            case "asc":  inventario = dao.listarOrdenadoCantidadAsc(idHogar);  break;
            case "desc": inventario = dao.listarOrdenadoCantidadDesc(idHogar); break;
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

    /**
     * NUEVA ACCIÓN: autoAnadirALista
     * Pasos:
     *   A) Obtiene la categoría del producto (nombreTipoProducto)
     *   B) Busca lista activa con nombre "Lista de [Categoría]"
     *   C) Si existe → agrega producto (si ya existe en la lista, suma +1)
     *      Si no existe → crea la lista y agrega el producto
     *   D) Redirige al inventario con mensaje SweetAlert via parámetro
     */
    private void autoAnadirALista(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws IOException {
        int idInventario = parseInt(req.getParameter("idProducto")); // se llama idProducto en la URL
        InventarioCasa inv = new InventarioCasaDao().obtenerPorId(idInventario, idHogar);

        if (inv == null) {
            resp.sendRedirect(req.getContextPath() + "/Inventario?accion=consultar&error=no_encontrado");
            return;
        }

        // Paso A: nombre de la lista
        String categoria   = inv.getNombreTipoProducto() != null ? inv.getNombreTipoProducto() : "General";
        String nombreLista = "Lista de " + categoria;

        ListaComprasDao listaDao     = new ListaComprasDao();
        DetalleListaComprasDao detDao = new DetalleListaComprasDao();

        // Paso B: buscar lista activa
        ListaCompras lista = listaDao.buscarActivaPorNombre(idHogar, nombreLista);

        // Paso C: si no existe, crearla
        if (lista == null) {
            ListaCompras nueva = new ListaCompras();
            nueva.setIdHogar(idHogar);
            nueva.setNombreLista(nombreLista);
            int idNueva = listaDao.insertar(nueva);
            if (idNueva <= 0) {
                resp.sendRedirect(req.getContextPath() + "/Inventario?accion=consultar&error=error_lista");
                return;
            }
            lista = listaDao.obtenerPorId(idNueva, idHogar);
        }

        // Agregar producto (+1 cantidad — si ya existe suma, si no crea)
        detDao.agregarProducto(lista.getIdListaCompras(), inv.getIdProducto(), 1);
        listaDao.recalcularEstado(lista.getIdListaCompras());

        // Paso D: redirigir con mensaje
        String msg = java.net.URLEncoder.encode(
            "Producto añadido a tu " + nombreLista + " automáticamente", "UTF-8");
        resp.sendRedirect(req.getContextPath() +
            "/Inventario?accion=consultar&exito_auto=" + msg);
    }

    // ─── Acciones POST ────────────────────────────────────────────────────────

    private void guardarProducto(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {

        String nombre      = nvl(req.getParameter("nombreProducto")).trim();
        BigDecimal cantidad = parseBD(req.getParameter("cantidad"));
        int idTipo         = parseInt(req.getParameter("idTipoProducto"));
        String descripcion = nvl(req.getParameter("descripcion"));

        // ── Validar nombre ────────────────────────────────────────────────
        String errN = validarNombreProducto(nombre);
        if (errN != null) {
            req.setAttribute("error", errN);
            req.setAttribute("tiposProducto", new ProductoDao().listarTipos());
            forward(req, resp, "02_RegistrarProductoDisponibles.jsp"); return;
        }

        // ── Validar cantidad decimal ──────────────────────────────────────
        if (cantidad.compareTo(BigDecimal.ZERO) <= 0 || cantidad.compareTo(new BigDecimal("999")) > 0) {
            req.setAttribute("error", "La cantidad debe ser mayor a 0 y máximo 999.");
            req.setAttribute("tiposProducto", new ProductoDao().listarTipos());
            forward(req, resp, "02_RegistrarProductoDisponibles.jsp"); return;
        }

        if (idTipo <= 0) idTipo = 5;

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
            req.setAttribute("nombreProducto", nombre);
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

        int id = parseInt(req.getParameter("idInventario"));
        BigDecimal cantidad = parseBD(req.getParameter("cantidad"));

        if (cantidad.compareTo(BigDecimal.ZERO) < 0 || cantidad.compareTo(new BigDecimal("999")) > 0) {
            resp.sendRedirect(req.getContextPath() + "/Inventario?accion=editar&id=" + id + "&error=cantidad_invalida");
            return;
        }

        InventarioCasa inv = new InventarioCasa();
        inv.setIdInventario(id);
        inv.setIdHogar(idHogar);
        inv.setCantidad(cantidad);

        if (new InventarioCasaDao().actualizar(inv)) {
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
        InventarioCasa inv = new InventarioCasaDao().obtenerPorId(id, idHogar);
        String nombre = inv != null ? inv.getNombreProducto() : "";
        new InventarioCasaDao().eliminar(id, idHogar);
        req.setAttribute("nombreProducto", nombre);
        forward(req, resp, "07_ProductoEliminado.jsp");
    }

    // ─── Validaciones ─────────────────────────────────────────────────────────

    private String validarNombreProducto(String nombre) {
        if (nombre == null || nombre.isEmpty()) return "El nombre del producto es obligatorio.";
        if (nombre.length() < 5) return "El nombre debe tener al menos 5 caracteres.";
        if (nombre.length() > 50) return "El nombre no puede superar 50 caracteres.";
        if (!P_NOMBRE_PRODUCTO.matcher(nombre).matches())
            return "Solo se permiten letras, números, espacios y los símbolos: . , # -";
        return null;
    }

    // ─── Utilidades ───────────────────────────────────────────────────────────

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

    private int idHogar(HttpServletRequest req) { return (Integer) req.getSession().getAttribute("idHogar"); }
    private int idRol(HttpServletRequest req)   { return (Integer) req.getSession().getAttribute("idRol"); }

    private BigDecimal parseBD(String s) {
        try {
            if (s == null || s.trim().isEmpty()) return BigDecimal.ZERO;
            return new BigDecimal(s.trim().replace(",", "."));
        } catch (Exception e) { return BigDecimal.ZERO; }
    }

    private int parseInt(String s) { try { return Integer.parseInt(s); } catch (Exception e) { return 0; } }
    private String nvl(String s)   { return s == null ? "" : s; }
}
