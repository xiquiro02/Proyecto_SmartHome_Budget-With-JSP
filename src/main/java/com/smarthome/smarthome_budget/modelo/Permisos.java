package com.smarthome.smarthome_budget.modelo;

/* Clase: Permisos
   Propósito: Representar un permiso del sistema (tabla Permisos). Cada permiso define
   una acción específica permitida dentro de un módulo. Los permisos se asocian a roles
   mediante la tabla DetallesPermisos para controlar el acceso granular por funcionalidad.
*/
public class Permisos {

    // Identificador único del permiso en la base de datos
    private int idPermiso;
    // Nombre descriptivo del permiso (ej.: "Crear egreso", "Ver inventario")
    private String nombrePermiso;
    // Nombre del módulo al que pertenece el permiso (ej.: "Facturas", "Inventario")
    private String modulo;

    /* Constructor vacío requerido para instanciar el objeto antes de asignar valores mediante setters */
    public Permisos() {
    }

    /* Constructor completo para crear un permiso con todos sus datos en una sola instrucción */
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
