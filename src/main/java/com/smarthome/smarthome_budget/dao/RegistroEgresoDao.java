package com.smarthome.smarthome_budget.dao;

import com.smarthome.smarthome_budget.modelo.RegistroEgreso;
import com.smarthome.smarthome_budget.basedatos.claseConexion;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RegistroEgresoDao {

    // language=sql
    private static final String SQL_INSERT =
        "INSERT INTO Registro_Egresos " +
        "(IDHogar, DescripcionPago, Monto, IDCategoriaEgreso, IDMetodoPago, FechaVencimiento, EstadoPago) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";

    // ── Base de SELECT (JOIN fijo) ─────────────────────────────────────────────
    // language=sql
    private static final String SQL_SELECT_BASE =
        "SELECT re.*, ce.NombreCategoriaEgreso, mp.NombreMetodoPago " +
        "FROM Registro_Egresos re " +
        "JOIN Categorias_Egresos ce ON re.IDCategoriaEgreso = ce.IDCategoriaEgreso " +
        "JOIN Metodo_Pago mp ON re.IDMetodoPago = mp.IDMetodoPago ";

    // ── Consultas del módulo Finanzas (filtran EstadoEgresos) ─────────────────
    // language=sql
    private static final String SQL_SELECT_ALL =
        SQL_SELECT_BASE +
        "WHERE re.IDHogar = ? AND re.EstadoEgresos = 'Activo' " +
        "ORDER BY re.FechaVencimiento ASC";
    // language=sql
    private static final String SQL_SELECT_ANULADOS =
        SQL_SELECT_BASE +
        "WHERE re.IDHogar = ? AND re.EstadoEgresos = 'Anulado' " +
        "ORDER BY re.FechaVencimiento DESC";

    // ── Consultas del módulo Facturas (también excluyen anulados) ─────────────
    // language=sql
    private static final String SQL_SELECT_BY_ID =
        SQL_SELECT_BASE + "WHERE re.IDEgresos = ? AND re.IDHogar = ?";
    // language=sql
    private static final String SQL_SELECT_BY_ESTADO =
        SQL_SELECT_BASE +
        "WHERE re.IDHogar = ? AND re.EstadoPago = ? AND re.EstadoEgresos = 'Activo' " +
        "ORDER BY re.FechaVencimiento ASC";
    // language=sql
    private static final String SQL_SELECT_BY_CATEGORIA =
        SQL_SELECT_BASE +
        "WHERE re.IDHogar = ? AND re.IDCategoriaEgreso = ? AND re.EstadoEgresos = 'Activo' " +
        "ORDER BY re.FechaVencimiento ASC";
    // language=sql
    private static final String SQL_SELECT_BY_MES =
        SQL_SELECT_BASE +
        "WHERE re.IDHogar = ? AND MONTH(re.FechaVencimiento) = ? AND YEAR(re.FechaVencimiento) = ? " +
        "AND re.EstadoEgresos = 'Activo' " +
        "ORDER BY re.FechaVencimiento ASC";
    // language=sql
    private static final String SQL_SELECT_PAGADAS =
        SQL_SELECT_BASE +
        "WHERE re.IDHogar = ? AND re.EstadoPago = 'Pagada' AND re.EstadoEgresos = 'Activo' " +
        "ORDER BY re.FechaPago DESC";
    // language=sql
    private static final String SQL_SELECT_PAGADAS_BY_CATEGORIA =
        SQL_SELECT_BASE +
        "WHERE re.IDHogar = ? AND re.EstadoPago = 'Pagada' AND re.IDCategoriaEgreso = ? " +
        "AND re.EstadoEgresos = 'Activo' " +
        "ORDER BY re.FechaPago DESC";
    // language=sql
    private static final String SQL_SELECT_PAGADAS_BY_MES =
        SQL_SELECT_BASE +
        "WHERE re.IDHogar = ? AND re.EstadoPago = 'Pagada' " +
        "AND MONTH(re.FechaPago) = ? AND YEAR(re.FechaPago) = ? " +
        "AND re.EstadoEgresos = 'Activo' " +
        "ORDER BY re.FechaPago DESC";
    // language=sql
    private static final String SQL_SELECT_PAGADAS_BY_MONTO =
        SQL_SELECT_BASE +
        "WHERE re.IDHogar = ? AND re.EstadoPago = 'Pagada' " +
        "AND re.Monto >= ? AND re.Monto <= ? AND re.EstadoEgresos = 'Activo' " +
        "ORDER BY re.Monto ASC";

    // ── Actualizar ────────────────────────────────────────────────────────────
    // language=sql
    private static final String SQL_UPDATE =
        "UPDATE Registro_Egresos " +
        "SET DescripcionPago=?, Monto=?, IDCategoriaEgreso=?, IDMetodoPago=?, " +
        "FechaVencimiento=?, EstadoPago=?, FechaPago=? " +
        "WHERE IDEgresos=? AND IDHogar=?";
    // language=sql
    private static final String SQL_MARCAR_PAGADA =
        "UPDATE Registro_Egresos SET EstadoPago='Pagada', FechaPago=NOW() " +
        "WHERE IDEgresos=? AND IDHogar=?";
    // language=sql — método unificado para anular/reactivar
    private static final String SQL_CAMBIAR_ESTADO =
        "UPDATE Registro_Egresos SET EstadoEgresos=? WHERE IDEgresos=? AND IDHogar=?";

    // ── Resumen / Totales (excluyen anulados) ─────────────────────────────────
    // language=sql
    private static final String SQL_RESUMEN =
        "SELECT " +
        "  SUM(CASE WHEN EstadoPago='Pendiente' THEN 1 ELSE 0 END) AS totalPendientes, " +
        "  SUM(CASE WHEN EstadoPago='Pagada'    THEN 1 ELSE 0 END) AS totalPagadas, " +
        "  SUM(CASE WHEN EstadoPago='Vencida'   THEN 1 ELSE 0 END) AS totalVencidas " +
        "FROM Registro_Egresos WHERE IDHogar=? AND EstadoEgresos = 'Activo'";
    // language=sql
    private static final String SQL_TOTAL_PAGADO =
        "SELECT COALESCE(SUM(Monto),0) AS totalPagado, COUNT(*) AS cantidadPagadas " +
        "FROM Registro_Egresos " +
        "WHERE IDHogar=? AND EstadoPago='Pagada' AND EstadoEgresos = 'Activo'";
    // language=sql
    private static final String SQL_VENCER_AUTOMATICO =
        "UPDATE Registro_Egresos SET EstadoPago='Vencida' " +
        "WHERE EstadoPago='Pendiente' AND FechaVencimiento < NOW() AND EstadoEgresos = 'Activo'";

    // ── Insertar ──────────────────────────────────────────────────────────────

    /** Registra una factura (módulo Facturas). Retorna el ID generado o -1 si falla. */
    public int insertarEgreso(RegistroEgreso egreso) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, egreso.getIdHogar());
            String desc = egreso.getDescripcionPago() != null ? egreso.getDescripcionPago().trim() : "";
            ps.setString(2, desc);
            ps.setBigDecimal(3, egreso.getMonto());
            ps.setInt(4, egreso.getIdCategoriaEgreso());
            ps.setInt(5, egreso.getIdMetodoPago());
            ps.setObject(6, egreso.getFechaVencimiento());
            ps.setString(7, egreso.getEstadoPago());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("[EgresoDao] Error al insertar: " + e.getMessage());
        }
        return -1;
    }

    /** Registra un egreso simple desde el módulo Finanzas. */
    public boolean registrar(RegistroEgreso egreso) {
        // language=sql
        String sql =
            "INSERT INTO Registro_Egresos " +
            "(IDHogar, DescripcionPago, Monto, IDCategoriaEgreso, IDMetodoPago, " +
            "FechaVencimiento, EstadoPago) " +
            "VALUES (?, ?, ?, ?, 1, NOW(), 'Pagada')";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, egreso.getIdHogar());
            String desc = egreso.getDescripcionPago() != null ? egreso.getDescripcionPago() : "Egreso";
            ps.setString(2, desc);
            ps.setBigDecimal(3, egreso.getMonto());
            ps.setInt(4, egreso.getIdCategoriaEgreso());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[EgresoDao] Error al registrar simple: " + e.getMessage());
        }
        return false;
    }

    // ── Consultar ─────────────────────────────────────────────────────────────

    public List<RegistroEgreso> listarPorHogar(int idHogar) {
        return listarConParam(SQL_SELECT_ALL, idHogar);
    }

    public RegistroEgreso obtenerPorId(int idEgreso, int idHogar) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, idEgreso);
            ps.setInt(2, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("[EgresoDao] Error al obtener por id: " + e.getMessage());
        }
        return null;
    }

    public List<RegistroEgreso> listarPorEstado(int idHogar, String estado) {
        List<RegistroEgreso> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_ESTADO)) {
            ps.setInt(1, idHogar);
            ps.setString(2, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[EgresoDao] Error al filtrar por estado: " + e.getMessage());
        }
        return lista;
    }

    public List<RegistroEgreso> listarPorCategoria(int idHogar, int idCategoria) {
        List<RegistroEgreso> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_CATEGORIA)) {
            ps.setInt(1, idHogar);
            ps.setInt(2, idCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[EgresoDao] Error al filtrar por categoría: " + e.getMessage());
        }
        return lista;
    }

    public List<RegistroEgreso> listarPorMes(int idHogar, int mes, int anio) {
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
            System.err.println("[EgresoDao] Error al filtrar por mes: " + e.getMessage());
        }
        return lista;
    }

    public List<RegistroEgreso> listarPagadas(int idHogar) {
        return listarConParam(SQL_SELECT_PAGADAS, idHogar);
    }

    public List<RegistroEgreso> listarPagadasPorCategoria(int idHogar, int idCategoria) {
        List<RegistroEgreso> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_PAGADAS_BY_CATEGORIA)) {
            ps.setInt(1, idHogar);
            ps.setInt(2, idCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[EgresoDao] Error historial por categoría: " + e.getMessage());
        }
        return lista;
    }

    public List<RegistroEgreso> listarPagadasPorMes(int idHogar, int mes, int anio) {
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
            System.err.println("[EgresoDao] Error historial por mes: " + e.getMessage());
        }
        return lista;
    }

    public List<RegistroEgreso> listarPagadasPorMonto(int idHogar,
                                                       BigDecimal montoMin, BigDecimal montoMax) {
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
            System.err.println("[EgresoDao] Error historial por monto: " + e.getMessage());
        }
        return lista;
    }

    public List<RegistroEgreso> listarAnulados(int idHogar) {
        return listarConParam(SQL_SELECT_ANULADOS, idHogar);
    }

    // ── Actualizar ────────────────────────────────────────────────────────────

    public boolean actualizar(RegistroEgreso egreso) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {
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
            System.err.println("[EgresoDao] Error al actualizar: " + e.getMessage());
        }
        return false;
    }

    public boolean marcarComoPagada(int idEgreso, int idHogar) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_MARCAR_PAGADA)) {
            ps.setInt(1, idEgreso);
            ps.setInt(2, idHogar);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[EgresoDao] Error al marcar pagada: " + e.getMessage());
        }
        return false;
    }

    // ── Anular / Reactivar ────────────────────────────────────────────────────

    /**
     * Método unificado para cambiar el estado de un egreso.
     * nuevoEstado debe ser 'Activo' o 'Anulado'.
     * Retorna true si se actualizó al menos 1 fila.
     */
    public boolean cambiarEstado(int idEgreso, int idHogar, String nuevoEstado) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_CAMBIAR_ESTADO)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idEgreso);
            ps.setInt(3, idHogar);
            int filas = ps.executeUpdate();
            System.out.println("[EgresoDao] cambiarEstado → id=" + idEgreso
                    + " hogar=" + idHogar + " estado=" + nuevoEstado + " filasAfectadas=" + filas);
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("[EgresoDao] Error al cambiar estado: " + e.getMessage());
        }
        return false;
    }

    public boolean anular(int idEgreso, int idHogar) {
        return cambiarEstado(idEgreso, idHogar, "Anulado");
    }

    public boolean reactivar(int idEgreso, int idHogar) {
        return cambiarEstado(idEgreso, idHogar, "Activo");
    }

    // ── Resumen / Totales ─────────────────────────────────────────────────────

    public void actualizarVencidas() {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_VENCER_AUTOMATICO)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[EgresoDao] Error actualizando vencidas: " + e.getMessage());
        }
    }

    public int[] obtenerResumen(int idHogar) {
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
            System.err.println("[EgresoDao] Error al obtener resumen: " + e.getMessage());
        }
        return r;
    }

    public BigDecimal[] obtenerTotalPagado(int idHogar) {
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
            System.err.println("[EgresoDao] Error total pagado: " + e.getMessage());
        }
        return t;
    }

    // ── Categorías ────────────────────────────────────────────────────────────

    public List<Object[]> listarCategorias() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT IDCategoriaEgreso, NombreCategoriaEgreso " +
                     "FROM Categorias_Egresos ORDER BY IDCategoriaEgreso";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                lista.add(new Object[]{rs.getInt(1), rs.getString(2)});
        } catch (SQLException e) {
            System.err.println("[EgresoDao] Error al listar categorías: " + e.getMessage());
        }
        return lista;
    }

    // ── Mapeo interno ─────────────────────────────────────────────────────────

    private RegistroEgreso mapear(ResultSet rs) throws SQLException {
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

    private List<RegistroEgreso> listarConParam(String sql, int idHogar) {
        List<RegistroEgreso> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[EgresoDao] Error al listar: " + e.getMessage());
        }
        return lista;
    }
}
