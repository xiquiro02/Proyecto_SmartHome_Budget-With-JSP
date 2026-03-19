package com.smarthome.smarthome_budget.modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DetalleListaCompras {

    private int idDetalleLista;
    private BigDecimal cantidadDecimal;
    private boolean comprado;
    private int idListaCompras;
    private int idProducto;
    private String nombreProducto;
    private String nombreTipoProducto;

    public DetalleListaCompras() {
        this.cantidadDecimal = BigDecimal.ONE;
    }

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

    // ── Getters / Setters ──────────────────────────────────────────────────

    public int getIdDetalleLista()       { return idDetalleLista; }
    public void setIdDetalleLista(int v) { this.idDetalleLista = v; }

    public BigDecimal getCantidadDecimal()         { return cantidadDecimal != null ? cantidadDecimal : BigDecimal.ONE; }
    public void setCantidadDecimal(BigDecimal v)   { this.cantidadDecimal = (v != null) ? v : BigDecimal.ONE; }

    /** Compatibilidad con código existente que usa int */
    public int getCantidad()        { return cantidadDecimal != null ? cantidadDecimal.intValue() : 1; }
    public void setCantidad(int v)  { this.cantidadDecimal = new BigDecimal(v); }

    /**
     * Retorna la cantidad como texto legible:
     * si es entero exacto muestra sin decimales, si tiene fracción muestra hasta 2 decimales.
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

    public String getTipoProducto()              { return nombreTipoProducto; }
    public void setTipoProducto(String v)        { this.nombreTipoProducto = v; }
}
