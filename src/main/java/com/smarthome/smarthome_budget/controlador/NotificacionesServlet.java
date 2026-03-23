package com.smarthome.smarthome_budget.controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/Notificaciones")
public class NotificacionesServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.setStatus(401);
            return;
        }
        
        String accion = req.getParameter("accion");
        
        if ("cargarPreferencias".equals(accion)) {
            // Simular preferencias del usuario (en producción, sacar de BD)
            String jsonResponse = "{\"notificacionesActivadas\":true,\"stockBajo\":true,\"egresosAltos\":true,\"recordatorios\":true}";
            
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            out.print(jsonResponse);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.setStatus(401);
            return;
        }
        
        String accion = req.getParameter("accion");
        String tipo = req.getParameter("tipo");
        String estado = req.getParameter("estado");
        
        // Guardar preferencias en sesión
        session.setAttribute("notificaciones_" + tipo, "true".equals(estado));
        
        // Para el tipo 'notificaciones', guardar también el estado general
        if ("notificaciones".equals(tipo)) {
            session.setAttribute("notificacionesActivadas", "true".equals(estado));
        }
        
        // Responder con JSON manual
        String jsonResponse = "{\"success\":true,\"mensaje\":\"Preferencia guardada correctamente\",\"tipo\":\"" + tipo + "\",\"estado\":\"" + estado + "\"}";
        
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(jsonResponse);
    }
    
    // Método para enviar notificaciones reales
    public static void enviarNotificacion(HttpServletRequest req, String titulo, String mensaje, String tipo) {
        HttpSession session = req.getSession(false);
        if (session == null) return;
        
        // Verificar si las notificaciones están activadas
        Boolean notificacionesActivadas = (Boolean) session.getAttribute("notificacionesActivadas");
        if (notificacionesActivadas == null || !notificacionesActivadas) {
            return; // No enviar si están desactivadas
        }
        
        // Aquí podrías integrar con servicios de notificación push, email, etc.
        System.out.println("NOTIFICACIÓN: " + titulo + " - " + mensaje + " (" + tipo + ")");
    }
}
