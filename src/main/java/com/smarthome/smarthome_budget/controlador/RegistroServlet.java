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

        String nombre = request.getParameter("nombre");
        String primerApellido = request.getParameter("apellido1");
        String segundoApellido = request.getParameter("apellido2");
        String correo = request.getParameter("correo");
        String telefono = request.getParameter("telefono");
        String contrasena = request.getParameter("contrasena");
        String codigoInvitacion = request.getParameter("codigoInvitacion");

        if (nombre == null || nombre.trim().isEmpty() ||
            primerApellido == null || primerApellido.trim().isEmpty() ||
            correo == null || correo.trim().isEmpty() ||
            telefono == null || telefono.trim().isEmpty() ||
            contrasena == null || contrasena.trim().isEmpty()) {
            
            response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/02_registrarse.jsp?error=campos_vacios");
            return;
        }

        // Validación de código de invitación FUERA del try-catch
        if (codigoInvitacion != null && !codigoInvitacion.trim().isEmpty()) {
            try {
                CodigosInvitacionDao codigosDao = new CodigosInvitacionDao();
                CodigosInvitacion codigoValido = codigosDao.validarCodigo(codigoInvitacion.trim());

                if (codigoValido == null) {
                    response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/02_registrarse.jsp?error=codigo_invalido");
                    return; // CORTAR EJECUCIÓN - NO entrar al try-catch general
                }
            } catch (Exception e) {
                // Si hay error en la validación del código, mostrar error específico
                response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/02_registrarse.jsp?error=codigo_invalido");
                return;
            }
        }

        try {
            UsuarioDao usuarioDao = new UsuarioDao();
            
            if (usuarioDao.correoExiste(correo.trim().toLowerCase())) {
                response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/02_registrarse.jsp?error=correo_existe");
                return;
            }

            Usuario usuario = new Usuario();
            usuario.setNombreUsuario(nombre.trim());
            usuario.setPrimerApellido(primerApellido.trim());
            usuario.setSegundoApellido(segundoApellido != null ? segundoApellido.trim() : "");
            usuario.setCorreo(correo.trim().toLowerCase());
            usuario.setTelefono(telefono.trim());
            usuario.setContrasenaUsuario(contrasena);

            int idUsuario = usuarioDao.registrarUsuario(usuario);

            if (idUsuario == -1) {
                response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/02_registrarse.jsp?error=registro_fallido");
                return;
            }

            if (codigoInvitacion == null || codigoInvitacion.trim().isEmpty()) {
                registroSinCodigo(idUsuario, nombre, request, response);
            } else {
                registroConCodigo(idUsuario, codigoInvitacion.trim(), nombre, request, response);
            }

        } catch (Exception e) {
            // Catch general solo para errores inesperados del flujo principal
            response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/02_registrarse.jsp?error=error_db");
        }
    }

    private void registroSinCodigo(int idUsuario, String nombre, HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        HogarDao hogarDao = new HogarDao();
        int idHogar = hogarDao.crearHogar("Hogar de " + nombre);

        if (idHogar == -1) {
            response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/02_registrarse.jsp?error=crear_hogar");
            return;
        }

        DetallesHogaresDao detallesDao = new DetallesHogaresDao();
        boolean relacionCreada = detallesDao.crearRelacion(idUsuario, idHogar, 1);

        if (!relacionCreada) {
            response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/02_registrarse.jsp?error=crear_relacion");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/03_ConfirmacionRegistro.jsp?nombre=" + URLEncoder.encode(nombre, "UTF-8"));
    }

    private void registroConCodigo(int idUsuario, String codigo, String nombre, HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        CodigosInvitacionDao codigosDao = new CodigosInvitacionDao();
        // El código ya fue validado en doPost, aquí solo obtenemos los datos
        CodigosInvitacion codigoValido = codigosDao.validarCodigo(codigo); // Para obtener IDHogar e IDRol

        DetallesHogaresDao detallesDao = new DetallesHogaresDao();
        boolean relacionCreada = detallesDao.crearRelacion(
            idUsuario, 
            codigoValido.getIDHogar(), 
            codigoValido.getIDRol()
        );

        if (!relacionCreada) {
            response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/02_registrarse.jsp?error=crear_relacion");
            return;
        }

        boolean codigoMarcado = codigosDao.marcarComoUsado(codigo);

        if (!codigoMarcado) {
            response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/02_registrarse.jsp?error=marcar_codigo");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/03_ConfirmacionRegistro.jsp?nombre=" + URLEncoder.encode(nombre, "UTF-8"));
    }
}
