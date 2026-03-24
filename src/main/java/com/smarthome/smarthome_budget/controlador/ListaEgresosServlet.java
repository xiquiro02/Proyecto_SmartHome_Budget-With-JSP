package com.smarthome.smarthome_budget.controlador;

// Importación de clases necesarias para el manejo de peticiones HTTP
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/* Clase: ListaEgresosServlet
   Propósito: Servlet de compatibilidad y redirección hacia el módulo de Facturas.
   Cualquier petición GET o POST a /ListaEgresos es redirigida a la vista de lista
   de facturas en /Facturas?accion=lista para mantener consistencia en la navegación.
   URL mapeada: /ListaEgresos
*/
@WebServlet("/ListaEgresos")
public class ListaEgresosServlet extends HttpServlet {

    /* Método: doGet
       Propósito: Redirigir cualquier acceso por GET a la vista de lista de facturas.
       @param request  → Objeto de la petición HTTP GET
       @param response → Objeto para ejecutar la redirección
    */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirige a la lista de facturas en el servlet /Facturas
        response.sendRedirect(request.getContextPath() + "/Facturas?accion=lista");
    }

    /* Método: doPost
       Propósito: Redirigir cualquier envío de formulario por POST a la vista de lista de facturas.
       @param request  → Objeto de la petición HTTP POST
       @param response → Objeto para ejecutar la redirección
    */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirige a la lista de facturas en el servlet /Facturas
        response.sendRedirect(request.getContextPath() + "/Facturas?accion=lista");
    }
}
