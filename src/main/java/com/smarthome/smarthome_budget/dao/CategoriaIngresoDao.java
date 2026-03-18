package com.smarthome.smarthome_budget.dao;

import com.smarthome.smarthome_budget.modelo.CategoriasIngresos;
import com.smarthome.smarthome_budget.basedatos.claseConexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaIngresoDao {
    
    // language=sql
    private static final String SQL_SELECT_ALL =
        "SELECT IDCategoriaIngreso, NombreCategoriaIngreso " +
        "FROM Categorias_Ingresos ORDER BY NombreCategoriaIngreso";
    // language=sql
    private static final String SQL_SELECT_BY_ID =
        "SELECT IDCategoriaIngreso, NombreCategoriaIngreso " +
        "FROM Categorias_Ingresos WHERE IDCategoriaIngreso = ?";
    // language=sql
    private static final String SQL_INSERT =
        "INSERT INTO Categorias_Ingresos (NombreCategoriaIngreso) VALUES (?)";
    // language=sql
    private static final String SQL_UPDATE =
        "UPDATE Categorias_Ingresos SET NombreCategoriaIngreso = ? WHERE IDCategoriaIngreso = ?";
    // language=sql
    private static final String SQL_DELETE =
        "DELETE FROM Categorias_Ingresos WHERE IDCategoriaIngreso = ?";
    // language=sql
    private static final String SQL_TIENE_INGRESOS =
        "SELECT COUNT(*) FROM Registro_Ingresos WHERE IDCategoriaIngreso = ?";
    // language=sql
    private static final String SQL_NOMBRE_DUPLICADO =
        "SELECT IDCategoriaIngreso FROM Categorias_Ingresos " +
        "WHERE LOWER(NombreCategoriaIngreso) = LOWER(?) AND IDCategoriaIngreso != ?";

    public List<CategoriasIngresos> listarCategorias() {
        List<CategoriasIngresos> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                CategoriasIngresos c = new CategoriasIngresos();
                c.setIdCategoriaIngreso(rs.getInt("IDCategoriaIngreso"));
                c.setNombreCategoriaIngreso(rs.getString("NombreCategoriaIngreso"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar categorías ingreso: " + e.getMessage());
        }
        return lista;
    }

    public CategoriasIngresos obtenerPorId(int id) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CategoriasIngresos c = new CategoriasIngresos();
                    c.setIdCategoriaIngreso(rs.getInt("IDCategoriaIngreso"));
                    c.setNombreCategoriaIngreso(rs.getString("NombreCategoriaIngreso"));
                    return c;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo categoría ingreso: " + e.getMessage());
        }
        return null;
    }

    /*Retorna el ID generado o -1 si falla */
    public int crear(String nombre) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre.trim());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error creando categoría ingreso: " + e.getMessage());
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
            System.err.println("Error actualizando categoría ingreso: " + e.getMessage());
        }
        return false;
    }

    /*retorna false si tiene ingresos asociados */
    public boolean eliminar(int id) {
        if (tieneIngresos(id)) return false;
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error eliminando categoría ingreso: " + e.getMessage());
        }
        return false;
    }

    public boolean tieneIngresos(int id) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_TIENE_INGRESOS)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error verificando ingresos: " + e.getMessage());
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
