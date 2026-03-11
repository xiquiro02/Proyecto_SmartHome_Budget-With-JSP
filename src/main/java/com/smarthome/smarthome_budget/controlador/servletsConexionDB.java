package com.smarthome.smarthome_budget.controlador;

import com.smarthome.smarthome_budget.basedatos.claseConexion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/testConexion")
public class servletsConexionDB extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        Connection conexion = null;
        
        try {
            conexion = claseConexion.MetodoConectar();
            
            if (conexion != null && !conexion.isClosed()) {
                response.getWriter().println(
                    "<html><body>" +
                    "<h1 style='color: green;'>✓ Conexión exitosa a la base de datos</h1>" +
                    "<p>La conexión está funcionando correctamente.</p>" +
                    "</body></html>"
                );
            } else {
                response.getWriter().println(
                    "<html><body>" +
                    "<h1 style='color: red;'>✗ Error de conexión</h1>" +
                    "<p>No se pudo establecer conexión con la base de datos.</p>" +
                    "</body></html>"
                );
            }
        } catch (Exception e) {
            response.getWriter().println(
                "<html><body>" +
                "<h1 style='color: red;'>✗ Error de conexión</h1>" +
                "<p>Ocurrió un error al intentar conectar con la base de datos.</p>" +
                "</body></html>"
            );
        } finally {
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (Exception e) {
                    // Error al cerrar conexión, pero no exponer detalles
                }
            }
        }
    }
}
