package com.smarthome.smarthome_budget.controlador;

import com.smarthome.smarthome_budget.dao.*;
import com.smarthome.smarthome_budget.modelo.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;

/* Clase: InventarioServlet
   Propósito: Gestionar todas las operaciones del módulo Mi Inventario del hogar.
   Permite registrar, consultar (con filtros), editar y eliminar productos del inventario,
   así como mover automáticamente un producto agotado a una lista de compras.
   El rol 3 (Invitado) puede consultar pero no puede modificar el inventario.

   ACCIONES GET:
     (default)           → 01_MiInventario.jsp (dashboard con resumen)
     registrar           → formulario para registrar un nuevo producto
     consultar           → lista completa del inventario
     filtroTipo          → filtra el inventario por tipo/categoría de producto
     filtroCantidad      → filtra el inventario por rango de cantidad (bajo, alto, orden)
     editar              → formulario para editar un producto existente
     confirmarEliminar   → vista de confirmación antes de eliminar un producto
     autoAnadirALista    → mueve un producto del inventario a una lista de compras

   ACCIONES POST:
     guardar    → registra un nuevo producto o suma cantidad si ya existe
     actualizar → guarda los cambios de cantidad de un producto existente
     eliminar   → elimina un producto del inventario

   URL mapeada: /Inventario
*/
@WebServlet("/Inventario")
public class InventarioServlet extends HttpServlet {

    // Constante de tipo texto con la ruta base de las vistas del módulo de inventario
    private static final String BASE = "/public/modules/04_ProductosDisponiblesCasa/";

    // Patrón de validación para nombres de producto: letras (con tildes/ñ), números, espacios y: . , # -
    private static final Pattern P_NOMBRE_PRODUCTO = Pattern.compile("^[\\p{L}\\p{N} .,#\\-]+$");

    /* Método: doGet
       Propósito: Mostrar las vistas del módulo de inventario según la acción solicitada.
       Enruta al dashboard, formularios, filtros o la acción de auto-añadir a lista.
       @param req  → Objeto con el parámetro "accion" en la URL
       @param resp → Objeto para hacer forward a la vista correspondiente
    */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Verifica que haya sesión activa con usuario autenticado
        if (!sesionValida(req, resp)) return;
        // Variable entera que almacena el ID del hogar del usuario en sesión
        int idHogar = idHogar(req);
        // Variable de tipo texto que almacena la acción solicitada por parámetro URL
        String accion = nvl(req.getParameter("accion"));

