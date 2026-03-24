package com.smarthome.smarthome_budget.controlador;

// Importación de clases necesarias para manejo de archivos, sesión y base de datos
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.nio.file.*;
import java.util.UUID;

import com.smarthome.smarthome_budget.dao.UsuarioDao;
import com.smarthome.smarthome_budget.modelo.Usuario;

/* Clase: PerfilServlet
   Propósito: Gestionar todas las operaciones del módulo Mi Perfil.
   Permite al usuario autenticado ver su perfil, editar sus datos personales
   (incluyendo foto), cambiar contraseña, cerrar sesión y eliminar su cuenta.
   URL mapeada: /Perfil
   Configuración: acepta archivos multipart de hasta 2 MB (para la foto de perfil)
*/
@WebServlet("/Perfil")
@MultipartConfig(maxFileSize = 2 * 1024 * 1024) // Tamaño máximo de archivo: 2 MB
public class PerfilServlet extends HttpServlet {

    // Instancia del DAO de usuario para todas las operaciones de datos del perfil
    private final UsuarioDao usuarioDao = new UsuarioDao();
    // Constante de tipo texto con la ruta base de las vistas de autenticación/perfil
    private static final String BASE = "/public/modules/01_autenticacion/";

    /* Método: doGet
       Propósito: Mostrar la vista correspondiente según la acción solicitada.
       Si no hay acción, muestra el perfil del usuario. Otras acciones dirigen
       a los formularios de edición, cierre de sesión o eliminación de cuenta.
       @param req  → Objeto con los parámetros de la petición HTTP GET
       @param resp → Objeto para hacer forward o redirigir a la vista
    */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Verifica que haya una sesión activa con usuario autenticado
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            // Sin sesión, redirige al formulario de inicio de sesión
            resp.sendRedirect(req.getContextPath() + BASE + "04_iniciarSesion.jsp");
            return;
        }

        // Variable de tipo texto que almacena la acción solicitada por parámetro URL
        String accion = req.getParameter("accion");
        // Si no se especifica acción, se asigna cadena vacía para el switch
        if (accion == null) accion = "";

        switch (accion) {
            case "editar":
                // Muestra el formulario de edición de datos del perfil
                req.getRequestDispatcher(BASE + "11_EditarPerfil.jsp").forward(req, resp);
                break;
            case "cerrarSesion":
                // Muestra la vista de confirmación antes de cerrar sesión
                req.getRequestDispatcher(BASE + "13_Cerrar_Sesion.jsp").forward(req, resp);
                break;
            case "eliminarCuenta":
                // Muestra la vista para que el usuario confirme la eliminación de su cuenta
                req.getRequestDispatcher(BASE + "14_EliminarCuenta.jsp").forward(req, resp);
                break;
            default:
                // Sin acción específica, muestra la vista principal del perfil
                req.getRequestDispatcher(BASE + "10_MiPerfil.jsp").forward(req, resp);
        }
    }

    /* Método: doPost
       Propósito: Procesar las acciones del perfil enviadas por formulario:
       guardar edición de datos, confirmar cierre de sesión o confirmar eliminación de cuenta.
       @param req  → Objeto con los datos del formulario (puede incluir archivo de foto)
       @param resp → Objeto para hacer forward o redirigir al resultado
    */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Verifica que haya una sesión activa con usuario autenticado
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            // Sin sesión, redirige al formulario de inicio de sesión
            resp.sendRedirect(req.getContextPath() + BASE + "04_iniciarSesion.jsp");
            return;
        }

        // Objeto Usuario recuperado de la sesión con los datos del usuario actual
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        // Variable de tipo texto que almacena la acción enviada por el formulario
        String accion = req.getParameter("accion");
        // Si no se especifica acción, se asigna cadena vacía para evitar NullPointerException
        if (accion == null) accion = "";

        switch (accion) {

            // ── GUARDAR EDICIÓN DE PERFIL ───────────────────────────────────
            case "guardarEdicion": {
                // Variable de tipo texto que almacena el nuevo correo electrónico ingresado
                String correo    = req.getParameter("correo");
                // Variable de tipo texto que almacena el nuevo número de teléfono ingresado
                String telefono  = req.getParameter("telefono");
                // Variable de tipo texto que almacena el nuevo nombre de usuario ingresado
                String username  = req.getParameter("Usuario");

                // Variable de tipo texto que almacena la ruta relativa de la foto guardada; null si no se subió
                String rutaFoto = null;
                try {
                    // Recupera la parte del formulario correspondiente al archivo de foto
                    Part foto = req.getPart("foto");
                    if (foto != null && foto.getSize() > 0) {
                        // Variable de tipo texto que almacena el tipo MIME del archivo subido
                        String contentType = foto.getContentType();
                        // Solo se aceptan imágenes PNG, JPEG y JPG
                        if (contentType != null && (contentType.contains("png") || contentType.contains("jpeg") || contentType.contains("jpg"))) {
                            // Genera un nombre de archivo único usando UUID para evitar colisiones
                            String fileName   = UUID.randomUUID() + "_" + Paths.get(foto.getSubmittedFileName()).getFileName().toString();

                            // 1. Guarda la foto en el directorio desplegado (para acceso inmediato en ejecución)
                            String uploadPath = getServletContext().getRealPath("/asset/fotos/");
                            Files.createDirectories(Paths.get(uploadPath));
                            foto.write(uploadPath + File.separator + fileName);
                            // Asigna la ruta relativa de la foto para guardar en base de datos
                            rutaFoto = "/asset/fotos/" + fileName;

                            // 2. Copia la foto a src/main/webapp para que sobreviva a Maven Clean and Build
                            try {
                                // Objeto File que representa la raíz del directorio desplegado
                                File deployedRoot = new File(getServletContext().getRealPath("/"));
                                // Ruta de tipo Path que apunta a la carpeta de fotos en el código fuente
                                Path srcFotosDir  = deployedRoot.toPath()
                                        .getParent()   // Sube a target/
                                        .getParent()   // Sube a la raíz del proyecto
                                        .resolve("src/main/webapp/asset/fotos");
                                Files.createDirectories(srcFotosDir);
                                // Copia el archivo sobreescribiendo si ya existe con el mismo nombre
                                Files.copy(
                                    Paths.get(uploadPath, fileName),
                                    srcFotosDir.resolve(fileName),
                                    StandardCopyOption.REPLACE_EXISTING
                                );
                            } catch (Exception ignored) { /* No bloquea el flujo si la copia falla */ }
                        }
                    }
                } catch (Exception e) {
                    // Si ocurre cualquier error al leer la foto, continúa sin actualizar la imagen
                }

                // Actualiza los datos del perfil en la base de datos
                // Variable booleana que indica si la actualización fue exitosa
                boolean ok = usuarioDao.actualizarPerfil(usuario.getIDUsuario(), correo, telefono, username, rutaFoto);
                if (ok) {
                    // Refresca el objeto usuario en la sesión con los datos actualizados
                    Usuario actualizado = usuarioDao.obtenerPorId(usuario.getIDUsuario());
                    if (actualizado != null) session.setAttribute("usuario", actualizado);
                    // Muestra la vista de confirmación de cambios guardados
                    req.getRequestDispatcher(BASE + "12_Confirmar-cambio.jsp").forward(req, resp);
                } else {
                    // Si falló la actualización, muestra el error en el formulario
                    req.setAttribute("error", "No se pudo actualizar el perfil. Intenta de nuevo.");
                    req.getRequestDispatcher(BASE + "11_EditarPerfil.jsp").forward(req, resp);
                }
                break;
            }

            // ── CONFIRMAR CIERRE DE SESIÓN ──────────────────────────────────
            case "confirmarCerrarSesion": {
                // Invalida la sesión activa eliminando todos los atributos almacenados
                session.invalidate();
                // Redirige a la página principal de la aplicación
                resp.sendRedirect(req.getContextPath() + BASE + "01_principal.jsp");
                break;
            }

            // ── ELIMINAR CUENTA ─────────────────────────────────────────────
            case "confirmarEliminar": {
                // Variable de tipo texto que almacena la contraseña ingresada para confirmar la eliminación
                String password = req.getParameter("password");
                // Variable booleana que indica si la contraseña ingresada coincide con la almacenada
                boolean passOk = com.smarthome.smarthome_budget.utils.Encriptador.verificar(password, usuario.getContrasenaUsuario());
                if (passOk) {
                    // Variable booleana que indica si la cuenta fue eliminada correctamente de la BD
                    boolean eliminado = usuarioDao.eliminarUsuario(usuario.getIDUsuario());
                    if (eliminado) {
                        // Elimina la sesión y muestra la vista de confirmación de eliminación
                        session.invalidate();
                        req.getRequestDispatcher(BASE + "15_ConfirmarEliminacion.jsp").forward(req, resp);
                        return;
                    }
                }
                // Si la contraseña no coincide o la eliminación falló, muestra el error
                req.setAttribute("error", "Contraseña incorrecta o no se pudo eliminar la cuenta.");
                req.getRequestDispatcher(BASE + "14_EliminarCuenta.jsp").forward(req, resp);
                break;
            }

            default:
                // Si la acción no es reconocida, redirige al perfil del usuario
                resp.sendRedirect(req.getContextPath() + "/Perfil");
        }
    }
}
