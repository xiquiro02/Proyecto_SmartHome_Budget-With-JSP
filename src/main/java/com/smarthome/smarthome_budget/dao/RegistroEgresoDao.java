package com.smarthome.smarthome_budget.dao;

import com.smarthome.smarthome_budget.modelo.RegistroEgreso;
import com.smarthome.smarthome_budget.basedatos.claseConexion;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de Facturas (Registro_Egresos).
 * Todas las consultas filtran siempre por IDHogar para garantizar
 * que cada hogar solo vea sus propios datos.
 */
public class RegistroEgresoDao {

    // ─── SQL ─────────────────────────────────────────────────────────────────

    private static final String SQL_INSERT =
        "INSERT INTO Registro_Egresos (IDHogar, IDUsuario, NombreFactura, Monto, " +
        "IDCategoriaEgreso, IDMetodoPago, FechaVencimiento, Descripcion, EstadoPago) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_SELECT_ALL =
        "SELECT re.*, ce.NombreCategoriaEgreso, mp.NombreMetodoPago " +
        "FROM Registro_Egresos re " +
        "JOIN Categorias_Egresos ce ON re.IDCategoriaEgreso = ce.IDCategoriaEgreso " +
        "JOIN Metodo_Pago mp ON re.IDMetodoPago = mp.IDMetodoPago " +
        "WHERE re.IDHogar = ? ORDER BY re.FechaVencimiento ASC";

    private static final String SQL_SELECT_BY_ID =
        "SELECT re.*, ce.NombreCategoriaEgreso, mp.NombreMetodoPago " +
        "FROM Registro_Egresos re " +
        "JOIN Categorias_Egresos ce ON re.IDCategoriaEgreso = ce.IDCategoriaEgreso " +
        "JOIN Metodo_Pago mp ON re.IDMetodoPago = mp.IDMetodoPago " +
        "WHERE re.IDEgresos = ? AND re.IDHogar = ?";

    private static final String SQL_SELECT_BY_ESTADO =
        "SELECT re.*, ce.NombreCategoriaEgreso, mp.NombreMetodoPago " +
        "FROM Registro_Egresos re " +
        "JOIN Categorias_Egresos ce ON re.IDCategoriaEgreso = ce.IDCategoriaEgreso " +
        "JOIN Metodo_Pago mp ON re.IDMetodoPago = mp.IDMetodoPago " +
        "WHERE re.IDHogar = ? AND re.EstadoPago = ? ORDER BY re.FechaVencimiento ASC";

    private static final String SQL_SELECT_BY_CATEGORIA =
        "SELECT re.*, ce.NombreCategoriaEgreso, mp.NombreMetodoPago " +
        "FROM Registro_Egresos re " +
        "JOIN Categorias_Egresos ce ON re.IDCategoriaEgreso = ce.IDCategoriaEgreso " +
        "JOIN Metodo_Pago mp ON re.IDMetodoPago = mp.IDMetodoPago " +
        "WHERE re.IDHogar = ? AND re.IDCategoriaEgreso = ? ORDER BY re.FechaVencimiento ASC";

    private static final String SQL_SELECT_BY_MES =
        "SELECT re.*, ce.NombreCategoriaEgreso, mp.NombreMetodoPago " +
        "FROM Registro_Egresos re " +
        "JOIN Categorias_Egresos ce ON re.IDCategoriaEgreso = ce.IDCategoriaEgreso " +
        "JOIN Metodo_Pago mp ON re.IDMetodoPago = mp.IDMetodoPago " +
        "WHERE re.IDHogar = ? AND MONTH(re.FechaVencimiento) = ? AND YEAR(re.FechaVencimiento) = ? " +
        "ORDER BY re.FechaVencimiento ASC";

    private static final String SQL_SELECT_PAGADAS =
        "SELECT re.*, ce.NombreCategoriaEgreso, mp.NombreMetodoPago " +
        "FROM Registro_Egresos re " +
        "JOIN Categorias_Egresos ce ON re.IDCategoriaEgreso = ce.IDCategoriaEgreso " +
        "JOIN Metodo_Pago mp ON re.IDMetodoPago = mp.IDMetodoPago " +
        "WHERE re.IDHogar = ? AND re.EstadoPago = 'Pagada' ORDER BY re.FechaPago DESC";

