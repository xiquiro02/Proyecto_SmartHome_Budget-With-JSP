package com.smarthome.smarthome_budget.modelo;

/* Clase: Roles
   Propósito: Representar un rol de usuario del sistema (tabla Roles). Los roles definen
   el nivel de acceso de cada miembro del hogar: 1=Administrador, 2=Cotitular, 3=Invitado.
   Expone alias getIDRol / setIDRol para compatibilidad con expresiones EL en JSPs.
*/
public class Roles {

    // Identificador único del rol en la base de datos (1=Administrador, 2=Cotitular, 3=Invitado)
    private int idRol;
    // Nombre del rol (ej.: "Administrador", "Cotitular", "Invitado")
    private String nombreRol;
    // Descripción textual del alcance y permisos del rol
    private String descripcion;

    /* Constructor vacío requerido para instanciar el objeto antes de asignar valores mediante setters */
    public Roles() {}

    /* Constructor completo para crear un rol con todos sus datos en una sola instrucción */
    public Roles(int idRol, String nombreRol, String descripcion) {
        this.idRol = idRol;
        this.nombreRol = nombreRol;
        this.descripcion = descripcion;
    }

    public int getIdRol()               { return idRol; }
    public void setIdRol(int v)         { this.idRol = v; }

    // Alias en mayúsculas para compatibilidad con expresiones EL en JSPs (${r.IDRol})
    public int getIDRol()               { return idRol; }
    public void setIDRol(int v)         { this.idRol = v; }

    public String getNombreRol()        { return nombreRol; }
    public void setNombreRol(String v)  { this.nombreRol = v; }

    public String getDescripcion()      { return descripcion; }
    public void setDescripcion(String v){ this.descripcion = v; }
}
