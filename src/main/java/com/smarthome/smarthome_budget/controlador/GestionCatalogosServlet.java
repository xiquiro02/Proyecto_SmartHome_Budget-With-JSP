package com.smarthome.smarthome_budget.controlador;

// Importación de todas las clases DAO y modelo necesarias para gestionar los catálogos
import com.smarthome.smarthome_budget.dao.*;
import com.smarthome.smarthome_budget.modelo.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/* Clase: GestionCatalogosServlet
   Propósito: Gestionar los catálogos del sistema: categorías de egreso, categorías de ingreso,
   métodos de pago y tipos de producto. Permite crear, editar y eliminar elementos de cada
   catálogo con validaciones de duplicados y restricciones de uso. Solo accesible para
   usuarios con rol 1 (Administrador) o rol 2 (Cotitular); el rol 3 no tiene acceso.
   URL mapeada: /Catalogos
*/
@WebServlet("/Catalogos")
public class GestionCatalogosServlet extends HttpServlet {

    // Constante de tipo texto con la ruta base de las vistas del módulo de catálogos
    private static final String BASE = "/public/modules/06_Catalogos/";

    /* Método: doGet
       Propósito: Mostrar la lista de elementos del catálogo solicitado o el formulario
       de edición según el parámetro "tipo" y la acción. Bloquea el acceso al rol 3.
       @param req  → Objeto con los parámetros "tipo" y "accion" en la URL
       @param resp → Objeto para hacer forward a la vista del catálogo correspondiente
    */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Verifica que haya sesión activa con usuario autenticado
        if (!sesionValida(req, resp)) return;
        // Variable entera que almacena el ID del rol del usuario en sesión
        int idRol = idRol(req);

        // El rol 3 (Invitado) no tiene acceso a la gestión de catálogos
        if (idRol == 3) { resp.sendRedirect(req.getContextPath() + "/Menu?error=acceso_denegado"); return; }

        // Variable de tipo texto que almacena el tipo de catálogo solicitado (ej. "categoriaEgreso")
        String tipo   = nvl(req.getParameter("tipo"));
        // Variable de tipo texto que almacena la acción solicitada (ej. "editar" o vacío para listar)
        String accion = nvl(req.getParameter("accion"));

