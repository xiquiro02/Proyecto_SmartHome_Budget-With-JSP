package com.smarthome.smarthome_budget.dao;

import com.smarthome.smarthome_budget.modelo.Producto;
import com.smarthome.smarthome_budget.modelo.TipoProducto;
import com.smarthome.smarthome_budget.basedatos.claseConexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDao {

    // language=sql
    private static final String SQL_BUSCAR_POR_NOMBRE =
        "SELECT IDProducto FROM Producto WHERE LOWER(NombreProducto) = LOWER(?) AND IDTipoProducto = ?";
    // language=sql
    private static final String SQL_BUSCAR_POR_NOMBRE_SOLO =
        "SELECT IDProducto FROM Producto WHERE LOWER(NombreProducto) = LOWER(?)";
    // language=sql
    private static final String SQL_INSERT =
        "INSERT INTO Producto (NombreProducto, IDTipoProducto) VALUES (?, ?)";
    // language=sql
    private static final String SQL_SELECT_ALL =
        "SELECT p.IDProducto, p.NombreProducto, p.Descripcion, p.IDTipoProducto, " +
        "tp.NombreTipoProducto " +
        "FROM Producto p " +
        "JOIN Tipo_Producto tp ON p.IDTipoProducto = tp.IDTipoProducto " +
        "ORDER BY p.NombreProducto";
    // language=sql
    private static final String SQL_SELECT_TIPOS =
        "SELECT IDTipoProducto, NombreTipoProducto FROM Tipo_Producto ORDER BY NombreTipoProducto";

    /*
     * Busca o crea un producto por nombre e IDTipoProducto.
     * Retorna el IDProducto (existente o recién creado), o -1 si falla.
     */
    public int obtenerOCrearProducto(String nombreProducto, int idTipoProducto) {
        // Buscar por nombre + tipo
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_NOMBRE)) {
            ps.setString(1, nombreProducto.trim());
            ps.setInt(2, idTipoProducto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error buscando producto por nombre+tipo: " + e.getMessage());
        }

        // Buscar solo por nombre (puede existir con otro tipo)
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_NOMBRE_SOLO)) {
            ps.setString(1, nombreProducto.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error buscando producto por nombre: " + e.getMessage());
        }

        // No existe → crear
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
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

    /* Lista todos los productos con su tipo (para selects en formularios) */
    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("IDProducto"));
                p.setNombreProducto(rs.getString("NombreProducto"));
                p.setDescripcion(rs.getString("Descripcion"));
                p.setIdTipoProducto(rs.getInt("IDTipoProducto"));
                p.setNombreTipoProducto(rs.getString("NombreTipoProducto"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error listando productos: " + e.getMessage());
        }
        return lista;
    }

    /*
     * Lista los tipos de producto como objetos TipoProducto.
     * Usado en formularios de registro de productos e inventario.
     */
    public List<TipoProducto> listarTiposProducto() {
        List<TipoProducto> tipos = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_TIPOS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TipoProducto t = new TipoProducto();
                t.setIdTipoProducto(rs.getInt("IDTipoProducto"));
                t.setNombreTipoProducto(rs.getString("NombreTipoProducto"));
                tipos.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Error listando tipos de producto: " + e.getMessage());
        }
        return tipos;
    }

    /*
     * Alias listarTipos() — mantiene compatibilidad con código existente
     * (InventarioServlet, ListasComprasServlet usan new ProductoDao().listarTipos())
     */
    public List<TipoProducto> listarTipos() {
        return listarTiposProducto();
    }
}
