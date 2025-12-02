

package vista.proveedor;

import dao.ProveedorDAO;
import modelo.Proveedor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ProveedorFrame extends JFrame {
    private JTable tablaProveedores;
    private DefaultTableModel modeloTabla;
    private JButton btnAgregar, btnEditar, btnEliminar, btnActualizar;

    public ProveedorFrame() {
        setTitle("Gestión de Proveedores");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Modelo de la tabla
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Teléfono"}, 0);
        tablaProveedores = new JTable(modeloTabla);
        add(new JScrollPane(tablaProveedores), BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        btnAgregar = new JButton("Agregar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnActualizar = new JButton("Actualizar");
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnActualizar);
        add(panelBotones, BorderLayout.SOUTH);

        // Acción para actualizar la tabla
        btnActualizar.addActionListener((ActionEvent e) -> cargarProveedores());

        // Acción para agregar (conectaremos el diálogo en el siguiente paso)
        btnAgregar.addActionListener((ActionEvent e) -> {
            AgregarProveedorDialog dialog = new AgregarProveedorDialog(this);
            dialog.setVisible(true);
            cargarProveedores();
        });

        btnEditar.addActionListener((ActionEvent e) -> {
            int fila = tablaProveedores.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un proveedor para editar");
                return;
            }

            Proveedor proveedor = new Proveedor(
                    (int) modeloTabla.getValueAt(fila, 0),
                    (String) modeloTabla.getValueAt(fila, 1),
                    (String) modeloTabla.getValueAt(fila, 2)
            );

            EditarProveedorDialog dialog = new EditarProveedorDialog(this, proveedor);
            dialog.setVisible(true);
            cargarProveedores(); // Actualiza la tabla después de editar
        });

        btnEliminar.addActionListener((ActionEvent e) -> {
            int fila = tablaProveedores.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un proveedor para eliminar");
                return;
            }

            int idProveedor = (int) modeloTabla.getValueAt(fila, 0);

            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar el proveedor seleccionado?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                ProveedorDAO dao = new ProveedorDAO();
                if (dao.eliminar(idProveedor)) {
                    JOptionPane.showMessageDialog(this, "Proveedor eliminado correctamente");
                    cargarProveedores();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar proveedor");
                }
            }
        });

        // Cargar datos al iniciar
        cargarProveedores();
    }

    private void cargarProveedores() {
        modeloTabla.setRowCount(0); // Limpiar tabla
        ProveedorDAO dao = new ProveedorDAO();
        List<Proveedor> lista = dao.listar();
        for (Proveedor p : lista) {
            modeloTabla.addRow(new Object[]{p.getIdProveedor(), p.getNombre(), p.getTelefono()});
        }
    }
}