        // Enruta según el tipo de catálogo: si la acción es "editar" muestra el formulario, si no lista
        switch (tipo) {
            case "categoriaEgreso":
                if ("editar".equals(accion)) mostrarFormEditar(req, resp, tipo); else listar(req, resp, tipo); break;
            case "categoriaIngreso":
                if ("editar".equals(accion)) mostrarFormEditar(req, resp, tipo); else listar(req, resp, tipo); break;
            case "metodoPago":
                if ("editar".equals(accion)) mostrarFormEditar(req, resp, tipo); else listar(req, resp, tipo); break;
            case "tipoProducto":
                if ("editar".equals(accion)) mostrarFormEditar(req, resp, tipo); else listar(req, resp, tipo); break;
            default:
                // Si el tipo no es reconocido, redirige al menú principal
                resp.sendRedirect(req.getContextPath() + "/Menu");
        }
    }

    /* Método: doPost
       Propósito: Procesar las operaciones CRUD sobre los catálogos: crear, actualizar o eliminar
       elementos. Valida que el nombre no esté vacío, que no esté duplicado y que el elemento
       no tenga registros asociados antes de eliminar. Solo el rol 1 puede eliminar.
       @param req  → Objeto con los parámetros "tipo", "accion", "nombre" e "id" del formulario
       @param resp → Objeto para redirigir o hacer forward al resultado
    */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Establece la codificación UTF-8 para aceptar caracteres especiales
        req.setCharacterEncoding("UTF-8");
        // Verifica que haya sesión activa con usuario autenticado
        if (!sesionValida(req, resp)) return;
        // Variable entera que almacena el ID del rol del usuario
        int idRol = idRol(req);
        // El rol 3 no puede realizar operaciones sobre los catálogos
        if (idRol == 3) { resp.sendRedirect(req.getContextPath() + "/Menu?error=acceso_denegado"); return; }

        // Variable de tipo texto que almacena el tipo de catálogo sobre el que se opera
        String tipo   = nvl(req.getParameter("tipo"));
        // Variable de tipo texto que almacena la operación a realizar: "crear", "actualizar" o "eliminar"
        String accion = nvl(req.getParameter("accion"));
        // Variable de tipo texto que almacena el nombre del elemento del catálogo (sin espacios al inicio/final)
        String nombre = nvl(req.getParameter("nombre")).trim();
        // Variable entera que almacena el ID del elemento a editar o eliminar (0 si es nuevo)
        int id        = parseInt(req.getParameter("id"));

        // Solo el rol 1 (Administrador) puede eliminar elementos del catálogo
        if ("eliminar".equals(accion) && idRol != 1) {
            resp.sendRedirect(req.getContextPath() + "/Catalogos?tipo=" + tipo + "&error=sin_permiso"); return;
        }

        // Valida que el nombre no esté vacío para las acciones de crear o actualizar
        if (nombre.isEmpty() && !"eliminar".equals(accion)) {
            req.setAttribute("error", "El nombre no puede estar vacío.");
            req.setAttribute("tipo", tipo);
            listar(req, resp, tipo); return;
        }

        // Variable booleana que indica si la operación fue exitosa
        boolean ok = false;
        // Variable de tipo texto que almacena el mensaje de error si la operación falla; null si no hay error
        String errorMsg = null;

        // Ejecuta la operación correspondiente según el tipo de catálogo
        switch (tipo) {
            case "categoriaEgreso": {
                // Instancia del DAO de categoría de egreso para operar sobre ese catálogo
                CategoriaEgresoDao dao = new CategoriaEgresoDao();
                switch (accion) {
                    case "crear":
                        // Verifica que el nombre no esté duplicado (id=0 indica nuevo elemento)
                        if (dao.nombreDuplicado(nombre, 0)) { errorMsg = "Ya existe una categoría con ese nombre."; break; }
                        // Variable booleana de resultado: true si se creó correctamente
                        ok = dao.crear(nombre) > 0; break;
                    case "actualizar":
                        // Verifica duplicados excluyendo el elemento actual (por su id)
                        if (dao.nombreDuplicado(nombre, id)) { errorMsg = "Ya existe una categoría con ese nombre."; break; }
                        ok = dao.actualizar(id, nombre); break;
                    case "eliminar":
                        // Verifica que la categoría no tenga facturas o egresos asociados
                        if (dao.tieneEgresos(id)) { errorMsg = "No se puede eliminar: tiene facturas o egresos asociados."; break; }
                        ok = dao.eliminar(id); break;
                }
                break;
            }
            case "categoriaIngreso": {
                // Instancia del DAO de categoría de ingreso para operar sobre ese catálogo
                CategoriaIngresoDao dao = new CategoriaIngresoDao();
                switch (accion) {
                    case "crear":
                        if (dao.nombreDuplicado(nombre, 0)) { errorMsg = "Ya existe una categoría con ese nombre."; break; }
                        ok = dao.crear(nombre) > 0; break;
                    case "actualizar":
                        if (dao.nombreDuplicado(nombre, id)) { errorMsg = "Ya existe una categoría con ese nombre."; break; }
                        ok = dao.actualizar(id, nombre); break;
                    case "eliminar":
                        // Verifica que la categoría no tenga ingresos asociados
                        if (dao.tieneIngresos(id)) { errorMsg = "No se puede eliminar: tiene ingresos asociados."; break; }
                        ok = dao.eliminar(id); break;
                }
                break;
            }
            case "metodoPago": {
                // Instancia del DAO de método de pago para operar sobre ese catálogo
                MetodoPagoDao dao = new MetodoPagoDao();
                switch (accion) {
                    case "crear":
                        if (dao.nombreDuplicado(nombre, 0)) { errorMsg = "Ya existe un método con ese nombre."; break; }
                        ok = dao.crear(nombre) > 0; break;
                    case "actualizar":
                        if (dao.nombreDuplicado(nombre, id)) { errorMsg = "Ya existe un método con ese nombre."; break; }
                        ok = dao.actualizar(id, nombre); break;
                    case "eliminar":
                        // Verifica que no sea el método predeterminado (Efectivo) ni tenga egresos asociados
                        if (dao.esPredeterminado(id)) { errorMsg = "No se puede eliminar el método predeterminado (Efectivo)."; break; }
                        if (dao.tieneEgresos(id)) { errorMsg = "No se puede eliminar: tiene egresos asociados."; break; }
                        ok = dao.eliminar(id); break;
                }
                break;
            }
            case "tipoProducto": {
                // Instancia del DAO de tipo de producto para operar sobre ese catálogo
                TipoProductoDao dao = new TipoProductoDao();
                switch (accion) {
                    case "crear":
                        if (dao.nombreDuplicado(nombre, 0)) { errorMsg = "Ya existe un tipo con ese nombre."; break; }
                        ok = dao.crear(nombre) > 0; break;
                    case "actualizar":
                        if (dao.nombreDuplicado(nombre, id)) { errorMsg = "Ya existe un tipo con ese nombre."; break; }
                        ok = dao.actualizar(id, nombre); break;
                    case "eliminar":
                        // Verifica que el tipo no tenga productos registrados en el inventario
                        if (dao.tieneProductos(id)) { errorMsg = "No se puede eliminar: tiene productos registrados."; break; }
                        ok = dao.eliminar(id); break;
                }
                break;
            }
        }

        if (errorMsg != null) {
            // Si hay un mensaje de error, lo pasa como atributo y muestra de nuevo la lista
            req.setAttribute("error", errorMsg);
            req.setAttribute("tipo", tipo);
            listar(req, resp, tipo);
        } else if (ok) {
            // Si la operación fue exitosa, redirige con parámetro de éxito para mostrar notificación
            resp.sendRedirect(req.getContextPath() + "/Catalogos?tipo=" + tipo + "&exito=" + accion);
        } else {
            // Si la operación falló sin un error específico, muestra error genérico
            req.setAttribute("error", "No se pudo completar la operación. Intenta de nuevo.");
            req.setAttribute("tipo", tipo);
            listar(req, resp, tipo);
        }
    }

    /* Método: listar
       Propósito: Cargar los elementos del catálogo indicado y hacer forward a la vista
       de gestión de catálogos con los datos necesarios para mostrar la lista.
       @param req  → Objeto de la petición HTTP
       @param resp → Objeto para hacer forward a la vista
       @param tipo → Texto que identifica el tipo de catálogo a mostrar
    */
    private void listar(HttpServletRequest req, HttpServletResponse resp, String tipo)
            throws ServletException, IOException {
        // Carga la lista de elementos según el tipo de catálogo
        cargarDatos(req, tipo);
        // Pasa el tipo como atributo para que la vista sepa qué catálogo está mostrando
        req.setAttribute("tipo", tipo);
        // Hace forward a la vista única de gestión de catálogos
        req.getRequestDispatcher(BASE + "01_gestionCatalogos.jsp").forward(req, resp);
    }

    /* Método: mostrarFormEditar
       Propósito: Obtener el elemento a editar por su ID y mostrarlo en el formulario
       de edición dentro de la vista de catálogos.
       @param req  → Objeto de la petición HTTP (contiene el parámetro "id")
       @param resp → Objeto para hacer forward o redirigir
       @param tipo → Texto que identifica el tipo de catálogo
    */
    private void mostrarFormEditar(HttpServletRequest req, HttpServletResponse resp, String tipo)
            throws ServletException, IOException {
        // Variable entera que almacena el ID del elemento a editar
        int id = parseInt(req.getParameter("id"));
        // Objeto genérico que contiene el elemento encontrado; null si no existe
        Object item = obtenerPorId(tipo, id);
        if (item == null) { resp.sendRedirect(req.getContextPath() + "/Catalogos?tipo=" + tipo); return; }
        // Pasa el elemento a editar a la vista
        req.setAttribute("itemEditar", item);
        req.setAttribute("tipo", tipo);
        cargarDatos(req, tipo);
        req.getRequestDispatcher(BASE + "01_gestionCatalogos.jsp").forward(req, resp);
    }

    /* Método: cargarDatos
       Propósito: Consultar todos los elementos del catálogo indicado y asignarlos
       como atributo "items" del request para que la vista los pueda listar.
       @param req  → Objeto de la petición HTTP donde se asigna el atributo "items"
       @param tipo → Texto que identifica el tipo de catálogo a consultar
    */
    private void cargarDatos(HttpServletRequest req, String tipo) {
        switch (tipo) {
            // Asigna la lista de categorías de egreso al atributo "items"
            case "categoriaEgreso":  req.setAttribute("items", new CategoriaEgresoDao().listarCategorias()); break;
            // Asigna la lista de categorías de ingreso al atributo "items"
            case "categoriaIngreso": req.setAttribute("items", new CategoriaIngresoDao().listarCategorias()); break;
            // Asigna la lista de métodos de pago al atributo "items"
            case "metodoPago":       req.setAttribute("items", new MetodoPagoDao().listarMetodosPago()); break;
            // Asigna la lista de tipos de producto al atributo "items"
            case "tipoProducto":     req.setAttribute("items", new TipoProductoDao().listarTipos()); break;
        }
    }

    /* Método: obtenerPorId
       Propósito: Consultar un elemento específico del catálogo por su ID,
       retornando el objeto correspondiente al tipo indicado.
       @param tipo → Texto que identifica el tipo de catálogo
       @param id   → Entero con el ID del elemento a buscar
       @return Object → El objeto encontrado (CategoriaEgreso, MetodoPago, etc.) o null si no existe
    */
    private Object obtenerPorId(String tipo, int id) {
        switch (tipo) {
            case "categoriaEgreso":  return new CategoriaEgresoDao().obtenerPorId(id);
            case "categoriaIngreso": return new CategoriaIngresoDao().obtenerPorId(id);
            case "metodoPago":       return new MetodoPagoDao().obtenerPorId(id);
            case "tipoProducto":     return new TipoProductoDao().obtenerPorId(id);
            default: return null;
        }
    }

    /* Método: sesionValida
       Propósito: Verificar que exista una sesión HTTP activa con un usuario autenticado.
       Si no existe, redirige al login automáticamente.
       @param req  → Objeto de la petición HTTP
       @param resp → Objeto para redirigir si la sesión no es válida
       @return boolean → true si la sesión es válida, false si fue redirigido
    */
    private boolean sesionValida(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/public/modules/01_autenticacion/04_iniciarSesion.jsp?error=sesion_requerida");
            return false;
        }
        return true;
    }

    /* Método: idRol
       Propósito: Obtener el ID del rol del usuario desde la sesión activa de forma segura.
       @param req → Objeto de la petición HTTP
       @return int → Entero con el ID del rol (por defecto 3 si no se encuentra en sesión)
    */
    private int idRol(HttpServletRequest req) {
        Object r = req.getSession().getAttribute("idRol");
        return r != null ? (Integer) r : 3;
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
       @return String → El mismo texto sin espacios al inicio/final, o "" si era null
    */
    private String nvl(String s)   { return s == null ? "" : s.trim(); }
}
