package com.smarthome.smarthome_budget.modelo;

import java.time.LocalDateTime;

public class CodigosInvitacion {
    private int IDCodigo;
    private String Codigo;
    private int IDHogar;
    private int IDRol;
    private LocalDateTime FechaCreacion;
    private LocalDateTime FechaExpiracion;
    private String Estado;

    public CodigosInvitacion() {
    }

    public CodigosInvitacion(int IDCodigo, String Codigo, int IDHogar, int IDRol, LocalDateTime FechaCreacion, LocalDateTime FechaExpiracion, String Estado) {
        this.IDCodigo = IDCodigo;
        this.Codigo = Codigo;
        this.IDHogar = IDHogar;
        this.IDRol = IDRol;
        this.FechaCreacion = FechaCreacion;
        this.FechaExpiracion = FechaExpiracion;
        this.Estado = Estado;
    }

    public int getIDCodigo() {
        return IDCodigo;
    }

    public void setIDCodigo(int IDCodigo) {
        this.IDCodigo = IDCodigo;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String Codigo) {
        this.Codigo = Codigo;
    }

    public int getIDHogar() {
        return IDHogar;
    }

    public void setIDHogar(int IDHogar) {
        this.IDHogar = IDHogar;
    }

    public int getIDRol() {
        return IDRol;
    }

    public void setIDRol(int IDRol) {
        this.IDRol = IDRol;
    }

    public LocalDateTime getFechaCreacion() {
        return FechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime FechaCreacion) {
        this.FechaCreacion = FechaCreacion;
    }

    public LocalDateTime getFechaExpiracion() {
        return FechaExpiracion;
    }

    public void setFechaExpiracion(LocalDateTime FechaExpiracion) {
        this.FechaExpiracion = FechaExpiracion;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String Estado) {
        this.Estado = Estado;
    }
}
