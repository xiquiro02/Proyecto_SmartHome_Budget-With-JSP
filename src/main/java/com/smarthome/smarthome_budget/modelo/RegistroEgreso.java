package com.smarthome.smarthome_budget.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Modelo que representa una factura/egreso del hogar.
 * Mapea la tabla Registro_Egresos de la base de datos.
 */
public class RegistroEgreso {
    private String Descripcion;
    private BigDecimal Monto;
    private int idCategoriaEgreso;
    private String nombreCategoria;
    private int idMetodoPago;
    private String nombreMetodoPago;
    private LocalDateTime fechaPago;
    private LocalDateTime fechaVencimiento;
    private String descripcion;
    private String estadoPago;
    private int idEgresos;
    private int idHogar;

    public RegistroEgreso() {}

    public int getIdEgresos() { 
        return idEgresos; 
    }
    
    public void setIdEgresos(int idEgresos) { 
        this.idEgresos = idEgresos; 
    }
    
    public int getIdHogar() { 
        return idHogar; 
    }
    
    public void setIdHogar(int idHogar) { 
        this.idHogar = idHogar; 
    }
    
    public int getIdUsuario() { 
        return idUsuario; 
    }
    
    public void setIdUsuario(int idUsuario) { 
        this.idUsuario = idUsuario; 
    }
    
    public String getNombreFactura() { 
        return nombreFactura; 
    }
    
    public void setNombreFactura(String nombreFactura) { 
        this.nombreFactura = nombreFactura; 
    }
    
    public BigDecimal getMonto() { 
        return monto; 
    }
    
    public void setMonto(BigDecimal monto) { 
        this.monto = monto; 
    }
    
    public int getIdCategoriaEgreso() { 
        return idCategoriaEgreso; 
    }
    
    public void setIdCategoriaEgreso(int idCategoriaEgreso) { 
        this.idCategoriaEgreso = idCategoriaEgreso; 
    }
    
    public String getNombreCategoria() { 
        return nombreCategoria; 
    }
    
    public void setNombreCategoria(String nombreCategoria) { 
        this.nombreCategoria = nombreCategoria; 
    }
    
    public int getIdMetodoPago() { 
        return idMetodoPago; 
    }
    
    public void setIdMetodoPago(int idMetodoPago) { 
        this.idMetodoPago = idMetodoPago; 
    }
    
    public String getNombreMetodoPago() { 
        return nombreMetodoPago; 
    }
    
    public void setNombreMetodoPago(String nombreMetodoPago) { 
        this.nombreMetodoPago = nombreMetodoPago; 
    }
    
    public LocalDateTime getFechaPago() { 
        return fechaPago; 
    }
    
    public void setFechaPago(LocalDateTime fechaPago) { 
        this.fechaPago = fechaPago; 
    }
    
    public LocalDateTime getFechaVencimiento() { 
        return fechaVencimiento; 
    }
    
    public void setFechaVencimiento(LocalDateTime fechaVencimiento) { 
        this.fechaVencimiento = fechaVencimiento; 
    }
    
    public String getDescripcion() { 
        return descripcion; 
    }
    
    public void setDescripcion(String descripcion) { 
        this.descripcion = descripcion; 
    }
    
    public String getEstadoPago() { 
        return estadoPago; 
    }
    
    public void setEstadoPago(String estadoPago) { 
        this.estadoPago = estadoPago; 
    }
    
    public String getFechaVencimientoFormateada() {
        if (this.fechaVencimiento == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        return this.fechaVencimiento.format(formatter);
    }
}
