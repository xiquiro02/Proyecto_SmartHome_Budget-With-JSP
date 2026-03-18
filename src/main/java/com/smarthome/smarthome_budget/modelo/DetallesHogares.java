package com.smarthome.smarthome_budget.modelo;

public class DetallesHogares {
    private int idDetallesHogar;
    private int idUsuario;
    private int idHogar;
    private int idRol;

    public DetallesHogares() {
    }

    public DetallesHogares(int idDetallesHogar, int idUsuario, int idHogar, int idRol) {
        this.idDetallesHogar = idDetallesHogar;
        this.idUsuario = idUsuario;
        this.idHogar = idHogar;
        this.idRol = idRol;
    }

    public int getIdDetallesHogar() {
        return idDetallesHogar;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public int getIdHogar() {
        return idHogar;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdDetallesHogar(int idDetallesHogar) {
        this.idDetallesHogar = idDetallesHogar;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdHogar(int idHogar) {
        this.idHogar = idHogar;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }
}
