package com.smarthome.smarthome_budget.controlador;

// Importación de clases necesarias para el proceso de recuperación de contraseña
import com.smarthome.smarthome_budget.dao.UsuarioDao;
import java.io.IOException;
import java.util.UUID;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/* Clase: servletRecuperarContrasena
   Propósito: Gestionar el inicio del proceso de recuperación de contraseña.
   Recibe el correo electrónico del usuario, genera un token único de seguridad,
   lo guarda en la base de datos y envía al usuario un correo con el enlace
   para restablecer su contraseña.
   URL mapeada: /RecuperarClave
*/
@WebServlet("/RecuperarClave")
public class servletRecuperarContrasena extends HttpServlet {

    /* Método: doPost
       Propósito: Recibir el correo electrónico del usuario, generar un token UUID
       único de seguridad, guardarlo en la base de datos asociado al correo y
       enviar el enlace de recuperación por correo electrónico.
       @param request  → Objeto con el campo "email" del formulario de recuperación
       @param response → Objeto para redirigir al resultado (confirmación o error)
    */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Establece la codificación UTF-8 para aceptar caracteres especiales
        request.setCharacterEncoding("UTF-8");

        // Variable de tipo texto que almacena el correo electrónico ingresado por el usuario
        String emailDestino = request.getParameter("email");

        // Valida que el campo de correo no esté vacío ni sea nulo
        if (emailDestino == null || emailDestino.trim().isEmpty()) {
            // Si viene vacío, redirige al formulario con error de campo vacío
            response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/07_SolicitarEmail.jsp?error=vacio");
            return;
        }

        // Normaliza el correo: elimina espacios y convierte a minúsculas
        emailDestino = emailDestino.trim().toLowerCase();
        // Variable de tipo texto que almacena el token UUID generado para este proceso de recuperación
        String token = UUID.randomUUID().toString();

        // Instancia del DAO de usuario para guardar el token y verificar si el correo existe
        UsuarioDao usuarioDao = new UsuarioDao();
        // Variable booleana que indica si el token fue guardado correctamente (true = correo existe en BD)
        boolean tokenGuardado = usuarioDao.guardarToken(emailDestino, token);

        if (!tokenGuardado) {
            // Si el correo no existe en la base de datos, redirige con error
            response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/07_SolicitarEmail.jsp?error=noexiste");
            return;
        }

        // Instancia del servicio de correo para enviar el enlace de recuperación
        EmailService emailService = new EmailService();
        // Variable booleana que indica si el correo fue enviado correctamente al destinatario
        boolean emailEnviado = emailService.enviarLinkRecuperacion(emailDestino, token, request.getContextPath());

        if (emailEnviado) {
            // Correo enviado exitosamente: redirige a la página de confirmación
            response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/08_ConfirmacionEmail.jsp");
        } else {
            // Si falló el envío del correo, redirige con error de envío
            response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/07_SolicitarEmail.jsp?error=envio");
        }
    }
}
