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

/* Clase: FinanzasServlet
   Propósito: Gestionar todas las operaciones del módulo Finanzas.
   Controla ingresos, egresos y presupuesto mensual del hogar.
   Los roles 1 y 2 tienen acceso; el rol 3 (Invitado) no puede acceder.

   ACCIONES GET:
     (default/menu)    → 01_Finanzas.jsp (menú principal del módulo)
     resumen           → 08_ResumenFinanciero.jsp
     detalleIngresos   → 04_DetalleIngresos.jsp
     formIngreso       → 02_RegistrarIngresos.jsp (nuevo ingreso)
     editarIngreso     → 02_RegistrarIngresos.jsp (modo edición, solo Rol 1)
     detalleEgresos    → 07_DetalleEgresos.jsp
     formEgreso        → 05_RegistrarEgresos.jsp (nuevo egreso)
     editarEgreso      → 05_RegistrarEgresos.jsp (modo edición, solo Rol 1)
     formPresupuesto   → 09_PresupuestoMensual.jsp (solo Rol 1)

   ACCIONES POST:
     guardarIngreso    → registra o actualiza un ingreso
     anularIngreso     → soft-delete de ingreso (solo Rol 1)
     reactivarIngreso  → reactiva un ingreso anulado (solo Rol 1)
     guardarEgreso     → registra o actualiza un egreso
     anularEgreso      → soft-delete de egreso (solo Rol 1)
     reactivarEgreso   → reactiva un egreso anulado (solo Rol 1)
     guardarPresupuesto→ guarda el presupuesto mensual del hogar (solo Rol 1)

   URL mapeada: /Finanzas
*/

@WebServlet("/Finanzas")
public class FinanzasServlet extends HttpServlet {

    // Instancia del DAO de ingresos reutilizable en todos los métodos del servlet
    private final RegistroIngresoDao ingresoDao = new RegistroIngresoDao();
    // Instancia del DAO de egresos reutilizable en todos los métodos del servlet
    private final RegistroEgresoDao  egresoDao  = new RegistroEgresoDao();
    // Instancia del DAO de presupuesto mensual reutilizable en todos los métodos
    private final PresupuestoMensualDao presDao  = new PresupuestoMensualDao();

    // Constante de tipo texto con la ruta base de las vistas del módulo de finanzas
    private static final String BASE = "/public/modules/05_Finanzas/";

