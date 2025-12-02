
package vista.venta;

import dao.VentaDAO;
import modelo.Venta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentaFrame extends JFrame {
    private JTable tablaVentas;
    private DefaultTableModel modeloTabla;

    public VentaFrame() {
        setTitle("Lista de Ventas");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        modeloTabla = new DefaultTableModel(new String[]{"ID Venta", "Fecha"}, 0);
        tablaVentas = new JTable(modeloTabla);
        add(new JScrollPane(tablaVentas), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        add(panelBotones, BorderLayout.SOUTH);

        btnActualizar.addActionListener(e -> cargarVentas());

        btnEliminar.addActionListener(e -> eliminarVenta());

        btnEditar.addActionListener(e -> editarVenta());

        cargarVentas();
    }

    private void cargarVentas() {
        modeloTabla.setRowCount(0);
        VentaDAO dao = new VentaDAO();
        List<Venta> lista = dao.listar();
        for (Venta v : lista) {
            modeloTabla.addRow(new Object[]{v.getIdVenta(), v.getFecha()});
        }
    }

    private void eliminarVenta() {
        int fila = tablaVentas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una venta para eliminar");
            return;
        }
        int idVenta = (int) modeloTabla.getValueAt(fila, 0);
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Eliminar la venta seleccionada?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            VentaDAO dao = new VentaDAO();
            if (dao.eliminar(idVenta)) {
                JOptionPane.showMessageDialog(this, "Venta eliminada correctamente");
                cargarVentas();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar la venta");
            }
        }
    }

    private void editarVenta() {
        int fila = tablaVentas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una venta para editar");
            return;
        }
        int idVenta = (int) modeloTabla.getValueAt(fila, 0);
        new EditarVentaDialog(this, idVenta).setVisible(true);
        cargarVentas();
    }
}
