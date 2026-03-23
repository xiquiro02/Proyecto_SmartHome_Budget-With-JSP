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

@WebServlet("/Listas")
public class ListasComprasServlet extends HttpServlet {

    private static final String BASE = "/public/modules/03_ListasCompras/";

    // Letras (con tildes/ñ), números, espacios, . - _
    private static final Pattern P_NOMBRE_LISTA    = Pattern.compile("^[\\p{L}\\p{N} .\\-_]+$");
    // Letras, números, espacios y: . , # -
    private static final Pattern P_NOMBRE_PRODUCTO = Pattern.compile("^[\\p{L}\\p{N} .,#\\-]+$");

    // ─── GET ──────────────────────────────────────────────────────────────────

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!sesionValida(req, resp)) return;

        int idHogar = idHogar(req);
        String accion = nvl(req.getParameter("accion"));
        if (accion.isEmpty()) accion = "dashboard";

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

    // ─── POST ─────────────────────────────────────────────────────────────────

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        if (!sesionValida(req, resp)) return;

        int idHogar = idHogar(req);
        int idRol   = idRol(req);
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");
        String accion   = nvl(req.getParameter("accion"));

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

    private void mostrarDashboard(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {
        req.setAttribute("listas", new ListaComprasDao().listarPorHogar(idHogar));
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
        req.setAttribute("lista", lista);
        req.setAttribute("detalles", new DetalleListaComprasDao().listarPorLista(id));
        forward(req, resp, "04_EditarListaCompras.jsp");
    }

    private void mostrarDetalle(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {
        int id = parseInt(req.getParameter("id"));
        if (id <= 0) { resp.sendRedirect(req.getContextPath() + "/Listas"); return; }
        ListaCompras lista = new ListaComprasDao().obtenerPorId(id, idHogar);
        if (lista == null) { resp.sendRedirect(req.getContextPath() + "/Listas?error=no_encontrada"); return; }
        req.setAttribute("lista", lista);
        req.setAttribute("detalles", new DetalleListaComprasDao().listarPorLista(id));
        forward(req, resp, "14_VerDetallesLista.jsp");
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

        String nombre = nvl(req.getParameter("nombreLista")).trim();
        String err = validarNombreLista(nombre);
        if (err != null) {
            req.setAttribute("error", err);
            req.setAttribute("valorNombre", nombre);
            forward(req, resp, "02_CrearLista.jsp"); return;
        }

        ListaComprasDao dao = new ListaComprasDao();
        if (dao.existeNombre(idHogar, nombre)) {
            req.setAttribute("error", "Ya existe una lista con ese nombre. Elige un nombre diferente.");
            req.setAttribute("valorNombre", nombre);
            forward(req, resp, "02_CrearLista.jsp"); return;
        }

        ListaCompras lista = new ListaCompras();
        lista.setIdHogar(idHogar);
        lista.setNombreLista(nombre);

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

    private void actualizarLista(HttpServletRequest req, HttpServletResponse resp,
                                 int idHogar, int idRol)
            throws ServletException, IOException {
        if (!puedeGestionar(idRol)) {
            resp.sendRedirect(req.getContextPath() + "/Listas?error=sin_permiso"); return;
        }
        int id = parseInt(req.getParameter("idLista"));
        String nombre = nvl(req.getParameter("nombreLista")).trim();
        String estado = req.getParameter("estadoLista");

        String err = validarNombreLista(nombre);
        if (id <= 0 || err != null) {
            resp.sendRedirect(req.getContextPath() + "/Listas?accion=editar&id=" + id +
                "&error=" + (err != null ? "nombre_invalido" : "campos_vacios"));
            return;
        }

        ListaCompras lista = new ListaCompras();
        lista.setIdListaCompras(id);
        lista.setIdHogar(idHogar);
        lista.setNombreLista(nombre);
        lista.setEstadoLista(estado != null ? estado : "Pendiente");

        if (new ListaComprasDao().actualizar(lista)) {
            req.setAttribute("nombreLista", nombre);
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

        int idLista       = parseInt(req.getParameter("idLista"));
        String nombreProd = nvl(req.getParameter("nombreProducto")).trim();
        String cantStr    = nvl(req.getParameter("cantidad")).replace(",", ".");
        int idTipo        = parseInt(req.getParameter("idTipoProducto"));

        ListaCompras lista = new ListaComprasDao().obtenerPorId(idLista, idHogar);

        String errN = validarNombreProducto(nombreProd);
        if (errN != null) {
            req.setAttribute("error", errN);
            req.setAttribute("lista", lista);
            req.setAttribute("tiposProducto", new ProductoDao().listarTipos());
            forward(req, resp, "08_AgregarProductos.jsp"); return;
        }

        BigDecimal cantidad;
        try { cantidad = new BigDecimal(cantStr); }
        catch (Exception e) { cantidad = BigDecimal.ZERO; }

        if (cantidad.compareTo(BigDecimal.ZERO) <= 0 || cantidad.compareTo(new BigDecimal("999")) > 0) {
            req.setAttribute("error", "La cantidad debe ser mayor a 0 y máximo 999.");
            req.setAttribute("lista", lista);
            req.setAttribute("tiposProducto", new ProductoDao().listarTipos());
            forward(req, resp, "08_AgregarProductos.jsp"); return;
        }

        if (idTipo <= 0) idTipo = 5;

        int idProducto = new ProductoDao().obtenerOCrearProducto(nombreProd, idTipo);
        if (idProducto <= 0) {
            req.setAttribute("error", "Error al registrar el producto.");
            req.setAttribute("lista", lista);
            req.setAttribute("tiposProducto", new ProductoDao().listarTipos());
            forward(req, resp, "08_AgregarProductos.jsp"); return;
        }

        int resultado = new DetalleListaComprasDao().agregarProducto(idLista, idProducto, cantidad);
        new ListaComprasDao().recalcularEstado(idLista);

        if (resultado >= 1) {
            req.setAttribute("nombreProducto", nombreProd);
            req.setAttribute("cantidad", cantidad.toPlainString());
            req.setAttribute("idLista", idLista);
            req.setAttribute("fueActualizado", resultado == 2);
            forward(req, resp, "09_ConfirmarProducto.jsp");
        } else {
            req.setAttribute("error", "No se pudo agregar el producto.");
            req.setAttribute("lista", lista);
            req.setAttribute("tiposProducto", new ProductoDao().listarTipos());
            forward(req, resp, "08_AgregarProductos.jsp");
        }
    }

    private void actualizarProducto(HttpServletRequest req, HttpServletResponse resp,
                                    int idHogar, int idRol)
            throws ServletException, IOException {
        int idDetalle = parseInt(req.getParameter("idDetalle"));
        int idLista   = parseInt(req.getParameter("idLista"));
        String cantStr = nvl(req.getParameter("cantidad")).replace(",", ".");

        BigDecimal cantidad;
        try { cantidad = new BigDecimal(cantStr); }
        catch (Exception e) { cantidad = BigDecimal.ZERO; }

        if (cantidad.compareTo(BigDecimal.ZERO) <= 0 || cantidad.compareTo(new BigDecimal("999")) > 0) {
            resp.sendRedirect(req.getContextPath() +
                "/Listas?accion=editarProducto&idDetalle=" + idDetalle +
                "&idLista=" + idLista + "&error=cantidad_invalida");
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

    /**
     * FIX: usa redirect en lugar de forward para evitar que la vista se rompa.
     * El forward perdía el objeto 'lista' y 'detalles' porque no los volvía a cargar.
     */
    private void marcarTodos(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws IOException {
        int idLista = parseInt(req.getParameter("idLista"));
        boolean todos = "true".equals(req.getParameter("comprado"));
        new DetalleListaComprasDao().marcarTodos(idLista, todos);
        new ListaComprasDao().recalcularEstado(idLista);
        resp.sendRedirect(req.getContextPath() + "/Listas?accion=verDetalle&id=" + idLista +
            (todos ? "&info=todos_marcados" : "&info=todos_desmarcados"));
    }

    // ─── Validaciones ─────────────────────────────────────────────────────────

    private String validarNombreLista(String nombre) {
        if (nombre == null || nombre.isEmpty()) return "El nombre de la lista es obligatorio.";
        if (nombre.length() < 5) return "El nombre debe tener al menos 5 caracteres.";
        if (nombre.length() > 50) return "El nombre no puede superar 50 caracteres.";
        if (!P_NOMBRE_LISTA.matcher(nombre).matches())
            return "Solo se permiten letras, números, espacios, puntos, guiones y guiones bajos.";
        return null;
    }

    private String validarNombreProducto(String nombre) {
        if (nombre == null || nombre.isEmpty()) return "El nombre del producto es obligatorio.";
        if (nombre.length() < 5) return "El nombre del producto debe tener al menos 5 caracteres.";
        if (nombre.length() > 50) return "El nombre del producto no puede superar 50 caracteres.";
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

    private boolean puedeGestionar(int idRol) { return idRol == 1 || idRol == 2; }

    private void forward(HttpServletRequest req, HttpServletResponse resp, String vista)
            throws ServletException, IOException {
        req.getRequestDispatcher(BASE + vista).forward(req, resp);
    }

    private int idHogar(HttpServletRequest req) { return (Integer) req.getSession().getAttribute("idHogar"); }
    private int idRol(HttpServletRequest req)   { return (Integer) req.getSession().getAttribute("idRol"); }
    private int parseInt(String s) { try { return Integer.parseInt(s); } catch (Exception e) { return 0; } }
    private String nvl(String s)   { return s == null ? "" : s; }
}
