package com.smarthome.smarthome_budget.controlador;
import com.smarthome.smarthome_budget.dao.usuarioDao;
import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/NuevaClave")
public class NuevaClaveservlet extends HttpServlet {

    // GET: usuario llega desde el link del correo
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String token = request.getParameter("token");
        usuarioDao dao = new usuarioDao();
        String correo = dao.obtenerCorreoPorToken(token);

        if (correo == null) {
            response.sendRedirect("public/modules/01_autenticacion/07_SolicitarEmail.jsp?error=tokenexpirado");
            return;
        }

        request.getSession().setAttribute("email_recuperacion", correo);
        response.sendRedirect("public/modules/01_autenticacion/09_NuevaClave.jsp");
    }

    // POST: usuario envía su nueva contraseña
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        System.out.println("=== doPost de NuevaClave Servlet ===");
        
        String nuevaClave = request.getParameter("nueva_clave");
        String correo = (String) request.getSession().getAttribute("email_recuperacion");
        
        System.out.println("Nueva clave recibida: " + (nuevaClave != null ? "SÍ" : "NO"));
        System.out.println("Correo de sesión: " + correo);

        if (correo == null) {
            System.out.println("ERROR: Sesión expirada, no hay correo en sesión");
            response.sendRedirect("public/modules/01_autenticacion/07_SolicitarEmail.jsp?error=sesionexpirada");
            return;
        }

        usuarioDao dao = new usuarioDao();
        boolean actualizado = dao.actualizarClave(correo, nuevaClave);
        
        System.out.println("Contraseña actualizada: " + actualizado);

        if (actualizado) {
            System.out.println("EXITO: Redirigiendo a login con éxito");
            request.getSession().removeAttribute("email_recuperacion");
            response.sendRedirect("public/modules/01_autenticacion/04_iniciarSesion.jsp?exito=true");
        } else {
            System.out.println("ERROR: Redirigiendo a formulario con error");
            response.sendRedirect("public/modules/01_autenticacion/09_NuevaClave.jsp?error=true");
        }
    }
}