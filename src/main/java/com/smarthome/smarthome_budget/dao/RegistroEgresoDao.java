package com.smarthome.smarthome_budget.dao;

import com.smarthome.smarthome_budget.modelo.RegistroEgreso;
import com.smarthome.smarthome_budget.basedatos.claseConexion;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/* Clase: RegistroEgresoDao
   Propósito: Gestionar las operaciones de acceso a datos de los egresos del hogar
   (tabla Registro_Egresos). Cubre los módulos de Facturas y Finanzas: permite insertar
   facturas completas con fecha de vencimiento, registrar egresos simples marcados como pagados,
   listar por estado o categoría, filtrar el historial de pagados, actualizar, anular y reactivar
   registros, y calcular resúmenes de contadores y totales monetarios. Todos los listados excluyen
   los egresos anulados salvo el listado específico de anulados.
*/
public class RegistroEgresoDao {

    // Consulta SQL para insertar una factura completa con estado activo, incluyendo fecha de vencimiento y método de pago
    // language=sql
    private static final String SQL_INSERT =
        "INSERT INTO Registro_Egresos " +
        "(IDHogar, DescripcionPago, Monto, IDCategoriaEgreso, IDMetodoPago, FechaVencimiento, EstadoPago, EstadoEgresos) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, 'Activo')";

    // Base del SELECT con JOIN a categorías y métodos de pago; reutilizada por todas las consultas de lectura
    // language=sql
    private static final String SQL_SELECT_BASE =
        "SELECT re.*, ce.NombreCategoriaEgreso, mp.NombreMetodoPago " +
        "FROM Registro_Egresos re " +
        "JOIN Categorias_Egresos ce ON re.IDCategoriaEgreso = ce.IDCategoriaEgreso " +
        "JOIN Metodo_Pago mp ON re.IDMetodoPago = mp.IDMetodoPago ";

    // Consulta SQL que lista todos los egresos activos del hogar ordenados por fecha de vencimiento ascendente
    // language=sql
    private static final String SQL_SELECT_ALL =
        SQL_SELECT_BASE +
        "WHERE re.IDHogar = ? AND re.EstadoEgresos = 'Activo' " +
        "ORDER BY re.FechaVencimiento ASC";

    // Consulta SQL que lista solo los egresos anulados del hogar ordenados por fecha de vencimiento descendente
    // language=sql
    private static final String SQL_SELECT_ANULADOS =
        SQL_SELECT_BASE +
        "WHERE re.IDHogar = ? AND re.EstadoEgresos = 'Anulado' " +
        "ORDER BY re.FechaVencimiento DESC";

    // Consulta SQL que obtiene un egreso específico por su ID y hogar, sin filtrar por estado
    // language=sql
    private static final String SQL_SELECT_BY_ID =
        SQL_SELECT_BASE + "WHERE re.IDEgresos = ? AND re.IDHogar = ?";

    // Consulta SQL que filtra egresos activos por estado de pago (Pendiente, Pagada, Vencida)
    // language=sql
    private static final String SQL_SELECT_BY_ESTADO =
        SQL_SELECT_BASE +
        "WHERE re.IDHogar = ? AND re.EstadoPago = ? AND re.EstadoEgresos = 'Activo' " +
        "ORDER BY re.FechaVencimiento ASC";

    // Consulta SQL que filtra egresos activos por categoría de egreso
    // language=sql
    private static final String SQL_SELECT_BY_CATEGORIA =
        SQL_SELECT_BASE +
        "WHERE re.IDHogar = ? AND re.IDCategoriaEgreso = ? AND re.EstadoEgresos = 'Activo' " +
        "ORDER BY re.FechaVencimiento ASC";

    // Consulta SQL que filtra egresos activos por mes y año de la fecha de vencimiento
    // language=sql
    private static final String SQL_SELECT_BY_MES =
        SQL_SELECT_BASE +
        "WHERE re.IDHogar = ? AND MONTH(re.FechaVencimiento) = ? AND YEAR(re.FechaVencimiento) = ? " +
        "AND re.EstadoEgresos = 'Activo' " +
        "ORDER BY re.FechaVencimiento ASC";

