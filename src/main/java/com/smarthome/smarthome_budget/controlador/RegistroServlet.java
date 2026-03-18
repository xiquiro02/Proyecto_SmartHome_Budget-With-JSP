package com.smarthome.smarthome_budget.controlador;

import com.smarthome.smarthome_budget.dao.UsuarioDao;
import com.smarthome.smarthome_budget.dao.HogarDao;
import com.smarthome.smarthome_budget.dao.DetallesHogaresDao;
import com.smarthome.smarthome_budget.dao.CodigosInvitacionDao;
import com.smarthome.smarthome_budget.modelo.Usuario;
import com.smarthome.smarthome_budget.modelo.CodigosInvitacion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@WebServlet("/Registro")
public class RegistroServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String documento       = request.getParameter("documento");
        String nombre          = request.getParameter("nombre");
        String primerApellido  = request.getParameter("apellido1");
        String segundoApellido = request.getParameter("apellido2");
        String correo          = request.getParameter("correo");
        String telefono        = request.getParameter("telefono");
        String contrasena      = request.getParameter("contrasena");
        String codigoInvitacion = request.getParameter("codigoInvitacion");

        if (estaVacio(documento) || estaVacio(nombre) || estaVacio(primerApellido) ||
            estaVacio(correo) || estaVacio(telefono) || estaVacio(contrasena)) {
            response.sendRedirect(request.getContextPath() +
                "/public/modules/01_autenticacion/02_registrarse.jsp?error=campos_vacios");
            return;
        }

        // Validar código de invitación antes del flujo principal
        if (codigoInvitacion != null && !codigoInvitacion.trim().isEmpty()) {
            try {
                CodigosInvitacionDao codigosDao = new CodigosInvitacionDao();
                CodigosInvitacion codigoValido = codigosDao.validarCodigo(codigoInvitacion.trim());
                if (codigoValido == null) {
                    response.sendRedirect(request.getContextPath() +
                        "/public/modules/01_autenticacion/02_registrarse.jsp?error=codigo_invalido");
                    return;
                }
            } catch (Exception e) {
                response.sendRedirect(request.getContextPath() +
                    "/public/modules/01_autenticacion/02_registrarse.jsp?error=codigo_invalido");
                return;
            }
        }

        try {
            UsuarioDao usuarioDao = new UsuarioDao();

            // Verificar correo duplicado
            if (usuarioDao.correoExiste(correo.trim().toLowerCase())) {
                response.sendRedirect(request.getContextPath() +
                    "/public/modules/01_autenticacion/02_registrarse.jsp?error=correo_existe");
                return;
            }

            // Verificar documento duplicado
            if (usuarioDao.documentoExiste(documento.trim())) {
                response.sendRedirect(request.getContextPath() +
                    "/public/modules/01_autenticacion/02_registrarse.jsp?error=documento_existe");
                return;
            }

            // Construir objeto Usuario con todos los campos de BD
            Usuario usuario = new Usuario();
            usuario.setDocumento(documento.trim());
            usuario.setNombreUsuario(nombre.trim());
            // setPrimerApellido es alias de setApellido en el modelo
            usuario.setPrimerApellido(primerApellido.trim());
            // setSegundoApellido concatena si no está vacío
            usuario.setSegundoApellido(segundoApellido != null ? segundoApellido.trim() : "");
            usuario.setCorreo(correo.trim().toLowerCase());
            usuario.setTelefono(telefono.trim());
            usuario.setContrasenaUsuario(contrasena);

            int idUsuario = usuarioDao.registrarUsuario(usuario);

            if (idUsuario == -1) {
                response.sendRedirect(request.getContextPath() +
                    "/public/modules/01_autenticacion/02_registrarse.jsp?error=registro_fallido");
                return;
            }

            if (codigoInvitacion == null || codigoInvitacion.trim().isEmpty()) {
                registroSinCodigo(idUsuario, nombre, request, response);
            } else {
                registroConCodigo(idUsuario, codigoInvitacion.trim(), nombre, request, response);
            }

        } catch (Exception e) {
            System.err.println("Error inesperado en registro: " + e.getMessage());
            response.sendRedirect(request.getContextPath() +
                "/public/modules/01_autenticacion/02_registrarse.jsp?error=error_db");
        }
    }

    private void registroSinCodigo(int idUsuario, String nombre,
                                   HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HogarDao hogarDao = new HogarDao();
        int idHogar = hogarDao.crearHogar("Hogar de " + nombre);

        if (idHogar == -1) {
            response.sendRedirect(request.getContextPath() +
                "/public/modules/01_autenticacion/02_registrarse.jsp?error=crear_hogar");
            return;
        }

        DetallesHogaresDao detallesDao = new DetallesHogaresDao();
        boolean relacionCreada = detallesDao.crearRelacion(idUsuario, idHogar, 1); // rol 1 = Administrador

        if (!relacionCreada) {
            response.sendRedirect(request.getContextPath() +
                "/public/modules/01_autenticacion/02_registrarse.jsp?error=crear_relacion");
            return;
        }

        response.sendRedirect(request.getContextPath() +
            "/public/modules/01_autenticacion/03_ConfirmacionRegistro.jsp?nombre=" +
            URLEncoder.encode(nombre, "UTF-8"));
    }

    private void registroConCodigo(int idUsuario, String codigo, String nombre,
                                   HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        CodigosInvitacionDao codigosDao = new CodigosInvitacionDao();
        CodigosInvitacion codigoValido = codigosDao.validarCodigo(codigo);

        DetallesHogaresDao detallesDao = new DetallesHogaresDao();
        boolean relacionCreada = detallesDao.crearRelacion(
            idUsuario,
            codigoValido.getIDHogar(),
            codigoValido.getIDRol()
        );

        if (!relacionCreada) {
            response.sendRedirect(request.getContextPath() +
                "/public/modules/01_autenticacion/02_registrarse.jsp?error=crear_relacion");
            return;
        }

        codigosDao.marcarComoUsado(codigo);

        response.sendRedirect(request.getContextPath() +
            "/public/modules/01_autenticacion/03_ConfirmacionRegistro.jsp?nombre=" +
            URLEncoder.encode(nombre, "UTF-8"));
    }

    private boolean estaVacio(String s) { return s == null || s.trim().isEmpty(); }
}
