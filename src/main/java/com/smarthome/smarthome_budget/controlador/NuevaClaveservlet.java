package com.smarthome.smarthome_budget.controlador;

// Importación de clases DAO necesarias para validar el token y actualizar la contraseña
import com.smarthome.smarthome_budget.dao.TokensDao;
import com.smarthome.smarthome_budget.dao.UsuarioDao;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/* Clase: NuevaClaveservlet
   Propósito: Gestionar el restablecimiento de contraseña a través del enlace
   enviado por correo electrónico. Valida el token de seguridad recibido por URL,
   guarda el correo en sesión y procesa el formulario con la nueva contraseña.
   URL mapeada: /NuevaClave
*/
@WebServlet("/NuevaClave")
public class NuevaClaveservlet extends HttpServlet {

    /* Método: doGet
       Propósito: Validar el token de restablecimiento de contraseña recibido
       por parámetro en la URL del enlace enviado al correo del usuario.
       Si el token es válido, guarda el correo y el token en sesión y redirige
       al formulario para ingresar la nueva contraseña.
       @param request  → Objeto con el parámetro "token" en la URL
       @param response → Objeto para redirigir al formulario o a la página de error
    */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Establece la codificación UTF-8 para aceptar caracteres especiales
        request.setCharacterEncoding("UTF-8");
        // Variable de tipo texto que almacena el token de seguridad recibido por URL
        String token = request.getParameter("token");

        // Valida que el token no sea nulo ni esté vacío
        if (token == null || token.trim().isEmpty()) {
            // Si no viene token, redirige indicando que el token es inválido
            response.sendRedirect(request.getContextPath() +
                "/public/modules/01_autenticacion/07_SolicitarEmail.jsp?error=tokeninvalido");
            return;
        }

        // Instancia del DAO de usuario para verificar el token contra la base de datos
        UsuarioDao usuarioDao = new UsuarioDao();
        // Variable de tipo texto que almacena el correo asociado al token; null si expiró o no existe
        String correo = usuarioDao.obtenerCorreoPorToken(token);

        if (correo == null) {
            // Si no hay correo asociado, el token ya expiró o fue usado
            response.sendRedirect(request.getContextPath() +
                "/public/modules/01_autenticacion/07_SolicitarEmail.jsp?error=tokenexpirado");
            return;
        }

        // Guarda el correo del usuario en sesión para usarlo en el paso POST
        request.getSession().setAttribute("email_recuperacion", correo);
        // Guarda el token en sesión para marcarlo como usado después de cambiar la clave
        request.getSession().setAttribute("token_recuperacion", token);
        // Redirige al formulario donde el usuario ingresa su nueva contraseña
        response.sendRedirect(request.getContextPath() +
            "/public/modules/01_autenticacion/09_NuevaClave.jsp");
    }

    /* Método: doPost
       Propósito: Recibir la nueva contraseña ingresada por el usuario, verificar
       que la sesión aún sea válida, actualizar la contraseña en la base de datos
       con hash seguro y marcar el token como ya utilizado.
       @param request  → Objeto con el campo "nueva_clave" del formulario
       @param response → Objeto para redirigir al login (éxito) o al formulario (error)
    */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Establece la codificación UTF-8 para aceptar caracteres especiales
        request.setCharacterEncoding("UTF-8");

        // Variable de tipo texto que almacena la nueva contraseña ingresada por el usuario
        String nuevaClave = request.getParameter("nueva_clave");
        // Variable de tipo texto con el correo guardado en sesión durante el paso GET
        String correo     = (String) request.getSession().getAttribute("email_recuperacion");
        // Variable de tipo texto con el token guardado en sesión para invalidarlo después
        String token      = (String) request.getSession().getAttribute("token_recuperacion");

        // Verifica que el correo de recuperación siga en sesión (no expiró)
        if (correo == null) {
            // Si la sesión ya no tiene el correo, redirige indicando sesión expirada
            response.sendRedirect(request.getContextPath() +
                "/public/modules/01_autenticacion/07_SolicitarEmail.jsp?error=sesionexpirada");
            return;
        }

        // Valida que la nueva contraseña no esté vacía
        if (nuevaClave == null || nuevaClave.trim().isEmpty()) {
            // Si viene vacía, redirige de nuevo al formulario con error
            response.sendRedirect(request.getContextPath() +
                "/public/modules/01_autenticacion/09_NuevaClave.jsp?error=vacia");
            return;
        }

        // Instancia del DAO de usuario para actualizar la contraseña en la base de datos
        UsuarioDao usuarioDao = new UsuarioDao();
        // Variable booleana que indica si la contraseña fue actualizada correctamente
        // Nota: actualizarClave() también invalida todos los tokens del correo internamente
        boolean actualizado = usuarioDao.actualizarClave(correo, nuevaClave);

        if (actualizado) {
            // Marca el token específico como usado para que no pueda reutilizarse
            if (token != null) new TokensDao().marcarUsado(token);

            // Elimina el correo de recuperación de la sesión
            request.getSession().removeAttribute("email_recuperacion");
            // Elimina el token de recuperación de la sesión
            request.getSession().removeAttribute("token_recuperacion");
            // Redirige al login con mensaje de éxito
            response.sendRedirect(request.getContextPath() +
                "/public/modules/01_autenticacion/04_iniciarSesion.jsp?exito=contrasena_cambiada");
        } else {
            // Si la actualización falló, redirige de nuevo al formulario con error
            response.sendRedirect(request.getContextPath() +
                "/public/modules/01_autenticacion/09_NuevaClave.jsp?error=true");
        }
    }
}