    /* Método: doGet
       Propósito: Mostrar las vistas del módulo de finanzas según la acción solicitada.
       Bloquea el acceso al rol 3 y enruta a la vista o acción correspondiente.
       @param req  → Objeto con el parámetro "accion" en la URL
       @param resp → Objeto para hacer forward o redirigir a la vista
    */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Verifica que haya sesión activa con usuario autenticado
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp");
            return;
        }

        // Variable entera que almacena el ID del hogar del usuario en sesión
        int idHogar = (Integer) session.getAttribute("idHogar");
        // Variable entera que almacena el ID del rol del usuario (1=Admin, 2=Cotitular, 3=Invitado)
        int idRol   = (Integer) session.getAttribute("idRol");

        // El rol 3 (Invitado) no tiene acceso al módulo de finanzas
        if (idRol == 3) {
            resp.sendRedirect(req.getContextPath() + "/Menu?error=sin_permiso_finanzas");
            return;
        }

        // Variable de tipo texto que almacena la acción solicitada por parámetro URL
        String accion = nvl(req.getParameter("accion"));

        switch (accion) {

            // ── RESUMEN ────────────────────────────────────────────────────
            case "resumen": {
                // Carga los totales del mes y el presupuesto para la vista de resumen financiero
                cargarResumen(req, idHogar);
                fwd(req, resp, "08_ResumenFinanciero.jsp");
                break;
            }

            // ── DETALLE INGRESOS ───────────────────────────────────────────
            case "detalleIngresos": {
                // Lista de objetos RegistroIngreso con todos los ingresos activos del hogar
                List<RegistroIngreso> ingresos = ingresoDao.listarPorHogar(idHogar);
                // Variable de tipo BigDecimal que almacena la suma total de todos los ingresos activos
                BigDecimal total = ingresos.stream().map(RegistroIngreso::getMonto)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                // Asigna la lista de ingresos activos como atributo para que la vista los itere
                req.setAttribute("ingresos",         ingresos);
                // Asigna el total acumulado de ingresos como atributo para mostrarlo en la vista
                req.setAttribute("totalIngresos",    total);
                // Asigna la lista de ingresos anulados (soft-delete) para mostrarlos en sección aparte
                req.setAttribute("ingresosAnulados", ingresoDao.listarAnulados(idHogar));
                fwd(req, resp, "04_DetalleIngresos.jsp");
                break;
            }

            // ── FORM NUEVO INGRESO ─────────────────────────────────────────
            case "formIngreso": {
                // Carga las categorías de ingreso disponibles para el selector del formulario
                req.setAttribute("categorias", ingresoDao.listarCategorias());
                // Indica a la vista que está en modo creación (no edición)
                req.setAttribute("modoEdicion", false);
                fwd(req, resp, "02_RegistrarIngresos.jsp");
                break;
            }

            // ── FORM EDITAR INGRESO (solo Rol 1) ───────────────────────────
            case "editarIngreso": {
                // Solo el rol 1 (Administrador) puede editar ingresos
                if (idRol != 1) { resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=detalleIngresos&error=sin_permiso"); return; }
                // Variable entera que almacena el ID del ingreso a editar; 0 si no se puede parsear
                int id = parseInt(req.getParameter("id"));
                // Objeto RegistroIngreso que almacena los datos del ingreso encontrado; null si no existe
                RegistroIngreso ingreso = ingresoDao.obtenerPorId(id, idHogar);
                // Si el ingreso no existe o no pertenece al hogar, redirige con error
                if (ingreso == null) { resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=detalleIngresos&error=no_encontrado"); return; }
                // Carga las categorías para el selector del formulario de edición
                req.setAttribute("categorias",   ingresoDao.listarCategorias());
                // Asigna el ingreso a editar para que la vista precargue sus datos en el formulario
                req.setAttribute("ingreso",      ingreso);
                // Indica a la vista que está en modo edición
                req.setAttribute("modoEdicion",  true);
                fwd(req, resp, "02_RegistrarIngresos.jsp");
                break;
            }

            // ── DETALLE EGRESOS ────────────────────────────────────────────
            case "detalleEgresos": {
                // Lista de objetos RegistroEgreso con todos los egresos activos del hogar
                List<RegistroEgreso> egresos = egresoDao.listarPorHogar(idHogar);
                // Variable de tipo BigDecimal que almacena la suma total de todos los egresos activos
                BigDecimal total = egresos.stream().map(RegistroEgreso::getMonto)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                // Asigna la lista de egresos activos como atributo para que la vista los itere
                req.setAttribute("egresos",         egresos);
                // Asigna el total acumulado de egresos como atributo para mostrarlo en la vista
                req.setAttribute("totalEgresos",    total);
                // Asigna la lista de egresos anulados (soft-delete) para mostrarlos en sección aparte
                req.setAttribute("egresosAnulados", egresoDao.listarAnulados(idHogar));
                fwd(req, resp, "07_DetalleEgresos.jsp");
                break;
            }

            // ── FORM NUEVO EGRESO ──────────────────────────────────────────
            case "formEgreso": {
                // Carga las categorías de egreso disponibles para el selector del formulario
                req.setAttribute("categorias", egresoDao.listarCategorias());
                // Indica a la vista que está en modo creación (no edición)
                req.setAttribute("modoEdicion", false);
                fwd(req, resp, "05_RegistrarEgresos.jsp");
                break;
            }

            // ── FORM EDITAR EGRESO (solo Rol 1) ────────────────────────────
            case "editarEgreso": {
                // Solo el rol 1 (Administrador) puede editar egresos
                if (idRol != 1) { resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=detalleEgresos&error=sin_permiso"); return; }
                // Variable entera que almacena el ID del egreso a editar; 0 si no se puede parsear
                int id = parseInt(req.getParameter("id"));
                // Objeto RegistroEgreso que almacena los datos del egreso encontrado; null si no existe
                RegistroEgreso egreso = egresoDao.obtenerPorId(id, idHogar);
                // Si el egreso no existe o no pertenece al hogar, redirige con error
                if (egreso == null) { resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=detalleEgresos&error=no_encontrado"); return; }
                // Carga las categorías para el selector del formulario de edición
                req.setAttribute("categorias",  egresoDao.listarCategorias());
                // Asigna el egreso a editar para que la vista precargue sus datos en el formulario
                req.setAttribute("egreso",      egreso);
                // Indica a la vista que está en modo edición
                req.setAttribute("modoEdicion", true);
                fwd(req, resp, "05_RegistrarEgresos.jsp");
                break;
            }

            // ── PRESUPUESTO (solo Rol 1) ────────────────────────────────────
            case "formPresupuesto": {
                // Solo el rol 1 (Administrador) puede gestionar el presupuesto mensual
                if (idRol != 1) {
                    resp.sendRedirect(req.getContextPath() + "/Finanzas?error=sin_permiso");
                    return;
                }
                // Objeto PresupuestoMensual con el presupuesto configurado para el mes actual; null si no existe
                PresupuestoMensual pres = presDao.obtenerMesActual(idHogar);
                // Variable de tipo BigDecimal que almacena el total de egresos del mes actual
                BigDecimal totalEgresos = presDao.totalEgresosMesActual(idHogar);
                if (pres != null) {
                    // Asigna el total de egresos al objeto presupuesto para calcular disponible
                    pres.setTotalEgresos(totalEgresos);
                    // Calcula y asigna el monto disponible: presupuesto máximo menos total de egresos
                    pres.setDisponible(pres.getMontoMax().subtract(totalEgresos));
                }
                // Asigna el objeto de presupuesto como atributo; puede ser null si no hay presupuesto configurado
                req.setAttribute("presupuesto",  pres);
                // Asigna el total de egresos del mes como atributo para mostrarlo en la vista
                req.setAttribute("totalEgresos", totalEgresos);
                fwd(req, resp, "09_PresupuestoMensual.jsp");
                break;
            }

            // ── MENÚ PRINCIPAL (default) ────────────────────────────────────
            default: {
                // Carga el resumen financiero y muestra el menú principal del módulo
                cargarResumen(req, idHogar);
                fwd(req, resp, "01_Finanzas.jsp");
            }
        }
    }

    /* Método: doPost
       Propósito: Procesar las acciones que modifican datos del módulo de finanzas:
       guardar ingresos/egresos (nuevos o editados), anularlos, reactivarlos
       y guardar el presupuesto mensual del hogar.
       @param req  → Objeto con los datos del formulario enviado
       @param resp → Objeto para hacer forward o redirigir al resultado
    */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Verifica que haya sesión activa con usuario autenticado
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp");
            return;
        }

        // Variable entera que almacena el ID del hogar del usuario en sesión
        int idHogar = (Integer) session.getAttribute("idHogar");
        // Variable entera que almacena el ID del rol del usuario
        int idRol   = (Integer) session.getAttribute("idRol");

        // El rol 3 no puede realizar operaciones sobre finanzas
        if (idRol == 3) {
            resp.sendRedirect(req.getContextPath() + "/Menu?error=sin_permiso_finanzas");
            return;
        }

        // Variable de tipo texto que almacena la acción enviada por el formulario
        String accion = nvl(req.getParameter("accion"));

        switch (accion) {

            // ── GUARDAR INGRESO (nuevo o edición) ──────────────────────────
            case "guardarIngreso": {
                try {
                    // Variable de tipo BigDecimal que almacena el monto del ingreso parseado
                    BigDecimal monto = parseMonto(req.getParameter("monto"));
                    // Variable entera que almacena el ID de la categoría de ingreso seleccionada
                    int idCat        = parseInt(req.getParameter("idCategoriaIngreso"));
                    // Variable de tipo texto que almacena la descripción opcional del ingreso
                    String desc      = nvl(req.getParameter("descripcion"));
                    // Variable de tipo texto que almacena el ID del ingreso si es edición; vacío si es nuevo
                    String idStr     = nvl(req.getParameter("idIngreso"));

                    // Objeto RegistroIngreso que almacena los datos del ingreso a registrar o actualizar
                    RegistroIngreso ingreso = new RegistroIngreso();
                    ingreso.setIdHogar(idHogar);
                    ingreso.setMonto(monto);
                    ingreso.setIdCategoriaIngreso(idCat);
                    // Asigna null si la descripción está en blanco; de lo contrario la guarda sin espacios
                    ingreso.setDescripcion(desc.isBlank() ? null : desc.trim());

                    // Variable booleana que indica si la operación (insertar o actualizar) fue exitosa
                    boolean ok;
                    // Variable booleana que indica si la acción es edición (true) o creación (false)
                    boolean esEdicion = !idStr.isEmpty() && parseInt(idStr) > 0;

                    if (esEdicion && idRol == 1) {
                        // Solo el rol 1 puede editar; asigna el ID del ingreso a actualizar
                        ingreso.setIdIngresos(parseInt(idStr));
                        ok = ingresoDao.actualizar(ingreso);
                    } else {
                        // Si no es edición o no tiene permiso para editar, registra como nuevo ingreso
                        ok = ingresoDao.registrar(ingreso);
                    }

                    if (ok) {
                        // Si la operación fue exitosa, muestra la vista de confirmación
                        fwd(req, resp, "03_RegistroIngresoExito.jsp");
                    } else {
                        // Si falló, muestra el formulario con el mensaje de error
                        req.setAttribute("error", "No se pudo guardar el ingreso.");
                        req.setAttribute("categorias", ingresoDao.listarCategorias());
                        req.setAttribute("modoEdicion", esEdicion);
                        fwd(req, resp, "02_RegistrarIngresos.jsp");
                    }
                } catch (Exception e) {
                    // Si los datos son inválidos (ej. monto no numérico), muestra el formulario con error
                    req.setAttribute("error", "Datos inválidos: " + e.getMessage());
                    req.setAttribute("categorias", ingresoDao.listarCategorias());
                    req.setAttribute("modoEdicion", false);
                    fwd(req, resp, "02_RegistrarIngresos.jsp");
                }
                break;
            }

            // ── ANULAR INGRESO (solo Rol 1) ────────────────────────────────
            case "anularIngreso": {
                // Solo el rol 1 (Administrador) puede anular ingresos
                if (idRol != 1) {
                    resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=detalleIngresos&error=sin_permiso");
                    return;
                }
                // Variable entera que almacena el ID del ingreso a anular
                int id = parseInt(req.getParameter("idIngreso"));
                // Variable booleana que indica si la anulación fue exitosa en la base de datos
                boolean ok = ingresoDao.anular(id, idHogar);
                // Variable de tipo texto que almacena el parámetro de resultado para la redirección
                String param = ok ? "exito=ingreso_anulado" : "error=no_se_pudo_anular";
                resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=detalleIngresos&" + param);
                break;
            }

            // ── REACTIVAR INGRESO (solo Rol 1) ─────────────────────────────
            case "reactivarIngreso": {
                // Solo el rol 1 (Administrador) puede reactivar ingresos anulados
                if (idRol != 1) {
                    resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=detalleIngresos&error=sin_permiso");
                    return;
                }
                // Variable entera que almacena el ID del ingreso anulado a reactivar
                int id = parseInt(req.getParameter("idIngreso"));
                // Variable booleana que indica si la reactivación fue exitosa en la base de datos
                boolean ok = ingresoDao.reactivar(id, idHogar);
                // Variable de tipo texto que almacena el parámetro de resultado para la redirección
                String param = ok ? "exito=ingreso_reactivado" : "error=no_se_pudo_reactivar";
                resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=detalleIngresos&" + param);
                break;
            }

            // ── GUARDAR EGRESO (nuevo o edición) ───────────────────────────
            case "guardarEgreso": {
                try {
                    // Variable de tipo BigDecimal que almacena el monto del egreso parseado
                    BigDecimal monto = parseMonto(req.getParameter("monto"));
                    // Variable entera que almacena el ID de la categoría de egreso seleccionada
                    int idCat        = parseInt(req.getParameter("idCategoriaEgreso"));
                    // Variable de tipo texto que almacena la descripción opcional del egreso
                    String desc      = nvl(req.getParameter("descripcion"));
                    // Variable de tipo texto que almacena el ID del egreso si es edición; vacío si es nuevo
                    String idStr     = nvl(req.getParameter("idEgreso"));

                    // Objeto RegistroEgreso que almacena los datos del egreso a registrar o actualizar
                    RegistroEgreso egreso = new RegistroEgreso();
                    egreso.setIdHogar(idHogar);
                    egreso.setMonto(monto);
                    egreso.setIdCategoriaEgreso(idCat);
                    // Asigna null si la descripción está en blanco; de lo contrario la guarda sin espacios
                    egreso.setDescripcion(desc.isBlank() ? null : desc.trim());

                    // Variable booleana que indica si la operación (insertar o actualizar) fue exitosa
                    boolean ok;
                    // Variable booleana que indica si la acción es edición (true) o creación (false)
                    boolean esEdicion = !idStr.isEmpty() && parseInt(idStr) > 0;

                    if (esEdicion && idRol == 1) {
                        // Solo el rol 1 puede editar; asigna el ID del egreso y campos obligatorios
                        egreso.setIdEgresos(parseInt(idStr));
                        // Asigna método de pago por defecto (Efectivo = 1) requerido por la tabla
                        egreso.setIdMetodoPago(1);
                        // Asigna la fecha de vencimiento y pago con la fecha y hora actual
                        egreso.setFechaVencimiento(java.time.LocalDateTime.now());
                        egreso.setEstadoPago("Pagada");
                        egreso.setFechaPago(java.time.LocalDateTime.now());
                        ok = egresoDao.actualizar(egreso);
                    } else {
                        // Si no es edición o no tiene permiso, registra como nuevo egreso
                        ok = egresoDao.registrar(egreso);
                    }

                    if (ok) {
                        // Si la operación fue exitosa, muestra la vista de confirmación
                        fwd(req, resp, "06_RegistroEgresoExito.jsp");
                    } else {
                        // Si falló, muestra el formulario con el mensaje de error
                        req.setAttribute("error", "No se pudo guardar el egreso.");
                        req.setAttribute("categorias", egresoDao.listarCategorias());
                        req.setAttribute("modoEdicion", esEdicion);
                        fwd(req, resp, "05_RegistrarEgresos.jsp");
                    }
                } catch (Exception e) {
                    // Si los datos son inválidos (ej. monto no numérico), muestra el formulario con error
                    req.setAttribute("error", "Datos inválidos: " + e.getMessage());
                    req.setAttribute("categorias", egresoDao.listarCategorias());
                    req.setAttribute("modoEdicion", false);
                    fwd(req, resp, "05_RegistrarEgresos.jsp");
                }
                break;
            }

            // ── ANULAR EGRESO (solo Rol 1) ─────────────────────────────────
            case "anularEgreso": {
                // Solo el rol 1 (Administrador) puede anular egresos
                if (idRol != 1) {
                    resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=detalleEgresos&error=sin_permiso");
                    return;
                }
                // Variable entera que almacena el ID del egreso a anular
                int id = parseInt(req.getParameter("idEgreso"));
                // Variable booleana que indica si la anulación fue exitosa en la base de datos
                boolean ok = egresoDao.anular(id, idHogar);
                // Variable de tipo texto que almacena el parámetro de resultado para la redirección
                String param = ok ? "exito=egreso_anulado" : "error=no_se_pudo_anular";
                resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=detalleEgresos&" + param);
                break;
            }

            // ── REACTIVAR EGRESO (solo Rol 1) ──────────────────────────────
            case "reactivarEgreso": {
                // Solo el rol 1 (Administrador) puede reactivar egresos anulados
                if (idRol != 1) {
                    resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=detalleEgresos&error=sin_permiso");
                    return;
                }
                // Variable entera que almacena el ID del egreso anulado a reactivar
                int id = parseInt(req.getParameter("idEgreso"));
                // Variable booleana que indica si la reactivación fue exitosa en la base de datos
                boolean ok = egresoDao.reactivar(id, idHogar);
                // Variable de tipo texto que almacena el parámetro de resultado para la redirección
                String param = ok ? "exito=egreso_reactivado" : "error=no_se_pudo_reactivar";
                resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=detalleEgresos&" + param);
                break;
            }

            // ── GUARDAR PRESUPUESTO (solo Rol 1) ───────────────────────────
            case "guardarPresupuesto": {
                // Solo el rol 1 (Administrador) puede configurar el presupuesto mensual
                if (idRol != 1) {
                    resp.sendRedirect(req.getContextPath() + "/Finanzas?error=sin_permiso");
                    return;
                }
                try {
                    // Variable de tipo BigDecimal que almacena el monto máximo del presupuesto parseado
                    BigDecimal monto = parseMonto(req.getParameter("montoMax"));
                    // Variable entera que almacena el número del mes al que aplica el presupuesto (1-12)
                    int mes          = parseInt(req.getParameter("mes"));

                    // Objeto PresupuestoMensual que almacena los datos a guardar en la base de datos
                    PresupuestoMensual p = new PresupuestoMensual();
                    p.setIdHogar(idHogar);
                    p.setMontoMax(monto);
                    p.setMes(mes);
                    // Asigna el año actual obtenido del sistema como año del presupuesto
                    p.setAnio(java.time.LocalDate.now().getYear());

                    // Inserta o actualiza el presupuesto en la base de datos
                    presDao.guardar(p);
                    // Redirige al resumen financiero para mostrar el presupuesto actualizado
                    resp.sendRedirect(req.getContextPath() + "/Finanzas?accion=resumen");
                } catch (Exception e) {
                    // Si los datos son inválidos, muestra el formulario con mensaje de error
                    req.setAttribute("error", "Datos inválidos: " + e.getMessage());
                    fwd(req, resp, "09_PresupuestoMensual.jsp");
                }
                break;
            }

            default:
                // Si la acción no es reconocida, redirige al módulo de finanzas
                resp.sendRedirect(req.getContextPath() + "/Finanzas");
        }
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    /* Método: cargarResumen
       Propósito: Calcular y asignar al request los totales de ingresos, egresos,
       disponible y el objeto de presupuesto del mes actual para las vistas de resumen.
       @param req     → Objeto de la petición HTTP donde se asignan los atributos
       @param idHogar → Entero con el ID del hogar para filtrar los datos
    */
    private void cargarResumen(HttpServletRequest req, int idHogar) {
        // Variable de tipo BigDecimal que almacena el total de ingresos del mes actual
        BigDecimal totalIngresos = ingresoDao.totalMesActual(idHogar);
        // Variable de tipo BigDecimal que almacena el total de egresos del mes actual
        BigDecimal totalEgresos  = presDao.totalEgresosMesActual(idHogar);
        // Variable de tipo BigDecimal que almacena la diferencia entre ingresos y egresos del mes
        BigDecimal disponible    = totalIngresos.subtract(totalEgresos);
        // Objeto PresupuestoMensual con el presupuesto configurado para el mes actual; null si no existe
        PresupuestoMensual pres  = presDao.obtenerMesActual(idHogar);
        if (pres != null) {
            // Asigna el total de egresos al objeto para poder calcular el disponible respecto al presupuesto
            pres.setTotalEgresos(totalEgresos);
            // Calcula y asigna cuánto queda disponible del presupuesto máximo configurado
            pres.setDisponible(pres.getMontoMax().subtract(totalEgresos));
        }
        // Asigna los atributos al request para que las vistas los muestren
        req.setAttribute("totalIngresos", totalIngresos);
        req.setAttribute("totalEgresos",  totalEgresos);
        req.setAttribute("disponible",    disponible);
        req.setAttribute("presupuesto",   pres);
    }

    /* Método: parseMonto
       Propósito: Convertir un texto con formato de monto a BigDecimal de forma segura,
       eliminando caracteres no numéricos como comas, signo $ y espacios.
       @param s → Texto con el monto a parsear (ej. "$1,500.00")
       @return BigDecimal → Valor numérico resultante
    */
    private BigDecimal parseMonto(String s) {
        // Si el texto es null lanza excepción para que el bloque catch la maneje con mensaje de error
        if (s == null) throw new NumberFormatException("Monto nulo");
        // Elimina comas, signo $ y espacios antes de convertir a BigDecimal
        return new BigDecimal(s.replace(",", "").replace("$", "").replace(" ", "").trim());
    }

    /* Método: fwd
       Propósito: Hacer forward a una vista JSP del módulo de finanzas usando la ruta
       base definida en la constante BASE.
       @param req   → Objeto de la petición HTTP
       @param resp  → Objeto de respuesta HTTP
       @param vista → Nombre del archivo JSP a mostrar (sin la ruta base)
    */
    private void fwd(HttpServletRequest req, HttpServletResponse resp, String vista)
            throws ServletException, IOException {
        req.getRequestDispatcher(BASE + vista).forward(req, resp);
    }

    /* Método: parseInt
       Propósito: Convertir texto a entero de forma segura, retornando 0 si la
       conversión falla para evitar excepciones en tiempo de ejecución.
       @param s → Texto a convertir
       @return int → Entero resultante, o 0 si la conversión falla
    */
    private int parseInt(String s) { try { return Integer.parseInt(s); } catch (Exception e) { return 0; } }

    /* Método: nvl
       Propósito: Retornar la cadena recibida sin modificar, o una cadena vacía si es null.
       Equivalente al NVL de SQL para evitar NullPointerException en operaciones con texto.
       @param s → Texto a evaluar
       @return String → El mismo texto, o "" si era null
    */
    private String nvl(String s)   { return s == null ? "" : s; }
}
