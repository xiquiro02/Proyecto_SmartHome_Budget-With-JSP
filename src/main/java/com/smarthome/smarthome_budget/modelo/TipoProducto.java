package com.smarthome.smarthome_budget.modelo;

/* Clase: TipoProducto
   Propósito: Representar un tipo de producto del catálogo (tabla Tipo_Producto).
   Se usa para clasificar los productos en el módulo de Inventario y Listas de Compras,
   y para poblar los selectores en los formularios de registro de productos.
   Expone alias getIDTipoProducto / setIDTipoProducto para compatibilidad con JSPs que
   usan la convención de nombres en mayúsculas.
*/
public class TipoProducto {

    // Identificador único del tipo de producto en la base de datos
    private int idTipoProducto;
    // Nombre descriptivo del tipo de producto (ej.: "Alimentos", "Aseo", "Personal")
    private String nombreTipoProducto;

    /* Constructor vacío requerido para instanciar el objeto antes de asignar valores mediante setters */
    public TipoProducto() {}

    /* Constructor completo para crear un tipo de producto con todos sus datos en una sola instrucción */
    public TipoProducto(int idTipoProducto, String nombreTipoProducto) {
        this.idTipoProducto = idTipoProducto;
        this.nombreTipoProducto = nombreTipoProducto;
    }

    // Getters y setters estándar
    public int getIdTipoProducto()              { return idTipoProducto; }
    public void setIdTipoProducto(int v)        { this.idTipoProducto = v; }

    // Alias en mayúsculas para compatibilidad con expresiones EL en JSPs (${t.IDTipoProducto})
    public int getIDTipoProducto()              { return idTipoProducto; }
    public void setIDTipoProducto(int v)        { this.idTipoProducto = v; }

    public String getNombreTipoProducto()       { return nombreTipoProducto; }
    public void setNombreTipoProducto(String v) { this.nombreTipoProducto = v; }
}
