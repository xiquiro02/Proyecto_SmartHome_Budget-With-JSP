package com.smarthome.smarthome_budget.dao;

import com.smarthome.smarthome_budget.modelo.CategoriaEgreso;
import com.smarthome.smarthome_budget.basedatos.claseConexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaEgresoDao {

    // language=sql
    private static final String SQL_SELECT_ALL =
        "SELECT IDCategoriaEgreso, NombreCategoriaEgreso FROM Categorias_Egresos ORDER BY NombreCategoriaEgreso";
    // language=sql
    private static final String SQL_SELECT_BY_ID =
        "SELECT IDCategoriaEgreso, NombreCategoriaEgreso FROM Categorias_Egresos WHERE IDCategoriaEgreso=?";
    // language=sql
    private static final String SQL_INSERT =
        "INSERT INTO Categorias_Egresos (NombreCategoriaEgreso) VALUES (?)";
    // language=sql
    private static final String SQL_UPDATE =
        "UPDATE Categorias_Egresos SET NombreCategoriaEgreso=? WHERE IDCategoriaEgreso=?";
    // language=sql
    private static final String SQL_DELETE =
        "DELETE FROM Categorias_Egresos WHERE IDCategoriaEgreso=?";
    // language=sql
    private static final String SQL_TIENE_EGRESOS =
        "SELECT COUNT(*) FROM Registro_Egresos WHERE IDCategoriaEgreso=?";
    // language=sql
    private static final String SQL_EXISTE_NOMBRE =
        "SELECT IDCategoriaEgreso FROM Categorias_Egresos WHERE LOWER(NombreCategoriaEgreso)=LOWER(?) AND IDCategoriaEgreso != ?";

    public List<CategoriaEgreso> listarCategorias() {
        List<CategoriaEgreso> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                CategoriaEgreso c = new CategoriaEgreso();
                c.setIdCategoriaEgreso(rs.getInt("IDCategoriaEgreso"));
                c.setNombreCategoriaEgreso(rs.getString("NombreCategoriaEgreso"));
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public CategoriaEgreso obtenerPorId(int id) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CategoriaEgreso c = new CategoriaEgreso();
                    c.setIdCategoriaEgreso(rs.getInt("IDCategoriaEgreso"));
                    c.setNombreCategoriaEgreso(rs.getString("NombreCategoriaEgreso"));
                    return c;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo categoría egreso: " + e.getMessage());
        }
        return null;
    }

    /** Retorna el ID generado o -1 si falla o nombre duplicado */
    public int crear(String nombre) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre.trim());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error creando categoría egreso: " + e.getMessage());
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
            System.err.println("Error actualizando categoría egreso: " + e.getMessage());
        }
        return false;
    }

    /* Elimina una categoría. retorna false si tiene egresos asociados. */
    public boolean eliminar(int id) {
        if (tieneEgresos(id)) return false;
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error eliminando categoría egreso: " + e.getMessage());
        }
        return false;
    }

    /* true si existen egresos registrados bajo esta categoría */
    public boolean tieneEgresos(int id) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_TIENE_EGRESOS)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error verificando egresos: " + e.getMessage());
        }
        return true; // por seguridad, asumir que tiene registros
    }

    /* true si ya existe otra categoría con ese nombre */
    public boolean nombreDuplicado(String nombre, int idExcluir) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_EXISTE_NOMBRE)) {
            ps.setString(1, nombre.trim());
            ps.setInt(2, idExcluir);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error verificando nombre duplicado: " + e.getMessage());
        }
        return false;
    }
}
