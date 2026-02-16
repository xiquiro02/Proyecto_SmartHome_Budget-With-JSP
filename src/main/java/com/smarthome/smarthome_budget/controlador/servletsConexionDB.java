package com.smarthome.smarthome_budget.controlador;
import com.smarthome.smarthome_budget.basedatos.claseConexion;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/testConexion")
public class servletsConexionDB extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    { 
        Connection conexion = claseConexion.MetodoConectar();

        response.setContentType("text/html");
        System.out.println("=========== Iniciando validación de conexión... ===========");
        
        if (conexion != null) 
        {
            System.out.println("Conexión establecida con éxito...");
            response.getWriter().println("<h1>Conexion exitosa</h1>");
        } 
        else 
        {
            System.err.println("No se pudo conectar a la base de datos.");
            response.getWriter().println("<h1>Error de conexion: Revisa la consola</h1>");
        }
    }
}