    // Consulta SQL que lista todos los egresos pagados y activos del hogar, ordenados por fecha de pago descendente
    // language=sql
    private static final String SQL_SELECT_PAGADAS =
        SQL_SELECT_BASE +
        "WHERE re.IDHogar = ? AND re.EstadoPago = 'Pagada' AND re.EstadoEgresos = 'Activo' " +
        "ORDER BY re.FechaPago DESC";

    // Consulta SQL que filtra el historial de pagados por categoría de egreso
    // language=sql
    private static final String SQL_SELECT_PAGADAS_BY_CATEGORIA =
        SQL_SELECT_BASE +
        "WHERE re.IDHogar = ? AND re.EstadoPago = 'Pagada' AND re.IDCategoriaEgreso = ? " +
        "AND re.EstadoEgresos = 'Activo' " +
        "ORDER BY re.FechaPago DESC";

    // Consulta SQL que filtra el historial de pagados por mes y año de la fecha de pago
    // language=sql
    private static final String SQL_SELECT_PAGADAS_BY_MES =
        SQL_SELECT_BASE +
        "WHERE re.IDHogar = ? AND re.EstadoPago = 'Pagada' " +
        "AND MONTH(re.FechaPago) = ? AND YEAR(re.FechaPago) = ? " +
        "AND re.EstadoEgresos = 'Activo' " +
        "ORDER BY re.FechaPago DESC";

    // Consulta SQL que filtra el historial de pagados por rango de monto (mínimo y máximo)
    // language=sql
    private static final String SQL_SELECT_PAGADAS_BY_MONTO =
        SQL_SELECT_BASE +
        "WHERE re.IDHogar = ? AND re.EstadoPago = 'Pagada' " +
        "AND re.Monto >= ? AND re.Monto <= ? AND re.EstadoEgresos = 'Activo' " +
        "ORDER BY re.Monto ASC";

    // Consulta SQL para actualizar todos los campos editables de un egreso, incluyendo fecha de pago
    // language=sql
    private static final String SQL_UPDATE =
        "UPDATE Registro_Egresos " +
        "SET DescripcionPago=?, Monto=?, IDCategoriaEgreso=?, IDMetodoPago=?, " +
        "FechaVencimiento=?, EstadoPago=?, FechaPago=? " +
        "WHERE IDEgresos=? AND IDHogar=?";

    // Consulta SQL que marca un egreso como pagado y registra la fecha de pago con la hora actual
    // language=sql
    private static final String SQL_MARCAR_PAGADA =
        "UPDATE Registro_Egresos SET EstadoPago='Pagada', FechaPago=NOW() " +
        "WHERE IDEgresos=? AND IDHogar=?";

    // Consulta SQL que cambia el EstadoEgresos entre 'Activo' y 'Anulado' para anular o reactivar
    // language=sql
    private static final String SQL_CAMBIAR_ESTADO =
        "UPDATE Registro_Egresos SET EstadoEgresos=? WHERE IDEgresos=? AND IDHogar=?";

    // Consulta SQL que cuenta los egresos activos agrupados por estado de pago para el resumen del módulo
    // language=sql
    private static final String SQL_RESUMEN =
        "SELECT " +
        "  SUM(CASE WHEN EstadoPago='Pendiente' THEN 1 ELSE 0 END) AS totalPendientes, " +
        "  SUM(CASE WHEN EstadoPago='Pagada'    THEN 1 ELSE 0 END) AS totalPagadas, " +
        "  SUM(CASE WHEN EstadoPago='Vencida'   THEN 1 ELSE 0 END) AS totalVencidas " +
        "FROM Registro_Egresos WHERE IDHogar=? AND EstadoEgresos = 'Activo'";

    // Consulta SQL que obtiene la suma y cantidad total de egresos pagados y activos del hogar
    // language=sql
    private static final String SQL_TOTAL_PAGADO =
        "SELECT COALESCE(SUM(Monto),0) AS totalPagado, COUNT(*) AS cantidadPagadas " +
        "FROM Registro_Egresos " +
        "WHERE IDHogar=? AND EstadoPago='Pagada' AND EstadoEgresos = 'Activo'";

