package com.smarthome.smarthome_budget.controlador;

import com.smarthome.smarthome_budget.dao.usuarioDao;
import com.smarthome.smarthome_budget.modelo.usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/Login")
public class servletLogin extends HttpServlet {

    @Override

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("correo");
        String password = request.getParameter("contrasena");

        usuarioDao dao = new usuarioDao();
        usuario user = dao.login(email, password);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("usuario", user);
            response.sendRedirect(request.getContextPath() + "/public/modules/MenuPrincipal/01_menuPrincipal.jsp");
        } else {
            response.sendRedirect(
                    request.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=invalido");
        }
    }
}
