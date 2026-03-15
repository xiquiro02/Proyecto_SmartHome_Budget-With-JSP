package com.smarthome.smarthome_budget.modelo;

import java.time.LocalDateTime;


public class tokenes {
    private String tokenRecuperacion;
    private LocalDateTime FechaExpiracion;
    private boolean Usado;
    private int IDtoken;
    private int IDUsuario;

    public tokenes() {
    }
    
    public tokenes(String tokenRecuperacion, LocalDateTime FechaExpiracion, boolean Usado, int IDtoken, int IDUsuario) {
        this.tokenRecuperacion = tokenRecuperacion;
        this.FechaExpiracion = FechaExpiracion;
        this.Usado = Usado;
        this.IDtoken = IDtoken;
        this.IDUsuario = IDUsuario;
    }

    public String getTokenRecuperacion() {
        return tokenRecuperacion;
    }

    public LocalDateTime getFechaExpiracion() {
        return FechaExpiracion;
    }

    public boolean isUsado() {
        return Usado;
    }

    public int getIDtoken() {
        return IDtoken;
    }

    public int getIDUsuario() {
        return IDUsuario;
    }

    public void setTokenRecuperacion(String tokenRecuperacion) {
        this.tokenRecuperacion = tokenRecuperacion;
    }

    public void setFechaExpiracion(LocalDateTime FechaExpiracion) {
        this.FechaExpiracion = FechaExpiracion;
    }

    public void setUsado(boolean Usado) {
        this.Usado = Usado;
    }

    public void setIDtoken(int IDtoken) {
        this.IDtoken = IDtoken;
    }

    public void setIDUsuario(int IDUsuario) {
        this.IDUsuario = IDUsuario;
    }
    
    
}
