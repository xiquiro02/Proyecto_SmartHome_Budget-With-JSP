package com.smarthome.smarthome_budget.modelo;

public class Hogar {
    private int IDHogar;
    private String NombreHogar;

    public Hogar() {
    }

    public Hogar(int IDHogar, String NombreHogar) {
        this.IDHogar = IDHogar;
        this.NombreHogar = NombreHogar;
    }

    public int getIDHogar() {
        return IDHogar;
    }

    public void setIDHogar(int IDHogar) {
        this.IDHogar = IDHogar;
    }

    public String getNombreHogar() {
        return NombreHogar;
    }

    public void setNombreHogar(String NombreHogar) {
        this.NombreHogar = NombreHogar;
    }
}
