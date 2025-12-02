
package vista.medicamento;

import dao.MedicamentoDAO;
import modelo.Medicamento;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AgregarMedicamentoDialog extends JDialog {
    private JTextField txtId, txtNombre, txtPresentacion, txtPrincipioActivo;
    private JButton btnGuardar, btnCancelar;

    public AgregarMedicamentoDialog(JFrame parent) {
        super(parent, "Agregar Medicamento", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(5, 2, 10, 10));

        // Campos
        add(new JLabel("ID:"));
        txtId = new JTextField();
        add(txtId);

        add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        add(txtNombre);

        add(new JLabel("Presentación:"));
        txtPresentacion = new JTextField();
        add(txtPresentacion);

        add(new JLabel("Principio Activo:"));
        txtPrincipioActivo = new JTextField();
        add(txtPrincipioActivo);

        // Botones
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        add(btnGuardar);
        add(btnCancelar);

        // Acción Guardar
        btnGuardar.addActionListener((ActionEvent e) -> {
            if (validarCampos()) {
                Medicamento med = new Medicamento(
                        Integer.parseInt(txtId.getText()),
                        txtNombre.getText(),
                        txtPresentacion.getText(),
                        txtPrincipioActivo.getText()
                );
                MedicamentoDAO dao = new MedicamentoDAO();

                if (dao.existeIdMedicamento(Integer.parseInt(txtId.getText()))) {
                    JOptionPane.showMessageDialog(this, "El ID de medicamento ya existe. Ingrese otro.");
                    return;
                }

                if (dao.agregar(med)) {
                    JOptionPane.showMessageDialog(this, "Medicamento agregado correctamente");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al agregar medicamento");
                }
            }
        });

        // Acción Cancelar
        btnCancelar.addActionListener(e -> dispose());
    }

    private boolean validarCampos() {
        if (txtId.getText().isEmpty() || txtNombre.getText().isEmpty() ||
                txtPresentacion.getText().isEmpty() || txtPrincipioActivo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios");
            return false;
        }
        try {
            Integer.parseInt(txtId.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número");
            return false;
        }
        return true;
    }
}
