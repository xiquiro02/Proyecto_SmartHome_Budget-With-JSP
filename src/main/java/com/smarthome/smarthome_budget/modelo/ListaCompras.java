package com.smarthome.smarthome_budget.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ListaCompras {

    private int idListaCompras;
    private String nombreLista;
    private LocalDateTime fechaCreacion;
    private String estadoLista;
    private int idHogar;
    // ── Campos auxiliares calculados por DAO 
    private int totalProductos;
    private int totalComprados;
    private int totalPendientes;

    public ListaCompras() {}

    public ListaCompras(int idListaCompras, String nombreLista, LocalDateTime fechaCreacion,
                        String estadoLista, int idHogar,
                        int totalProductos, int totalComprados, int totalPendientes) {
        this.idListaCompras = idListaCompras;
        this.nombreLista = nombreLista;
        this.fechaCreacion = fechaCreacion;
        this.estadoLista = estadoLista;
        this.idHogar = idHogar;
        this.totalProductos = totalProductos;
        this.totalComprados = totalComprados;
        this.totalPendientes = totalPendientes;
    }

    // ── Getters / Setters ─────────────────────────────────────────────────────

    public int getIdListaCompras()              { return idListaCompras; }
    public void setIdListaCompras(int v)        { this.idListaCompras = v; }

    public String getNombreLista()              { return nombreLista; }
    public void setNombreLista(String v)        { this.nombreLista = v; }

    public LocalDateTime getFechaCreacion()     { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime v){ this.fechaCreacion = v; }

    public String getEstadoLista()              { return estadoLista; }
    public void setEstadoLista(String v)        { this.estadoLista = v; }

    public int getIdHogar()                     { return idHogar; }
    public void setIdHogar(int v)               { this.idHogar = v; }

    public int getTotalProductos()              { return totalProductos; }
    public void setTotalProductos(int v)        { this.totalProductos = v; }

    public int getTotalComprados()              { return totalComprados; }
    public void setTotalComprados(int v)        { this.totalComprados = v; }

    public int getTotalPendientes()             { return totalPendientes; }
    public void setTotalPendientes(int v)       { this.totalPendientes = v; }

    public String getFechaCreacionFormateada() {
        if (fechaCreacion == null) return "";
        return fechaCreacion.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    public int getPorcentajeCompletado() {
        if (totalProductos == 0) return 0;
        return (totalComprados * 100) / totalProductos;
    }
}
