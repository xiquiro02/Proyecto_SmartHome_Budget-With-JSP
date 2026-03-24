package com.smarthome.smarthome_budget.modelo;

/* Clase: DetallesPermisos
   Propósito: Representar la relación entre un rol y un permiso (tabla DetallesPermisos).
   Es la tabla de unión muchos-a-muchos entre Roles y Permisos. Permite definir qué
   acciones concretas tiene habilitadas cada rol del sistema.
*/
public class DetallesPermisos {

    // Identificador único del registro de relación rol-permiso en la base de datos
    private int idDetallesPermiso;
    // Identificador del rol al que se le asigna el permiso (clave foránea a Roles)
    private int idRol;
    // Identificador del permiso asignado al rol (clave foránea a Permisos)
    private int idPermiso;

    /* Constructor vacío requerido para instanciar el objeto antes de asignar valores mediante setters */
    public DetallesPermisos() {
    }

    /* Constructor completo para crear el registro de relación con todos sus datos en una sola instrucción */
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
