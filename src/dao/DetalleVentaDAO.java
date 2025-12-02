
package dao;
import modelo.Conexion;
import modelo.DetalleVenta;
import java.sql.*;

public class DetalleVentaDAO {
    public boolean agregarDetalle(DetalleVenta dv) {
        String sql = "INSERT INTO DetalleVenta (id_venta, id_lote, cantidad) VALUES (?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, dv.getIdVenta());
            ps.setInt(2, dv.getIdLote());
            ps.setInt(3, dv.getCantidad());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al agregar detalle de venta: " + e.getMessage());
            return false;
        }
    }
}
