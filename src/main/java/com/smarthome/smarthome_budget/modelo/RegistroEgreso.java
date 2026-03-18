package com.smarthome.smarthome_budget.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RegistroEgreso {

    private int idEgresos;
    private String descripcionPago;       
    private BigDecimal monto;
    private LocalDateTime fechaPago;
    private LocalDateTime fechaVencimiento;
    private String estadoPago;
    private int idHogar;
    private int idCategoriaEgreso;
    private int idMetodoPago;
    //  Campos auxiliares de JOIN
    private String nombreCategoriaEgreso;
    private String nombreMetodoPago;

    public RegistroEgreso() {}

    public RegistroEgreso(int idEgresos, String descripcionPago, BigDecimal monto,
                          LocalDateTime fechaPago, LocalDateTime fechaVencimiento,
                          String estadoPago, int idHogar,
                          int idCategoriaEgreso, String nombreCategoriaEgreso,
                          int idMetodoPago, String nombreMetodoPago) {
        this.idEgresos = idEgresos;
        this.descripcionPago = descripcionPago;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.fechaVencimiento = fechaVencimiento;
        this.estadoPago = estadoPago;
        this.idHogar = idHogar;
        this.idCategoriaEgreso = idCategoriaEgreso;
        this.nombreCategoriaEgreso = nombreCategoriaEgreso;
        this.idMetodoPago = idMetodoPago;
        this.nombreMetodoPago = nombreMetodoPago;
    }

    // ── Getters / Setters ─────────────────────────────────────────────────────

    public int getIdEgresos()             { return idEgresos; }
    public void setIdEgresos(int v)       { this.idEgresos = v; }

    public String getDescripcionPago()          { return descripcionPago; }
    public void setDescripcionPago(String v)    { this.descripcionPago = v; }

    public String getNombreFactura()            { return descripcionPago; }
    public void setNombreFactura(String v)      { this.descripcionPago = v; }

    public String getDescripcion()              { return descripcionPago; }
    public void setDescripcion(String v)        { this.descripcionPago = v; }

    public BigDecimal getMonto()                { return monto; }
    public void setMonto(BigDecimal v)          { this.monto = v; }

    public LocalDateTime getFechaPago()         { return fechaPago; }
    public void setFechaPago(LocalDateTime v)   { this.fechaPago = v; }

    public LocalDateTime getFechaVencimiento()        { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDateTime v)  { this.fechaVencimiento = v; }

    public String getEstadoPago()               { return estadoPago; }
    public void setEstadoPago(String v)         { this.estadoPago = v; }

    public int getIdHogar()                     { return idHogar; }
    public void setIdHogar(int v)               { this.idHogar = v; }

    public int getIdCategoriaEgreso()           { return idCategoriaEgreso; }
    public void setIdCategoriaEgreso(int v)     { this.idCategoriaEgreso = v; }

    public int getIdMetodoPago()                { return idMetodoPago; }
    public void setIdMetodoPago(int v)          { this.idMetodoPago = v; }

    public String getNombreCategoriaEgreso()        { return nombreCategoriaEgreso; }
    public void setNombreCategoriaEgreso(String v)  { this.nombreCategoriaEgreso = v; }

    public String getNombreCategoria()              { return nombreCategoriaEgreso; }
    public void setNombreCategoria(String v)        { this.nombreCategoriaEgreso = v; }

    public String getNombreMetodoPago()         { return nombreMetodoPago; }
    public void setNombreMetodoPago(String v)   { this.nombreMetodoPago = v; }

    public String getFechaVencimientoFormateada() {
        if (fechaVencimiento == null) return "";
        return fechaVencimiento.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    public String getFechaPagoFormateada() {
        if (fechaPago == null) return "";
        return fechaPago.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }
}
