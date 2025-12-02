
package vista.venta;

import dao.VentaDAO;
import modelo.Venta;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditarVentaDialog extends JDialog {
    private JTextField txtIdVenta;
    private JTextField txtFecha;
    private int idVenta;

    public EditarVentaDialog(JFrame parent, int idVenta) {
        super(parent, "Editar Venta", true);
        this.idVenta = idVenta;
        setSize(400, 200);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("ID Venta:"));
        txtIdVenta = new JTextField(String.valueOf(idVenta));
        txtIdVenta.setEditable(false);
        add(txtIdVenta);

        add(new JLabel("Fecha (yyyy-MM-dd):"));
        txtFecha = new JTextField();
        add(txtFecha);

        JButton btnGuardar = new JButton("Guardar Cambios");
        add(new JLabel());
        add(btnGuardar);

        cargarDatosVenta();

        btnGuardar.addActionListener(e -> guardarCambios());
    }

    private void cargarDatosVenta() {
        VentaDAO dao = new VentaDAO();
        Venta venta = dao.obtenerPorId(idVenta); // Debes crear este método en VentaDAO
        if (venta != null) {
            txtFecha.setText(new SimpleDateFormat("yyyy-MM-dd").format(venta.getFecha()));
        }
    }

    private void guardarCambios() {
        try {
            Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(txtFecha.getText());
            VentaDAO dao = new VentaDAO();
            if (dao.actualizar(new Venta(idVenta, fecha))) {
                JOptionPane.showMessageDialog(this, "Venta actualizada correctamente");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar la venta");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
