package com.smarthome.smarthome_budget.controlador;

import com.smarthome.smarthome_budget.dao.UsuarioDao;
import java.io.IOException;
import java.util.UUID;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/RecuperarClave")
public class servletRecuperarContrasena extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String emailDestino = request.getParameter("email");

        if (emailDestino == null || emailDestino.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/07_SolicitarEmail.jsp?error=vacio");
            return;
        }

        emailDestino = emailDestino.trim().toLowerCase();
        String token = UUID.randomUUID().toString();

        UsuarioDao usuarioDao = new UsuarioDao();
        boolean tokenGuardado = usuarioDao.guardarToken(emailDestino, token);

        if (!tokenGuardado) {
            response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/07_SolicitarEmail.jsp?error=noexiste");
            return;
        }

        EmailService emailService = new EmailService();
        boolean emailEnviado = emailService.enviarLinkRecuperacion(emailDestino, token, request.getContextPath());

        if (emailEnviado) {
            response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/08_ConfirmacionEmail.jsp");
        } else {
            response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/07_SolicitarEmail.jsp?error=envio");
        }
    }
}