    private static final String SQL_SELECT_PAGADAS_BY_CATEGORIA =
        "SELECT re.*, ce.NombreCategoriaEgreso, mp.NombreMetodoPago " +
        "FROM Registro_Egresos re " +
        "JOIN Categorias_Egresos ce ON re.IDCategoriaEgreso = ce.IDCategoriaEgreso " +
        "JOIN Metodo_Pago mp ON re.IDMetodoPago = mp.IDMetodoPago " +
        "WHERE re.IDHogar = ? AND re.EstadoPago = 'Pagada' AND re.IDCategoriaEgreso = ? " +
        "ORDER BY re.FechaPago DESC";

    private static final String SQL_SELECT_PAGADAS_BY_MES =
        "SELECT re.*, ce.NombreCategoriaEgreso, mp.NombreMetodoPago " +
        "FROM Registro_Egresos re " +
        "JOIN Categorias_Egresos ce ON re.IDCategoriaEgreso = ce.IDCategoriaEgreso " +
        "JOIN Metodo_Pago mp ON re.IDMetodoPago = mp.IDMetodoPago " +
        "WHERE re.IDHogar = ? AND re.EstadoPago = 'Pagada' " +
        "AND MONTH(re.FechaPago) = ? AND YEAR(re.FechaPago) = ? " +
        "ORDER BY re.FechaPago DESC";

    private static final String SQL_SELECT_PAGADAS_BY_MONTO =
        "SELECT re.*, ce.NombreCategoriaEgreso, mp.NombreMetodoPago " +
        "FROM Registro_Egresos re " +
        "JOIN Categorias_Egresos ce ON re.IDCategoriaEgreso = ce.IDCategoriaEgreso " +
        "JOIN Metodo_Pago mp ON re.IDMetodoPago = mp.IDMetodoPago " +
        "WHERE re.IDHogar = ? AND re.EstadoPago = 'Pagada' " +
        "AND re.Monto >= ? AND re.Monto <= ? ORDER BY re.Monto ASC";

    private static final String SQL_UPDATE =
        "UPDATE Registro_Egresos SET NombreFactura=?, Monto=?, IDCategoriaEgreso=?, " +
        "IDMetodoPago=?, FechaVencimiento=?, Descripcion=?, EstadoPago=?, FechaPago=? " +
        "WHERE IDEgresos=? AND IDHogar=?";

    private static final String SQL_DELETE =
        "DELETE FROM Registro_Egresos WHERE IDEgresos=? AND IDHogar=?";

    private static final String SQL_MARCAR_PAGADA =
        "UPDATE Registro_Egresos SET EstadoPago='Pagada', FechaPago=NOW() " +
        "WHERE IDEgresos=? AND IDHogar=?";

    private static final String SQL_RESUMEN =
        "SELECT " +
        "  SUM(CASE WHEN EstadoPago='Pendiente' THEN 1 ELSE 0 END) AS totalPendientes, " +
        "  SUM(CASE WHEN EstadoPago='Pagada'    THEN 1 ELSE 0 END) AS totalPagadas, " +
        "  SUM(CASE WHEN EstadoPago='Vencida'   THEN 1 ELSE 0 END) AS totalVencidas " +
        "FROM Registro_Egresos WHERE IDHogar=?";

    private static final String SQL_TOTAL_PAGADO =
        "SELECT COALESCE(SUM(Monto),0) AS totalPagado, COUNT(*) AS cantidadPagadas " +
        "FROM Registro_Egresos WHERE IDHogar=? AND EstadoPago='Pagada'";

    private static final String SQL_VENCER_AUTOMATICO =
        "UPDATE Registro_Egresos SET EstadoPago='Vencida' " +
        "WHERE EstadoPago='Pendiente' AND FechaVencimiento < NOW()";

    // ─── Métodos CRUD ────────────────────────────────────────────────────────

