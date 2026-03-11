package com.smarthome.smarthome_budget.dao;

import java.sql.*;
import com.smarthome.smarthome_budget.basedatos.claseConexion;
import com.smarthome.smarthome_budget.modelo.Usuario;
import com.smarthome.smarthome_budget.utils.Encriptador;

public class UsuarioDao {
    // language=sql
    private static final String SQL_INSERT = "INSERT INTO Usuario (NombreUsuario, PrimerApellido, SegundoApellido, correo, telefono, ContrasenaUsuario, FechaRegistro) VALUES (?, ?, ?, ?, ?, ?, NOW())";
    // language=sql
    private static final String SQL_SELECT_CORREO = "SELECT * FROM Usuario WHERE correo = ?";
    // language=sql
    private static final String SQL_SELECT_LOGIN = "SELECT * FROM Usuario WHERE correo = ?";
    // language=sql
    private static final String SQL_UPDATE_TOKEN = "UPDATE Usuario SET tokenRecuperacion = ?, tokenExpiracion = ? WHERE correo = ?";
    // language=sql
    private static final String SQL_SELECT_TOKEN = "SELECT correo FROM Usuario WHERE tokenRecuperacion = ? AND tokenExpiracion > NOW()";
    // language=sql
    private static final String SQL_UPDATE_PASSWORD = "UPDATE Usuario SET ContrasenaUsuario = ?, tokenRecuperacion = NULL, tokenExpiracion = NULL WHERE correo = ?";

    public int registrarUsuario(Usuario usuario) {
        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, usuario.getNombreUsuario());
            ps.setString(2, usuario.getPrimerApellido());
            ps.setString(3, usuario.getSegundoApellido());
            ps.setString(4, usuario.getCorreo());
            ps.setString(5, usuario.getTelefono());
            ps.setString(6, Encriptador.encriptar(usuario.getContrasenaUsuario()));
            
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
        }
        return -1;
    }

    public boolean correoExiste(String correo) {
        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_CORREO)) {

            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar correo: " + e.getMessage());
        }
        return false;
    }

    public Usuario obtenerUsuarioPorCorreo(String correo) {
        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_CORREO)) {

            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setIDUsuario(rs.getInt("IDUsuario"));
                    usuario.setNombreUsuario(rs.getString("NombreUsuario"));
                    usuario.setPrimerApellido(rs.getString("PrimerApellido"));
                    usuario.setSegundoApellido(rs.getString("SegundoApellido"));
                    usuario.setCorreo(rs.getString("correo"));
                    usuario.setTelefono(rs.getString("telefono"));
                    usuario.setContrasenaUsuario(rs.getString("ContrasenaUsuario"));
                    usuario.setFechaRegistro(rs.getObject("FechaRegistro", java.time.LocalDateTime.class));
                    usuario.setTokenRecuperacion(rs.getString("tokenRecuperacion"));
                    usuario.setTokenExpiracion(rs.getObject("tokenExpiracion", java.time.LocalDateTime.class));
                    return usuario;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por correo: " + e.getMessage());
        }
        return null;
    }

    public Usuario login(String correo, String contrasena) {
        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_LOGIN)) {

            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String passwordBD = rs.getString("ContrasenaUsuario");
                    
                    if (Encriptador.verificar(contrasena, passwordBD)) {
                        Usuario usuario = new Usuario();
                        usuario.setIDUsuario(rs.getInt("IDUsuario"));
                        usuario.setNombreUsuario(rs.getString("NombreUsuario"));
                        usuario.setPrimerApellido(rs.getString("PrimerApellido"));
                        usuario.setSegundoApellido(rs.getString("SegundoApellido"));
                        usuario.setCorreo(rs.getString("correo"));
                        usuario.setTelefono(rs.getString("telefono"));
                        usuario.setFechaRegistro(rs.getObject("FechaRegistro", java.time.LocalDateTime.class));
                        return usuario;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al iniciar sesión: " + e.getMessage());
        }
        return null;
    }

    public boolean guardarToken(String correo, String token) {
        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(SQL_UPDATE_TOKEN)) {

            ps.setString(1, token);
            ps.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now().plusMinutes(20)));
            ps.setString(3, correo);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar token: " + e.getMessage());
        }
        return false;
    }

    public String obtenerCorreoPorToken(String token) {
        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_TOKEN)) {

            ps.setString(1, token);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("correo");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener correo por token: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizarClave(String correo, String nuevaClave) {
        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(SQL_UPDATE_PASSWORD)) {

            ps.setString(1, Encriptador.encriptar(nuevaClave));
            ps.setString(2, correo);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar clave: " + e.getMessage());
        }
        return false;
    }

    public boolean actualizarPerfil(int idUsuario, String correo, String telefono, String nombreUsuario, String rutaFoto) {
        StringBuilder sql = new StringBuilder("UPDATE Usuario SET correo=?, telefono=?, NombreUsuario=?");
        if (rutaFoto != null && !rutaFoto.isEmpty()) sql.append(", fotoPerfil=?");
        sql.append(" WHERE IDUsuario=?");
        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(sql.toString())) {
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

    public Usuario obtenerPorId(int idUsuario) {
        String sql = "SELECT * FROM Usuario WHERE IDUsuario = ?";
        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setIDUsuario(rs.getInt("IDUsuario"));
                    usuario.setNombreUsuario(rs.getString("NombreUsuario"));
                    usuario.setPrimerApellido(rs.getString("PrimerApellido"));
                    usuario.setSegundoApellido(rs.getString("SegundoApellido"));
                    usuario.setCorreo(rs.getString("correo"));
                    usuario.setTelefono(rs.getString("telefono"));
                    usuario.setContrasenaUsuario(rs.getString("ContrasenaUsuario"));
                    try { usuario.setFotoPerfil(rs.getString("fotoPerfil")); } catch (Exception ignore) {}
                    return usuario;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por id: " + e.getMessage());
        }
        return null;
    }

    public boolean eliminarUsuario(int idUsuario) {
        String sql = "DELETE FROM Usuario WHERE IDUsuario = ?";
        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
        }
        return false;
    }

    public boolean actualizarClaveConHash(int idUsuario, String nuevaClave) {
        String sql = "UPDATE Usuario SET ContrasenaUsuario = ?, tokenRecuperacion = NULL, tokenExpiracion = NULL WHERE IDUsuario = ?";
        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, Encriptador.encriptar(nuevaClave));
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar clave por id: " + e.getMessage());
        }
        return false;
    }
}
