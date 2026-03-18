package com.smarthome.smarthome_budget.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PresupuestoMensual {

    private int idPresupuesto;
    private BigDecimal montoMax;
    private int mes;
    private int anio;
    private LocalDateTime fechaCreacion;
    private int idHogar;
    // ── Campos auxiliares calculados
    private BigDecimal totalEgresos;
    private BigDecimal disponible;

    public PresupuestoMensual() {}

    public PresupuestoMensual(int idPresupuesto, BigDecimal montoMax, int mes, int anio,
                              LocalDateTime fechaCreacion, int idHogar,
                              BigDecimal totalEgresos, BigDecimal disponible) {
        this.idPresupuesto = idPresupuesto;
        this.montoMax = montoMax;
        this.mes = mes;
        this.anio = anio;
        this.fechaCreacion = fechaCreacion;
        this.idHogar = idHogar;
        this.totalEgresos = totalEgresos;
        this.disponible = disponible;
    }

    // ── Getters / Setters ─────────────────────────────────────────────────────

    public int getIdPresupuesto()               { return idPresupuesto; }
    public void setIdPresupuesto(int v)         { this.idPresupuesto = v; }

    public BigDecimal getMontoMax()             { return montoMax; }
    public void setMontoMax(BigDecimal v)       { this.montoMax = v; }

    public int getMes()                         { return mes; }
    public void setMes(int v)                   { this.mes = v; }

    public int getAnio()                        { return anio; }
    public void setAnio(int v)                  { this.anio = v; }

    public LocalDateTime getFechaCreacion()         { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime v)   { this.fechaCreacion = v; }

    public int getIdHogar()                     { return idHogar; }
    public void setIdHogar(int v)               { this.idHogar = v; }

    public BigDecimal getTotalEgresos()         { return totalEgresos; }
    public void setTotalEgresos(BigDecimal v)   { this.totalEgresos = v; }

    public BigDecimal getDisponible()           { return disponible; }
    public void setDisponible(BigDecimal v)     { this.disponible = v; }
    
    /* Nombre del mes en español */
    public String getNombreMes() {
        String[] meses = {"", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                          "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return (mes >= 1 && mes <= 12) ? meses[mes] : "";
    }

    public int getPorcentajeUsado() {
        if (montoMax == null || montoMax.compareTo(BigDecimal.ZERO) == 0 || totalEgresos == null)
            return 0;
        return totalEgresos.multiply(new BigDecimal(100))
                           .divide(montoMax, 0, java.math.RoundingMode.HALF_UP)
                           .intValue();
    }

    public boolean isAlerta80() { return getPorcentajeUsado() >= 80; }

    public boolean isAlertaSuperePresupuesto() { return getPorcentajeUsado() >= 100; }

    public boolean isSuperado() { return getPorcentajeUsado() >= 100; }

    public boolean isCercaDelLimite() { return getPorcentajeUsado() >= 80; }
}
