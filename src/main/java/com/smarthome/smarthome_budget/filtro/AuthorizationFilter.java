package com.smarthome.smarthome_budget.filtro;

import com.smarthome.smarthome_budget.dao.DetallesHogaresDao;
import com.smarthome.smarthome_budget.modelo.Usuario;
import com.smarthome.smarthome_budget.modelo.Roles;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthorizationFilter implements Filter {

    private static final String[] PUBLIC_PATHS = {
        "/",
        "/index.jsp",
        "/Login", "/Registro", "/RecuperarClave", "/NuevaClave", "/testConexion",
        "/public/modules/01_autenticacion/", "/public/css/", "/public/js/", "/public/images/",
        "/asset/"
    };

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  httpRequest  = (HttpServletRequest)  request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = httpRequest.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        if (usuario == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath()
                    + "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_requerida");
            return;
        }

        // CORRECCIÓN: obtener idHogar desde sesión, no hardcodeado
        if (!tieneAccesoModulo(httpRequest, session)) {
            httpResponse.sendRedirect(httpRequest.getContextPath()
                    + "/public/modules/MenuPrincipal/accesoDenegado.jsp");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        for (String publicPath : PUBLIC_PATHS) {
            if (path.startsWith(publicPath)) return true;
        }
        return false;
    }

    private boolean tieneAccesoModulo(HttpServletRequest request, HttpSession session) {
        String path = request.getRequestURI().substring(request.getContextPath().length());

        // Obtener idRol directamente de sesión (ya calculado en login)
        Integer idRol = (Integer) session.getAttribute("idRol");
        if (idRol == null) return false;

        // Módulo finanzas: solo ROL 1 (ADMIN) y ROL 2 (COTITULAR) tienen acceso completo
        // ROL 3 (INVITADO) no tiene acceso a finanzas
        if (path.startsWith("/Finanzas") || path.contains("/05_Finanzas/")) {
            return idRol == 1 || idRol == 2;
        }

        
        // Facturas: todos pueden ver, pero escritura solo ROL 1 y 2
        // El control granular lo hace el servlet
        if (path.startsWith("/Facturas") || path.contains("/02_Gestion_facturas")) {
            return true; // el servlet controla permisos de escritura
        }

        // Listas, Inventario: todos pueden acceder (el servlet limita escritura)
        return true;
    }

    @Override
    public void destroy() {}
}
