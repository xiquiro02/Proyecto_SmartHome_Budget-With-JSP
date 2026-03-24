package com.smarthome.smarthome_budget.controlador;

// Importación de la clase de conexión y clases necesarias para el servlet de prueba
import com.smarthome.smarthome_budget.basedatos.claseConexion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

/* Clase: servletsConexionDB
   Propósito: Servlet de diagnóstico y prueba de conectividad con la base de datos.
   Al acceder por GET a /testConexion, intenta establecer una conexión con MySQL
   usando claseConexion y muestra en el navegador si fue exitosa o no.
   Útil para verificar que la base de datos esté disponible durante el desarrollo.
   URL mapeada: /testConexion
*/
@WebServlet("/testConexion")
public class servletsConexionDB extends HttpServlet {

    /* Método: doGet
       Propósito: Intentar establecer una conexión con la base de datos y mostrar
       el resultado visualmente en el navegador mediante una página HTML simple
       con indicador de color verde (éxito) o rojo (fallo).
       @param request  → Objeto de la petición HTTP GET (no requiere parámetros)
       @param response → Objeto para escribir la respuesta HTML al navegador
    */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Establece el tipo de contenido de la respuesta como HTML con codificación UTF-8
        response.setContentType("text/html;charset=UTF-8");
        // Variable de tipo Connection que almacena la conexión a la base de datos; inicia en null
        Connection conexion = null;

        try {
            // Intenta obtener una conexión activa usando el método estático de claseConexion
            conexion = claseConexion.MetodoConectar();

            if (conexion != null && !conexion.isClosed()) {
                // Si la conexión es válida y está abierta, muestra mensaje de éxito en verde
                response.getWriter().println(
                    "<html><body>" +
                    "<h1 style='color: green;'>✓ Conexión exitosa a la base de datos</h1>" +
                    "<p>La conexión está funcionando correctamente.</p>" +
                    "</body></html>"
                );
            } else {
                // Si la conexión es null o está cerrada, muestra mensaje de error en rojo
                response.getWriter().println(
                    "<html><body>" +
                    "<h1 style='color: red;'>✗ Error de conexión</h1>" +
                    "<p>No se pudo establecer conexión con la base de datos.</p>" +
                    "</body></html>"
                );
            }
        } catch (Exception e) {
            // Captura cualquier excepción durante el intento de conexión y muestra error
            response.getWriter().println(
                "<html><body>" +
                "<h1 style='color: red;'>✗ Error de conexión</h1>" +
                "<p>Ocurrió un error al intentar conectar con la base de datos.</p>" +
                "</body></html>"
            );
        } finally {
            // El bloque finally garantiza que la conexión siempre se cierre
            if (conexion != null) {
                try {
                    // Cierra la conexión para liberar recursos de la base de datos
                    conexion.close();
                } catch (Exception e) {
                    // Error al cerrar la conexión; no se exponen detalles al cliente
                }
            }
        }
    }
}
