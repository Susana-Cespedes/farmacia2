
package dao;

import modelo.Conexion;
import modelo.Medicamento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoDAO {

    public boolean agregar(Medicamento med) {
        String sql = "INSERT INTO Medicamento (id_medicamento, nombre, presentacion, principio_activo) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, med.getIdMedicamento());
            ps.setString(2, med.getNombre());
            ps.setString(3, med.getPresentacion());
            ps.setString(4, med.getPrincipioActivo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al agregar medicamento: " + e.getMessage());
            return false;
        }
    }

    public boolean existeIdMedicamento(int idMedicamento) {
        String sql = "SELECT COUNT(*) FROM Medicamento WHERE id_medicamento=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMedicamento);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar ID de medicamento: " + e.getMessage());
        }
        return false;
    }

    public boolean actualizar(Medicamento med) {
        String sql = "UPDATE Medicamento SET nombre=?, presentacion=?, principio_activo=? WHERE id_medicamento=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, med.getNombre());
            ps.setString(2, med.getPresentacion());
            ps.setString(3, med.getPrincipioActivo());
            ps.setInt(4, med.getIdMedicamento());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar medicamento: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idMedicamento) {
        String sql = "DELETE FROM Medicamento WHERE id_medicamento=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMedicamento);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar medicamento: " + e.getMessage());
            return false;
        }
    }

    public List<Medicamento> listar() {
        List<Medicamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM Medicamento";
        try (Connection con = Conexion.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Medicamento med = new Medicamento(
                        rs.getInt("id_medicamento"),
                        rs.getString("nombre"),
                        rs.getString("presentacion"),
                        rs.getString("principio_activo")
                );
                lista.add(med);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar medicamentos: " + e.getMessage());
        }
        return lista;
    }
}
