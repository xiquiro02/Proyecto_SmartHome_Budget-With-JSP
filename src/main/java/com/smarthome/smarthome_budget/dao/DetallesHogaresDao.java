package com.smarthome.smarthome_budget.dao;

import java.sql.*;
import com.smarthome.smarthome_budget.basedatos.claseConexion;
import com.smarthome.smarthome_budget.modelo.Roles;

/* Clase: DetallesHogaresDao
   Propósito: Gestionar las operaciones de acceso a datos de la tabla DetallesHogares,
   que representa la relación entre usuarios, hogares y roles. Permite crear la relación
   al registrar o invitar a un usuario, consultar el rol de un usuario dentro de un hogar
   y obtener el primer hogar al que pertenece un usuario (usado en el inicio de sesión).
*/
public class DetallesHogaresDao {

    // Consulta SQL para insertar la relación entre un usuario, un hogar y un rol
    // language=sql
    private static final String SQL_INSERT =
        "INSERT INTO DetallesHogares (IDUsuario, IDHogar, IDRol) VALUES (?, ?, ?)";

    // Consulta SQL que obtiene los datos del rol de un usuario dentro de un hogar específico mediante JOIN
    // language=sql
    private static final String SQL_SELECT_ROL =
        "SELECT Roles.* FROM Roles " +
        "INNER JOIN DetallesHogares ON Roles.IDRol = DetallesHogares.IDRol " +
        "WHERE DetallesHogares.IDUsuario = ? AND DetallesHogares.IDHogar = ?";

    // Consulta SQL que obtiene el primer hogar (y su rol) al que pertenece un usuario, limitado a 1 resultado
    // language=sql
    private static final String SQL_SELECT_PRIMER_HOGAR =
        "SELECT IDHogar, IDRol FROM DetallesHogares WHERE IDUsuario = ? LIMIT 1";

    /* Método: crearRelacion
       Propósito: Insertar el vínculo entre un usuario, un hogar y un rol en la base de datos.
       Se usa al registrar el primer administrador de un hogar o al aceptar una invitación.
       @param idUsuario → Entero con el ID del usuario a vincular
       @param idHogar   → Entero con el ID del hogar al que se vincula
       @param idRol     → Entero con el ID del rol asignado al usuario en ese hogar
       @return boolean → true si se creó la relación correctamente, false si falló
    */
    public boolean crearRelacion(int idUsuario, int idHogar, int idRol) {
        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(SQL_INSERT)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, idHogar);
            ps.setInt(3, idRol);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DetallesHogaresDao] Error al crear relación: " + e.getMessage());
        }
        return false;
    }

    /* Método: obtenerRolDeUsuarioEnHogar
       Propósito: Consultar el rol que tiene un usuario dentro de un hogar específico,
       retornando el objeto Roles con su ID, nombre y descripción.
       @param idUsuario → Entero con el ID del usuario a consultar
       @param idHogar   → Entero con el ID del hogar en el que se busca el rol
       @return Roles → El objeto con los datos del rol, o null si el usuario no pertenece al hogar
    */
    public Roles obtenerRolDeUsuarioEnHogar(int idUsuario, int idHogar) {
        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_ROL)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Objeto Roles construido con los datos del rol encontrado en la fila
                    Roles rol = new Roles();
                    rol.setIDRol(rs.getInt("IDRol"));
                    rol.setNombreRol(rs.getString("NombreRol"));
                    rol.setDescripcion(rs.getString("Descripcion"));
                    return rol;
                }
            }
        } catch (SQLException e) {
            System.err.println("[DetallesHogaresDao] Error al obtener rol: " + e.getMessage());
        }
        return null;
    }

    /* Método: obtenerPrimerHogarDeUsuario
       Propósito: Obtener el ID del primer hogar y el ID del rol del usuario en ese hogar,
       usado durante el inicio de sesión para cargar los datos de la sesión automáticamente.
       @param idUsuario → Entero con el ID del usuario a consultar
       @return int[] → Arreglo de dos enteros: [0]=IDHogar, [1]=IDRol; null si el usuario no tiene hogar
    */
    public int[] obtenerPrimerHogarDeUsuario(int idUsuario) {
        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_PRIMER_HOGAR)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Arreglo de dos enteros: posición 0 = IDHogar, posición 1 = IDRol
                    return new int[]{rs.getInt("IDHogar"), rs.getInt("IDRol")};
                }
            }
        } catch (SQLException e) {
            System.err.println("[DetallesHogaresDao] Error al obtener primer hogar: " + e.getMessage());
        }
        return null;
    }
}
