package com.smarthome.smarthome_budget.dao;

import java.sql.*;
import com.smarthome.smarthome_budget.basedatos.claseConexion;
import com.smarthome.smarthome_budget.modelo.Hogar;

/* Clase: HogarDao
   Propósito: Gestionar las operaciones de acceso a datos de la tabla Hogar.
   Permite crear un nuevo hogar al momento del registro y consultar los datos
   de un hogar por su ID para usarlos en la sesión del usuario.
*/
public class HogarDao {

    // Consulta SQL para insertar un nuevo hogar con el nombre indicado
    // language=sql
    private static final String SQL_INSERT =
        "INSERT INTO Hogar (NombreHogar) VALUES (?)";

    // Consulta SQL para obtener todos los datos de un hogar por su ID
    // language=sql
    private static final String SQL_SELECT_BY_ID =
        "SELECT * FROM Hogar WHERE IDHogar = ?";

    /* Método: crearHogar
       Propósito: Insertar un nuevo hogar en la base de datos y retornar el ID generado.
       Se usa durante el proceso de registro del primer usuario (Administrador del hogar).
       @param nombreHogar → Texto con el nombre del hogar a crear
       @return int → ID autogenerado del nuevo hogar, o -1 si la inserción falla
    */
    public int crearHogar(String nombreHogar) {
        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nombreHogar);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                // Variable entera que almacena el ID autogenerado por la base de datos
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("[HogarDao] Error al crear hogar: " + e.getMessage());
        }
        return -1;
    }

    /* Método: obtenerHogarPorId
       Propósito: Consultar los datos de un hogar específico por su ID.
       Usado para cargar el nombre del hogar en la sesión del usuario tras el login.
       @param idHogar → Entero con el ID del hogar a buscar
       @return Hogar → El objeto Hogar con los datos encontrados, o null si no existe
    */
    public Hogar obtenerHogarPorId(int idHogar) {
        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_BY_ID)) {

            ps.setInt(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Objeto Hogar construido con los datos de la fila encontrada
                    Hogar hogar = new Hogar();
                    hogar.setIDHogar(rs.getInt("IDHogar"));
                    hogar.setNombreHogar(rs.getString("NombreHogar"));
                    return hogar;
                }
            }
        } catch (SQLException e) {
            System.err.println("[HogarDao] Error al obtener hogar: " + e.getMessage());
        }
        return null;
    }
}
