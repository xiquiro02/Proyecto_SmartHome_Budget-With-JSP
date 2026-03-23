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

/*
 * FinanzasServlet — módulo Finanzas reestructurado.
 *
 * MENÚ PRINCIPAL (01_Finanzas.jsp):
 *   Solo muestra "Resumen Financiero" y "Presupuesto Mensual" (Rol 1 y 2).
 *   El registro de ingresos/egresos ocurre únicamente desde el detalle.
 *
 * ROLES:
 *   Rol 1 (Administrador) → CRUD completo
 *   Rol 2 (Adulto/Cotitular) → puede ver y crear; NO puede editar ni eliminar
 *   Rol 3 (Invitado/Hijo) → sin acceso a finanzas
 *
 * ACCIONES GET:
 *   (default/menu)    → 01_Finanzas.jsp
 *   resumen           → 08_ResumenFinanciero.jsp
 *   detalleIngresos   → 04_DetalleIngresos.jsp
 *   formIngreso       → 02_RegistrarIngresos.jsp
 *   editarIngreso     → 02_RegistrarIngresos.jsp (modo edición)
 *   detalleEgresos    → 07_DetalleEgresos.jsp
 *   formEgreso        → 05_RegistrarEgresos.jsp
 *   editarEgreso      → 05_RegistrarEgresos.jsp (modo edición)
 *   formPresupuesto   → 09_PresupuestoMensual.jsp  [solo Rol 1]
 *
 * ACCIONES POST:
 *   guardarIngreso    → registra o actualiza ingreso
 *   anularIngreso     → soft-delete ingreso   [solo Rol 1]
 *   reactivarIngreso  → reactiva ingreso      [solo Rol 1]
 *   guardarEgreso     → registra o actualiza egreso
 *   anularEgreso      → soft-delete egreso    [solo Rol 1]
 *   reactivarEgreso   → reactiva egreso       [solo Rol 1]
 *   guardarPresupuesto → guarda presupuesto mensual [solo Rol 1]
 */

@WebServlet("/Finanzas")
public class FinanzasServlet extends HttpServlet {

    private final RegistroIngresoDao ingresoDao = new RegistroIngresoDao();
    private final RegistroEgresoDao  egresoDao  = new RegistroEgresoDao();
    private final PresupuestoMensualDao presDao  = new PresupuestoMensualDao();

    private static final String BASE = "/public/modules/05_Finanzas/";