    /** Registra una nueva factura. Retorna el ID generado o -1 si falla. */
    public int insertarEgreso(RegistroEgreso egreso) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, egreso.getIdHogar());
            ps.setInt(2, egreso.getIdUsuario());
            ps.setString(3, egreso.getNombreFactura().trim());
            ps.setBigDecimal(4, egreso.getMonto());
            ps.setInt(5, egreso.getIdCategoriaEgreso());
            ps.setInt(6, egreso.getIdMetodoPago());
            ps.setObject(7, egreso.getFechaVencimiento());
            ps.setString(8, egreso.getDescripcion() != null ? egreso.getDescripcion().trim() : "");
            ps.setString(9, egreso.getEstadoPago());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar egreso: " + e.getMessage());
        }
        return -1;
    }

    /** Lista todas las facturas del hogar. */
    public List<RegistroEgreso> listarPorHogar(int idHogar) {
        List<RegistroEgreso> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL)) {
            ps.setInt(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar egresos: " + e.getMessage());
        }
        return lista;
    }

    /** Obtiene una factura por ID verificando que pertenezca al hogar. */
    public RegistroEgreso obtenerPorId(int idEgreso, int idHogar) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, idEgreso);
            ps.setInt(2, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener egreso por id: " + e.getMessage());
        }
        return null;
    }

    /** Filtra facturas por estado (Pendiente / Pagada / Vencida). */
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
            System.err.println("Error al filtrar por estado: " + e.getMessage());
        }
        return lista;
    }

    /** Filtra facturas por categoría. */
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
            System.err.println("Error al filtrar por categoria: " + e.getMessage());
        }
        return lista;
    }

    /** Filtra facturas por mes y año de vencimiento. */
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
            System.err.println("Error al filtrar por mes: " + e.getMessage());
        }
        return lista;
    }

    /** Historial: solo facturas pagadas, ordenadas por fecha de pago. */
    public List<RegistroEgreso> listarPagadas(int idHogar) {
        List<RegistroEgreso> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_PAGADAS)) {
            ps.setInt(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar pagadas: " + e.getMessage());
        }
        return lista;
    }

    /** Historial filtrado por categoría. */
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
            System.err.println("Error al filtrar historial por categoria: " + e.getMessage());
        }
        return lista;
    }

    /** Historial filtrado por mes de pago. */
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
            System.err.println("Error al filtrar historial por mes: " + e.getMessage());
        }
        return lista;
    }

    /** Historial filtrado por rango de monto. */
    public List<RegistroEgreso> listarPagadasPorMonto(int idHogar, BigDecimal montoMin, BigDecimal montoMax) {
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
            System.err.println("Error al filtrar historial por monto: " + e.getMessage());
        }
        return lista;
    }

    /** Actualiza los datos de una factura. */
    public boolean actualizar(RegistroEgreso egreso) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, egreso.getNombreFactura().trim());
            ps.setBigDecimal(2, egreso.getMonto());
            ps.setInt(3, egreso.getIdCategoriaEgreso());
            ps.setInt(4, egreso.getIdMetodoPago());
            ps.setObject(5, egreso.getFechaVencimiento());
            ps.setString(6, egreso.getDescripcion() != null ? egreso.getDescripcion().trim() : "");
            ps.setString(7, egreso.getEstadoPago());
            ps.setObject(8, egreso.getFechaPago());
            ps.setInt(9, egreso.getIdEgresos());
            ps.setInt(10, egreso.getIdHogar());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar egreso: " + e.getMessage());
        }
        return false;
    }

    /** Elimina una factura. */
    public boolean eliminar(int idEgreso, int idHogar) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, idEgreso);
            ps.setInt(2, idHogar);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar egreso: " + e.getMessage());
        }
        return false;
    }

    /** Marca una factura como Pagada y registra la fecha de pago actual. */
    public boolean marcarComoPagada(int idEgreso, int idHogar) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_MARCAR_PAGADA)) {
            ps.setInt(1, idEgreso);
            ps.setInt(2, idHogar);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al marcar como pagada: " + e.getMessage());
        }
        return false;
    }

    /**
     * Actualiza automáticamente a "Vencida" las facturas cuya fecha de
     * vencimiento ya pasó y aún están como Pendiente.
     * Llamar al inicio de cada listado.
     */
    public void actualizarVencidas() {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_VENCER_AUTOMATICO)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar vencidas: " + e.getMessage());
        }
    }

    /** Resumen rápido: cantidad por estado para el dashboard. */
    public int[] obtenerResumen(int idHogar) {
        // [0]=pendientes [1]=pagadas [2]=vencidas
        int[] resumen = {0, 0, 0};
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_RESUMEN)) {
            ps.setInt(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    resumen[0] = rs.getInt("totalPendientes");
                    resumen[1] = rs.getInt("totalPagadas");
                    resumen[2] = rs.getInt("totalVencidas");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener resumen: " + e.getMessage());
        }
        return resumen;
    }

    /** Total pagado y cantidad de facturas pagadas (para el historial). */
    public BigDecimal[] obtenerTotalPagado(int idHogar) {
        // [0]=totalMonto [1]=cantidad
        BigDecimal[] totales = {BigDecimal.ZERO, BigDecimal.ZERO};
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_TOTAL_PAGADO)) {
            ps.setInt(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    totales[0] = rs.getBigDecimal("totalPagado");
                    totales[1] = new BigDecimal(rs.getInt("cantidadPagadas"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al calcular total pagado: " + e.getMessage());
        }
        return totales;
    }

    // ─── Métodos para Módulo Finanzas ────────────────────────────────────────

    /** Registra un egreso simple desde el módulo Finanzas (sin fecha vencimiento) */
    public boolean registrar(RegistroEgreso egreso) {
        String sql = "INSERT INTO Registro_Egresos (IDHogar, IDUsuario, NombreFactura, Monto, IDCategoriaEgreso, IDMetodoPago, FechaVencimiento, Descripcion, EstadoPago) " +
                     "VALUES (?, ?, ?, ?, ?, 1, NOW(), ?, 'Pagada')";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, egreso.getIdHogar());
            ps.setInt(2, egreso.getIdUsuario());
            ps.setString(3, egreso.getNombreFactura() != null ? egreso.getNombreFactura() : "Egreso");
            ps.setBigDecimal(4, egreso.getMonto());
            ps.setInt(5, egreso.getIdCategoriaEgreso());
            ps.setString(6, egreso.getDescripcion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar egreso simple: " + e.getMessage());
            return false;
        }
    }

    public java.util.List<Object[]> listarCategorias() {
        java.util.List<Object[]> lista = new java.util.ArrayList<>();
        String sql = "SELECT IDCategoriaEgreso, NombreCategoriaEgreso FROM Categorias_Egresos ORDER BY IDCategoriaEgreso";
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Object[]{rs.getInt(1), rs.getString(2)});
            }
        } catch (SQLException e) {
            System.err.println("Error al listar categorias egreso: " + e.getMessage());
        }
        return lista;
    }

    // ─── Mapeo interno ───────────────────────────────────────────────────────

    private RegistroEgreso mapear(ResultSet rs) throws SQLException {
        RegistroEgreso e = new RegistroEgreso();
        e.setIdEgresos(rs.getInt("IDEgresos"));
        e.setIdHogar(rs.getInt("IDHogar"));
        e.setIdUsuario(rs.getInt("IDUsuario"));
        e.setNombreFactura(rs.getString("NombreFactura"));
        e.setMonto(rs.getBigDecimal("Monto"));
        e.setIdCategoriaEgreso(rs.getInt("IDCategoriaEgreso"));
        e.setNombreCategoria(rs.getString("NombreCategoriaEgreso"));
        e.setIdMetodoPago(rs.getInt("IDMetodoPago"));
        e.setNombreMetodoPago(rs.getString("NombreMetodoPago"));
        e.setFechaVencimiento(rs.getObject("FechaVencimiento", LocalDateTime.class));
        e.setFechaPago(rs.getObject("FechaPago", LocalDateTime.class));
        e.setDescripcion(rs.getString("Descripcion"));
        e.setEstadoPago(rs.getString("EstadoPago"));
        return e;
    }
}
