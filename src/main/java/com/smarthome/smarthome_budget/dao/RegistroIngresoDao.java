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
    // language=sql
    private static final String SQL_SELECT_ALL =
        "SELECT ri.*, ci.NombreCategoriaIngreso FROM Registro_Ingresos ri " +
        "JOIN Categorias_Ingresos ci ON ri.IDCategoriaIngreso = ci.IDCategoriaIngreso " +
        "WHERE ri.IDHogar = ? ORDER BY ri.FechaIngreso DESC";
    // language=sql
    private static final String SQL_TOTAL_MES =
        "SELECT COALESCE(SUM(Monto), 0) FROM Registro_Ingresos " +
        "WHERE IDHogar = ? AND MONTH(FechaIngreso) = MONTH(NOW()) AND YEAR(FechaIngreso) = YEAR(NOW())";

    public boolean registrar(RegistroIngreso ingreso) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT)) {
            ps.setInt(1, ingreso.getIdHogar());
            ps.setBigDecimal(2, ingreso.getMonto());
            ps.setString(3, ingreso.getDescripcion());
            ps.setInt(4, ingreso.getIdCategoriaIngreso());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar ingreso: " + e.getMessage());
        }
        return false;
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
            System.err.println("Error al listar ingresos: " + e.getMessage());
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
            System.err.println("Error al calcular total ingresos: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }

    public List<Object[]> listarCategorias() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT IDCategoriaIngreso, NombreCategoriaIngreso FROM Categorias_Ingresos ORDER BY IDCategoriaIngreso";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                lista.add(new Object[]{rs.getInt(1), rs.getString(2)});
        } catch (SQLException e) {
            System.err.println("Error al listar categorías ingreso: " + e.getMessage());
        }
        return lista;
    }

    private RegistroIngreso mapear(ResultSet rs) throws SQLException {
        RegistroIngreso r = new RegistroIngreso();
        r.setIdIngresos(rs.getInt("IDIngresos"));
        r.setIdHogar(rs.getInt("IDHogar"));
        r.setMonto(rs.getBigDecimal("Monto"));
        r.setFechaIngreso(rs.getObject("FechaIngreso", java.time.LocalDateTime.class));
        r.setDescripcion(rs.getString("Descripcion"));
        r.setIdCategoriaIngreso(rs.getInt("IDCategoriaIngreso"));
        r.setNombreCategoria(rs.getString("NombreCategoriaIngreso"));
        return r;
    }
}
