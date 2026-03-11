package com.smarthome.smarthome_budget.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Random;
import com.smarthome.smarthome_budget.basedatos.claseConexion;
import com.smarthome.smarthome_budget.modelo.CodigosInvitacion;

public class CodigosInvitacionDao {
    // language=sql
    private static final String SQL_INSERT = "INSERT INTO CodigosInvitacion (Codigo, IDHogar, IDRol, FechaCreacion, FechaExpiracion, Estado) VALUES (?, ?, ?, NOW(), ?, 'Activo')";
    // language=sql
    private static final String SQL_SELECT_VALIDAR = "SELECT * FROM CodigosInvitacion WHERE Codigo = ? AND Estado = 'Activo' AND FechaExpiracion > NOW()";
    // language=sql
    private static final String SQL_UPDATE_USADO = "UPDATE CodigosInvitacion SET Estado = 'Usado' WHERE Codigo = ?";

    public String generarCodigo(int idHogar, int idRol) {
        String codigo = generarCodigoAleatorio();
        
        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(SQL_INSERT)) {

            ps.setString(1, codigo);
            ps.setInt(2, idHogar);
            ps.setInt(3, idRol);
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now().plusDays(7)));
            
            if (ps.executeUpdate() > 0) {
                return codigo;
            }
        } catch (SQLException e) {
            System.err.println("Error al generar código de invitación: " + e.getMessage());
        }
        return null;
    }

    public CodigosInvitacion validarCodigo(String codigo) {
        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_VALIDAR)) {

            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CodigosInvitacion codigoInvitacion = new CodigosInvitacion();
                    codigoInvitacion.setIDCodigo(rs.getInt("IDCodigo"));
                    codigoInvitacion.setCodigo(rs.getString("Codigo"));
                    codigoInvitacion.setIDHogar(rs.getInt("IDHogar"));
                    codigoInvitacion.setIDRol(rs.getInt("IDRol"));
                    codigoInvitacion.setFechaCreacion(rs.getObject("FechaCreacion", LocalDateTime.class));
                    codigoInvitacion.setFechaExpiracion(rs.getObject("FechaExpiracion", LocalDateTime.class));
                    codigoInvitacion.setEstado(rs.getString("Estado"));
                    return codigoInvitacion;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al validar código de invitación: " + e.getMessage());
        }
        return null;
    }

    public boolean marcarComoUsado(String codigo) {
        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(SQL_UPDATE_USADO)) {

            ps.setString(1, codigo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al marcar código como usado: " + e.getMessage());
        }
        return false;
    }

    private String generarCodigoAleatorio() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder codigo = new StringBuilder();
        Random random = new Random();
        
        for (int i = 0; i < 8; i++) {
            codigo.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        
        return codigo.toString();
    }
}
