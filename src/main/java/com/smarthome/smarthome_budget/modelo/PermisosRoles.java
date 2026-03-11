package com.smarthome.smarthome_budget.modelo;

public class PermisosRoles {
    private int IDPermiso;
    private String NombrePermiso;
    private String Modulo;

    public PermisosRoles() {
    }

    public PermisosRoles(int IDPermiso, String NombrePermiso, String Modulo) {
        this.IDPermiso = IDPermiso;
        this.NombrePermiso = NombrePermiso;
        this.Modulo = Modulo;
    }

    public int getIDPermiso() {
        return IDPermiso;
    }

    public void setIDPermiso(int IDPermiso) {
        this.IDPermiso = IDPermiso;
    }

    public String getNombrePermiso() {
        return NombrePermiso;
    }

    public void setNombrePermiso(String NombrePermiso) {
        this.NombrePermiso = NombrePermiso;
    }

    public String getModulo() {
        return Modulo;
    }

    public void setModulo(String Modulo) {
        this.Modulo = Modulo;
    }
}
