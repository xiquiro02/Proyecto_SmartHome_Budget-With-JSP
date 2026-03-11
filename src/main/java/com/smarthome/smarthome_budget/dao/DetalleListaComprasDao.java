package com.smarthome.smarthome_budget.dao;

import com.smarthome.smarthome_budget.modelo.DetalleListaCompras;
import com.smarthome.smarthome_budget.basedatos.claseConexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para Detalle_Lista_Compras.
 * Controla RF14: no duplicados, solo enteros positivos > 0.
 * Controla RF18: marcar/desmarcar comprado (reversible).
 */
public class DetalleListaComprasDao {

    private static final String SQL_INSERT =
        "INSERT INTO Detalle_Lista_Compras (IDListaCompras, IDProducto, Cantidad, Comprado) VALUES (?,?,?,false)";

    private static final String SQL_SELECT_POR_LISTA =
        "SELECT d.*, p.NombreProducto, tp.NombreTipoProducto " +
        "FROM Detalle_Lista_Compras d " +
        "JOIN Producto p ON d.IDProducto = p.IDProducto " +
        "JOIN Tipo_Producto tp ON p.IDTipoProducto = tp.IDTipoProducto " +
        "WHERE d.IDListaCompras = ? ORDER BY d.Comprado ASC, p.NombreProducto ASC";

    private static final String SQL_SELECT_BY_ID =
        "SELECT d.*, p.NombreProducto, tp.NombreTipoProducto " +
        "FROM Detalle_Lista_Compras d " +
        "JOIN Producto p ON d.IDProducto = p.IDProducto " +
        "JOIN Tipo_Producto tp ON p.IDTipoProducto = tp.IDTipoProducto " +
        "WHERE d.IDDetalleLista = ?";

    private static final String SQL_EXISTE_PRODUCTO =
        "SELECT IDDetalleLista FROM Detalle_Lista_Compras WHERE IDListaCompras=? AND IDProducto=?";

    private static final String SQL_UPDATE_CANTIDAD =
        "UPDATE Detalle_Lista_Compras SET Cantidad=? WHERE IDDetalleLista=?";

    private static final String SQL_MARCAR_COMPRADO =
        "UPDATE Detalle_Lista_Compras SET Comprado=? WHERE IDDetalleLista=?";

    private static final String SQL_MARCAR_TODOS =
        "UPDATE Detalle_Lista_Compras SET Comprado=? WHERE IDListaCompras=?";

    private static final String SQL_DELETE =
        "DELETE FROM Detalle_Lista_Compras WHERE IDDetalleLista=?";

    // ─── Operaciones ─────────────────────────────────────────────────────────

    /**
     * RF14: Agrega un producto a la lista.
     * Si el producto ya existe, incrementa la cantidad en lugar de duplicar.
     * Retorna: 1=agregado, 2=cantidad incrementada, -1=error
     */
    public int agregarProducto(int idLista, int idProducto, int cantidad) {
        if (cantidad <= 0) return -1; // RF14: solo enteros positivos > 0

        // Verificar duplicado
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement psBuscar = con.prepareStatement(SQL_EXISTE_PRODUCTO)) {
            psBuscar.setInt(1, idLista);
            psBuscar.setInt(2, idProducto);
            try (ResultSet rs = psBuscar.executeQuery()) {
                if (rs.next()) {
                    // RF14: ya existe → actualizar cantidad sumando
                    int idDetalle = rs.getInt("IDDetalleLista");
                    String sqlSum = "UPDATE Detalle_Lista_Compras SET Cantidad = Cantidad + ? WHERE IDDetalleLista = ?";
                    try (PreparedStatement psUp = con.prepareStatement(sqlSum)) {
                        psUp.setInt(1, cantidad);
                        psUp.setInt(2, idDetalle);
                        psUp.executeUpdate();
                    }
                    return 2; // señal: era duplicado, se sumó
                }
            }
        } catch (SQLException e) {
            System.err.println("Error verificando duplicado: " + e.getMessage());
            return -1;
        }

        // No existe → insertar
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT)) {
            ps.setInt(1, idLista);
            ps.setInt(2, idProducto);
            ps.setInt(3, cantidad);
            return ps.executeUpdate() > 0 ? 1 : -1;
        } catch (SQLException e) {
            System.err.println("Error agregando producto a lista: " + e.getMessage());
        }
        return -1;
    }

    /** Lista todos los detalles de una lista ordenados: pendientes primero. */
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

    /** Actualiza la cantidad de un producto. RF14: solo enteros > 0. */
    public boolean actualizarCantidad(int idDetalle, int nuevaCantidad) {
        if (nuevaCantidad <= 0) return false;
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE_CANTIDAD)) {
            ps.setInt(1, nuevaCantidad);
            ps.setInt(2, idDetalle);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error actualizando cantidad: " + e.getMessage());
        }
        return false;
    }

    /** RF18: Marca/desmarca un producto como comprado (reversible). */
    public boolean toggleComprado(int idDetalle, boolean comprado) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_MARCAR_COMPRADO)) {
            ps.setBoolean(1, comprado);
            ps.setInt(2, idDetalle);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error toggling comprado: " + e.getMessage());
        }
        return false;
    }

    /** Marca/desmarca TODOS los productos de una lista. */
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

    /** Elimina un producto de la lista. */
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
        d.setCantidad(rs.getInt("Cantidad"));
        d.setComprado(rs.getBoolean("Comprado"));
        d.setNombreProducto(rs.getString("NombreProducto"));
        d.setTipoProducto(rs.getString("NombreTipoProducto"));
        return d;
    }
}
