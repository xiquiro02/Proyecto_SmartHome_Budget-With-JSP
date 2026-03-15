package com.smarthome.smarthome_budget.modelo;

import java.time.LocalDateTime;

public class CodigosInvitacion {
    private String Codigo;
    private LocalDateTime FechaCreacion;
    private LocalDateTime FechaExpiracion;
    private String Estado;
    private int IDCodigo;
    private int IDHogar;
    private int IDRol;


    public CodigosInvitacion() {
    }

    public CodigosInvitacion(String Codigo, LocalDateTime FechaCreacion, LocalDateTime FechaExpiracion, String Estado, int IDCodigo, int IDHogar, int IDRol) {
        this.Codigo = Codigo;
        this.FechaCreacion = FechaCreacion;
        this.FechaExpiracion = FechaExpiracion;
        this.Estado = Estado;
        this.IDCodigo = IDCodigo;
        this.IDHogar = IDHogar;
        this.IDRol = IDRol;
    }

    public String getCodigo() {
        return Codigo;
    }

    public LocalDateTime getFechaCreacion() {
        return FechaCreacion;
    }

    public LocalDateTime getFechaExpiracion() {
        return FechaExpiracion;
    }

    public String getEstado() {
        return Estado;
    }

    public int getIDCodigo() {
        return IDCodigo;
    }

    public int getIDHogar() {
        return IDHogar;
    }

    public int getIDRol() {
        return IDRol;
    }

    public void setCodigo(String Codigo) {
        this.Codigo = Codigo;
    }

    public void setFechaCreacion(LocalDateTime FechaCreacion) {
        this.FechaCreacion = FechaCreacion;
    }

    public void setFechaExpiracion(LocalDateTime FechaExpiracion) {
        this.FechaExpiracion = FechaExpiracion;
    }

    public void setEstado(String Estado) {
        this.Estado = Estado;
    }

    public void setIDCodigo(int IDCodigo) {
        this.IDCodigo = IDCodigo;
    }

    public void setIDHogar(int IDHogar) {
        this.IDHogar = IDHogar;
    }

    public void setIDRol(int IDRol) {
        this.IDRol = IDRol;
    }
}
