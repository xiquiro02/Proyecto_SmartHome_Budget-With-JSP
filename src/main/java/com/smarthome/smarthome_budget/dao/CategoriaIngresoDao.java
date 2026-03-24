package com.smarthome.smarthome_budget.dao;

import com.smarthome.smarthome_budget.modelo.CategoriasIngresos;
import com.smarthome.smarthome_budget.basedatos.claseConexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/* Clase: CategoriaIngresoDao
   Propósito: Gestionar las operaciones de acceso a datos del catálogo de categorías
   de ingreso (tabla Categorias_Ingresos). Permite listar, crear, actualizar, eliminar
   y verificar duplicados o restricciones de uso antes de operar.
*/
public class CategoriaIngresoDao {

    // Consulta SQL que obtiene todas las categorías de ingreso ordenadas alfabéticamente
    // language=sql
    private static final String SQL_SELECT_ALL =
        "SELECT IDCategoriaIngreso, NombreCategoriaIngreso " +
        "FROM Categorias_Ingresos ORDER BY NombreCategoriaIngreso";

    // Consulta SQL que obtiene una categoría de ingreso por su ID
    // language=sql
    private static final String SQL_SELECT_BY_ID =
        "SELECT IDCategoriaIngreso, NombreCategoriaIngreso " +
        "FROM Categorias_Ingresos WHERE IDCategoriaIngreso = ?";

    // Consulta SQL para insertar una nueva categoría de ingreso
    // language=sql
    private static final String SQL_INSERT =
        "INSERT INTO Categorias_Ingresos (NombreCategoriaIngreso) VALUES (?)";

    // Consulta SQL para actualizar el nombre de una categoría de ingreso existente
    // language=sql
    private static final String SQL_UPDATE =
        "UPDATE Categorias_Ingresos SET NombreCategoriaIngreso = ? WHERE IDCategoriaIngreso = ?";

    // Consulta SQL para eliminar una categoría de ingreso por su ID
    // language=sql
    private static final String SQL_DELETE =
        "DELETE FROM Categorias_Ingresos WHERE IDCategoriaIngreso = ?";

    // Consulta SQL que cuenta los ingresos registrados bajo una categoría específica
    // language=sql
    private static final String SQL_TIENE_INGRESOS =
        "SELECT COUNT(*) FROM Registro_Ingresos WHERE IDCategoriaIngreso = ?";

    // Consulta SQL que busca otra categoría con el mismo nombre (insensible a mayúsculas), excluyendo el ID indicado
    // language=sql
    private static final String SQL_NOMBRE_DUPLICADO =
        "SELECT IDCategoriaIngreso FROM Categorias_Ingresos " +
        "WHERE LOWER(NombreCategoriaIngreso) = LOWER(?) AND IDCategoriaIngreso != ?";

    /* Método: listarCategorias
       Propósito: Obtener todas las categorías de ingreso registradas en la base de datos,
       ordenadas alfabéticamente por nombre.
       @return List<CategoriasIngresos> → Lista con todos los objetos; vacía si no hay datos
    */
    public List<CategoriasIngresos> listarCategorias() {
        // Lista de objetos CategoriasIngresos que se llenará con los resultados de la consulta
        List<CategoriasIngresos> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // Objeto CategoriasIngresos construido con los datos de cada fila del ResultSet
                CategoriasIngresos c = new CategoriasIngresos();
                c.setIdCategoriaIngreso(rs.getInt("IDCategoriaIngreso"));
                c.setNombreCategoriaIngreso(rs.getString("NombreCategoriaIngreso"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("[CategoriaIngresoDao] Error al listar categorías: " + e.getMessage());
        }
        return lista;
    }

    /* Método: obtenerPorId
       Propósito: Buscar una categoría de ingreso específica por su ID.
       @param id → Entero con el ID de la categoría a buscar
       @return CategoriasIngresos → El objeto encontrado, o null si no existe
    */
    public CategoriasIngresos obtenerPorId(int id) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Objeto CategoriasIngresos construido con los datos de la fila encontrada
                    CategoriasIngresos c = new CategoriasIngresos();
                    c.setIdCategoriaIngreso(rs.getInt("IDCategoriaIngreso"));
                    c.setNombreCategoriaIngreso(rs.getString("NombreCategoriaIngreso"));
                    return c;
                }
            }
        } catch (SQLException e) {
            System.err.println("[CategoriaIngresoDao] Error obteniendo categoría: " + e.getMessage());
        }
        return null;
    }

    /* Método: crear
       Propósito: Insertar una nueva categoría de ingreso en la base de datos.
       @param nombre → Texto con el nombre de la nueva categoría (se aplica trim)
       @return int → ID generado de la nueva categoría, o -1 si la inserción falla
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
            System.err.println("[CategoriaIngresoDao] Error creando categoría: " + e.getMessage());
        }
        return -1;
    }

    /* Método: actualizar
       Propósito: Modificar el nombre de una categoría de ingreso existente.
       @param id          → Entero con el ID de la categoría a actualizar
       @param nuevoNombre → Texto con el nuevo nombre para la categoría
       @return boolean → true si se actualizó al menos una fila, false si falló
    */
    public boolean actualizar(int id, String nuevoNombre) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, nuevoNombre.trim());
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CategoriaIngresoDao] Error actualizando categoría: " + e.getMessage());
        }
        return false;
    }

    /* Método: eliminar
       Propósito: Eliminar una categoría de ingreso de la base de datos. Retorna false
       si la categoría tiene ingresos asociados para evitar inconsistencias de datos.
       @param id → Entero con el ID de la categoría a eliminar
       @return boolean → true si se eliminó correctamente, false si tiene ingresos asociados o falló
    */
    public boolean eliminar(int id) {
        // Verifica que la categoría no tenga ingresos registrados antes de eliminar
        if (tieneIngresos(id)) return false;
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CategoriaIngresoDao] Error eliminando categoría: " + e.getMessage());
        }
        return false;
    }

    /* Método: tieneIngresos
       Propósito: Verificar si existen registros de ingreso asociados a la categoría indicada.
       Se usa como restricción antes de eliminar para mantener integridad referencial.
       @param id → Entero con el ID de la categoría a verificar
       @return boolean → true si tiene ingresos asociados, false si no; true por seguridad ante error
    */
    public boolean tieneIngresos(int id) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_TIENE_INGRESOS)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                // Variable entera que almacena la cantidad de ingresos asociados a la categoría
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("[CategoriaIngresoDao] Error verificando ingresos: " + e.getMessage());
        }
        // Por seguridad, si ocurre un error se asume que tiene registros para no eliminar
        return true;
    }

    /* Método: nombreDuplicado
       Propósito: Verificar si ya existe otra categoría de ingreso con el mismo nombre,
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
            System.err.println("[CategoriaIngresoDao] Error verificando duplicado: " + e.getMessage());
        }
        return false;
    }
}
