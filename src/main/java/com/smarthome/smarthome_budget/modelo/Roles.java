package com.smarthome.smarthome_budget.modelo;

public class Roles {
    private String NombreRol;
    private String Descripcion;
    private int IDRol;

    public Roles() {
    }

    public Roles(String NombreRol, String Descripcion, int IDRol) {
        this.NombreRol = NombreRol;
        this.Descripcion = Descripcion;
        this.IDRol = IDRol;
    }

    public String getNombreRol() {
        return NombreRol;
    }

    public String getDescripcion() {
        return Descripcion;
    }
    
    public int getIDRol() {
        return IDRol;
    }

    public void setNombreRol(String NombreRol) {
        this.NombreRol = NombreRol;
    }
    
    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }
    
    public void setIDRol(int IDRol) {
        this.IDRol = IDRol;
    }
}
