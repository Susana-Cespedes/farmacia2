
package dao;
import modelo.Conexion;
import modelo.Venta;
import java.sql.*;
import java.util.*;

public class VentaDAO {
    public boolean agregar(Venta v) {
        String sql = "INSERT INTO Venta (id_venta, fecha) VALUES (?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, v.getIdVenta());
            ps.setDate(2, new java.sql.Date(v.getFecha().getTime()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al agregar venta: " + e.getMessage());
            return false;
        }
    }


    public Venta obtenerPorId(int idVenta) {
        String sql = "SELECT * FROM Venta WHERE id_venta=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVenta);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Venta(rs.getInt("id_venta"), rs.getDate("fecha"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener venta: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizar(Venta venta) {
        String sql = "UPDATE Venta SET fecha=? WHERE id_venta=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(venta.getFecha().getTime()));
            ps.setInt(2, venta.getIdVenta());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar venta: " + e.getMessage());
            return false;
        }
    }


    public boolean existeIdVenta(int idVenta) {
        String sql = "SELECT COUNT(*) FROM Venta WHERE id_venta=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVenta);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error al verificar ID venta: " + e.getMessage());
        }
        return false;
    }

    public List<Venta> listar() {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT * FROM Venta";
        try (Connection con = Conexion.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Venta(rs.getInt("id_venta"), rs.getDate("fecha")));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar ventas: " + e.getMessage());
        }
        return lista;
    }


    public boolean eliminar(int idVenta) {
        String sqlObtenerDetalles = "SELECT id_lote, cantidad FROM DetalleVenta WHERE id_venta=?";
        String sqlEliminarDetalle = "DELETE FROM DetalleVenta WHERE id_venta=?";
        String sqlEliminarVenta = "DELETE FROM Venta WHERE id_venta=?";

        try (Connection con = Conexion.getConexion()) {
            con.setAutoCommit(false);

            // 1. Obtener detalles de la venta
            PreparedStatement psDetalles = con.prepareStatement(sqlObtenerDetalles);
            psDetalles.setInt(1, idVenta);
            ResultSet rs = psDetalles.executeQuery();

            // 2. Sumar stock a cada lote
            PreparedStatement psActualizarStock = con.prepareStatement("UPDATE Lote SET cantidad = cantidad + ? WHERE id_lote=?");
            while (rs.next()) {
                int idLote = rs.getInt("id_lote");
                int cantidad = rs.getInt("cantidad");
                psActualizarStock.setInt(1, cantidad);
                psActualizarStock.setInt(2, idLote);
                psActualizarStock.executeUpdate();
            }

            // 3. Eliminar detalles
            PreparedStatement psEliminarDetalle = con.prepareStatement(sqlEliminarDetalle);
            psEliminarDetalle.setInt(1, idVenta);
            psEliminarDetalle.executeUpdate();

            // 4. Eliminar venta
            PreparedStatement psEliminarVenta = con.prepareStatement(sqlEliminarVenta);
            psEliminarVenta.setInt(1, idVenta);
            psEliminarVenta.executeUpdate();

            con.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al eliminar venta y actualizar stock: " + e.getMessage());
            return false;
        }
    }

}
