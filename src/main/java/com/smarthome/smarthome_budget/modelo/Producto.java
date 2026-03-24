package com.smarthome.smarthome_budget.modelo;

/* Clase: Producto
   Propósito: Representar un producto del catálogo (tabla Producto). Contiene los datos
   propios del producto (nombre y descripción) junto con la referencia a su tipo de producto.
   El campo nombreTipoProducto es auxiliar: se carga mediante JOIN en los listados para
   evitar consultas adicionales al mostrarlo en las vistas de inventario y listas de compras.
*/
public class Producto {

    // Identificador único del producto en la base de datos
    private int idProducto;
    // Nombre del producto (ej.: "Arroz", "Jabón líquido")
    private String nombreProducto;
    // Descripción opcional del producto; puede ser null si no se especifica
    private String descripcion;
    // Identificador del tipo de producto al que pertenece (clave foránea a Tipo_Producto)
    private int idTipoProducto;
    // Campo auxiliar cargado por JOIN con Tipo_Producto para mostrar el nombre sin consulta adicional
    private String nombreTipoProducto;

    /* Constructor vacío requerido para instanciar el objeto antes de asignar valores mediante setters */
    public Producto() {}

    /* Constructor completo para crear un producto con todos sus datos en una sola instrucción */
    public Producto(int idProducto, String nombreProducto, String descripcion,
                    int idTipoProducto, String nombreTipoProducto) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.descripcion = descripcion;
        this.idTipoProducto = idTipoProducto;
        this.nombreTipoProducto = nombreTipoProducto;
    }

    // ── Getters / Setters ─────────────────────────────────────────────────────

    public int getIdProducto()              { return idProducto; }
    public void setIdProducto(int v)        { this.idProducto = v; }

    public String getNombreProducto()       { return nombreProducto; }
    public void setNombreProducto(String v) { this.nombreProducto = v; }

    public String getDescripcion()          { return descripcion; }
    public void setDescripcion(String v)    { this.descripcion = v; }

    public int getIdTipoProducto()          { return idTipoProducto; }
    public void setIdTipoProducto(int v)    { this.idTipoProducto = v; }

    public String getNombreTipoProducto()       { return nombreTipoProducto; }
    public void setNombreTipoProducto(String v) { this.nombreTipoProducto = v; }
}
