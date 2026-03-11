package com.smarthome.smarthome_budget.dao;

import com.smarthome.smarthome_budget.modelo.InventarioCasa;
import com.smarthome.smarthome_budget.basedatos.claseConexion;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para Inventario_Casa.
 * CORRECCIÓN: la tabla usa StockActual y FechaActualizacion (no Cantidad/FechaRegistro).
 */
public class InventarioCasaDao {

    // ─── SQL ──────────────────────────────────────────────────────────────────

    private static final String SQL_SELECT_BASE =
        "SELECT ic.IDInventario, ic.IDProducto, ic.IDHogar, ic.StockActual, ic.FechaActualizacion, " +
        "  p.NombreProducto, p.Descripcion, p.StockMinimo, " +
        "  p.IDTipoProducto, tp.NombreTipoProducto " +
        "FROM Inventario_Casa ic " +
        "JOIN Producto p ON ic.IDProducto = p.IDProducto " +
        "JOIN Tipo_Producto tp ON p.IDTipoProducto = tp.IDTipoProducto ";

    private static final String SQL_INSERT =
        "INSERT INTO Inventario_Casa (IDProducto, IDHogar, StockActual, FechaActualizacion) VALUES (?,?,?,NOW())";

    private static final String SQL_UPDATE =
        "UPDATE Inventario_Casa SET StockActual=?, FechaActualizacion=NOW() WHERE IDInventario=? AND IDHogar=?";

    private static final String SQL_DELETE =
        "DELETE FROM Inventario_Casa WHERE IDInventario=? AND IDHogar=?";

    private static final String SQL_EXISTE =
        "SELECT IDInventario FROM Inventario_Casa WHERE IDProducto=? AND IDHogar=?";

    // ─── Operaciones ─────────────────────────────────────────────────────────

