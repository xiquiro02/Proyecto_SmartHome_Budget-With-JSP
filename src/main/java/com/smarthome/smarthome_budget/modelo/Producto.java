package com.smarthome.smarthome_budget.modelo;

/**
 * Modelo para Producto.
 */
public class Producto {
    private int idProducto;
    private String nombreProducto;
    private String descripcion;
    private int idTipoProducto;
    private String nombreTipoProducto;
    private int stockInicial;
    private int stockMinimo;

    public Producto() {}

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getIdTipoProducto() { return idTipoProducto; }
    public void setIdTipoProducto(int idTipoProducto) { this.idTipoProducto = idTipoProducto; }
    public String getNombreTipoProducto() { return nombreTipoProducto; }
    public void setNombreTipoProducto(String nombreTipoProducto) { this.nombreTipoProducto = nombreTipoProducto; }
    public int getStockInicial() { return stockInicial; }
    public void setStockInicial(int stockInicial) { this.stockInicial = stockInicial; }
    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }
}
