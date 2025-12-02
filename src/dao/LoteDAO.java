
package dao;
import modelo.Conexion;
import modelo.Lote;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoteDAO {
    public boolean agregarLote(Lote lote) {
        String sql = "INSERT INTO Lote (id_lote, id_medicamento, fecha_caducidad, cantidad) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, lote.getIdLote());
            ps.setInt(2, lote.getIdMedicamento());
            ps.setDate(3, new java.sql.Date(lote.getFechaCaducidad().getTime()));
            ps.setInt(4, lote.getCantidad());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al agregar lote: " + e.getMessage());
            return false;
        }
    }



    public int obtenerStock(int idLote) {
        String sql = "SELECT cantidad FROM Lote WHERE id_lote=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idLote);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("cantidad");
        } catch (SQLException e) {
            System.err.println("Error al obtener stock: " + e.getMessage());
        }
        return 0;
    }

    public String obtenerNombreMedicamentoPorLote(int idLote) {
        String sql = "SELECT m.nombre FROM Lote l JOIN Medicamento m ON l.id_medicamento = m.id_medicamento WHERE l.id_lote=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idLote);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("nombre");
        } catch (SQLException e) {
            System.err.println("Error al obtener nombre del medicamento: " + e.getMessage());
        }
        return "Desconocido";
    }



    public boolean reducirStock(int idLote, int cantidad) {
        String sql = "UPDATE Lote SET cantidad = cantidad - ? WHERE id_lote=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, idLote);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al reducir stock: " + e.getMessage());
            return false;
        }
    }



    public List<Lote> listarPorCompra(int idCompra) {
        List<Lote> lista = new ArrayList<>();
        String sql = "SELECT l.* FROM Lote l INNER JOIN DetalleCompra dc ON l.id_lote = dc.id_lote WHERE dc.id_compra=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCompra);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Lote(rs.getInt("id_lote"), rs.getInt("id_medicamento"), rs.getDate("fecha_caducidad"), rs.getInt("cantidad")));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar lotes por compra: " + e.getMessage());
        }
        return lista;
    }


    public boolean existeIdLote(int idLote) {
        String sql = "SELECT COUNT(*) FROM Lote WHERE id_lote=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idLote);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error al verificar ID lote: " + e.getMessage());
        }
        return false;
    }
}
