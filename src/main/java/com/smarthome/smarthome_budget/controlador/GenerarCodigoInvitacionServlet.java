package com.smarthome.smarthome_budget.controlador;

// Importación de clases DAO y modelo necesarias para generar códigos de invitación
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

/* Clase: GenerarCodigoInvitacionServlet
   Propósito: Permitir que el Administrador (rol 1) de un hogar genere un código
   de invitación para agregar nuevos miembros con un rol específico (Administrador,
   Cotitular o Invitado). El código generado se guarda en sesión y se muestra en
   el menú principal.
   URL mapeada: /GenerarCodigoInvitacion
*/
@WebServlet("/GenerarCodigoInvitacion")
public class GenerarCodigoInvitacionServlet extends HttpServlet {

    /* Método: doPost
       Propósito: Recibir la solicitud de generación de código de invitación,
       validar que quien lo solicita sea Administrador del hogar, generar el código
       a través del DAO y almacenarlo en la sesión para mostrarlo al usuario.
       @param request  → Objeto con los datos del formulario (idRol seleccionado)
       @param response → Objeto para redirigir con el resultado
    */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Establece la codificación UTF-8 para aceptar caracteres especiales
        request.setCharacterEncoding("UTF-8");

        // Objeto Usuario obtenido de la sesión activa para verificar identidad
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        if (usuario == null) {
            // Si no hay sesión activa, redirige al login
            response.sendRedirect(request.getContextPath() +
                "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_requerida");
            return;
        }

        // Variable entera envuelta en Integer que almacena el ID del hogar de la sesión
        Integer idHogar = (Integer) request.getSession().getAttribute("idHogar");
        if (idHogar == null) {
            // Si no se encuentra el hogar en sesión, redirige con error
            response.sendRedirect(request.getContextPath() + "/Menu?error=sin_hogar");
            return;
        }

        // Variable de tipo texto que almacena el ID de rol enviado por el formulario
        String idRolParam = request.getParameter("idRol");
        if (idRolParam == null || idRolParam.trim().isEmpty()) {
            // Si no se seleccionó rol, redirige con error
            response.sendRedirect(request.getContextPath() + "/Menu?error=rol_requerido");
            return;
        }

        try {
            // Variable entera que almacena el ID de rol convertido desde texto a número
            int idRol = Integer.parseInt(idRolParam.trim());
            if (idRol < 1 || idRol > 3) {
                // El rol debe ser 1, 2 o 3; si no, redirige con error
                response.sendRedirect(request.getContextPath() + "/Menu?error=rol_invalido");
                return;
            }

            // Solo el ADMINISTRADOR (rol 1) puede generar códigos de invitación
            // Instancia del DAO para verificar el rol del usuario en el hogar
            DetallesHogaresDao detallesDao = new DetallesHogaresDao();
            // Objeto Roles que contiene el rol actual del usuario en el hogar
            Roles rolUsuario = detallesDao.obtenerRolDeUsuarioEnHogar(usuario.getIDUsuario(), idHogar);

            if (rolUsuario == null || rolUsuario.getIdRol() != 1) {
                // Si el usuario no es Administrador, redirige con acceso denegado
                response.sendRedirect(request.getContextPath() + "/Menu?error=acceso_denegado");
                return;
            }

            // Instancia del DAO de códigos de invitación para generar el código
            CodigosInvitacionDao codigosDao = new CodigosInvitacionDao();
            // Variable de tipo texto que almacena el código generado (UUID o similar)
            String codigoGenerado = codigosDao.generarCodigo(idHogar, idRol);

            if (codigoGenerado == null) {
                // Si la generación falló en la base de datos, redirige con error
                response.sendRedirect(request.getContextPath() + "/Menu?error=generar_codigo");
                return;
            }

            // Guarda el código generado en la sesión para mostrarlo en el menú principal
            request.getSession().setAttribute("codigo_generado", codigoGenerado);
            // Guarda el nombre del rol asignado en sesión para mostrarlo en la vista
            request.getSession().setAttribute("rol_asignado", obtenerNombreRol(idRol));

            // Redirige al menú con la acción para mostrar el código recién generado
            response.sendRedirect(request.getContextPath() + "/Menu?accion=codigoGenerado");

        } catch (NumberFormatException e) {
            // Si el parámetro del rol no es un número válido, redirige con error
            response.sendRedirect(request.getContextPath() + "/Menu?error=rol_invalido");
        }
    }

    /* Método: doGet
       Propósito: Redirigir al menú principal si alguien accede directamente por GET
       a esta URL (uso no permitido; la acción solo es válida por POST).
       @param request  → Objeto de la petición HTTP
       @param response → Objeto para redirigir al menú
    */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Cualquier acceso directo por GET redirige al menú principal
        response.sendRedirect(request.getContextPath() + "/Menu");
    }

    /* Método: obtenerNombreRol
       Propósito: Convertir un ID de rol numérico en su nombre descriptivo en texto.
       @param idRol → Entero con el ID del rol (1 = Administrador, 2 = Cotitular, 3 = Invitado)
       @return String → Retorna el nombre del rol en texto, o "Desconocido" si el ID no es válido
    */
    private String obtenerNombreRol(int idRol) {
        switch (idRol) {
            case 1: return "Administrador";
            case 2: return "Cotitular";
            case 3: return "Invitado";
            default: return "Desconocido";
        }
    }
}
