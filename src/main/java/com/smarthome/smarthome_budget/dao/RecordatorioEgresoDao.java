package com.smarthome.smarthome_budget.dao;

import com.smarthome.smarthome_budget.basedatos.claseConexion;
import java.sql.*;
import java.time.LocalDateTime;

/**
 * DAO para Recordatorios_Egresos.
 * Crea recordatorios automáticos al registrar una factura (2 días antes y 1 día antes).
 */
public class RecordatorioEgresoDao {

    public boolean crearRecordatoriosAutomaticos(int idEgreso, int idUsuario,
                                                  LocalDateTime fechaVencimiento,
                                                  String nombreFactura) {
        String sql = "INSERT INTO Recordatorios_Egresos (IDEgresos, IDUsuario, DescripcionAlerta, FechaRecordatorio) VALUES (?, ?, ?, ?)";
        boolean ok1 = false, ok2 = false;

        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Recordatorio 2 días antes
            ps.setInt(1, idEgreso);
            ps.setInt(2, idUsuario);
            ps.setString(3, "Recordatorio: la factura '" + nombreFactura + "' vence en 2 días.");
            ps.setObject(4, fechaVencimiento.minusDays(2));
            ok1 = ps.executeUpdate() > 0;

            // Recordatorio 1 día antes
            ps.setInt(1, idEgreso);
            ps.setInt(2, idUsuario);
            ps.setString(3, "Recordatorio: la factura '" + nombreFactura + "' vence mañana.");
            ps.setObject(4, fechaVencimiento.minusDays(1));
            ok2 = ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al crear recordatorios: " + e.getMessage());
        }
        return ok1 && ok2;
    }

    /** Elimina todos los recordatorios de un egreso al eliminarlo. */
    public void eliminarPorEgreso(int idEgreso) {
        String sql = "DELETE FROM Recordatorios_Egresos WHERE IDEgresos = ?";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEgreso);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar recordatorios del egreso: " + e.getMessage());
        }
    }
}
