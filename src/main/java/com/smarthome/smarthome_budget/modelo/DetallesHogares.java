package com.smarthome.smarthome_budget.modelo;

public class DetallesHogares {
    private int IDDetallesHogar;
    private int IDUsuario;
    private int IDHogar;
    private int IDRol;

    public DetallesHogares() {
    }

    public DetallesHogares(int IDDetallesHogar, int IDUsuario, int IDHogar, int IDRol) {
        this.IDDetallesHogar = IDDetallesHogar;
        this.IDUsuario = IDUsuario;
        this.IDHogar = IDHogar;
        this.IDRol = IDRol;
    }

    public int getIDDetallesHogar() {
        return IDDetallesHogar;
    }

    public int getIDUsuario() {
        return IDUsuario;
    }

    public int getIDHogar() {
        return IDHogar;
    }

    public int getIDRol() {
        return IDRol;
    }

    public void setIDDetallesHogar(int IDDetallesHogar) {
        this.IDDetallesHogar = IDDetallesHogar;
    }

    public void setIDUsuario(int IDUsuario) {
        this.IDUsuario = IDUsuario;
    }

    public void setIDHogar(int IDHogar) {
        this.IDHogar = IDHogar;
    }

    public void setIDRol(int IDRol) {
        this.IDRol = IDRol;
    }
}
