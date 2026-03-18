package com.smarthome.smarthome_budget.dao;

import java.sql.*;
import java.time.LocalDateTime;
import com.smarthome.smarthome_budget.basedatos.claseConexion;

public class TokensDao {

    // language=sql
    private static final String SQL_INSERT =
        "INSERT INTO Tokens (tokenRecuperacion, FechaExpiracion, Usado, IDUsuario) " +
        "VALUES (?, ?, false, ?)";
    // language=sql
    private static final String SQL_SELECT_VALIDO =
        "SELECT t.tokenRecuperacion, u.correo FROM Tokens t " +
        "JOIN Usuario u ON t.IDUsuario = u.IDUsuario " +
        "WHERE t.tokenRecuperacion = ? AND t.Usado = false AND t.FechaExpiracion > NOW()";
    // language=sql
    private static final String SQL_MARCAR_USADO =
        "UPDATE Tokens SET Usado = true WHERE tokenRecuperacion = ?";
    // language=sql
    private static final String SQL_INVALIDAR_POR_CORREO =
        "UPDATE Tokens t JOIN Usuario u ON t.IDUsuario = u.IDUsuario " +
        "SET t.Usado = true WHERE u.correo = ? AND t.Usado = false";

    /* Crea un nuevo token de recuperación válido por 20 minutos.*/
    public boolean crearToken(int idUsuario, String token) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT)) {
            ps.setString(1, token);
            ps.setObject(2, LocalDateTime.now().plusMinutes(20));
            ps.setInt(3, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear token: " + e.getMessage());
        }
        return false;
    }

    public String obtenerCorreoPorToken(String token) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_VALIDO)) {
            ps.setString(1, token);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("correo");
            }
        } catch (SQLException e) {
            System.err.println("Error al validar token: " + e.getMessage());
        }
        return null;
    }

    /* Marca el token como usado. */
    public boolean marcarUsado(String token) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_MARCAR_USADO)) {
            ps.setString(1, token);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al marcar token como usado: " + e.getMessage());
        }
        return false;
    }

    /* Invalida todos los tokens activos de un usuario por su correo.*/
    public boolean invalidarTokensPorCorreo(String correo) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_INVALIDAR_POR_CORREO)) {
            ps.setString(1, correo);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al invalidar tokens: " + e.getMessage());
        }
        return false;
    }
}
