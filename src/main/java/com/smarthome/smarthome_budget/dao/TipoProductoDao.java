package com.smarthome.smarthome_budget.dao;

import com.smarthome.smarthome_budget.modelo.TipoProducto;
import com.smarthome.smarthome_budget.basedatos.claseConexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoProductoDao {
    
    // language=sql
    private static final String SQL_SELECT_ALL =
        "SELECT IDTipoProducto, NombreTipoProducto FROM Tipo_Producto ORDER BY NombreTipoProducto";
    // language=sql
    private static final String SQL_SELECT_BY_ID =
        "SELECT IDTipoProducto, NombreTipoProducto FROM Tipo_Producto WHERE IDTipoProducto = ?";
    // language=sql
    private static final String SQL_INSERT =
        "INSERT INTO Tipo_Producto (NombreTipoProducto) VALUES (?)";
    // language=sql
    private static final String SQL_UPDATE =
        "UPDATE Tipo_Producto SET NombreTipoProducto = ? WHERE IDTipoProducto = ?";
    // language=sql
    private static final String SQL_DELETE =
        "DELETE FROM Tipo_Producto WHERE IDTipoProducto = ?";
    // language=sql
    private static final String SQL_TIENE_PRODUCTOS =
        "SELECT COUNT(*) FROM Producto WHERE IDTipoProducto = ?";
    // language=sql
    private static final String SQL_NOMBRE_DUPLICADO =
        "SELECT IDTipoProducto FROM Tipo_Producto " +
        "WHERE LOWER(NombreTipoProducto) = LOWER(?) AND IDTipoProducto != ?";

    public List<TipoProducto> listarTipos() {
        List<TipoProducto> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TipoProducto t = new TipoProducto();
                t.setIdTipoProducto(rs.getInt("IDTipoProducto"));
                t.setNombreTipoProducto(rs.getString("NombreTipoProducto"));
                lista.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar tipos de producto: " + e.getMessage());
        }
        return lista;
    }

    public TipoProducto obtenerPorId(int id) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TipoProducto t = new TipoProducto();
                    t.setIdTipoProducto(rs.getInt("IDTipoProducto"));
                    t.setNombreTipoProducto(rs.getString("NombreTipoProducto"));
                    return t;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo tipo de producto: " + e.getMessage());
        }
        return null;
    }

    public int crear(String nombre) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre.trim());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error creando tipo de producto: " + e.getMessage());
        }
        return -1;
    }

    public boolean actualizar(int id, String nuevoNombre) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, nuevoNombre.trim());
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error actualizando tipo de producto: " + e.getMessage());
        }
        return false;
    }

    /*no eliminar si tiene productos asociados */
    public boolean eliminar(int id) {
        if (tieneProductos(id)) return false;
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error eliminando tipo de producto: " + e.getMessage());
        }
        return false;
    }

    public boolean tieneProductos(int id) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_TIENE_PRODUCTOS)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error verificando productos: " + e.getMessage());
        }
        return true;
    }

    public boolean nombreDuplicado(String nombre, int idExcluir) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_NOMBRE_DUPLICADO)) {
            ps.setString(1, nombre.trim());
            ps.setInt(2, idExcluir);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) {
            System.err.println("Error verificando duplicado: " + e.getMessage());
        }
        return false;
    }
}
