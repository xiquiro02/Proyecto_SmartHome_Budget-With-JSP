package com.smarthome.smarthome_budget.dao;

import com.smarthome.smarthome_budget.modelo.Producto;
import com.smarthome.smarthome_budget.basedatos.claseConexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla Producto y Tipo_Producto.
 */
public class ProductoDao {

    /** Busca o crea un producto por nombre (case-insensitive). Retorna su ID. */
    public int obtenerOCrearProducto(String nombreProducto, int idTipoProducto) {
        // Primero buscar si ya existe
        String sqlBuscar = "SELECT IDProducto FROM Producto WHERE LOWER(NombreProducto) = LOWER(?)";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sqlBuscar)) {
            ps.setString(1, nombreProducto.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error buscando producto: " + e.getMessage());
        }
        // No existe → crear
        String sqlInsertar = "INSERT INTO Producto (NombreProducto, IDTipoProducto, StockInicial, StockMinimo) VALUES (?,?,0,0)";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sqlInsertar, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombreProducto.trim());
            ps.setInt(2, idTipoProducto);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error creando producto: " + e.getMessage());
        }
        return -1;
    }

    /** Lista todos los productos (para selects). */
    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.*, tp.NombreTipoProducto FROM Producto p " +
                     "JOIN Tipo_Producto tp ON p.IDTipoProducto = tp.IDTipoProducto ORDER BY p.NombreProducto";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("IDProducto"));
                p.setNombreProducto(rs.getString("NombreProducto"));
                p.setDescripcion(rs.getString("Descripcion"));
                p.setIdTipoProducto(rs.getInt("IDTipoProducto"));
                p.setNombreTipoProducto(rs.getString("NombreTipoProducto"));
                p.setStockInicial(rs.getInt("StockInicial"));
                p.setStockMinimo(rs.getInt("StockMinimo"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error listando productos: " + e.getMessage());
        }
        return lista;
    }

    /** Lista los tipos de producto disponibles. */
    public List<Producto> listarTipos() {
        List<Producto> tipos = new ArrayList<>();
        String sql = "SELECT IDTipoProducto, NombreTipoProducto FROM Tipo_Producto";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Producto p = new Producto();
                p.setIdTipoProducto(rs.getInt("IDTipoProducto"));
                p.setNombreTipoProducto(rs.getString("NombreTipoProducto"));
                tipos.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error listando tipos: " + e.getMessage());
        }
        return tipos;
    }
}
