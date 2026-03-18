package com.smarthome.smarthome_budget.filtro;

import com.smarthome.smarthome_budget.modelo.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthorizationFilter implements Filter {

    // ── Rutas completamente públicas (sin sesión) ─────────────────────────────
    private static final String[] PUBLIC_PATHS = {
        "/",
        "/index.jsp",
        "/Login",
        "/Registro",
        "/RecuperarClave",
        "/NuevaClave",
        "/testConexion",
        "/public/modules/01_autenticacion/",
        "/asset/"
    };

    // ── Extensiones de archivos estáticos — siempre permitir ─────────────────
    private static final String[] STATIC_EXTENSIONS = {
        ".css", ".js", ".png", ".jpg", ".jpeg", ".gif", ".webp",
        ".svg", ".ico", ".woff", ".woff2", ".ttf", ".eot"
    };

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req  = (HttpServletRequest)  request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String path = req.getRequestURI().substring(req.getContextPath().length());

        // 1. Archivos estáticos — siempre pasar sin verificar sesión
        if (esArchivoEstatico(path)) {
            chain.doFilter(request, response);
            return;
        }

        // 2. Rutas públicas — pasar sin verificar sesión
        if (esRutaPublica(path)) {
            chain.doFilter(request, response);
            return;
        }

        // 3. Verificar sesión activa
        HttpSession session = req.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        if (usuario == null) {
            resp.sendRedirect(req.getContextPath() +
                "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_requerida");
            return;
        }

        // 4. Obtener rol de sesión
        Integer idRol = (Integer) session.getAttribute("idRol");
        if (idRol == null) {
            // Sesión incompleta — forzar nuevo login
            session.invalidate();
            resp.sendRedirect(req.getContextPath() +
                "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_invalida");
            return;
        }

        // 5. Verificar acceso según rol y módulo
        if (!tieneAccesoModulo(path, idRol)) {
            // CORRECCIÓN: ya no redirige a accesoDenegado.jsp (inexistente)
            // Redirige al menú con parámetro de error — el servlet lo gestiona
            resp.sendRedirect(req.getContextPath() + "/Menu?error=acceso_denegado");
            return;
        }

        chain.doFilter(request, response);
    }

    // ── Lógica de control de acceso por módulo (RF30) ─────────────────────────

    /**
     * Determina si el rol tiene acceso al path solicitado.
     *
     * ROL 1 (Padre/Admin)       → acceso total
     * ROL 2 (Adulto/Cotitular)  → sin acceso a escritura en Finanzas
     *                             (el servlet FinanzasServlet controla esto)
     * ROL 3 (Hijo/Invitado)     → sin acceso a Finanzas en absoluto
     *                             (sin acceso a escritura en Facturas, Inventario, Listas)
     */
    private boolean tieneAccesoModulo(String path, int idRol) {

        // ── Módulo Finanzas (/Finanzas, JSPs 05_Finanzas) ──────────────────────
        // ROL 3 no puede entrar en absoluto (RF30: "Restricción total")
        if (path.startsWith("/Finanzas") || path.contains("/05_Finanzas/")) {
            return idRol == 1 || idRol == 2;
        }

        // ── Módulo Facturas ─────────────────────────────────────────────────────
        // Todos pueden ver; la escritura la controla FacturasServlet por rol
        if (path.startsWith("/Facturas") || path.contains("/02_Gestion_facturas")) {
            return true;
        }

        // ── Módulo Listas de Compras ────────────────────────────────────────────
        // Todos pueden acceder; escritura controlada por ListasComprasServlet
        if (path.startsWith("/Listas") || path.contains("/03_ListasCompras")) {
            return true;
        }

        // ── Módulo Inventario ───────────────────────────────────────────────────
        // ROL 3 solo puede ver (RF30); escritura controlada por InventarioServlet
        if (path.startsWith("/Inventario") || path.contains("/04_ProductosDisponiblesCasa")) {
            return true;
        }

        // ── Menú, Perfil, Seguridad, Generar Código, Catálogos ──────────────────
        // Todos los roles autenticados pueden acceder a estas rutas
        // (el servlet de catálogos bloquea ROL 3 internamente)
        if (path.startsWith("/Menu") ||
            path.startsWith("/Perfil") ||
            path.startsWith("/Seguridad") ||
            path.startsWith("/GenerarCodigoInvitacion") ||
            path.startsWith("/Catalogos") ||
            path.contains("/06_Catalogos/")) {
            return true;
        }

        // ── Módulo MenuPrincipal JSPs ────────────────────────────────────────────
        if (path.contains("/MenuPrincipal/")) {
            return true;
        }

        // Para cualquier otra ruta autenticada: permitir
        // (control granular siempre en el servlet correspondiente)
        return true;
    }

    private boolean esRutaPublica(String path) {
        for (String pub : PUBLIC_PATHS) {
            if (path.startsWith(pub)) return true;
        }
        return false;
    }

    private boolean esArchivoEstatico(String path) {
        String lower = path.toLowerCase();
        for (String ext : STATIC_EXTENSIONS) {
            if (lower.endsWith(ext)) return true;
        }
        return false;
    }

    @Override
    public void destroy() {}
}
