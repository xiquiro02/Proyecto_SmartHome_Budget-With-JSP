package com.smarthome.smarthome_budget.modelo;

public class Permisos {
    private int idPermiso;
    private String nombrePermiso;
    private String modulo;

    public Permisos() {
    }

    public Permisos(int idPermiso, String nombrePermiso, String modulo) {
        this.idPermiso = idPermiso;
        this.nombrePermiso = nombrePermiso;
        this.modulo = modulo;
    }

    public int getIdPermiso() {
        return idPermiso;
    }

    public String getNombrePermiso() {
        return nombrePermiso;
    }

    public String getModulo() {
        return modulo;
    }

    public void setIdPermiso(int idPermiso) {
        this.idPermiso = idPermiso;
    }

    public void setNombrePermiso(String nombrePermiso) {
        this.nombrePermiso = nombrePermiso;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }
}
