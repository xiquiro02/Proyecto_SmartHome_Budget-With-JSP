package com.smarthome.smarthome_budget.dao;

import com.smarthome.smarthome_budget.modelo.DetalleListaCompras;
import com.smarthome.smarthome_budget.basedatos.claseConexion;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleListaComprasDao {

    private static final String SQL_INSERT =
        "INSERT INTO Detalle_ListaCompras (IDListaCompras, IDProducto, Cantidad, Comprado) VALUES (?,?,?,false)";
    private static final String SQL_SELECT_POR_LISTA =
        "SELECT d.*, p.NombreProducto, tp.NombreTipoProducto " +
        "FROM Detalle_ListaCompras d " +
        "JOIN Producto p ON d.IDProducto = p.IDProducto " +
        "JOIN Tipo_Producto tp ON p.IDTipoProducto = tp.IDTipoProducto " +
        "WHERE d.IDListaCompras = ? ORDER BY d.Comprado ASC, p.NombreProducto ASC";
    private static final String SQL_SELECT_BY_ID =
        "SELECT d.*, p.NombreProducto, tp.NombreTipoProducto " +
        "FROM Detalle_ListaCompras d " +
        "JOIN Producto p ON d.IDProducto = p.IDProducto " +
        "JOIN Tipo_Producto tp ON p.IDTipoProducto = tp.IDTipoProducto " +
        "WHERE d.IDDetalleLista = ?";
    private static final String SQL_EXISTE_PRODUCTO =
        "SELECT IDDetalleLista FROM Detalle_ListaCompras WHERE IDListaCompras=? AND IDProducto=?";
    private static final String SQL_UPDATE_CANTIDAD =
        "UPDATE Detalle_ListaCompras SET Cantidad=? WHERE IDDetalleLista=?";
    private static final String SQL_UPDATE_CANTIDAD_SUMA =
        "UPDATE Detalle_ListaCompras SET Cantidad = Cantidad + ? WHERE IDDetalleLista=?";
    private static final String SQL_MARCAR_COMPRADO =
        "UPDATE Detalle_ListaCompras SET Comprado=? WHERE IDDetalleLista=?";
    private static final String SQL_MARCAR_TODOS =
        "UPDATE Detalle_ListaCompras SET Comprado=? WHERE IDListaCompras=?";
    private static final String SQL_DELETE =
        "DELETE FROM Detalle_ListaCompras WHERE IDDetalleLista=?";

    /*
     * Agrega producto con cantidad DECIMAL.
     * Si ya existe, suma la cantidad. No duplica.
     * Retorna: 1=nuevo, 2=cantidad sumada, -1=error
     */
    public int agregarProducto(int idLista, int idProducto, BigDecimal cantidad) {
        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) return -1;

        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement psBuscar = con.prepareStatement(SQL_EXISTE_PRODUCTO)) {
            psBuscar.setInt(1, idLista);
            psBuscar.setInt(2, idProducto);
            try (ResultSet rs = psBuscar.executeQuery()) {
                if (rs.next()) {
                    int idDetalle = rs.getInt("IDDetalleLista");
                    try (PreparedStatement psUp = con.prepareStatement(SQL_UPDATE_CANTIDAD_SUMA)) {
                        psUp.setBigDecimal(1, cantidad);
                        psUp.setInt(2, idDetalle);
                        psUp.executeUpdate();
                    }
                    return 2;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error verificando duplicado: " + e.getMessage());
            return -1;
        }

        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT)) {
            ps.setInt(1, idLista);
            ps.setInt(2, idProducto);
            ps.setBigDecimal(3, cantidad);
            return ps.executeUpdate() > 0 ? 1 : -1;
        } catch (SQLException e) {
            System.err.println("Error agregando producto: " + e.getMessage());
        }
        return -1;
    }

    /* Sobrecarga int para compatibilidad con código existente */
    public int agregarProducto(int idLista, int idProducto, int cantidad) {
        return agregarProducto(idLista, idProducto, new BigDecimal(cantidad));
    }

    public List<DetalleListaCompras> listarPorLista(int idLista) {
        List<DetalleListaCompras> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_POR_LISTA)) {
            ps.setInt(1, idLista);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listando detalles: " + e.getMessage());
        }
        return lista;
    }

    public DetalleListaCompras obtenerPorId(int idDetalle) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, idDetalle);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo detalle: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizarCantidad(int idDetalle, BigDecimal nuevaCantidad) {
        if (nuevaCantidad == null || nuevaCantidad.compareTo(BigDecimal.ZERO) <= 0) return false;
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE_CANTIDAD)) {
            ps.setBigDecimal(1, nuevaCantidad);
            ps.setInt(2, idDetalle);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error actualizando cantidad: " + e.getMessage());
        }
        return false;
    }

    public boolean actualizarCantidad(int idDetalle, int nuevaCantidad) {
        return actualizarCantidad(idDetalle, new BigDecimal(nuevaCantidad));
    }

    public boolean toggleComprado(int idDetalle, boolean comprado) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_MARCAR_COMPRADO)) {
            ps.setBoolean(1, comprado);
            ps.setInt(2, idDetalle);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error en toggle comprado: " + e.getMessage());
        }
        return false;
    }

    public boolean marcarTodos(int idLista, boolean comprado) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_MARCAR_TODOS)) {
            ps.setBoolean(1, comprado);
            ps.setInt(2, idLista);
            return ps.executeUpdate() >= 0;
        } catch (SQLException e) {
            System.err.println("Error marcando todos: " + e.getMessage());
        }
        return false;
    }

    public boolean eliminar(int idDetalle) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, idDetalle);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error eliminando detalle: " + e.getMessage());
        }
        return false;
    }

    private DetalleListaCompras mapear(ResultSet rs) throws SQLException {
        DetalleListaCompras d = new DetalleListaCompras();
        d.setIdDetalleLista(rs.getInt("IDDetalleLista"));
        d.setIdListaCompras(rs.getInt("IDListaCompras"));
        d.setIdProducto(rs.getInt("IDProducto"));
        d.setCantidadDecimal(rs.getBigDecimal("Cantidad"));
        d.setComprado(rs.getBoolean("Comprado"));
        d.setNombreProducto(rs.getString("NombreProducto"));
        d.setNombreTipoProducto(rs.getString("NombreTipoProducto"));
        return d;
    }
}
