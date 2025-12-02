
package vista.compra;

import dao.CompraDAO;
import dao.LoteDAO;
import modelo.Compra;
import modelo.Lote;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EditarCompraDialog extends JDialog {
    private JTextField txtIdCompra;
    private JComboBox<String> comboProveedor;
    private JTextField txtFecha;
    private JTable tablaLotes;
    private DefaultTableModel modeloLotes;
    private int idCompra;

    public EditarCompraDialog(JFrame parent, int idCompra) {
        super(parent, "Editar Compra", true);
        this.idCompra = idCompra;
        setSize(800, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Panel superior
        JPanel panelDatos = new JPanel(new GridLayout(3, 2, 10, 10));
        panelDatos.add(new JLabel("ID Compra:"));
        txtIdCompra = new JTextField(String.valueOf(idCompra));
        txtIdCompra.setEditable(false);
        panelDatos.add(txtIdCompra);

        panelDatos.add(new JLabel("Fecha (yyyy-MM-dd):"));
        txtFecha = new JTextField();
        panelDatos.add(txtFecha);

        panelDatos.add(new JLabel("Proveedor:"));
        comboProveedor = new JComboBox<>();
        cargarProveedores();
        panelDatos.add(comboProveedor);
        add(panelDatos, BorderLayout.NORTH);

        // Tabla lotes
        modeloLotes = new DefaultTableModel(new String[]{"ID Lote", "Medicamento", "Fecha Caducidad", "Cantidad"}, 0);
        tablaLotes = new JTable(modeloLotes);
        add(new JScrollPane(tablaLotes), BorderLayout.CENTER);

        // Panel botones
        JPanel panelBotones = new JPanel();
        JButton btnGuardar = new JButton("Guardar Cambios");
        panelBotones.add(btnGuardar);
        add(panelBotones, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> guardarCambios());

        cargarDatosCompra();
    }

    private void cargarProveedores() {
        dao.ProveedorDAO dao = new dao.ProveedorDAO();
        for (modelo.Proveedor p : dao.listar()) {
            comboProveedor.addItem(p.getIdProveedor() + " - " + p.getNombre());
        }
    }

    private void cargarDatosCompra() {
        CompraDAO compraDAO = new CompraDAO();
        Compra compra = compraDAO.obtenerPorId(idCompra); // Debemos crear este método en CompraDAO
        if (compra != null) {
            txtFecha.setText(new SimpleDateFormat("yyyy-MM-dd").format(compra.getFecha()));
            comboProveedor.setSelectedItem(compra.getIdProveedor() + " - " + obtenerNombreProveedor(compra.getIdProveedor()));
        }

        // Cargar lotes asociados
        LoteDAO loteDAO = new LoteDAO();
        List<Lote> lotes = loteDAO.listarPorCompra(idCompra); // Debemos crear este método en LoteDAO
        for (Lote l : lotes) {
            modeloLotes.addRow(new Object[]{
                    l.getIdLote(),
                    l.getIdMedicamento() + " - " + obtenerNombreMedicamento(l.getIdMedicamento()),
                    new SimpleDateFormat("yyyy-MM-dd").format(l.getFechaCaducidad()),
                    l.getCantidad()
            });
        }
    }

    private String obtenerNombreProveedor(int idProveedor) {
        // Puedes implementar una consulta rápida en ProveedorDAO
        return "Proveedor"; // Placeholder
    }

    private String obtenerNombreMedicamento(int idMedicamento) {
        // Puedes implementar una consulta rápida en MedicamentoDAO
        return "Medicamento"; // Placeholder
    }

    private void guardarCambios() {
        try {
            Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(txtFecha.getText());
            String proveedorSeleccionado = (String) comboProveedor.getSelectedItem();
            int idProveedor = Integer.parseInt(proveedorSeleccionado.split(" - ")[0]);

            CompraDAO compraDAO = new CompraDAO();
            if (compraDAO.actualizar(new Compra(idCompra, fecha, idProveedor))) {
                JOptionPane.showMessageDialog(this, "Compra actualizada correctamente");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar la compra");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
