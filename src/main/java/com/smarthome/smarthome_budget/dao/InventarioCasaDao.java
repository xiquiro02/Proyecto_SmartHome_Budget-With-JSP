package com.smarthome.smarthome_budget.dao;

import com.smarthome.smarthome_budget.modelo.InventarioCasa;
import com.smarthome.smarthome_budget.basedatos.claseConexion;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InventarioCasaDao {

    // Umbral fijo de alerta de stock bajo.
    private static final String UMBRAL_STOCK_BAJO = "2.00";
    // language=sql
    private static final String SQL_SELECT_BASE =
        "SELECT ic.IDInventario, ic.IDProducto, ic.IDHogar, " +
        "ic.StockActual, ic.FechaActualizacion, " +
        "p.NombreProducto, p.Descripcion, " +
        "p.IDTipoProducto, tp.NombreTipoProducto " +
        "FROM Inventario_Casa ic " +
        "JOIN Producto p ON ic.IDProducto = p.IDProducto " +
        "JOIN Tipo_Producto tp ON p.IDTipoProducto = tp.IDTipoProducto ";
    // language=sql
    private static final String SQL_INSERT =
        "INSERT INTO Inventario_Casa (IDProducto, IDHogar, StockActual, FechaActualizacion) " +
        "VALUES (?,?,?,NOW())";
    // language=sql
    private static final String SQL_UPDATE =
        "UPDATE Inventario_Casa SET StockActual=?, FechaActualizacion=NOW() " +
        "WHERE IDInventario=? AND IDHogar=?";
    // language=sql
    private static final String SQL_DELETE =
        "DELETE FROM Inventario_Casa WHERE IDInventario=? AND IDHogar=?";
    // language=sql
    private static final String SQL_EXISTE =
        "SELECT IDInventario FROM Inventario_Casa WHERE IDProducto=? AND IDHogar=?";

    /*
     * Registra o suma stock de un producto en el inventario.
     * Retorna: 1=nuevo, 2=cantidad sumada, -1=error
     */
    public int registrar(InventarioCasa inv) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement psEx = con.prepareStatement(SQL_EXISTE)) {
            psEx.setInt(1, inv.getIdProducto());
            psEx.setInt(2, inv.getIdHogar());
            try (ResultSet rs = psEx.executeQuery()) {
                if (rs.next()) {
                    int idInv = rs.getInt("IDInventario");
                    String sqlSum =
                        "UPDATE Inventario_Casa SET StockActual = StockActual + ?, " +
                        "FechaActualizacion = NOW() WHERE IDInventario = ?";
                    try (PreparedStatement psUp = con.prepareStatement(sqlSum)) {
                            psUp.setBigDecimal(1, inv.getStockActual()); 
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

        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT)) {
            ps.setInt(1, inv.getIdProducto());
            ps.setInt(2, inv.getIdHogar());
            ps.setBigDecimal(3, inv.getStockActual());
            return ps.executeUpdate() > 0 ? 1 : -1;
        } catch (SQLException e) {
            System.err.println("Error registrando en inventario: " + e.getMessage());
        }
        return -1;
    }

    public List<InventarioCasa> listarPorHogar(int idHogar) {
        return listar(idHogar, null, null);
    }

    public List<InventarioCasa> listarPorTipo(int idHogar, int idTipo) {
        return listar(idHogar, "tp.IDTipoProducto = ?", idTipo);
    }

    /* Stock bajo: StockActual > 0 y <= umbral de alerta */
    public List<InventarioCasa> listarStockBajo(int idHogar) {
        return listar(idHogar,
            "ic.StockActual > 0 AND ic.StockActual <= " + UMBRAL_STOCK_BAJO, null);
    }

    public List<InventarioCasa> listarAgotados(int idHogar) {
        return listar(idHogar, "ic.StockActual = 0", null);
    }

    public List<InventarioCasa> listarOrdenadoCantidadAsc(int idHogar) {
        List<InventarioCasa> lista = new ArrayList<>();
        String sql = SQL_SELECT_BASE +
                     "WHERE ic.IDHogar = ? ORDER BY ic.StockActual ASC, p.NombreProducto ASC";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listando inventario asc: " + e.getMessage());
        }
        return lista;
    }

    public List<InventarioCasa> listarOrdenadoCantidadDesc(int idHogar) {
        List<InventarioCasa> lista = new ArrayList<>();
        String sql = SQL_SELECT_BASE +
                     "WHERE ic.IDHogar = ? ORDER BY ic.StockActual DESC, p.NombreProducto ASC";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listando inventario desc: " + e.getMessage());
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
            ps.setBigDecimal(1, inv.getStockActual());
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
            System.err.println("Error eliminando inventario: " + e.getMessage());
        }
        return false;
    }

    public int[] obtenerResumen(int idHogar) {
        int[] r = new int[3];
        String sql =
            "SELECT " +
            "SUM(CASE WHEN ic.StockActual > " + UMBRAL_STOCK_BAJO + " THEN 1 ELSE 0 END) AS disponibles, " +
            "SUM(CASE WHEN ic.StockActual <= " + UMBRAL_STOCK_BAJO + " AND ic.StockActual > 0 THEN 1 ELSE 0 END) AS porAgotar, " +
            "COUNT(DISTINCT p.IDTipoProducto) AS categorias " +
            "FROM Inventario_Casa ic " +
            "JOIN Producto p ON ic.IDProducto = p.IDProducto " +
            "WHERE ic.IDHogar = ?";
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
            System.err.println("Error resumen inventario: " + e.getMessage());
        }
        return r;
    }

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
        inv.setStockActual(rs.getBigDecimal("StockActual"));
        inv.setFechaActualizacion(rs.getObject("FechaActualizacion", LocalDateTime.class));
        inv.setNombreProducto(rs.getString("NombreProducto"));
        inv.setDescripcion(rs.getString("Descripcion"));
        inv.setIdTipoProducto(rs.getInt("IDTipoProducto"));
        inv.setNombreTipoProducto(rs.getString("NombreTipoProducto"));
        return inv;
    }
}
