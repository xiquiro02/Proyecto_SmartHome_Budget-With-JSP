package com.smarthome.smarthome_budget.dao;

import com.smarthome.smarthome_budget.modelo.MetodoPago;
import com.smarthome.smarthome_budget.basedatos.claseConexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/* Clase: MetodoPagoDao
   Propósito: Gestionar las operaciones de acceso a datos del catálogo de métodos de pago
   (tabla Metodo_Pago). Permite listar, crear, actualizar y eliminar métodos de pago,
   con la restricción de que el método predeterminado (Efectivo, ID=1) no puede eliminarse
   ni puede eliminarse un método que tenga egresos registrados asociados.
*/
public class MetodoPagoDao {

    // Constante entera que identifica el ID del método de pago predeterminado (Efectivo), protegido contra eliminación
    private static final int ID_METODO_PREDETERMINADO = 1;

    // Consulta SQL que obtiene todos los métodos de pago ordenados alfabéticamente
    // language=sql
    private static final String SQL_SELECT_ALL =
        "SELECT IDMetodoPago, NombreMetodoPago FROM Metodo_Pago ORDER BY NombreMetodoPago";

    // Consulta SQL que obtiene un método de pago por su ID
    // language=sql
    private static final String SQL_SELECT_BY_ID =
        "SELECT IDMetodoPago, NombreMetodoPago FROM Metodo_Pago WHERE IDMetodoPago=?";

    // Consulta SQL para insertar un nuevo método de pago
    // language=sql
    private static final String SQL_INSERT =
        "INSERT INTO Metodo_Pago (NombreMetodoPago) VALUES (?)";

    // Consulta SQL para actualizar el nombre de un método de pago existente
    // language=sql
    private static final String SQL_UPDATE =
        "UPDATE Metodo_Pago SET NombreMetodoPago=? WHERE IDMetodoPago=?";

    // Consulta SQL para eliminar un método de pago por su ID
    // language=sql
    private static final String SQL_DELETE =
        "DELETE FROM Metodo_Pago WHERE IDMetodoPago=?";

    // Consulta SQL que cuenta los egresos que usan un método de pago específico
    // language=sql
    private static final String SQL_TIENE_EGRESOS =
        "SELECT COUNT(*) FROM Registro_Egresos WHERE IDMetodoPago=?";

    // Consulta SQL que busca otro método con el mismo nombre (insensible a mayúsculas), excluyendo el ID indicado
    // language=sql
    private static final String SQL_EXISTE_NOMBRE =
        "SELECT IDMetodoPago FROM Metodo_Pago WHERE LOWER(NombreMetodoPago)=LOWER(?) AND IDMetodoPago != ?";

