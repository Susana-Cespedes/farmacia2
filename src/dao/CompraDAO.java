
package dao;
import modelo.Conexion;
import modelo.Compra;
import java.sql.*;
import java.util.*;

public class CompraDAO {
    public boolean agregar(Compra c) {
        String sql = "INSERT INTO Compra (id_compra, fecha, id_proveedor) VALUES (?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, c.getIdCompra());
            ps.setDate(2, new java.sql.Date(c.getFecha().getTime()));
            ps.setInt(3, c.getIdProveedor());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al agregar compra: " + e.getMessage());
            return false;
        }
    }


    public Compra obtenerPorId(int idCompra) {
        String sql = "SELECT * FROM Compra WHERE id_compra=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCompra);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Compra(rs.getInt("id_compra"), rs.getDate("fecha"), rs.getInt("id_proveedor"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener compra: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizar(Compra compra) {
        String sql = "UPDATE Compra SET fecha=?, id_proveedor=? WHERE id_compra=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(compra.getFecha().getTime()));
            ps.setInt(2, compra.getIdProveedor());
            ps.setInt(3, compra.getIdCompra());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar compra: " + e.getMessage());
            return false;
        }
    }



    public boolean eliminar(int idCompra) {
        String sqlObtenerLotes = "SELECT id_lote FROM DetalleCompra WHERE id_compra=?";
        String sqlEliminarDetalle = "DELETE FROM DetalleCompra WHERE id_compra=?";
        String sqlEliminarLote = "DELETE FROM Lote WHERE id_lote=?";
        String sqlEliminarCompra = "DELETE FROM Compra WHERE id_compra=?";

        try (Connection con = Conexion.getConexion()) {
            con.setAutoCommit(false); // Iniciar transacción

            // 1. Obtener lotes asociados
            PreparedStatement psObtenerLotes = con.prepareStatement(sqlObtenerLotes);
            psObtenerLotes.setInt(1, idCompra);
            ResultSet rs = psObtenerLotes.executeQuery();

            // Guardar IDs de lotes
            List<Integer> lotes = new ArrayList<>();
            while (rs.next()) {
                lotes.add(rs.getInt("id_lote"));
            }

            // 2. Eliminar detalles
            PreparedStatement psEliminarDetalle = con.prepareStatement(sqlEliminarDetalle);
            psEliminarDetalle.setInt(1, idCompra);
            psEliminarDetalle.executeUpdate();

            // 3. Eliminar lotes (solo si no están en otros detalles)
            PreparedStatement psEliminarLote = con.prepareStatement(sqlEliminarLote);
            for (int idLote : lotes) {
                if (!loteTieneOtraCompra(con, idLote)) {
                    psEliminarLote.setInt(1, idLote);
                    psEliminarLote.executeUpdate();
                }
            }

            // 4. Eliminar compra
            PreparedStatement psEliminarCompra = con.prepareStatement(sqlEliminarCompra);
            psEliminarCompra.setInt(1, idCompra);
            psEliminarCompra.executeUpdate();

            con.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al eliminar compra y lotes: " + e.getMessage());
            return false;
        }
    }

    // Método auxiliar para verificar si el lote está en otra compra
    private boolean loteTieneOtraCompra(Connection con, int idLote) throws SQLException {
        String sql = "SELECT COUNT(*) FROM DetalleCompra WHERE id_lote=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idLote);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }



    public List<Compra> listar() {
        List<Compra> lista = new ArrayList<>();
        String sql = "SELECT * FROM Compra";
        try (Connection con = Conexion.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Compra(
                        rs.getInt("id_compra"),
                        rs.getDate("fecha"),
                        rs.getInt("id_proveedor")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar compras: " + e.getMessage());
        }
        return lista;
    }


    public boolean existeIdCompra(int idCompra) {
        String sql = "SELECT COUNT(*) FROM Compra WHERE id_compra=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCompra);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error al verificar ID compra: " + e.getMessage());
        }
        return false;
    }
}
