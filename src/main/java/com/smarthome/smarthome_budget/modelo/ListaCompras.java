package com.smarthome.smarthome_budget.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/* Clase: ListaCompras
   Propósito: Representar una lista de compras del hogar (tabla Lista_Compras). Cada lista
   tiene un nombre, un estado que refleja el progreso de sus ítems (Pendiente, En progreso,
   Completa) y tres contadores auxiliares calculados por el DAO mediante subconsultas:
   total de productos, total comprados y total pendientes. Incluye métodos de formato y
   cálculo de porcentaje para ser usados directamente desde las vistas JSP.
*/
public class ListaCompras {

    // Identificador único de la lista de compras en la base de datos
    private int idListaCompras;
    // Nombre descriptivo de la lista (ej.: "Mercado semanal", "Cumpleaños")
    private String nombreLista;
    // Fecha y hora en que fue creada la lista
    private LocalDateTime fechaCreacion;
    // Estado actual de la lista: 'Pendiente', 'En progreso' o 'Completa'
    private String estadoLista;
    // Identificador del hogar al que pertenece la lista (clave foránea a Hogar)
    private int idHogar;
    // Campo auxiliar: cantidad total de ítems (productos) en la lista, calculado por subconsulta en el DAO
    private int totalProductos;
    // Campo auxiliar: cantidad de ítems marcados como comprados, calculado por subconsulta en el DAO
    private int totalComprados;
    // Campo auxiliar: cantidad de ítems aún pendientes (totalProductos - totalComprados)
    private int totalPendientes;

    /* Constructor vacío requerido para instanciar el objeto antes de asignar valores mediante setters */
    public ListaCompras() {}

    /* Constructor completo para crear una lista con todos sus datos y contadores en una sola instrucción */
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

    /* Método: getFechaCreacionFormateada
       Propósito: Obtener la fecha de creación de la lista en formato legible para las tarjetas
       de la vista (ej.: "15 Mar 2025"). Retorna cadena vacía si la fecha es null.
       @return String → Fecha formateada como "dd MMM yyyy", o "" si no hay fecha
    */
    public String getFechaCreacionFormateada() {
        if (fechaCreacion == null) return "";
        return fechaCreacion.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    /* Método: getPorcentajeCompletado
       Propósito: Calcular el porcentaje de ítems comprados respecto al total de la lista,
       para mostrar la barra de progreso en las vistas. Retorna 0 si la lista no tiene ítems.
       @return int → Porcentaje entre 0 y 100 representando el avance de la lista
    */
    public int getPorcentajeCompletado() {
        if (totalProductos == 0) return 0;
        return (totalComprados * 100) / totalProductos;
    }
}
