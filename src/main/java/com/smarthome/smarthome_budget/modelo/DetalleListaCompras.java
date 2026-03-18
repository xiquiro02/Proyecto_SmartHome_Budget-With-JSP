package com.smarthome.smarthome_budget.modelo;

public class DetalleListaCompras {

    private int idDetalleLista;
    private int cantidad;
    private boolean comprado;
    private int idListaCompras;
    private int idProducto;
    // Campos auxiliares de JOIN
    private String nombreProducto;
    private String nombreTipoProducto;

    public DetalleListaCompras() {}

    public DetalleListaCompras(int idDetalleLista, int cantidad, boolean comprado,
                               int idListaCompras, int idProducto,
                               String nombreProducto, String nombreTipoProducto) {
        this.idDetalleLista = idDetalleLista;
        this.cantidad = cantidad;
        this.comprado = comprado;
        this.idListaCompras = idListaCompras;
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.nombreTipoProducto = nombreTipoProducto;
    }

    // ── Getters / Setters ─────────────────────────────────────────────────────

    public int getIdDetalleLista()          { return idDetalleLista; }
    public void setIdDetalleLista(int v)    { this.idDetalleLista = v; }

    public int getCantidad()                { return cantidad; }
    public void setCantidad(int v)          { this.cantidad = v; }

    public boolean isComprado()             { return comprado; }
    public void setComprado(boolean v)      { this.comprado = v; }

    public int getIdListaCompras()          { return idListaCompras; }
    public void setIdListaCompras(int v)    { this.idListaCompras = v; }

    public int getIdProducto()              { return idProducto; }
    public void setIdProducto(int v)        { this.idProducto = v; }

    public String getNombreProducto()       { return nombreProducto; }
    public void setNombreProducto(String v) { this.nombreProducto = v; }

    public String getNombreTipoProducto()       { return nombreTipoProducto; }
    public void setNombreTipoProducto(String v) { this.nombreTipoProducto = v; }

    public String getTipoProducto()             { return nombreTipoProducto; }
    public void setTipoProducto(String v)       { this.nombreTipoProducto = v; }
}
