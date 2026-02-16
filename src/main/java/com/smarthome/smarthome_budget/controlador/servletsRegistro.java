package com.smarthome.smarthome_budget.controlador;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import java.io.IOException;

import com.smarthome.smarthome_budget.dao.usuarioDao;
import com.smarthome.smarthome_budget.modelo.usuario;

@WebServlet("/RegistroServlet")

public class servletsRegistro extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String apellido1 = request.getParameter("apellido1");
        String apellido2 = request.getParameter("apellido2");
        String correo = request.getParameter("correo");
        String telefono = request.getParameter("telefono");
        String contrasena = request.getParameter("contrasena");
        String confirmarContrasena = request.getParameter("confirmarContrasena");

        if (contrasena == null || !contrasena.equals(confirmarContrasena)) {
            response.sendRedirect(request.getContextPath()
                    + "/public/modules/01_autenticacion/02_registrarse.jsp?error=contrasena_no_coincide");
            return;
        }
        usuario nuevoUsuario = new usuario(nombre, apellido1, apellido2, correo, telefono, contrasena);

        usuarioDao dao = new usuarioDao();
        boolean registroExitoso = dao.registrarUsuario(nuevoUsuario);

        if (registroExitoso) {
            response.sendRedirect(request.getContextPath()
                    + "/public/modules/01_autenticacion/03_ConfirmacionRegistro.jsp?nombre=" + nombre);
        } else {
            response.sendRedirect(request.getContextPath()
                    + "/public/modules/01_autenticacion/02_registrarse.jsp?error=error_db");
        }

    }
}
