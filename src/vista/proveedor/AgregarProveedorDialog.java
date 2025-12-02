
package vista.proveedor;

import dao.ProveedorDAO;
import modelo.Proveedor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AgregarProveedorDialog extends JDialog {
    private JTextField txtId, txtNombre, txtTelefono;
    private JButton btnGuardar, btnCancelar;

    public AgregarProveedorDialog(JFrame parent) {
        super(parent, "Agregar Proveedor", true);
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(4, 2, 10, 10));

        // Campos
        add(new JLabel("ID:"));
        txtId = new JTextField();
        add(txtId);

        add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        add(txtNombre);

        add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        add(txtTelefono);

        // Botones
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        add(btnGuardar);
        add(btnCancelar);

        // Acción Guardar
        btnGuardar.addActionListener((ActionEvent e) -> {
            if (validarCampos()) {
                Proveedor p = new Proveedor(
                        Integer.parseInt(txtId.getText()),
                        txtNombre.getText(),
                        txtTelefono.getText()
                );
                ProveedorDAO dao = new ProveedorDAO();

                if (dao.existeIdProveedor(Integer.parseInt(txtId.getText()))) {
                    JOptionPane.showMessageDialog(this, "El ID de proveedor ya existe. Ingrese otro.");
                    return;
                }

                if (dao.agregar(p)) {
                    JOptionPane.showMessageDialog(this, "Proveedor agregado correctamente");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al agregar proveedor");
                }
            }
        });

        btnCancelar.addActionListener(e -> dispose());
    }

    private boolean validarCampos() {
        if (txtId.getText().isEmpty() || txtNombre.getText().isEmpty() || txtTelefono.getText().isEmpty()) {
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
