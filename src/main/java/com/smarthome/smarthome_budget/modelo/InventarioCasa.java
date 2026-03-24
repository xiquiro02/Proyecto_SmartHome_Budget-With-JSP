package com.smarthome.smarthome_budget.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/* Clase: InventarioCasa
   Propósito: Representar un producto en el inventario del hogar (tabla Inventario_Casa).
   Registra el stock actual de cada producto disponible en casa, con su fecha de última
   actualización. Los campos de nombre, descripción y tipo son auxiliares cargados por JOIN.
   Incluye métodos de alerta de stock (agotado / bajo), selección de color e ícono por tipo
   de producto, y formato de cantidad y fecha para usar directamente en las vistas JSP.
*/
public class InventarioCasa {

    // Identificador único del registro de inventario en la base de datos
    private int idInventario;
    // Stock actual del producto en el hogar; usa BigDecimal para soportar cantidades decimales (ej.: 1.5 kg)
    private BigDecimal stockActual;
    // Fecha y hora de la última actualización del stock de este producto
    private LocalDateTime fechaActualizacion;
    // Identificador del producto registrado en inventario (clave foránea a Producto)
    private int idProducto;
    // Identificador del hogar al que pertenece este inventario (clave foránea a Hogar)
    private int idHogar;
    // Campo auxiliar cargado por JOIN con Producto para mostrar el nombre sin consulta adicional
    private String nombreProducto;
    // Campo auxiliar cargado por JOIN con Producto para mostrar la descripción sin consulta adicional
    private String descripcion;
    // Campo auxiliar cargado por JOIN con Tipo_Producto para el ID del tipo
    private int idTipoProducto;
    // Campo auxiliar cargado por JOIN con Tipo_Producto para mostrar el nombre del tipo sin consulta adicional
    private String nombreTipoProducto;

    // Umbral de stock bajo: si el stock está entre 1 y este valor se muestra alerta de stock bajo
    private static final BigDecimal STOCK_MINIMO_ALERTA = new BigDecimal("2.00");

    /* Constructor vacío: inicializa stockActual en cero como valor predeterminado seguro */
    public InventarioCasa() {
        this.stockActual = BigDecimal.ZERO;
    }

    /* Constructor completo para crear un registro de inventario con todos sus datos en una sola instrucción */
    public InventarioCasa(int idInventario, BigDecimal stockActual, LocalDateTime fechaActualizacion,
                          int idProducto, int idHogar, String nombreProducto, String descripcion,
                          int idTipoProducto, String nombreTipoProducto) {
        this.idInventario = idInventario;
        this.stockActual = (stockActual != null) ? stockActual : BigDecimal.ZERO;
        this.fechaActualizacion = fechaActualizacion;
        this.idProducto = idProducto;
        this.idHogar = idHogar;
        this.nombreProducto = nombreProducto;
        this.descripcion = descripcion;
        this.idTipoProducto = idTipoProducto;
        this.nombreTipoProducto = nombreTipoProducto;
    }

    // ── Getters / Setters ─────────────────────────────────────────────────────

    public int getIdInventario()            { return idInventario; }
    public void setIdInventario(int v)      { this.idInventario = v; }

    // Retorna BigDecimal.ZERO si stockActual es null para evitar NullPointerException en las vistas
    public BigDecimal getStockActual()             { return stockActual; }
    public void setStockActual(BigDecimal v)       { this.stockActual = (v != null) ? v : BigDecimal.ZERO; }

    // Alias para compatibilidad con código que llama a getCantidad() / setCantidad() en lugar de getStockActual()
    public BigDecimal getCantidad()                { return stockActual; }
    public void setCantidad(BigDecimal v)          { this.stockActual = (v != null) ? v : BigDecimal.ZERO; }

    public LocalDateTime getFechaActualizacion()        { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime v)  { this.fechaActualizacion = v; }

    // Alias para compatibilidad con código que accede a la fecha como "fechaRegistro"
    public LocalDateTime getFechaRegistro()             { return fechaActualizacion; }
    public void setFechaRegistro(LocalDateTime v)       { this.fechaActualizacion = v; }

    public int getIdProducto()              { return idProducto; }
    public void setIdProducto(int v)        { this.idProducto = v; }

    public int getIdHogar()                 { return idHogar; }
    public void setIdHogar(int v)           { this.idHogar = v; }

