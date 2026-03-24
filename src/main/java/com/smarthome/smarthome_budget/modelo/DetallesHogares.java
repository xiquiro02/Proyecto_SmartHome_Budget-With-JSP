package com.smarthome.smarthome_budget.modelo;

/* Clase: DetallesHogares
   Propósito: Representar la relación entre un usuario, un hogar y su rol dentro de ese hogar
   (tabla Detalles_Hogares). Es la tabla de unión que define la membresía: qué usuarios
   pertenecen a qué hogar y con qué nivel de acceso (Administrador, Cotitular o Invitado).
*/
public class DetallesHogares {

    // Identificador único del registro de membresía en la base de datos
    private int idDetallesHogar;
    // Identificador del usuario miembro del hogar (clave foránea a Usuario)
    private int idUsuario;
    // Identificador del hogar al que pertenece el usuario (clave foránea a Hogar)
    private int idHogar;
    // Identificador del rol asignado al usuario en este hogar (clave foránea a Roles)
    private int idRol;

    /* Constructor vacío requerido para instanciar el objeto antes de asignar valores mediante setters */
    public DetallesHogares() {
    }

    /* Constructor completo para crear el registro de membresía con todos sus datos en una sola instrucción */
    public DetallesHogares(int idDetallesHogar, int idUsuario, int idHogar, int idRol) {
        this.idDetallesHogar = idDetallesHogar;
        this.idUsuario = idUsuario;
        this.idHogar = idHogar;
        this.idRol = idRol;
    }

    public int getIdDetallesHogar() {
        return idDetallesHogar;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public int getIdHogar() {
        return idHogar;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdDetallesHogar(int idDetallesHogar) {
        this.idDetallesHogar = idDetallesHogar;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdHogar(int idHogar) {
        this.idHogar = idHogar;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }
}
