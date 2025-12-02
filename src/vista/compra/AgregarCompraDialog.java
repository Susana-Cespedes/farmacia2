package vista.compra;

import com.toedter.calendar.JDateChooser;
import dao.CompraDAO;
import dao.LoteDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AgregarCompraDialog extends JDialog {
    private JTextField txtIdCompra;
    private JComboBox<String> comboProveedor;
    private JDateChooser dateCompra;
    private JTable tablaLotes;
    private DefaultTableModel modeloLotes;

    public AgregarCompraDialog(JFrame parent) {
        super(parent, "Registrar Compra", true);
        setSize(800, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Panel superior
        JPanel panelDatos = new JPanel(new GridLayout(3, 2, 10, 10));
        panelDatos.add(new JLabel("ID Compra:"));
        txtIdCompra = new JTextField();
        panelDatos.add(txtIdCompra);

        panelDatos.add(new JLabel("Fecha Compra:"));
        dateCompra = new JDateChooser();
        dateCompra.setDateFormatString("yyyy-MM-dd");
        panelDatos.add(dateCompra);

        panelDatos.add(new JLabel("Proveedor:"));
        comboProveedor = new JComboBox<>();
        cargarProveedores();
        panelDatos.add(comboProveedor);
        add(panelDatos, BorderLayout.NORTH);

        // Tabla lotes
        modeloLotes = new DefaultTableModel(new String[]{"ID Lote", "Medicamento", "Fecha Caducidad", "Cantidad"}, 0);
        tablaLotes = new JTable(modeloLotes);
        add(new JScrollPane(tablaLotes), BorderLayout.CENTER);

        // Configurar columna Medicamento como ComboBox
        tablaLotes.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(crearComboMedicamentos()));

        // Panel botones
        JPanel panelBotones = new JPanel();
        //JButton btnAgregarFila = new JButton("Agregar Lote");
        //JButton btnGuardar = new JButton("Guardar");


        JButton btnAgregarFila = new JButton("Agregar Lote");
        JButton btnEliminarFila = new JButton("Eliminar Lote");
        JButton btnGuardar = new JButton("Guardar");
        panelBotones.add(btnAgregarFila);
        panelBotones.add(btnEliminarFila);
        panelBotones.add(btnGuardar);


        panelBotones.add(btnAgregarFila);
        panelBotones.add(btnGuardar);
        add(panelBotones, BorderLayout.SOUTH);

        btnAgregarFila.addActionListener(e -> modeloLotes.addRow(new Object[]{"", "", "", ""}));


        btnEliminarFila.addActionListener(e -> {
            int filaSeleccionada = tablaLotes.getSelectedRow();
            if (filaSeleccionada != -1) {
                modeloLotes.removeRow(filaSeleccionada);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un lote para eliminar");
            }
        });


        btnGuardar.addActionListener(e -> guardarCompra());
    }

    private void cargarProveedores() {
        dao.ProveedorDAO dao = new dao.ProveedorDAO();
        for (modelo.Proveedor p : dao.listar()) {
            comboProveedor.addItem(p.getIdProveedor() + " - " + p.getNombre());
        }
    }

    private JComboBox<String> crearComboMedicamentos() {
        JComboBox<String> combo = new JComboBox<>();
        try (Connection con = modelo.Conexion.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT id_medicamento, nombre FROM Medicamento")) {
            while (rs.next()) {
                combo.addItem(rs.getInt("id_medicamento") + " - " + rs.getString("nombre"));
            }
        } catch (Exception e) {
            System.err.println("Error al cargar medicamentos: " + e.getMessage());
        }
        return combo;
    }

    private void guardarCompra() {
        try {
            int idCompra = Integer.parseInt(txtIdCompra.getText());
            Date fecha = dateCompra.getDate();
            if (fecha == null) {
                JOptionPane.showMessageDialog(this, "Seleccione la fecha de compra");
                return;
            }
            String proveedorSeleccionado = (String) comboProveedor.getSelectedItem();
            int idProveedor = Integer.parseInt(proveedorSeleccionado.split(" - ")[0]);

            CompraDAO compraDAO = new CompraDAO();
            if (compraDAO.existeIdCompra(idCompra)) {
                JOptionPane.showMessageDialog(this, "El ID de compra ya existe");
                return;
            }

            if (compraDAO.agregar(new modelo.Compra(idCompra, fecha, idProveedor))) {
                LoteDAO loteDAO = new LoteDAO();
                for (int i = 0; i < modeloLotes.getRowCount(); i++) {
                    int idLote = Integer.parseInt(modeloLotes.getValueAt(i, 0).toString());
                    String medicamentoStr = modeloLotes.getValueAt(i, 1).toString();
                    int idMedicamento = Integer.parseInt(medicamentoStr.split(" - ")[0]);
                    Date fechaCad = new SimpleDateFormat("yyyy-MM-dd").parse(modeloLotes.getValueAt(i, 2).toString());
                    int cantidad = Integer.parseInt(modeloLotes.getValueAt(i, 3).toString());

                    if (loteDAO.existeIdLote(idLote)) {
                        JOptionPane.showMessageDialog(this, "El ID de lote " + idLote + " ya existe");
                        return;
                    }

                    loteDAO.agregarLote(new modelo.Lote(idLote, idMedicamento, fechaCad, cantidad));
                }
                JOptionPane.showMessageDialog(this, "Compra registrada correctamente");
                dispose();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
