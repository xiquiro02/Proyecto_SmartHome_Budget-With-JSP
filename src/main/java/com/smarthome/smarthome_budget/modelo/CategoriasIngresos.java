package com.smarthome.smarthome_budget.modelo;

/* Clase: CategoriasIngresos
   Propósito: Representar una categoría de ingreso del catálogo (tabla Categorias_Ingresos).
   Se usa para clasificar los ingresos registrados en el módulo de Finanzas y para poblar
   los selectores en los formularios de registro y edición de ingresos.
*/
public class CategoriasIngresos {

    // Identificador único de la categoría de ingreso en la base de datos
    private int idCategoriaIngreso;
    // Nombre descriptivo de la categoría de ingreso (ej.: "Salario", "Arriendo")
    private String nombreCategoriaIngreso;

    /* Constructor vacío requerido para instanciar el objeto antes de asignar valores mediante setters */
    public CategoriasIngresos() {
    }

    /* Constructor completo para crear una categoría con todos sus datos en una sola instrucción */
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
