package com.smarthome.smarthome_budget.modelo;

/* Clase: Hogar
   Propósito: Representar un hogar del sistema (tabla Hogar). Cada hogar agrupa a los
   usuarios miembros y centraliza todos sus datos financieros: egresos, ingresos,
   inventario, listas de compras y presupuesto mensual.
   Expone alias getIDHogar / setIDHogar para compatibilidad con expresiones EL en JSPs.
*/
public class Hogar {

    // Identificador único del hogar en la base de datos
    private int idHogar;
    // Nombre del hogar definido por el administrador al crearlo (ej.: "Hogar Familia García")
    private String nombreHogar;

    /* Constructor vacío requerido para instanciar el objeto antes de asignar valores mediante setters */
    public Hogar() {}

    /* Constructor completo para crear un hogar con todos sus datos en una sola instrucción */
    public Hogar(int idHogar, String nombreHogar) {
        this.idHogar = idHogar;
        this.nombreHogar = nombreHogar;
    }

    public int getIdHogar()             { return idHogar; }
    public void setIdHogar(int v)       { this.idHogar = v; }

    // Alias en mayúsculas para compatibilidad con expresiones EL en JSPs (${h.IDHogar})
    public int getIDHogar()             { return idHogar; }
    public void setIDHogar(int v)       { this.idHogar = v; }

    public String getNombreHogar()      { return nombreHogar; }
    public void setNombreHogar(String v){ this.nombreHogar = v; }
}
