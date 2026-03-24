package com.smarthome.smarthome_budget.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/* Clase: RegistroIngreso
   Propósito: Representar un ingreso registrado por el hogar (tabla Registro_Ingresos).
   Almacena el monto, la fecha de registro, la descripción y la categoría. El campo
   nombreCategoria es auxiliar cargado mediante JOIN para evitar consultas adicionales
   en los listados. El estado del ingreso ('Activo' o 'Anulado') permite ocultar registros
   sin eliminarlos físicamente de la base de datos.
*/
public class RegistroIngreso {

    // Identificador único del ingreso en la base de datos
    private int idIngresos;
    // Monto del ingreso en la moneda del hogar
    private BigDecimal monto;
    // Fecha y hora en que fue registrado el ingreso (se establece con NOW() en el DAO)
    private LocalDateTime fechaIngreso;
    // Descripción o concepto del ingreso (ej.: "Salario febrero", "Arriendo local")
    private String descripcion;
    // Identificador del hogar al que pertenece este ingreso (clave foránea a Hogar)
    private int idHogar;
    // Identificador de la categoría del ingreso (clave foránea a Categorias_Ingresos)
    private int idCategoriaIngreso;
    // Campo auxiliar cargado por JOIN con Categorias_Ingresos para mostrar el nombre sin consulta adicional
    private String nombreCategoria;
    // Estado del ingreso: 'Activo' si está visible, 'Anulado' si fue ocultado; valor predeterminado 'Activo'
    private String estadoIngreso = "Activo";

    /* Constructor vacío requerido para instanciar el objeto antes de asignar valores mediante setters */
    public RegistroIngreso() {}

    /* Constructor parcial para crear un ingreso con los campos principales en una sola instrucción */
    public RegistroIngreso(int idIngresos, BigDecimal monto, LocalDateTime fechaIngreso,
                           String descripcion, int idHogar,
                           int idCategoriaIngreso, String nombreCategoria) {
        this.idIngresos = idIngresos;
        this.monto = monto;
        this.fechaIngreso = fechaIngreso;
        this.descripcion = descripcion;
        this.idHogar = idHogar;
        this.idCategoriaIngreso = idCategoriaIngreso;
        this.nombreCategoria = nombreCategoria;
    }

    // ── Getters / Setters ─────────────────────────────────────────────────────

    public int getIdIngresos()                  { return idIngresos; }
    public void setIdIngresos(int v)            { this.idIngresos = v; }

    public BigDecimal getMonto()                { return monto; }
    public void setMonto(BigDecimal v)          { this.monto = v; }

    public LocalDateTime getFechaIngreso()      { return fechaIngreso; }
    public void setFechaIngreso(LocalDateTime v){ this.fechaIngreso = v; }

    public String getDescripcion()              { return descripcion; }
    public void setDescripcion(String v)        { this.descripcion = v; }

    public int getIdHogar()                     { return idHogar; }
    public void setIdHogar(int v)               { this.idHogar = v; }

    public int getIdCategoriaIngreso()          { return idCategoriaIngreso; }
    public void setIdCategoriaIngreso(int v)    { this.idCategoriaIngreso = v; }

    public String getNombreCategoria()          { return nombreCategoria; }
    public void setNombreCategoria(String v)    { this.nombreCategoria = v; }

    public String getEstadoIngreso()            { return estadoIngreso; }
    public void setEstadoIngreso(String v)      { this.estadoIngreso = v; }

    /* Método: getFechaIngresoFormateada
       Propósito: Obtener la fecha de registro del ingreso en formato legible para las vistas
       (ej.: "15 Mar 2025"). Retorna cadena vacía si la fecha es null.
       @return String → Fecha formateada como "dd MMM yyyy", o "" si no hay fecha
    */
    public String getFechaIngresoFormateada() {
        if (fechaIngreso == null) return "";
        return fechaIngreso.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    /* Método: setIdUsuario
       Propósito: Método generado automáticamente por el IDE que no aplica a este modelo.
       Lanza UnsupportedOperationException para evidenciar que no debe usarse.
       @param idUsuario → Parámetro no utilizado
    */
    public void setIdUsuario(int idUsuario) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
