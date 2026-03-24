package com.smarthome.smarthome_budget.dao;

import java.sql.*;
import java.time.LocalDateTime;
import com.smarthome.smarthome_budget.basedatos.claseConexion;

/* Clase: TokensDao
   Propósito: Gestionar las operaciones de acceso a datos de la tabla Tokens, usada para
   el flujo de recuperación de contraseña. Permite crear tokens con expiración automática,
   validar tokens activos, marcarlos como usados e invalidar todos los tokens de un usuario
   por correo. Un token solo es válido si no ha sido usado y no ha expirado.
*/
public class TokensDao {

    // Consulta SQL para insertar un nuevo token de recuperación con fecha de expiración y estado no usado
    // language=sql
    private static final String SQL_INSERT =
        "INSERT INTO Tokens (tokenRecuperacion, FechaExpiracion, Usado, IDUsuario) " +
        "VALUES (?, ?, false, ?)";

    // Consulta SQL que valida un token: busca por valor, verifica que no esté usado y que no haya expirado
    // language=sql
    private static final String SQL_SELECT_VALIDO =
        "SELECT t.tokenRecuperacion, u.correo FROM Tokens t " +
        "JOIN Usuario u ON t.IDUsuario = u.IDUsuario " +
        "WHERE t.tokenRecuperacion = ? AND t.Usado = false AND t.FechaExpiracion > NOW()";

    // Consulta SQL para marcar un token como usado, invalidándolo para usos futuros
    // language=sql
    private static final String SQL_MARCAR_USADO =
        "UPDATE Tokens SET Usado = true WHERE tokenRecuperacion = ?";

    // Consulta SQL para invalidar todos los tokens activos de un usuario identificado por su correo
    // language=sql
    private static final String SQL_INVALIDAR_POR_CORREO =
        "UPDATE Tokens t JOIN Usuario u ON t.IDUsuario = u.IDUsuario " +
        "SET t.Usado = true WHERE u.correo = ? AND t.Usado = false";

    /* Método: crearToken
       Propósito: Insertar un nuevo token de recuperación en la base de datos, válido
       por 20 minutos desde el momento de su creación.
       @param idUsuario → Entero con el ID del usuario al que pertenece el token
       @param token     → Texto con el valor único del token generado externamente
       @return boolean → true si el token fue creado correctamente, false si falló
    */
    public boolean crearToken(int idUsuario, String token) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT)) {
            ps.setString(1, token);
            // Fecha de expiración calculada como 20 minutos a partir del momento actual
            ps.setObject(2, LocalDateTime.now().plusMinutes(20));
            ps.setInt(3, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[TokensDao] Error al crear token: " + e.getMessage());
        }
        return false;
    }

    /* Método: obtenerCorreoPorToken
       Propósito: Validar un token de recuperación y obtener el correo del usuario asociado.
       El token debe existir, no haber sido usado y no haber expirado para ser válido.
       @param token → Texto con el valor del token a validar
       @return String → Correo del usuario si el token es válido, o null si es inválido o expirado
    */
    public String obtenerCorreoPorToken(String token) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_VALIDO)) {
            ps.setString(1, token);
            try (ResultSet rs = ps.executeQuery()) {
                // Texto con el correo del usuario asociado al token válido
                if (rs.next()) return rs.getString("correo");
            }
        } catch (SQLException e) {
            System.err.println("[TokensDao] Error al validar token: " + e.getMessage());
        }
        return null;
    }

    /* Método: marcarUsado
       Propósito: Marcar un token como usado una vez que el usuario completa el restablecimiento
       de contraseña, impidiendo que el mismo enlace sea reutilizado.
       @param token → Texto con el valor del token a invalidar
       @return boolean → true si se marcó correctamente, false si no existía o falló
    */
    public boolean marcarUsado(String token) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_MARCAR_USADO)) {
            ps.setString(1, token);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[TokensDao] Error al marcar token como usado: " + e.getMessage());
        }
        return false;
    }

    /* Método: invalidarTokensPorCorreo
       Propósito: Invalidar todos los tokens activos de un usuario identificado por su correo.
       Se usa al actualizar la contraseña para asegurar que los tokens de recuperación previos
       queden inutilizables aunque no hayan expirado aún.
       @param correo → Texto con el correo del usuario cuyos tokens se deben invalidar
       @return boolean → true si la operación se ejecutó correctamente, false si falló
    */
    public boolean invalidarTokensPorCorreo(String correo) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_INVALIDAR_POR_CORREO)) {
            ps.setString(1, correo);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("[TokensDao] Error al invalidar tokens: " + e.getMessage());
        }
        return false;
    }
}
