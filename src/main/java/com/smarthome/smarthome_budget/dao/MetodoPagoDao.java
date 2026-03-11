package com.smarthome.smarthome_budget.dao;

import com.smarthome.smarthome_budget.modelo.MetodoPago;
import com.smarthome.smarthome_budget.basedatos.claseConexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MetodoPagoDao {
    
    public List<MetodoPago> listarMetodosPago() {
        List<MetodoPago> metodosPago = new ArrayList<>();
        // language=sql
        String sql = "SELECT * FROM Metodo_Pago";
        
        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                MetodoPago metodoPago = new MetodoPago();
                metodoPago.setIdMetodoPago(rs.getInt("IDMetodoPago"));
                metodoPago.setNombreMetodoPago(rs.getString("NombreMetodoPago"));
                metodosPago.add(metodoPago);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return metodosPago;
    }
}
