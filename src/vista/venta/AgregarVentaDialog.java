
package vista.venta;

import dao.VentaDAO;
import dao.DetalleVentaDAO;
import dao.LoteDAO;
import modelo.Venta;
import modelo.DetalleVenta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AgregarVentaDialog extends JDialog {
    private JTextField txtIdVenta;
    private JTextField txtFecha;
    private JTable tablaLotes;
    private DefaultTableModel modeloLotes;

    public AgregarVentaDialog(JFrame parent) {
        super(parent, "Registrar Venta", true);
        setSize(700, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Panel superior
        JPanel panelDatos = new JPanel(new GridLayout(2, 2, 10, 10));
        panelDatos.add(new JLabel("ID Venta:"));
        txtIdVenta = new JTextField();
        panelDatos.add(txtIdVenta);

        panelDatos.add(new JLabel("Fecha (yyyy-MM-dd):"));
        txtFecha = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        panelDatos.add(txtFecha);
        add(panelDatos, BorderLayout.NORTH);

        // Tabla lotes
        modeloLotes = new DefaultTableModel(new String[]{"ID Lote", "Medicamento", "Cantidad", "Stock"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0 || column == 2; // Solo ID Lote y Cantidad editables
            }
        };
        tablaLotes = new JTable(modeloLotes);
        add(new JScrollPane(tablaLotes), BorderLayout.CENTER);

        // Listener para actualizar Medicamento y Stock al ingresar ID Lote
        modeloLotes.addTableModelListener(e -> {
            int fila = e.getFirstRow();
            int columna = e.getColumn();
            if (columna == 0) { // ID Lote
                Object valor = modeloLotes.getValueAt(fila, columna);
                if (valor != null && !valor.toString().trim().isEmpty()) {
                    try {
                        int idLote = Integer.parseInt(valor.toString());
                        LoteDAO loteDAO = new LoteDAO();
                        String nombreMedicamento = loteDAO.obtenerNombreMedicamentoPorLote(idLote);
                        int stock = loteDAO.obtenerStock(idLote);
                        modeloLotes.setValueAt(nombreMedicamento, fila, 1); // Medicamento
                        modeloLotes.setValueAt(stock, fila, 3); // Stock
                        // ✅ NO tocar Cantidad
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "ID Lote inválido");
                    }
                }
            }
        });

        // Panel botones
        JPanel panelBotones = new JPanel();
        JButton btnAgregarFila = new JButton("Agregar Lote");
        JButton btnEliminarFila = new JButton("Eliminar Lote");
        JButton btnGuardar = new JButton("Guardar Venta");
        panelBotones.add(btnAgregarFila);
        panelBotones.add(btnEliminarFila);
        panelBotones.add(btnGuardar);
        add(panelBotones, BorderLayout.SOUTH);

        btnAgregarFila.addActionListener(e -> modeloLotes.addRow(new Object[]{"", "", "", ""}));
        btnEliminarFila.addActionListener(e -> {
            int fila = tablaLotes.getSelectedRow();
            if (fila != -1) modeloLotes.removeRow(fila);
        });

        btnGuardar.addActionListener(e -> guardarVenta());
    }

    private void guardarVenta() {
        try {
            int idVenta = Integer.parseInt(txtIdVenta.getText());
            Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(txtFecha.getText());

            VentaDAO ventaDAO = new VentaDAO();
            if (ventaDAO.existeIdVenta(idVenta)) {
                JOptionPane.showMessageDialog(this, "El ID de venta ya existe");
                return;
            }

            if (modeloLotes.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Debe agregar al menos un lote");
                return;
            }

            LoteDAO loteDAO = new LoteDAO();
            DetalleVentaDAO detalleDAO = new DetalleVentaDAO();

            // Validar stock antes de guardar
            for (int i = 0; i < modeloLotes.getRowCount(); i++) {
                int cantidad = Integer.parseInt(modeloLotes.getValueAt(i, 2).toString());
                int stock = Integer.parseInt(modeloLotes.getValueAt(i, 3).toString());
                if (cantidad > stock) {
                    JOptionPane.showMessageDialog(this, "Stock insuficiente en la fila " + (i + 1));
                    return;
                }
            }

            // Insertar venta
            if (ventaDAO.agregar(new Venta(idVenta, fecha))) {
                for (int i = 0; i < modeloLotes.getRowCount(); i++) {
                    int idLote = Integer.parseInt(modeloLotes.getValueAt(i, 0).toString());
                    int cantidad = Integer.parseInt(modeloLotes.getValueAt(i, 2).toString());
                    detalleDAO.agregarDetalle(new DetalleVenta(idVenta, idLote, cantidad));
                    loteDAO.reducirStock(idLote, cantidad);
                }
                JOptionPane.showMessageDialog(this, "Venta registrada correctamente");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar la venta");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
