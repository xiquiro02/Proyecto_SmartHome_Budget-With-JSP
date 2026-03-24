package com.smarthome.smarthome_budget.dao;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.smarthome.smarthome_budget.basedatos.claseConexion;
import com.smarthome.smarthome_budget.modelo.RegistroIngreso;

/* Clase: RegistroIngresoDao
   Propósito: Gestionar las operaciones de acceso a datos de los ingresos del hogar
   (tabla Registro_Ingresos). Permite registrar, consultar, actualizar, anular y reactivar
   ingresos, así como calcular el total del mes en curso. Los ingresos anulados se excluyen
   de todas las consultas activas y se pueden consultar por separado. El cambio de estado
   entre 'Activo' y 'Anulado' se gestiona con un método unificado.
*/
public class RegistroIngresoDao {

    // Consulta SQL para insertar un nuevo ingreso con la fecha actual y los datos del hogar
    // language=sql
    private static final String SQL_INSERT =
        "INSERT INTO Registro_Ingresos (IDHogar, Monto, FechaIngreso, Descripcion, IDCategoriaIngreso) " +
        "VALUES (?, ?, NOW(), ?, ?)";

    // Consulta SQL que lista todos los ingresos activos del hogar con el nombre de su categoría, ordenados por fecha descendente
    // language=sql
    private static final String SQL_SELECT_ALL =
        "SELECT ri.*, ci.NombreCategoriaIngreso FROM Registro_Ingresos ri " +
        "JOIN Categorias_Ingresos ci ON ri.IDCategoriaIngreso = ci.IDCategoriaIngreso " +
        "WHERE ri.IDHogar = ? AND ri.EstadoIngreso = 'Activo' ORDER BY ri.FechaIngreso DESC";

    // Consulta SQL que lista solo los ingresos anulados del hogar con el nombre de su categoría
    // language=sql
    private static final String SQL_SELECT_ANULADOS =
        "SELECT ri.*, ci.NombreCategoriaIngreso FROM Registro_Ingresos ri " +
        "JOIN Categorias_Ingresos ci ON ri.IDCategoriaIngreso = ci.IDCategoriaIngreso " +
        "WHERE ri.IDHogar = ? AND ri.EstadoIngreso = 'Anulado' ORDER BY ri.FechaIngreso DESC";

    // Consulta SQL que obtiene un ingreso específico por su ID y hogar, con el nombre de su categoría
    // language=sql
    private static final String SQL_SELECT_BY_ID =
        "SELECT ri.*, ci.NombreCategoriaIngreso FROM Registro_Ingresos ri " +
        "JOIN Categorias_Ingresos ci ON ri.IDCategoriaIngreso = ci.IDCategoriaIngreso " +
        "WHERE ri.IDIngresos = ? AND ri.IDHogar = ?";

    // Consulta SQL para actualizar el monto, descripción y categoría de un ingreso existente
    // language=sql
    private static final String SQL_UPDATE =
        "UPDATE Registro_Ingresos SET Monto=?, Descripcion=?, IDCategoriaIngreso=? " +
        "WHERE IDIngresos=? AND IDHogar=?";

    // Consulta SQL que cambia el estado de un ingreso entre 'Activo' y 'Anulado'
    // language=sql
    private static final String SQL_CAMBIAR_ESTADO =
        "UPDATE Registro_Ingresos SET EstadoIngreso=? WHERE IDIngresos=? AND IDHogar=?";

    // Consulta SQL que suma todos los ingresos activos del mes y año actuales para el hogar
    // language=sql
    private static final String SQL_TOTAL_MES =
        "SELECT COALESCE(SUM(Monto), 0) FROM Registro_Ingresos " +
        "WHERE IDHogar = ? AND EstadoIngreso = 'Activo' " +
        "AND MONTH(FechaIngreso) = MONTH(NOW()) AND YEAR(FechaIngreso) = YEAR(NOW())";

