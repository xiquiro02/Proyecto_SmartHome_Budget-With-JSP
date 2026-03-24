package com.smarthome.smarthome_budget.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/* Clase: PresupuestoMensual
   Propósito: Representar el presupuesto mensual configurado por el hogar (tabla Presupuesto_Mensual).
   Almacena el monto máximo de gasto permitido para un mes y año específicos. Los campos
   totalEgresos y disponible son auxiliares calculados en el servlet (no vienen del DAO)
   para mostrar en las vistas cuánto se ha gastado y cuánto queda disponible.
   Incluye métodos de cálculo y alerta para controlar visualmente el consumo del presupuesto.
*/
public class PresupuestoMensual {

    // Identificador único del presupuesto mensual en la base de datos
    private int idPresupuesto;
    // Monto máximo de gasto permitido para el mes configurado
    private BigDecimal montoMax;
    // Número del mes al que aplica este presupuesto (1=Enero, 12=Diciembre)
    private int mes;
    // Año al que aplica este presupuesto
    private int anio;
    // Fecha y hora en que fue creado o actualizado el presupuesto
    private LocalDateTime fechaCreacion;
    // Identificador del hogar al que pertenece este presupuesto (clave foránea a Hogar)
    private int idHogar;
    // Campo auxiliar: suma de egresos pagados del mes, calculado por el servlet para comparar contra montoMax
    private BigDecimal totalEgresos;
    // Campo auxiliar: dinero disponible restante (montoMax - totalEgresos), calculado por el servlet
    private BigDecimal disponible;

    /* Constructor vacío requerido para instanciar el objeto antes de asignar valores mediante setters */
    public PresupuestoMensual() {}

    /* Constructor completo para crear un presupuesto con todos sus datos en una sola instrucción */
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

    /* Método: getNombreMes
       Propósito: Obtener el nombre del mes en español para mostrarlo en las vistas.
       @return String → Nombre del mes (ej.: "Marzo"), o cadena vacía si el número de mes es inválido
    */
    public String getNombreMes() {
        String[] meses = {"", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                          "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return (mes >= 1 && mes <= 12) ? meses[mes] : "";
    }

    /* Método: getPorcentajeUsado
       Propósito: Calcular el porcentaje del presupuesto ya consumido (totalEgresos / montoMax * 100).
       Retorna 0 si montoMax es cero o null, o si totalEgresos es null.
       @return int → Porcentaje consumido entre 0 y N (puede superar 100 si hay sobregasto)
    */
    public int getPorcentajeUsado() {
        if (montoMax == null || montoMax.compareTo(BigDecimal.ZERO) == 0 || totalEgresos == null)
            return 0;
        return totalEgresos.multiply(new BigDecimal(100))
                           .divide(montoMax, 0, java.math.RoundingMode.HALF_UP)
                           .intValue();
    }

    /* Método: isAlerta80
       Propósito: Indicar si el gasto ha alcanzado o superado el 80% del presupuesto,
       para mostrar una alerta visual preventiva en las vistas.
       @return boolean → true si el porcentaje usado es 80% o más
    */
    public boolean isAlerta80() { return getPorcentajeUsado() >= 80; }

    /* Método: isAlertaSuperePresupuesto
       Propósito: Indicar si el gasto ha igualado o superado el 100% del presupuesto,
       para mostrar una alerta crítica de sobregasto en las vistas.
       @return boolean → true si el porcentaje usado es 100% o más
    */
    public boolean isAlertaSuperePresupuesto() { return getPorcentajeUsado() >= 100; }

    /* Método: isSuperado
       Propósito: Alias de isAlertaSuperePresupuesto() para usarse en expresiones EL más cortas.
       @return boolean → true si el presupuesto ha sido igualado o superado
    */
    public boolean isSuperado() { return getPorcentajeUsado() >= 100; }

    /* Método: isCercaDelLimite
       Propósito: Alias de isAlerta80() para usarse en expresiones EL más descriptivas.
       @return boolean → true si el gasto está al 80% o más del presupuesto máximo
    */
    public boolean isCercaDelLimite() { return getPorcentajeUsado() >= 80; }
}
