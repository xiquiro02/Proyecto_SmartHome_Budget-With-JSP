package com.smarthome.smarthome_budget.modelo;

import java.time.LocalDateTime;

public class Usuario {
    private int IDUsuario;
    private String NombreUsuario;
    private String PrimerApellido;
    private String SegundoApellido;
    private String correo;
    private String telefono;
    private String ContrasenaUsuario;
    private LocalDateTime FechaRegistro;
    private String tokenRecuperacion;
    private LocalDateTime tokenExpiracion;
    private String fotoPerfil;

    public String getFotoPerfil() { return fotoPerfil; }
    public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; }

    public Usuario() {
    }

    public Usuario(int IDUsuario, String NombreUsuario, String PrimerApellido, String SegundoApellido, String correo, String telefono, String ContrasenaUsuario, LocalDateTime FechaRegistro, String tokenRecuperacion, LocalDateTime tokenExpiracion) {
        this.IDUsuario = IDUsuario;
        this.NombreUsuario = NombreUsuario;
        this.PrimerApellido = PrimerApellido;
        this.SegundoApellido = SegundoApellido;
        this.correo = correo;
        this.telefono = telefono;
        this.ContrasenaUsuario = ContrasenaUsuario;
        this.FechaRegistro = FechaRegistro;
        this.tokenRecuperacion = tokenRecuperacion;
        this.tokenExpiracion = tokenExpiracion;
    }

    public int getIDUsuario() {
        return IDUsuario;
    }

    public void setIDUsuario(int IDUsuario) {
        this.IDUsuario = IDUsuario;
    }

    public String getNombreUsuario() {
        return NombreUsuario;
    }

    public void setNombreUsuario(String NombreUsuario) {
        this.NombreUsuario = NombreUsuario;
    }

    public String getPrimerApellido() {
        return PrimerApellido;
    }

    public void setPrimerApellido(String PrimerApellido) {
        this.PrimerApellido = PrimerApellido;
    }

    public String getSegundoApellido() {
        return SegundoApellido;
    }

    public void setSegundoApellido(String SegundoApellido) {
        this.SegundoApellido = SegundoApellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContrasenaUsuario() {
        return ContrasenaUsuario;
    }

    public void setContrasenaUsuario(String ContrasenaUsuario) {
        this.ContrasenaUsuario = ContrasenaUsuario;
    }

    public LocalDateTime getFechaRegistro() {
        return FechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime FechaRegistro) {
        this.FechaRegistro = FechaRegistro;
    }

    public String getTokenRecuperacion() {
        return tokenRecuperacion;
    }

    public void setTokenRecuperacion(String tokenRecuperacion) {
        this.tokenRecuperacion = tokenRecuperacion;
    }

    public LocalDateTime getTokenExpiracion() {
        return tokenExpiracion;
    }

    public void setTokenExpiracion(LocalDateTime tokenExpiracion) {
        this.tokenExpiracion = tokenExpiracion;
    }
}