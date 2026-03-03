package com.smarthome.smarthome_budget.controlador;
import com.smarthome.smarthome_budget.dao.usuarioDao;
import java.io.IOException;
import java.util.UUID;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/RecuperarClave")
public class servletRecuperarContrasena extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        System.out.println("=== SERVLET RECUPERAR CONTRASEÑA ===");
        
        String emailDestino = request.getParameter("email");
        System.out.println("Email del formulario: " + emailDestino);
        
        String token = UUID.randomUUID().toString();
        System.out.println("Token UUID generado: " + token);

        usuarioDao dao = new usuarioDao();
        boolean emailExiste = dao.guardarToken(emailDestino, token);
        
        System.out.println("Resultado guardarToken: " + emailExiste);

        if (!emailExiste) {
            System.out.println("Redirigiendo a formulario con error=noexiste");
            response.sendRedirect("public/modules/01_autenticacion/07_SolicitarEmail.jsp?error=noexiste");
            return;
        }

        EmailService emailService = new EmailService();
        boolean enviado = emailService.enviarLinkRecuperacion(emailDestino, token, request.getContextPath());
        
        System.out.println("Email enviado: " + enviado);

        if (enviado) {
            System.out.println("Redirigiendo a ConfirmacionEmail.jsp");
            response.sendRedirect("public/modules/01_autenticacion/08_ConfirmacionEmail.jsp");
        } else {
            System.out.println("Redirigiendo a formulario con error=true");
            response.sendRedirect("public/modules/01_autenticacion/07_SolicitarEmail.jsp?error=true");
        }
    }
}
