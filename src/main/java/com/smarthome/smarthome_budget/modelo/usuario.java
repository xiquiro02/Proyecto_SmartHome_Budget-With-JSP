package com.smarthome.smarthome_budget.modelo;

import java.sql.Timestamp;

// En esta clase definimos qué datos vamos a guardar de cada usuario en el sistema.
public class usuario {
    // Lista de datos que necesitamos para identificar y registrar a cada usuario.
    // Variables para guardar la información personal del usuario.
    private int idUsuario;
    private String nombreUsuario;
    private String primerApellido;
    private String segundoApellido;
    private String correo;
    private String telefono;
    private String contrasenaUsuario;
    private Timestamp fechaRegistro; // Guarda la fecha actual en la que se registra el usuario

    public usuario() {
    }

    // Este es el "Constructor" se usa para crear un usuario nuevo con todos sus
    // datos y asignarle sus datos por primera vez.

    public usuario(String nombreUsuario, String primerApellido, String segundoApellido, String correo,
            String telefono, String contrasenaUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.correo = correo;
        this.telefono = telefono;
        this.contrasenaUsuario = contrasenaUsuario;
    }

    public usuario(int idUsuario, String nombreUsuario, String primerApellido, String segundoApellido,
            String correo, String telefono, String contrasenaUsuario, Timestamp fechaRegistro) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.correo = correo;
        this.telefono = telefono;
        this.contrasenaUsuario = contrasenaUsuario;
        this.fechaRegistro = fechaRegistro;
    }

    // --- GETTERS Estos métodos sirven para LEER, consultar o ver los datos del
    // usuario ---

    public int getIdUsuario() {
        return idUsuario; // Retorna el identificador del usuario
    }

    public String getNombreUsuario() {
        return nombreUsuario; // Retorna el nombre del usuario
    }

    public String getPrimerApellido() {
        return primerApellido; // Retorna el primer apellido del usuario
    }

    public String getSegundoApellido() {
        return segundoApellido; // Retorna el segundo apellido del usuario
    }

    public String getCorreo() {
        return correo; // Retorna el correo del usuario
    }

    public String getTelefono() {
        return telefono; // Retorna el teléfono del usuario
    }

    public String getContrasenaUsuario() {
        return contrasenaUsuario; // Retorna la contraseña del usuario
    }

    public Timestamp getFechaRegistro() {
        return fechaRegistro; // Retorna la fecha de registro del usuario
    }

    // --- SETTERS Estos métodos sirven para MODIFICAR, cambiar o actualizar los
    // datos del usuario ---

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario; // Modifica el identificador del usuario
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario; // Modifica el nombre del usuario
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido; // Modifica el primer apellido del usuario
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido; // Modifica el segundo apellido del usuario
    }

    public void setCorreo(String correo) {
        this.correo = correo; // Modifica el correo del usuario
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono; // Modifica el teléfono del usuario
    }

    public void setContrasenaUsuario(String contrasenaUsuario) {
        this.contrasenaUsuario = contrasenaUsuario; // Modifica la contraseña del usuario
    }

    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro; // Modifica la fecha de registro del usuario
    }
}