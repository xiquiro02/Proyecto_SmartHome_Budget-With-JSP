package com.smarthome.smarthome_budget.modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;

/* Clase: DetalleListaCompras
   Propósito: Representar un ítem (producto) dentro de una lista de compras
   (tabla Detalle_ListaCompras). Almacena la cantidad como BigDecimal para soportar
   valores decimales (ej.: 1.5 kg de arroz), el estado de compra y referencias al
   producto. Incluye alias de compatibilidad para código legado que usaba cantidad como int,
   y un método de formato que muestra enteros sin decimales y fracciones con hasta 2 decimales.
*/
public class DetalleListaCompras {

    // Identificador único del ítem en la base de datos
    private int idDetalleLista;
    // Cantidad del producto en la lista; usa BigDecimal para soportar valores decimales
    private BigDecimal cantidadDecimal;
    // Indicador de si el producto ya fue comprado (true) o sigue pendiente (false)
    private boolean comprado;
    // Identificador de la lista de compras a la que pertenece este ítem (clave foránea a Lista_Compras)
    private int idListaCompras;
    // Identificador del producto asociado a este ítem (clave foránea a Producto)
    private int idProducto;
    // Campo auxiliar cargado por JOIN con Producto para mostrar el nombre sin consulta adicional
    private String nombreProducto;
    // Campo auxiliar cargado por JOIN con Tipo_Producto para mostrar el tipo sin consulta adicional
    private String nombreTipoProducto;

    /* Constructor vacío: inicializa cantidadDecimal en 1 como valor predeterminado seguro */
    public DetalleListaCompras() {
        this.cantidadDecimal = BigDecimal.ONE;
    }

    /* Constructor de compatibilidad que acepta cantidad como int y la convierte a BigDecimal */
    public DetalleListaCompras(int idDetalleLista, int cantidad, boolean comprado,
                               int idListaCompras, int idProducto,
                               String nombreProducto, String nombreTipoProducto) {
        this.idDetalleLista     = idDetalleLista;
        this.cantidadDecimal    = new BigDecimal(cantidad);
        this.comprado           = comprado;
        this.idListaCompras     = idListaCompras;
        this.idProducto         = idProducto;
        this.nombreProducto     = nombreProducto;
        this.nombreTipoProducto = nombreTipoProducto;
    }

    // ── Getters / Setters ─────────────────────────────────────────────────────

    public int getIdDetalleLista()       { return idDetalleLista; }
    public void setIdDetalleLista(int v) { this.idDetalleLista = v; }

    // Retorna BigDecimal.ONE como valor seguro si cantidadDecimal es null
    public BigDecimal getCantidadDecimal()         { return cantidadDecimal != null ? cantidadDecimal : BigDecimal.ONE; }
    public void setCantidadDecimal(BigDecimal v)   { this.cantidadDecimal = (v != null) ? v : BigDecimal.ONE; }

    // Alias int para compatibilidad con código existente que usa cantidad como entero
    public int getCantidad()        { return cantidadDecimal != null ? cantidadDecimal.intValue() : 1; }
    public void setCantidad(int v)  { this.cantidadDecimal = new BigDecimal(v); }

    /* Método: getCantidadFormateada
       Propósito: Obtener la cantidad como texto legible para las vistas: si la cantidad es un
       número entero exacto (ej.: 2.00) muestra solo el entero ("2"); si tiene decimales
       significativos (ej.: 1.50) muestra hasta 2 decimales ("1.50").
       @return String → Cantidad formateada sin ceros innecesarios; "1" si cantidadDecimal es null
    */
    public String getCantidadFormateada() {
        if (cantidadDecimal == null) return "1";
        if (cantidadDecimal.stripTrailingZeros().scale() <= 0) {
            return cantidadDecimal.toBigInteger().toString();
        }
        return cantidadDecimal.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    public boolean isComprado()          { return comprado; }
    public void setComprado(boolean v)   { this.comprado = v; }

    public int getIdListaCompras()       { return idListaCompras; }
    public void setIdListaCompras(int v) { this.idListaCompras = v; }

    public int getIdProducto()           { return idProducto; }
    public void setIdProducto(int v)     { this.idProducto = v; }

    public String getNombreProducto()        { return nombreProducto; }
    public void setNombreProducto(String v)  { this.nombreProducto = v; }

    public String getNombreTipoProducto()        { return nombreTipoProducto; }
    public void setNombreTipoProducto(String v)  { this.nombreTipoProducto = v; }

    // Alias corto para JSPs que acceden al tipo como "tipoProducto" en lugar de "nombreTipoProducto"
    public String getTipoProducto()              { return nombreTipoProducto; }
    public void setTipoProducto(String v)        { this.nombreTipoProducto = v; }
}
