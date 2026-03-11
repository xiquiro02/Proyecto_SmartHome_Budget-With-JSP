package com.smarthome.smarthome_budget.dao;

import java.sql.*;
import com.smarthome.smarthome_budget.basedatos.claseConexion;
import com.smarthome.smarthome_budget.modelo.Roles;

public class DetallesHogaresDao {
    // language=sql
    private static final String SQL_INSERT = "INSERT INTO DetallesHogares (IDUsuario, IDHogar, IDRol) VALUES (?, ?, ?)";
    // language=sql
    private static final String SQL_SELECT_ROL = "SELECT Roles.* FROM Roles INNER JOIN DetallesHogares ON Roles.IDRol = DetallesHogares.IDRol WHERE DetallesHogares.IDUsuario = ? AND DetallesHogares.IDHogar = ?";
    // language=sql
    private static final String SQL_SELECT_PRIMER_HOGAR = "SELECT IDHogar, IDRol FROM DetallesHogares WHERE IDUsuario = ? LIMIT 1";

    public boolean crearRelacion(int idUsuario, int idHogar, int idRol) {
        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(SQL_INSERT)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, idHogar);
            ps.setInt(3, idRol);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear relación en DetallesHogares: " + e.getMessage());
        }
        return false;
    }

    public Roles obtenerRolDeUsuarioEnHogar(int idUsuario, int idHogar) {
        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_ROL)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Roles rol = new Roles();
                    rol.setIDRol(rs.getInt("IDRol"));
                    rol.setNombreRol(rs.getString("NombreRol"));
                    rol.setDescripcion(rs.getString("Descripcion"));
                    return rol;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener rol de usuario en hogar: " + e.getMessage());
        }
        return null;
    }

    public int[] obtenerPrimerHogarDeUsuario(int idUsuario) {
        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_PRIMER_HOGAR)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new int[]{rs.getInt("IDHogar"), rs.getInt("IDRol")};
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener primer hogar de usuario: " + e.getMessage());
        }
        return null;
    }
}
