package com.smarthome.smarthome_budget.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Random;
import com.smarthome.smarthome_budget.basedatos.claseConexion;
import com.smarthome.smarthome_budget.modelo.CodigosInvitacion;

/* Clase: CodigosInvitacionDao
   Propósito: Gestionar las operaciones de acceso a datos de los códigos de invitación
   (tabla CodigosInvitacion). Permite generar, validar y marcar como usados los códigos
   que sirven para que nuevos usuarios se unan a un hogar con un rol específico.
   Los códigos generados son alfanuméricos de 8 caracteres y expiran en 7 días.
*/
public class CodigosInvitacionDao {

    // Consulta SQL para insertar un nuevo código de invitación con estado 'Activo' y fecha de expiración a 7 días
    // language=sql
    private static final String SQL_INSERT =
        "INSERT INTO CodigosInvitacion (Codigo, IDHogar, IDRol, FechaCreacion, FechaExpiracion, Estado) " +
        "VALUES (?, ?, ?, NOW(), ?, 'Activo')";

    // Consulta SQL para validar un código: debe existir, estar 'Activo' y no haber expirado
    // language=sql
    private static final String SQL_SELECT_VALIDAR =
        "SELECT * FROM CodigosInvitacion WHERE Codigo = ? AND Estado = 'Activo' AND FechaExpiracion > NOW()";

    // Consulta SQL para marcar un código como 'Usado' una vez que fue reclamado
    // language=sql
    private static final String SQL_UPDATE_USADO =
        "UPDATE CodigosInvitacion SET Estado = 'Usado' WHERE Codigo = ?";

    /* Método: generarCodigo
       Propósito: Crear un código de invitación aleatorio de 8 caracteres alfanuméricos,
       persistirlo en la base de datos con una vigencia de 7 días y retornarlo.
       @param idHogar → Entero con el ID del hogar al que se invita
       @param idRol   → Entero con el ID del rol que tendrá el usuario invitado en ese hogar
       @return String → El código generado en texto, o null si la inserción falla
    */
    public String generarCodigo(int idHogar, int idRol) {
        // Variable de tipo texto que almacena el código alfanumérico generado aleatoriamente
        String codigo = generarCodigoAleatorio();

        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(SQL_INSERT)) {

            ps.setString(1, codigo);
            ps.setInt(2, idHogar);
            ps.setInt(3, idRol);
            // Variable de tipo Timestamp que almacena la fecha de expiración: 7 días desde ahora
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now().plusDays(7)));

            if (ps.executeUpdate() > 0) {
                return codigo;
            }
        } catch (SQLException e) {
            System.err.println("[CodigosInvitacionDao] Error al generar código: " + e.getMessage());
        }
        return null;
    }

    /* Método: validarCodigo
       Propósito: Verificar si un código de invitación es válido (activo y no expirado)
       y retornar sus datos completos para procesar el ingreso al hogar.
       @param codigo → Texto con el código de invitación a validar
       @return CodigosInvitacion → El objeto con los datos del código si es válido, o null si no existe o expiró
    */
    public CodigosInvitacion validarCodigo(String codigo) {
        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_VALIDAR)) {

            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Objeto CodigosInvitacion construido con todos los datos de la fila encontrada
                    CodigosInvitacion codigoInvitacion = new CodigosInvitacion();
                    codigoInvitacion.setIDCodigo(rs.getInt("IDCodigo"));
                    codigoInvitacion.setCodigo(rs.getString("Codigo"));
                    codigoInvitacion.setIDHogar(rs.getInt("IDHogar"));
                    codigoInvitacion.setIDRol(rs.getInt("IDRol"));
                    codigoInvitacion.setFechaCreacion(rs.getObject("FechaCreacion", LocalDateTime.class));
                    codigoInvitacion.setFechaExpiracion(rs.getObject("FechaExpiracion", LocalDateTime.class));
                    codigoInvitacion.setEstado(rs.getString("Estado"));
                    return codigoInvitacion;
                }
            }
        } catch (SQLException e) {
            System.err.println("[CodigosInvitacionDao] Error al validar código: " + e.getMessage());
        }
        return null;
    }

    /* Método: marcarComoUsado
       Propósito: Cambiar el estado de un código de invitación a 'Usado' para que
       no pueda ser reutilizado una vez que el usuario ya ingresó al hogar.
       @param codigo → Texto con el código a marcar como usado
       @return boolean → true si se actualizó correctamente, false si falló
    */
    public boolean marcarComoUsado(String codigo) {
        try (Connection conexion = claseConexion.MetodoConectar();
             PreparedStatement ps = conexion.prepareStatement(SQL_UPDATE_USADO)) {

            ps.setString(1, codigo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CodigosInvitacionDao] Error al marcar código como usado: " + e.getMessage());
        }
        return false;
    }

    /* Método: generarCodigoAleatorio
       Propósito: Generar una cadena aleatoria de 8 caracteres usando letras mayúsculas
       y dígitos (0-9), utilizada como código único de invitación.
       @return String → Texto de 8 caracteres alfanuméricos en mayúsculas
    */
    private String generarCodigoAleatorio() {
        // Cadena de texto que contiene todos los caracteres permitidos para el código
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        // Constructor de texto que acumula los caracteres seleccionados aleatoriamente
        StringBuilder codigo = new StringBuilder();
        // Generador de números aleatorios para seleccionar caracteres
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            // Variable entera con el índice aleatorio dentro del arreglo de caracteres
            codigo.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }

        return codigo.toString();
    }
}
