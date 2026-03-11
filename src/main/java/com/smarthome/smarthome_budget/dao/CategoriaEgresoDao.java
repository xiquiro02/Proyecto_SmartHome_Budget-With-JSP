package com.smarthome.smarthome_budget.dao;

import com.smarthome.smarthome_budget.modelo.CategoriaEgreso;
import com.smarthome.smarthome_budget.basedatos.claseConexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaEgresoDao {
    
    public List<CategoriaEgreso> listarCategorias() {
        List<CategoriaEgreso> categorias = new ArrayList<>();
        // language=sql
        String sql = "SELECT * FROM Categorias_Egresos";
        
        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                CategoriaEgreso categoria = new CategoriaEgreso();
                categoria.setIdCategoriaEgreso(rs.getInt("IDCategoriaEgreso"));
                categoria.setNombreCategoriaEgreso(rs.getString("NombreCategoriaEgreso"));
                categorias.add(categoria);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return categorias;
    }
}
