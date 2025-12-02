
package vista.medicamento;

import dao.MedicamentoDAO;
import modelo.Medicamento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MedicamentoFrame extends JFrame {
    private JTable tablaMedicamentos;
    private DefaultTableModel modeloTabla;
    private JButton btnAgregar, btnEditar, btnEliminar, btnActualizar;

    public MedicamentoFrame() {
        setTitle("Gestión de Medicamentos");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Modelo de la tabla
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Presentación", "Principio Activo"}, 0);
        tablaMedicamentos = new JTable(modeloTabla);
        add(new JScrollPane(tablaMedicamentos), BorderLayout.CENTER);

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
        btnActualizar.addActionListener((ActionEvent e) -> cargarMedicamentos());

        btnAgregar.addActionListener((ActionEvent e) -> {
            AgregarMedicamentoDialog dialog = new AgregarMedicamentoDialog(this);
            dialog.setVisible(true);
            cargarMedicamentos(); // Actualiza la tabla después de agregar
        });

        btnEditar.addActionListener((ActionEvent e) -> {
            int fila = tablaMedicamentos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un medicamento para editar");
                return;
            }

            Medicamento med = new Medicamento(
                    (int) modeloTabla.getValueAt(fila, 0),
                    (String) modeloTabla.getValueAt(fila, 1),
                    (String) modeloTabla.getValueAt(fila, 2),
                    (String) modeloTabla.getValueAt(fila, 3)
            );

            EditarMedicamentoDialog dialog = new EditarMedicamentoDialog(this, med);
            dialog.setVisible(true);
            cargarMedicamentos(); // Actualiza la tabla después de editar
        });


        btnEliminar.addActionListener((ActionEvent e) -> {
            int fila = tablaMedicamentos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un medicamento para eliminar");
                return;
            }

            int idMedicamento = (int) modeloTabla.getValueAt(fila, 0);

            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar el medicamento seleccionado?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                MedicamentoDAO dao = new MedicamentoDAO();
                if (dao.eliminar(idMedicamento)) {
                    JOptionPane.showMessageDialog(this, "Medicamento eliminado correctamente");
                    cargarMedicamentos();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar medicamento");
                }
            }
        });


        // Cargar datos al iniciar
        cargarMedicamentos();
    }

    private void cargarMedicamentos() {
        modeloTabla.setRowCount(0); // Limpiar tabla
        MedicamentoDAO dao = new MedicamentoDAO();
        List<Medicamento> lista = dao.listar();
        for (Medicamento m : lista) {
            modeloTabla.addRow(new Object[]{m.getIdMedicamento(), m.getNombre(), m.getPresentacion(), m.getPrincipioActivo()});
        }
    }
}
