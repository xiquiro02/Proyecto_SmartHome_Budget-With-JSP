package com.smarthome.smarthome_budget.modelo;

public class Roles {

    private int idRol;
    private String nombreRol;
    private String descripcion;

    public Roles() {}

    public Roles(int idRol, String nombreRol, String descripcion) {
        this.idRol = idRol;
        this.nombreRol = nombreRol;
        this.descripcion = descripcion;
    }

    public int getIdRol()               { return idRol; }
    public void setIdRol(int v)         { this.idRol = v; }

    public int getIDRol()               { return idRol; }
    public void setIDRol(int v)         { this.idRol = v; }

    public String getNombreRol()        { return nombreRol; }
    public void setNombreRol(String v)  { this.nombreRol = v; }

    public String getDescripcion()      { return descripcion; }
    public void setDescripcion(String v){ this.descripcion = v; }
}
