package com.smarthome.smarthome_budget.dao;

import com.smarthome.smarthome_budget.modelo.ListaCompras;
import com.smarthome.smarthome_budget.basedatos.claseConexion;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ListaComprasDao {

    private static final String SQL_INSERT =
        "INSERT INTO Lista_Compras (IDHogar, NombreLista, FechaCreacion, EstadoLista) " +
        "VALUES (?, ?, NOW(), 'Pendiente')";
    private static final String SQL_SELECT_ALL =
        "SELECT lc.*, " +
        "  (SELECT COUNT(*) FROM Detalle_ListaCompras d WHERE d.IDListaCompras = lc.IDListaCompras) AS totalProductos, " +
        "  (SELECT COUNT(*) FROM Detalle_ListaCompras d WHERE d.IDListaCompras = lc.IDListaCompras AND d.Comprado = true) AS totalComprados " +
        "FROM Lista_Compras lc WHERE lc.IDHogar = ? ORDER BY lc.FechaCreacion DESC";
    private static final String SQL_SELECT_BY_ID =
        "SELECT lc.*, " +
        "  (SELECT COUNT(*) FROM Detalle_ListaCompras d WHERE d.IDListaCompras = lc.IDListaCompras) AS totalProductos, " +
        "  (SELECT COUNT(*) FROM Detalle_ListaCompras d WHERE d.IDListaCompras = lc.IDListaCompras AND d.Comprado = true) AS totalComprados " +
        "FROM Lista_Compras lc WHERE lc.IDListaCompras = ? AND lc.IDHogar = ?";
    private static final String SQL_UPDATE =
        "UPDATE Lista_Compras SET NombreLista=?, EstadoLista=? WHERE IDListaCompras=? AND IDHogar=?";
    private static final String SQL_DELETE =
        "DELETE FROM Lista_Compras WHERE IDListaCompras=? AND IDHogar=?";
    private static final String SQL_RECALCULAR_ESTADO =
        "UPDATE Lista_Compras SET EstadoLista = " +
        "  CASE " +
        "    WHEN (SELECT COUNT(*) FROM Detalle_ListaCompras d WHERE d.IDListaCompras = ? AND d.Comprado = false) = 0 " +
        "     AND (SELECT COUNT(*) FROM Detalle_ListaCompras d WHERE d.IDListaCompras = ?) > 0 " +
        "    THEN 'Completa' " +
        "    WHEN (SELECT COUNT(*) FROM Detalle_ListaCompras d WHERE d.IDListaCompras = ? AND d.Comprado = true) > 0 " +
        "    THEN 'En progreso' " +
        "    ELSE 'Pendiente' " +
        "  END " +
        "WHERE IDListaCompras = ?";
    private static final String SQL_EXISTE_NOMBRE =
        "SELECT COUNT(*) FROM Lista_Compras WHERE IDHogar = ? AND TRIM(NombreLista) = TRIM(?)";
    private static final String SQL_BUSCAR_ACTIVA_POR_NOMBRE =
        "SELECT lc.*, " +
        "  (SELECT COUNT(*) FROM Detalle_ListaCompras d WHERE d.IDListaCompras = lc.IDListaCompras) AS totalProductos, " +
        "  (SELECT COUNT(*) FROM Detalle_ListaCompras d WHERE d.IDListaCompras = lc.IDListaCompras AND d.Comprado = true) AS totalComprados " +
        "FROM Lista_Compras lc " +
        "WHERE lc.IDHogar = ? AND TRIM(lc.NombreLista) = TRIM(?) " +
        "  AND lc.EstadoLista IN ('Pendiente','En progreso') LIMIT 1";

    // ── CRUD ────────────────────────────────────────────────────────────────

    /*
     * Verifica si ya existe una lista con ese nombre exacto (ignora espacios extremos).
     */
    public boolean existeNombre(int idHogar, String nombreLista) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_EXISTE_NOMBRE)) {
            ps.setInt(1, idHogar);
            ps.setString(2, nombreLista.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error verificando nombre lista: " + e.getMessage());
        }
        return false;
    }

    public int insertar(ListaCompras lista) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, lista.getIdHogar());
            ps.setString(2, lista.getNombreLista().trim());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error insertando lista: " + e.getMessage());
        }
        return -1;
    }

    public List<ListaCompras> listarPorHogar(int idHogar) {
        List<ListaCompras> lista = new ArrayList<>();
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL)) {
            ps.setInt(1, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listando listas: " + e.getMessage());
        }
        return lista;
    }

    public ListaCompras obtenerPorId(int idLista, int idHogar) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, idLista);
            ps.setInt(2, idHogar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo lista: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizar(ListaCompras lista) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, lista.getNombreLista().trim());
            ps.setString(2, lista.getEstadoLista());
            ps.setInt(3, lista.getIdListaCompras());
            ps.setInt(4, lista.getIdHogar());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error actualizando lista: " + e.getMessage());
        }
        return false;
    }

    public boolean eliminar(int idLista, int idHogar) {
        String sqlDet = "DELETE FROM Detalle_ListaCompras WHERE IDListaCompras = ?";
        try (Connection con = claseConexion.MetodoConectar()) {
            try (PreparedStatement psDet = con.prepareStatement(sqlDet)) {
                psDet.setInt(1, idLista);
                psDet.executeUpdate();
            }
            try (PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {
                ps.setInt(1, idLista);
                ps.setInt(2, idHogar);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error eliminando lista: " + e.getMessage());
        }
        return false;
    }

    public void recalcularEstado(int idLista) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_RECALCULAR_ESTADO)) {
            ps.setInt(1, idLista);
            ps.setInt(2, idLista);
            ps.setInt(3, idLista);
            ps.setInt(4, idLista);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error recalculando estado: " + e.getMessage());
        }
    }

    /*
     * Busca una lista activa (Pendiente o En progreso) por nombre exacto en el hogar.
     * Usado por autoAnadirALista del InventarioServlet.
     */
    public ListaCompras buscarActivaPorNombre(int idHogar, String nombre) {
        try (Connection con = claseConexion.MetodoConectar();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_ACTIVA_POR_NOMBRE)) {
            ps.setInt(1, idHogar);
            ps.setString(2, nombre.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error buscando lista por nombre: " + e.getMessage());
        }
        return null;
    }

    private ListaCompras mapear(ResultSet rs) throws SQLException {
        ListaCompras l = new ListaCompras();
        l.setIdListaCompras(rs.getInt("IDListaCompras"));
        l.setIdHogar(rs.getInt("IDHogar"));
        l.setNombreLista(rs.getString("NombreLista"));
        l.setFechaCreacion(rs.getObject("FechaCreacion", LocalDateTime.class));
        l.setEstadoLista(rs.getString("EstadoLista"));
        int total    = rs.getInt("totalProductos");
        int comprados = rs.getInt("totalComprados");
        l.setTotalProductos(total);
        l.setTotalComprados(comprados);
        l.setTotalPendientes(total - comprados);
        return l;
    }
}
