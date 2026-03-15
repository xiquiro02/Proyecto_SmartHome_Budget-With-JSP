package com.smarthome.smarthome_budget.modelo;

public class PermisosRoles {
    private String NombrePermiso;
    private String Modulo;
    private int IDPermiso;

    public PermisosRoles() {
    }

    public PermisosRoles(String NombrePermiso, String Modulo, int IDPermiso) {
        this.NombrePermiso = NombrePermiso;
        this.Modulo = Modulo;
        this.IDPermiso = IDPermiso;
    }

    public String getNombrePermiso() {
        return NombrePermiso;
    }

    public String getModulo() {
        return Modulo;
    }
    
    public int getIDPermiso() {
        return IDPermiso;
    }

    public void setNombrePermiso(String NombrePermiso) {
        this.NombrePermiso = NombrePermiso;
    }

    public void setModulo(String Modulo) {
        this.Modulo = Modulo;
    }
    
    public void setIDPermiso(int IDPermiso) {
        this.IDPermiso = IDPermiso;
    }
}
