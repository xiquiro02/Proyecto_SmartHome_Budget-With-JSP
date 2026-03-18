package com.smarthome.smarthome_budget.modelo;

public class DetallesPermisos {
    private int idDetallesPermiso;
    private int idRol;
    private int idPermiso;

    public DetallesPermisos() {
    }

    public DetallesPermisos(int idDetallesPermiso, int idRol, int idPermiso) {
        this.idDetallesPermiso = idDetallesPermiso;
        this.idRol = idRol;
        this.idPermiso = idPermiso;
    }

    public int getIdDetallesPermiso() {
        return idDetallesPermiso;
    }

    public int getIdRol() {
        return idRol;
    }

    public int getIdPermiso() {
        return idPermiso;
    }

    public void setIdDetallesPermiso(int idDetallesPermiso) {
        this.idDetallesPermiso = idDetallesPermiso;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public void setIdPermiso(int idPermiso) {
        this.idPermiso = idPermiso;
    }
}
