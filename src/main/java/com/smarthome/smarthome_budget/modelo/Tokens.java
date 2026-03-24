package com.smarthome.smarthome_budget.modelo;

import java.time.LocalDateTime;

/* Clase: Tokens
   Propósito: Representar un token de recuperación de contraseña (tabla Tokens).
   Cada token es un valor único de texto generado externamente, válido por un tiempo limitado
   (20 minutos) y asociado a un usuario. Una vez utilizado para restablecer la contraseña,
   el campo 'usado' se marca como true y el token deja de ser válido.
*/
public class Tokens {

    // Identificador único del token en la base de datos
    private int idtoken;
    // Valor único del token de recuperación enviado al correo del usuario
    private String tokenRecuperacion;
    // Fecha y hora en que el token deja de ser válido (generalmente NOW + 20 minutos)
    private LocalDateTime fechaExpiracion;
    // Indicador de si el token ya fue utilizado para restablecer la contraseña
    private boolean usado;
    // Identificador del usuario al que pertenece este token (clave foránea a Usuario)
    private int idUsuario;

    /* Constructor vacío requerido para instanciar el objeto antes de asignar valores mediante setters */
    public Tokens() {
    }

    /* Constructor completo para crear un token con todos sus datos en una sola instrucción */
    public Tokens(int idtoken, String tokenRecuperacion, LocalDateTime fechaExpiracion,
                  boolean usado, int idUsuario) {
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
