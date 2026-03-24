package com.smarthome.smarthome_budget.dao;

import com.smarthome.smarthome_budget.modelo.CategoriaEgreso;
import com.smarthome.smarthome_budget.basedatos.claseConexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/* Clase: CategoriaEgresoDao
   Propósito: Gestionar las operaciones de acceso a datos del catálogo de categorías
   de egreso (tabla Categorias_Egresos). Permite listar, crear, actualizar, eliminar
   y verificar duplicados o restricciones de uso antes de operar.
*/
public class CategoriaEgresoDao {

    // Consulta SQL que obtiene todas las categorías de egreso ordenadas alfabéticamente
    // language=sql
    private static final String SQL_SELECT_ALL =
        "SELECT IDCategoriaEgreso, NombreCategoriaEgreso FROM Categorias_Egresos ORDER BY NombreCategoriaEgreso";

    // Consulta SQL que obtiene una categoría de egreso por su ID
    // language=sql
    private static final String SQL_SELECT_BY_ID =
        "SELECT IDCategoriaEgreso, NombreCategoriaEgreso FROM Categorias_Egresos WHERE IDCategoriaEgreso=?";

    // Consulta SQL para insertar una nueva categoría de egreso
    // language=sql
    private static final String SQL_INSERT =
        "INSERT INTO Categorias_Egresos (NombreCategoriaEgreso) VALUES (?)";

    // Consulta SQL para actualizar el nombre de una categoría de egreso existente
    // language=sql
    private static final String SQL_UPDATE =
        "UPDATE Categorias_Egresos SET NombreCategoriaEgreso=? WHERE IDCategoriaEgreso=?";

    // Consulta SQL para eliminar una categoría de egreso por su ID
    // language=sql
    private static final String SQL_DELETE =
        "DELETE FROM Categorias_Egresos WHERE IDCategoriaEgreso=?";

    // Consulta SQL que cuenta los egresos registrados bajo una categoría específica
    // language=sql
    private static final String SQL_TIENE_EGRESOS =
        "SELECT COUNT(*) FROM Registro_Egresos WHERE IDCategoriaEgreso=?";

    // Consulta SQL que busca otra categoría con el mismo nombre (insensible a mayúsculas), excluyendo el ID indicado
    // language=sql
    private static final String SQL_EXISTE_NOMBRE =
        "SELECT IDCategoriaEgreso FROM Categorias_Egresos WHERE LOWER(NombreCategoriaEgreso)=LOWER(?) AND IDCategoriaEgreso != ?";

    /* Método: listarCategorias
       Propósito: Obtener todas las categorías de egreso registradas en la base de datos,
       ordenadas alfabéticamente por nombre.
       @return List<CategoriaEgreso> → Lista con todos los objetos CategoriaEgreso; vacía si no hay datos
    */
    public List<CategoriaEgreso> listarCategorias() {
        // Lista de objetos CategoriaEgreso que se irá llenando con los resultados de la consulta
        List<CategoriaEgreso> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // Objeto CategoriaEgreso construido con los datos de cada fila del ResultSet
                CategoriaEgreso c = new CategoriaEgreso();
                c.setIdCategoriaEgreso(rs.getInt("IDCategoriaEgreso"));
                c.setNombreCategoriaEgreso(rs.getString("NombreCategoriaEgreso"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("[CategoriaEgresoDao] Error al listar categorías: " + e.getMessage());
        }
        return lista;
    }

    /* Método: obtenerPorId
       Propósito: Buscar una categoría de egreso específica por su ID.
       @param id → Entero con el ID de la categoría a buscar
       @return CategoriaEgreso → El objeto encontrado, o null si no existe
    */
    public CategoriaEgreso obtenerPorId(int id) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Objeto CategoriaEgreso construido con los datos de la fila encontrada
                    CategoriaEgreso c = new CategoriaEgreso();
                    c.setIdCategoriaEgreso(rs.getInt("IDCategoriaEgreso"));
                    c.setNombreCategoriaEgreso(rs.getString("NombreCategoriaEgreso"));
                    return c;
                }
            }
        } catch (SQLException e) {
            System.err.println("[CategoriaEgresoDao] Error obteniendo categoría: " + e.getMessage());
        }
        return null;
    }

    /* Método: crear
       Propósito: Insertar una nueva categoría de egreso en la base de datos.
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
            System.err.println("[CategoriaEgresoDao] Error creando categoría: " + e.getMessage());
        }
        return -1;
    }

    /* Método: actualizar
       Propósito: Modificar el nombre de una categoría de egreso existente.
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
            System.err.println("[CategoriaEgresoDao] Error actualizando categoría: " + e.getMessage());
        }
        return false;
    }

    /* Método: eliminar
       Propósito: Eliminar una categoría de egreso de la base de datos. Retorna false
       si la categoría tiene egresos asociados para evitar inconsistencias de datos.
       @param id → Entero con el ID de la categoría a eliminar
       @return boolean → true si se eliminó correctamente, false si tiene egresos asociados o falló
    */
    public boolean eliminar(int id) {
        // Verifica que la categoría no tenga egresos registrados antes de eliminar
        if (tieneEgresos(id)) return false;
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CategoriaEgresoDao] Error eliminando categoría: " + e.getMessage());
        }
        return false;
    }

    /* Método: tieneEgresos
       Propósito: Verificar si existen registros de egreso asociados a la categoría indicada.
       Se usa como restricción antes de eliminar para mantener integridad referencial.
       @param id → Entero con el ID de la categoría a verificar
       @return boolean → true si tiene egresos asociados, false si no; true por seguridad ante error
    */
    public boolean tieneEgresos(int id) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_TIENE_EGRESOS)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                // Variable entera que almacena la cantidad de egresos asociados a la categoría
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("[CategoriaEgresoDao] Error verificando egresos: " + e.getMessage());
        }
        // Por seguridad, si ocurre un error se asume que tiene registros para no eliminar
        return true;
    }

    /* Método: nombreDuplicado
       Propósito: Verificar si ya existe otra categoría de egreso con el mismo nombre,
       excluyendo el ID indicado (útil para no marcar el elemento actual como duplicado al editar).
       @param nombre     → Texto con el nombre a verificar (insensible a mayúsculas)
       @param idExcluir  → Entero con el ID a excluir de la búsqueda; usar 0 para crear
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
            System.err.println("[CategoriaEgresoDao] Error verificando duplicado: " + e.getMessage());
        }
        return false;
    }
}
