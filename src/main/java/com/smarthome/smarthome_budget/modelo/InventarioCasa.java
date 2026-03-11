package com.smarthome.smarthome_budget.modelo;

import java.time.LocalDateTime;

/**
 * Modelo para Inventario_Casa.
 * Representa un producto registrado en el hogar con su stock actual.
 */
public class InventarioCasa {
    private int idInventario;
    private int idProducto;
    private int idHogar;
    private int cantidad;
    private LocalDateTime fechaRegistro;

    // Campos auxiliares (JOIN con Producto y Tipo_Producto)
    private String nombreProducto;
    private String descripcion;
    private int idTipoProducto;
    private String nombreTipoProducto;
    private int stockMinimo;  // alerta si cantidad <= stockMinimo

    public InventarioCasa() {}

    // ─── Getters y Setters ────────────────────────────────────────────────────

    public int getIdInventario() { return idInventario; }
    public void setIdInventario(int idInventario) { this.idInventario = idInventario; }
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    public int getIdHogar() { return idHogar; }
    public void setIdHogar(int idHogar) { this.idHogar = idHogar; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getIdTipoProducto() { return idTipoProducto; }
    public void setIdTipoProducto(int idTipoProducto) { this.idTipoProducto = idTipoProducto; }
    public String getNombreTipoProducto() { return nombreTipoProducto; }
    public void setNombreTipoProducto(String nombreTipoProducto) { this.nombreTipoProducto = nombreTipoProducto; }
    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }

    /** Retorna true si el stock actual es <= al stock mínimo (alerta de reposición). */
    public boolean isStockBajo() { return cantidad <= stockMinimo; }

    /** Retorna true si el producto está agotado (cantidad == 0). */
    public boolean isAgotado() { return cantidad == 0; }

    /** Color CSS según tipo de producto (para tarjetas). */
    public String getColorCSS() {
        if (nombreTipoProducto == null) return "gris";
        switch (nombreTipoProducto.toLowerCase()) {
            case "alimentos":    return "verde";
            case "aseo":         return "azul";
            case "personal":     return "morado";
            case "ropa y calzado": return "naranja";
            default:             return "gris";
        }
    }

    /** Icono según tipo. */
    public String getIconoProducto() {
        if (nombreTipoProducto == null) return "cajas.png";
        switch (nombreTipoProducto.toLowerCase()) {
            case "alimentos":    return "alimentos-saludables.png";
            case "aseo":         return "Aseo.png";
            case "personal":     return "maquillaje.png";
            case "ropa y calzado": return "Ropa-calzado.jpg";
            default:             return "cajas.png";
        }
    }
}
