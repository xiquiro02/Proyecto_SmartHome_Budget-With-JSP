package com.smarthome.smarthome_budget.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InventarioCasa {

    private int idInventario;
    private BigDecimal stockActual;                  
    private LocalDateTime fechaActualizacion; 
    private int idProducto;
    private int idHogar;
    // Campos auxiliares de JOIN 
    private String nombreProducto;
    private String descripcion;
    private int idTipoProducto;
    private String nombreTipoProducto;
    
    
    private static final BigDecimal STOCK_MINIMO_ALERTA = new BigDecimal("2.00");

    public InventarioCasa() {
        this.stockActual = BigDecimal.ZERO;
    }

    public InventarioCasa(int idInventario, BigDecimal stockActual, LocalDateTime fechaActualizacion,
                          int idProducto, int idHogar, String nombreProducto, String descripcion, int idTipoProducto, String nombreTipoProducto) {
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

    public BigDecimal getStockActual()             { return stockActual; }
    public void setStockActual(BigDecimal v)       { this.stockActual = (v != null) ? v : BigDecimal.ZERO; }

    public BigDecimal getCantidad()                { return stockActual; }
    public void setCantidad(BigDecimal v)          { this.stockActual = (v != null) ? v : BigDecimal.ZERO; }

    public LocalDateTime getFechaActualizacion()        { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime v)  { this.fechaActualizacion = v; }

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

    public BigDecimal getStockMinimo()             { return STOCK_MINIMO_ALERTA; }
    public void setStockMinimo(int v)       { /* ignorado — valor fijo */ }

    public boolean isAgotado() { 
        return stockActual == null || stockActual.compareTo(BigDecimal.ZERO) <= 0; 
    }
        public boolean isStockBajo() { 
        if (stockActual == null) return false;
        return stockActual.compareTo(BigDecimal.ZERO) > 0 && 
               stockActual.compareTo(STOCK_MINIMO_ALERTA) <= 0; 
    }

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

    public String getFechaActualizacionFormateada() {
        if (fechaActualizacion == null) return "";
        return fechaActualizacion.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    /** Muestra entero si no tiene decimales significativos (1.00 → "1"), decimal si los tiene (1.50 → "1.5") */
    public String getCantidadFormateada() {
        if (stockActual == null) return "0";
        java.math.BigDecimal stripped = stockActual.stripTrailingZeros();
        if (stripped.scale() <= 0) {
            return stripped.toBigIntegerExact().toString();
        }
        return stripped.toPlainString();
    }
}
