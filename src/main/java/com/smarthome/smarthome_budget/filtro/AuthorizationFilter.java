package com.smarthome.smarthome_budget.filtro;

import com.smarthome.smarthome_budget.modelo.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/* Clase: AuthorizationFilter
   Propósito: Filtro de seguridad global que intercepta todas las solicitudes HTTP de la aplicación
   (patrón "/*"). Controla el acceso en tres niveles: primero permite sin restricción los archivos
   estáticos y las rutas públicas (login, registro, recuperación de clave); luego verifica que exista
   una sesión activa con usuario y rol válidos; finalmente aplica las reglas de acceso por módulo
   según el rol del usuario (RF30). Ante sesión inválida redirige al login; ante acceso denegado
   redirige al menú con parámetro de error para que el servlet lo gestione.
*/
@WebFilter("/*")
public class AuthorizationFilter implements Filter {

    // Arreglo de rutas completamente públicas que no requieren sesión activa para ser accedidas
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

    // Arreglo de extensiones de archivos estáticos que siempre se sirven sin verificar sesión
    private static final String[] STATIC_EXTENSIONS = {
        ".css", ".js", ".png", ".jpg", ".jpeg", ".gif", ".webp",
        ".svg", ".ico", ".woff", ".woff2", ".ttf", ".eot"
    };

    /* Método: init
       Propósito: Inicializar el filtro al arrancar la aplicación. No requiere configuración adicional.
       @param filterConfig → Objeto FilterConfig con la configuración del filtro (no utilizado)
    */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    /* Método: doFilter
       Propósito: Interceptar cada solicitud HTTP y aplicar las tres capas de control de acceso:
       archivos estáticos → rutas públicas → sesión válida → rol con permiso al módulo.
       Si la sesión no existe o está incompleta, redirige al login. Si el rol no tiene acceso
       al módulo solicitado, redirige al menú con el parámetro error=acceso_denegado.
       @param request  → ServletRequest de la solicitud HTTP entrante
       @param response → ServletResponse de la respuesta HTTP saliente
       @param chain    → FilterChain para continuar al siguiente filtro o al servlet destino
    */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Objetos HTTP tipados para acceder a sesión, URI y redirecciones
        HttpServletRequest  req  = (HttpServletRequest)  request;
        HttpServletResponse resp = (HttpServletResponse) response;
        // Texto con la ruta relativa de la solicitud sin el contexto de la aplicación
        String path = req.getRequestURI().substring(req.getContextPath().length());

        // Paso 1: archivos estáticos — se sirven siempre sin verificar sesión
        if (esArchivoEstatico(path)) {
            chain.doFilter(request, response);
            return;
        }

        // Paso 2: rutas públicas — se permiten siempre sin verificar sesión
        if (esRutaPublica(path)) {
            chain.doFilter(request, response);
            return;
        }

        // Paso 3: verificar que exista una sesión activa con usuario autenticado
        // Objeto HttpSession obtenido sin crear uno nuevo si no existe (false)
        HttpSession session = req.getSession(false);
        // Objeto Usuario recuperado del atributo de sesión; null si no hay sesión o no está autenticado
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        if (usuario == null) {
            resp.sendRedirect(req.getContextPath() +
                "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_requerida");
            return;
        }

        // Paso 4: verificar que la sesión tenga el atributo de rol definido
        // Variable entera con el ID de rol del usuario en sesión; null si la sesión está incompleta
        Integer idRol = (Integer) session.getAttribute("idRol");
        if (idRol == null) {
            // Sesión incompleta — se invalida y se fuerza un nuevo inicio de sesión
            session.invalidate();
            resp.sendRedirect(req.getContextPath() +
                "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_invalida");
            return;
        }

        // Paso 5: verificar que el rol tenga permiso de acceso al módulo solicitado
        if (!tieneAccesoModulo(path, idRol)) {
            // Redirige al menú con parámetro de error; el MenuServlet gestiona el mensaje al usuario
            resp.sendRedirect(req.getContextPath() + "/Menu?error=acceso_denegado");
            return;
        }

