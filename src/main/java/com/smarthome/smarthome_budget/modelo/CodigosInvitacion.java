package com.smarthome.smarthome_budget.modelo;

import java.time.LocalDateTime;

/* Clase: CodigosInvitacion
   Propósito: Representar un código de invitación generado para unirse a un hogar
   (tabla Codigos_Invitacion). El administrador genera un código con rol y hogar predefinidos;
   el invitado lo usa al registrarse para quedar vinculado automáticamente. El código tiene
   vigencia limitada y un estado ('Activo' o 'Usado') que impide su reutilización.
   Expone alias getIDCodigo / getIDHogar / getIDRol para compatibilidad con expresiones EL en JSPs.
*/
public class CodigosInvitacion {

    // Identificador único del código de invitación en la base de datos
    private int idCodigo;
    // Texto del código único generado aleatoriamente (ej.: "AB3X9K")
    private String codigo;
    // Fecha y hora en que fue generado el código
    private LocalDateTime fechaCreacion;
    // Fecha y hora límite para usar el código; después de este momento deja de ser válido
    private LocalDateTime fechaExpiracion;
    // Estado actual del código: 'Activo' si aún puede usarse, 'Usado' si ya fue canjeado
    private String estado;
    // Identificador del hogar al que se unirá el usuario que use este código (clave foránea a Hogar)
    private int idHogar;
    // Identificador del rol que se asignará al usuario al unirse con este código (clave foránea a Roles)
    private int idRol;

    /* Constructor vacío requerido para instanciar el objeto antes de asignar valores mediante setters */
    public CodigosInvitacion() {}

    /* Constructor completo para crear un código de invitación con todos sus datos en una sola instrucción */
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

    // Alias en mayúsculas para compatibilidad con expresiones EL en JSPs (${c.IDCodigo})
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

    // Alias en mayúsculas para compatibilidad con expresiones EL en JSPs (${c.IDHogar})
    public int getIDHogar()                 { return idHogar; }
    public void setIDHogar(int v)           { this.idHogar = v; }

    public int getIdRol()                   { return idRol; }
    public void setIdRol(int v)             { this.idRol = v; }

    // Alias en mayúsculas para compatibilidad con expresiones EL en JSPs (${c.IDRol})
    public int getIDRol()                   { return idRol; }
    public void setIDRol(int v)             { this.idRol = v; }

    /* Método: isValido
       Propósito: Verificar si el código puede ser utilizado para unirse al hogar.
       Un código es válido únicamente si su estado es 'Activo' y su fecha de expiración
       aún no ha llegado según el momento actual.
       @return boolean → true si el código está activo y no ha expirado, false en cualquier otro caso
    */
    public boolean isValido() {
        return "Activo".equals(estado) &&
               fechaExpiracion != null &&
               LocalDateTime.now().isBefore(fechaExpiracion);
    }
}
