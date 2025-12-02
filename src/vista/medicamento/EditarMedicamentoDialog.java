
package vista.medicamento;

import dao.MedicamentoDAO;
import modelo.Medicamento;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EditarMedicamentoDialog extends JDialog {
    private JTextField txtId, txtNombre, txtPresentacion, txtPrincipioActivo;
    private JButton btnGuardar, btnCancelar;

    public EditarMedicamentoDialog(JFrame parent, Medicamento medicamento) {
        super(parent, "Editar Medicamento", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(5, 2, 10, 10));

        // Campos
        add(new JLabel("ID:"));
        txtId = new JTextField(String.valueOf(medicamento.getIdMedicamento()));
        txtId.setEditable(false); // No permitir cambiar ID
        add(txtId);

        add(new JLabel("Nombre:"));
        txtNombre = new JTextField(medicamento.getNombre());
        add(txtNombre);

        add(new JLabel("Presentación:"));
        txtPresentacion = new JTextField(medicamento.getPresentacion());
        add(txtPresentacion);

        add(new JLabel("Principio Activo:"));
        txtPrincipioActivo = new JTextField(medicamento.getPrincipioActivo());
        add(txtPrincipioActivo);

        // Botones
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        add(btnGuardar);
        add(btnCancelar);

        // Acción Guardar
        btnGuardar.addActionListener((ActionEvent e) -> {
            if (validarCampos()) {
                medicamento.setNombre(txtNombre.getText());
                medicamento.setPresentacion(txtPresentacion.getText());
                medicamento.setPrincipioActivo(txtPrincipioActivo.getText());

                MedicamentoDAO dao = new MedicamentoDAO();
                if (dao.actualizar(medicamento)) {
                    JOptionPane.showMessageDialog(this, "Medicamento actualizado correctamente");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar medicamento");
                }
            }
        });

        btnCancelar.addActionListener(e -> dispose());
    }

    private boolean validarCampos() {
        return !txtNombre.getText().isEmpty() &&
                !txtPresentacion.getText().isEmpty() &&
                !txtPrincipioActivo.getText().isEmpty();
    }
}
