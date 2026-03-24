package com.smarthome.smarthome_budget.dao;

import com.smarthome.smarthome_budget.modelo.TipoProducto;
import com.smarthome.smarthome_budget.basedatos.claseConexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/* Clase: TipoProductoDao
   Propósito: Gestionar las operaciones de acceso a datos del catálogo de tipos de producto
   (tabla Tipo_Producto). Permite listar, crear, actualizar, eliminar y verificar duplicados
   o restricciones de uso. No se puede eliminar un tipo que tenga productos registrados.
*/
public class TipoProductoDao {

    // Consulta SQL que obtiene todos los tipos de producto ordenados alfabéticamente
    // language=sql
    private static final String SQL_SELECT_ALL =
        "SELECT IDTipoProducto, NombreTipoProducto FROM Tipo_Producto ORDER BY NombreTipoProducto";

    // Consulta SQL que obtiene un tipo de producto por su ID
    // language=sql
    private static final String SQL_SELECT_BY_ID =
        "SELECT IDTipoProducto, NombreTipoProducto FROM Tipo_Producto WHERE IDTipoProducto = ?";

    // Consulta SQL para insertar un nuevo tipo de producto
    // language=sql
    private static final String SQL_INSERT =
        "INSERT INTO Tipo_Producto (NombreTipoProducto) VALUES (?)";

    // Consulta SQL para actualizar el nombre de un tipo de producto existente
    // language=sql
    private static final String SQL_UPDATE =
        "UPDATE Tipo_Producto SET NombreTipoProducto = ? WHERE IDTipoProducto = ?";

    // Consulta SQL para eliminar un tipo de producto por su ID
    // language=sql
    private static final String SQL_DELETE =
        "DELETE FROM Tipo_Producto WHERE IDTipoProducto = ?";

    // Consulta SQL que cuenta los productos registrados bajo un tipo de producto específico
    // language=sql
    private static final String SQL_TIENE_PRODUCTOS =
        "SELECT COUNT(*) FROM Producto WHERE IDTipoProducto = ?";

    // Consulta SQL que busca otro tipo con el mismo nombre (insensible a mayúsculas), excluyendo el ID indicado
    // language=sql
    private static final String SQL_NOMBRE_DUPLICADO =
        "SELECT IDTipoProducto FROM Tipo_Producto " +
        "WHERE LOWER(NombreTipoProducto) = LOWER(?) AND IDTipoProducto != ?";

    /* Método: listarTipos
       Propósito: Obtener todos los tipos de producto registrados, ordenados alfabéticamente.
       @return List<TipoProducto> → Lista con todos los tipos de producto; vacía si no hay datos
    */
    public List<TipoProducto> listarTipos() {
        // Lista de objetos TipoProducto que se llenará con los resultados de la consulta
        List<TipoProducto> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // Objeto TipoProducto construido con los datos de cada fila del ResultSet
                TipoProducto t = new TipoProducto();
                t.setIdTipoProducto(rs.getInt("IDTipoProducto"));
                t.setNombreTipoProducto(rs.getString("NombreTipoProducto"));
                lista.add(t);
            }
        } catch (SQLException e) {
            System.err.println("[TipoProductoDao] Error al listar tipos: " + e.getMessage());
        }
        return lista;
    }

    /* Método: obtenerPorId
       Propósito: Buscar un tipo de producto específico por su ID.
       @param id → Entero con el ID del tipo de producto a buscar
       @return TipoProducto → El objeto encontrado, o null si no existe
    */
    public TipoProducto obtenerPorId(int id) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Objeto TipoProducto construido con los datos de la fila encontrada
                    TipoProducto t = new TipoProducto();
                    t.setIdTipoProducto(rs.getInt("IDTipoProducto"));
                    t.setNombreTipoProducto(rs.getString("NombreTipoProducto"));
                    return t;
                }
            }
        } catch (SQLException e) {
            System.err.println("[TipoProductoDao] Error obteniendo tipo: " + e.getMessage());
        }
        return null;
    }

    /* Método: crear
       Propósito: Insertar un nuevo tipo de producto en la base de datos.
       @param nombre → Texto con el nombre del nuevo tipo (se aplica trim)
       @return int → ID generado del nuevo tipo, o -1 si la inserción falla
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
            System.err.println("[TipoProductoDao] Error creando tipo: " + e.getMessage());
        }
        return -1;
    }

    /* Método: actualizar
       Propósito: Modificar el nombre de un tipo de producto existente.
       @param id          → Entero con el ID del tipo a actualizar
       @param nuevoNombre → Texto con el nuevo nombre para el tipo
       @return boolean → true si se actualizó al menos una fila, false si falló
    */
    public boolean actualizar(int id, String nuevoNombre) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, nuevoNombre.trim());
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[TipoProductoDao] Error actualizando tipo: " + e.getMessage());
        }
        return false;
    }

    /* Método: eliminar
       Propósito: Eliminar un tipo de producto de la base de datos. Retorna false si
       el tipo tiene productos registrados para evitar inconsistencias de datos.
       @param id → Entero con el ID del tipo a eliminar
       @return boolean → true si se eliminó correctamente, false si tiene productos o falló
    */
    public boolean eliminar(int id) {
        // Verifica que el tipo no tenga productos registrados antes de eliminar
        if (tieneProductos(id)) return false;
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[TipoProductoDao] Error eliminando tipo: " + e.getMessage());
        }
        return false;
    }

    /* Método: tieneProductos
       Propósito: Verificar si existen productos registrados bajo el tipo de producto indicado.
       Se usa como restricción antes de eliminar para mantener integridad referencial.
       @param id → Entero con el ID del tipo a verificar
       @return boolean → true si tiene productos asociados, false si no; true por seguridad ante error
    */
    public boolean tieneProductos(int id) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_TIENE_PRODUCTOS)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                // Variable entera que almacena la cantidad de productos del tipo indicado
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("[TipoProductoDao] Error verificando productos: " + e.getMessage());
        }
        // Por seguridad, si ocurre un error se asume que tiene registros para no eliminar
        return true;
    }

    /* Método: nombreDuplicado
       Propósito: Verificar si ya existe otro tipo de producto con el mismo nombre,
       excluyendo el ID indicado (útil para no marcar el elemento actual como duplicado al editar).
       @param nombre    → Texto con el nombre a verificar (insensible a mayúsculas)
       @param idExcluir → Entero con el ID a excluir de la búsqueda; usar 0 para crear
       @return boolean → true si existe un duplicado, false si el nombre está disponible
    */
    public boolean nombreDuplicado(String nombre, int idExcluir) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_NOMBRE_DUPLICADO)) {
            ps.setString(1, nombre.trim());
            ps.setInt(2, idExcluir);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) {
            System.err.println("[TipoProductoDao] Error verificando duplicado: " + e.getMessage());
        }
        return false;
    }
}
