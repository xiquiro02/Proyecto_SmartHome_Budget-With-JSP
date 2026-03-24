package com.smarthome.smarthome_budget.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/* Clase: RegistroEgreso
   Propósito: Representar un egreso (factura o gasto) registrado por el hogar
   (tabla Registro_Egresos). Almacena la descripción, el monto, las fechas de vencimiento
   y pago, el estado de pago y las referencias a categoría y método de pago. Los campos
   nombreCategoriaEgreso y nombreMetodoPago son auxiliares cargados mediante JOIN.
   Expone alias de compatibilidad para JSPs y servlets (getDescripcion, getNombreCategoria).
   El estadoEgreso ('Activo'/'Anulado') permite ocultar registros sin eliminarlos físicamente.
   Incluye métodos de formato para los tres tipos de fecha que usan las vistas.
*/
public class RegistroEgreso {

    // Identificador único del egreso en la base de datos
    private int idEgresos;
    // Descripción o concepto del pago (ej.: "Factura de luz marzo", "Arriendo")
    private String descripcionPago;
    // Monto del egreso en la moneda del hogar
    private BigDecimal monto;
    // Fecha y hora en que se realizó el pago; null si aún no ha sido pagado
    private LocalDateTime fechaPago;
    // Fecha y hora límite de pago de la factura
    private LocalDateTime fechaVencimiento;
    // Estado del pago: 'Pendiente', 'Pagada' o 'Vencida'
    private String estadoPago;
    // Identificador del hogar al que pertenece este egreso (clave foránea a Hogar)
    private int idHogar;
    // Identificador de la categoría del egreso (clave foránea a Categorias_Egresos)
    private int idCategoriaEgreso;
    // Identificador del método de pago usado (clave foránea a Metodo_Pago)
    private int idMetodoPago;
    // Campo auxiliar cargado por JOIN con Categorias_Egresos para mostrar el nombre sin consulta adicional
    private String nombreCategoriaEgreso;
    // Campo auxiliar cargado por JOIN con Metodo_Pago para mostrar el nombre sin consulta adicional
    private String nombreMetodoPago;
    // Estado del egreso en el sistema: 'Activo' si está visible, 'Anulado' si fue ocultado
    private String estadoEgreso = "Activo";

    /* Constructor vacío requerido para instanciar el objeto antes de asignar valores mediante setters */
    public RegistroEgreso() {}

    /* Constructor completo para crear un egreso con todos sus datos en una sola instrucción */
    public RegistroEgreso(int idEgresos, String descripcionPago, BigDecimal monto,
                          LocalDateTime fechaPago, LocalDateTime fechaVencimiento,
                          String estadoPago, int idHogar,
                          int idCategoriaEgreso, String nombreCategoriaEgreso,
                          int idMetodoPago, String nombreMetodoPago) {
        this.idEgresos             = idEgresos;
        this.descripcionPago       = descripcionPago;
        this.monto                 = monto;
        this.fechaPago             = fechaPago;
        this.fechaVencimiento      = fechaVencimiento;
        this.estadoPago            = estadoPago;
        this.idHogar               = idHogar;
        this.idCategoriaEgreso     = idCategoriaEgreso;
        this.nombreCategoriaEgreso = nombreCategoriaEgreso;
        this.idMetodoPago          = idMetodoPago;
        this.nombreMetodoPago      = nombreMetodoPago;
    }

    // ── Getters / Setters ─────────────────────────────────────────────────────

    public int getIdEgresos()             { return idEgresos; }
    public void setIdEgresos(int v)       { this.idEgresos = v; }

    public String getDescripcionPago()       { return descripcionPago; }
    public void setDescripcionPago(String v) { this.descripcionPago = v; }

    // Alias de compatibilidad para FinanzasServlet y otros módulos que usan getDescripcion() / setDescripcion()
    // No duplica datos: apunta al mismo campo descripcionPago
    public String getDescripcion()       { return descripcionPago; }
    public void setDescripcion(String v) { this.descripcionPago = v; }

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

    // Alias corto para expresiones EL en JSPs: ${f.nombreCategoria} en lugar de ${f.nombreCategoriaEgreso}
    public String getNombreCategoria()              { return nombreCategoriaEgreso; }
    public void setNombreCategoria(String v)        { this.nombreCategoriaEgreso = v; }

    public String getNombreMetodoPago()         { return nombreMetodoPago; }
    public void setNombreMetodoPago(String v)   { this.nombreMetodoPago = v; }

    public String getEstadoEgreso()             { return estadoEgreso; }
    public void setEstadoEgreso(String v)       { this.estadoEgreso = v; }

    // ── Métodos de formato ────────────────────────────────────────────────────

    /* Método: getFechaVencimientoFormateada
       Propósito: Obtener la fecha de vencimiento en formato legible para tarjetas y listados
       (ej.: "15 abr 2025"). Retorna cadena vacía si la fecha es null.
       @return String → Fecha formateada como "dd MMM yyyy", o "" si no hay fecha
    */
    public String getFechaVencimientoFormateada() {
        if (fechaVencimiento == null) return "";
        return fechaVencimiento.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    /* Método: getFechaVencimientoISO
       Propósito: Obtener la fecha de vencimiento en formato ISO (yyyy-MM-dd) para usarla
       como valor predeterminado en el input type="date" del formulario de edición.
       @return String → Fecha formateada como "yyyy-MM-dd", o "" si no hay fecha
    */
    public String getFechaVencimientoISO() {
        if (fechaVencimiento == null) return "";
        return fechaVencimiento.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /* Método: getFechaPagoFormateada
       Propósito: Obtener la fecha de pago en formato legible para el historial de pagos
       (ej.: "20 abr 2025"). Retorna cadena vacía si la factura aún no ha sido pagada.
       @return String → Fecha formateada como "dd MMM yyyy", o "" si no hay fecha de pago
    */
    public String getFechaPagoFormateada() {
        if (fechaPago == null) return "";
        return fechaPago.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }
}
