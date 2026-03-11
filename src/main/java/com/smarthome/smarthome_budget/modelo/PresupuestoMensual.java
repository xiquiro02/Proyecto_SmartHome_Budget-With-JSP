package com.smarthome.smarthome_budget.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PresupuestoMensual {
    private int idPresupuesto;
    private int idHogar;
    private BigDecimal montoMax;
    private int mes;
    private int anio;
    private boolean alerta80;
    private boolean alertaSuperePresupuesto;
    private LocalDateTime fechaCreacion;
    // auxiliares
    private BigDecimal totalEgresos;
    private BigDecimal disponible;

    public PresupuestoMensual() {}

    public int getIdPresupuesto() { return idPresupuesto; }
    public void setIdPresupuesto(int idPresupuesto) { this.idPresupuesto = idPresupuesto; }

    public int getIdHogar() { return idHogar; }
    public void setIdHogar(int idHogar) { this.idHogar = idHogar; }

    public BigDecimal getMontoMax() { return montoMax; }
    public void setMontoMax(BigDecimal montoMax) { this.montoMax = montoMax; }

    public int getMes() { return mes; }
    public void setMes(int mes) { this.mes = mes; }

    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }

    public boolean isAlerta80() { return alerta80; }
    public void setAlerta80(boolean alerta80) { this.alerta80 = alerta80; }

    public boolean isAlertaSuperePresupuesto() { return alertaSuperePresupuesto; }
    public void setAlertaSuperePresupuesto(boolean alertaSuperePresupuesto) { this.alertaSuperePresupuesto = alertaSuperePresupuesto; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public BigDecimal getTotalEgresos() { return totalEgresos; }
    public void setTotalEgresos(BigDecimal totalEgresos) { this.totalEgresos = totalEgresos; }

    public BigDecimal getDisponible() { return disponible; }
    public void setDisponible(BigDecimal disponible) { this.disponible = disponible; }

    /** Nombre del mes en español */
    public String getNombreMes() {
        String[] meses = {"", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                          "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return (mes >= 1 && mes <= 12) ? meses[mes] : "";
    }

    /** Porcentaje gastado respecto al presupuesto (0-100+) */
    public int getPorcentajeUsado() {
        if (montoMax == null || montoMax.compareTo(BigDecimal.ZERO) == 0 || totalEgresos == null) return 0;
        return totalEgresos.multiply(new BigDecimal(100)).divide(montoMax, 0, java.math.RoundingMode.HALF_UP).intValue();
    }

    /** true si superó el 80% */
    public boolean isCercaDelLimite() { return getPorcentajeUsado() >= 80; }

    /** true si superó el 100% */
    public boolean isSuperado() { return getPorcentajeUsado() >= 100; }
}
