package com.smarthome.smarthome_budget.dao;

import java.math.BigDecimal;
import java.sql.*;
import com.smarthome.smarthome_budget.basedatos.claseConexion;
import com.smarthome.smarthome_budget.modelo.PresupuestoMensual;

public class PresupuestoMensualDao {

    // language=sql
    private static final String SQL_UPSERT =
        "INSERT INTO Presupuesto_Mensual (IDHogar, MontoMax, Mes, Anio, FechaCreacion) " +
        "VALUES (?, ?, ?, ?, NOW()) " +
        "ON DUPLICATE KEY UPDATE MontoMax = VALUES(MontoMax)";
    // language=sql
    private static final String SQL_SELECT_MES_ACTUAL =
        "SELECT * FROM Presupuesto_Mensual " +
        "WHERE IDHogar = ? AND Mes = MONTH(NOW()) AND Anio = YEAR(NOW())";
    // language=sql
    private static final String SQL_TOTAL_EGRESOS_MES =
        "SELECT COALESCE(SUM(Monto), 0) FROM Registro_Egresos " +
        "WHERE IDHogar = ? AND EstadoPago = 'Pagada' " +
        "AND MONTH(FechaVencimiento) = MONTH(NOW()) AND YEAR(FechaVencimiento) = YEAR(NOW())";

    /** Guarda o actualiza el presupuesto del mes (UPSERT por constraint unique IDHogar+Mes+Anio) */
    public boolean guardar(PresupuestoMensual p) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_UPSERT)) {
            ps.setInt(1, p.getIdHogar());
            ps.setBigDecimal(2, p.getMontoMax());
            ps.setInt(3, p.getMes());
            ps.setInt(4, p.getAnio());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar presupuesto: " + e.getMessage());
        }
        return false;
    }

    /** Obtiene el presupuesto del mes actual; null si no existe */
    public PresupuestoMensual obtenerMesActual(int idHogar) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_MES_ACTUAL)) {
            ps.setInt(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener presupuesto: " + e.getMessage());
        }
        return null;
    }

    /*
     * Total de egresos pagados del mes actual para el hogar.
     * Usa FechaVencimiento de facturas con EstadoPago='Pagada'.
     */
    public BigDecimal totalEgresosMesActual(int idHogar) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_TOTAL_EGRESOS_MES)) {
            ps.setInt(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getBigDecimal(1);
            }
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
        p.setFechaCreacion(rs.getObject("FechaCreacion", java.time.LocalDateTime.class));
        return p;
    }
}
