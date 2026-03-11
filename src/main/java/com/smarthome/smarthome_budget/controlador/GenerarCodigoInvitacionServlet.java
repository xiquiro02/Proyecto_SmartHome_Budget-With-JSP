package com.smarthome.smarthome_budget.controlador;

import com.smarthome.smarthome_budget.dao.DetallesHogaresDao;
import com.smarthome.smarthome_budget.dao.CodigosInvitacionDao;
import com.smarthome.smarthome_budget.modelo.Usuario;
import com.smarthome.smarthome_budget.modelo.Roles;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/GenerarCodigoInvitacion")
public class GenerarCodigoInvitacionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_requerida");
            return;
        }

        String idRolParam = request.getParameter("idRol");

        if (idRolParam == null || idRolParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/public/modules/MenuPrincipal/01_menuPrincipal.jsp?error=rol_requerido");
            return;
        }

        try {
            int idRol = Integer.parseInt(idRolParam.trim());

            if (idRol < 1 || idRol > 3) {
                response.sendRedirect(request.getContextPath() + "/public/modules/MenuPrincipal/01_menuPrincipal.jsp?error=rol_invalido");
                return;
            }

            DetallesHogaresDao detallesDao = new DetallesHogaresDao();
            Roles rolUsuario = detallesDao.obtenerRolDeUsuarioEnHogar(usuario.getIDUsuario(), 1);

            if (rolUsuario == null || !"ADMINISTRADOR".equals(rolUsuario.getNombreRol())) {
                response.sendRedirect(request.getContextPath() + "/public/modules/MenuPrincipal/01_menuPrincipal.jsp?error=acceso_denegado");
                return;
            }

            CodigosInvitacionDao codigosDao = new CodigosInvitacionDao();
            String codigoGenerado = codigosDao.generarCodigo(1, idRol);

            if (codigoGenerado == null) {
                response.sendRedirect(request.getContextPath() + "/public/modules/MenuPrincipal/01_menuPrincipal.jsp?error=generar_codigo");
                return;
            }

            request.getSession().setAttribute("codigo_generado", codigoGenerado);
            request.getSession().setAttribute("rol_asignado", obtenerNombreRol(idRol));
            response.sendRedirect(request.getContextPath() + "/public/modules/MenuPrincipal/02_codigoGenerado.jsp");

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/public/modules/MenuPrincipal/01_menuPrincipal.jsp?error=rol_invalido");
        }
    }

    private String obtenerNombreRol(int idRol) {
        switch (idRol) {
            case 1: return "ADMINISTRADOR";
            case 2: return "COTITULAR";
            case 3: return "INVITADO";
            default: return "DESCONOCIDO";
        }
    }
}
