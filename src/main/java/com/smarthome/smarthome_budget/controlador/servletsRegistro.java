package com.smarthome.smarthome_budget.controlador;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.net.URLEncoder;

import com.smarthome.smarthome_budget.dao.usuarioDao;
import com.smarthome.smarthome_budget.modelo.usuario;

@WebServlet("/RegistroServlet")

public class servletsRegistro extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nombre = request.getParameter("nombre").trim();
        String apellido1 = request.getParameter("apellido1").trim();
        String apellido2 = request.getParameter("apellido2") != null ? request.getParameter("apellido2").trim() : "";
        String correo = request.getParameter("correo").trim().toLowerCase();
        String telefono = request.getParameter("telefono").trim();
        String contrasena = request.getParameter("contrasena") != null ? request.getParameter("contrasena") : "";
        String confirmarContrasena = request.getParameter("confirmarContrasena") != null
                ? request.getParameter("confirmarContrasena")
                : "";

        if (nombre.isEmpty() || apellido1.isEmpty() || correo.isEmpty() || telefono.isEmpty() || contrasena.isEmpty()
                || confirmarContrasena.isEmpty()) {
            response.sendRedirect(request.getContextPath()
                    + "/public/modules/01_autenticacion/02_registrarse.jsp?error=campos_vacios");
            return;
        }

        if (!contrasena.equals(confirmarContrasena)) {
            response.sendRedirect(request.getContextPath()
                    + "/public/modules/01_autenticacion/02_registrarse.jsp?error=contrasena_no_coincide");
            return;
        }

        usuarioDao dao = new usuarioDao();

        if (dao.correoExiste(correo)) {
            response.sendRedirect(request.getContextPath()
                    + "/public/modules/01_autenticacion/02_registrarse.jsp?error=correo_ya_registrado");
            return;
        }

        usuario nuevoUsuario = new usuario(nombre, apellido1, apellido2, correo, telefono, contrasena);
        boolean registroExitoso = dao.registrarUsuario(nuevoUsuario);

        if (registroExitoso) {
            response.sendRedirect(request.getContextPath()
                    + "/public/modules/01_autenticacion/03_ConfirmacionRegistro.jsp?nombre="
                    + URLEncoder.encode(nombre, "UTF-8"));
        } else {
            response.sendRedirect(request.getContextPath()
                    + "/public/modules/01_autenticacion/02_registrarse.jsp?error=error_db");
        }

    }
}
