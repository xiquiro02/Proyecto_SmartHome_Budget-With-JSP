package com.smarthome.smarthome_budget.modelo;

/* Clase: MetodoPago
   Propósito: Representar un método de pago del catálogo (tabla Metodo_Pago).
   Se usa para asociar el medio de pago (Efectivo, Tarjeta, etc.) a los egresos registrados
   en el módulo de Facturas y para poblar los selectores en los formularios correspondientes.
*/
public class MetodoPago {

    // Identificador único del método de pago en la base de datos
    private int idMetodoPago;
    // Nombre descriptivo del método de pago (ej.: "Efectivo", "Tarjeta débito")
    private String nombreMetodoPago;

    /* Constructor vacío requerido para instanciar el objeto antes de asignar valores mediante setters */
    public MetodoPago() {
    }

    /* Constructor completo para crear un método de pago con todos sus datos en una sola instrucción */
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
