package com.smarthome.smarthome_budget.dao;

import com.smarthome.smarthome_budget.modelo.MetodoPago;
import com.smarthome.smarthome_budget.basedatos.claseConexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MetodoPagoDao {

    private static final int ID_METODO_PREDETERMINADO = 1; 
    // language=sql
    private static final String SQL_SELECT_ALL =
        "SELECT IDMetodoPago, NombreMetodoPago FROM Metodo_Pago ORDER BY NombreMetodoPago";
    // language=sql
    private static final String SQL_SELECT_BY_ID =
        "SELECT IDMetodoPago, NombreMetodoPago FROM Metodo_Pago WHERE IDMetodoPago=?";
    // language=sql
    private static final String SQL_INSERT =
        "INSERT INTO Metodo_Pago (NombreMetodoPago) VALUES (?)";
    // language=sql
    private static final String SQL_UPDATE =
        "UPDATE Metodo_Pago SET NombreMetodoPago=? WHERE IDMetodoPago=?";
    // language=sql
    private static final String SQL_DELETE =
        "DELETE FROM Metodo_Pago WHERE IDMetodoPago=?";
    // language=sql
    private static final String SQL_TIENE_EGRESOS =
        "SELECT COUNT(*) FROM Registro_Egresos WHERE IDMetodoPago=?";
    // language=sql
    private static final String SQL_EXISTE_NOMBRE =
        "SELECT IDMetodoPago FROM Metodo_Pago WHERE LOWER(NombreMetodoPago)=LOWER(?) AND IDMetodoPago != ?";

    public List<MetodoPago> listarMetodosPago() {
        List<MetodoPago> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                MetodoPago m = new MetodoPago();
                m.setIdMetodoPago(rs.getInt("IDMetodoPago"));
                m.setNombreMetodoPago(rs.getString("NombreMetodoPago"));
                lista.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public MetodoPago obtenerPorId(int id) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    MetodoPago m = new MetodoPago();
                    m.setIdMetodoPago(rs.getInt("IDMetodoPago"));
                    m.setNombreMetodoPago(rs.getString("NombreMetodoPago"));
                    return m;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo método pago: " + e.getMessage());
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
            System.err.println("Error creando método pago: " + e.getMessage());
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
            System.err.println("Error actualizando método pago: " + e.getMessage());
        }
        return false;
    }

    /*
     * Elimina un método de pago.
     * El método predeterminado (Efectivo) no puede eliminarse.
     * No se puede eliminar si tiene egresos asociados.
     */
    public boolean eliminar(int id) {
        if (id == ID_METODO_PREDETERMINADO) return false;
        if (tieneEgresos(id)) return false;
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error eliminando método pago: " + e.getMessage());
        }
        return false;
    }

    public boolean esPredeterminado(int id) {
        return id == ID_METODO_PREDETERMINADO;
    }

    public boolean tieneEgresos(int id) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_TIENE_EGRESOS)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error verificando egresos de método: " + e.getMessage());
        }
        return true;
    }

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
