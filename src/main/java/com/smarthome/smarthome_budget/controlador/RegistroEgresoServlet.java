package com.smarthome.smarthome_budget.controlador;

import com.smarthome.smarthome_budget.dao.CategoriaEgresoDao;
import com.smarthome.smarthome_budget.dao.MetodoPagoDao;
import com.smarthome.smarthome_budget.dao.RegistroEgresoDao;
import com.smarthome.smarthome_budget.modelo.CategoriaEgreso;
import com.smarthome.smarthome_budget.modelo.MetodoPago;
import com.smarthome.smarthome_budget.modelo.RegistroEgreso;
import com.smarthome.smarthome_budget.modelo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/RegistroEgreso")
public class RegistroEgresoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Obtener categorías
            CategoriaEgresoDao categoriaDao = new CategoriaEgresoDao();
            List<CategoriaEgreso> categorias = categoriaDao.listarCategorias();
            request.setAttribute("categorias", categorias);
            
            // Obtener métodos de pago
            MetodoPagoDao metodoDao = new MetodoPagoDao();
            List<MetodoPago> metodosPago = metodoDao.listarMetodosPago();
            request.setAttribute("metodosPago", metodosPago);
            
            // Redirigir al JSP
            request.getRequestDispatcher("/public/modules/02_Gestion_facturas-recordatorios/02_formularioRegistrar-facturas.jsp").forward(request, response);
            
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/public/modules/MenuPrincipal/01_menuPrincipal.jsp?error=cargar_formulario");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        try {
            // Obtener sesión y datos del usuario
            HttpSession session = request.getSession(false);
            if (session == null) {
                response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_expirada");
                return;
            }
            
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            Integer idHogar = (Integer) session.getAttribute("idHogar");
            
            if (usuario == null || idHogar == null) {
                response.sendRedirect(request.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_invalida");
                return;
            }
            
            // Obtener parámetros del formulario
            String nombreFactura = request.getParameter("nombreFactura");
            String montoStr = request.getParameter("monto");
            String idCategoriaStr = request.getParameter("idCategoriaEgreso");
            String idMetodoStr = request.getParameter("idMetodoPago");
            String fechaVencStr = request.getParameter("fechaVencimiento");
            String descripcion = request.getParameter("descripcion");
            String estadoPago = request.getParameter("estadoPago");
            
            // Validaciones básicas
            if (nombreFactura == null || nombreFactura.trim().isEmpty() ||
                montoStr == null || montoStr.trim().isEmpty() ||
                idCategoriaStr == null || idCategoriaStr.trim().isEmpty() ||
                idMetodoStr == null || idMetodoStr.trim().isEmpty() ||
                fechaVencStr == null || fechaVencStr.trim().isEmpty() ||
                estadoPago == null || estadoPago.trim().isEmpty()) {
                
                request.setAttribute("error", "Todos los campos son obligatorios");
                doGet(request, response);
                return;
            }
            
            // Convertir datos con validación específica
            BigDecimal monto;
            try {
                monto = new BigDecimal(montoStr);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "El monto debe ser un número válido");
                doGet(request, response);
                return;
            }
            
            int idCategoriaEgreso;
            try {
                idCategoriaEgreso = Integer.parseInt(idCategoriaStr);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "La categoría debe ser un número válido");
                doGet(request, response);
                return;
            }
            
            int idMetodoPago;
            try {
                idMetodoPago = Integer.parseInt(idMetodoStr);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "El método de pago debe ser un número válido");
                doGet(request, response);
                return;
            }
            
            // Parsear fecha y hora
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime fechaVencimiento = LocalDateTime.parse(fechaVencStr, formatter);
            
            // Crear objeto RegistroEgreso
            RegistroEgreso egreso = new RegistroEgreso();
            egreso.setIdHogar(idHogar);
            egreso.setIdUsuario(usuario.getIDUsuario());
            egreso.setNombreFactura(nombreFactura.trim());
            egreso.setMonto(monto);
            egreso.setIdCategoriaEgreso(idCategoriaEgreso);
            egreso.setIdMetodoPago(idMetodoPago);
            egreso.setFechaVencimiento(fechaVencimiento);
            egreso.setDescripcion(descripcion != null ? descripcion.trim() : "");
            egreso.setEstadoPago(estadoPago);
            
            // Guardar en base de datos
            RegistroEgresoDao egresoDao = new RegistroEgresoDao();
            int idGenerado = egresoDao.insertarEgreso(egreso);
            
            if (idGenerado > 0) {
                response.sendRedirect(request.getContextPath() + "/ListaEgresos?exito=registrado");
            } else {
                request.setAttribute("error", "Error al guardar el egreso. Inténtalo nuevamente.");
                doGet(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Formato de datos inválido");
            doGet(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error inesperado. Inténtalo nuevamente.");
            doGet(request, response);
        }
    }
}