    /**
     * Registra un producto en el inventario.
     * Si ya existe, actualiza sumando la cantidad.
     * Retorna: 1=registrado nuevo, 2=cantidad actualizada, -1=error
     */
    public int registrar(InventarioCasa inv) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement psEx = con.prepareStatement(SQL_EXISTE)) {
            psEx.setInt(1, inv.getIdProducto());
            psEx.setInt(2, inv.getIdHogar());
            try (ResultSet rs = psEx.executeQuery()) {
                if (rs.next()) {
                    // Ya existe → sumar cantidad
                    int idInv = rs.getInt("IDInventario");
                    String sqlSum = "UPDATE Inventario_Casa SET StockActual = StockActual + ?, FechaActualizacion = NOW() WHERE IDInventario = ?";
                    try (PreparedStatement psUp = con.prepareStatement(sqlSum)) {
                        psUp.setInt(1, inv.getCantidad());
                        psUp.setInt(2, idInv);
                        psUp.executeUpdate();
                    }
                    return 2;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error verificando existencia: " + e.getMessage());
            return -1;
        }

        // No existe → insertar
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT)) {
            ps.setInt(1, inv.getIdProducto());
            ps.setInt(2, inv.getIdHogar());
            ps.setInt(3, inv.getCantidad());
            return ps.executeUpdate() > 0 ? 1 : -1;
        } catch (SQLException e) {
            System.err.println("Error registrando en inventario: " + e.getMessage());
        }
        return -1;
    }

    /** Lista todo el inventario de un hogar, ordenado por tipo y nombre. */
    public List<InventarioCasa> listarPorHogar(int idHogar) {
        return listar(idHogar, null, null);
    }

    /** Lista filtrando por tipo de producto. */
    public List<InventarioCasa> listarPorTipo(int idHogar, int idTipo) {
        return listar(idHogar, "tp.IDTipoProducto = ?", idTipo);
    }

    /** Lista solo productos con stock bajo o igual al mínimo (alertas). */
    public List<InventarioCasa> listarStockBajo(int idHogar) {
        return listar(idHogar, "ic.StockActual <= p.StockMinimo", null);
    }

    /** Lista solo productos agotados (StockActual == 0). */
    public List<InventarioCasa> listarAgotados(int idHogar) {
        return listar(idHogar, "ic.StockActual = 0", null);
    }

    /** Lista ordenando cantidad ascendente (menor a mayor). */
    public List<InventarioCasa> listarOrdenadoCantidadAsc(int idHogar) {
        List<InventarioCasa> lista = new ArrayList<>();
        String sql = SQL_SELECT_BASE + "WHERE ic.IDHogar = ? ORDER BY ic.StockActual ASC, p.NombreProducto ASC";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listando inventario: " + e.getMessage());
        }
        return lista;
    }

    /** Lista ordenando cantidad descendente (mayor a menor). */
    public List<InventarioCasa> listarOrdenadoCantidadDesc(int idHogar) {
        List<InventarioCasa> lista = new ArrayList<>();
        String sql = SQL_SELECT_BASE + "WHERE ic.IDHogar = ? ORDER BY ic.StockActual DESC, p.NombreProducto ASC";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listando inventario: " + e.getMessage());
        }
        return lista;
    }

    public InventarioCasa obtenerPorId(int idInventario, int idHogar) {
        String sql = SQL_SELECT_BASE + "WHERE ic.IDInventario = ? AND ic.IDHogar = ?";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idInventario);
            ps.setInt(2, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo inventario: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizar(InventarioCasa inv) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {
            ps.setInt(1, inv.getCantidad());
            ps.setInt(2, inv.getIdInventario());
            ps.setInt(3, inv.getIdHogar());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error actualizando inventario: " + e.getMessage());
        }
        return false;
    }

    public boolean eliminar(int idInventario, int idHogar) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, idInventario);
            ps.setInt(2, idHogar);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error eliminando de inventario: " + e.getMessage());
        }
        return false;
    }

    /**
     * Resumen rápido: [disponibles, porAgotar, categorias]
     * disponibles = StockActual > StockMinimo
     * porAgotar   = StockActual <= StockMinimo AND StockActual > 0
     */
    public int[] obtenerResumen(int idHogar) {
        int[] r = new int[3];
        String sql = "SELECT " +
            "SUM(CASE WHEN ic.StockActual > p.StockMinimo THEN 1 ELSE 0 END) AS disponibles, " +
            "SUM(CASE WHEN ic.StockActual <= p.StockMinimo AND ic.StockActual > 0 THEN 1 ELSE 0 END) AS porAgotar, " +
            "COUNT(DISTINCT p.IDTipoProducto) AS categorias " +
            "FROM Inventario_Casa ic JOIN Producto p ON ic.IDProducto = p.IDProducto WHERE ic.IDHogar = ?";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    r[0] = rs.getInt("disponibles");
                    r[1] = rs.getInt("porAgotar");
                    r[2] = rs.getInt("categorias");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo resumen: " + e.getMessage());
        }
        return r;
    }

    // ─── Privados ─────────────────────────────────────────────────────────────

    private List<InventarioCasa> listar(int idHogar, String whereExtra, Object param) {
        List<InventarioCasa> lista = new ArrayList<>();
        String sql = SQL_SELECT_BASE + "WHERE ic.IDHogar = ?" +
                     (whereExtra != null ? " AND " + whereExtra : "") +
                     " ORDER BY tp.NombreTipoProducto ASC, p.NombreProducto ASC";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idHogar);
            if (param != null) ps.setObject(2, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listando inventario: " + e.getMessage());
        }
        return lista;
    }

    private InventarioCasa mapear(ResultSet rs) throws SQLException {
        InventarioCasa inv = new InventarioCasa();
        inv.setIdInventario(rs.getInt("IDInventario"));
        inv.setIdProducto(rs.getInt("IDProducto"));
        inv.setIdHogar(rs.getInt("IDHogar"));
        inv.setCantidad(rs.getInt("StockActual"));                        // BD: StockActual
        inv.setFechaRegistro(rs.getObject("FechaActualizacion", LocalDateTime.class)); // BD: FechaActualizacion
        inv.setNombreProducto(rs.getString("NombreProducto"));
        inv.setDescripcion(rs.getString("Descripcion"));
        inv.setStockMinimo(rs.getInt("StockMinimo"));
        inv.setIdTipoProducto(rs.getInt("IDTipoProducto"));
        inv.setNombreTipoProducto(rs.getString("NombreTipoProducto"));
        return inv;
    }
}
