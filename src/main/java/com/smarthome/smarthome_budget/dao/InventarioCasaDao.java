package com.smarthome.smarthome_budget.dao;

import com.smarthome.smarthome_budget.modelo.InventarioCasa;
import com.smarthome.smarthome_budget.basedatos.claseConexion;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/* Clase: InventarioCasaDao
   Propósito: Gestionar las operaciones de acceso a datos del inventario del hogar
   (tabla Inventario_Casa). Permite registrar o actualizar el stock de productos,
   listarlos con distintos filtros (por tipo, stock bajo, agotados, orden), obtener
   un producto específico y obtener el resumen general del inventario.
   Al registrar: si el producto ya existe para el hogar, se suma el stock; si no, se inserta.
*/
public class InventarioCasaDao {

    // Constante de texto que define el umbral de stock bajo para las consultas de alerta
    private static final String UMBRAL_STOCK_BAJO = "2.00";

    // Fragmento SQL base para las consultas SELECT: incluye JOIN con Producto y Tipo_Producto
    // language=sql
    private static final String SQL_SELECT_BASE =
        "SELECT ic.IDInventario, ic.IDProducto, ic.IDHogar, " +
        "ic.StockActual, ic.FechaActualizacion, " +
        "p.NombreProducto, p.Descripcion, " +
        "p.IDTipoProducto, tp.NombreTipoProducto " +
        "FROM Inventario_Casa ic " +
        "JOIN Producto p ON ic.IDProducto = p.IDProducto " +
        "JOIN Tipo_Producto tp ON p.IDTipoProducto = tp.IDTipoProducto ";

    // Consulta SQL para insertar un nuevo registro de inventario con la fecha actual
    // language=sql
    private static final String SQL_INSERT =
        "INSERT INTO Inventario_Casa (IDProducto, IDHogar, StockActual, FechaActualizacion) " +
        "VALUES (?,?,?,NOW())";

    // Consulta SQL para actualizar el stock y la fecha de un registro de inventario existente
    // language=sql
    private static final String SQL_UPDATE =
        "UPDATE Inventario_Casa SET StockActual=?, FechaActualizacion=NOW() " +
        "WHERE IDInventario=? AND IDHogar=?";

    // Consulta SQL para eliminar un registro de inventario por su ID y hogar
    // language=sql
    private static final String SQL_DELETE =
        "DELETE FROM Inventario_Casa WHERE IDInventario=? AND IDHogar=?";

    // Consulta SQL que verifica si ya existe un registro del producto en el inventario del hogar
    // language=sql
    private static final String SQL_EXISTE =
        "SELECT IDInventario FROM Inventario_Casa WHERE IDProducto=? AND IDHogar=?";

