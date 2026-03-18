package com.smarthome.smarthome_budget.controlador;

import com.smarthome.smarthome_budget.dao.*;
import com.smarthome.smarthome_budget.modelo.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/Catalogos")
public class GestionCatalogosServlet extends HttpServlet {

    private static final String BASE = "/public/modules/06_Catalogos/";

    // ── GET ───────────────────────────────────────────────────────────────────
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!sesionValida(req, resp)) return;
        int idRol = idRol(req);

        // ROL 3 sin acceso
        if (idRol == 3) { resp.sendRedirect(req.getContextPath() + "/Menu?error=acceso_denegado"); return; }

        String tipo   = nvl(req.getParameter("tipo"));
        String accion = nvl(req.getParameter("accion"));

        switch (tipo) {
            case "categoriaEgreso":
                if ("editar".equals(accion)) mostrarFormEditar(req, resp, tipo); else listar(req, resp, tipo); break;
            case "categoriaIngreso":
                if ("editar".equals(accion)) mostrarFormEditar(req, resp, tipo); else listar(req, resp, tipo); break;
            case "metodoPago":
                if ("editar".equals(accion)) mostrarFormEditar(req, resp, tipo); else listar(req, resp, tipo); break;
            case "tipoProducto":
                if ("editar".equals(accion)) mostrarFormEditar(req, resp, tipo); else listar(req, resp, tipo); break;
            default:
                resp.sendRedirect(req.getContextPath() + "/Menu");
        }
    }

    // ── POST ──────────────────────────────────────────────────────────────────
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        if (!sesionValida(req, resp)) return;
        int idRol = idRol(req);
        if (idRol == 3) { resp.sendRedirect(req.getContextPath() + "/Menu?error=acceso_denegado"); return; }

        String tipo   = nvl(req.getParameter("tipo"));
        String accion = nvl(req.getParameter("accion"));
        String nombre = nvl(req.getParameter("nombre")).trim();
        int id        = parseInt(req.getParameter("id"));

        // Solo ROL 1 puede eliminar
        if ("eliminar".equals(accion) && idRol != 1) {
            resp.sendRedirect(req.getContextPath() + "/Catalogos?tipo=" + tipo + "&error=sin_permiso"); return;
        }

        if (nombre.isEmpty() && !"eliminar".equals(accion)) {
            req.setAttribute("error", "El nombre no puede estar vacío.");
            req.setAttribute("tipo", tipo);
            listar(req, resp, tipo); return;
        }

        boolean ok = false;
        String errorMsg = null;

        switch (tipo) {
            case "categoriaEgreso": {
                CategoriaEgresoDao dao = new CategoriaEgresoDao();
                switch (accion) {
                    case "crear":
                        if (dao.nombreDuplicado(nombre, 0)) { errorMsg = "Ya existe una categoría con ese nombre."; break; }
                        ok = dao.crear(nombre) > 0; break;
                    case "actualizar":
                        if (dao.nombreDuplicado(nombre, id)) { errorMsg = "Ya existe una categoría con ese nombre."; break; }
                        ok = dao.actualizar(id, nombre); break;
                    case "eliminar":
                        if (dao.tieneEgresos(id)) { errorMsg = "No se puede eliminar: tiene facturas o egresos asociados."; break; }
                        ok = dao.eliminar(id); break;
                }
                break;
            }
            case "categoriaIngreso": {
                CategoriaIngresoDao dao = new CategoriaIngresoDao();
                switch (accion) {
                    case "crear":
                        if (dao.nombreDuplicado(nombre, 0)) { errorMsg = "Ya existe una categoría con ese nombre."; break; }
                        ok = dao.crear(nombre) > 0; break;
                    case "actualizar":
                        if (dao.nombreDuplicado(nombre, id)) { errorMsg = "Ya existe una categoría con ese nombre."; break; }
                        ok = dao.actualizar(id, nombre); break;
                    case "eliminar":
                        if (dao.tieneIngresos(id)) { errorMsg = "No se puede eliminar: tiene ingresos asociados."; break; }
                        ok = dao.eliminar(id); break;
                }
                break;
            }
            case "metodoPago": {
                MetodoPagoDao dao = new MetodoPagoDao();
                switch (accion) {
                    case "crear":
                        if (dao.nombreDuplicado(nombre, 0)) { errorMsg = "Ya existe un método con ese nombre."; break; }
                        ok = dao.crear(nombre) > 0; break;
                    case "actualizar":
                        if (dao.nombreDuplicado(nombre, id)) { errorMsg = "Ya existe un método con ese nombre."; break; }
                        ok = dao.actualizar(id, nombre); break;
                    case "eliminar":
                        if (dao.esPredeterminado(id)) { errorMsg = "No se puede eliminar el método predeterminado (Efectivo)."; break; }
                        if (dao.tieneEgresos(id)) { errorMsg = "No se puede eliminar: tiene egresos asociados."; break; }
                        ok = dao.eliminar(id); break;
                }
                break;
            }
            case "tipoProducto": {
                TipoProductoDao dao = new TipoProductoDao();
                switch (accion) {
                    case "crear":
                        if (dao.nombreDuplicado(nombre, 0)) { errorMsg = "Ya existe un tipo con ese nombre."; break; }
                        ok = dao.crear(nombre) > 0; break;
                    case "actualizar":
                        if (dao.nombreDuplicado(nombre, id)) { errorMsg = "Ya existe un tipo con ese nombre."; break; }
                        ok = dao.actualizar(id, nombre); break;
                    case "eliminar":
                        if (dao.tieneProductos(id)) { errorMsg = "No se puede eliminar: tiene productos registrados."; break; }
                        ok = dao.eliminar(id); break;
                }
                break;
            }
        }

        if (errorMsg != null) {
            req.setAttribute("error", errorMsg);
            req.setAttribute("tipo", tipo);
            listar(req, resp, tipo);
        } else if (ok) {
            resp.sendRedirect(req.getContextPath() + "/Catalogos?tipo=" + tipo + "&exito=" + accion);
        } else {
            req.setAttribute("error", "No se pudo completar la operación. Intenta de nuevo.");
            req.setAttribute("tipo", tipo);
            listar(req, resp, tipo);
        }
    }

    // ── Vistas ─────────────────────────────────────────────────────────────────

    private void listar(HttpServletRequest req, HttpServletResponse resp, String tipo)
            throws ServletException, IOException {
        cargarDatos(req, tipo);
        req.setAttribute("tipo", tipo);
        req.getRequestDispatcher(BASE + "01_gestionCatalogos.jsp").forward(req, resp);
    }

    private void mostrarFormEditar(HttpServletRequest req, HttpServletResponse resp, String tipo)
            throws ServletException, IOException {
        int id = parseInt(req.getParameter("id"));
        Object item = obtenerPorId(tipo, id);
        if (item == null) { resp.sendRedirect(req.getContextPath() + "/Catalogos?tipo=" + tipo); return; }
        req.setAttribute("itemEditar", item);
        req.setAttribute("tipo", tipo);
        cargarDatos(req, tipo);
        req.getRequestDispatcher(BASE + "01_gestionCatalogos.jsp").forward(req, resp);
    }

    private void cargarDatos(HttpServletRequest req, String tipo) {
        switch (tipo) {
            case "categoriaEgreso":  req.setAttribute("items", new CategoriaEgresoDao().listarCategorias()); break;
            case "categoriaIngreso": req.setAttribute("items", new CategoriaIngresoDao().listarCategorias()); break;
            case "metodoPago":       req.setAttribute("items", new MetodoPagoDao().listarMetodosPago()); break;
            case "tipoProducto":     req.setAttribute("items", new TipoProductoDao().listarTipos()); break;
        }
    }

    private Object obtenerPorId(String tipo, int id) {
        switch (tipo) {
            case "categoriaEgreso":  return new CategoriaEgresoDao().obtenerPorId(id);
            case "categoriaIngreso": return new CategoriaIngresoDao().obtenerPorId(id);
            case "metodoPago":       return new MetodoPagoDao().obtenerPorId(id);
            case "tipoProducto":     return new TipoProductoDao().obtenerPorId(id);
            default: return null;
        }
    }

    private boolean sesionValida(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_requerida");
            return false;
        }
        return true;
    }

    private int idRol(HttpServletRequest req) {
        Object r = req.getSession().getAttribute("idRol");
        return r != null ? (Integer) r : 3;
    }
    private int parseInt(String s) { try { return Integer.parseInt(s); } catch (Exception e) { return 0; } }
    private String nvl(String s)   { return s == null ? "" : s.trim(); }
}
