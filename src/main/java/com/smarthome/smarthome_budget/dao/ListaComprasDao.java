package com.smarthome.smarthome_budget.dao;

import com.smarthome.smarthome_budget.modelo.ListaCompras;
import com.smarthome.smarthome_budget.basedatos.claseConexion;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/* Clase: ListaComprasDao
   Propósito: Gestionar las operaciones de acceso a datos de las listas de compras
   (tabla Lista_Compras). Permite crear, listar, obtener, actualizar y eliminar listas,
   así como recalcular automáticamente su estado (Pendiente, En progreso, Completa)
   según el porcentaje de ítems comprados. Cada lista incluye contadores de ítems totales
   y comprados calculados mediante subconsultas.
*/
public class ListaComprasDao {

    // Consulta SQL para insertar una nueva lista de compras con estado inicial 'Pendiente'
    private static final String SQL_INSERT =
        "INSERT INTO Lista_Compras (IDHogar, NombreLista, FechaCreacion, EstadoLista) " +
        "VALUES (?, ?, NOW(), 'Pendiente')";

    // Consulta SQL que lista todas las listas del hogar con contadores de ítems totales y comprados
    private static final String SQL_SELECT_ALL =
        "SELECT lc.*, " +
        "  (SELECT COUNT(*) FROM Detalle_ListaCompras d WHERE d.IDListaCompras = lc.IDListaCompras) AS totalProductos, " +
        "  (SELECT COUNT(*) FROM Detalle_ListaCompras d WHERE d.IDListaCompras = lc.IDListaCompras AND d.Comprado = true) AS totalComprados " +
        "FROM Lista_Compras lc WHERE lc.IDHogar = ? ORDER BY lc.FechaCreacion DESC";

    // Consulta SQL que obtiene una lista específica por ID y hogar, con sus contadores
    private static final String SQL_SELECT_BY_ID =
        "SELECT lc.*, " +
        "  (SELECT COUNT(*) FROM Detalle_ListaCompras d WHERE d.IDListaCompras = lc.IDListaCompras) AS totalProductos, " +
        "  (SELECT COUNT(*) FROM Detalle_ListaCompras d WHERE d.IDListaCompras = lc.IDListaCompras AND d.Comprado = true) AS totalComprados " +
        "FROM Lista_Compras lc WHERE lc.IDListaCompras = ? AND lc.IDHogar = ?";

    // Consulta SQL para actualizar el nombre y estado de una lista de compras
    private static final String SQL_UPDATE =
        "UPDATE Lista_Compras SET NombreLista=?, EstadoLista=? WHERE IDListaCompras=? AND IDHogar=?";

    // Consulta SQL para eliminar una lista de compras por ID y hogar
    private static final String SQL_DELETE =
        "DELETE FROM Lista_Compras WHERE IDListaCompras=? AND IDHogar=?";

    // Consulta SQL que recalcula el estado de una lista: 'Completa' si todos están comprados,
    // 'En progreso' si al menos uno está comprado, 'Pendiente' si ninguno está comprado
    private static final String SQL_RECALCULAR_ESTADO =
        "UPDATE Lista_Compras SET EstadoLista = " +
        "  CASE " +
        "    WHEN (SELECT COUNT(*) FROM Detalle_ListaCompras d WHERE d.IDListaCompras = ? AND d.Comprado = false) = 0 " +
        "     AND (SELECT COUNT(*) FROM Detalle_ListaCompras d WHERE d.IDListaCompras = ?) > 0 " +
        "    THEN 'Completa' " +
        "    WHEN (SELECT COUNT(*) FROM Detalle_ListaCompras d WHERE d.IDListaCompras = ? AND d.Comprado = true) > 0 " +
        "    THEN 'En progreso' " +
        "    ELSE 'Pendiente' " +
        "  END " +
        "WHERE IDListaCompras = ?";

    // Consulta SQL que verifica si ya existe una lista con el mismo nombre en el hogar
    private static final String SQL_EXISTE_NOMBRE =
        "SELECT COUNT(*) FROM Lista_Compras WHERE IDHogar = ? AND TRIM(NombreLista) = TRIM(?)";

