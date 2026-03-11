package com.smarthome.smarthome_budget.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RegistroIngreso {
    private int idIngresos;
    private int idHogar;
    private int idUsuario;
    private BigDecimal monto;
    private LocalDateTime fechaIngreso;
    private String descripcion;
    private int idCategoriaIngreso;
    // auxiliares
    private String nombreCategoria;

    public RegistroIngreso() {}

    public int getIdIngresos() { return idIngresos; }
    public void setIdIngresos(int idIngresos) { this.idIngresos = idIngresos; }

    public int getIdHogar() { return idHogar; }
    public void setIdHogar(int idHogar) { this.idHogar = idHogar; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public LocalDateTime getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDateTime fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getIdCategoriaIngreso() { return idCategoriaIngreso; }
    public void setIdCategoriaIngreso(int idCategoriaIngreso) { this.idCategoriaIngreso = idCategoriaIngreso; }

    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }
}
