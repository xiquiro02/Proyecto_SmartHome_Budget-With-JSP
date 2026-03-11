package com.smarthome.smarthome_budget.controlador;

import com.smarthome.smarthome_budget.dao.RegistroEgresoDao;
import com.smarthome.smarthome_budget.modelo.RegistroEgreso;
import com.smarthome.smarthome_budget.modelo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/ListaEgresos")
public class ListaEgresosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        try {
            // Obtener sesión y validar que exista
            HttpSession session = request.getSession(false);
            if (session == null) {
                response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_expirada");
                return;
            }
            
            // Recuperar usuario y idHogar de la sesión
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            Integer idHogar = (Integer) session.getAttribute("idHogar");
            
            if (usuario == null || idHogar == null) {
                response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_invalida");
                return;
            }
            
            // Consultar todos los egresos del hogar
            RegistroEgresoDao egresoDao = new RegistroEgresoDao();
            List<RegistroEgreso> egresos = egresoDao.listarPorHogar(idHogar);
            
            // Formatear la fecha de cada egreso a yyyy-MM-dd HH:mm
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            for (RegistroEgreso egreso : egresos) {
                if (egreso.getFechaVencimiento() != null) {
                    String fechaFormateada = egreso.getFechaVencimiento().format(formatter);
                    egreso.setDescripcion(egreso.getDescripcion() + "|FECHA:" + fechaFormateada);
                }
            }
            
            // Guardar la lista en request
            request.setAttribute("egresos", egresos);
            
            // Redirigir al JSP
            request.getRequestDispatcher("/public/modules/02_Gestion_facturas-recordatorios/03_listaEgresos.jsp").forward(request, response);
            
        } catch (Exception e) {
            // Manejar excepciones y redirigir a página de error
            response.sendRedirect(request.getContextPath() + "/public/modules/MenuPrincipal/01_menuPrincipal.jsp?error=lista_egresos");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // doPost simplemente llama a doGet (sin edición en la lista)
        doGet(request, response);
    }
}
