package com.smarthome.smarthome_budget.modelo;

import java.time.LocalDateTime;


public class Tokens {
    
    private int idtoken;
    private String tokenRecuperacion;
    private LocalDateTime fechaExpiracion;
    private boolean usado;
    private int idUsuario;

    public Tokens() {
    }

    public Tokens(int idtoken, String tokenRecuperacion, LocalDateTime fechaExpiracion, boolean usado, int idUsuario) {
        this.idtoken = idtoken;
        this.tokenRecuperacion = tokenRecuperacion;
        this.fechaExpiracion = fechaExpiracion;
        this.usado = usado;
        this.idUsuario = idUsuario;
    }

    public int getIdtoken() {
        return idtoken;
    }

    public String getTokenRecuperacion() {
        return tokenRecuperacion;
    }

    public LocalDateTime getFechaExpiracion() {
        return fechaExpiracion;
    }

    public boolean isUsado() {
        return usado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdtoken(int idtoken) {
        this.idtoken = idtoken;
    }

    public void setTokenRecuperacion(String tokenRecuperacion) {
        this.tokenRecuperacion = tokenRecuperacion;
    }

    public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public void setUsado(boolean usado) {
        this.usado = usado;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