    /* Método: registrar
       Propósito: Insertar un nuevo ingreso en la base de datos con la fecha actual del sistema.
       @param ingreso → Objeto RegistroIngreso con idHogar, monto, descripción e idCategoriaIngreso
       @return boolean → true si el ingreso fue registrado correctamente, false si falló
    */
    public boolean registrar(RegistroIngreso ingreso) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT)) {
            ps.setInt(1, ingreso.getIdHogar());
            ps.setBigDecimal(2, ingreso.getMonto());
            ps.setString(3, ingreso.getDescripcion());
            ps.setInt(4, ingreso.getIdCategoriaIngreso());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[RegistroIngresoDao] Error al registrar: " + e.getMessage());
        }
        return false;
    }

    /* Método: obtenerPorId
       Propósito: Buscar un ingreso específico por su ID, validando que pertenezca al hogar indicado.
       @param idIngreso → Entero con el ID del ingreso a buscar
       @param idHogar   → Entero con el ID del hogar para validar pertenencia
       @return RegistroIngreso → El objeto encontrado, o null si no existe o no pertenece al hogar
    */
    public RegistroIngreso obtenerPorId(int idIngreso, int idHogar) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, idIngreso);
            ps.setInt(2, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("[RegistroIngresoDao] Error al obtener por id: " + e.getMessage());
        }
        return null;
    }

    /* Método: listarPorHogar
       Propósito: Obtener todos los ingresos activos del hogar con el nombre de su categoría,
       ordenados de más reciente a más antiguo.
       @param idHogar → Entero con el ID del hogar a consultar
       @return List<RegistroIngreso> → Lista con todos los ingresos activos; vacía si no hay datos
    */
    public List<RegistroIngreso> listarPorHogar(int idHogar) {
        // Lista de objetos RegistroIngreso que se llenará con los resultados de la consulta
        List<RegistroIngreso> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL)) {
            ps.setInt(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[RegistroIngresoDao] Error al listar: " + e.getMessage());
        }
        return lista;
    }

    /* Método: listarAnulados
       Propósito: Obtener todos los ingresos anulados del hogar para mostrarlos en la
       vista de historial o papelera del módulo de ingresos.
       @param idHogar → Entero con el ID del hogar a consultar
       @return List<RegistroIngreso> → Lista con todos los ingresos anulados; vacía si no hay datos
    */
    public List<RegistroIngreso> listarAnulados(int idHogar) {
        // Lista de objetos RegistroIngreso que se llenará con los ingresos anulados del hogar
        List<RegistroIngreso> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_ANULADOS)) {
            ps.setInt(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[RegistroIngresoDao] Error al listar anulados: " + e.getMessage());
        }
        return lista;
    }

    /* Método: totalMesActual
       Propósito: Calcular la suma de todos los ingresos activos registrados durante
       el mes y año actuales para el hogar indicado.
       @param idHogar → Entero con el ID del hogar a calcular
       @return BigDecimal → Total de ingresos del mes actual; BigDecimal.ZERO si no hay datos o falla
    */
    public BigDecimal totalMesActual(int idHogar) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_TOTAL_MES)) {
            ps.setInt(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                // Variable BigDecimal que almacena la suma de los ingresos activos del mes
                if (rs.next()) return rs.getBigDecimal(1);
            }
        } catch (SQLException e) {
            System.err.println("[RegistroIngresoDao] Error al calcular total: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }

    /* Método: actualizar
       Propósito: Modificar el monto, descripción y categoría de un ingreso existente.
       @param ingreso → Objeto RegistroIngreso con idIngresos, idHogar, monto, descripción e idCategoriaIngreso
       @return boolean → true si se actualizó correctamente, false si no existe o falló
    */
    public boolean actualizar(RegistroIngreso ingreso) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {
            ps.setBigDecimal(1, ingreso.getMonto());
            ps.setString(2, ingreso.getDescripcion());
            ps.setInt(3, ingreso.getIdCategoriaIngreso());
            ps.setInt(4, ingreso.getIdIngresos());
            ps.setInt(5, ingreso.getIdHogar());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[RegistroIngresoDao] Error al actualizar: " + e.getMessage());
        }
        return false;
    }

    /* Método: cambiarEstado
       Propósito: Cambiar el estado de un ingreso entre 'Activo' y 'Anulado'. Método unificado
       utilizado por anular() y reactivar() para evitar duplicación de lógica.
       @param idIngreso   → Entero con el ID del ingreso a modificar
       @param idHogar     → Entero con el ID del hogar para validar pertenencia
       @param nuevoEstado → Texto con el nuevo estado a establecer ('Activo' o 'Anulado')
       @return boolean → true si el estado fue cambiado correctamente, false si no existe o falló
    */
    public boolean cambiarEstado(int idIngreso, int idHogar, String nuevoEstado) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_CAMBIAR_ESTADO)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idIngreso);
            ps.setInt(3, idHogar);
            // Variable entera que almacena la cantidad de filas afectadas por la actualización
            int filas = ps.executeUpdate();
            System.out.println("[RegistroIngresoDao] cambiarEstado → id=" + idIngreso
                    + " hogar=" + idHogar + " estado=" + nuevoEstado + " filasAfectadas=" + filas);
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("[RegistroIngresoDao] Error al cambiar estado: " + e.getMessage());
        }
        return false;
    }

    /* Método: anular
       Propósito: Cambiar el estado de un ingreso a 'Anulado', ocultándolo de las vistas activas.
       @param idIngreso → Entero con el ID del ingreso a anular
       @param idHogar   → Entero con el ID del hogar para validar pertenencia
       @return boolean → true si el ingreso fue anulado correctamente, false si falló
    */
    public boolean anular(int idIngreso, int idHogar) {
        return cambiarEstado(idIngreso, idHogar, "Anulado");
    }

    /* Método: reactivar
       Propósito: Cambiar el estado de un ingreso a 'Activo', restaurándolo en las vistas activas.
       @param idIngreso → Entero con el ID del ingreso a reactivar
       @param idHogar   → Entero con el ID del hogar para validar pertenencia
       @return boolean → true si el ingreso fue reactivado correctamente, false si falló
    */
    public boolean reactivar(int idIngreso, int idHogar) {
        return cambiarEstado(idIngreso, idHogar, "Activo");
    }

    /* Método: listarCategorias
       Propósito: Obtener todos los pares ID-nombre de las categorías de ingreso disponibles,
       para poblar los selectores en los formularios del módulo de ingresos.
       @return List<Object[]> → Lista de arreglos con [IDCategoriaIngreso, NombreCategoriaIngreso]
    */
    public List<Object[]> listarCategorias() {
        // Lista de arreglos que almacenará cada par de ID y nombre de categoría de ingreso
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT IDCategoriaIngreso, NombreCategoriaIngreso " +
                     "FROM Categorias_Ingresos ORDER BY IDCategoriaIngreso";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                lista.add(new Object[]{rs.getInt(1), rs.getString(2)});
        } catch (SQLException e) {
            System.err.println("[RegistroIngresoDao] Error al listar categorías: " + e.getMessage());
        }
        return lista;
    }

    /* Método: mapear
       Propósito: Construir un objeto RegistroIngreso a partir de la fila actual del ResultSet,
       incluyendo el nombre de la categoría obtenido mediante JOIN.
       @param rs → ResultSet posicionado en la fila a mapear
       @return RegistroIngreso → El objeto construido con los datos de la fila
    */
    private RegistroIngreso mapear(ResultSet rs) throws SQLException {
        // Objeto RegistroIngreso que recibirá los valores de cada columna de la fila
        RegistroIngreso r = new RegistroIngreso();
        r.setIdIngresos(rs.getInt("IDIngresos"));
        r.setIdHogar(rs.getInt("IDHogar"));
        r.setMonto(rs.getBigDecimal("Monto"));
        r.setFechaIngreso(rs.getObject("FechaIngreso", java.time.LocalDateTime.class));
        r.setDescripcion(rs.getString("Descripcion"));
        r.setIdCategoriaIngreso(rs.getInt("IDCategoriaIngreso"));
        r.setNombreCategoria(rs.getString("NombreCategoriaIngreso"));
        r.setEstadoIngreso(rs.getString("EstadoIngreso"));
        return r;
    }
}
