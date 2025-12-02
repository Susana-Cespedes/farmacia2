
package vista.proveedor;

import dao.ProveedorDAO;
import modelo.Proveedor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EditarProveedorDialog extends JDialog {
    private JTextField txtId, txtNombre, txtTelefono;
    private JButton btnGuardar, btnCancelar;

    public EditarProveedorDialog(JFrame parent, Proveedor proveedor) {
        super(parent, "Editar Proveedor", true);
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(4, 2, 10, 10));

        // Campos
        add(new JLabel("ID:"));
        txtId = new JTextField(String.valueOf(proveedor.getIdProveedor()));
        txtId.setEditable(false); // No permitir cambiar ID
        add(txtId);

        add(new JLabel("Nombre:"));
        txtNombre = new JTextField(proveedor.getNombre());
        add(txtNombre);

        add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField(proveedor.getTelefono());
        add(txtTelefono);

        // Botones
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        add(btnGuardar);
        add(btnCancelar);

        // Acción Guardar
        btnGuardar.addActionListener((ActionEvent e) -> {
            if (validarCampos()) {
                proveedor.setNombre(txtNombre.getText());
                proveedor.setTelefono(txtTelefono.getText());

                ProveedorDAO dao = new ProveedorDAO();
                if (dao.actualizar(proveedor)) {
                    JOptionPane.showMessageDialog(this, "Proveedor actualizado correctamente");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar proveedor");
                }
            }
        });

        btnCancelar.addActionListener(e -> dispose());
    }

    private boolean validarCampos() {
        return !txtNombre.getText().isEmpty() && !txtTelefono.getText().isEmpty();
    }
}
