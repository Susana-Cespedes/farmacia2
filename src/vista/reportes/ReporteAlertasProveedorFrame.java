package vista.reportes;



import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ReporteAlertasProveedorFrame extends JFrame {

    private JTable tablaAlertas;
    private DefaultTableModel modeloTabla;

    public ReporteAlertasProveedorFrame() {
        setTitle("Reporte: Alertas de vencimiento (Medicamentos próximos a caducar)");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        // Modelo de la tabla
        modeloTabla = new DefaultTableModel(new Object[]{"Medicamento", "ID Lote", "Fecha Caducidad", "Cantidad"}, 0);
        tablaAlertas = new JTable(modeloTabla);

        JScrollPane scrollPane = new JScrollPane(tablaAlertas);
        add(scrollPane, BorderLayout.CENTER);

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargarDatos());
        add(btnActualizar, BorderLayout.SOUTH);

        cargarDatos();
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0); // Limpiar tabla

        String query = """
            SELECT m.nombre AS Medicamento, l.id_lote, l.fecha_caducidad, l.cantidad
            FROM Lote l
            JOIN Medicamento m ON l.id_medicamento = m.id_medicamento
            WHERE l.fecha_caducidad BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY)
            ORDER BY l.fecha_caducidad ASC;
        """;

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/farmacia", "root", "root");
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String medicamento = rs.getString("Medicamento");
                int idLote = rs.getInt("id_lote");
                Date fechaCaducidad = rs.getDate("fecha_caducidad");
                int cantidad = rs.getInt("cantidad");

                modeloTabla.addRow(new Object[]{medicamento, idLote, fechaCaducidad, cantidad});
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReporteAlertasProveedorFrame().setVisible(true));
    }
}
