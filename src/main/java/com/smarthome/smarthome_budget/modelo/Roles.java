package com.smarthome.smarthome_budget.modelo;

public class Roles {
    private int IDRol;
    private String NombreRol;
    private String Descripcion;

    public Roles() {
    }

    public Roles(int IDRol, String NombreRol, String Descripcion) {
        this.IDRol = IDRol;
        this.NombreRol = NombreRol;
        this.Descripcion = Descripcion;
    }

    public int getIDRol() {
        return IDRol;
    }

    public void setIDRol(int IDRol) {
        this.IDRol = IDRol;
    }

    public String getNombreRol() {
        return NombreRol;
    }

    public void setNombreRol(String NombreRol) {
        this.NombreRol = NombreRol;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }
}
