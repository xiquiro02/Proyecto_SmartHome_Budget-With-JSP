package com.smarthome.smarthome_budget.modelo;

import java.time.LocalDateTime;

/* Clase: Usuario
   Propósito: Representar un usuario registrado en el sistema (tabla Usuario). Almacena los
   datos personales, de contacto, la contraseña encriptada y la foto de perfil. Se guarda
   en sesión tras autenticarse y se usa en todos los módulos para verificar identidad y rol.
   Expone alias getIDUsuario / getPrimerApellido para compatibilidad con código existente,
   y setSegundoApellido para formularios de registro que separan los apellidos en dos campos.
*/
public class Usuario {

    // Identificador único del usuario en la base de datos
    private int idUsuario;
    // Número de documento de identidad del usuario (cédula, pasaporte, etc.)
    private String documento;
    // Nombre o nombres del usuario
    private String nombreUsuario;
    // Apellido o apellidos del usuario almacenados en un solo campo
    private String apellido;
    // Correo electrónico del usuario, usado como identificador de inicio de sesión
    private String correo;
    // Número de teléfono de contacto del usuario
    private String telefono;
    // Ruta relativa de la foto de perfil; null si el usuario no ha subido una imagen
    private String fotoPerfil;
    // Contraseña del usuario almacenada como hash bcrypt mediante Encriptador
    private String contrasenaUsuario;
    // Fecha y hora en que el usuario se registró en el sistema
    private LocalDateTime fechaRegistro;

    /* Constructor vacío requerido para instanciar el objeto antes de asignar valores mediante setters */
    public Usuario() {}

    /* Constructor completo para crear un usuario con todos sus datos en una sola instrucción */
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

    // Alias en mayúsculas para compatibilidad con expresiones EL en JSPs (${u.IDUsuario})
    public int getIDUsuario()             { return idUsuario; }
    public void setIDUsuario(int v)       { this.idUsuario = v; }

    public String getDocumento()          { return documento; }
    public void setDocumento(String v)    { this.documento = v; }

    public String getNombreUsuario()      { return nombreUsuario; }
    public void setNombreUsuario(String v){ this.nombreUsuario = v; }

    public String getApellido()           { return apellido; }
    public void setApellido(String v)     { this.apellido = v; }

    // Alias para formularios y código que acceden al apellido como "primerApellido"
    public String getPrimerApellido()     { return apellido; }
    public void setPrimerApellido(String v){ this.apellido = v; }

    /* Método: setSegundoApellido
       Propósito: Recibir el segundo apellido del formulario de registro y concatenarlo al
       campo apellido si no está vacío. Permite que formularios con dos campos de apellido
       almacenen ambos valores en el único campo apellido del modelo.
       @param v → Texto con el segundo apellido; se ignora si es null o vacío
    */
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

    /* Método: getNombreCompleto
       Propósito: Obtener el nombre completo del usuario concatenando el nombre y el apellido.
       Si el apellido es null, retorna únicamente el nombre.
       @return String → Nombre completo del usuario en formato "Nombre Apellido"
    */
    public String getNombreCompleto() {
        return nombreUsuario + (apellido != null ? " " + apellido : "");
    }
}
