
package vista.compra;

import dao.CompraDAO;
import modelo.Compra;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CompraFrame extends JFrame {
    private JTable tablaCompras;
    private DefaultTableModel modeloTabla;

    public CompraFrame() {
        setTitle("Lista de Compras");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        modeloTabla = new DefaultTableModel(new String[]{"ID Compra", "Fecha", "Proveedor"}, 0);
        tablaCompras = new JTable(modeloTabla);
        add(new JScrollPane(tablaCompras), BorderLayout.CENTER);


        JPanel panelBotones = new JPanel();
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        add(panelBotones, BorderLayout.SOUTH);


        btnActualizar.addActionListener(e -> cargarCompras());

        btnEliminar.addActionListener(e -> {
            int fila = tablaCompras.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione una compra para eliminar");
                return;
            }
            int idCompra = (int) modeloTabla.getValueAt(fila, 0);
            int confirmacion = JOptionPane.showConfirmDialog(this, "¿Eliminar la compra seleccionada?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                CompraDAO dao = new CompraDAO();
                if (dao.eliminar(idCompra)) {
                    JOptionPane.showMessageDialog(this, "Compra eliminada correctamente");
                    cargarCompras();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar la compra");
                }
            }
        });


        btnEditar.addActionListener(e -> {
            int fila = tablaCompras.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione una compra para editar");
                return;
            }
            int idCompra = (int) modeloTabla.getValueAt(fila, 0);
            new EditarCompraDialog(this, idCompra).setVisible(true);
            cargarCompras(); // Actualiza la lista después de editar
        });


        cargarCompras();
    }

    private void cargarCompras() {
        modeloTabla.setRowCount(0);
        CompraDAO dao = new CompraDAO();
        List<Compra> lista = dao.listar(); // Debes agregar este método en CompraDAO
        for (Compra c : lista) {
            modeloTabla.addRow(new Object[]{c.getIdCompra(), c.getFecha(), c.getIdProveedor()});
        }
    }
}