    public String getNombreProducto()       { return nombreProducto; }
    public void setNombreProducto(String v) { this.nombreProducto = v; }

    public String getDescripcion()          { return descripcion; }
    public void setDescripcion(String v)    { this.descripcion = v; }

    public int getIdTipoProducto()          { return idTipoProducto; }
    public void setIdTipoProducto(int v)    { this.idTipoProducto = v; }

    public String getNombreTipoProducto()       { return nombreTipoProducto; }
    public void setNombreTipoProducto(String v) { this.nombreTipoProducto = v; }

    // El stock mínimo de alerta es un valor fijo de clase; el setter existe por compatibilidad pero no tiene efecto
    public BigDecimal getStockMinimo()      { return STOCK_MINIMO_ALERTA; }
    public void setStockMinimo(int v)       { /* ignorado — valor fijo de clase */ }

    // ── Métodos de alerta y presentación ─────────────────────────────────────

    /* Método: isAgotado
       Propósito: Verificar si el producto está completamente agotado (stock cero o menor).
       @return boolean → true si el stock es null, cero o negativo
    */
    public boolean isAgotado() {
        return stockActual == null || stockActual.compareTo(BigDecimal.ZERO) <= 0;
    }

    /* Método: isStockBajo
       Propósito: Verificar si el stock está en nivel bajo de alerta (mayor que cero
       pero menor o igual al umbral STOCK_MINIMO_ALERTA de 2 unidades).
       @return boolean → true si el stock está entre 0 (exclusivo) y 2 (inclusivo)
    */
    public boolean isStockBajo() {
        if (stockActual == null) return false;
        return stockActual.compareTo(BigDecimal.ZERO) > 0 &&
               stockActual.compareTo(STOCK_MINIMO_ALERTA) <= 0;
    }

    /* Método: getColorCSS
       Propósito: Obtener el nombre de la clase CSS de color asociada al tipo de producto,
       para aplicar el estilo visual correspondiente en las tarjetas del inventario.
       @return String → Nombre del color CSS: "verde", "azul", "morado", "naranja" o "gris" por defecto
    */
    public String getColorCSS() {
        if (nombreTipoProducto == null) return "gris";
        switch (nombreTipoProducto.toLowerCase()) {
            case "alimentos":       return "verde";
            case "aseo":            return "azul";
            case "personal":        return "morado";
            case "ropa y calzado":  return "naranja";
            default:                return "gris";
        }
    }

    /* Método: getIconoProducto
       Propósito: Obtener el nombre del archivo de imagen del ícono asociado al tipo de producto,
       para mostrarlo en las tarjetas del inventario según la categoría del producto.
       @return String → Nombre del archivo de imagen, o "cajas.png" como ícono genérico por defecto
    */
    public String getIconoProducto() {
        if (nombreTipoProducto == null) return "cajas.png";
        switch (nombreTipoProducto.toLowerCase()) {
            case "alimentos":       return "alimentos-saludables.png";
            case "aseo":            return "Aseo.png";
            case "personal":        return "maquillaje.png";
            case "ropa y calzado":  return "Ropa-calzado.jpg";
            default:                return "cajas.png";
        }
    }

    /* Método: getFechaActualizacionFormateada
       Propósito: Obtener la fecha de última actualización del stock en formato legible
       para las vistas (ej.: "15 Mar 2025"). Retorna cadena vacía si la fecha es null.
       @return String → Fecha formateada como "dd MMM yyyy", o "" si no hay fecha
    */
    public String getFechaActualizacionFormateada() {
        if (fechaActualizacion == null) return "";
        return fechaActualizacion.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    /* Método: getCantidadFormateada
       Propósito: Obtener el stock actual como texto legible: si es un número entero exacto
       (ej.: 3.00) muestra solo el entero ("3"); si tiene decimales significativos (ej.: 1.50)
       muestra la representación decimal sin ceros innecesarios ("1.5").
       @return String → Stock formateado sin ceros innecesarios; "0" si stockActual es null
    */
    public String getCantidadFormateada() {
        if (stockActual == null) return "0";
        java.math.BigDecimal stripped = stockActual.stripTrailingZeros();
        if (stripped.scale() <= 0) {
            return stripped.toBigIntegerExact().toString();
        }
        return stripped.toPlainString();
    }
}
