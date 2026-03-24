package com.smarthome.smarthome_budget.controlador;

// Importación de clases para acceso a datos y manejo de sesión
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

/* Clase: servletLogin
   Propósito: Procesar el inicio de sesión del usuario.
   Valida las credenciales ingresadas, obtiene el hogar y rol asociado,
   crea la sesión activa y redirige al menú principal si todo es correcto.
   URL mapeada: /Login
*/
@WebServlet("/Login")
public class servletLogin extends HttpServlet {

    /* Método: doPost
       Propósito: Recibir el formulario de inicio de sesión (correo y contraseña),
       validar los campos, autenticar al usuario contra la base de datos y
       establecer los atributos de sesión necesarios para la aplicación.
       @param request  → Objeto que contiene los datos enviados por el formulario
       @param response → Objeto que permite redirigir o responder al cliente
    */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Establece la codificación UTF-8 para aceptar caracteres especiales del formulario
        request.setCharacterEncoding("UTF-8");

        // Variable de tipo texto que almacena el correo electrónico enviado por el usuario
        String email    = request.getParameter("correo");
        // Variable de tipo texto que almacena la contraseña enviada por el usuario
        String password = request.getParameter("contrasena");

        // Valida que los campos no estén vacíos o nulos antes de continuar
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            // Si algún campo está vacío, redirige de nuevo al login con parámetro de error
            response.sendRedirect(
                    request.getContextPath()
                            + "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=campos_vacios");
            return;
        }

        // Normaliza el correo: elimina espacios y lo convierte a minúsculas
        email = email.trim().toLowerCase();

        // Instancia del DAO de usuario para consultar la base de datos
        UsuarioDao usuarioDao = new UsuarioDao();
        // Objeto de tipo Usuario que almacena el resultado del intento de autenticación
        Usuario usuario = usuarioDao.login(email, password);

        // Si el usuario es null, las credenciales son incorrectas
        if (usuario == null) {
            // Redirige al login con error de credenciales inválidas
            response.sendRedirect(
                    request.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=invalido");
            return;
        }

        // Instancia del DAO para obtener el hogar y rol del usuario autenticado
        DetallesHogaresDao detallesDao = new DetallesHogaresDao();
        // Arreglo de enteros: posición 0 = ID del hogar, posición 1 = ID del rol
        int[] hogarYRol = detallesDao.obtenerPrimerHogarDeUsuario(usuario.getIDUsuario());

        // Si el arreglo es null, el usuario no tiene hogar registrado
        if (hogarYRol == null) {
            // Redirige al login indicando que no se encontró hogar asociado
            response.sendRedirect(
                    request.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sin_hogar");
            return;
        }

        // Se obtiene o crea la sesión activa del usuario
        HttpSession session = request.getSession();
        // Se guarda el objeto Usuario completo en la sesión con la clave "usuario"
        session.setAttribute("usuario", usuario);
        // Se guarda el ID del hogar (entero) en la sesión con la clave "idHogar"
        session.setAttribute("idHogar", hogarYRol[0]);
        // Se guarda el ID del rol (entero) en la sesión con la clave "idRol"
        session.setAttribute("idRol",   hogarYRol[1]);

        // Redirige al servlet /Menu para cargar los datos dinámicos del dashboard principal
        response.sendRedirect(request.getContextPath() + "/Menu");
    }
}
