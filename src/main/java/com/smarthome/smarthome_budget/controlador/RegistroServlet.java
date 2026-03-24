package com.smarthome.smarthome_budget.controlador;

// Importación de clases DAO, modelo y utilitarios necesarios para el registro
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

/* Clase: RegistroServlet
   Propósito: Gestionar el registro de nuevos usuarios en la aplicación.
   Si el usuario ingresa un código de invitación válido, se une al hogar existente
   con el rol asignado por el código. Si no ingresa código, se crea un nuevo hogar
   y el usuario queda como Administrador (rol 1).
   URL mapeada: /Registro
*/
@WebServlet("/Registro")
public class RegistroServlet extends HttpServlet {

    /* Método: doPost
       Propósito: Recibir y procesar el formulario de registro de nuevos usuarios.
       Valida que los campos obligatorios no estén vacíos, verifica duplicados de
       correo y documento, construye el objeto Usuario y lo persiste en la base de datos.
       Luego crea el hogar o vincula al usuario al hogar del código de invitación.
       @param request  → Objeto que contiene los datos del formulario de registro
       @param response → Objeto que permite redirigir al resultado correspondiente
    */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Establece la codificación UTF-8 para aceptar caracteres especiales
        request.setCharacterEncoding("UTF-8");

        // Variable de tipo texto que almacena el número de documento del usuario
        String documento       = request.getParameter("documento");
        // Variable de tipo texto que almacena el nombre del usuario
        String nombre          = request.getParameter("nombre");
        // Variable de tipo texto que almacena el primer apellido del usuario
        String primerApellido  = request.getParameter("apellido1");
        // Variable de tipo texto que almacena el segundo apellido (puede ser vacío)
        String segundoApellido = request.getParameter("apellido2");
        // Variable de tipo texto que almacena el correo electrónico del usuario
        String correo          = request.getParameter("correo");
        // Variable de tipo texto que almacena el número de teléfono del usuario
        String telefono        = request.getParameter("telefono");
        // Variable de tipo texto que almacena la contraseña ingresada
        String contrasena      = request.getParameter("contrasena");
        // Variable de tipo texto que almacena el código de invitación (puede ser nulo)
        String codigoInvitacion = request.getParameter("codigoInvitacion");

        // Valida que los campos obligatorios no estén vacíos
        if (estaVacio(documento) || estaVacio(nombre) || estaVacio(primerApellido) ||
            estaVacio(correo) || estaVacio(telefono) || estaVacio(contrasena)) {
            // Si falta algún campo, redirige con error de campos vacíos
            response.sendRedirect(request.getContextPath() +
                "/public/modules/01_autenticacion/02_registrarse.jsp?error=campos_vacios");
            return;
        }

        // Valida el código de invitación si fue proporcionado, antes del flujo principal
        if (codigoInvitacion != null && !codigoInvitacion.trim().isEmpty()) {
            try {
                // Instancia del DAO de códigos de invitación para validar el código
                CodigosInvitacionDao codigosDao = new CodigosInvitacionDao();
                // Objeto que contiene los datos del código si es válido, o null si no lo es
                CodigosInvitacion codigoValido = codigosDao.validarCodigo(codigoInvitacion.trim());
                if (codigoValido == null) {
                    // El código no existe, está vencido o ya fue usado
                    response.sendRedirect(request.getContextPath() +
                        "/public/modules/01_autenticacion/02_registrarse.jsp?error=codigo_invalido");
                    return;
                }
            } catch (Exception e) {
                // Error al consultar el código: redirige con error
                response.sendRedirect(request.getContextPath() +
                    "/public/modules/01_autenticacion/02_registrarse.jsp?error=codigo_invalido");
                return;
            }
        }