    /* Método: registrar
       Propósito: Registrar un producto en el inventario del hogar. Si el producto ya existe,
       suma la cantidad al stock actual. Si no existe, inserta un nuevo registro.
       @param inv → Objeto InventarioCasa con idProducto, idHogar y stockActual (cantidad a agregar)
       @return int → 1 si se creó nuevo registro, 2 si se sumó cantidad a uno existente, -1 si falla
    */
    public int registrar(InventarioCasa inv) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement psEx = con.prepareStatement(SQL_EXISTE)) {
            psEx.setInt(1, inv.getIdProducto());
            psEx.setInt(2, inv.getIdHogar());
            try (ResultSet rs = psEx.executeQuery()) {
                if (rs.next()) {
                    // Variable entera que almacena el ID del registro existente para sumarle el stock
                    int idInv = rs.getInt("IDInventario");
                    // Consulta SQL para sumar el stock adicional al registro ya existente
                    String sqlSum =
                        "UPDATE Inventario_Casa SET StockActual = StockActual + ?, " +
                        "FechaActualizacion = NOW() WHERE IDInventario = ?";
                    try (PreparedStatement psUp = con.prepareStatement(sqlSum)) {
                        psUp.setBigDecimal(1, inv.getStockActual());
                        psUp.setInt(2, idInv);
                        psUp.executeUpdate();
                    }
                    return 2;
                }
            }
        } catch (SQLException e) {
            System.err.println("[InventarioCasaDao] Error verificando existencia: " + e.getMessage());
            return -1;
        }

        // El producto no existe en el inventario del hogar: se inserta como nuevo registro
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT)) {
            ps.setInt(1, inv.getIdProducto());
            ps.setInt(2, inv.getIdHogar());
            ps.setBigDecimal(3, inv.getStockActual());
            return ps.executeUpdate() > 0 ? 1 : -1;
        } catch (SQLException e) {
            System.err.println("[InventarioCasaDao] Error registrando en inventario: " + e.getMessage());
        }
        return -1;
    }

    /* Método: listarPorHogar
       Propósito: Obtener todos los productos del inventario de un hogar, ordenados
       por tipo de producto y luego por nombre de producto.
       @param idHogar → Entero con el ID del hogar a consultar
       @return List<InventarioCasa> → Lista con todos los productos del inventario; vacía si no hay
    */
    public List<InventarioCasa> listarPorHogar(int idHogar) {
        return listar(idHogar, null, null);
    }

    /* Método: listarPorTipo
       Propósito: Filtrar los productos del inventario por tipo de producto.
       @param idHogar → Entero con el ID del hogar
       @param idTipo  → Entero con el ID del tipo de producto a filtrar
       @return List<InventarioCasa> → Lista con los productos del tipo indicado
    */
    public List<InventarioCasa> listarPorTipo(int idHogar, int idTipo) {
        return listar(idHogar, "tp.IDTipoProducto = ?", idTipo);
    }

    /* Método: listarStockBajo
       Propósito: Obtener los productos cuyo stock es mayor a 0 pero menor o igual
       al umbral de alerta (2.00 unidades), considerados en riesgo de agotarse.
       @param idHogar → Entero con el ID del hogar
       @return List<InventarioCasa> → Lista con los productos en stock bajo
    */
    public List<InventarioCasa> listarStockBajo(int idHogar) {
        return listar(idHogar,
            "ic.StockActual > 0 AND ic.StockActual <= " + UMBRAL_STOCK_BAJO, null);
    }

    /* Método: listarAgotados
       Propósito: Obtener los productos cuyo stock es exactamente 0 (completamente agotados).
       @param idHogar → Entero con el ID del hogar
       @return List<InventarioCasa> → Lista con los productos agotados
    */
    public List<InventarioCasa> listarAgotados(int idHogar) {
        return listar(idHogar, "ic.StockActual = 0", null);
    }

    /* Método: listarOrdenadoCantidadAsc
       Propósito: Obtener todos los productos del inventario ordenados por stock de menor a mayor.
       @param idHogar → Entero con el ID del hogar
       @return List<InventarioCasa> → Lista de productos ordenada de menor a mayor stock
    */
    public List<InventarioCasa> listarOrdenadoCantidadAsc(int idHogar) {
        // Lista de objetos InventarioCasa que se llenará con los resultados de la consulta
        List<InventarioCasa> lista = new ArrayList<>();
        // Consulta SQL con orden ascendente por StockActual y luego por nombre de producto
        String sql = SQL_SELECT_BASE +
                     "WHERE ic.IDHogar = ? ORDER BY ic.StockActual ASC, p.NombreProducto ASC";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[InventarioCasaDao] Error listando inventario asc: " + e.getMessage());
        }
        return lista;
    }

    /* Método: listarOrdenadoCantidadDesc
       Propósito: Obtener todos los productos del inventario ordenados por stock de mayor a menor.
       @param idHogar → Entero con el ID del hogar
       @return List<InventarioCasa> → Lista de productos ordenada de mayor a menor stock
    */
    public List<InventarioCasa> listarOrdenadoCantidadDesc(int idHogar) {
        // Lista de objetos InventarioCasa que se llenará con los resultados de la consulta
        List<InventarioCasa> lista = new ArrayList<>();
        // Consulta SQL con orden descendente por StockActual y luego por nombre de producto
        String sql = SQL_SELECT_BASE +
                     "WHERE ic.IDHogar = ? ORDER BY ic.StockActual DESC, p.NombreProducto ASC";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[InventarioCasaDao] Error listando inventario desc: " + e.getMessage());
        }
        return lista;
    }

    /* Método: obtenerPorId
       Propósito: Buscar un registro de inventario específico por su ID, validando
       que pertenezca al hogar indicado.
       @param idInventario → Entero con el ID del registro de inventario
       @param idHogar      → Entero con el ID del hogar para validar pertenencia
       @return InventarioCasa → El objeto encontrado, o null si no existe o no pertenece al hogar
    */
    public InventarioCasa obtenerPorId(int idInventario, int idHogar) {
        // Consulta SQL que filtra por ID de inventario y ID de hogar para seguridad
        String sql = SQL_SELECT_BASE + "WHERE ic.IDInventario = ? AND ic.IDHogar = ?";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idInventario);
            ps.setInt(2, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("[InventarioCasaDao] Error obteniendo inventario: " + e.getMessage());
        }
        return null;
    }

    /* Método: actualizar
       Propósito: Actualizar el stock actual de un registro de inventario existente
       y refrescar su fecha de actualización a la fecha y hora actuales.
       @param inv → Objeto InventarioCasa con idInventario, idHogar y el nuevo stockActual
       @return boolean → true si se actualizó correctamente, false si falló
    */
    public boolean actualizar(InventarioCasa inv) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {
            ps.setBigDecimal(1, inv.getStockActual());
            ps.setInt(2, inv.getIdInventario());
            ps.setInt(3, inv.getIdHogar());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[InventarioCasaDao] Error actualizando inventario: " + e.getMessage());
        }
        return false;
    }

    /* Método: eliminar
       Propósito: Eliminar un registro de inventario por su ID, validando que
       pertenezca al hogar indicado para evitar eliminaciones no autorizadas.
       @param idInventario → Entero con el ID del registro a eliminar
       @param idHogar      → Entero con el ID del hogar para validar pertenencia
       @return boolean → true si se eliminó correctamente, false si no existe o falló
    */
    public boolean eliminar(int idInventario, int idHogar) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, idInventario);
            ps.setInt(2, idHogar);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[InventarioCasaDao] Error eliminando inventario: " + e.getMessage());
        }
        return false;
    }

    /* Método: obtenerResumen
       Propósito: Calcular el resumen del inventario del hogar: cantidad de productos
       disponibles (stock > umbral), por agotar (stock bajo pero > 0) y cantidad
       de categorías distintas de tipos de producto en el inventario.
       @param idHogar → Entero con el ID del hogar a resumir
       @return int[] → Arreglo de tres enteros: [0]=disponibles, [1]=porAgotar, [2]=categorias
    */
    public int[] obtenerResumen(int idHogar) {
        // Arreglo de tres enteros: posición 0 = disponibles, 1 = por agotar, 2 = categorías
        int[] r = new int[3];
        // Consulta SQL que calcula los tres contadores en una sola consulta con CASE
        String sql =
            "SELECT " +
            "SUM(CASE WHEN ic.StockActual > " + UMBRAL_STOCK_BAJO + " THEN 1 ELSE 0 END) AS disponibles, " +
            "SUM(CASE WHEN ic.StockActual <= " + UMBRAL_STOCK_BAJO + " AND ic.StockActual > 0 THEN 1 ELSE 0 END) AS porAgotar, " +
            "COUNT(DISTINCT p.IDTipoProducto) AS categorias " +
            "FROM Inventario_Casa ic " +
            "JOIN Producto p ON ic.IDProducto = p.IDProducto " +
            "WHERE ic.IDHogar = ?";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    r[0] = rs.getInt("disponibles");
                    r[1] = rs.getInt("porAgotar");
                    r[2] = rs.getInt("categorias");
                }
            }
        } catch (SQLException e) {
            System.err.println("[InventarioCasaDao] Error resumen inventario: " + e.getMessage());
        }
        return r;
    }

    /* Método: listar
       Propósito: Método privado reutilizable para ejecutar consultas de listado con
       un filtro WHERE adicional opcional y un parámetro extra para ese filtro.
       @param idHogar    → Entero con el ID del hogar (primer parámetro siempre)
       @param whereExtra → Texto con la condición SQL adicional; null si no hay filtro extra
       @param param      → Objeto con el valor del parámetro adicional; null si whereExtra es null
       @return List<InventarioCasa> → Lista de productos que cumplen el filtro; vacía si no hay
    */
    private List<InventarioCasa> listar(int idHogar, String whereExtra, Object param) {
        // Lista de objetos InventarioCasa que recibirá los resultados de la consulta
        List<InventarioCasa> lista = new ArrayList<>();
        // Consulta SQL construida dinámicamente con el filtro opcional
        String sql = SQL_SELECT_BASE + "WHERE ic.IDHogar = ?" +
                     (whereExtra != null ? " AND " + whereExtra : "") +
                     " ORDER BY tp.NombreTipoProducto ASC, p.NombreProducto ASC";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idHogar);
            // Si hay un filtro extra con parámetro, se asigna como segundo parámetro
            if (param != null) ps.setObject(2, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[InventarioCasaDao] Error listando inventario: " + e.getMessage());
        }
        return lista;
    }

    /* Método: mapear
       Propósito: Construir un objeto InventarioCasa a partir de la fila actual del ResultSet,
       mapeando cada columna a su atributo correspondiente del modelo.
       @param rs → ResultSet posicionado en la fila a mapear
       @return InventarioCasa → El objeto construido con los datos de la fila
    */
    private InventarioCasa mapear(ResultSet rs) throws SQLException {
        // Objeto InventarioCasa que recibirá los valores de cada columna de la fila
        InventarioCasa inv = new InventarioCasa();
        inv.setIdInventario(rs.getInt("IDInventario"));
        inv.setIdProducto(rs.getInt("IDProducto"));
        inv.setIdHogar(rs.getInt("IDHogar"));
        inv.setStockActual(rs.getBigDecimal("StockActual"));
        inv.setFechaActualizacion(rs.getObject("FechaActualizacion", LocalDateTime.class));
        inv.setNombreProducto(rs.getString("NombreProducto"));
        inv.setDescripcion(rs.getString("Descripcion"));
        inv.setIdTipoProducto(rs.getInt("IDTipoProducto"));
        inv.setNombreTipoProducto(rs.getString("NombreTipoProducto"));
        return inv;
    }
}
