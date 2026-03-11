package com.smarthome.smarthome_budget.modelo;

/**
 * Modelo para Detalle_Lista_Compras.
 * Cada fila es un producto dentro de una lista.
 */
public class DetalleListaCompras {
    private int idDetalleLista;
    private int idListaCompras;
    private int idProducto;
    private int cantidad;
    private boolean comprado;

    // campos auxiliares del JOIN con Producto
    private String nombreProducto;
    private String tipoProducto;     // NombreTipoProducto

    public DetalleListaCompras() {}

    public int getIdDetalleLista() { return idDetalleLista; }
    public void setIdDetalleLista(int idDetalleLista) { this.idDetalleLista = idDetalleLista; }
    public int getIdListaCompras() { return idListaCompras; }
    public void setIdListaCompras(int idListaCompras) { this.idListaCompras = idListaCompras; }
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public boolean isComprado() { return comprado; }
    public void setComprado(boolean comprado) { this.comprado = comprado; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public String getTipoProducto() { return tipoProducto; }
    public void setTipoProducto(String tipoProducto) { this.tipoProducto = tipoProducto; }
}
