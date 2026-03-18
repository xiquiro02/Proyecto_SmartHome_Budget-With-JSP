package com.smarthome.smarthome_budget.modelo;

public class CategoriasIngresos {
    
    private int idCategoriaIngreso;
    private String nombreCategoriaIngreso;

    public CategoriasIngresos(){
        
    }
    
    public CategoriasIngresos(int idCategoriaIngreso, String nombreCategoriaIngreso) {
        this.idCategoriaIngreso = idCategoriaIngreso;
        this.nombreCategoriaIngreso = nombreCategoriaIngreso;
    }

    public int getIdCategoriaIngreso() {
        return idCategoriaIngreso;
    }

    public String getNombreCategoriaIngreso() {
        return nombreCategoriaIngreso;
    }

    public void setIdCategoriaIngreso(int idCategoriaIngreso) {
        this.idCategoriaIngreso = idCategoriaIngreso;
    }

    public void setNombreCategoriaIngreso(String nombreCategoriaIngreso) {
        this.nombreCategoriaIngreso = nombreCategoriaIngreso;
    }
}
