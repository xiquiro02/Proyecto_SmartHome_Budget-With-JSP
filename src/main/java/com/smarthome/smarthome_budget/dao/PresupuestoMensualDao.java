package com.smarthome.smarthome_budget.dao;

import java.math.BigDecimal;
import java.sql.*;
import com.smarthome.smarthome_budget.basedatos.claseConexion;
import com.smarthome.smarthome_budget.modelo.PresupuestoMensual;

/* Clase: PresupuestoMensualDao
   Propósito: Gestionar las operaciones de acceso a datos del presupuesto mensual por hogar
   (tabla Presupuesto_Mensual). Permite guardar o actualizar el monto máximo de un mes mediante
   UPSERT, obtener el presupuesto del mes en curso y calcular el total de egresos pagados en
   el mes actual para compararlo contra el límite establecido.
*/
public class PresupuestoMensualDao {

    // Consulta SQL que inserta o actualiza el presupuesto mensual del hogar (UPSERT por constraint único IDHogar+Mes+Anio)
    // language=sql
    private static final String SQL_UPSERT =
        "INSERT INTO Presupuesto_Mensual (IDHogar, MontoMax, Mes, Anio, FechaCreacion) " +
        "VALUES (?, ?, ?, ?, NOW()) " +
        "ON DUPLICATE KEY UPDATE MontoMax = VALUES(MontoMax)";

    // Consulta SQL que obtiene el presupuesto del mes y año actuales para el hogar indicado
    // language=sql
    private static final String SQL_SELECT_MES_ACTUAL =
        "SELECT * FROM Presupuesto_Mensual " +
        "WHERE IDHogar = ? AND Mes = MONTH(NOW()) AND Anio = YEAR(NOW())";

    // Consulta SQL que suma los egresos pagados activos del mes actual, usando la fecha de vencimiento como referencia
    // language=sql
    private static final String SQL_TOTAL_EGRESOS_MES =
        "SELECT COALESCE(SUM(Monto), 0) FROM Registro_Egresos " +
        "WHERE IDHogar = ? AND EstadoPago = 'Pagada' AND EstadoEgresos = 'Activo' " +
        "AND MONTH(FechaVencimiento) = MONTH(NOW()) AND YEAR(FechaVencimiento) = YEAR(NOW())";

    /* Método: guardar
       Propósito: Guardar o actualizar el monto máximo del presupuesto mensual del hogar.
       Si ya existe un presupuesto para el mismo mes y año del hogar, actualiza el monto;
       si no existe, inserta uno nuevo. Usa ON DUPLICATE KEY UPDATE para el UPSERT.
       @param p → Objeto PresupuestoMensual con idHogar, montoMax, mes y anio configurados
       @return boolean → true si el presupuesto fue guardado o actualizado, false si falló
    */
    public boolean guardar(PresupuestoMensual p) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_UPSERT)) {
            ps.setInt(1, p.getIdHogar());
            ps.setBigDecimal(2, p.getMontoMax());
            ps.setInt(3, p.getMes());
            ps.setInt(4, p.getAnio());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[PresupuestoMensualDao] Error al guardar presupuesto: " + e.getMessage());
        }
        return false;
    }

    /* Método: obtenerMesActual
       Propósito: Obtener el presupuesto mensual configurado para el mes y año en curso
       del hogar indicado. Retorna null si aún no se ha definido un presupuesto este mes.
       @param idHogar → Entero con el ID del hogar a consultar
       @return PresupuestoMensual → El presupuesto del mes actual, o null si no existe
    */
    public PresupuestoMensual obtenerMesActual(int idHogar) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_MES_ACTUAL)) {
            ps.setInt(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("[PresupuestoMensualDao] Error al obtener presupuesto: " + e.getMessage());
        }
        return null;
    }

    /* Método: totalEgresosMesActual
       Propósito: Calcular la suma de todos los egresos pagados y activos del mes en curso
       para el hogar indicado. Se usa para comparar el gasto real contra el presupuesto máximo.
       @param idHogar → Entero con el ID del hogar a calcular
       @return BigDecimal → Total de egresos pagados del mes actual; BigDecimal.ZERO si no hay datos o falla
    */
    public BigDecimal totalEgresosMesActual(int idHogar) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_TOTAL_EGRESOS_MES)) {
            ps.setInt(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                // Variable BigDecimal que almacena la suma de los egresos pagados del mes
                if (rs.next()) return rs.getBigDecimal(1);
            }
        } catch (SQLException e) {
            System.err.println("[PresupuestoMensualDao] Error al calcular egresos mes: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }

    /* Método: mapear
       Propósito: Construir un objeto PresupuestoMensual a partir de la fila actual del ResultSet.
       @param rs → ResultSet posicionado en la fila a mapear
       @return PresupuestoMensual → El objeto construido con los datos de la fila
    */
    private PresupuestoMensual mapear(ResultSet rs) throws SQLException {
        // Objeto PresupuestoMensual que recibirá los valores de cada columna de la fila
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
