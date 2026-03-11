package com.smarthome.smarthome_budget.controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDateTime;

import com.smarthome.smarthome_budget.dao.UsuarioDao;
import com.smarthome.smarthome_budget.modelo.Usuario;
import com.smarthome.smarthome_budget.utils.Encriptador;

@WebServlet("/Seguridad")
public class SeguridadServlet extends HttpServlet {

    private final UsuarioDao usuarioDao = new UsuarioDao();
    private static final String BASE_MENU = "/public/modules/MenuPrincipal/";
    private static final String BASE_AUTH = "/public/modules/01_autenticacion/";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + BASE_AUTH + "04_iniciarSesion.jsp");
            return;
        }

        String accion = req.getParameter("accion");
        if (accion == null) accion = "";

        switch (accion) {
            case "formCambiarContrasena":
                req.getRequestDispatcher(BASE_MENU + "08_Cambiarcontrasena.jsp").forward(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/Perfil");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + BASE_AUTH + "04_iniciarSesion.jsp");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String accion = req.getParameter("accion");
        if (accion == null) accion = "";

        switch (accion) {

            // ── CAMBIAR CONTRASEÑA ──────────────────────────────────────────
            case "cambiarContrasena": {
                String password  = req.getParameter("password");
                String password2 = req.getParameter("password2");

                // Validaciones
                String errorMsg = validarContrasena(password, password2);
                if (errorMsg != null) {
                    req.setAttribute("error", errorMsg);
                    req.getRequestDispatcher(BASE_MENU + "08_Cambiarcontrasena.jsp").forward(req, resp);
                    return;
                }

                boolean ok = usuarioDao.actualizarClaveConHash(usuario.getIDUsuario(), password);
                if (ok) {
                    // Refrescar usuario en sesión para que tenga la nueva clave
                    Usuario actualizado = usuarioDao.obtenerPorId(usuario.getIDUsuario());
                    if (actualizado != null) session.setAttribute("usuario", actualizado);
                    req.getRequestDispatcher(BASE_MENU + "11_Contrasenaguardada.jsp").forward(req, resp);
                } else {
                    req.setAttribute("error", "No se pudo actualizar la contraseña. Intenta de nuevo.");
                    req.getRequestDispatcher(BASE_MENU + "08_Cambiarcontrasena.jsp").forward(req, resp);
                }
                break;
            }

            default:
                resp.sendRedirect(req.getContextPath() + "/Perfil");
        }
    }

    // ── Validación de contraseña ─────────────────────────────────────────────
    private String validarContrasena(String pass, String pass2) {
        if (pass == null || pass.trim().isEmpty())
            return "La contraseña no puede estar vacía.";
        if (!pass.equals(pass2))
            return "Las contraseñas no coinciden.";
        if (pass.length() < 8)
            return "La contraseña debe tener al menos 8 caracteres.";
        if (!pass.matches(".*[A-Z].*"))
            return "La contraseña debe incluir al menos una letra mayúscula.";
        if (!pass.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*"))
            return "La contraseña debe incluir al menos un símbolo especial.";
        return null;
    }
}
