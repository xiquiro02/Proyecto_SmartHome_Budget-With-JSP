package com.smarthome.smarthome_budget.modelo;

import java.time.LocalDateTime;

public class Usuario {
    private int Documento;
    private String NombreUsuario;
    private String Apellido;
    private String correo;
    private String telefono;
    private String fotoPerfil;
    private String ContrasenaUsuario;
    private LocalDateTime FechaRegistro;
    private int IDUsuario;

    public Usuario() {
    }

    public Usuario(int Documento, String NombreUsuario, String Apellido, String correo, String telefono, String fotoPerfil, String ContrasenaUsuario, LocalDateTime FechaRegistro, int IDUsuario) {
        this.Documento = Documento;
        this.NombreUsuario = NombreUsuario;
        this.Apellido = Apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.fotoPerfil = fotoPerfil;
        this.ContrasenaUsuario = ContrasenaUsuario;
        this.FechaRegistro = FechaRegistro;
        this.IDUsuario = IDUsuario;
    }

    public int getDocumento() {
        return Documento;
    }

    public String getNombreUsuario() {
        return NombreUsuario;
    }

    public String getApellido() {
        return Apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public String getContrasenaUsuario() {
        return ContrasenaUsuario;
    }

    public LocalDateTime getFechaRegistro() {
        return FechaRegistro;
    }

    public int getIDUsuario() {
        return IDUsuario;
    }

    public void setDocumento(int Documento) {
        this.Documento = Documento;
    }

    public void setNombreUsuario(String NombreUsuario) {
        this.NombreUsuario = NombreUsuario;
    }

    public void setApellido(String Apellido) {
        this.Apellido = Apellido;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public void setContrasenaUsuario(String ContrasenaUsuario) {
        this.ContrasenaUsuario = ContrasenaUsuario;
    }

    public void setFechaRegistro(LocalDateTime FechaRegistro) {
        this.FechaRegistro = FechaRegistro;
    }

    public void setIDUsuario(int IDUsuario) {
        this.IDUsuario = IDUsuario;
    }
}