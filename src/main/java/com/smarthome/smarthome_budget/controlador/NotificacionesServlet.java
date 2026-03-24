package com.smarthome.smarthome_budget.controlador;

// Importación de clases necesarias para el manejo de sesión y respuesta JSON
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

/* Clase: NotificacionesServlet
   Propósito: Gestionar las preferencias de notificaciones in-app del usuario autenticado.
   Permite cargar las preferencias actuales vía GET y guardar los cambios vía POST.
   Las preferencias se almacenan en la sesión y pueden integrarse con una base de datos
   o con un servicio de notificaciones push en versiones futuras.
   URL mapeada: /Notificaciones
*/
@WebServlet("/Notificaciones")
public class NotificacionesServlet extends HttpServlet {

    /* Método: doGet
       Propósito: Cargar y retornar las preferencias actuales de notificaciones del usuario
       en formato JSON. Si la acción es "cargarPreferencias", responde con un objeto JSON
       con los estados de cada tipo de notificación.
       @param req  → Objeto con el parámetro "accion" en la URL
       @param resp → Objeto para escribir la respuesta JSON directamente al cliente
    */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Verifica que haya una sesión activa con usuario autenticado
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            // Sin sesión activa, responde con código HTTP 401 (no autorizado)
            resp.setStatus(401);
            return;
        }

        // Variable de tipo texto que almacena la acción solicitada por parámetro URL
        String accion = req.getParameter("accion");

        if ("cargarPreferencias".equals(accion)) {
            // Cadena JSON que representa el estado actual de las preferencias de notificaciones
            // (En producción, este JSON debería cargarse desde la base de datos)
            String jsonResponse = "{\"notificacionesActivadas\":true,\"stockBajo\":true,\"egresosAltos\":true,\"recordatorios\":true}";

            // Establece el tipo de contenido de la respuesta como JSON
            resp.setContentType("application/json");
            // Obtiene el escritor para enviar la respuesta al cliente
            PrintWriter out = resp.getWriter();
            // Escribe el JSON en la respuesta
            out.print(jsonResponse);
        }
    }

    /* Método: doPost
       Propósito: Recibir y guardar una preferencia específica de notificación enviada
       por el cliente (activo/inactivo). Almacena el estado en la sesión del usuario
       y responde con un JSON confirmando la operación.
       @param req  → Objeto con los parámetros "accion", "tipo" y "estado" del formulario
       @param resp → Objeto para escribir la respuesta JSON de confirmación
    */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Verifica que haya una sesión activa con usuario autenticado
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            // Sin sesión activa, responde con código HTTP 401 (no autorizado)
            resp.setStatus(401);
            return;
        }

        // Variable de tipo texto que almacena la acción enviada (ej. "guardar")
        String accion = req.getParameter("accion");
        // Variable de tipo texto que almacena el tipo de notificación a actualizar (ej. "stockBajo")
        String tipo   = req.getParameter("tipo");
        // Variable de tipo texto que almacena el nuevo estado: "true" (activo) o "false" (inactivo)
        String estado = req.getParameter("estado");

        // Guarda la preferencia del tipo de notificación en la sesión con clave dinámica
        // La clave se construye como "notificaciones_" + el tipo (ej. "notificaciones_stockBajo")
        session.setAttribute("notificaciones_" + tipo, "true".equals(estado));

        // Si el tipo es "notificaciones" (estado general), actualiza también el atributo global
        if ("notificaciones".equals(tipo)) {
            // Variable booleana que almacena si las notificaciones en general están activadas
            session.setAttribute("notificacionesActivadas", "true".equals(estado));
        }

        // Construye la respuesta JSON de confirmación con el tipo y estado recibidos
        String jsonResponse = "{\"success\":true,\"mensaje\":\"Preferencia guardada correctamente\",\"tipo\":\"" + tipo + "\",\"estado\":\"" + estado + "\"}";

        // Establece el tipo de contenido de la respuesta como JSON
        resp.setContentType("application/json");
        // Obtiene el escritor para enviar la respuesta al cliente
        PrintWriter out = resp.getWriter();
        // Escribe el JSON en la respuesta
        out.print(jsonResponse);
    }

    /* Método: enviarNotificacion
       Propósito: Método estático auxiliar para registrar una notificación desde
       cualquier servlet de la aplicación, verificando primero si las notificaciones
       están habilitadas en la sesión del usuario.
       @param req     → Objeto de la petición HTTP para acceder a la sesión
       @param titulo  → Texto con el título de la notificación
       @param mensaje → Texto con el cuerpo o detalle de la notificación
       @param tipo    → Texto con el tipo de notificación (ej. "info", "warning", "error")
    */
    public static void enviarNotificacion(HttpServletRequest req, String titulo, String mensaje, String tipo) {
        // Obtiene la sesión activa sin crear una nueva si no existe
        HttpSession session = req.getSession(false);
        if (session == null) return;

        // Verifica si el usuario tiene las notificaciones generales activadas en sesión
        // Variable booleana que almacena si las notificaciones están habilitadas globalmente
        Boolean notificacionesActivadas = (Boolean) session.getAttribute("notificacionesActivadas");
        if (notificacionesActivadas == null || !notificacionesActivadas) {
            // Si están desactivadas, no procesa la notificación
            return;
        }

        // Registra la notificación en la consola del servidor
        // (En producción puede integrarse con servicios de push, email, etc.)
        System.out.println("NOTIFICACIÓN: " + titulo + " - " + mensaje + " (" + tipo + ")");
    }
}
