package com.smarthome.smarthome_budget.modelo;

public class CategoriaEgreso {
    
    private int idCategoriaEgreso;
    private String nombreCategoriaEgreso;

    // Constructor vacío
    public CategoriaEgreso() {
    }

    // Constructor con todos los atributos
    public CategoriaEgreso(int idCategoriaEgreso, String nombreCategoriaEgreso) {
        this.idCategoriaEgreso = idCategoriaEgreso;
        this.nombreCategoriaEgreso = nombreCategoriaEgreso;
    }

    public int getIdCategoriaEgreso() {
        return idCategoriaEgreso;
    }

    public String getNombreCategoriaEgreso() {
        return nombreCategoriaEgreso;
    }

    public void setIdCategoriaEgreso(int idCategoriaEgreso) {
        this.idCategoriaEgreso = idCategoriaEgreso;
    }

    public void setNombreCategoriaEgreso(String nombreCategoriaEgreso) {
        this.nombreCategoriaEgreso = nombreCategoriaEgreso;
    }
}