    // ─── GET ──────────────────────────────────────────────────────────────────

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp");
            return;
        }

        int idHogar = (Integer) session.getAttribute("idHogar");
        int idRol   = (Integer) session.getAttribute("idRol");

        // Rol 3 no tiene acceso a finanzas
        if (idRol == 3) {
            resp.sendRedirect(req.getContextPath() + "/Menu?error=sin_permiso_finanzas");
            return;
        }

        String accion = nvl(req.getParameter("accion"));

        switch (accion) {

            // ── RESUMEN ────────────────────────────────────────────────────
            case "resumen": {
                cargarResumen(req, idHogar);
                fwd(req, resp, "08_ResumenFinanciero.jsp");
                break;
            }

            // ── DETALLE INGRESOS ───────────────────────────────────────────
            case "detalleIngresos": {
                List<RegistroIngreso> ingresos = ingresoDao.listarPorHogar(idHogar);
                BigDecimal total = ingresos.stream().map(RegistroIngreso::getMonto)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                req.setAttribute("ingresos",         ingresos);
                req.setAttribute("totalIngresos",    total);
                req.setAttribute("ingresosAnulados", ingresoDao.listarAnulados(idHogar));
                fwd(req, resp, "04_DetalleIngresos.jsp");
                break;
            }

            // ── FORM NUEVO INGRESO ─────────────────────────────────────────
            case "formIngreso": {
                req.setAttribute("categorias", ingresoDao.listarCategorias());
                req.setAttribute("modoEdicion", false);
                fwd(req, resp, "02_RegistrarIngresos.jsp");
                break;
            }

            // ── FORM EDITAR INGRESO (solo Rol 1) ───────────────────────────
            case "editarIngreso": {
                if (idRol != 1) { resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=detalleIngresos&error=sin_permiso"); return; }
                int id = parseInt(req.getParameter("id"));
                RegistroIngreso ingreso = ingresoDao.obtenerPorId(id, idHogar);
                if (ingreso == null) { resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=detalleIngresos&error=no_encontrado"); return; }
                req.setAttribute("categorias",   ingresoDao.listarCategorias());
                req.setAttribute("ingreso",      ingreso);
                req.setAttribute("modoEdicion",  true);
                fwd(req, resp, "02_RegistrarIngresos.jsp");
                break;
            }

            // ── DETALLE EGRESOS ────────────────────────────────────────────
            case "detalleEgresos": {
                List<RegistroEgreso> egresos = egresoDao.listarPorHogar(idHogar);
                BigDecimal total = egresos.stream().map(RegistroEgreso::getMonto)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                req.setAttribute("egresos",         egresos);
                req.setAttribute("totalEgresos",    total);
                req.setAttribute("egresosAnulados", egresoDao.listarAnulados(idHogar));
                fwd(req, resp, "07_DetalleEgresos.jsp");
                break;
            }

            // ── FORM NUEVO EGRESO ──────────────────────────────────────────
            case "formEgreso": {
                req.setAttribute("categorias", egresoDao.listarCategorias());
                req.setAttribute("modoEdicion", false);
                fwd(req, resp, "05_RegistrarEgresos.jsp");
                break;
            }

            // ── FORM EDITAR EGRESO (solo Rol 1) ────────────────────────────
            case "editarEgreso": {
                if (idRol != 1) { resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=detalleEgresos&error=sin_permiso"); return; }
                int id = parseInt(req.getParameter("id"));
                RegistroEgreso egreso = egresoDao.obtenerPorId(id, idHogar);
                if (egreso == null) { resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=detalleEgresos&error=no_encontrado"); return; }
                req.setAttribute("categorias",  egresoDao.listarCategorias());
                req.setAttribute("egreso",      egreso);
                req.setAttribute("modoEdicion", true);
                fwd(req, resp, "05_RegistrarEgresos.jsp");
                break;
            }

            // ── PRESUPUESTO (solo Rol 1) ────────────────────────────────────
            case "formPresupuesto": {
                if (idRol != 1) {
                    resp.sendRedirect(req.getContextPath() + "/Finanzas?error=sin_permiso");
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
                fwd(req, resp, "09_PresupuestoMensual.jsp");
                break;
            }

            // ── MENÚ PRINCIPAL (default) ────────────────────────────────────
            default: {
                cargarResumen(req, idHogar);
                fwd(req, resp, "01_Finanzas.jsp");
            }
        }
    }

    // ─── POST ─────────────────────────────────────────────────────────────────

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp");
            return;
        }

        int idHogar = (Integer) session.getAttribute("idHogar");
        int idRol   = (Integer) session.getAttribute("idRol");

        if (idRol == 3) {
            resp.sendRedirect(req.getContextPath() + "/Menu?error=sin_permiso_finanzas");
            return;
        }

        String accion = nvl(req.getParameter("accion"));

        switch (accion) {

            // ── GUARDAR INGRESO (nuevo o edición) ──────────────────────────
            case "guardarIngreso": {
                try {
                    BigDecimal monto = parseMonto(req.getParameter("monto"));
                    int idCat        = parseInt(req.getParameter("idCategoriaIngreso"));
                    String desc      = nvl(req.getParameter("descripcion"));
                    String idStr     = nvl(req.getParameter("idIngreso"));

                    RegistroIngreso ingreso = new RegistroIngreso();
                    ingreso.setIdHogar(idHogar);
                    ingreso.setMonto(monto);
                    ingreso.setIdCategoriaIngreso(idCat);
                    ingreso.setDescripcion(desc.isBlank() ? null : desc.trim());

                    boolean ok;
                    boolean esEdicion = !idStr.isEmpty() && parseInt(idStr) > 0;

                    if (esEdicion && idRol == 1) {
                        // Solo Rol 1 puede editar
                        ingreso.setIdIngresos(parseInt(idStr));
                        ok = ingresoDao.actualizar(ingreso);
                    } else {
                        ok = ingresoDao.registrar(ingreso);
                    }

                    if (ok) {
                        fwd(req, resp, "03_RegistroIngresoExito.jsp");
                    } else {
                        req.setAttribute("error", "No se pudo guardar el ingreso.");
                        req.setAttribute("categorias", ingresoDao.listarCategorias());
                        req.setAttribute("modoEdicion", esEdicion);
                        fwd(req, resp, "02_RegistrarIngresos.jsp");
                    }
                } catch (Exception e) {
                    req.setAttribute("error", "Datos inválidos: " + e.getMessage());
                    req.setAttribute("categorias", ingresoDao.listarCategorias());
                    req.setAttribute("modoEdicion", false);
                    fwd(req, resp, "02_RegistrarIngresos.jsp");
                }
                break;
            }

            // ── ANULAR INGRESO (solo Rol 1) ────────────────────────────────
            case "anularIngreso": {
                if (idRol != 1) {
                    resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=detalleIngresos&error=sin_permiso");
                    return;
                }
                int id = parseInt(req.getParameter("idIngreso"));
                boolean ok = ingresoDao.anular(id, idHogar);
                String param = ok ? "exito=ingreso_anulado" : "error=no_se_pudo_anular";
                resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=detalleIngresos&" + param);
                break;
            }

            // ── REACTIVAR INGRESO (solo Rol 1) ─────────────────────────────
            case "reactivarIngreso": {
                if (idRol != 1) {
                    resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=detalleIngresos&error=sin_permiso");
                    return;
                }
                int id = parseInt(req.getParameter("idIngreso"));
                boolean ok = ingresoDao.reactivar(id, idHogar);
                String param = ok ? "exito=ingreso_reactivado" : "error=no_se_pudo_reactivar";
                resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=detalleIngresos&" + param);
                break;
            }

            // ── GUARDAR EGRESO (nuevo o edición) ───────────────────────────
            case "guardarEgreso": {
                try {
                    BigDecimal monto = parseMonto(req.getParameter("monto"));
                    int idCat        = parseInt(req.getParameter("idCategoriaEgreso"));
                    String desc      = nvl(req.getParameter("descripcion"));
                    String idStr     = nvl(req.getParameter("idEgreso"));

                    RegistroEgreso egreso = new RegistroEgreso();
                    egreso.setIdHogar(idHogar);
                    egreso.setMonto(monto);
                    egreso.setIdCategoriaEgreso(idCat);
                    egreso.setDescripcion(desc.isBlank() ? null : desc.trim());

                    boolean ok;
                    boolean esEdicion = !idStr.isEmpty() && parseInt(idStr) > 0;

                    if (esEdicion && idRol == 1) {
                        // Solo Rol 1 puede editar
                        egreso.setIdEgresos(parseInt(idStr));
                        egreso.setIdMetodoPago(1);
                        egreso.setFechaVencimiento(java.time.LocalDateTime.now());
                        egreso.setEstadoPago("Pagada");
                        egreso.setFechaPago(java.time.LocalDateTime.now());
                        ok = egresoDao.actualizar(egreso);
                    } else {
                        ok = egresoDao.registrar(egreso);
                    }

                    if (ok) {
                        fwd(req, resp, "06_RegistroEgresoExito.jsp");
                    } else {
                        req.setAttribute("error", "No se pudo guardar el egreso.");
                        req.setAttribute("categorias", egresoDao.listarCategorias());
                        req.setAttribute("modoEdicion", esEdicion);
                        fwd(req, resp, "05_RegistrarEgresos.jsp");
                    }
                } catch (Exception e) {
                    req.setAttribute("error", "Datos inválidos: " + e.getMessage());
                    req.setAttribute("categorias", egresoDao.listarCategorias());
                    req.setAttribute("modoEdicion", false);
                    fwd(req, resp, "05_RegistrarEgresos.jsp");
                }
                break;
            }

            // ── ANULAR EGRESO (solo Rol 1) ─────────────────────────────────
            case "anularEgreso": {
                if (idRol != 1) {
                    resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=detalleEgresos&error=sin_permiso");
                    return;
                }
                int id = parseInt(req.getParameter("idEgreso"));
                boolean ok = egresoDao.anular(id, idHogar);
                String param = ok ? "exito=egreso_anulado" : "error=no_se_pudo_anular";
                resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=detalleEgresos&" + param);
                break;
            }

            // ── REACTIVAR EGRESO (solo Rol 1) ──────────────────────────────
            case "reactivarEgreso": {
                if (idRol != 1) {
                    resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=detalleEgresos&error=sin_permiso");
                    return;
                }
                int id = parseInt(req.getParameter("idEgreso"));
                boolean ok = egresoDao.reactivar(id, idHogar);
                String param = ok ? "exito=egreso_reactivado" : "error=no_se_pudo_reactivar";
                resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=detalleEgresos&" + param);
                break;
            }

            // ── GUARDAR PRESUPUESTO (solo Rol 1) ───────────────────────────
            case "guardarPresupuesto": {
                if (idRol != 1) {
                    resp.sendRedirect(req.getContextPath() + "/Finanzas?error=sin_permiso");
                    return;
                }
                try {
                    BigDecimal monto = parseMonto(req.getParameter("montoMax"));
                    int mes          = parseInt(req.getParameter("mes"));

                    PresupuestoMensual p = new PresupuestoMensual();
                    p.setIdHogar(idHogar);
                    p.setMontoMax(monto);
                    p.setMes(mes);
                    p.setAnio(java.time.LocalDate.now().getYear());

                    presDao.guardar(p);
                    resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=resumen");
                } catch (Exception e) {
                    req.setAttribute("error", "Datos inválidos: " + e.getMessage());
                    fwd(req, resp, "09_PresupuestoMensual.jsp");
                }
                break;
            }

            default:
                resp.sendRedirect(req.getContextPath() + "/Finanzas");
        }
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    /** Carga los datos comunes del resumen financiero en el request. */
    private void cargarResumen(HttpServletRequest req, int idHogar) {
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
    }

    private BigDecimal parseMonto(String s) {
        if (s == null) throw new NumberFormatException("Monto nulo");
        return new BigDecimal(s.replace(",", "").replace("$", "").replace(" ", "").trim());
    }

    private void fwd(HttpServletRequest req, HttpServletResponse resp, String vista)
            throws ServletException, IOException {
        req.getRequestDispatcher(BASE + vista).forward(req, resp);
    }

    private int parseInt(String s) { try { return Integer.parseInt(s); } catch (Exception e) { return 0; } }
    private String nvl(String s)   { return s == null ? "" : s; }
}
