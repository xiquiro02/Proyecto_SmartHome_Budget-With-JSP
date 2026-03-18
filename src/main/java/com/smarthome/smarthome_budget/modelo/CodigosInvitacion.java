package com.smarthome.smarthome_budget.modelo;

import java.time.LocalDateTime;

public class CodigosInvitacion {

    private int idCodigo;
    private String codigo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaExpiracion;
    private String estado;
    private int idHogar;
    private int idRol;

    public CodigosInvitacion() {}

    public CodigosInvitacion(int idCodigo, String codigo, LocalDateTime fechaCreacion,
                              LocalDateTime fechaExpiracion, String estado,
                              int idHogar, int idRol) {
        this.idCodigo = idCodigo;
        this.codigo = codigo;
        this.fechaCreacion = fechaCreacion;
        this.fechaExpiracion = fechaExpiracion;
        this.estado = estado;
        this.idHogar = idHogar;
        this.idRol = idRol;
    }

    public int getIdCodigo()                { return idCodigo; }
    public void setIdCodigo(int v)          { this.idCodigo = v; }

    public int getIDCodigo()                { return idCodigo; }
    public void setIDCodigo(int v)          { this.idCodigo = v; }

    public String getCodigo()               { return codigo; }
    public void setCodigo(String v)         { this.codigo = v; }

    public LocalDateTime getFechaCreacion()         { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime v)   { this.fechaCreacion = v; }

    public LocalDateTime getFechaExpiracion()       { return fechaExpiracion; }
    public void setFechaExpiracion(LocalDateTime v) { this.fechaExpiracion = v; }

    public String getEstado()               { return estado; }
    public void setEstado(String v)         { this.estado = v; }

    public int getIdHogar()                 { return idHogar; }
    public void setIdHogar(int v)           { this.idHogar = v; }

    public int getIDHogar()                 { return idHogar; }
    public void setIDHogar(int v)           { this.idHogar = v; }

    public int getIdRol()                   { return idRol; }
    public void setIdRol(int v)             { this.idRol = v; }

    public int getIDRol()                   { return idRol; }
    public void setIDRol(int v)             { this.idRol = v; }

    /* true si el código está activo y no ha expirado */
    public boolean isValido() {
        return "Activo".equals(estado) &&
               fechaExpiracion != null &&
               LocalDateTime.now().isBefore(fechaExpiracion);
    }
}
