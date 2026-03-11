package com.smarthome.smarthome_budget.controlador;

import com.smarthome.smarthome_budget.dao.UsuarioDao;
import com.smarthome.smarthome_budget.dao.DetallesHogaresDao;
import com.smarthome.smarthome_budget.modelo.Usuario;
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

        request.setCharacterEncoding("UTF-8");

        String email    = request.getParameter("correo");
        String password = request.getParameter("contrasena");

        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            response.sendRedirect(
                    request.getContextPath()
                            + "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=campos_vacios");
            return;
        }

        email = email.trim().toLowerCase();

        UsuarioDao usuarioDao = new UsuarioDao();
        Usuario usuario = usuarioDao.login(email, password);

        if (usuario == null) {
            response.sendRedirect(
                    request.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=invalido");
            return;
        }

        DetallesHogaresDao detallesDao = new DetallesHogaresDao();
        int[] hogarYRol = detallesDao.obtenerPrimerHogarDeUsuario(usuario.getIDUsuario());

        if (hogarYRol == null) {
            response.sendRedirect(
                    request.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sin_hogar");
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("usuario", usuario);
        session.setAttribute("idHogar", hogarYRol[0]);
        session.setAttribute("idRol",   hogarYRol[1]);

        // CORRECCIÓN: redirigir al servlet /Menu para cargar datos dinámicos del dashboard
        response.sendRedirect(request.getContextPath() + "/Menu");
    }
}
