package com.smarthome.smarthome_budget.modelo;

import java.time.LocalDateTime;

public class Usuario {

    private int idUsuario;
    private String documento;
    private String nombreUsuario;
    private String apellido;
    private String correo;
    private String telefono;
    private String fotoPerfil;
    private String contrasenaUsuario;
    private LocalDateTime fechaRegistro;

    public Usuario() {}

    public Usuario(int idUsuario, String documento, String nombreUsuario,
                   String apellido, String correo, String telefono,
                   String fotoPerfil, String contrasenaUsuario,
                   LocalDateTime fechaRegistro) {
        this.idUsuario = idUsuario;
        this.documento = documento;
        this.nombreUsuario = nombreUsuario;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.fotoPerfil = fotoPerfil;
        this.contrasenaUsuario = contrasenaUsuario;
        this.fechaRegistro = fechaRegistro;
    }

    // ── Getters / Setters ─────────────────────────────────────────────────────

    public int getIdUsuario()             { return idUsuario; }
    public void setIdUsuario(int v)       { this.idUsuario = v; }

    public int getIDUsuario()             { return idUsuario; }
    public void setIDUsuario(int v)       { this.idUsuario = v; }

    public String getDocumento()          { return documento; }
    public void setDocumento(String v)    { this.documento = v; }

    public String getNombreUsuario()      { return nombreUsuario; }
    public void setNombreUsuario(String v){ this.nombreUsuario = v; }

    public String getApellido()           { return apellido; }
    public void setApellido(String v)     { this.apellido = v; }

    public String getPrimerApellido()     { return apellido; }
    public void setPrimerApellido(String v){ this.apellido = v; }

    public String getSegundoApellido()    { return ""; }
    public void setSegundoApellido(String v) {
        if (v != null && !v.trim().isEmpty())
            this.apellido = (this.apellido != null ? this.apellido + " " : "") + v.trim();
    }

    public String getCorreo()             { return correo; }
    public void setCorreo(String v)       { this.correo = v; }

    public String getTelefono()           { return telefono; }
    public void setTelefono(String v)     { this.telefono = v; }

    public String getFotoPerfil()         { return fotoPerfil; }
    public void setFotoPerfil(String v)   { this.fotoPerfil = v; }

    public String getContrasenaUsuario()        { return contrasenaUsuario; }
    public void setContrasenaUsuario(String v)  { this.contrasenaUsuario = v; }

    public LocalDateTime getFechaRegistro()         { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime v)   { this.fechaRegistro = v; }

    public String getNombreCompleto() {
        return nombreUsuario + (apellido != null ? " " + apellido : "");
    }
}
