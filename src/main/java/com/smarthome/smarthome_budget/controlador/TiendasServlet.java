package com.smarthome.smarthome_budget.controlador;

import com.smarthome.smarthome_budget.basedatos.claseConexion;
import com.smarthome.smarthome_budget.modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet del módulo Tiendas Cercanas.
 * Carga las tiendas de la BD (tabla Tienda) y la ubicación registrada del usuario.
 * Las vistas usan Leaflet.js + OpenStreetMap para el mapa dinámico.
 *
 * GET /Tiendas                             → dashboard 01_TiendasCercanas.jsp
 * GET /Tiendas?accion=lista                → 06_Lista-tiendasCercanas.jsp (con mapa)
 * GET /Tiendas?accion=informacion&id=X     → 07_InformacionTienda.jsp
 * GET /Tiendas?accion=comparar             → 08_CompararPrecios.jsp
 * GET /Tiendas?accion=ubicacionGps         → 03_Ubicacion-gps.jsp
 * GET /Tiendas?accion=ubicacionManual      → 04_UbicacionManualmente.jsp
 * POST /Tiendas?accion=guardarUbicacion    → guarda ubicación manual y redirige a lista
 */
@WebServlet("/Tiendas")
public class TiendasServlet extends HttpServlet {

    private static final String BASE = "/public/modules/05_TiendasCercanas/";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() +
                    "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_requerida");
            return;
        }

        // Solo ADMIN y COTITULAR acceden a tiendas
        int idRol = (Integer) session.getAttribute("idRol");
        if (idRol == 3) {
            resp.sendRedirect(req.getContextPath() + "/Menu");
            return;
        }

        String accion = req.getParameter("accion");
        if (accion == null) accion = "dashboard";

        switch (accion) {
            case "lista":
                mostrarLista(req, resp, session);
                break;
            case "informacion":
                mostrarInformacion(req, resp);
                break;
            case "comparar":
                forward(req, resp, "08_CompararPrecios.jsp");
                break;
            case "ubicacionGps":
                forward(req, resp, "03_Ubicacion-gps.jsp");
                break;
            case "ubicacionManual":
                forward(req, resp, "04_UbicacionManualmente.jsp");
                break;
            default:
                forward(req, resp, "01_TiendasCercanas.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() +
                    "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_requerida");
            return;
        }

        String accion = req.getParameter("accion");
        if ("guardarUbicacion".equals(accion)) {
            guardarUbicacion(req, resp, session);
        } else {
            resp.sendRedirect(req.getContextPath() + "/Tiendas");
        }
    }

    // ─── Vistas ───────────────────────────────────────────────────────────────

    private void mostrarLista(HttpServletRequest req, HttpServletResponse resp, HttpSession session)
            throws ServletException, IOException {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        List<Object[]> tiendas = listarTiendasConCoordenadas();
        Object[] ubicacionUsuario = obtenerUbicacionUsuario(usuario.getIDUsuario());

        req.setAttribute("tiendas",         tiendas);
        req.setAttribute("ubicacionUsuario", ubicacionUsuario);
        forward(req, resp, "06_Lista-tiendasCercanas.jsp");
    }

    private void mostrarInformacion(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int idTienda = parsearInt(req.getParameter("id"));
        if (idTienda > 0) {
            Object[] tienda = obtenerTiendaPorId(idTienda);
            req.setAttribute("tienda", tienda);
        }
        forward(req, resp, "07_InformacionTienda.jsp");
    }

    private void guardarUbicacion(HttpServletRequest req, HttpServletResponse resp, HttpSession session)
            throws IOException {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String pais      = req.getParameter("pais");
        String ciudad    = req.getParameter("ciudad");
        String direccion = req.getParameter("direccion");

        if (pais == null || ciudad == null || direccion == null
                || pais.trim().isEmpty() || ciudad.trim().isEmpty() || direccion.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/Tiendas?accion=ubicacionManual&error=campos_vacios");
            return;
        }

        String sql = "INSERT INTO Ubicacion_Usuario (Pais, Ciudad, Direccion, FechaRegistro, IDUsuario) " +
                     "VALUES (?, ?, ?, NOW(), ?) " +
                     "ON DUPLICATE KEY UPDATE Pais=VALUES(Pais), Ciudad=VALUES(Ciudad), " +
                     "Direccion=VALUES(Direccion), FechaRegistro=NOW()";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, pais.trim());
            ps.setString(2, ciudad.trim());
            ps.setString(3, direccion.trim());
            ps.setInt(4, usuario.getIDUsuario());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error guardando ubicación: " + e.getMessage());
        }

        // Mostrar confirmación y luego redirigir a la lista
        resp.sendRedirect(req.getContextPath() + "/Tiendas?accion=lista&exito=ubicacion_guardada");
    }

    // ─── Consultas BD ─────────────────────────────────────────────────────────

    /**
     * Lista todas las tiendas registradas con sus coordenadas.
     * Cada Object[] = {idTienda, nombre, pais, ciudad, direccion, latitud, longitud}
     */
    private List<Object[]> listarTiendasConCoordenadas() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT t.IDTienda, t.NombreTienda, t.Pais, t.Ciudad, t.Direccion, " +
                     "  COALESCE(c.Latitud, 0) AS Latitud, COALESCE(c.Logitud, 0) AS Longitud " +
                     "FROM Tienda t " +
                     "LEFT JOIN Coordenadas c ON t.IDCoordenada = c.IDCoordenada " +
                     "ORDER BY t.NombreTienda ASC";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("IDTienda"),
                    rs.getString("NombreTienda"),
                    rs.getString("Pais"),
                    rs.getString("Ciudad"),
                    rs.getString("Direccion"),
                    rs.getDouble("Latitud"),
                    rs.getDouble("Longitud")
                });
            }
        } catch (SQLException e) {
            System.err.println("Error listando tiendas: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Obtiene la última ubicación registrada del usuario.
     * Retorna Object[] = {pais, ciudad, direccion, latitud, longitud} o null si no existe.
     */
    private Object[] obtenerUbicacionUsuario(int idUsuario) {
        String sql = "SELECT u.Pais, u.Ciudad, u.Direccion, " +
                     "  COALESCE(c.Latitud, 0) AS Latitud, COALESCE(c.Logitud, 0) AS Longitud " +
                     "FROM Ubicacion_Usuario u " +
                     "LEFT JOIN Coordenadas c ON u.IDCoordenada = c.IDCoordenada " +
                     "WHERE u.IDUsuario = ? ORDER BY u.FechaRegistro DESC LIMIT 1";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Object[]{
                        rs.getString("Pais"),
                        rs.getString("Ciudad"),
                        rs.getString("Direccion"),
                        rs.getDouble("Latitud"),
                        rs.getDouble("Longitud")
                    };
                }
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo ubicación usuario: " + e.getMessage());
        }
        return null;
    }

    private Object[] obtenerTiendaPorId(int idTienda) {
        String sql = "SELECT t.*, COALESCE(c.Latitud, 0) AS Latitud, COALESCE(c.Logitud, 0) AS Longitud " +
                     "FROM Tienda t LEFT JOIN Coordenadas c ON t.IDCoordenada = c.IDCoordenada " +
                     "WHERE t.IDTienda = ?";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTienda);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Object[]{
                        rs.getInt("IDTienda"),
                        rs.getString("NombreTienda"),
                        rs.getString("Pais"),
                        rs.getString("Ciudad"),
                        rs.getString("Direccion"),
                        rs.getDouble("Latitud"),
                        rs.getDouble("Longitud")
                    };
                }
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo tienda: " + e.getMessage());
        }
        return null;
    }

    // ─── Utilidades ──────────────────────────────────────────────────────────

    private void forward(HttpServletRequest req, HttpServletResponse resp, String vista)
            throws ServletException, IOException {
        req.getRequestDispatcher(BASE + vista).forward(req, resp);
    }

    private int parsearInt(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }
}
