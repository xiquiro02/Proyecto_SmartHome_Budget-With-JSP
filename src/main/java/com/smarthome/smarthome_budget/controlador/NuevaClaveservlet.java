package com.smarthome.smarthome_budget.controlador;

import com.smarthome.smarthome_budget.dao.UsuarioDao;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/NuevaClave")
public class NuevaClaveservlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String token = request.getParameter("token");

        if (token == null || token.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/07_SolicitarEmail.jsp?error=tokeninvalido");
            return;
        }

        UsuarioDao usuarioDao = new UsuarioDao();
        String correo = usuarioDao.obtenerCorreoPorToken(token);

        if (correo == null) {
            response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/07_SolicitarEmail.jsp?error=tokenexpirado");
            return;
        }

        request.getSession().setAttribute("email_recuperacion", correo);
        response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/09_NuevaClave.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nuevaClave = request.getParameter("nueva_clave");
        String correo = (String) request.getSession().getAttribute("email_recuperacion");

        if (correo == null) {
            response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/07_SolicitarEmail.jsp?error=sesionexpirada");
            return;
        }

        if (nuevaClave == null || nuevaClave.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/09_NuevaClave.jsp?error=vacia");
            return;
        }

        UsuarioDao usuarioDao = new UsuarioDao();
        boolean actualizado = usuarioDao.actualizarClave(correo, nuevaClave);

        if (actualizado) {
            request.getSession().removeAttribute("email_recuperacion");
            response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp?exito=true");
        } else {
            response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/09_NuevaClave.jsp?error=true");
        }
    }
}