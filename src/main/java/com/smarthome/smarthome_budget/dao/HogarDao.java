package com.smarthome.smarthome_budget.dao;

import java.sql.*;
import com.smarthome.smarthome_budget.basedatos.claseConexion;
import com.smarthome.smarthome_budget.modelo.Hogar;

public class HogarDao {
    // language=sql
    private static final String SQL_INSERT = "INSERT INTO Hogar (NombreHogar) VALUES (?)";
    // language=sql
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM Hogar WHERE IDHogar = ?";

    public int crearHogar(String nombreHogar) {
        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nombreHogar);
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al crear hogar: " + e.getMessage());
        }
        return -1;
    }

    public Hogar obtenerHogarPorId(int idHogar) {
        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_BY_ID)) {

            ps.setInt(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Hogar hogar = new Hogar();
                    hogar.setIDHogar(rs.getInt("IDHogar"));
                    hogar.setNombreHogar(rs.getString("NombreHogar"));
                    return hogar;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener hogar por ID: " + e.getMessage());
        }
        return null;
    }
}
