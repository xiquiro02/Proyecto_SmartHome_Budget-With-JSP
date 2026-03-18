package com.smarthome.smarthome_budget.dao;

import java.time.LocalDateTime;

public class RecordatorioEgresoDao {

    public boolean crearRecordatoriosAutomaticos(int idEgreso, int idUsuario, LocalDateTime fechaVencimiento, String nombreFactura) {
        return true;
    }

    /* Elimina recordatorios de un egreso.*/
    public void eliminarPorEgreso(int idEgreso) {
        
    }
}
