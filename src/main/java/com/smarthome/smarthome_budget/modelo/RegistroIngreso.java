package com.smarthome.smarthome_budget.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RegistroIngreso {

    private int idIngresos;
    private BigDecimal monto;
    private LocalDateTime fechaIngreso;
    private String descripcion;
    private int idHogar;
    private int idCategoriaIngreso;
    // Campo auxiliar de JOIN 
    private String nombreCategoria;

    public RegistroIngreso() {}

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

    public String getFechaIngresoFormateada() {
        if (fechaIngreso == null) return "";
        return fechaIngreso.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    public void setIdUsuario(int idUsuario) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
