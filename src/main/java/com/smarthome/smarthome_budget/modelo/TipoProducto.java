package com.smarthome.smarthome_budget.modelo;

public class TipoProducto {

    private int idTipoProducto;
    private String nombreTipoProducto;

    public TipoProducto() {}

    public TipoProducto(int idTipoProducto, String nombreTipoProducto) {
        this.idTipoProducto = idTipoProducto;
        this.nombreTipoProducto = nombreTipoProducto;
    }

    // ── Getters / Setters estándar ────────────────────────────────────────────

    public int getIdTipoProducto()              { return idTipoProducto; }
    public void setIdTipoProducto(int v)        { this.idTipoProducto = v; }

    public int getIDTipoProducto()              { return idTipoProducto; }
    public void setIDTipoProducto(int v)        { this.idTipoProducto = v; }

    public String getNombreTipoProducto()       { return nombreTipoProducto; }
    public void setNombreTipoProducto(String v) { this.nombreTipoProducto = v; }
}
