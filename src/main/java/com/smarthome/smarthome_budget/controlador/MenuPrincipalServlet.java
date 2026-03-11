package com.smarthome.smarthome_budget.controlador;

import com.smarthome.smarthome_budget.dao.RegistroEgresoDao;
import com.smarthome.smarthome_budget.dao.PresupuestoMensualDao;
import com.smarthome.smarthome_budget.modelo.PresupuestoMensual;
import com.smarthome.smarthome_budget.modelo.RegistroEgreso;
import com.smarthome.smarthome_budget.modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Servlet del menú principal.
 * Carga los datos reales del dashboard (próximo pago, presupuesto disponible)
 * filtrando siempre por el IDHogar del usuario en sesión.
 *
 * GET /Menu → 01_menuPrincipal.jsp con atributos de sesión
 */
@WebServlet("/Menu")
public class MenuPrincipalServlet extends HttpServlet {

    private static final String BASE = "/public/modules/MenuPrincipal/";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() +
                    "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_requerida");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        int idHogar     = (Integer) session.getAttribute("idHogar");
        int idRol       = (Integer) session.getAttribute("idRol");

        // ── Próximo pago pendiente ────────────────────────────────────────────
        RegistroEgresoDao egresoDao = new RegistroEgresoDao();
        egresoDao.actualizarVencidas();   // actualizar vencidas antes de mostrar

        List<RegistroEgreso> pendientes = egresoDao.listarPorEstado(idHogar, "Pendiente");
        RegistroEgreso proximoPago = pendientes.isEmpty() ? null : pendientes.get(0);
        // pendientes ya viene ordenado por FechaVencimiento ASC → el primero es el más próximo

        // ── Presupuesto disponible del mes ────────────────────────────────────
        PresupuestoMensualDao presDao = new PresupuestoMensualDao();
        PresupuestoMensual presupuesto = presDao.obtenerMesActual(idHogar);
        BigDecimal totalEgresos       = presDao.totalEgresosMesActual(idHogar);
        BigDecimal disponible         = BigDecimal.ZERO;

        if (presupuesto != null) {
            disponible = presupuesto.getMontoMax().subtract(totalEgresos);
            if (disponible.compareTo(BigDecimal.ZERO) < 0) disponible = BigDecimal.ZERO;
        }

        // ── Pasar atributos a la vista ────────────────────────────────────────
        req.setAttribute("proximoPago",  proximoPago);
        req.setAttribute("presupuesto",  presupuesto);
        req.setAttribute("totalEgresos", totalEgresos);
        req.setAttribute("disponible",   disponible);
        req.setAttribute("idRol",        idRol);

        req.getRequestDispatcher(BASE + "01_menuPrincipal.jsp").forward(req, resp);
    }
}
