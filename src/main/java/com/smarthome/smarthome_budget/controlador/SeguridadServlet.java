package com.smarthome.smarthome_budget.controlador;

// Importación de clases necesarias para el manejo de sesión y seguridad
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDateTime;

import com.smarthome.smarthome_budget.dao.UsuarioDao;
import com.smarthome.smarthome_budget.modelo.Usuario;
import com.smarthome.smarthome_budget.utils.Encriptador;

/* Clase: SeguridadServlet
   Propósito: Gestionar las operaciones de seguridad del usuario autenticado.
   Actualmente cubre el cambio de contraseña con validaciones de formato y
   coincidencia. Puede ampliarse para otras acciones de seguridad.
   URL mapeada: /Seguridad
*/
@WebServlet("/Seguridad")
public class SeguridadServlet extends HttpServlet {

    // Instancia del DAO de usuario reutilizable en todos los métodos del servlet
    private final UsuarioDao usuarioDao = new UsuarioDao();
    // Constante de tipo texto con la ruta base de las vistas del menú principal
    private static final String BASE_MENU = "/public/modules/MenuPrincipal/";
    // Constante de tipo texto con la ruta base de las vistas de autenticación
    private static final String BASE_AUTH = "/public/modules/01_autenticacion/";

    /* Método: doGet
       Propósito: Mostrar las vistas de seguridad según la acción solicitada.
       Si la acción es "formCambiarContrasena", muestra el formulario correspondiente.
       Cualquier otra acción redirige al perfil del usuario.
       @param req  → Objeto con los parámetros de la petición HTTP GET
       @param resp → Objeto para redirigir o hacer forward a la vista
    */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Verifica que haya una sesión activa con usuario autenticado
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            // Sin sesión activa, redirige al formulario de inicio de sesión
            resp.sendRedirect(req.getContextPath() + BASE_AUTH + "04_iniciarSesion.jsp");
            return;
        }

        // Variable de tipo texto que almacena la acción solicitada por parámetro URL
        String accion = req.getParameter("accion");
        // Si no se especifica acción, se asigna cadena vacía para evitar NullPointerException
        if (accion == null) accion = "";

        switch (accion) {
            case "formCambiarContrasena":
                // Muestra el formulario para que el usuario ingrese su nueva contraseña
                req.getRequestDispatcher(BASE_MENU + "08_Cambiarcontrasena.jsp").forward(req, resp);
                break;
            default:
                // Si la acción no es reconocida, redirige al perfil del usuario
                resp.sendRedirect(req.getContextPath() + "/Perfil");
        }
    }

    /* Método: doPost
       Propósito: Procesar las acciones de seguridad enviadas por formulario.
       Actualmente gestiona el cambio de contraseña: valida los campos, actualiza
       la contraseña con hash en la base de datos y refresca la sesión del usuario.
       @param req  → Objeto con los datos del formulario enviado
       @param resp → Objeto para redirigir o hacer forward al resultado
    */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Verifica que haya una sesión activa con usuario autenticado
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            // Sin sesión activa, redirige al formulario de inicio de sesión
            resp.sendRedirect(req.getContextPath() + BASE_AUTH + "04_iniciarSesion.jsp");
            return;
        }

        // Objeto Usuario recuperado de la sesión para obtener el ID del usuario actual
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        // Variable de tipo texto que almacena la acción enviada por el formulario
        String accion = req.getParameter("accion");
        // Si no se especifica acción, se asigna cadena vacía para evitar NullPointerException
        if (accion == null) accion = "";

        switch (accion) {

            // ── CAMBIAR CONTRASEÑA ──────────────────────────────────────────
            case "cambiarContrasena": {
                // Variable de tipo texto que almacena la nueva contraseña ingresada
                String password  = req.getParameter("password");
                // Variable de tipo texto que almacena la confirmación de la nueva contraseña
                String password2 = req.getParameter("password2");

                // Ejecuta las validaciones de formato y coincidencia sobre la contraseña
                // Variable de tipo texto que almacena el mensaje de error, o null si es válida
                String errorMsg = validarContrasena(password, password2);
                if (errorMsg != null) {
                    // Si hay error de validación, lo pasa como atributo a la vista
                    req.setAttribute("error", errorMsg);
                    req.getRequestDispatcher(BASE_MENU + "08_Cambiarcontrasena.jsp").forward(req, resp);
                    return;
                }

                // Actualiza la contraseña en la base de datos con hash seguro
                // Variable booleana que indica si la actualización fue exitosa
                boolean ok = usuarioDao.actualizarClaveConHash(usuario.getIDUsuario(), password);
                if (ok) {
                    // Refresca el objeto usuario en sesión para que tenga la nueva clave
                    Usuario actualizado = usuarioDao.obtenerPorId(usuario.getIDUsuario());
                    if (actualizado != null) session.setAttribute("usuario", actualizado);
                    // Muestra la vista de confirmación de contraseña guardada
                    req.getRequestDispatcher(BASE_MENU + "11_Contrasenaguardada.jsp").forward(req, resp);
                } else {
                    // Si la actualización falló, muestra el error en el formulario
                    req.setAttribute("error", "No se pudo actualizar la contraseña. Intenta de nuevo.");
                    req.getRequestDispatcher(BASE_MENU + "08_Cambiarcontrasena.jsp").forward(req, resp);
                }
                break;
            }

            default:
                // Si la acción no es reconocida, redirige al perfil del usuario
                resp.sendRedirect(req.getContextPath() + "/Perfil");
        }
    }

    /* Método: validarContrasena
       Propósito: Verificar que la contraseña ingresada cumpla los requisitos mínimos
       de seguridad: no estar vacía, coincidir con la confirmación, tener al menos
       8 caracteres, incluir letras y números.
       @param pass  → Texto con la nueva contraseña ingresada
       @param pass2 → Texto con la confirmación de la nueva contraseña
       @return String → Mensaje de error en texto si no es válida, o null si es correcta
    */
    private String validarContrasena(String pass, String pass2) {
        // Valida que la contraseña no esté vacía
        if (pass == null || pass.trim().isEmpty())
            return "La contraseña no puede estar vacía.";
        // Valida que ambas contraseñas coincidan exactamente
        if (!pass.equals(pass2))
            return "Las contraseñas no coinciden.";
        // Valida que la contraseña tenga al menos 8 caracteres
        if (pass.length() < 8)
            return "La contraseña debe tener al menos 8 caracteres.";
        // Valida que la contraseña incluya al menos una letra
        if (!pass.matches(".*[a-zA-Z].*"))
            return "La contraseña debe incluir al menos una letra.";
        // Valida que la contraseña incluya al menos un número
        if (!pass.matches(".*[0-9].*"))
            return "La contraseña debe incluir al menos un número.";
        // Si pasa todas las validaciones, retorna null (sin error)
        return null;
    }
}
