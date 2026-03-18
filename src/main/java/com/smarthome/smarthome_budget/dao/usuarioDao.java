package com.smarthome.smarthome_budget.dao;

import java.sql.*;
import com.smarthome.smarthome_budget.basedatos.claseConexion;
import com.smarthome.smarthome_budget.modelo.Usuario;
import com.smarthome.smarthome_budget.utils.Encriptador;

public class UsuarioDao {

    // language=sql
    private static final String SQL_INSERT =
        "INSERT INTO Usuario (Documento, NombreUsuario, Apellido, correo, telefono, ContrasenaUsuario) " +
        "VALUES (?, ?, ?, ?, ?, ?)";
    // language=sql
    private static final String SQL_SELECT_POR_CORREO =
        "SELECT * FROM Usuario WHERE correo = ?";
    // language=sql
    private static final String SQL_SELECT_POR_ID =
        "SELECT * FROM Usuario WHERE IDUsuario = ?";
    // language=sql
    private static final String SQL_UPDATE_CLAVE =
        "UPDATE Usuario SET ContrasenaUsuario = ? WHERE correo = ?";
    // language=sql
    private static final String SQL_UPDATE_CLAVE_POR_ID =
        "UPDATE Usuario SET ContrasenaUsuario = ? WHERE IDUsuario = ?";
    // language=sql
    private static final String SQL_UPDATE_PERFIL_BASE =
        "UPDATE Usuario SET correo=?, telefono=?, NombreUsuario=? WHERE IDUsuario=?";
    // language=sql
    private static final String SQL_UPDATE_PERFIL_CON_FOTO =
        "UPDATE Usuario SET correo=?, telefono=?, NombreUsuario=?, fotoPerfil=? WHERE IDUsuario=?";
    // language=sql
    private static final String SQL_DELETE =
        "DELETE FROM Usuario WHERE IDUsuario = ?";
    // language=sql
    private static final String SQL_EXISTE_CORREO =
        "SELECT IDUsuario FROM Usuario WHERE correo = ?";
    // language=sqlS
    private static final String SQL_EXISTE_DOCUMENTO =
        "SELECT IDUsuario FROM Usuario WHERE Documento = ?";

    // ── Registrar usuario ─────────────────────────────────────────────────────

    public int registrarUsuario(Usuario usuario) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, usuario.getDocumento());
            ps.setString(2, usuario.getNombreUsuario());
            // Apellido: si viene de getPrimerApellido(), el alias en el modelo lo resuelve
            String apellido = usuario.getApellido() != null ? usuario.getApellido() : "";
            ps.setString(3, apellido);
            ps.setString(4, usuario.getCorreo());
            ps.setString(5, usuario.getTelefono());
            ps.setString(6, Encriptador.encriptar(usuario.getContrasenaUsuario()));

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
        }
        return -1;
    }

    // ── Verificaciones de existencia ──────────────────────────────────────────
    public boolean correoExiste(String correo) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_EXISTE_CORREO)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) {
            System.err.println("Error al verificar correo: " + e.getMessage());
        }
        return false;
    }

    public boolean documentoExiste(String documento) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_EXISTE_DOCUMENTO)) {
            ps.setString(1, documento);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) {
            System.err.println("Error al verificar documento: " + e.getMessage());
        }
        return false;
    }

    // ── Obtener usuario ───────────────────────────────────────────────────────

    public Usuario obtenerUsuarioPorCorreo(String correo) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_POR_CORREO)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearUsuario(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por correo: " + e.getMessage());
        }
        return null;
    }

    public Usuario obtenerPorId(int idUsuario) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_POR_ID)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearUsuario(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por id: " + e.getMessage());
        }
        return null;
    }

    // ── Login ─────────────────────────────────────────────────────────────────

    public Usuario login(String correo, String contrasena) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_POR_CORREO)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashBD = rs.getString("ContrasenaUsuario");
                    if (Encriptador.verificar(contrasena, hashBD)) {
                        return mapearUsuario(rs);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al iniciar sesión: " + e.getMessage());
        }
        return null;
    }

    // ── Token de recuperación (delegado a TokensDao) ──────────────────────────
    public boolean guardarToken(String correo, String token) {
        Usuario u = obtenerUsuarioPorCorreo(correo);
        if (u == null) return false;
        TokensDao tokensDao = new TokensDao();
        return tokensDao.crearToken(u.getIDUsuario(), token);
    }

    public String obtenerCorreoPorToken(String token) {
        TokensDao tokensDao = new TokensDao();
        return tokensDao.obtenerCorreoPorToken(token);
    }

    // ── Actualizar contraseña ─────────────────────────────────────────────────

    public boolean actualizarClave(String correo, String nuevaClave) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE_CLAVE)) {
            ps.setString(1, Encriptador.encriptar(nuevaClave));
            ps.setString(2, correo);
            boolean ok = ps.executeUpdate() > 0;
            if (ok) {
                // Invalidar token usado en tabla Tokens
                new TokensDao().invalidarTokensPorCorreo(correo);
            }
            return ok;
        } catch (SQLException e) {
            System.err.println("Error al actualizar clave: " + e.getMessage());
        }
        return false;
    }

    public boolean actualizarClaveConHash(int idUsuario, String nuevaClave) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE_CLAVE_POR_ID)) {
            ps.setString(1, Encriptador.encriptar(nuevaClave));
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar clave por id: " + e.getMessage());
        }
        return false;
    }

    // ── Actualizar perfil ─────────────────────────────────────────────────────

    public boolean actualizarPerfil(int idUsuario, String correo, String telefono,
                                    String nombreUsuario, String rutaFoto) {
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
            System.err.println("Error al actualizar perfil: " + e.getMessage());
        }
        return false;
    }

    // ── Eliminar cuenta ───────────────────────────────────────────────────────

    public boolean eliminarUsuario(int idUsuario) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
        }
        return false;
    }

    // ── Mapeo interno ─────────────────────────────────────────────────────────

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setIDUsuario(rs.getInt("IDUsuario"));
        u.setDocumento(rs.getString("Documento"));
        u.setNombreUsuario(rs.getString("NombreUsuario"));
        u.setApellido(rs.getString("Apellido"));
        u.setCorreo(rs.getString("correo"));
        u.setTelefono(rs.getString("telefono"));
        u.setContrasenaUsuario(rs.getString("ContrasenaUsuario"));
        u.setFechaRegistro(rs.getObject("FechaRegistro", java.time.LocalDateTime.class));
        try { u.setFotoPerfil(rs.getString("fotoPerfil")); } catch (Exception ignore) {}
        return u;
    }
}
