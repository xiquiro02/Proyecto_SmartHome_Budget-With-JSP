package com.smarthome.smarthome_budget.dao;

import com.smarthome.smarthome_budget.modelo.Producto;
import com.smarthome.smarthome_budget.modelo.TipoProducto;
import com.smarthome.smarthome_budget.basedatos.claseConexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/* Clase: ProductoDao
   Propósito: Gestionar las operaciones de acceso a datos de la tabla Producto y
   Tipo_Producto. Permite obtener o crear productos por nombre (reutilizando existentes
   antes de insertar nuevos), listar todos los productos con su tipo y listar los tipos
   de producto disponibles para los formularios de registro.
*/
public class ProductoDao {

    // Consulta SQL que busca un producto por nombre exacto (insensible a mayúsculas) y tipo de producto
    // language=sql
    private static final String SQL_BUSCAR_POR_NOMBRE =
        "SELECT IDProducto FROM Producto WHERE LOWER(NombreProducto) = LOWER(?) AND IDTipoProducto = ?";

    // Consulta SQL que busca un producto solo por nombre, sin importar el tipo
    // language=sql
    private static final String SQL_BUSCAR_POR_NOMBRE_SOLO =
        "SELECT IDProducto FROM Producto WHERE LOWER(NombreProducto) = LOWER(?)";

    // Consulta SQL para insertar un nuevo producto con su tipo de producto
    // language=sql
    private static final String SQL_INSERT =
        "INSERT INTO Producto (NombreProducto, IDTipoProducto) VALUES (?, ?)";

    // Consulta SQL que lista todos los productos con el nombre de su tipo, ordenados por nombre
    // language=sql
    private static final String SQL_SELECT_ALL =
        "SELECT p.IDProducto, p.NombreProducto, p.Descripcion, p.IDTipoProducto, " +
        "tp.NombreTipoProducto " +
        "FROM Producto p " +
        "JOIN Tipo_Producto tp ON p.IDTipoProducto = tp.IDTipoProducto " +
        "ORDER BY p.NombreProducto";

    // Consulta SQL que lista todos los tipos de producto ordenados alfabéticamente
    // language=sql
    private static final String SQL_SELECT_TIPOS =
        "SELECT IDTipoProducto, NombreTipoProducto FROM Tipo_Producto ORDER BY NombreTipoProducto";

    /* Método: obtenerOCrearProducto
       Propósito: Buscar un producto existente por nombre y tipo; si no existe con ese tipo,
       buscar solo por nombre (puede existir con otro tipo); si tampoco existe, crearlo nuevo.
       Evita duplicar productos en la tabla Producto al agregar ítems a listas o inventario.
       @param nombreProducto  → Texto con el nombre del producto a buscar o crear
       @param idTipoProducto  → Entero con el ID del tipo de producto preferido
       @return int → ID del producto existente o recién creado, o -1 si todas las operaciones fallan
    */
    public int obtenerOCrearProducto(String nombreProducto, int idTipoProducto) {
        // Paso 1: buscar el producto por nombre exacto y tipo de producto específico
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_NOMBRE)) {
            ps.setString(1, nombreProducto.trim());
            ps.setInt(2, idTipoProducto);
            try (ResultSet rs = ps.executeQuery()) {
                // Variable entera que almacena el ID del producto si ya existe con ese nombre y tipo
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[ProductoDao] Error buscando por nombre+tipo: " + e.getMessage());
        }

        // Paso 2: buscar solo por nombre (puede existir con otro tipo de producto)
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_NOMBRE_SOLO)) {
            ps.setString(1, nombreProducto.trim());
            try (ResultSet rs = ps.executeQuery()) {
                // Variable entera que almacena el ID del producto si ya existe con ese nombre
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[ProductoDao] Error buscando por nombre: " + e.getMessage());
        }

        // Paso 3: el producto no existe → crear uno nuevo con el nombre y tipo indicados
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombreProducto.trim());
            ps.setInt(2, idTipoProducto);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                // Variable entera que almacena el ID autogenerado del nuevo producto
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[ProductoDao] Error creando producto: " + e.getMessage());
        }
        return -1;
    }

    /* Método: listarTodos
       Propósito: Obtener todos los productos registrados en el sistema con el nombre
       de su tipo de producto, ordenados alfabéticamente.
       @return List<Producto> → Lista con todos los productos; vacía si no hay datos
    */
    public List<Producto> listarTodos() {
        // Lista de objetos Producto que se llenará con los resultados de la consulta
        List<Producto> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // Objeto Producto construido con los datos de cada fila del ResultSet
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("IDProducto"));
                p.setNombreProducto(rs.getString("NombreProducto"));
                p.setDescripcion(rs.getString("Descripcion"));
                p.setIdTipoProducto(rs.getInt("IDTipoProducto"));
                p.setNombreTipoProducto(rs.getString("NombreTipoProducto"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("[ProductoDao] Error listando productos: " + e.getMessage());
        }
        return lista;
    }

    /* Método: listarTiposProducto
       Propósito: Obtener todos los tipos de producto disponibles para poblar
       los selectores en los formularios de registro de productos e inventario.
       @return List<TipoProducto> → Lista con todos los tipos de producto; vacía si no hay datos
    */
    public List<TipoProducto> listarTiposProducto() {
        // Lista de objetos TipoProducto que se llenará con los resultados de la consulta
        List<TipoProducto> tipos = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_TIPOS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // Objeto TipoProducto construido con los datos de cada fila del ResultSet
                TipoProducto t = new TipoProducto();
                t.setIdTipoProducto(rs.getInt("IDTipoProducto"));
                t.setNombreTipoProducto(rs.getString("NombreTipoProducto"));
                tipos.add(t);
            }
        } catch (SQLException e) {
            System.err.println("[ProductoDao] Error listando tipos: " + e.getMessage());
        }
        return tipos;
    }

    /* Método: listarTipos
       Propósito: Alias de listarTiposProducto() para mantener compatibilidad con el código
       existente en InventarioServlet y ListasComprasServlet que usan este nombre.
       @return List<TipoProducto> → Lista con todos los tipos de producto
    */
    public List<TipoProducto> listarTipos() {
        return listarTiposProducto();
    }
}
