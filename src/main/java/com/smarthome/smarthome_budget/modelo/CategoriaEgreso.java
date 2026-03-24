package com.smarthome.smarthome_budget.modelo;

/* Clase: CategoriaEgreso
   Propósito: Representar una categoría de egreso del catálogo (tabla Categorias_Egresos).
   Se usa para clasificar los egresos registrados en el módulo de Facturas y Finanzas,
   y para poblar los selectores en los formularios de registro y edición.
*/
public class CategoriaEgreso {

    // Identificador único de la categoría de egreso en la base de datos
    private int idCategoriaEgreso;
    // Nombre descriptivo de la categoría de egreso (ej.: "Servicios", "Alimentación")
    private String nombreCategoriaEgreso;

    /* Constructor vacío requerido para instanciar el objeto antes de asignar valores mediante setters */
    public CategoriaEgreso() {
    }

    /* Constructor completo para crear una categoría con todos sus datos en una sola instrucción */
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
