package com.smarthome.smarthome_budget.dao;

import com.smarthome.smarthome_budget.modelo.DetalleListaCompras;
import com.smarthome.smarthome_budget.basedatos.claseConexion;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/* Clase: DetalleListaComprasDao
   Propósito: Gestionar las operaciones de acceso a datos de los ítems de las listas
   de compras (tabla Detalle_ListaCompras). Permite agregar productos a una lista,
   actualizar cantidades, marcar ítems como comprados/pendientes, eliminarlos y listarlos.
   Si el producto ya existe en la lista al agregar, se suma la cantidad en lugar de duplicar.
*/
public class DetalleListaComprasDao {

    // Consulta SQL para insertar un nuevo ítem en una lista de compras con estado inicial no comprado
    private static final String SQL_INSERT =
        "INSERT INTO Detalle_ListaCompras (IDListaCompras, IDProducto, Cantidad, Comprado) VALUES (?,?,?,false)";

    // Consulta SQL que obtiene todos los ítems de una lista con el nombre del producto y su tipo,
    // ordenando primero los pendientes y luego los comprados, ambos grupos alfabéticamente
    private static final String SQL_SELECT_POR_LISTA =
        "SELECT d.*, p.NombreProducto, tp.NombreTipoProducto " +
        "FROM Detalle_ListaCompras d " +
        "JOIN Producto p ON d.IDProducto = p.IDProducto " +
        "JOIN Tipo_Producto tp ON p.IDTipoProducto = tp.IDTipoProducto " +
        "WHERE d.IDListaCompras = ? ORDER BY d.Comprado ASC, p.NombreProducto ASC";

    // Consulta SQL que obtiene un ítem específico por su ID de detalle, incluyendo nombre del producto y tipo
    private static final String SQL_SELECT_BY_ID =
        "SELECT d.*, p.NombreProducto, tp.NombreTipoProducto " +
        "FROM Detalle_ListaCompras d " +
        "JOIN Producto p ON d.IDProducto = p.IDProducto " +
        "JOIN Tipo_Producto tp ON p.IDTipoProducto = tp.IDTipoProducto " +
        "WHERE d.IDDetalleLista = ?";

    // Consulta SQL que busca si ya existe un ítem con el mismo producto en la lista
    private static final String SQL_EXISTE_PRODUCTO =
        "SELECT IDDetalleLista FROM Detalle_ListaCompras WHERE IDListaCompras=? AND IDProducto=?";

    // Consulta SQL para reemplazar la cantidad de un ítem existente
    private static final String SQL_UPDATE_CANTIDAD =
        "UPDATE Detalle_ListaCompras SET Cantidad=? WHERE IDDetalleLista=?";

    // Consulta SQL para sumar una cantidad adicional a la cantidad existente de un ítem
    private static final String SQL_UPDATE_CANTIDAD_SUMA =
        "UPDATE Detalle_ListaCompras SET Cantidad = Cantidad + ? WHERE IDDetalleLista=?";

    // Consulta SQL para cambiar el estado comprado de un ítem individual
    private static final String SQL_MARCAR_COMPRADO =
        "UPDATE Detalle_ListaCompras SET Comprado=? WHERE IDDetalleLista=?";

    // Consulta SQL para cambiar el estado comprado de todos los ítems de una lista a la vez
    private static final String SQL_MARCAR_TODOS =
        "UPDATE Detalle_ListaCompras SET Comprado=? WHERE IDListaCompras=?";

    // Consulta SQL para eliminar un ítem de la lista por su ID
    private static final String SQL_DELETE =
        "DELETE FROM Detalle_ListaCompras WHERE IDDetalleLista=?";

