package com.smarthome.smarthome_budget.dao;

import java.time.LocalDateTime;

/* Clase: RecordatorioEgresoDao
   Propósito: Gestionar las operaciones de acceso a datos relacionadas con los recordatorios
   de egresos (facturas). Actualmente los métodos están reservados para implementación futura;
   la creación automática de recordatorios y su eliminación están declaradas pero no operativas.
*/
public class RecordatorioEgresoDao {

    /* Método: crearRecordatoriosAutomaticos
       Propósito: Crear recordatorios automáticos para un egreso próximo a vencer,
       notificando al usuario según la fecha de vencimiento de la factura.
       (Implementación pendiente — retorna true por defecto para no bloquear el flujo)
       @param idEgreso        → Entero con el ID del egreso al que se asociarán los recordatorios
       @param idUsuario       → Entero con el ID del usuario que recibirá las notificaciones
       @param fechaVencimiento → LocalDateTime con la fecha límite de pago de la factura
       @param nombreFactura   → Texto con la descripción de la factura para el mensaje del recordatorio
       @return boolean → true siempre (pendiente de implementación real)
    */
    public boolean crearRecordatoriosAutomaticos(int idEgreso, int idUsuario,
                                                  LocalDateTime fechaVencimiento, String nombreFactura) {
        return true;
    }

    /* Método: eliminarPorEgreso
       Propósito: Eliminar todos los recordatorios asociados a un egreso específico,
       por ejemplo al anular o eliminar la factura correspondiente.
       (Implementación pendiente — no realiza operaciones actualmente)
       @param idEgreso → Entero con el ID del egreso cuyos recordatorios se deben eliminar
    */
    public void eliminarPorEgreso(int idEgreso) {
        // Implementación pendiente
    }
}