    // Consulta SQL que actualiza a 'Vencida' todos los egresos pendientes activos cuya fecha ya pasó
    // language=sql
    private static final String SQL_VENCER_AUTOMATICO =
        "UPDATE Registro_Egresos SET EstadoPago='Vencida' " +
        "WHERE EstadoPago='Pendiente' AND FechaVencimiento < NOW() AND EstadoEgresos = 'Activo'";

    /* Método: insertarEgreso
       Propósito: Registrar una factura completa en el módulo de Facturas, incluyendo fecha de
       vencimiento, método de pago y estado de pago. El EstadoEgresos se establece como 'Activo'.
       @param egreso → Objeto RegistroEgreso con todos los campos del formulario de facturas
       @return int → ID autogenerado del egreso creado, o -1 si la inserción falla
    */
    public int insertarEgreso(RegistroEgreso egreso) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, egreso.getIdHogar());
            // Texto con la descripción del pago; se usa cadena vacía si viene nula
            String desc = egreso.getDescripcionPago() != null ? egreso.getDescripcionPago().trim() : "";
            ps.setString(2, desc);
            ps.setBigDecimal(3, egreso.getMonto());
            ps.setInt(4, egreso.getIdCategoriaEgreso());
            ps.setInt(5, egreso.getIdMetodoPago());
            ps.setObject(6, egreso.getFechaVencimiento());
            ps.setString(7, egreso.getEstadoPago());
            // Variable entera que almacena la cantidad de filas afectadas por la inserción
            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    // Variable entera que almacena el ID autogenerado por la base de datos
                    if (rs.next()) return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("[RegistroEgresoDao] Error al insertar: " + e.getMessage());
        }
        return -1;
    }

    /* Método: registrar
       Propósito: Registrar un egreso simple desde el módulo de Finanzas. Usa método de pago
       Efectivo (ID=1), fecha actual como vencimiento y lo marca automáticamente como 'Pagada'.
       @param egreso → Objeto RegistroEgreso con idHogar, monto, descripción e idCategoriaEgreso
       @return boolean → true si el egreso fue registrado correctamente, false si falló
    */
    public boolean registrar(RegistroEgreso egreso) {
        // Consulta SQL inline para egreso simple: usa método de pago Efectivo y lo marca como pagado de inmediato
        // language=sql
        String sql =
            "INSERT INTO Registro_Egresos " +
            "(IDHogar, DescripcionPago, Monto, IDCategoriaEgreso, IDMetodoPago, " +
            "FechaVencimiento, EstadoPago) " +
            "VALUES (?, ?, ?, ?, 1, NOW(), 'Pagada')";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, egreso.getIdHogar());
            // Texto con la descripción del egreso; se usa 'Egreso' como valor por defecto si viene nula
            String desc = egreso.getDescripcionPago() != null ? egreso.getDescripcionPago() : "Egreso";
            ps.setString(2, desc);
            ps.setBigDecimal(3, egreso.getMonto());
            ps.setInt(4, egreso.getIdCategoriaEgreso());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[RegistroEgresoDao] Error al registrar simple: " + e.getMessage());
        }
        return false;
    }

    /* Método: listarPorHogar
       Propósito: Obtener todos los egresos activos del hogar ordenados por fecha de vencimiento ascendente.
       @param idHogar → Entero con el ID del hogar a consultar
       @return List<RegistroEgreso> → Lista con todos los egresos activos; vacía si no hay datos
    */
    public List<RegistroEgreso> listarPorHogar(int idHogar) {
        return listarConParam(SQL_SELECT_ALL, idHogar);
    }

    /* Método: obtenerPorId
       Propósito: Buscar un egreso específico por su ID, validando que pertenezca al hogar indicado.
       No filtra por EstadoEgresos, por lo que retorna tanto activos como anulados.
       @param idEgreso → Entero con el ID del egreso a buscar
       @param idHogar  → Entero con el ID del hogar para validar pertenencia
       @return RegistroEgreso → El objeto encontrado, o null si no existe o no pertenece al hogar
    */
    public RegistroEgreso obtenerPorId(int idEgreso, int idHogar) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, idEgreso);
            ps.setInt(2, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("[RegistroEgresoDao] Error al obtener por id: " + e.getMessage());
        }
        return null;
    }

    /* Método: listarPorEstado
       Propósito: Obtener los egresos activos del hogar filtrados por su estado de pago.
       @param idHogar → Entero con el ID del hogar a consultar
       @param estado  → Texto con el estado de pago a filtrar ('Pendiente', 'Pagada' o 'Vencida')
       @return List<RegistroEgreso> → Lista de egresos activos con ese estado; vacía si no hay datos
    */
    public List<RegistroEgreso> listarPorEstado(int idHogar, String estado) {
        // Lista de objetos RegistroEgreso que se llenará con los egresos del estado indicado
        List<RegistroEgreso> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_ESTADO)) {
            ps.setInt(1, idHogar);
            ps.setString(2, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[RegistroEgresoDao] Error al filtrar por estado: " + e.getMessage());
        }
        return lista;
    }

    /* Método: listarPorCategoria
       Propósito: Obtener los egresos activos del hogar filtrados por categoría de egreso.
       @param idHogar     → Entero con el ID del hogar a consultar
       @param idCategoria → Entero con el ID de la categoría por la que filtrar
       @return List<RegistroEgreso> → Lista de egresos activos de esa categoría; vacía si no hay datos
    */
    public List<RegistroEgreso> listarPorCategoria(int idHogar, int idCategoria) {
        // Lista de objetos RegistroEgreso que se llenará con los egresos de la categoría indicada
        List<RegistroEgreso> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_CATEGORIA)) {
            ps.setInt(1, idHogar);
            ps.setInt(2, idCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[RegistroEgresoDao] Error al filtrar por categoría: " + e.getMessage());
        }
        return lista;
    }

    /* Método: listarPorMes
       Propósito: Obtener los egresos activos del hogar filtrados por mes y año de vencimiento.
       @param idHogar → Entero con el ID del hogar a consultar
       @param mes     → Entero con el mes a filtrar (1-12)
       @param anio    → Entero con el año a filtrar
       @return List<RegistroEgreso> → Lista de egresos activos del mes indicado; vacía si no hay datos
    */
    public List<RegistroEgreso> listarPorMes(int idHogar, int mes, int anio) {
        // Lista de objetos RegistroEgreso que se llenará con los egresos del mes y año indicados
        List<RegistroEgreso> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_MES)) {
            ps.setInt(1, idHogar);
            ps.setInt(2, mes);
            ps.setInt(3, anio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[RegistroEgresoDao] Error al filtrar por mes: " + e.getMessage());
        }
        return lista;
    }

    /* Método: listarPagadas
       Propósito: Obtener todos los egresos activos pagados del hogar, ordenados por fecha de pago
       descendente. Usado en el historial de pagos del módulo de facturas.
       @param idHogar → Entero con el ID del hogar a consultar
       @return List<RegistroEgreso> → Lista de egresos pagados y activos; vacía si no hay datos
    */
    public List<RegistroEgreso> listarPagadas(int idHogar) {
        return listarConParam(SQL_SELECT_PAGADAS, idHogar);
    }

    /* Método: listarPagadasPorCategoria
       Propósito: Filtrar el historial de egresos pagados por categoría de egreso.
       @param idHogar     → Entero con el ID del hogar a consultar
       @param idCategoria → Entero con el ID de la categoría por la que filtrar
       @return List<RegistroEgreso> → Lista de egresos pagados de esa categoría; vacía si no hay datos
    */
    public List<RegistroEgreso> listarPagadasPorCategoria(int idHogar, int idCategoria) {
        // Lista de objetos RegistroEgreso que se llenará con los pagados de la categoría indicada
        List<RegistroEgreso> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_PAGADAS_BY_CATEGORIA)) {
            ps.setInt(1, idHogar);
            ps.setInt(2, idCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[RegistroEgresoDao] Error historial por categoría: " + e.getMessage());
        }
        return lista;
    }

    /* Método: listarPagadasPorMes
       Propósito: Filtrar el historial de egresos pagados por mes y año de la fecha de pago.
       @param idHogar → Entero con el ID del hogar a consultar
       @param mes     → Entero con el mes a filtrar (1-12)
       @param anio    → Entero con el año a filtrar
       @return List<RegistroEgreso> → Lista de egresos pagados del mes indicado; vacía si no hay datos
    */
    public List<RegistroEgreso> listarPagadasPorMes(int idHogar, int mes, int anio) {
        // Lista de objetos RegistroEgreso que se llenará con los pagados del mes y año indicados
        List<RegistroEgreso> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_PAGADAS_BY_MES)) {
            ps.setInt(1, idHogar);
            ps.setInt(2, mes);
            ps.setInt(3, anio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[RegistroEgresoDao] Error historial por mes: " + e.getMessage());
        }
        return lista;
    }

    /* Método: listarPagadasPorMonto
       Propósito: Filtrar el historial de egresos pagados por rango de monto (mínimo y máximo).
       @param idHogar  → Entero con el ID del hogar a consultar
       @param montoMin → BigDecimal con el monto mínimo del rango (inclusivo)
       @param montoMax → BigDecimal con el monto máximo del rango (inclusivo)
       @return List<RegistroEgreso> → Lista de egresos pagados dentro del rango; vacía si no hay datos
    */
    public List<RegistroEgreso> listarPagadasPorMonto(int idHogar,
                                                       BigDecimal montoMin, BigDecimal montoMax) {
        // Lista de objetos RegistroEgreso que se llenará con los pagados dentro del rango de monto
        List<RegistroEgreso> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_PAGADAS_BY_MONTO)) {
            ps.setInt(1, idHogar);
            ps.setBigDecimal(2, montoMin);
            ps.setBigDecimal(3, montoMax);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[RegistroEgresoDao] Error historial por monto: " + e.getMessage());
        }
        return lista;
    }

    /* Método: listarAnulados
       Propósito: Obtener todos los egresos anulados del hogar, para mostrarlos en la
       papelera o historial de anulados del módulo de facturas.
       @param idHogar → Entero con el ID del hogar a consultar
       @return List<RegistroEgreso> → Lista de egresos anulados; vacía si no hay datos
    */
    public List<RegistroEgreso> listarAnulados(int idHogar) {
        return listarConParam(SQL_SELECT_ANULADOS, idHogar);
    }

    /* Método: actualizar
       Propósito: Modificar todos los campos editables de un egreso existente: descripción,
       monto, categoría, método de pago, fecha de vencimiento, estado de pago y fecha de pago.
       @param egreso → Objeto RegistroEgreso con todos los campos actualizados
       @return boolean → true si se actualizó correctamente, false si no existe o falló
    */
    public boolean actualizar(RegistroEgreso egreso) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {
            // Texto con la descripción del pago; se usa cadena vacía si viene nula
            String desc = egreso.getDescripcionPago() != null ? egreso.getDescripcionPago().trim() : "";
            ps.setString(1, desc);
            ps.setBigDecimal(2, egreso.getMonto());
            ps.setInt(3, egreso.getIdCategoriaEgreso());
            ps.setInt(4, egreso.getIdMetodoPago());
            ps.setObject(5, egreso.getFechaVencimiento());
            ps.setString(6, egreso.getEstadoPago());
            ps.setObject(7, egreso.getFechaPago());
            ps.setInt(8, egreso.getIdEgresos());
            ps.setInt(9, egreso.getIdHogar());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[RegistroEgresoDao] Error al actualizar: " + e.getMessage());
        }
        return false;
    }

    /* Método: marcarComoPagada
       Propósito: Actualizar el estado de pago de un egreso a 'Pagada' y registrar la
       fecha y hora actuales como fecha de pago.
       @param idEgreso → Entero con el ID del egreso a marcar como pagado
       @param idHogar  → Entero con el ID del hogar para validar pertenencia
       @return boolean → true si el egreso fue marcado como pagado, false si no existe o falló
    */
    public boolean marcarComoPagada(int idEgreso, int idHogar) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_MARCAR_PAGADA)) {
            ps.setInt(1, idEgreso);
            ps.setInt(2, idHogar);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[RegistroEgresoDao] Error al marcar pagada: " + e.getMessage());
        }
        return false;
    }

    /* Método: cambiarEstado
       Propósito: Cambiar el EstadoEgresos de un egreso entre 'Activo' y 'Anulado'. Método
       unificado utilizado por anular() y reactivar() para evitar duplicación de lógica.
       @param idEgreso    → Entero con el ID del egreso a modificar
       @param idHogar     → Entero con el ID del hogar para validar pertenencia
       @param nuevoEstado → Texto con el nuevo estado a establecer ('Activo' o 'Anulado')
       @return boolean → true si el estado fue cambiado correctamente, false si no existe o falló
    */
    public boolean cambiarEstado(int idEgreso, int idHogar, String nuevoEstado) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_CAMBIAR_ESTADO)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idEgreso);
            ps.setInt(3, idHogar);
            // Variable entera que almacena la cantidad de filas afectadas por la actualización de estado
            int filas = ps.executeUpdate();
            System.out.println("[RegistroEgresoDao] cambiarEstado → id=" + idEgreso
                    + " hogar=" + idHogar + " estado=" + nuevoEstado + " filasAfectadas=" + filas);
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("[RegistroEgresoDao] Error al cambiar estado: " + e.getMessage());
        }
        return false;
    }

    /* Método: anular
       Propósito: Cambiar el EstadoEgresos de un egreso a 'Anulado', ocultándolo de las vistas activas.
       @param idEgreso → Entero con el ID del egreso a anular
       @param idHogar  → Entero con el ID del hogar para validar pertenencia
       @return boolean → true si el egreso fue anulado correctamente, false si falló
    */
    public boolean anular(int idEgreso, int idHogar) {
        return cambiarEstado(idEgreso, idHogar, "Anulado");
    }

    /* Método: reactivar
       Propósito: Cambiar el EstadoEgresos de un egreso a 'Activo', restaurándolo en las vistas activas.
       @param idEgreso → Entero con el ID del egreso a reactivar
       @param idHogar  → Entero con el ID del hogar para validar pertenencia
       @return boolean → true si el egreso fue reactivado correctamente, false si falló
    */
    public boolean reactivar(int idEgreso, int idHogar) {
        return cambiarEstado(idEgreso, idHogar, "Activo");
    }

    /* Método: actualizarVencidas
       Propósito: Marcar automáticamente como 'Vencida' todos los egresos activos con
       estado 'Pendiente' cuya fecha de vencimiento ya pasó. Se ejecuta al cargar los módulos
       de facturas y finanzas para mantener los estados actualizados.
    */
    public void actualizarVencidas() {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_VENCER_AUTOMATICO)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[RegistroEgresoDao] Error actualizando vencidas: " + e.getMessage());
        }
    }

    /* Método: obtenerResumen
       Propósito: Obtener un resumen con la cantidad de egresos activos agrupados por estado de pago
       (pendientes, pagados y vencidos) para mostrar en el panel de control del módulo.
       @param idHogar → Entero con el ID del hogar a resumir
       @return int[] → Arreglo de 3 enteros: [totalPendientes, totalPagadas, totalVencidas]
    */
    public int[] obtenerResumen(int idHogar) {
        // Arreglo entero que almacenará los contadores de pendientes, pagadas y vencidas
        int[] r = {0, 0, 0};
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_RESUMEN)) {
            ps.setInt(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    r[0] = rs.getInt("totalPendientes");
                    r[1] = rs.getInt("totalPagadas");
                    r[2] = rs.getInt("totalVencidas");
                }
            }
        } catch (SQLException e) {
            System.err.println("[RegistroEgresoDao] Error al obtener resumen: " + e.getMessage());
        }
        return r;
    }

    /* Método: obtenerTotalPagado
       Propósito: Calcular la suma total de dinero pagado y la cantidad de egresos pagados
       y activos del hogar, para mostrarlos en el resumen financiero.
       @param idHogar → Entero con el ID del hogar a calcular
       @return BigDecimal[] → Arreglo de 2 valores: [suma total pagada, cantidad de pagos]
    */
    public BigDecimal[] obtenerTotalPagado(int idHogar) {
        // Arreglo BigDecimal que almacenará la suma total pagada y la cantidad de pagos
        BigDecimal[] t = {BigDecimal.ZERO, BigDecimal.ZERO};
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_TOTAL_PAGADO)) {
            ps.setInt(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    t[0] = rs.getBigDecimal("totalPagado");
                    t[1] = new BigDecimal(rs.getInt("cantidadPagadas"));
                }
            }
        } catch (SQLException e) {
            System.err.println("[RegistroEgresoDao] Error total pagado: " + e.getMessage());
        }
        return t;
    }

    /* Método: listarCategorias
       Propósito: Obtener todos los pares ID-nombre de las categorías de egreso disponibles,
       para poblar los selectores en los formularios del módulo de facturas y finanzas.
       @return List<Object[]> → Lista de arreglos con [IDCategoriaEgreso, NombreCategoriaEgreso]
    */
    public List<Object[]> listarCategorias() {
        // Lista de arreglos que almacenará cada par de ID y nombre de categoría de egreso
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT IDCategoriaEgreso, NombreCategoriaEgreso " +
                     "FROM Categorias_Egresos ORDER BY IDCategoriaEgreso";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                lista.add(new Object[]{rs.getInt(1), rs.getString(2)});
        } catch (SQLException e) {
            System.err.println("[RegistroEgresoDao] Error al listar categorías: " + e.getMessage());
        }
        return lista;
    }

    /* Método: mapear
       Propósito: Construir un objeto RegistroEgreso a partir de la fila actual del ResultSet,
       incluyendo los nombres de categoría y método de pago obtenidos mediante JOIN.
       @param rs → ResultSet posicionado en la fila a mapear
       @return RegistroEgreso → El objeto construido con los datos de la fila
    */
    private RegistroEgreso mapear(ResultSet rs) throws SQLException {
        // Objeto RegistroEgreso que recibirá los valores de cada columna de la fila
        RegistroEgreso e = new RegistroEgreso();
        e.setIdEgresos(rs.getInt("IDEgresos"));
        e.setIdHogar(rs.getInt("IDHogar"));
        e.setDescripcionPago(rs.getString("DescripcionPago"));
        e.setMonto(rs.getBigDecimal("Monto"));
        e.setIdCategoriaEgreso(rs.getInt("IDCategoriaEgreso"));
        e.setNombreCategoriaEgreso(rs.getString("NombreCategoriaEgreso"));
        e.setIdMetodoPago(rs.getInt("IDMetodoPago"));
        e.setNombreMetodoPago(rs.getString("NombreMetodoPago"));
        e.setFechaVencimiento(rs.getObject("FechaVencimiento", LocalDateTime.class));
        e.setFechaPago(rs.getObject("FechaPago", LocalDateTime.class));
        e.setEstadoPago(rs.getString("EstadoPago"));
        e.setEstadoEgreso(rs.getString("EstadoEgresos"));
        return e;
    }

    /* Método: listarConParam
       Propósito: Ejecutar una consulta SQL parametrizada que recibe únicamente el ID del hogar
       como parámetro y retorna una lista de RegistroEgreso. Reutilizado por listarPorHogar,
       listarPagadas y listarAnulados para evitar duplicación de código de iteración.
       @param sql     → Texto con la consulta SQL a ejecutar
       @param idHogar → Entero con el ID del hogar a pasar como parámetro
       @return List<RegistroEgreso> → Lista de egresos resultantes; vacía si no hay datos o falla
    */
    private List<RegistroEgreso> listarConParam(String sql, int idHogar) {
        // Lista de objetos RegistroEgreso que se llenará con los resultados de la consulta recibida
        List<RegistroEgreso> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[RegistroEgresoDao] Error al listar: " + e.getMessage());
        }
        return lista;
    }
}