    /* Método: listarMetodosPago
       Propósito: Obtener todos los métodos de pago registrados, ordenados alfabéticamente.
       @return List<MetodoPago> → Lista con todos los métodos de pago; vacía si no hay datos
    */
    public List<MetodoPago> listarMetodosPago() {
        // Lista de objetos MetodoPago que se llenará con los resultados de la consulta
        List<MetodoPago> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // Objeto MetodoPago construido con los datos de cada fila del ResultSet
                MetodoPago m = new MetodoPago();
                m.setIdMetodoPago(rs.getInt("IDMetodoPago"));
                m.setNombreMetodoPago(rs.getString("NombreMetodoPago"));
                lista.add(m);
            }
        } catch (SQLException e) {
            System.err.println("[MetodoPagoDao] Error al listar métodos: " + e.getMessage());
        }
        return lista;
    }

    /* Método: obtenerPorId
       Propósito: Buscar un método de pago específico por su ID.
       @param id → Entero con el ID del método de pago a buscar
       @return MetodoPago → El objeto encontrado, o null si no existe
    */
    public MetodoPago obtenerPorId(int id) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Objeto MetodoPago construido con los datos de la fila encontrada
                    MetodoPago m = new MetodoPago();
                    m.setIdMetodoPago(rs.getInt("IDMetodoPago"));
                    m.setNombreMetodoPago(rs.getString("NombreMetodoPago"));
                    return m;
                }
            }
        } catch (SQLException e) {
            System.err.println("[MetodoPagoDao] Error obteniendo método: " + e.getMessage());
        }
        return null;
    }

    /* Método: crear
       Propósito: Insertar un nuevo método de pago en la base de datos.
       @param nombre → Texto con el nombre del nuevo método (se aplica trim)
       @return int → ID generado del nuevo método, o -1 si la inserción falla
    */
    public int crear(String nombre) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre.trim());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                // Variable entera que almacena el ID autogenerado por la base de datos
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[MetodoPagoDao] Error creando método: " + e.getMessage());
        }
        return -1;
    }

    /* Método: actualizar
       Propósito: Modificar el nombre de un método de pago existente.
       @param id          → Entero con el ID del método a actualizar
       @param nuevoNombre → Texto con el nuevo nombre para el método
       @return boolean → true si se actualizó al menos una fila, false si falló
    */
    public boolean actualizar(int id, String nuevoNombre) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, nuevoNombre.trim());
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[MetodoPagoDao] Error actualizando método: " + e.getMessage());
        }
        return false;
    }

    /* Método: eliminar
       Propósito: Eliminar un método de pago de la base de datos. No se puede eliminar
       el método predeterminado (Efectivo) ni uno que tenga egresos registrados asociados.
       @param id → Entero con el ID del método a eliminar
       @return boolean → true si se eliminó correctamente, false si es predeterminado, tiene egresos o falló
    */
    public boolean eliminar(int id) {
        // Verifica que no sea el método predeterminado (Efectivo)
        if (id == ID_METODO_PREDETERMINADO) return false;
        // Verifica que no tenga egresos asociados antes de eliminar
        if (tieneEgresos(id)) return false;
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[MetodoPagoDao] Error eliminando método: " + e.getMessage());
        }
        return false;
    }

    /* Método: esPredeterminado
       Propósito: Verificar si el ID indicado corresponde al método de pago predeterminado
       (Efectivo, ID=1) que no puede modificarse ni eliminarse.
       @param id → Entero con el ID del método a verificar
       @return boolean → true si es el método predeterminado, false si no lo es
    */
    public boolean esPredeterminado(int id) {
        return id == ID_METODO_PREDETERMINADO;
    }

    /* Método: tieneEgresos
       Propósito: Verificar si existen egresos registrados que usan el método de pago indicado.
       Se usa como restricción antes de eliminar para mantener integridad referencial.
       @param id → Entero con el ID del método a verificar
       @return boolean → true si tiene egresos asociados, false si no; true por seguridad ante error
    */
    public boolean tieneEgresos(int id) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_TIENE_EGRESOS)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                // Variable entera que almacena la cantidad de egresos asociados al método
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("[MetodoPagoDao] Error verificando egresos: " + e.getMessage());
        }
        // Por seguridad, si ocurre un error se asume que tiene registros para no eliminar
        return true;
    }

    /* Método: nombreDuplicado
       Propósito: Verificar si ya existe otro método de pago con el mismo nombre,
       excluyendo el ID indicado (útil para no marcar el elemento actual como duplicado al editar).
       @param nombre    → Texto con el nombre a verificar (insensible a mayúsculas)
       @param idExcluir → Entero con el ID a excluir de la búsqueda; usar 0 para crear
       @return boolean → true si existe un duplicado, false si el nombre está disponible
    */
    public boolean nombreDuplicado(String nombre, int idExcluir) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_EXISTE_NOMBRE)) {
            ps.setString(1, nombre.trim());
            ps.setInt(2, idExcluir);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("[MetodoPagoDao] Error verificando duplicado: " + e.getMessage());
        }
        return false;
    }
}