    /* Método: agregarProducto (BigDecimal)
       Propósito: Agregar un producto a la lista de compras con cantidad decimal.
       Si el producto ya existe en la lista, se suma la cantidad al valor actual
       en lugar de crear un registro duplicado.
       @param idLista    → Entero con el ID de la lista donde se agrega el producto
       @param idProducto → Entero con el ID del producto a agregar
       @param cantidad   → BigDecimal con la cantidad del producto
       @return int → 1 si se creó nuevo ítem, 2 si se sumó cantidad a uno existente, -1 si falla
    */
    public int agregarProducto(int idLista, int idProducto, BigDecimal cantidad) {
        // Valida que la cantidad sea positiva antes de continuar
        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) return -1;

        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement psBuscar = con.prepareStatement(SQL_EXISTE_PRODUCTO)) {
            psBuscar.setInt(1, idLista);
            psBuscar.setInt(2, idProducto);
            try (ResultSet rs = psBuscar.executeQuery()) {
                if (rs.next()) {
                    // Variable entera que almacena el ID del detalle existente para sumarle la cantidad
                    int idDetalle = rs.getInt("IDDetalleLista");
                    try (PreparedStatement psUp = con.prepareStatement(SQL_UPDATE_CANTIDAD_SUMA)) {
                        psUp.setBigDecimal(1, cantidad);
                        psUp.setInt(2, idDetalle);
                        psUp.executeUpdate();
                    }
                    return 2;
                }
            }
        } catch (SQLException e) {
            System.err.println("[DetalleListaComprasDao] Error verificando duplicado: " + e.getMessage());
            return -1;
        }

        // El producto no existe en la lista: se inserta como nuevo ítem
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT)) {
            ps.setInt(1, idLista);
            ps.setInt(2, idProducto);
            ps.setBigDecimal(3, cantidad);
            return ps.executeUpdate() > 0 ? 1 : -1;
        } catch (SQLException e) {
            System.err.println("[DetalleListaComprasDao] Error agregando producto: " + e.getMessage());
        }
        return -1;
    }

    /* Método: agregarProducto (int)
       Propósito: Sobrecarga de agregarProducto para compatibilidad con cantidades enteras.
       Convierte el entero a BigDecimal y delega al método principal.
       @param idLista    → Entero con el ID de la lista
       @param idProducto → Entero con el ID del producto
       @param cantidad   → Entero con la cantidad del producto
       @return int → 1 si se creó nuevo ítem, 2 si se sumó cantidad, -1 si falla
    */
    public int agregarProducto(int idLista, int idProducto, int cantidad) {
        return agregarProducto(idLista, idProducto, new BigDecimal(cantidad));
    }

    /* Método: listarPorLista
       Propósito: Obtener todos los ítems de una lista de compras, incluyendo el nombre
       del producto y su tipo. Los pendientes aparecen primero, luego los comprados.
       @param idLista → Entero con el ID de la lista a consultar
       @return List<DetalleListaCompras> → Lista de ítems; vacía si la lista no tiene productos
    */
    public List<DetalleListaCompras> listarPorLista(int idLista) {
        // Lista de objetos DetalleListaCompras que se llenará con los ítems encontrados
        List<DetalleListaCompras> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_POR_LISTA)) {
            ps.setInt(1, idLista);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[DetalleListaComprasDao] Error listando detalles: " + e.getMessage());
        }
        return lista;
    }

    /* Método: obtenerPorId
       Propósito: Buscar un ítem de lista de compras específico por su ID de detalle,
       incluyendo el nombre del producto y su tipo mediante JOIN.
       @param idDetalle → Entero con el ID del detalle a buscar
       @return DetalleListaCompras → El objeto encontrado, o null si no existe
    */
    public DetalleListaCompras obtenerPorId(int idDetalle) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, idDetalle);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("[DetalleListaComprasDao] Error obteniendo detalle: " + e.getMessage());
        }
        return null;
    }

    /* Método: actualizarCantidad (BigDecimal)
       Propósito: Reemplazar la cantidad de un ítem de lista de compras por un nuevo valor decimal.
       @param idDetalle    → Entero con el ID del detalle a actualizar
       @param nuevaCantidad → BigDecimal con la nueva cantidad a asignar
       @return boolean → true si se actualizó correctamente, false si la cantidad no es válida o falló
    */
    public boolean actualizarCantidad(int idDetalle, BigDecimal nuevaCantidad) {
        // Valida que la nueva cantidad sea positiva antes de actualizar
        if (nuevaCantidad == null || nuevaCantidad.compareTo(BigDecimal.ZERO) <= 0) return false;
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE_CANTIDAD)) {
            ps.setBigDecimal(1, nuevaCantidad);
            ps.setInt(2, idDetalle);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DetalleListaComprasDao] Error actualizando cantidad: " + e.getMessage());
        }
        return false;
    }

    /* Método: actualizarCantidad (int)
       Propósito: Sobrecarga de actualizarCantidad para compatibilidad con valores enteros.
       @param idDetalle    → Entero con el ID del detalle a actualizar
       @param nuevaCantidad → Entero con la nueva cantidad a asignar
       @return boolean → true si se actualizó correctamente, false si falló
    */
    public boolean actualizarCantidad(int idDetalle, int nuevaCantidad) {
        return actualizarCantidad(idDetalle, new BigDecimal(nuevaCantidad));
    }

    /* Método: toggleComprado
       Propósito: Cambiar el estado comprado/pendiente de un ítem individual de la lista.
       @param idDetalle → Entero con el ID del detalle a modificar
       @param comprado  → Booleano: true = marcar como comprado, false = marcar como pendiente
       @return boolean → true si se actualizó correctamente, false si falló
    */
    public boolean toggleComprado(int idDetalle, boolean comprado) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_MARCAR_COMPRADO)) {
            ps.setBoolean(1, comprado);
            ps.setInt(2, idDetalle);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DetalleListaComprasDao] Error en toggle comprado: " + e.getMessage());
        }
        return false;
    }

    /* Método: marcarTodos
       Propósito: Cambiar el estado comprado de todos los ítems de una lista al mismo tiempo.
       @param idLista  → Entero con el ID de la lista cuyos ítems se van a marcar
       @param comprado → Booleano: true = marcar todos como comprados, false = desmarcar todos
       @return boolean → true si la operación se ejecutó (incluso si no había filas), false si falló
    */
    public boolean marcarTodos(int idLista, boolean comprado) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_MARCAR_TODOS)) {
            ps.setBoolean(1, comprado);
            ps.setInt(2, idLista);
            // Se acepta 0 filas afectadas (lista vacía) como operación exitosa
            return ps.executeUpdate() >= 0;
        } catch (SQLException e) {
            System.err.println("[DetalleListaComprasDao] Error marcando todos: " + e.getMessage());
        }
        return false;
    }

    /* Método: eliminar
       Propósito: Eliminar un ítem de la lista de compras por su ID de detalle.
       @param idDetalle → Entero con el ID del detalle a eliminar
       @return boolean → true si se eliminó correctamente, false si no existe o falló
    */
    public boolean eliminar(int idDetalle) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, idDetalle);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DetalleListaComprasDao] Error eliminando detalle: " + e.getMessage());
        }
        return false;
    }

    /* Método: mapear
       Propósito: Construir un objeto DetalleListaCompras a partir de la fila actual
       del ResultSet, mapeando cada columna a su atributo correspondiente.
       @param rs → ResultSet posicionado en la fila a mapear
       @return DetalleListaCompras → El objeto construido con los datos de la fila
    */
    private DetalleListaCompras mapear(ResultSet rs) throws SQLException {
        // Objeto DetalleListaCompras que recibirá los valores de cada columna de la fila
        DetalleListaCompras d = new DetalleListaCompras();
        d.setIdDetalleLista(rs.getInt("IDDetalleLista"));
        d.setIdListaCompras(rs.getInt("IDListaCompras"));
        d.setIdProducto(rs.getInt("IDProducto"));
        d.setCantidadDecimal(rs.getBigDecimal("Cantidad"));
        d.setComprado(rs.getBoolean("Comprado"));
        d.setNombreProducto(rs.getString("NombreProducto"));
        d.setNombreTipoProducto(rs.getString("NombreTipoProducto"));
        return d;
    }
}
