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
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/* Clase: FacturasServlet
   Propósito: Gestionar todas las operaciones del módulo de Facturas (egresos) del hogar,
   incluyendo el dashboard de resumen, registro, edición, eliminación, marcado como pagada,
   filtrado por estado, categoría y fecha, e historial de pagos con filtros por categoría,
   fecha y rango de monto. Solo los roles 1 (Administrador) y 2 (Cotitular) pueden
   modificar datos; el rol 3 (Invitado) solo puede consultar.
   URL mapeada: /Facturas
*/
@WebServlet("/Facturas")
public class FacturasServlet extends HttpServlet {

    // Constante de tipo texto con la ruta base de las vistas JSP del módulo de facturas
    private static final String BASE_JSP = "/public/modules/02_Gestion_facturas-recordatorios/";

    /* Método: doGet
       Propósito: Manejar las solicitudes GET del módulo de facturas. Enruta la petición
       según el parámetro "accion" hacia el dashboard, formularios, listas con filtros
       o historial de pagos. Si no hay sesión activa, redirige al login.
       @param req  → Objeto con los parámetros "accion" y los filtros opcionales en la URL
       @param resp → Objeto para hacer forward a la vista correspondiente o redirigir
    */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Variable de sesión HTTP; null si no existe sesión activa
        HttpSession session = req.getSession(false);
        // Si no hay sesión o el usuario no está autenticado, redirige al login
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_requerida");
            return;
        }
        // Variable entera que almacena el ID del hogar del usuario en sesión
        int idHogar = (Integer) session.getAttribute("idHogar");
        // Variable de tipo texto que almacena la acción solicitada en la URL
        String accion = req.getParameter("accion");
        // Si no se especifica acción, se muestra el dashboard por defecto
        if (accion == null) accion = "dashboard";
        // Instancia del DAO de egresos para consultar y operar sobre las facturas
        RegistroEgresoDao dao = new RegistroEgresoDao();
        // Actualiza automáticamente el estado de facturas vencidas antes de mostrar datos
        dao.actualizarVencidas();

        // Enruta la petición según la acción recibida
        switch (accion) {
            case "dashboard":
                // Muestra el resumen de facturas: pendientes, pagadas y vencidas
                mostrarDashboard(req, resp, dao, idHogar);
                break;

            case "form":
                // Muestra el formulario vacío para registrar una nueva factura
                mostrarFormularioRegistro(req, resp);
                break;

            case "lista":
                // Muestra todas las facturas del hogar sin ningún filtro aplicado
                mostrarLista(req, resp, dao, idHogar, null, null, null, null);
                break;

            case "filtroEstado":
                // Variable de tipo texto que almacena el estado recibido como filtro (ej. "Pendiente")
                String estado = req.getParameter("estado");
                // Si el estado es válido, registra el tipo de filtro activo para la vista
                if (estado != null && !estado.isEmpty())
                    req.setAttribute("modoFiltro", "estado");
                // Muestra la lista filtrada por estado; los demás filtros quedan en null
                mostrarLista(req, resp, dao, idHogar, estado, null, null, null);
                break;

            case "filtroCategoria":
                // Variable de tipo texto que almacena el ID de categoría recibido como parámetro
                String catParam = req.getParameter("idCategoria");
                // Variable entera (nullable) que almacena el ID de categoría convertido; null si vacío
                Integer idCat = (catParam == null || catParam.isEmpty()) ? null : Integer.parseInt(catParam);
                // Si la categoría es válida, registra el tipo de filtro activo para la vista
                if (idCat != null)
                    req.setAttribute("modoFiltro", "categoria");
                // Muestra la lista filtrada por categoría; los demás filtros quedan en null
                mostrarLista(req, resp, dao, idHogar, null, idCat, null, null);
                break;

            case "filtroFecha":
                // Variable entera que almacena el mes recibido como filtro; 0 si no se puede parsear
                Integer mes = parsearInt(req.getParameter("mes"));
                // Variable entera que almacena el año recibido como filtro; 0 si no se puede parsear
                Integer anio = parsearInt(req.getParameter("anio"));
                // Si ambos valores son válidos, registra el tipo de filtro activo para la vista
                if (mes > 0 && anio > 0)
                    req.setAttribute("modoFiltro", "fecha");
                // Muestra la lista filtrada por mes y año; los demás filtros quedan en null
                mostrarLista(req, resp, dao, idHogar, null, null, mes, anio);
                break;

            case "editar":
                // Busca la factura por ID y muestra el formulario de edición con sus datos
                mostrarFormularioEditar(req, resp, dao, idHogar);
                break;

            case "confirmarEliminar":
                // Busca la factura por ID y muestra la vista de confirmación de eliminación
                mostrarConfirmarEliminar(req, resp, dao, idHogar);
                break;

            case "historial":
                // Muestra el historial de facturas pagadas sin ningún filtro aplicado
                mostrarHistorial(req, resp, dao, idHogar, null, null, null, null);
                break;

            case "historialCategoria":
                // Variable de tipo texto que almacena el ID de categoría para filtrar el historial
                String catH = req.getParameter("idCategoria");
                // Variable entera (nullable) que almacena el ID convertido; null si vacío o inválido
                Integer idCatH = (catH == null || catH.isEmpty()) ? null : Integer.parseInt(catH);
                // Muestra el historial filtrado por categoría; los demás filtros quedan en null
                mostrarHistorial(req, resp, dao, idHogar, idCatH, null, null, null);
                break;

            case "historialFecha":
                // Variable entera (nullable) que almacena el mes para filtrar el historial
                Integer mesH  = parsearIntNullable(req.getParameter("mes"));
                // Variable entera (nullable) que almacena el año para filtrar el historial
                Integer anioH = parsearIntNullable(req.getParameter("anio"));
                // Muestra el historial filtrado por mes y año; los demás filtros quedan en null
                mostrarHistorial(req, resp, dao, idHogar, null, mesH, anioH, null);
                break;

            case "historialMonto":
                // Muestra el historial filtrado por rango de monto (ej. "menor50", "50a150", "mayor200")
                mostrarHistorial(req, resp, dao, idHogar, null, null, null, req.getParameter("rango"));
                break;

            default:
                // Si la acción no es reconocida, muestra el dashboard por defecto
                mostrarDashboard(req, resp, dao, idHogar);
        }
    }

    /* Método: doPost
       Propósito: Manejar las solicitudes POST del módulo de facturas. Procesa las
       operaciones de modificación: registrar nueva factura, actualizar una existente,
       eliminar (anular) una factura y marcar una factura como pagada. Valida sesión
       activa antes de ejecutar cualquier operación.
       @param req  → Objeto con los parámetros "accion" y los campos del formulario
       @param resp → Objeto para redirigir según el resultado de la operación
    */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Establece la codificación UTF-8 para aceptar caracteres especiales en los formularios
        req.setCharacterEncoding("UTF-8");
        // Variable de sesión HTTP; null si no existe sesión activa
        HttpSession session = req.getSession(false);
        // Si no hay sesión o el usuario no está autenticado, redirige al login
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_requerida");
            return;
        }

        // Objeto Usuario que almacena los datos del usuario autenticado en sesión
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        // Variable entera que almacena el ID del hogar del usuario en sesión
        int idHogar = (Integer) session.getAttribute("idHogar");
        // Variable entera que almacena el ID del rol del usuario en sesión
        int idRol   = (Integer) session.getAttribute("idRol");
        // Variable de tipo texto que almacena la acción POST solicitada
        String accion = req.getParameter("accion");
        // Si la acción es null, se asigna cadena vacía para evitar NullPointerException
        if (accion == null) accion = "";

        // Enruta la operación POST según la acción recibida
        switch (accion) {
            case "registrar":
                // Procesa el registro de una nueva factura con los datos del formulario
                procesarRegistro(req, resp, usuario, idHogar, idRol);
                break;

            case "actualizar":
                // Procesa la actualización de una factura existente identificada por su ID
                procesarActualizacion(req, resp, idHogar, idRol);
                break;

            case "eliminar":
                // Procesa la anulación (eliminación lógica) de una factura existente
                procesarEliminacion(req, resp, idHogar, idRol);
                break;

            case "marcarPagada":
                // Procesa el cambio de estado de una factura a "Pagada" con fecha actual
                procesarMarcarPagada(req, resp, idHogar, idRol);
                break;

            default:
                // Si la acción no es reconocida, redirige al dashboard de facturas
                resp.sendRedirect(req.getContextPath() + "/Facturas");
        }
    }

    /* Método: mostrarDashboard
       Propósito: Obtener el resumen de facturas del hogar (cantidades de pendientes,
       pagadas y vencidas) y mostrarlo en la vista principal del módulo.
       @param req     → Objeto de la petición HTTP donde se asignan los atributos del resumen
       @param resp    → Objeto para hacer forward a la vista
       @param dao     → Instancia del DAO de egresos para consultar el resumen
       @param idHogar → Entero con el ID del hogar para filtrar las facturas
    */
    private void mostrarDashboard(HttpServletRequest req, HttpServletResponse resp,
                                  RegistroEgresoDao dao, int idHogar)
            throws ServletException, IOException {
        // Arreglo de enteros: posición 0 = pendientes, posición 1 = pagadas, posición 2 = vencidas
        int[] resumen = dao.obtenerResumen(idHogar);
        // Asigna la cantidad de facturas pendientes como atributo para la vista
        req.setAttribute("pendientes", resumen[0]);
        // Asigna la cantidad de facturas pagadas como atributo para la vista
        req.setAttribute("pagadas",    resumen[1]);
        // Asigna la cantidad de facturas vencidas como atributo para la vista
        req.setAttribute("vencidas",   resumen[2]);
        forward(req, resp, "01_facturas.jsp");
    }

    /* Método: mostrarFormularioRegistro
       Propósito: Cargar los catálogos necesarios (categorías y métodos de pago)
       y mostrar el formulario vacío para registrar una nueva factura.
       @param req  → Objeto de la petición HTTP donde se asignan los catálogos
       @param resp → Objeto para hacer forward al formulario de registro
    */
    private void mostrarFormularioRegistro(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Carga las listas de categorías de egreso y métodos de pago en el request
        cargarCatalogos(req);
        forward(req, resp, "02_formularioRegistrar-facturas.jsp");
    }

    /* Método: mostrarLista
       Propósito: Obtener la lista de facturas del hogar aplicando el filtro indicado
       (estado, categoría o fecha) y mostrarla en la vista de consulta. Si todos los
       filtros son null, muestra todas las facturas sin filtrar.
       @param req         → Objeto de la petición HTTP
       @param resp        → Objeto para hacer forward a la vista de lista
       @param dao         → Instancia del DAO de egresos
       @param idHogar     → Entero con el ID del hogar
       @param estado      → Texto con el estado a filtrar ("Pendiente", "Pagada", "Vencida"); null si no aplica
       @param idCategoria → Entero (nullable) con el ID de categoría a filtrar; null si no aplica
       @param mes         → Entero (nullable) con el mes a filtrar (1-12); null si no aplica
       @param anio        → Entero (nullable) con el año a filtrar (ej. 2025); null si no aplica
    */
    private void mostrarLista(HttpServletRequest req, HttpServletResponse resp,
                              RegistroEgresoDao dao, int idHogar,
                              String estado, Integer idCategoria, Integer mes, Integer anio)
            throws ServletException, IOException {

        // Lista de objetos RegistroEgreso que almacenará las facturas a mostrar
        List<RegistroEgreso> facturas;
        // Variable de tipo texto que indica qué filtro está activo; "ninguno" por defecto
        String filtroActivo = "ninguno";

        if (estado != null && !estado.isEmpty()) {
            // Consulta facturas del hogar que coincidan con el estado indicado
            facturas = dao.listarPorEstado(idHogar, estado);
            filtroActivo = "estado";
            // Pasa el estado filtrado a la vista para mostrar el selector activo
            req.setAttribute("estadoFiltro", estado);
        } else if (idCategoria != null) {
            // Consulta facturas del hogar que pertenezcan a la categoría indicada
            facturas = dao.listarPorCategoria(idHogar, idCategoria);
            filtroActivo = "categoria";
            // Pasa el ID de categoría filtrada a la vista para resaltar la opción activa
            req.setAttribute("categoriaFiltro", idCategoria);
        } else if (mes != null && mes > 0 && anio != null && anio > 0) {
            // Consulta facturas del hogar cuya fecha de vencimiento corresponde al mes y año
            facturas = dao.listarPorMes(idHogar, mes, anio);
            filtroActivo = "fecha";
            // Pasa el mes y año filtrados a la vista para mantener los selectores seleccionados
            req.setAttribute("mesFiltro", mes);
            req.setAttribute("anioFiltro", anio);
        } else {
            // Sin filtros: obtiene todas las facturas del hogar
            facturas = dao.listarPorHogar(idHogar);
        }

        // Carga los catálogos necesarios para los selectores del formulario de filtro
        cargarCatalogos(req);
        // Asigna la lista de facturas como atributo para que la vista las itere
        req.setAttribute("facturas", facturas);
        // Asigna el filtro activo para que la vista resalte la opción correspondiente
        req.setAttribute("filtroActivo", filtroActivo);
        forward(req, resp, "03_Consultar-Facturas.jsp");
    }

    /* Método: mostrarFormularioEditar
       Propósito: Buscar la factura a editar por su ID, cargar los catálogos y mostrar
       el formulario de edición con los datos actuales de la factura.
       @param req     → Objeto de la petición HTTP (contiene el parámetro "id")
       @param resp    → Objeto para hacer forward o redirigir si no se encuentra la factura
       @param dao     → Instancia del DAO de egresos
       @param idHogar → Entero con el ID del hogar para validar pertenencia
    */
    private void mostrarFormularioEditar(HttpServletRequest req, HttpServletResponse resp,
                                         RegistroEgresoDao dao, int idHogar)
            throws ServletException, IOException {
        // Variable entera que almacena el ID de la factura a editar; 0 si no se puede parsear
        int id = parsearInt(req.getParameter("id"));
        // Si el ID no es válido, redirige con error
        if (id <= 0) { resp.sendRedirect(req.getContextPath() + "/Facturas?accion=lista&error=id_invalido"); return; }
        // Objeto RegistroEgreso que almacena los datos de la factura encontrada; null si no existe
        RegistroEgreso factura = dao.obtenerPorId(id, idHogar);
        // Si la factura no existe o no pertenece al hogar, redirige con error
        if (factura == null) { resp.sendRedirect(req.getContextPath() + "/Facturas?accion=lista&error=no_encontrada"); return; }
        // Carga los catálogos de categorías y métodos de pago para los selectores del formulario
        cargarCatalogos(req);
        // Asigna la factura como atributo para que la vista precargue los campos del formulario
        req.setAttribute("factura", factura);
        forward(req, resp, "04_EditarFactura.jsp");
    }

    /* Método: mostrarConfirmarEliminar
       Propósito: Buscar la factura por su ID y mostrar la vista de confirmación
       antes de proceder con la eliminación (anulación).
       @param req     → Objeto de la petición HTTP (contiene el parámetro "id")
       @param resp    → Objeto para hacer forward o redirigir si no se encuentra la factura
       @param dao     → Instancia del DAO de egresos
       @param idHogar → Entero con el ID del hogar para validar pertenencia
    */
    private void mostrarConfirmarEliminar(HttpServletRequest req, HttpServletResponse resp,
                                          RegistroEgresoDao dao, int idHogar)
            throws ServletException, IOException {
        // Variable entera que almacena el ID de la factura a eliminar; 0 si no se puede parsear
        int id = parsearInt(req.getParameter("id"));
        // Si el ID no es válido, redirige con error
        if (id <= 0) { resp.sendRedirect(req.getContextPath() + "/Facturas?accion=lista&error=id_invalido"); return; }
        // Objeto RegistroEgreso que almacena los datos de la factura encontrada; null si no existe
        RegistroEgreso factura = dao.obtenerPorId(id, idHogar);
        // Si la factura no existe o no pertenece al hogar, redirige con error
        if (factura == null) { resp.sendRedirect(req.getContextPath() + "/Facturas?accion=lista&error=no_encontrada"); return; }
        // Asigna la factura como atributo para mostrar sus datos en la vista de confirmación
        req.setAttribute("factura", factura);
        forward(req, resp, "06_EliminarFactura.jsp");
    }

    /* Método: mostrarHistorial
       Propósito: Obtener el historial de facturas pagadas del hogar aplicando el filtro
       indicado (categoría, fecha o rango de monto) y mostrarlo junto con los totales.
       Si todos los filtros son null, muestra todas las facturas pagadas sin filtrar.
       @param req         → Objeto de la petición HTTP
       @param resp        → Objeto para hacer forward a la vista del historial
       @param dao         → Instancia del DAO de egresos
       @param idHogar     → Entero con el ID del hogar
       @param idCategoria → Entero (nullable) con el ID de categoría a filtrar; null si no aplica
       @param mes         → Entero (nullable) con el mes a filtrar (1-12); null si no aplica
       @param anio        → Entero (nullable) con el año a filtrar; null si no aplica
       @param rango       → Texto con el rango de monto ("menor50", "50a150", "mayor200"); null si no aplica
    */
    private void mostrarHistorial(HttpServletRequest req, HttpServletResponse resp,
                                  RegistroEgresoDao dao, int idHogar,
                                  Integer idCategoria, Integer mes, Integer anio, String rango)
            throws ServletException, IOException {

        // Lista de objetos RegistroEgreso que almacenará las facturas pagadas a mostrar
        List<RegistroEgreso> historial;
        // Variable de tipo texto que indica qué filtro está activo; "ninguno" por defecto
        String filtroActivo = "ninguno";

        if (idCategoria != null && idCategoria > 0) {
            // Consulta facturas pagadas que pertenezcan a la categoría indicada
            historial = dao.listarPagadasPorCategoria(idHogar, idCategoria);
            filtroActivo = "categoria";
            // Pasa el ID de categoría filtrada a la vista para resaltar la opción activa
            req.setAttribute("categoriaFiltro", idCategoria);
        } else if (mes != null && mes > 0 && anio != null && anio > 0) {
            // Consulta facturas pagadas cuya fecha de pago corresponde al mes y año indicados
            historial = dao.listarPagadasPorMes(idHogar, mes, anio);
            filtroActivo = "fecha";
            // Pasa el mes y año filtrados a la vista para mantener los selectores seleccionados
            req.setAttribute("mesFiltro", mes);
            req.setAttribute("anioFiltro", anio);
        } else if (rango != null && !rango.isEmpty()) {
            // Variable de tipo BigDecimal que almacena el límite inferior del rango de monto
            BigDecimal min = BigDecimal.ZERO;
            // Variable de tipo BigDecimal que almacena el límite superior del rango de monto
            BigDecimal max = new BigDecimal("999999999");
            // Ajusta los límites según el rango seleccionado por el usuario
            switch (rango) {
                // Facturas con monto menor a 50,000
                case "menor50":  max = new BigDecimal("50000"); break;
                // Facturas con monto entre 50,000 y 150,000
                case "50a150":   min = new BigDecimal("50000"); max = new BigDecimal("150000"); break;
                // Facturas con monto mayor a 200,000
                case "mayor200": min = new BigDecimal("200000"); break;
            }
            // Consulta facturas pagadas cuyo monto esté dentro del rango establecido
            historial = dao.listarPagadasPorMonto(idHogar, min, max);
            filtroActivo = "monto";
            // Pasa el rango activo a la vista para resaltar la opción seleccionada
            req.setAttribute("rangoFiltro", rango);
        } else {
            // Sin filtros: obtiene todas las facturas pagadas del hogar
            historial = dao.listarPagadas(idHogar);
        }

        // Arreglo de BigDecimal: posición 0 = suma total pagada, posición 1 = cantidad de facturas pagadas
        BigDecimal[] totales = dao.obtenerTotalPagado(idHogar);
        // Carga los catálogos necesarios para los selectores de filtro de la vista
        cargarCatalogos(req);
        // Asigna la lista de facturas pagadas como atributo para que la vista las itere
        req.setAttribute("historial",       historial);
        // Asigna el total acumulado pagado como atributo para mostrarlo en la vista
        req.setAttribute("totalPagado",     totales[0]);
        // Asigna la cantidad de facturas pagadas como atributo (convertido a entero)
        req.setAttribute("cantidadPagadas", totales[1].intValue());
        // Asigna el filtro activo para que la vista resalte la opción correspondiente
        req.setAttribute("filtroActivo",    filtroActivo);
        forward(req, resp, "08_HistorialPagos.jsp");
    }

    /* Método: procesarRegistro
       Propósito: Validar los datos del formulario de registro y crear una nueva factura
       en la base de datos. Verifica permisos de rol, campos obligatorios, monto positivo
       y que la fecha de vencimiento no sea pasada (a menos que ya esté pagada).
       @param req      → Objeto de la petición HTTP con los datos del formulario
       @param resp     → Objeto para redirigir al éxito o hacer forward al formulario con error
       @param usuario  → Objeto Usuario con los datos del usuario que registra la factura
       @param idHogar  → Entero con el ID del hogar donde se registra la factura
       @param idRol    → Entero con el rol del usuario (1=Admin, 2=Cotitular, 3=Invitado)
    */
    private void procesarRegistro(HttpServletRequest req, HttpServletResponse resp,
                                  Usuario usuario, int idHogar, int idRol)
            throws ServletException, IOException {

        // Solo los roles 1 y 2 pueden registrar facturas; el rol 3 no tiene permiso
        if (idRol != 1 && idRol != 2) {
            resp.sendRedirect(req.getContextPath() + "/Facturas?error=sin_permiso"); return;
        }

        // Variable de tipo texto que almacena la descripción de la factura; vacío si no se ingresó
        String descripcion = nvl(req.getParameter("descripcion")).trim();
        // Variable de tipo texto que almacena el monto ingresado como texto para su posterior conversión
        String montoStr    = req.getParameter("monto");
        // Variable de tipo texto que almacena el ID de la categoría de egreso seleccionada
        String catStr      = req.getParameter("idCategoriaEgreso");
        // Variable de tipo texto que almacena el ID del método de pago seleccionado
        String metStr      = req.getParameter("idMetodoPago");
        // Variable de tipo texto que almacena la fecha de vencimiento en formato "yyyy-MM-dd"
        String fechaStr    = req.getParameter("fechaVencimiento");
        // Variable de tipo texto que almacena el estado de pago seleccionado ("Pendiente", "Pagada")
        String estadoPago  = req.getParameter("estadoPago");

        // Valida que todos los campos obligatorios estén completos
        if (estaVacio(montoStr) || estaVacio(catStr)
                || estaVacio(metStr) || estaVacio(fechaStr) || estaVacio(estadoPago)) {
            req.setAttribute("error", "Todos los campos obligatorios deben completarse.");
            cargarCatalogos(req);
            forward(req, resp, "02_formularioRegistrar-facturas.jsp"); return;
        }

        // Variable de tipo BigDecimal que almacena el monto convertido; se valida que sea positivo
        BigDecimal monto;
        try {
            // Reemplaza coma por punto para aceptar formatos decimales con coma
            monto = new BigDecimal(montoStr.replace(",", "."));
            // El monto debe ser mayor a cero
            if (monto.compareTo(BigDecimal.ZERO) <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            req.setAttribute("error", "El monto debe ser un número positivo.");
            cargarCatalogos(req);
            forward(req, resp, "02_formularioRegistrar-facturas.jsp"); return;
        }

        // Variable de tipo LocalDateTime que almacena la fecha de vencimiento parseada
        LocalDateTime fechaVencimiento;
        try {
            // Parsea la fecha en formato "yyyy-MM-dd" y la convierte al inicio del día
            fechaVencimiento = LocalDate.parse(fechaStr).atStartOfDay();
        } catch (DateTimeParseException e) {
            req.setAttribute("error", "La fecha de vencimiento no es válida.");
            cargarCatalogos(req);
            forward(req, resp, "02_formularioRegistrar-facturas.jsp"); return;
        }

        // Variable de tipo LocalDateTime que almacena el límite mínimo permitido: 3 meses atrás desde hoy
        LocalDateTime limiteMinimo = LocalDateTime.now().minusMonths(3);
        // Valida que la fecha no sea anterior a 3 meses atrás (se permite registrar pagos atrasados hasta ese límite)
        if (fechaVencimiento.isBefore(limiteMinimo)) {
            req.setAttribute("error", "La fecha de vencimiento no puede ser anterior a 3 meses atrás.");
            cargarCatalogos(req);
            forward(req, resp, "02_formularioRegistrar-facturas.jsp"); return;
        }

        // Objeto RegistroEgreso que almacena todos los datos de la nueva factura a insertar
        RegistroEgreso egreso = new RegistroEgreso();
        egreso.setIdHogar(idHogar);
        egreso.setDescripcionPago(descripcion);
        egreso.setMonto(monto);
        egreso.setIdCategoriaEgreso(Integer.parseInt(catStr));
        egreso.setIdMetodoPago(Integer.parseInt(metStr));
        egreso.setFechaVencimiento(fechaVencimiento);
        egreso.setEstadoPago(estadoPago);

        // Instancia del DAO de egresos para insertar la nueva factura en la base de datos
        RegistroEgresoDao dao = new RegistroEgresoDao();
        // Variable entera que almacena el ID generado al insertar; mayor a 0 si fue exitoso
        int idGenerado = dao.insertarEgreso(egreso);

        if (idGenerado > 0) {
            // Si se insertó correctamente, redirige con parámetro de éxito
            resp.sendRedirect(req.getContextPath() + "/Facturas?accion=lista&exito=registrada");
        } else {
            // Si falló la inserción, muestra el formulario con mensaje de error
            req.setAttribute("error", "Error al guardar la factura. Intenta nuevamente.");
            cargarCatalogos(req);
            forward(req, resp, "02_formularioRegistrar-facturas.jsp");
        }
    }

    /* Método: procesarActualizacion
       Propósito: Validar los datos del formulario de edición y actualizar la factura
       existente en la base de datos. Verifica permisos de rol, que el ID sea válido,
       que los campos obligatorios estén completos, el monto positivo y la fecha correcta.
       @param req     → Objeto de la petición HTTP con los datos del formulario de edición
       @param resp    → Objeto para redirigir al éxito o de vuelta al formulario con error
       @param idHogar → Entero con el ID del hogar para validar pertenencia de la factura
       @param idRol   → Entero con el rol del usuario (1=Admin, 2=Cotitular, 3=Invitado)
    */
    private void procesarActualizacion(HttpServletRequest req, HttpServletResponse resp,
                                       int idHogar, int idRol)
            throws ServletException, IOException {

        // Solo los roles 1 y 2 pueden actualizar facturas; el rol 3 no tiene permiso
        if (idRol != 1 && idRol != 2) {
            resp.sendRedirect(req.getContextPath() + "/Facturas?error=sin_permiso"); return;
        }

        // Variable entera que almacena el ID de la factura a actualizar; 0 si no se puede parsear
        int id             = parsearInt(req.getParameter("idEgreso"));
        // Variable de tipo texto que almacena la descripción actualizada de la factura
        String descripcion = nvl(req.getParameter("descripcion")).trim();
        // Variable de tipo texto que almacena el monto actualizado como texto
        String montoStr    = req.getParameter("monto");
        // Variable de tipo texto que almacena el ID de la categoría de egreso actualizada
        String catStr      = req.getParameter("idCategoriaEgreso");
        // Variable de tipo texto que almacena el ID del método de pago actualizado
        String metStr      = req.getParameter("idMetodoPago");
        // Variable de tipo texto que almacena la fecha de vencimiento actualizada ("yyyy-MM-dd")
        String fechaStr    = req.getParameter("fechaVencimiento");
        // Variable de tipo texto que almacena el estado de pago actualizado
        String estado      = req.getParameter("estadoPago");

        // Valida que el ID sea positivo y que los campos obligatorios no estén vacíos
        if (id <= 0 || estaVacio(montoStr) || estaVacio(catStr)
                || estaVacio(metStr) || estaVacio(fechaStr) || estaVacio(estado)) {
            resp.sendRedirect(req.getContextPath() + "/Facturas?accion=editar&id=" + id + "&error=campos_vacios");
            return;
        }

        // Variable de tipo BigDecimal que almacena el monto convertido y validado
        BigDecimal monto;
        try {
            monto = new BigDecimal(montoStr.replace(",", "."));
            if (monto.compareTo(BigDecimal.ZERO) <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/Facturas?accion=editar&id=" + id + "&error=monto_invalido");
            return;
        }

        // Variable de tipo LocalDateTime que almacena la fecha de vencimiento parseada
        LocalDateTime fechaVenc;
        try {
            fechaVenc = LocalDate.parse(fechaStr).atStartOfDay();
        } catch (DateTimeParseException e) {
            resp.sendRedirect(req.getContextPath() + "/Facturas?accion=editar&id=" + id + "&error=fecha_invalida");
            return;
        }

        // Objeto RegistroEgreso que almacena los datos actualizados de la factura
        RegistroEgreso egreso = new RegistroEgreso();
        egreso.setIdEgresos(id);
        egreso.setIdHogar(idHogar);
        egreso.setDescripcionPago(descripcion);
        egreso.setMonto(monto);
        egreso.setIdCategoriaEgreso(Integer.parseInt(catStr));
        egreso.setIdMetodoPago(Integer.parseInt(metStr));
        egreso.setFechaVencimiento(fechaVenc);
        egreso.setEstadoPago(estado);
        // Si el estado es "Pagada", asigna la fecha y hora actual como fecha de pago
        if ("Pagada".equals(estado)) egreso.setFechaPago(LocalDateTime.now());

        // Variable booleana que indica si la actualización en la base de datos fue exitosa
        boolean ok = new RegistroEgresoDao().actualizar(egreso);
        if (ok) {
            // Si se actualizó correctamente, redirige con parámetro de éxito
            resp.sendRedirect(req.getContextPath() + "/Facturas?accion=lista&exito=editada");
        } else {
            // Si falló la actualización, redirige de vuelta al formulario con error de base de datos
            resp.sendRedirect(req.getContextPath() + "/Facturas?accion=editar&id=" + id + "&error=error_db");
        }
    }

    /* Método: procesarEliminacion
       Propósito: Anular (eliminación lógica) una factura existente del hogar.
       Primero elimina los recordatorios asociados y luego anula la factura.
       Solo los roles 1 y 2 tienen permiso para ejecutar esta operación.
       @param req     → Objeto de la petición HTTP (contiene el parámetro "idEgreso")
       @param resp    → Objeto para redirigir según el resultado de la operación
       @param idHogar → Entero con el ID del hogar para validar pertenencia de la factura
       @param idRol   → Entero con el rol del usuario (1=Admin, 2=Cotitular, 3=Invitado)
    */
    private void procesarEliminacion(HttpServletRequest req, HttpServletResponse resp,
                                     int idHogar, int idRol)
            throws IOException {
        // Solo los roles 1 y 2 pueden eliminar facturas; el rol 3 no tiene permiso
        if (idRol != 1 && idRol != 2) {
            resp.sendRedirect(req.getContextPath() + "/Facturas?error=sin_permiso"); return;
        }
        // Variable entera que almacena el ID de la factura a anular; 0 si no se puede parsear
        int id = parsearInt(req.getParameter("idEgreso"));
        // Si el ID no es válido, redirige con error
        if (id <= 0) { resp.sendRedirect(req.getContextPath() + "/Facturas?accion=lista&error=id_invalido"); return; }

        // Elimina primero los recordatorios asociados a la factura antes de anularla
        new com.smarthome.smarthome_budget.dao.RecordatorioEgresoDao().eliminarPorEgreso(id);
        // Variable booleana que indica si la anulación de la factura fue exitosa
        boolean ok = new RegistroEgresoDao().anular(id, idHogar);

        if (ok) {
            // Si se anuló correctamente, redirige con parámetro de éxito
            resp.sendRedirect(req.getContextPath() + "/Facturas?accion=lista&exito=eliminada");
        } else {
            // Si falló la anulación, redirige con error
            resp.sendRedirect(req.getContextPath() + "/Facturas?accion=lista&error=error_eliminar");
        }
    }

    /* Método: procesarMarcarPagada
       Propósito: Cambiar el estado de una factura a "Pagada" y registrar la fecha
       de pago con la fecha y hora actual. Solo los roles 1 y 2 pueden ejecutar
       esta operación.
       @param req     → Objeto de la petición HTTP (contiene el parámetro "idEgreso")
       @param resp    → Objeto para redirigir después de la operación
       @param idHogar → Entero con el ID del hogar para validar pertenencia de la factura
       @param idRol   → Entero con el rol del usuario (1=Admin, 2=Cotitular, 3=Invitado)
    */
    private void procesarMarcarPagada(HttpServletRequest req, HttpServletResponse resp,
                                      int idHogar, int idRol)
            throws IOException {
        // Solo los roles 1 y 2 pueden marcar facturas como pagadas; el rol 3 no tiene permiso
        if (idRol != 1 && idRol != 2) {
            resp.sendRedirect(req.getContextPath() + "/Facturas?error=sin_permiso"); return;
        }
        // Variable entera que almacena el ID de la factura a marcar como pagada
        int id = parsearInt(req.getParameter("idEgreso"));
        // Actualiza el estado de la factura a "Pagada" y registra la fecha actual como fecha de pago
        new RegistroEgresoDao().marcarComoPagada(id, idHogar);
        // Redirige con parámetro de éxito para mostrar la notificación en la vista
        resp.sendRedirect(req.getContextPath() + "/Facturas?accion=lista&exito=pagada");
    }

    /* Método: cargarCatalogos
       Propósito: Consultar y asignar al request las listas de categorías de egreso
       y métodos de pago necesarias para poblar los selectores de los formularios.
       @param req → Objeto de la petición HTTP donde se asignan los atributos de catálogos
    */
    private void cargarCatalogos(HttpServletRequest req) {
        // Asigna la lista de categorías de egreso disponibles para el selector del formulario
        req.setAttribute("categorias",  new CategoriaEgresoDao().listarCategorias());
        // Asigna la lista de métodos de pago disponibles para el selector del formulario
        req.setAttribute("metodosPago", new MetodoPagoDao().listarMetodosPago());
    }

    /* Método: forward
       Propósito: Hacer forward a una vista JSP del módulo de facturas usando la ruta base
       definida en la constante BASE_JSP.
       @param req   → Objeto de la petición HTTP
       @param resp  → Objeto de respuesta HTTP
       @param vista → Nombre del archivo JSP a mostrar (sin la ruta base)
    */
    private void forward(HttpServletRequest req, HttpServletResponse resp, String vista)
            throws ServletException, IOException {
        req.getRequestDispatcher(BASE_JSP + vista).forward(req, resp);
    }

    /* Método: estaVacio
       Propósito: Verificar si una cadena de texto es null o está vacía (incluyendo solo espacios).
       @param s → Texto a evaluar
       @return boolean → true si la cadena es null o vacía, false si contiene contenido
    */
    private boolean estaVacio(String s)   { return s == null || s.trim().isEmpty(); }

    /* Método: nvl
       Propósito: Retornar la cadena recibida sin modificar, o una cadena vacía si es null.
       Equivalente al NVL de SQL para evitar NullPointerException en operaciones con texto.
       @param s → Texto a evaluar
       @return String → El mismo texto, o "" si era null
    */
    private String nvl(String s)          { return s == null ? "" : s; }

    /* Método: parsearInt
       Propósito: Convertir una cadena de texto a entero de forma segura, retornando 0
       si la conversión falla para evitar excepciones en tiempo de ejecución.
       @param s → Texto a convertir
       @return int → Entero resultante, o 0 si la conversión falla
    */
    private int parsearInt(String s)      { try { return Integer.parseInt(s); } catch (Exception e) { return 0; } }

    /* Método: parsearIntNullable
       Propósito: Convertir una cadena de texto a Integer (nullable) de forma segura.
       A diferencia de parsearInt, retorna null en lugar de 0 si la cadena es vacía
       o la conversión falla, permitiendo distinguir entre "no enviado" y "valor cero".
       @param s → Texto a convertir
       @return Integer → Entero resultante, o null si la cadena es null, vacía o no convertible
    */
    private Integer parsearIntNullable(String s) {
        if (s == null || s.isEmpty()) return null;
        try { return Integer.parseInt(s); } catch (Exception e) { return null; }
    }
}
