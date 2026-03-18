package com.smarthome.smarthome_budget.modelo;

public class Producto {

    private int idProducto;
    private String nombreProducto;
    private String descripcion;
    private int idTipoProducto;

    // Campo auxiliar de JOIN con Tipo_Producto
    private String nombreTipoProducto;

    public Producto() {}

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
