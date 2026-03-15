package com.smarthome.smarthome_budget.modelo;

public class MetodoPago {
    
    private int idMetodoPago;
    private String nombreMetodoPago;

    // Constructor vacío
    public MetodoPago() {
    }

    // Constructor con todos los atributos
    public MetodoPago(int idMetodoPago, String nombreMetodoPago) {
        this.idMetodoPago = idMetodoPago;
        this.nombreMetodoPago = nombreMetodoPago;
    }

    public int getIdMetodoPago() {
        return idMetodoPago;
    }

    public String getNombreMetodoPago() {
        return nombreMetodoPago;
    }

    public void setIdMetodoPago(int idMetodoPago) {
        this.idMetodoPago = idMetodoPago;
    }

    public void setNombreMetodoPago(String nombreMetodoPago) {
        this.nombreMetodoPago = nombreMetodoPago;
    }
}