        switch (accion) {
            case "registrar":           mostrarFormRegistrar(req, resp);                    break;
            case "consultar":           mostrarConsulta(req, resp, idHogar, null);          break;
            case "filtroTipo":          mostrarFiltroTipo(req, resp, idHogar);              break;
            case "filtroCantidad":      mostrarFiltroCantidad(req, resp, idHogar);          break;
            case "editar":              mostrarFormEditar(req, resp, idHogar);              break;
            case "confirmarEliminar":   mostrarConfirmarEliminar(req, resp, idHogar);       break;
            case "autoAnadirALista":    autoAnadirALista(req, resp, idHogar);               break;
            default:                    mostrarDashboard(req, resp, idHogar);
        }
    }

    /* Método: doPost
       Propósito: Procesar las acciones que modifican datos del inventario:
       guardar un nuevo producto, actualizar su cantidad o eliminarlo.
       El rol 3 (Invitado) no puede ejecutar ninguna de estas acciones.
       @param req  → Objeto con los datos del formulario enviado
       @param resp → Objeto para hacer forward o redirigir al resultado
    */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Establece la codificación UTF-8 para aceptar caracteres especiales del formulario
        req.setCharacterEncoding("UTF-8");
        // Verifica que haya sesión activa con usuario autenticado
        if (!sesionValida(req, resp)) return;

        // Variable entera que almacena el ID del hogar del usuario en sesión
        int idHogar = idHogar(req);
        // Variable entera que almacena el ID del rol del usuario
        int idRol   = idRol(req);
        // Variable de tipo texto que almacena la acción enviada por el formulario
        String accion = nvl(req.getParameter("accion"));

        // El rol 3 (Invitado) no puede modificar el inventario
        if (idRol == 3) {
            resp.sendRedirect(req.getContextPath() + "/Inventario?error=sin_permiso"); return;
        }

        switch (accion) {
            case "guardar":     guardarProducto(req, resp, idHogar);    break;
            case "actualizar":  actualizarProducto(req, resp, idHogar); break;
            case "eliminar":    eliminarProducto(req, resp, idHogar);   break;
            default: resp.sendRedirect(req.getContextPath() + "/Inventario");
        }
    }

    // ─── Vistas GET ───────────────────────────────────────────────────────────

    /* Método: mostrarDashboard
       Propósito: Obtener el resumen del inventario (productos disponibles, por agotar
       y categorías activas) y mostrarlo en la vista principal del módulo.
       @param req     → Objeto de la petición HTTP donde se asignan los atributos del resumen
       @param resp    → Objeto para hacer forward a la vista del dashboard
       @param idHogar → Entero con el ID del hogar para filtrar el inventario
    */
    private void mostrarDashboard(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {
        // Arreglo de enteros: posición 0 = disponibles, 1 = por agotar, 2 = categorías activas
        int[] resumen = new InventarioCasaDao().obtenerResumen(idHogar);
        // Asigna la cantidad de productos disponibles (cantidad > 2) como atributo para la vista
        req.setAttribute("disponibles", resumen[0]);
        // Asigna la cantidad de productos por agotar (cantidad <= 2) como atributo para la vista
        req.setAttribute("porAgotar",   resumen[1]);
        // Asigna el número de categorías con productos activos como atributo para la vista
        req.setAttribute("categorias",  resumen[2]);
        forward(req, resp, "01_MiInventario.jsp");
    }

    /* Método: mostrarFormRegistrar
       Propósito: Cargar los tipos de producto disponibles y mostrar el formulario
       vacío para registrar un nuevo producto en el inventario.
       @param req  → Objeto de la petición HTTP donde se asignan los tipos de producto
       @param resp → Objeto para hacer forward al formulario de registro
    */
    private void mostrarFormRegistrar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Carga la lista de tipos de producto para poblar el selector del formulario
        req.setAttribute("tiposProducto", new ProductoDao().listarTipos());
        forward(req, resp, "02_RegistrarProductoDisponibles.jsp");
    }

    /* Método: mostrarConsulta
       Propósito: Mostrar la lista del inventario del hogar. Si se recibe una lista
       previamente filtrada la usa; de lo contrario consulta todos los productos.
       Si el inventario está vacío redirige a la vista de inventario vacío.
       @param req                → Objeto de la petición HTTP
       @param resp               → Objeto para hacer forward a la vista correspondiente
       @param idHogar            → Entero con el ID del hogar
       @param inventarioFiltrado → Lista de InventarioCasa ya filtrada; null para consultar todo
    */
    private void mostrarConsulta(HttpServletRequest req, HttpServletResponse resp,
                                 int idHogar, List<InventarioCasa> inventarioFiltrado)
            throws ServletException, IOException {
        // Lista de objetos InventarioCasa: usa la filtrada si viene, de lo contrario consulta todo el hogar
        List<InventarioCasa> inventario = inventarioFiltrado != null
            ? inventarioFiltrado
            : new InventarioCasaDao().listarPorHogar(idHogar);

        if (inventario.isEmpty()) {
            // Si no hay productos en el inventario, muestra la vista de estado vacío
            forward(req, resp, "08_ConsultarInventario-Vacio.jsp");
        } else {
            // Asigna la lista de productos del inventario como atributo para que la vista los itere
            req.setAttribute("inventario", inventario);
            // Carga los tipos de producto para el selector de filtro de la vista
            req.setAttribute("tiposProducto", new ProductoDao().listarTipos());
            forward(req, resp, "09_ConsultarInventario.jsp");
        }
    }

    /* Método: mostrarFiltroTipo
       Propósito: Filtrar el inventario por tipo de producto y mostrar los resultados.
       Si no se envía tipo, muestra todos los productos del hogar sin filtrar.
       @param req     → Objeto de la petición HTTP (contiene el parámetro "tipo")
       @param resp    → Objeto para hacer forward a la vista de consulta
       @param idHogar → Entero con el ID del hogar
    */
    private void mostrarFiltroTipo(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {
        // Variable de tipo texto que almacena el ID del tipo de producto recibido como filtro
        String tipoStr = req.getParameter("tipo");
        // Lista de objetos InventarioCasa que almacenará los productos filtrados o todos si no hay filtro
        List<InventarioCasa> inventario;
        if (tipoStr != null && !tipoStr.isEmpty()) {
            // Filtra los productos del inventario que pertenezcan al tipo indicado
            inventario = new InventarioCasaDao().listarPorTipo(idHogar, parseInt(tipoStr));
        } else {
            // Sin filtro de tipo: obtiene todos los productos del hogar
            inventario = new InventarioCasaDao().listarPorHogar(idHogar);
        }
        // Asigna la lista filtrada como atributo para que la vista la itere
        req.setAttribute("inventario", inventario);
        // Carga los tipos de producto para mantener el selector de filtro en la vista
        req.setAttribute("tiposProducto", new ProductoDao().listarTipos());
        // Indica a la vista que el filtro activo es por tipo
        req.setAttribute("filtroActivo", "tipo");
        // Pasa el tipo seleccionado para que la vista mantenga la opción resaltada
        req.setAttribute("tipoSeleccionado", tipoStr);
        forward(req, resp, "09_ConsultarInventario.jsp");
    }

    /* Método: mostrarFiltroCantidad
       Propósito: Filtrar u ordenar el inventario según el criterio de cantidad recibido
       y mostrar los resultados en la vista de consulta.
       @param req     → Objeto de la petición HTTP (contiene el parámetro "orden")
       @param resp    → Objeto para hacer forward a la vista de consulta
       @param idHogar → Entero con el ID del hogar
    */
    private void mostrarFiltroCantidad(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {
        // Variable de tipo texto que almacena el criterio de orden/filtro seleccionado por el usuario
        String orden = nvl(req.getParameter("orden"));
        // Instancia del DAO de inventario para ejecutar las distintas consultas de filtro
        InventarioCasaDao dao = new InventarioCasaDao();
        // Lista de objetos InventarioCasa que almacenará los productos según el filtro aplicado
        List<InventarioCasa> inventario;
        switch (orden) {
            case "menorIgual2":
                // Obtiene productos con stock bajo (cantidad <= 2)
                inventario = dao.listarStockBajo(idHogar);
                break;
            case "mayorIgual10":
                // Obtiene todos los productos ordenados por cantidad descendente
                inventario = dao.listarOrdenadoCantidadDesc(idHogar);
                // Variable de tipo BigDecimal que representa el umbral mínimo para este filtro
                BigDecimal diez = new BigDecimal("10");
                // Elimina los productos con cantidad menor a 10 para mostrar solo los de stock alto
                inventario.removeIf(i -> i.getCantidad().compareTo(diez) < 0);
                break;
            case "asc":
                // Ordena el inventario de menor a mayor cantidad
                inventario = dao.listarOrdenadoCantidadAsc(idHogar);
                break;
            case "desc":
                // Ordena el inventario de mayor a menor cantidad
                inventario = dao.listarOrdenadoCantidadDesc(idHogar);
                break;
            default:
                // Sin criterio válido: muestra todos los productos del hogar
                inventario = dao.listarPorHogar(idHogar);
        }
        // Asigna la lista filtrada/ordenada como atributo para que la vista la itere
        req.setAttribute("inventario", inventario);
        // Carga los tipos de producto para mantener el selector de filtro en la vista
        req.setAttribute("tiposProducto", new ProductoDao().listarTipos());
        // Indica a la vista que el filtro activo es por cantidad
        req.setAttribute("filtroActivo", "cantidad");
        // Pasa el criterio de orden seleccionado para que la vista mantenga la opción resaltada
        req.setAttribute("ordenSeleccionado", orden);
        forward(req, resp, "09_ConsultarInventario.jsp");
    }

    /* Método: mostrarFormEditar
       Propósito: Buscar el producto del inventario por su ID y mostrar el formulario
       de edición con los datos actuales. Si no se encuentra, redirige a la consulta.
       @param req     → Objeto de la petición HTTP (contiene el parámetro "id")
       @param resp    → Objeto para hacer forward o redirigir si no se encuentra
       @param idHogar → Entero con el ID del hogar para validar pertenencia
    */
    private void mostrarFormEditar(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {
        // Variable entera que almacena el ID del registro de inventario a editar; 0 si no se parsea
        int id = parseInt(req.getParameter("id"));
        // Objeto InventarioCasa que almacena los datos del producto encontrado; null si no existe
        InventarioCasa inv = new InventarioCasaDao().obtenerPorId(id, idHogar);
        // Si el producto no existe o no pertenece al hogar, redirige a la consulta
        if (inv == null) { resp.sendRedirect(req.getContextPath() + "/Inventario?accion=consultar"); return; }
        // Asigna el objeto del inventario como atributo para que la vista precargue los datos
        req.setAttribute("inv", inv);
        // Carga los tipos de producto para el selector del formulario de edición
        req.setAttribute("tiposProducto", new ProductoDao().listarTipos());
        forward(req, resp, "04_EditarProductoCasa.jsp");
    }

    /* Método: mostrarConfirmarEliminar
       Propósito: Buscar el producto del inventario por su ID y mostrar la vista de
       confirmación antes de proceder con la eliminación.
       @param req     → Objeto de la petición HTTP (contiene el parámetro "id")
       @param resp    → Objeto para hacer forward o redirigir si no se encuentra
       @param idHogar → Entero con el ID del hogar para validar pertenencia
    */
    private void mostrarConfirmarEliminar(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {
        // Variable entera que almacena el ID del registro de inventario a eliminar; 0 si no se parsea
        int id = parseInt(req.getParameter("id"));
        // Objeto InventarioCasa que almacena los datos del producto encontrado; null si no existe
        InventarioCasa inv = new InventarioCasaDao().obtenerPorId(id, idHogar);
        // Si el producto no existe o no pertenece al hogar, redirige a la consulta
        if (inv == null) { resp.sendRedirect(req.getContextPath() + "/Inventario?accion=consultar"); return; }
        // Asigna el objeto del inventario como atributo para mostrar sus datos en la confirmación
        req.setAttribute("inv", inv);
        forward(req, resp, "06_EliminarProducto-Casa.jsp");
    }

    /* Método: autoAnadirALista
       Propósito: Mover un producto del inventario a una lista de compras activa.
       Pasos: A) Determina la categoría del producto para construir el nombre de la lista.
              B) Busca una lista activa con ese nombre en el hogar.
              C) Si no existe, la crea; luego agrega el producto (suma +1 si ya estaba).
              D) Elimina el producto del inventario y redirige con mensaje de confirmación.
       @param req     → Objeto de la petición HTTP (contiene el parámetro "idProducto")
       @param resp    → Objeto para redirigir al resultado de la operación
       @param idHogar → Entero con el ID del hogar
    */
    private void autoAnadirALista(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws IOException {
        // Variable entera que almacena el ID del registro de inventario a mover (parámetro "idProducto")
        int idInventario = parseInt(req.getParameter("idProducto"));
        // Objeto InventarioCasa que almacena los datos del producto encontrado; null si no existe
        InventarioCasa inv = new InventarioCasaDao().obtenerPorId(idInventario, idHogar);

        // Si el producto no existe en el inventario del hogar, redirige con error
        if (inv == null) {
            resp.sendRedirect(req.getContextPath() + "/Inventario?accion=consultar&error=no_encontrado");
            return;
        }

        // Paso A: Variable de tipo texto con el nombre de la categoría del producto para armar el nombre de lista
        String categoria   = inv.getNombreTipoProducto() != null ? inv.getNombreTipoProducto() : "General";
        // Variable de tipo texto que almacena el nombre de la lista de compras destino
        String nombreLista = "Lista de " + categoria;

        // Instancia del DAO de listas de compras para buscar o crear la lista destino
        ListaComprasDao listaDao      = new ListaComprasDao();
        // Instancia del DAO de detalle de lista de compras para agregar el producto a la lista
        DetalleListaComprasDao detDao = new DetalleListaComprasDao();

        // Paso B: Objeto ListaCompras con la lista activa encontrada; null si no existe ninguna con ese nombre
        ListaCompras lista = listaDao.buscarActivaPorNombre(idHogar, nombreLista);

        // Paso C: Si no existe una lista activa con ese nombre, crea una nueva
        if (lista == null) {
            // Objeto ListaCompras que almacena los datos de la nueva lista a crear
            ListaCompras nueva = new ListaCompras();
            nueva.setIdHogar(idHogar);
            nueva.setNombreLista(nombreLista);
            // Variable entera que almacena el ID generado de la nueva lista; <= 0 si falló
            int idNueva = listaDao.insertar(nueva);
            if (idNueva <= 0) {
                // Si no se pudo crear la lista, redirige con error
                resp.sendRedirect(req.getContextPath() + "/Inventario?accion=consultar&error=error_lista");
                return;
            }
            // Recupera el objeto completo de la lista recién creada para usarlo en el siguiente paso
            lista = listaDao.obtenerPorId(idNueva, idHogar);
        }

        // Agrega el producto a la lista con cantidad 1; si ya existe en la lista, suma +1
        detDao.agregarProducto(lista.getIdListaCompras(), inv.getIdProducto(), 1);
        // Recalcula el estado de la lista (pendiente/completada) tras el cambio
        listaDao.recalcularEstado(lista.getIdListaCompras());

        // Elimina el producto del inventario ya que fue movido a la lista de compras
        new InventarioCasaDao().eliminar(idInventario, idHogar);

        // Paso D: Variable de tipo texto que almacena el mensaje de confirmación codificado para la URL
        String msg = java.net.URLEncoder.encode(
            inv.getNombreProducto() + " movido a " + nombreLista, "UTF-8");
        // Redirige al inventario con el parámetro de éxito para mostrar el mensaje al usuario
        resp.sendRedirect(req.getContextPath() +
            "/Inventario?accion=consultar&exito_auto=" + msg);
    }

    // ─── Acciones POST ────────────────────────────────────────────────────────

    /* Método: guardarProducto
       Propósito: Validar los datos del formulario y registrar un nuevo producto en el
       inventario. Si el producto ya existe en el hogar, suma la cantidad indicada al stock
       actual. Valida el nombre con el patrón definido y que la cantidad sea mayor a 0.
       @param req     → Objeto de la petición HTTP con los datos del formulario
       @param resp    → Objeto para hacer forward a la confirmación o al formulario con error
       @param idHogar → Entero con el ID del hogar donde se registra el producto
    */
    private void guardarProducto(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {

        // Variable de tipo texto que almacena el nombre del producto sin espacios al inicio/final
        String nombre       = nvl(req.getParameter("nombreProducto")).trim();
        // Variable de tipo BigDecimal que almacena la cantidad ingresada; ZERO si no se puede parsear
        BigDecimal cantidad = parseBD(req.getParameter("cantidad"));
        // Variable entera que almacena el ID del tipo de producto seleccionado; 0 si no se seleccionó
        int idTipo          = parseInt(req.getParameter("idTipoProducto"));
        // Variable de tipo texto que almacena la descripción opcional del producto
        String descripcion  = nvl(req.getParameter("descripcion"));

        // Variable de tipo texto que almacena el mensaje de error del nombre; null si es válido
        String errN = validarNombreProducto(nombre);
        if (errN != null) {
            // Si el nombre no es válido, muestra el formulario con el mensaje de error
            req.setAttribute("error", errN);
            req.setAttribute("tiposProducto", new ProductoDao().listarTipos());
            forward(req, resp, "02_RegistrarProductoDisponibles.jsp"); return;
        }

        // Valida que la cantidad sea mayor a 0 y no supere el límite de 999
        if (cantidad.compareTo(BigDecimal.ZERO) <= 0 || cantidad.compareTo(new BigDecimal("999")) > 0) {
            req.setAttribute("error", "La cantidad debe ser mayor a 0 y máximo 999.");
            req.setAttribute("tiposProducto", new ProductoDao().listarTipos());
            forward(req, resp, "02_RegistrarProductoDisponibles.jsp"); return;
        }

        // Si no se seleccionó tipo de producto, asigna el tipo por defecto (5 = General)
        if (idTipo <= 0) idTipo = 5;

        // Variable entera que almacena el ID del producto (existente o recién creado en el catálogo)
        int idProducto = new ProductoDao().obtenerOCrearProducto(nombre, idTipo);
        if (!descripcion.isBlank()) {
            // Si se ingresó descripción, actualiza el campo en la tabla Producto
            try (java.sql.Connection con = com.smarthome.smarthome_budget.basedatos.claseConexion.MetodoConectar();
                 java.sql.PreparedStatement ps = con.prepareStatement(
                     "UPDATE Producto SET Descripcion=? WHERE IDProducto=?")) {
                ps.setString(1, descripcion.trim());
                ps.setInt(2, idProducto);
                ps.executeUpdate();
            } catch (java.sql.SQLException e) { /* ignorar error de descripción */ }
        }

        // Objeto InventarioCasa que almacena los datos del nuevo registro de inventario
        InventarioCasa inv = new InventarioCasa();
        inv.setIdHogar(idHogar);
        inv.setIdProducto(idProducto);
        inv.setCantidad(cantidad);

        // Variable entera: 1 si se creó nuevo registro, 2 si se sumó cantidad a uno existente, 0 si falló
        int res = new InventarioCasaDao().registrar(inv);
        if (res >= 1) {
            // Pasa el nombre y la cantidad para mostrarlos en la vista de confirmación
            req.setAttribute("nombreProducto", nombre);
            req.setAttribute("cantidad", cantidad);
            // Variable booleana que indica si el producto ya existía y se actualizó (true) o fue nuevo (false)
            req.setAttribute("fueActualizado", res == 2);
            forward(req, resp, "03_ConfirmarRegistroProductosDisponibles.jsp");
        } else {
            // Si falló el registro, muestra el formulario con mensaje de error genérico
            req.setAttribute("error", "Error al registrar el producto.");
            req.setAttribute("tiposProducto", new ProductoDao().listarTipos());
            forward(req, resp, "02_RegistrarProductoDisponibles.jsp");
        }
    }

    /* Método: actualizarProducto
       Propósito: Validar la nueva cantidad y actualizar el registro del inventario.
       Si la cantidad no es válida, redirige de vuelta al formulario con error.
       @param req     → Objeto de la petición HTTP (contiene "idInventario" y "cantidad")
       @param resp    → Objeto para hacer forward a la confirmación o redirigir con error
       @param idHogar → Entero con el ID del hogar para validar pertenencia del registro
    */
    private void actualizarProducto(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {

        // Variable entera que almacena el ID del registro de inventario a actualizar
        int id = parseInt(req.getParameter("idInventario"));
        // Variable de tipo BigDecimal que almacena la nueva cantidad ingresada; ZERO si no se parsea
        BigDecimal cantidad = parseBD(req.getParameter("cantidad"));

        // Valida que la cantidad sea >= 0 y no supere el límite de 999
        if (cantidad.compareTo(BigDecimal.ZERO) < 0 || cantidad.compareTo(new BigDecimal("999")) > 0) {
            resp.sendRedirect(req.getContextPath() + "/Inventario?accion=editar&id=" + id + "&error=cantidad_invalida");
            return;
        }

        // Objeto InventarioCasa que almacena los datos actualizados del registro
        InventarioCasa inv = new InventarioCasa();
        inv.setIdInventario(id);
        inv.setIdHogar(idHogar);
        inv.setCantidad(cantidad);

        if (new InventarioCasaDao().actualizar(inv)) {
            // Objeto InventarioCasa con los datos actualizados para mostrar el nombre en la confirmación
            InventarioCasa updated = new InventarioCasaDao().obtenerPorId(id, idHogar);
            // Asigna el nombre del producto para mostrarlo en la vista de cambios guardados
            req.setAttribute("nombreProducto", updated != null ? updated.getNombreProducto() : "");
            forward(req, resp, "05_CambiosGuardados.jsp");
        } else {
            // Si falló la actualización en la base de datos, redirige al formulario con error
            resp.sendRedirect(req.getContextPath() + "/Inventario?accion=editar&id=" + id + "&error=db");
        }
    }

    /* Método: eliminarProducto
       Propósito: Eliminar un producto del inventario del hogar y mostrar la vista
       de confirmación de eliminación con el nombre del producto eliminado.
       @param req     → Objeto de la petición HTTP (contiene el parámetro "idInventario")
       @param resp    → Objeto para hacer forward a la vista de confirmación
       @param idHogar → Entero con el ID del hogar para validar pertenencia del registro
    */
    private void eliminarProducto(HttpServletRequest req, HttpServletResponse resp, int idHogar)
            throws ServletException, IOException {
        // Variable entera que almacena el ID del registro de inventario a eliminar
        int id = parseInt(req.getParameter("idInventario"));
        // Objeto InventarioCasa con los datos del producto antes de eliminarlo para mostrar su nombre
        InventarioCasa inv = new InventarioCasaDao().obtenerPorId(id, idHogar);
        // Variable de tipo texto que almacena el nombre del producto; vacío si el objeto es null
        String nombre = inv != null ? inv.getNombreProducto() : "";
        // Elimina el registro del inventario de la base de datos
        new InventarioCasaDao().eliminar(id, idHogar);
        // Asigna el nombre del producto eliminado para mostrarlo en la vista de confirmación
        req.setAttribute("nombreProducto", nombre);
        forward(req, resp, "07_ProductoEliminado.jsp");
    }

    // ─── Validaciones ─────────────────────────────────────────────────────────

    /* Método: validarNombreProducto
       Propósito: Verificar que el nombre del producto cumpla las reglas de longitud
       y caracteres permitidos definidas por el patrón P_NOMBRE_PRODUCTO.
       @param nombre → Texto con el nombre del producto a validar
       @return String → Mensaje de error en texto si no es válido, o null si es correcto
    */
    private String validarNombreProducto(String nombre) {
        // Si el nombre es null o vacío, es obligatorio
        if (nombre == null || nombre.isEmpty()) return "El nombre del producto es obligatorio.";
        // El nombre debe tener al menos 5 caracteres
        if (nombre.length() < 5) return "El nombre debe tener al menos 5 caracteres.";
        // El nombre no puede superar los 50 caracteres
        if (nombre.length() > 50) return "El nombre no puede superar 50 caracteres.";
        // El nombre solo puede contener letras, números, espacios y los símbolos . , # -
        if (!P_NOMBRE_PRODUCTO.matcher(nombre).matches())
            return "Solo se permiten letras, números, espacios y los símbolos: . , # -";
        // Si pasa todas las validaciones, retorna null indicando que el nombre es válido
        return null;
    }

    // ─── Utilidades ───────────────────────────────────────────────────────────

    /* Método: sesionValida
       Propósito: Verificar que exista una sesión HTTP activa con un usuario autenticado.
       Si no existe, redirige al login automáticamente.
       @param req  → Objeto de la petición HTTP
       @param resp → Objeto para redirigir si la sesión no es válida
       @return boolean → true si la sesión es válida, false si fue redirigido al login
    */
    private boolean sesionValida(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Variable de sesión HTTP; null si no existe sesión activa
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("usuario") == null) {
            // Si no hay sesión o el usuario no está autenticado, redirige al login con mensaje
            resp.sendRedirect(req.getContextPath() +
                "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_requerida");
            return false;
        }
        return true;
    }

    /* Método: forward
       Propósito: Hacer forward a una vista JSP del módulo de inventario usando la ruta
       base definida en la constante BASE.
       @param req   → Objeto de la petición HTTP
       @param resp  → Objeto de respuesta HTTP
       @param vista → Nombre del archivo JSP a mostrar (sin la ruta base)
    */
    private void forward(HttpServletRequest req, HttpServletResponse resp, String vista)
            throws ServletException, IOException {
        req.getRequestDispatcher(BASE + vista).forward(req, resp);
    }

    /* Método: idHogar
       Propósito: Obtener el ID del hogar del usuario desde la sesión activa de forma segura.
       @param req → Objeto de la petición HTTP
       @return int → Entero con el ID del hogar del usuario en sesión
    */
    private int idHogar(HttpServletRequest req) { return (Integer) req.getSession().getAttribute("idHogar"); }

    /* Método: idRol
       Propósito: Obtener el ID del rol del usuario desde la sesión activa de forma segura.
       @param req → Objeto de la petición HTTP
       @return int → Entero con el ID del rol del usuario en sesión (1=Admin, 2=Cotitular, 3=Invitado)
    */
    private int idRol(HttpServletRequest req)   { return (Integer) req.getSession().getAttribute("idRol"); }

    /* Método: parseBD
       Propósito: Convertir una cadena de texto a BigDecimal de forma segura, aceptando
       tanto punto como coma como separador decimal. Retorna ZERO si falla.
       @param s → Texto a convertir (ej. "1,5" o "1.5")
       @return BigDecimal → Valor decimal resultante, o BigDecimal.ZERO si la conversión falla
    */
    private BigDecimal parseBD(String s) {
        try {
            // Si el texto es null o vacío, retorna ZERO
            if (s == null || s.trim().isEmpty()) return BigDecimal.ZERO;
            // Reemplaza coma por punto para aceptar ambos formatos decimales
            return new BigDecimal(s.trim().replace(",", "."));
        } catch (Exception e) { return BigDecimal.ZERO; }
    }

    /* Método: parseInt
       Propósito: Convertir una cadena de texto a entero de forma segura, retornando 0
       si la conversión falla para evitar excepciones en tiempo de ejecución.
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