    // Consulta SQL que busca una lista activa (Pendiente o En progreso) por nombre exacto en el hogar
    private static final String SQL_BUSCAR_ACTIVA_POR_NOMBRE =
        "SELECT lc.*, " +
        "  (SELECT COUNT(*) FROM Detalle_ListaCompras d WHERE d.IDListaCompras = lc.IDListaCompras) AS totalProductos, " +
        "  (SELECT COUNT(*) FROM Detalle_ListaCompras d WHERE d.IDListaCompras = lc.IDListaCompras AND d.Comprado = true) AS totalComprados " +
        "FROM Lista_Compras lc " +
        "WHERE lc.IDHogar = ? AND TRIM(lc.NombreLista) = TRIM(?) " +
        "  AND lc.EstadoLista IN ('Pendiente','En progreso') LIMIT 1";

    /* Método: existeNombre
       Propósito: Verificar si ya existe una lista con el mismo nombre en el hogar,
       ignorando espacios al inicio y al final del nombre.
       @param idHogar     → Entero con el ID del hogar a verificar
       @param nombreLista → Texto con el nombre de la lista a verificar
       @return boolean → true si ya existe una lista con ese nombre, false si está disponible
    */
    public boolean existeNombre(int idHogar, String nombreLista) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_EXISTE_NOMBRE)) {
            ps.setInt(1, idHogar);
            ps.setString(2, nombreLista.trim());
            try (ResultSet rs = ps.executeQuery()) {
                // Variable entera que almacena la cantidad de listas con ese nombre en el hogar
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("[ListaComprasDao] Error verificando nombre: " + e.getMessage());
        }
        return false;
    }

    /* Método: insertar
       Propósito: Crear una nueva lista de compras en la base de datos con estado inicial 'Pendiente'.
       @param lista → Objeto ListaCompras con idHogar y nombreLista configurados
       @return int → ID autogenerado de la nueva lista, o -1 si la inserción falla
    */
    public int insertar(ListaCompras lista) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, lista.getIdHogar());
            ps.setString(2, lista.getNombreLista().trim());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                // Variable entera que almacena el ID autogenerado por la base de datos
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[ListaComprasDao] Error insertando lista: " + e.getMessage());
        }
        return -1;
    }

    /* Método: listarPorHogar
       Propósito: Obtener todas las listas de compras de un hogar, ordenadas de más
       reciente a más antigua, incluyendo los contadores de ítems totales y comprados.
       @param idHogar → Entero con el ID del hogar a consultar
       @return List<ListaCompras> → Lista con todas las listas del hogar; vacía si no hay
    */
    public List<ListaCompras> listarPorHogar(int idHogar) {
        // Lista de objetos ListaCompras que se llenará con los resultados de la consulta
        List<ListaCompras> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL)) {
            ps.setInt(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ListaComprasDao] Error listando listas: " + e.getMessage());
        }
        return lista;
    }

    /* Método: obtenerPorId
       Propósito: Buscar una lista de compras específica por su ID, validando que
       pertenezca al hogar indicado, con sus contadores de ítems.
       @param idLista → Entero con el ID de la lista a buscar
       @param idHogar → Entero con el ID del hogar para validar pertenencia
       @return ListaCompras → El objeto encontrado, o null si no existe o no pertenece al hogar
    */
    public ListaCompras obtenerPorId(int idLista, int idHogar) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, idLista);
            ps.setInt(2, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("[ListaComprasDao] Error obteniendo lista: " + e.getMessage());
        }
        return null;
    }

    /* Método: actualizar
       Propósito: Actualizar el nombre y el estado de una lista de compras existente.
       @param lista → Objeto ListaCompras con idListaCompras, idHogar, nombreLista y estadoLista
       @return boolean → true si se actualizó correctamente, false si no existe o falló
    */
    public boolean actualizar(ListaCompras lista) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, lista.getNombreLista().trim());
            ps.setString(2, lista.getEstadoLista());
            ps.setInt(3, lista.getIdListaCompras());
            ps.setInt(4, lista.getIdHogar());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ListaComprasDao] Error actualizando lista: " + e.getMessage());
        }
        return false;
    }

    /* Método: eliminar
       Propósito: Eliminar una lista de compras junto con todos sus ítems (detalles).
       Primero elimina los detalles para evitar violación de clave foránea, luego la lista.
       Ambas operaciones usan la misma conexión para consistencia.
       @param idLista → Entero con el ID de la lista a eliminar
       @param idHogar → Entero con el ID del hogar para validar pertenencia
       @return boolean → true si se eliminó la lista correctamente, false si falló
    */
    public boolean eliminar(int idLista, int idHogar) {
        // Consulta SQL para eliminar primero todos los ítems de la lista antes de eliminar la lista
        String sqlDet = "DELETE FROM Detalle_ListaCompras WHERE IDListaCompras = ?";
        try (Connection con = claseConexion.MetodoConectar()) {
            // Paso 1: eliminar todos los detalles (ítems) de la lista
            try (PreparedStatement psDet = con.prepareStatement(sqlDet)) {
                psDet.setInt(1, idLista);
                psDet.executeUpdate();
            }
            // Paso 2: eliminar la lista principal
            try (PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {
                ps.setInt(1, idLista);
                ps.setInt(2, idHogar);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("[ListaComprasDao] Error eliminando lista: " + e.getMessage());
        }
        return false;
    }

    /* Método: recalcularEstado
       Propósito: Actualizar el estado de una lista de compras según el progreso de sus ítems:
       'Completa' si todos están comprados, 'En progreso' si algunos, 'Pendiente' si ninguno.
       Se llama automáticamente después de cada cambio de estado en los ítems.
       @param idLista → Entero con el ID de la lista cuyo estado debe recalcularse
    */
    public void recalcularEstado(int idLista) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_RECALCULAR_ESTADO)) {
            // El ID de lista se pasa cuatro veces: una por cada subconsulta y el WHERE final
            ps.setInt(1, idLista);
            ps.setInt(2, idLista);
            ps.setInt(3, idLista);
            ps.setInt(4, idLista);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[ListaComprasDao] Error recalculando estado: " + e.getMessage());
        }
    }

    /* Método: buscarActivaPorNombre
       Propósito: Buscar una lista activa (Pendiente o En progreso) por nombre exacto en el hogar.
       Usado por el módulo de inventario al agregar automáticamente un producto a una lista existente.
       @param idHogar → Entero con el ID del hogar donde buscar
       @param nombre  → Texto con el nombre exacto de la lista a buscar (se ignoran espacios extremos)
       @return ListaCompras → La lista activa encontrada, o null si no existe ninguna con ese nombre
    */
    public ListaCompras buscarActivaPorNombre(int idHogar, String nombre) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_ACTIVA_POR_NOMBRE)) {
            ps.setInt(1, idHogar);
            ps.setString(2, nombre.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("[ListaComprasDao] Error buscando lista por nombre: " + e.getMessage());
        }
        return null;
    }

    /* Método: mapear
       Propósito: Construir un objeto ListaCompras a partir de la fila actual del ResultSet,
       incluyendo el cálculo de ítems pendientes como diferencia entre total y comprados.
       @param rs → ResultSet posicionado en la fila a mapear
       @return ListaCompras → El objeto construido con los datos de la fila
    */
    private ListaCompras mapear(ResultSet rs) throws SQLException {
        // Objeto ListaCompras que recibirá los valores de cada columna de la fila
        ListaCompras l = new ListaCompras();
        l.setIdListaCompras(rs.getInt("IDListaCompras"));
        l.setIdHogar(rs.getInt("IDHogar"));
        l.setNombreLista(rs.getString("NombreLista"));
        l.setFechaCreacion(rs.getObject("FechaCreacion", LocalDateTime.class));
        l.setEstadoLista(rs.getString("EstadoLista"));
        // Variable entera que almacena el total de ítems en la lista
        int total    = rs.getInt("totalProductos");
        // Variable entera que almacena la cantidad de ítems ya comprados
        int comprados = rs.getInt("totalComprados");
        l.setTotalProductos(total);
        l.setTotalComprados(comprados);
        // Los pendientes se calculan como la diferencia entre el total y los comprados
        l.setTotalPendientes(total - comprados);
        return l;
    }
}
