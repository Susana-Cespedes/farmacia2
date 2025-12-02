
package dao;

import modelo.Conexion;
import modelo.Usuario;
import java.sql.*;

public class UsuarioDAO {
    public Usuario validarUsuario(String username, String password) {
        String sql = "SELECT * FROM Usuario WHERE username = ? AND password_hash = SHA2(?, 256)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Usuario(rs.getInt("id_usuario"), rs.getString("username"), rs.getInt("id_rol"));
            }
        } catch (SQLException e) {
            System.err.println("Error al validar usuario: " + e.getMessage());
        }
        return null;
    }
}
