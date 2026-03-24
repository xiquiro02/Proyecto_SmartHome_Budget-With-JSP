package com.smarthome.smarthome_budget.dao;

import java.sql.*;
import com.smarthome.smarthome_budget.basedatos.claseConexion;
import com.smarthome.smarthome_budget.modelo.Usuario;
import com.smarthome.smarthome_budget.utils.Encriptador;

/* Clase: UsuarioDao
   Propósito: Gestionar las operaciones de acceso a datos de la tabla Usuario. Centraliza
   el registro, consulta, autenticación, actualización de perfil y contraseña, y eliminación
   de cuentas. La contraseña siempre se almacena encriptada mediante Encriptador. Las operaciones
   de token de recuperación se delegan a TokensDao para mantener separación de responsabilidades.
*/
public class UsuarioDao {

    // Consulta SQL para insertar un nuevo usuario con todos sus datos personales y contraseña encriptada
    // language=sql
    private static final String SQL_INSERT =
        "INSERT INTO Usuario (Documento, NombreUsuario, Apellido, correo, telefono, ContrasenaUsuario) " +
        "VALUES (?, ?, ?, ?, ?, ?)";

    // Consulta SQL que obtiene todos los datos de un usuario por su correo electrónico
    // language=sql
    private static final String SQL_SELECT_POR_CORREO =
        "SELECT * FROM Usuario WHERE correo = ?";

    // Consulta SQL que obtiene todos los datos de un usuario por su ID
    // language=sql
    private static final String SQL_SELECT_POR_ID =
        "SELECT * FROM Usuario WHERE IDUsuario = ?";

    // Consulta SQL para actualizar la contraseña de un usuario identificado por su correo
    // language=sql
    private static final String SQL_UPDATE_CLAVE =
        "UPDATE Usuario SET ContrasenaUsuario = ? WHERE correo = ?";

    // Consulta SQL para actualizar la contraseña de un usuario identificado por su ID
    // language=sql
    private static final String SQL_UPDATE_CLAVE_POR_ID =
        "UPDATE Usuario SET ContrasenaUsuario = ? WHERE IDUsuario = ?";

    // Consulta SQL para actualizar datos del perfil del usuario sin cambiar la foto
    // language=sql
    private static final String SQL_UPDATE_PERFIL_BASE =
        "UPDATE Usuario SET correo=?, telefono=?, NombreUsuario=? WHERE IDUsuario=?";

    // Consulta SQL para actualizar datos del perfil incluyendo la ruta de la foto de perfil
    // language=sql
    private static final String SQL_UPDATE_PERFIL_CON_FOTO =
        "UPDATE Usuario SET correo=?, telefono=?, NombreUsuario=?, fotoPerfil=? WHERE IDUsuario=?";

    // Consulta SQL para eliminar un usuario por su ID
    // language=sql
    private static final String SQL_DELETE =
        "DELETE FROM Usuario WHERE IDUsuario = ?";

    // Consulta SQL que verifica si existe un usuario con el correo indicado
    // language=sql
    private static final String SQL_EXISTE_CORREO =
        "SELECT IDUsuario FROM Usuario WHERE correo = ?";

    // Consulta SQL que verifica si existe un usuario con el número de documento indicado
    // language=sql
    private static final String SQL_EXISTE_DOCUMENTO =
        "SELECT IDUsuario FROM Usuario WHERE Documento = ?";