        try {
            // Instancia del DAO de usuario para operaciones sobre la tabla Usuario
            UsuarioDao usuarioDao = new UsuarioDao();

            // Verifica si ya existe un usuario registrado con el mismo correo
            if (usuarioDao.correoExiste(correo.trim().toLowerCase())) {
                response.sendRedirect(request.getContextPath() +
                    "/public/modules/01_autenticacion/02_registrarse.jsp?error=correo_existe");
                return;
            }

            // Verifica si ya existe un usuario registrado con el mismo número de documento
            if (usuarioDao.documentoExiste(documento.trim())) {
                response.sendRedirect(request.getContextPath() +
                    "/public/modules/01_autenticacion/02_registrarse.jsp?error=documento_existe");
                return;
            }

            // Construye el objeto Usuario con los datos recibidos del formulario
            Usuario usuario = new Usuario();
            // Asigna el número de documento al objeto Usuario
            usuario.setDocumento(documento.trim());
            // Asigna el nombre de usuario (primer nombre)
            usuario.setNombreUsuario(nombre.trim());
            // Asigna el primer apellido (setPrimerApellido es alias de setApellido en el modelo)
            usuario.setPrimerApellido(primerApellido.trim());
            // Asigna el segundo apellido; si es nulo se guarda vacío
            usuario.setSegundoApellido(segundoApellido != null ? segundoApellido.trim() : "");
            // Asigna el correo en minúsculas y sin espacios
            usuario.setCorreo(correo.trim().toLowerCase());
            // Asigna el número de teléfono
            usuario.setTelefono(telefono.trim());
            // Asigna la contraseña (el DAO se encarga del hash internamente)
            usuario.setContrasenaUsuario(contrasena);

            // Registra el usuario en la base de datos y obtiene el ID generado
            // Variable entera que almacena el ID del nuevo usuario, o -1 si falló
            int idUsuario = usuarioDao.registrarUsuario(usuario);

            if (idUsuario == -1) {
                // El registro falló en la base de datos
                response.sendRedirect(request.getContextPath() +
                    "/public/modules/01_autenticacion/02_registrarse.jsp?error=registro_fallido");
                return;
            }

            // Determina el flujo según si hay código de invitación o no
            if (codigoInvitacion == null || codigoInvitacion.trim().isEmpty()) {
                // Sin código: crea un hogar nuevo y asigna al usuario como Administrador
                registroSinCodigo(idUsuario, nombre, request, response);
            } else {
                // Con código: vincula al usuario al hogar y rol del código de invitación
                registroConCodigo(idUsuario, codigoInvitacion.trim(), nombre, request, response);
            }

        } catch (Exception e) {
            // Captura errores inesperados de base de datos u otros
            System.err.println("Error inesperado en registro: " + e.getMessage());
            response.sendRedirect(request.getContextPath() +
                "/public/modules/01_autenticacion/02_registrarse.jsp?error=error_db");
        }
    }

    /* Método: registroSinCodigo
       Propósito: Crear un hogar nuevo y asignar al usuario recién registrado
       como Administrador (rol 1) de ese hogar.
       @param idUsuario → Entero con el ID del usuario recién creado en la BD
       @param nombre    → Texto con el nombre del usuario para nombrar el hogar
       @param request   → Objeto de la petición HTTP
       @param response  → Objeto de respuesta HTTP para redirigir
    */
    private void registroSinCodigo(int idUsuario, String nombre,
                                   HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Instancia del DAO de hogar para crear un nuevo hogar
        HogarDao hogarDao = new HogarDao();
        // Variable entera que almacena el ID del hogar creado, o -1 si falló
        int idHogar = hogarDao.crearHogar("Hogar de " + nombre);

        if (idHogar == -1) {
            // Si el hogar no se pudo crear, redirige con error
            response.sendRedirect(request.getContextPath() +
                "/public/modules/01_autenticacion/02_registrarse.jsp?error=crear_hogar");
            return;
        }

        // Instancia del DAO de detalles de hogar para vincular usuario con hogar y rol
        DetallesHogaresDao detallesDao = new DetallesHogaresDao();
        // Variable booleana que indica si la relación usuario-hogar-rol fue creada
        boolean relacionCreada = detallesDao.crearRelacion(idUsuario, idHogar, 1); // rol 1 = Administrador

        if (!relacionCreada) {
            // Si la relación no se pudo crear, redirige con error
            response.sendRedirect(request.getContextPath() +
                "/public/modules/01_autenticacion/02_registrarse.jsp?error=crear_relacion");
            return;
        }

        // Registro exitoso: redirige a la confirmación con el nombre del usuario
        response.sendRedirect(request.getContextPath() +
            "/public/modules/01_autenticacion/03_ConfirmacionRegistro.jsp?nombre=" +
            URLEncoder.encode(nombre, "UTF-8"));
    }

    /* Método: registroConCodigo
       Propósito: Vincular al usuario recién registrado al hogar y rol definidos
       en el código de invitación, y marcar dicho código como ya utilizado.
       @param idUsuario → Entero con el ID del usuario recién creado
       @param codigo    → Texto con el código de invitación validado
       @param nombre    → Texto con el nombre del usuario para la confirmación
       @param request   → Objeto de la petición HTTP
       @param response  → Objeto de respuesta HTTP para redirigir
    */
    private void registroConCodigo(int idUsuario, String codigo, String nombre,
                                   HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Instancia del DAO de códigos de invitación para obtener y actualizar el código
        CodigosInvitacionDao codigosDao = new CodigosInvitacionDao();
        // Objeto que contiene los datos del código: ID hogar e ID rol asignados
        CodigosInvitacion codigoValido = codigosDao.validarCodigo(codigo);

        // Instancia del DAO de detalles de hogar para crear la relación usuario-hogar-rol
        DetallesHogaresDao detallesDao = new DetallesHogaresDao();
        // Variable booleana que indica si la relación fue creada correctamente
        boolean relacionCreada = detallesDao.crearRelacion(
            idUsuario,
            codigoValido.getIDHogar(),  // ID del hogar al que pertenece el código
            codigoValido.getIDRol()     // ID del rol asignado por el administrador
        );

        if (!relacionCreada) {
            // Si la relación no se pudo crear, redirige con error
            response.sendRedirect(request.getContextPath() +
                "/public/modules/01_autenticacion/02_registrarse.jsp?error=crear_relacion");
            return;
        }

        // Marca el código como usado para que no pueda ser reutilizado
        codigosDao.marcarComoUsado(codigo);

        // Registro exitoso: redirige a la confirmación con el nombre del usuario
        response.sendRedirect(request.getContextPath() +
            "/public/modules/01_autenticacion/03_ConfirmacionRegistro.jsp?nombre=" +
            URLEncoder.encode(nombre, "UTF-8"));
    }

    /* Método: estaVacio
       Propósito: Verificar si una cadena de texto es nula o está vacía después de
       eliminar espacios en blanco, para validar campos del formulario.
       @param s → Texto de tipo String a evaluar
       @return boolean → Retorna true si el texto es nulo o vacío, false si tiene contenido
    */
    private boolean estaVacio(String s) { return s == null || s.trim().isEmpty(); }
}
