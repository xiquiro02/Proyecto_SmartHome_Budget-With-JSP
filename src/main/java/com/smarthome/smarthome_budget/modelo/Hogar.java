package com.smarthome.smarthome_budget.modelo;

public class Hogar {

    private int idHogar;
    private String nombreHogar;

    public Hogar() {}

    public Hogar(int idHogar, String nombreHogar) {
        this.idHogar = idHogar;
        this.nombreHogar = nombreHogar;
    }

    public int getIdHogar()             { return idHogar; }
    public void setIdHogar(int v)       { this.idHogar = v; }

    public int getIDHogar()             { return idHogar; }
    public void setIDHogar(int v)       { this.idHogar = v; }

    public String getNombreHogar()      { return nombreHogar; }
    public void setNombreHogar(String v){ this.nombreHogar = v; }
}
