package com.smarthome.smarthome_budget.modelo;

public class DetallesPermisos {
    private int IDDetallesPermiso;
    private int IDRol;
    private int IDPermiso;

    public DetallesPermisos() {
    }

    public DetallesPermisos(int IDDetallesPermiso, int IDRol, int IDPermiso) {
        this.IDDetallesPermiso = IDDetallesPermiso;
        this.IDRol = IDRol;
        this.IDPermiso = IDPermiso;
    }

    public int getIDDetallesPermiso() {
        return IDDetallesPermiso;
    }

    public void setIDDetallesPermiso(int IDDetallesPermiso) {
        this.IDDetallesPermiso = IDDetallesPermiso;
    }

    public int getIDRol() {
        return IDRol;
    }

    public void setIDRol(int IDRol) {
        this.IDRol = IDRol;
    }

    public int getIDPermiso() {
        return IDPermiso;
    }

    public void setIDPermiso(int IDPermiso) {
        this.IDPermiso = IDPermiso;
    }
}
