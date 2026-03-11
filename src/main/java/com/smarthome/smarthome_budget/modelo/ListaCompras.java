package com.smarthome.smarthome_budget.modelo;

import java.time.LocalDateTime;

/**
 * Modelo para Lista_Compras.
 * CategoriaLista fue eliminado de la BD — NO existe en este modelo.
 */
public class ListaCompras {
    private int idListaCompras;
    private int idHogar;
    private int idUsuario;
    private String nombreLista;
    private LocalDateTime fechaCreacion;
    private String estadoLista;       // Pendiente | En progreso | Completa

    // campos auxiliares calculados (no en BD)
    private int totalProductos;
    private int totalComprados;
    private int totalPendientes;

    public ListaCompras() {}

    public int getIdListaCompras() { return idListaCompras; }
    public void setIdListaCompras(int idListaCompras) { this.idListaCompras = idListaCompras; }
    public int getIdHogar() { return idHogar; }
    public void setIdHogar(int idHogar) { this.idHogar = idHogar; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getNombreLista() { return nombreLista; }
    public void setNombreLista(String nombreLista) { this.nombreLista = nombreLista; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getEstadoLista() { return estadoLista; }
    public void setEstadoLista(String estadoLista) { this.estadoLista = estadoLista; }
    public int getTotalProductos() { return totalProductos; }
    public void setTotalProductos(int totalProductos) { this.totalProductos = totalProductos; }
    public int getTotalComprados() { return totalComprados; }
    public void setTotalComprados(int totalComprados) { this.totalComprados = totalComprados; }
    public int getTotalPendientes() { return totalPendientes; }
    public void setTotalPendientes(int totalPendientes) { this.totalPendientes = totalPendientes; }
}
