package com.smarthome.smarthome_budget.dao;

import java.math.BigDecimal;
import java.sql.*;
import com.smarthome.smarthome_budget.basedatos.claseConexion;
import com.smarthome.smarthome_budget.modelo.PresupuestoMensual;

public class PresupuestoMensualDao {

    /** Guarda o actualiza presupuesto del mes (UPSERT) */
    public boolean guardar(PresupuestoMensual p) {
        String sql = "INSERT INTO Presupuesto_Mensual (IDHogar, MontoMax, Mes, Anio, Alerta80, AlertaSuperePresupuesto, FechaCreacion) " +
                     "VALUES (?, ?, ?, ?, ?, ?, NOW()) " +
                     "ON DUPLICATE KEY UPDATE MontoMax=VALUES(MontoMax), Alerta80=VALUES(Alerta80), AlertaSuperePresupuesto=VALUES(AlertaSuperePresupuesto)";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getIdHogar());
            ps.setBigDecimal(2, p.getMontoMax());
            ps.setInt(3, p.getMes());
            ps.setInt(4, p.getAnio());
            ps.setBoolean(5, p.isAlerta80());
            ps.setBoolean(6, p.isAlertaSuperePresupuesto());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar presupuesto: " + e.getMessage());
            return false;
        }
    }

    /** Obtiene presupuesto del mes actual; null si no existe */
    public PresupuestoMensual obtenerMesActual(int idHogar) {
        String sql = "SELECT * FROM Presupuesto_Mensual WHERE IDHogar = ? AND Mes = MONTH(NOW()) AND Anio = YEAR(NOW())";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idHogar);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("Error al obtener presupuesto: " + e.getMessage());
        }
        return null;
    }

    /**
     * Total de egresos del mes actual para el hogar.
     * CORRECCIÓN: la tabla Registro_Egresos no tiene columna FechaEgreso.
     * Se usa FechaVencimiento para facturas pagadas del mes actual.
     */
    public BigDecimal totalEgresosMesActual(int idHogar) {
        // Suma los egresos registrados como 'Pagada' cuya FechaVencimiento es del mes actual
        // También suma los egresos del módulo Finanzas (EstadoPago='Pagada') del mes
        String sql = "SELECT COALESCE(SUM(Monto), 0) FROM Registro_Egresos " +
                     "WHERE IDHogar = ? AND EstadoPago = 'Pagada' " +
                     "AND MONTH(FechaVencimiento) = MONTH(NOW()) AND YEAR(FechaVencimiento) = YEAR(NOW())";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idHogar);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getBigDecimal(1);
        } catch (SQLException e) {
            System.err.println("Error al calcular egresos mes: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }

    private PresupuestoMensual mapear(ResultSet rs) throws SQLException {
        PresupuestoMensual p = new PresupuestoMensual();
        p.setIdPresupuesto(rs.getInt("IDPresupuesto"));
        p.setIdHogar(rs.getInt("IDHogar"));
        p.setMontoMax(rs.getBigDecimal("MontoMax"));
        p.setMes(rs.getInt("Mes"));
        p.setAnio(rs.getInt("Anio"));
        p.setAlerta80(rs.getBoolean("Alerta80"));
        p.setAlertaSuperePresupuesto(rs.getBoolean("AlertaSuperePresupuesto"));
        p.setFechaCreacion(rs.getObject("FechaCreacion", java.time.LocalDateTime.class));
        return p;
    }
}