        // Todos los controles superados — continuar al servlet o JSP destino
        chain.doFilter(request, response);
    }

    /* Método: tieneAccesoModulo
       Propósito: Determinar si el rol indicado tiene permiso para acceder al path solicitado,
       según las reglas de acceso por módulo definidas en RF30. El control granular de escritura
       (crear, editar, eliminar) siempre recae en el servlet correspondiente de cada módulo.
       Reglas de acceso:
         ROL 1 (Administrador) → acceso total a todos los módulos
         ROL 2 (Cotitular)     → acceso total excepto escritura en Finanzas (controlado por FinanzasServlet)
         ROL 3 (Invitado)      → sin acceso al módulo de Finanzas; solo lectura en los demás módulos
       @param path  → Texto con la ruta relativa de la solicitud
       @param idRol → Entero con el ID del rol del usuario en sesión (1, 2 o 3)
       @return boolean → true si el rol tiene acceso al módulo, false si debe ser bloqueado
    */
    private boolean tieneAccesoModulo(String path, int idRol) {

        // Módulo Finanzas: ROL 3 (Invitado) no puede acceder en absoluto (RF30: restricción total)
        if (path.startsWith("/Finanzas") || path.contains("/05_Finanzas/")) {
            return idRol == 1 || idRol == 2;
        }

        // Módulo Facturas: todos los roles pueden acceder; la escritura la controla FacturasServlet por rol
        if (path.startsWith("/Facturas") || path.contains("/02_Gestion_facturas")) {
            return true;
        }

        // Módulo Listas de Compras: todos los roles pueden acceder; escritura controlada por ListasComprasServlet
        if (path.startsWith("/Listas") || path.contains("/03_ListasCompras")) {
            return true;
        }

        // Módulo Inventario: todos los roles pueden acceder; escritura controlada por InventarioServlet
        if (path.startsWith("/Inventario") || path.contains("/04_ProductosDisponiblesCasa")) {
            return true;
        }

        // Menú, Perfil, Seguridad, Generación de Código e Invitación, Catálogos: acceso para todos los roles autenticados
        // (el CatálogosServlet bloquea internamente las operaciones de escritura para ROL 3)
        if (path.startsWith("/Menu") ||
            path.startsWith("/Perfil") ||
            path.startsWith("/Seguridad") ||
            path.startsWith("/GenerarCodigoInvitacion") ||
            path.startsWith("/Catalogos") ||
            path.contains("/06_Catalogos/")) {
            return true;
        }

        // JSPs del menú principal: acceso para todos los roles autenticados
        if (path.contains("/MenuPrincipal/")) {
            return true;
        }

        // Cualquier otra ruta autenticada: permitir por defecto
        // (el control granular siempre recae en el servlet correspondiente)
        return true;
    }

    /* Método: esRutaPublica
       Propósito: Verificar si el path solicitado corresponde a una ruta pública definida
       en PUBLIC_PATHS que no requiere sesión activa para ser accedida.
       @param path → Texto con la ruta relativa de la solicitud
       @return boolean → true si la ruta es pública, false si requiere autenticación
    */
    private boolean esRutaPublica(String path) {
        for (String pub : PUBLIC_PATHS) {
            if (path.startsWith(pub)) return true;
        }
        return false;
    }

    /* Método: esArchivoEstatico
       Propósito: Verificar si el path solicitado corresponde a un archivo estático
       (CSS, JS, imágenes, fuentes) que debe servirse sin ningún control de sesión.
       @param path → Texto con la ruta relativa de la solicitud
       @return boolean → true si es un archivo estático por su extensión, false si no lo es
    */
    private boolean esArchivoEstatico(String path) {
        // Texto con la ruta en minúsculas para comparar extensiones sin importar el caso
        String lower = path.toLowerCase();
        for (String ext : STATIC_EXTENSIONS) {
            if (lower.endsWith(ext)) return true;
        }
        return false;
    }

    /* Método: destroy
       Propósito: Liberar recursos del filtro al detener la aplicación. No requiere limpieza adicional.
    */
    @Override
    public void destroy() {}
}
