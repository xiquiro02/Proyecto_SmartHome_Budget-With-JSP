package com.smarthome.smarthome_budget.dao;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.smarthome.smarthome_budget.basedatos.claseConexion;
import com.smarthome.smarthome_budget.modelo.RegistroIngreso;

public class RegistroIngresoDao {

    // language=sql
    private static final String SQL_INSERT =
        "INSERT INTO Registro_Ingresos (IDHogar, Monto, FechaIngreso, Descripcion, IDCategoriaIngreso) " +
        "VALUES (?, ?, NOW(), ?, ?)";
    // language=sql — solo activos
    private static final String SQL_SELECT_ALL =
        "SELECT ri.*, ci.NombreCategoriaIngreso FROM Registro_Ingresos ri " +
        "JOIN Categorias_Ingresos ci ON ri.IDCategoriaIngreso = ci.IDCategoriaIngreso " +
        "WHERE ri.IDHogar = ? AND ri.EstadoIngreso = 'Activo' ORDER BY ri.FechaIngreso DESC";
    // language=sql — solo anulados
    private static final String SQL_SELECT_ANULADOS =
        "SELECT ri.*, ci.NombreCategoriaIngreso FROM Registro_Ingresos ri " +
        "JOIN Categorias_Ingresos ci ON ri.IDCategoriaIngreso = ci.IDCategoriaIngreso " +
        "WHERE ri.IDHogar = ? AND ri.EstadoIngreso = 'Anulado' ORDER BY ri.FechaIngreso DESC";
    // language=sql
    private static final String SQL_SELECT_BY_ID =
        "SELECT ri.*, ci.NombreCategoriaIngreso FROM Registro_Ingresos ri " +
        "JOIN Categorias_Ingresos ci ON ri.IDCategoriaIngreso = ci.IDCategoriaIngreso " +
        "WHERE ri.IDIngresos = ? AND ri.IDHogar = ?";
    // language=sql
    private static final String SQL_UPDATE =
        "UPDATE Registro_Ingresos SET Monto=?, Descripcion=?, IDCategoriaIngreso=? " +
        "WHERE IDIngresos=? AND IDHogar=?";
    // language=sql — método unificado para anular/reactivar
    private static final String SQL_CAMBIAR_ESTADO =
        "UPDATE Registro_Ingresos SET EstadoIngreso=? WHERE IDIngresos=? AND IDHogar=?";
    // language=sql — total mes actual (solo activos)
    private static final String SQL_TOTAL_MES =
        "SELECT COALESCE(SUM(Monto), 0) FROM Registro_Ingresos " +
        "WHERE IDHogar = ? AND EstadoIngreso = 'Activo' " +
        "AND MONTH(FechaIngreso) = MONTH(NOW()) AND YEAR(FechaIngreso) = YEAR(NOW())";

    // ── Insertar ──────────────────────────────────────────────────────────────

    public boolean registrar(RegistroIngreso ingreso) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT)) {
            ps.setInt(1, ingreso.getIdHogar());
            ps.setBigDecimal(2, ingreso.getMonto());
            ps.setString(3, ingreso.getDescripcion());
            ps.setInt(4, ingreso.getIdCategoriaIngreso());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[IngresoDao] Error al registrar: " + e.getMessage());
        }
        return false;
    }

    // ── Consultar ─────────────────────────────────────────────────────────────

    public RegistroIngreso obtenerPorId(int idIngreso, int idHogar) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, idIngreso);
            ps.setInt(2, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("[IngresoDao] Error al obtener por id: " + e.getMessage());
        }
        return null;
    }

    public List<RegistroIngreso> listarPorHogar(int idHogar) {
        List<RegistroIngreso> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL)) {
            ps.setInt(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[IngresoDao] Error al listar: " + e.getMessage());
        }
        return lista;
    }

    public List<RegistroIngreso> listarAnulados(int idHogar) {
        List<RegistroIngreso> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_ANULADOS)) {
            ps.setInt(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[IngresoDao] Error al listar anulados: " + e.getMessage());
        }
        return lista;
    }

    public BigDecimal totalMesActual(int idHogar) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_TOTAL_MES)) {
            ps.setInt(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getBigDecimal(1);
            }
        } catch (SQLException e) {
            System.err.println("[IngresoDao] Error al calcular total: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }

    // ── Actualizar ────────────────────────────────────────────────────────────

    public boolean actualizar(RegistroIngreso ingreso) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {
            ps.setBigDecimal(1, ingreso.getMonto());
            ps.setString(2, ingreso.getDescripcion());
            ps.setInt(3, ingreso.getIdCategoriaIngreso());
            ps.setInt(4, ingreso.getIdIngresos());
            ps.setInt(5, ingreso.getIdHogar());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[IngresoDao] Error al actualizar: " + e.getMessage());
        }
        return false;
    }

    // ── Anular / Reactivar ────────────────────────────────────────────────────

    /**
     * Método unificado para cambiar el estado de un ingreso.
     * nuevoEstado debe ser 'Activo' o 'Anulado'.
     * Retorna true si se actualizó al menos 1 fila.
     */
    public boolean cambiarEstado(int idIngreso, int idHogar, String nuevoEstado) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_CAMBIAR_ESTADO)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idIngreso);
            ps.setInt(3, idHogar);
            int filas = ps.executeUpdate();
            System.out.println("[IngresoDao] cambiarEstado → id=" + idIngreso
                    + " hogar=" + idHogar + " estado=" + nuevoEstado + " filasAfectadas=" + filas);
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("[IngresoDao] Error al cambiar estado: " + e.getMessage());
        }
        return false;
    }

    public boolean anular(int idIngreso, int idHogar) {
        return cambiarEstado(idIngreso, idHogar, "Anulado");
    }

    public boolean reactivar(int idIngreso, int idHogar) {
        return cambiarEstado(idIngreso, idHogar, "Activo");
    }

    // ── Categorías ────────────────────────────────────────────────────────────

    public List<Object[]> listarCategorias() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT IDCategoriaIngreso, NombreCategoriaIngreso " +
                     "FROM Categorias_Ingresos ORDER BY IDCategoriaIngreso";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                lista.add(new Object[]{rs.getInt(1), rs.getString(2)});
        } catch (SQLException e) {
            System.err.println("[IngresoDao] Error al listar categorías: " + e.getMessage());
        }
        return lista;
    }

    // ── Mapeo ─────────────────────────────────────────────────────────────────

    private RegistroIngreso mapear(ResultSet rs) throws SQLException {
        RegistroIngreso r = new RegistroIngreso();
        r.setIdIngresos(rs.getInt("IDIngresos"));
        r.setIdHogar(rs.getInt("IDHogar"));
        r.setMonto(rs.getBigDecimal("Monto"));
        r.setFechaIngreso(rs.getObject("FechaIngreso", java.time.LocalDateTime.class));
        r.setDescripcion(rs.getString("Descripcion"));
        r.setIdCategoriaIngreso(rs.getInt("IDCategoriaIngreso"));
        r.setNombreCategoria(rs.getString("NombreCategoriaIngreso"));
        r.setEstadoIngreso(rs.getString("EstadoIngreso"));
        return r;
    }
}
