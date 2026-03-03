package com.smarthome.smarthome_budget.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.sql.*;
import com.smarthome.smarthome_budget.basedatos.claseConexion;
import com.smarthome.smarthome_budget.modelo.usuario;
import com.smarthome.smarthome_budget.utils.Encriptador;

public class usuarioDao {
    // language=sql
    private static final String SQL_INSERT = "INSERT INTO Usuario (NombreUsuario, PrimerApellido, SegundoApellido, correo, telefono, ContrasenaUsuario, FechaRegistro) VALUES (?, ?, ?, ?, ?, ?, NOW())";
    // language=sql
    private static final String SQL_INSRT_HOGAR = "INSERT INTO Hogar (NombreHogar) VALUES (?)";
    // language=sql
    private static final String SQL_INSERT_DETALLE_HOGAR = "INSERT INTO DetallesHogares (IDUsuario, IDHogar, IDRol) VALUES (?, ?, 1)";
    // Consulta para buscar y listar a todos los usuarios de la tabla usuario.
    // language=sql
    private static final String SQL_SELECT = "SELECT * FROM Usuario WHERE correo = ?";
    // language=sql
    private static final String SQL_SELECT_CORREO = "SELECT * FROM Usuario WHERE correo = ?";

    public boolean registrarUsuario(usuario user) {

        Connection conexion = null;
        PreparedStatement psUser = null;
        PreparedStatement psHogar = null;
        PreparedStatement psDetalle = null;
        ResultSet rs = null;

        try {
            conexion = claseConexion.MetodoConectar();
            conexion.setAutoCommit(false);
            psUser = conexion.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);

            psUser.setString(1, user.getNombreUsuario());
            psUser.setString(2, user.getPrimerApellido());
            psUser.setString(3, user.getSegundoApellido());
            psUser.setString(4, user.getCorreo());
            psUser.setString(5, user.getTelefono());
            String passworSeguro = Encriptador.encriptar(user.getContrasenaUsuario());
            psUser.setString(6, passworSeguro);
            psUser.executeUpdate();

            rs = psUser.getGeneratedKeys();
            int idUsuarioGenerado = 0;
            if (rs.next()) {
                idUsuarioGenerado = rs.getInt(1);
            }

            psHogar = conexion.prepareStatement(SQL_INSRT_HOGAR, Statement.RETURN_GENERATED_KEYS);
            psHogar.setString(1, "Hogar de " + user.getNombreUsuario());
            psHogar.executeUpdate();

            rs = psHogar.getGeneratedKeys();
            int idHogarGenerado = 0;
            if (rs.next()) {
                idHogarGenerado = rs.getInt(1);
            }

            psDetalle = conexion.prepareStatement(SQL_INSERT_DETALLE_HOGAR);
            psDetalle.setInt(1, idUsuarioGenerado);
            psDetalle.setInt(2, idHogarGenerado);
            psDetalle.executeUpdate();

            conexion.commit();
            return true;
        } catch (SQLException e) {
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.err.println("Error al registrar usuario: " + e.getMessage());
            return false;
        } finally {
            try {
                if (psUser != null) {
                    psUser.close();
                }
                if (psHogar != null) {
                    psHogar.close();
                }
                if (psDetalle != null) {
                    psDetalle.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean correoExiste(String correo) {
        try (Connection conexion = claseConexion.MetodoConectar();
                PreparedStatement pstmt = conexion.prepareStatement(SQL_SELECT_CORREO)) {

            pstmt.setString(1, correo);
            ResultSet rs = pstmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            System.err.println("Error al verificar correo: " + e.getMessage());
        }
        return false;
    }
    
    public boolean guardarToken(String correo, String token) {
        String verificar = "SELECT COUNT(*) FROM Usuario WHERE correo = ?";
        String actualizar = "UPDATE Usuario SET tokenRecuperacion = ?, tokenExpiracion = ? WHERE correo = ?";
        
        System.out.println("=== GUARDAR TOKEN DEBUG ===");
        System.out.println("Correo recibido: " + correo);
        System.out.println("Token generado: " + token);

        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement psVerificar = conexion.prepareStatement(verificar)) {

            psVerificar.setString(1, correo);
            ResultSet rs = psVerificar.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            System.out.println("Correos encontrados: " + count);
            
            if (count == 0) {
                System.out.println("ERROR: El correo no existe en la BD");
                return false;
            }

            try (PreparedStatement ps = conexion.prepareStatement(actualizar)) {
                ps.setString(1, token);
                ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now().plusMinutes(20)));
                ps.setString(3, correo);
                
                int filasAfectadas = ps.executeUpdate();
                System.out.println("Filas afectadas en UPDATE: " + filasAfectadas);
                
                if (filasAfectadas > 0) {
                    System.out.println("Token guardado exitosamente");
                } else {
                    System.out.println("ERROR: No se actualizó ninguna fila");
                }
            }
            return true;

        } catch (SQLException e) {
            System.err.println("Error al guardar token: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public String obtenerCorreoPorToken(String token) {
        String query = "SELECT correo FROM Usuario WHERE tokenRecuperacion = ? AND tokenExpiracion > NOW()";

        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(query)) {

            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("correo");

        } catch (SQLException e) {
            System.err.println("Error al obtener correo por token: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizarClave(String correo, String nuevaClave) {
        String query = "UPDATE Usuario SET ContrasenaUsuario = ?, tokenRecuperacion = NULL, tokenExpiracion = NULL WHERE correo = ?";

        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(query)) {

            ps.setString(1, Encriptador.encriptar(nuevaClave)); // encripta igual que en registro
            ps.setString(2, correo);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al actualizar clave: " + e.getMessage());
            return false;
        }
    } 
    
    public usuario login(String correo, String contrasenaUsuario) {
        usuario userEncontrado = null;

        try (Connection conexion = claseConexion.MetodoConectar();
                PreparedStatement pstmt = conexion.prepareStatement(SQL_SELECT)) {

            pstmt.setString(1, correo);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {

                String passwordBD = rs.getString("ContrasenaUsuario");

                if (Encriptador.verificar(contrasenaUsuario, passwordBD)) {
                    userEncontrado = new usuario();
                    userEncontrado.setIdUsuario(rs.getInt("IDUsuario"));
                    userEncontrado.setNombreUsuario(rs.getString("NombreUsuario"));
                    userEncontrado.setPrimerApellido(rs.getString("PrimerApellido"));
                    userEncontrado.setSegundoApellido(rs.getString("SegundoApellido"));
                    userEncontrado.setCorreo(rs.getString("correo"));
                    userEncontrado.setTelefono(rs.getString("telefono"));
                    userEncontrado.setFechaRegistro(rs.getTimestamp("FechaRegistro"));
                    return userEncontrado;
                }

            }
        } catch (SQLException e) {
            System.err.println("Error al iniciar sesión: " + e.getMessage());
        }
        return userEncontrado;
    }
}