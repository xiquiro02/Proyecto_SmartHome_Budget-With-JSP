package com.smarthome.smarthome_budget.controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import com.smarthome.smarthome_budget.dao.UsuarioDao;
import com.smarthome.smarthome_budget.modelo.Usuario;

@WebServlet("/Perfil")
@MultipartConfig(maxFileSize = 2 * 1024 * 1024) // 2 MB
public class PerfilServlet extends HttpServlet {

    private final UsuarioDao usuarioDao = new UsuarioDao();
    private static final String BASE = "/public/modules/01_autenticacion/";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + BASE + "04_iniciarSesion.jsp");
            return;
        }

        String accion = req.getParameter("accion");
        if (accion == null) accion = "";

        switch (accion) {
            case "editar":
                req.getRequestDispatcher(BASE + "11_EditarPerfil.jsp").forward(req, resp);
                break;
            case "cerrarSesion":
                req.getRequestDispatcher(BASE + "13_Cerrar_Sesion.jsp").forward(req, resp);
                break;
            case "eliminarCuenta":
                req.getRequestDispatcher(BASE + "14_EliminarCuenta.jsp").forward(req, resp);
                break;
            default:
                req.getRequestDispatcher(BASE + "10_MiPerfil.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + BASE + "04_iniciarSesion.jsp");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String accion = req.getParameter("accion");
        if (accion == null) accion = "";

        switch (accion) {

            // ── GUARDAR EDICIÓN DE PERFIL ───────────────────────────────────
            case "guardarEdicion": {
                String correo    = req.getParameter("correo");
                String telefono  = req.getParameter("telefono");
                String username  = req.getParameter("Usuario");

                // Manejo de foto de perfil
                String rutaFoto = null;
                try {
                    Part foto = req.getPart("foto");
                    if (foto != null && foto.getSize() > 0) {
                        String contentType = foto.getContentType();
                        if (contentType != null && (contentType.contains("png") || contentType.contains("jpeg") || contentType.contains("jpg"))) {
                            String fileName   = UUID.randomUUID() + "_" + Paths.get(foto.getSubmittedFileName()).getFileName().toString();
                            String uploadPath = getServletContext().getRealPath("/asset/fotos/");
                            Files.createDirectories(Paths.get(uploadPath));
                            foto.write(uploadPath + File.separator + fileName);
                            rutaFoto = "/asset/fotos/" + fileName;
                        }
                    }
                } catch (Exception e) {
                    // foto no cargada, continuar sin foto
                }

                boolean ok = usuarioDao.actualizarPerfil(usuario.getIDUsuario(), correo, telefono, username, rutaFoto);
                if (ok) {
                    // Refrescar usuario en sesión
                    Usuario actualizado = usuarioDao.obtenerPorId(usuario.getIDUsuario());
                    if (actualizado != null) session.setAttribute("usuario", actualizado);
                    req.getRequestDispatcher(BASE + "12_Confirmar-cambio.jsp").forward(req, resp);
                } else {
                    req.setAttribute("error", "No se pudo actualizar el perfil. Intenta de nuevo.");
                    req.getRequestDispatcher(BASE + "11_EditarPerfil.jsp").forward(req, resp);
                }
                break;
            }

            // ── CONFIRMAR CIERRE DE SESIÓN ──────────────────────────────────
            case "confirmarCerrarSesion": {
                session.invalidate();
                resp.sendRedirect(req.getContextPath() + BASE + "01_principal.jsp");
                break;
            }

            // ── ELIMINAR CUENTA ─────────────────────────────────────────────
            case "confirmarEliminar": {
                String password = req.getParameter("password");
                com.smarthome.smarthome_budget.utils.Encriptador enc = null;
                boolean passOk = com.smarthome.smarthome_budget.utils.Encriptador.verificar(password, usuario.getContrasenaUsuario());
                if (passOk) {
                    boolean eliminado = usuarioDao.eliminarUsuario(usuario.getIDUsuario());
                    if (eliminado) {
                        session.invalidate();
                        req.getRequestDispatcher(BASE + "15_ConfirmarEliminacion.jsp").forward(req, resp);
                        return;
                    }
                }
                req.setAttribute("error", "Contraseña incorrecta o no se pudo eliminar la cuenta.");
                req.getRequestDispatcher(BASE + "14_EliminarCuenta.jsp").forward(req, resp);
                break;
            }

            default:
                resp.sendRedirect(req.getContextPath() + "/Perfil");
        }
    }
}
