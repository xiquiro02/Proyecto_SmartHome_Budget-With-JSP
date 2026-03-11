package com.smarthome.smarthome_budget.controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.smarthome.smarthome_budget.dao.RegistroIngresoDao;
import com.smarthome.smarthome_budget.dao.RegistroEgresoDao;
import com.smarthome.smarthome_budget.dao.PresupuestoMensualDao;
import com.smarthome.smarthome_budget.modelo.RegistroIngreso;
import com.smarthome.smarthome_budget.modelo.RegistroEgreso;
import com.smarthome.smarthome_budget.modelo.PresupuestoMensual;
import com.smarthome.smarthome_budget.modelo.Usuario;

@WebServlet("/Finanzas")
public class FinanzasServlet extends HttpServlet {

    private final RegistroIngresoDao ingresoDao = new RegistroIngresoDao();
    private final RegistroEgresoDao egresoDao   = new RegistroEgresoDao();
    private final PresupuestoMensualDao presDao  = new PresupuestoMensualDao();

    private static final String BASE = "/public/modules/06_Finanzas/";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        int idHogar     = (Integer) session.getAttribute("idHogar");
        int idRol       = (Integer) session.getAttribute("idRol");
        String accion   = req.getParameter("accion");
        if (accion == null) accion = "";