    /* Método: registrarUsuario
       Propósito: Insertar un nuevo usuario en la base de datos, encriptando su contraseña
       antes de almacenarla. Retorna el ID autogenerado para asociarlo a un hogar.
       @param usuario → Objeto Usuario con todos los campos del formulario de registro
       @return int → ID del usuario creado, o -1 si la inserción falla
    */
    public int registrarUsuario(Usuario usuario) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, usuario.getDocumento());
            ps.setString(2, usuario.getNombreUsuario());
            // Texto con el apellido del usuario; se usa cadena vacía si viene nulo
            String apellido = usuario.getApellido() != null ? usuario.getApellido() : "";
            ps.setString(3, apellido);
            ps.setString(4, usuario.getCorreo());
            ps.setString(5, usuario.getTelefono());
            ps.setString(6, Encriptador.encriptar(usuario.getContrasenaUsuario()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                // Variable entera que almacena el ID autogenerado por la base de datos
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDao] Error al registrar usuario: " + e.getMessage());
        }
        return -1;
    }

    /* Método: correoExiste
       Propósito: Verificar si ya existe un usuario registrado con el correo indicado.
       Se usa en el formulario de registro para evitar duplicados.
       @param correo → Texto con el correo electrónico a verificar
       @return boolean → true si el correo ya está registrado, false si está disponible
    */
    public boolean correoExiste(String correo) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_EXISTE_CORREO)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) {
            System.err.println("[UsuarioDao] Error al verificar correo: " + e.getMessage());
        }
        return false;
    }

    /* Método: documentoExiste
       Propósito: Verificar si ya existe un usuario registrado con el número de documento indicado.
       Se usa en el formulario de registro para evitar duplicados de identidad.
       @param documento → Texto con el número de documento a verificar
       @return boolean → true si el documento ya está registrado, false si está disponible
    */
    public boolean documentoExiste(String documento) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_EXISTE_DOCUMENTO)) {
            ps.setString(1, documento);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) {
            System.err.println("[UsuarioDao] Error al verificar documento: " + e.getMessage());
        }
        return false;
    }

    /* Método: obtenerUsuarioPorCorreo
       Propósito: Buscar y construir un objeto Usuario a partir de su correo electrónico.
       Se usa principalmente en la recuperación de contraseña y al vincular tokens.
       @param correo → Texto con el correo del usuario a buscar
       @return Usuario → El objeto encontrado, o null si no existe ningún usuario con ese correo
    */
    public Usuario obtenerUsuarioPorCorreo(String correo) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_POR_CORREO)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearUsuario(rs);
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDao] Error al obtener usuario por correo: " + e.getMessage());
        }
        return null;
    }

    /* Método: obtenerPorId
       Propósito: Buscar y construir un objeto Usuario a partir de su ID.
       Se usa al cargar datos del perfil o al verificar permisos en sesión.
       @param idUsuario → Entero con el ID del usuario a buscar
       @return Usuario → El objeto encontrado, o null si no existe un usuario con ese ID
    */
    public Usuario obtenerPorId(int idUsuario) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_POR_ID)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearUsuario(rs);
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDao] Error al obtener usuario por id: " + e.getMessage());
        }
        return null;
    }

    /* Método: login
       Propósito: Autenticar a un usuario verificando su correo y contraseña. La contraseña
       ingresada se compara con el hash almacenado usando el Encriptador.
       @param correo    → Texto con el correo del usuario que intenta iniciar sesión
       @param contrasena → Texto con la contraseña en texto plano a verificar
       @return Usuario → El objeto del usuario autenticado, o null si las credenciales son inválidas
    */
    public Usuario login(String correo, String contrasena) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_POR_CORREO)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Texto con el hash de contraseña almacenado en la base de datos
                    String hashBD = rs.getString("ContrasenaUsuario");
                    if (Encriptador.verificar(contrasena, hashBD)) {
                        return mapearUsuario(rs);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDao] Error al iniciar sesión: " + e.getMessage());
        }
        return null;
    }

    /* Método: guardarToken
       Propósito: Crear un token de recuperación de contraseña para el usuario identificado
       por su correo. Delega la creación al TokensDao tras obtener el ID del usuario.
       @param correo → Texto con el correo del usuario que solicita recuperar su contraseña
       @param token  → Texto con el valor único del token generado externamente
       @return boolean → true si el token fue guardado correctamente, false si el correo no existe o falló
    */
    public boolean guardarToken(String correo, String token) {
        // Objeto Usuario recuperado de la base de datos para obtener su ID antes de guardar el token
        Usuario u = obtenerUsuarioPorCorreo(correo);
        if (u == null) return false;
        TokensDao tokensDao = new TokensDao();
        return tokensDao.crearToken(u.getIDUsuario(), token);
    }

    /* Método: obtenerCorreoPorToken
       Propósito: Validar un token de recuperación y obtener el correo del usuario asociado.
       Delega la validación al TokensDao.
       @param token → Texto con el valor del token de recuperación a validar
       @return String → Correo del usuario si el token es válido, o null si es inválido o expirado
    */
    public String obtenerCorreoPorToken(String token) {
        TokensDao tokensDao = new TokensDao();
        return tokensDao.obtenerCorreoPorToken(token);
    }

    /* Método: actualizarClave
       Propósito: Actualizar la contraseña de un usuario identificado por su correo, encriptando
       el nuevo valor antes de guardarlo. Invalida todos los tokens activos del usuario al finalizar.
       @param correo    → Texto con el correo del usuario cuya contraseña se actualizará
       @param nuevaClave → Texto con la nueva contraseña en texto plano
       @return boolean → true si la contraseña fue actualizada, false si falló
    */
    public boolean actualizarClave(String correo, String nuevaClave) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE_CLAVE)) {
            ps.setString(1, Encriptador.encriptar(nuevaClave));
            ps.setString(2, correo);
            // Variable booleana que indica si la actualización afectó al menos una fila
            boolean ok = ps.executeUpdate() > 0;
            if (ok) {
                // Al cambiar la clave se invalidan todos los tokens de recuperación activos del usuario
                new TokensDao().invalidarTokensPorCorreo(correo);
            }
            return ok;
        } catch (SQLException e) {
            System.err.println("[UsuarioDao] Error al actualizar clave: " + e.getMessage());
        }
        return false;
    }

    /* Método: actualizarClaveConHash
       Propósito: Actualizar la contraseña de un usuario identificado por su ID, encriptando
       el nuevo valor. Se usa desde el módulo de perfil donde el usuario ya está autenticado.
       @param idUsuario → Entero con el ID del usuario cuya contraseña se actualizará
       @param nuevaClave → Texto con la nueva contraseña en texto plano
       @return boolean → true si la contraseña fue actualizada, false si falló
    */
    public boolean actualizarClaveConHash(int idUsuario, String nuevaClave) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE_CLAVE_POR_ID)) {
            ps.setString(1, Encriptador.encriptar(nuevaClave));
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UsuarioDao] Error al actualizar clave por id: " + e.getMessage());
        }
        return false;
    }

    /* Método: actualizarPerfil
       Propósito: Actualizar los datos del perfil de un usuario. Si se proporciona una ruta
       de foto, se incluye en la actualización; de lo contrario, se omite ese campo.
       @param idUsuario    → Entero con el ID del usuario a actualizar
       @param correo       → Texto con el nuevo correo electrónico
       @param telefono     → Texto con el nuevo número de teléfono
       @param nombreUsuario → Texto con el nuevo nombre del usuario
       @param rutaFoto     → Texto con la ruta de la nueva foto de perfil, o null/vacío si no cambia
       @return boolean → true si el perfil fue actualizado correctamente, false si falló
    */
    public boolean actualizarPerfil(int idUsuario, String correo, String telefono,
                                    String nombreUsuario, String rutaFoto) {
        // Texto con la consulta SQL a usar: varía según si se actualiza o no la foto de perfil
        String sql = (rutaFoto != null && !rutaFoto.isEmpty())
                     ? SQL_UPDATE_PERFIL_CON_FOTO
                     : SQL_UPDATE_PERFIL_BASE;
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setString(2, telefono);
            ps.setString(3, nombreUsuario);
            if (rutaFoto != null && !rutaFoto.isEmpty()) {
                ps.setString(4, rutaFoto);
                ps.setInt(5, idUsuario);
            } else {
                ps.setInt(4, idUsuario);
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UsuarioDao] Error al actualizar perfil: " + e.getMessage());
        }
        return false;
    }

    /* Método: eliminarUsuario
       Propósito: Eliminar la cuenta de un usuario de la base de datos por su ID.
       Se usa en el módulo de perfil cuando el usuario decide eliminar su cuenta.
       @param idUsuario → Entero con el ID del usuario a eliminar
       @return boolean → true si el usuario fue eliminado, false si no existía o falló
    */
    public boolean eliminarUsuario(int idUsuario) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UsuarioDao] Error al eliminar usuario: " + e.getMessage());
        }
        return false;
    }

    /* Método: mapearUsuario
       Propósito: Construir un objeto Usuario a partir de la fila actual del ResultSet,
       mapeando todas las columnas de la tabla Usuario incluyendo la foto de perfil si existe.
       @param rs → ResultSet posicionado en la fila a mapear
       @return Usuario → El objeto construido con los datos de la fila
    */
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        // Objeto Usuario que recibirá los valores de cada columna de la fila
        Usuario u = new Usuario();
        u.setIDUsuario(rs.getInt("IDUsuario"));
        u.setDocumento(rs.getString("Documento"));
        u.setNombreUsuario(rs.getString("NombreUsuario"));
        u.setApellido(rs.getString("Apellido"));
        u.setCorreo(rs.getString("correo"));
        u.setTelefono(rs.getString("telefono"));
        u.setContrasenaUsuario(rs.getString("ContrasenaUsuario"));
        u.setFechaRegistro(rs.getObject("FechaRegistro", java.time.LocalDateTime.class));
        // La foto de perfil puede no existir en todas las consultas; se ignora si falla
        try { u.setFotoPerfil(rs.getString("fotoPerfil")); } catch (Exception ignore) {}
        return u;
    }
}
