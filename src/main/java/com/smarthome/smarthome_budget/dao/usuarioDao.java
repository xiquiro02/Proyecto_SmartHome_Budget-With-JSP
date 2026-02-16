package com.smarthome.smarthome_budget.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.smarthome.smarthome_budget.basedatos.claseConexion;
import com.smarthome.smarthome_budget.modelo.usuario;

public class usuarioDao {
    // language=sql
    private static final String SQL_INSERT = "INSERT INTO Usuario (NombreUsuario, PrimerApellido, SegundoApellido, correo, telefono, ContrasenaUsuario, FechaRegistro) VALUES (?, ?, ?, ?, ?, ?, NOW())";
    // language=sql
    private static final String SQL_INSRT_HOGAR = "INSERT INTO Hogar (NombreHogar) VALUES (?)";
    // language=sql
    private static final String SQL_INSERT_DETALLE_HOGAR = "INSERT INTO DetallesHogares (IDUsuario, IDHogar, IDRol) VALUES (?, ?, 1)";
    // Consulta para buscar y listar a todos los usuarios de la tabla usuario.
    // language=sql
    private static final String SQL_SELECT = "SELECT * FROM Usuario WHERE correo = ? AND ContrasenaUsuario = ?";

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
            psUser.setString(6, user.getContrasenaUsuario());
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

    public usuario login(String correo, String contrasenaUsuario) {
        usuario userEncontrado = null;

        try (Connection conexion = claseConexion.MetodoConectar();
                PreparedStatement pstmt = conexion.prepareStatement(SQL_SELECT)) {

            pstmt.setString(1, correo);
            pstmt.setString(2, contrasenaUsuario);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                userEncontrado = new usuario();
                userEncontrado.setIdUsuario(rs.getInt("idUsuario"));
                userEncontrado.setNombreUsuario(rs.getString("nombreUsuario"));
                userEncontrado.setPrimerApellido(rs.getString("primerApellido"));
                userEncontrado.setSegundoApellido(rs.getString("segundoApellido"));
                userEncontrado.setCorreo(rs.getString("correo"));
                userEncontrado.setTelefono(rs.getString("telefono"));
                userEncontrado.setContrasenaUsuario(rs.getString("contrasenaUsuario"));
                userEncontrado.setFechaRegistro(rs.getTimestamp("fechaRegistro"));
                return userEncontrado;
            }
        } catch (SQLException e) {
            System.err.println("Error al iniciar sesión: " + e.getMessage());
        }
        return userEncontrado;
    }
}