        switch (accion) {

            // ── RESUMEN FINANCIERO ──────────────────────────────────────────
            case "resumen": {
                BigDecimal totalIngresos = ingresoDao.totalMesActual(idHogar);
                BigDecimal totalEgresos  = presDao.totalEgresosMesActual(idHogar);
                BigDecimal disponible    = totalIngresos.subtract(totalEgresos);

                PresupuestoMensual pres  = presDao.obtenerMesActual(idHogar);
                if (pres != null) {
                    pres.setTotalEgresos(totalEgresos);
                    pres.setDisponible(pres.getMontoMax().subtract(totalEgresos));
                }

                req.setAttribute("totalIngresos", totalIngresos);
                req.setAttribute("totalEgresos",  totalEgresos);
                req.setAttribute("disponible",    disponible);
                req.setAttribute("presupuesto",   pres);
                req.getRequestDispatcher(BASE + "08_ResumenFinanciero.jsp").forward(req, resp);
                break;
            }

            // ── DETALLE INGRESOS ────────────────────────────────────────────
            case "detalleIngresos": {
                List<RegistroIngreso> ingresos = ingresoDao.listarPorHogar(idHogar);
                BigDecimal total = ingresos.stream()
                        .map(RegistroIngreso::getMonto)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                req.setAttribute("ingresos",      ingresos);
                req.setAttribute("totalIngresos", total);
                req.getRequestDispatcher(BASE + "04_DetalleIngresos.jsp").forward(req, resp);
                break;
            }

            // ── FORMULARIO REGISTRAR INGRESO ────────────────────────────────
            case "formIngreso": {
                req.setAttribute("categorias", ingresoDao.listarCategorias());
                req.getRequestDispatcher(BASE + "02_RegistrarIngresos.jsp").forward(req, resp);
                break;
            }

            // ── DETALLE EGRESOS ─────────────────────────────────────────────
            case "detalleEgresos": {
                List<RegistroEgreso> egresos = egresoDao.listarPorHogar(idHogar);
                BigDecimal total = egresos.stream()
                        .map(RegistroEgreso::getMonto)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                req.setAttribute("egresos",      egresos);
                req.setAttribute("totalEgresos", total);
                req.getRequestDispatcher(BASE + "07_DetalleEgresos.jsp").forward(req, resp);
                break;
            }

            // ── FORMULARIO REGISTRAR EGRESO ─────────────────────────────────
            case "formEgreso": {
                // sólo rol 1 y 2
                if (idRol == 3) {
                    resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=resumen&error=sin_permiso");
                    return;
                }
                req.setAttribute("categorias", egresoDao.listarCategorias());
                req.getRequestDispatcher(BASE + "05_RegistrarEgresos.jsp").forward(req, resp);
                break;
            }

            // ── FORMULARIO PRESUPUESTO ──────────────────────────────────────
            case "formPresupuesto": {
                if (idRol == 3) {
                    resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=resumen&error=sin_permiso");
                    return;
                }
                PresupuestoMensual pres = presDao.obtenerMesActual(idHogar);
                BigDecimal totalEgresos = presDao.totalEgresosMesActual(idHogar);
                if (pres != null) {
                    pres.setTotalEgresos(totalEgresos);
                    pres.setDisponible(pres.getMontoMax().subtract(totalEgresos));
                }
                req.setAttribute("presupuesto",  pres);
                req.setAttribute("totalEgresos", totalEgresos);
                req.getRequestDispatcher(BASE + "09_PresupuestoMensual.jsp").forward(req, resp);
                break;
            }

            // ── DEFAULT: dashboard ──────────────────────────────────────────
            default:
                BigDecimal totalIngresos = ingresoDao.totalMesActual(idHogar);
                BigDecimal totalEgresos  = presDao.totalEgresosMesActual(idHogar);
                PresupuestoMensual pres  = presDao.obtenerMesActual(idHogar);
                if (pres != null) {
                    pres.setTotalEgresos(totalEgresos);
                    pres.setDisponible(pres.getMontoMax().subtract(totalEgresos));
                }
                req.setAttribute("totalIngresos", totalIngresos);
                req.setAttribute("totalEgresos",  totalEgresos);
                req.setAttribute("presupuesto",   pres);
                req.getRequestDispatcher(BASE + "01_Finanzas.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        int idHogar     = (Integer) session.getAttribute("idHogar");
        int idRol       = (Integer) session.getAttribute("idRol");
        String accion   = req.getParameter("accion");
        if (accion == null) accion = "";

        switch (accion) {

            // ── GUARDAR INGRESO ─────────────────────────────────────────────
            case "guardarIngreso": {
                try {
                    BigDecimal monto = new BigDecimal(req.getParameter("monto").replace(",", "").replace("$", "").trim());
                    int idCat        = Integer.parseInt(req.getParameter("idCategoriaIngreso"));
                    String desc      = req.getParameter("descripcion");

                    RegistroIngreso ingreso = new RegistroIngreso();
                    ingreso.setIdHogar(idHogar);
                    ingreso.setIdUsuario(usuario.getIDUsuario());
                    ingreso.setMonto(monto);
                    ingreso.setIdCategoriaIngreso(idCat);
                    ingreso.setDescripcion(desc);

                    boolean ok = ingresoDao.registrar(ingreso);
                    if (ok) {
                        req.getRequestDispatcher(BASE + "03_RegistroIngresoExito.jsp").forward(req, resp);
                    } else {
                        req.setAttribute("error", "No se pudo guardar el ingreso.");
                        req.setAttribute("categorias", ingresoDao.listarCategorias());
                        req.getRequestDispatcher(BASE + "02_RegistrarIngresos.jsp").forward(req, resp);
                    }
                } catch (Exception e) {
                    req.setAttribute("error", "Datos inválidos: " + e.getMessage());
                    req.setAttribute("categorias", ingresoDao.listarCategorias());
                    req.getRequestDispatcher(BASE + "02_RegistrarIngresos.jsp").forward(req, resp);
                }
                break;
            }

            // ── GUARDAR EGRESO ──────────────────────────────────────────────
            case "guardarEgreso": {
                if (idRol == 3) {
                    resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=resumen&error=sin_permiso");
                    return;
                }
                try {
                    BigDecimal monto = new BigDecimal(req.getParameter("monto").replace(",", "").replace("$", "").trim());
                    int idCat        = Integer.parseInt(req.getParameter("idCategoriaEgreso"));
                    String desc      = req.getParameter("descripcion");

                    RegistroEgreso egreso = new RegistroEgreso();
                    egreso.setIdHogar(idHogar);
                    egreso.setIdUsuario(usuario.getIDUsuario());
                    egreso.setMonto(monto);
                    egreso.setIdCategoriaEgreso(idCat);
                    egreso.setDescripcion(desc);

                    boolean ok = egresoDao.registrar(egreso);
                    if (ok) {
                        req.getRequestDispatcher(BASE + "06_RegistroEgresoExito.jsp").forward(req, resp);
                    } else {
                        req.setAttribute("error", "No se pudo guardar el egreso.");
                        req.setAttribute("categorias", egresoDao.listarCategorias());
                        req.getRequestDispatcher(BASE + "05_RegistrarEgresos.jsp").forward(req, resp);
                    }
                } catch (Exception e) {
                    req.setAttribute("error", "Datos inválidos: " + e.getMessage());
                    req.setAttribute("categorias", egresoDao.listarCategorias());
                    req.getRequestDispatcher(BASE + "05_RegistrarEgresos.jsp").forward(req, resp);
                }
                break;
            }

            // ── GUARDAR PRESUPUESTO ─────────────────────────────────────────
            case "guardarPresupuesto": {
                if (idRol == 3) {
                    resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=resumen&error=sin_permiso");
                    return;
                }
                try {
                    BigDecimal monto = new BigDecimal(req.getParameter("montoMax").replace(",", "").replace("$", "").trim());
                    int mes          = Integer.parseInt(req.getParameter("mes"));
                    boolean al80     = "on".equals(req.getParameter("alerta80"));
                    boolean alSup    = "on".equals(req.getParameter("alertaSuperado"));

                    PresupuestoMensual p = new PresupuestoMensual();
                    p.setIdHogar(idHogar);
                    p.setMontoMax(monto);
                    p.setMes(mes);
                    p.setAnio(java.time.LocalDate.now().getYear());
                    p.setAlerta80(al80);
                    p.setAlertaSuperePresupuesto(alSup);

                    presDao.guardar(p);
                    resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=resumen");
                } catch (Exception e) {
                    req.setAttribute("error", "Datos inválidos: " + e.getMessage());
                    req.getRequestDispatcher(BASE + "09_PresupuestoMensual.jsp").forward(req, resp);
                }
                break;
            }

            default:
                resp.sendRedirect(req.getContextPath() + "/Finanzas");
        }
    }
}
