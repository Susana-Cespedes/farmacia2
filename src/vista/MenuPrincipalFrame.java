
package vista;

import modelo.Usuario;
import vista.compra.AgregarCompraDialog;
import vista.compra.CompraFrame;
import vista.medicamento.MedicamentoFrame;
import vista.proveedor.ProveedorFrame;
import vista.reportes.*;
import vista.venta.AgregarVentaDialog;
import vista.venta.VentaFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MenuPrincipalFrame extends JFrame {
    private Usuario usuario;

    public MenuPrincipalFrame(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Menú Principal - Sistema Farmacia");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();

        //Menu Medicamento
        JMenu menuMedicamentos = new JMenu("Medicamentos");
        JMenuItem itemCrudMedicamentos = new JMenuItem("Gestionar Medicamentos");

        //Menu Proveedores
        JMenu menuProveedores = new JMenu("Proveedores");
        JMenuItem itemCrudProveedores = new JMenuItem("Gestionar Proveedores");

        // Menú Compras
        JMenu menuCompras = new JMenu("Compras");
        JMenuItem itemGestionarCompras = new JMenuItem("Registrar Compra");

        // Menu Ventas
        JMenu menuVentas = new JMenu("Ventas");
        JMenuItem itemRegistrarVenta = new JMenuItem("Registrar Venta");

        // Menu Ver Ventas
        JMenuItem itemVerVentas = new JMenuItem("Ver Ventas");
        // Opcion para ver lista de compras
        JMenuItem itemVerCompras = new JMenuItem("Ver Compras");
        // ✅ Menú Reportes
        JMenu menuReportes = new JMenu("Reportes");

        menuMedicamentos.add(itemCrudMedicamentos);
        menuProveedores.add(itemCrudProveedores);
        menuCompras.add(itemGestionarCompras);
        menuVentas.add(itemRegistrarVenta);
        menuVentas.add(itemVerVentas);
        menuCompras.add(itemVerCompras);

        // Agregar menus a la barra
        menuBar.add(menuVentas);
        menuBar.add(menuCompras);
        menuBar.add(menuMedicamentos);
        menuBar.add(menuProveedores);
        menuBar.add(menuReportes);

        setJMenuBar(menuBar);

        if (usuario.getUsername().equals("empleado")) {
            menuCompras.setEnabled(false);
            menuMedicamentos.setEnabled(false);
            menuProveedores.setEnabled(false);
            menuReportes.setEnabled(false);
        }

        //Medicamento
        itemCrudMedicamentos.addActionListener((ActionEvent e) -> {
            new MedicamentoFrame().setVisible(true);
        });
        //Proveedores
        itemCrudProveedores.addActionListener((ActionEvent e) -> {
            new ProveedorFrame().setVisible(true);
        });
        //Compras
        itemGestionarCompras.addActionListener(e -> {
            new AgregarCompraDialog(this).setVisible(true);
        });
        //Ventas
        itemRegistrarVenta.addActionListener(e -> new AgregarVentaDialog(this).setVisible(true));
        //Ver ventas
        itemVerVentas.addActionListener(e -> new VentaFrame().setVisible(true));

        //Ver compras
        itemVerCompras.addActionListener(e -> {
            new CompraFrame().setVisible(true);
        });

// ✅ Menú Reportes
        //JMenu menuReportes = new JMenu("Reportes");

        JMenuItem itemMedicamentosCaducar = new JMenuItem("Medicamentos próximos a caducar (30 días)");
        itemMedicamentosCaducar.addActionListener(e -> new ReporteCaducidadFrame().setVisible(true));
        menuReportes.add(itemMedicamentosCaducar);

        JMenuItem itemInventarioDisponible = new JMenuItem("Inventario disponible por medicamento");
        itemInventarioDisponible.addActionListener(e -> new ReporteInventarioFrame().setVisible(true));
        menuReportes.add(itemInventarioDisponible);

        JMenuItem itemComprasVentasProveedor = new JMenuItem("Reporte de compras y ventas por proveedor");
        itemComprasVentasProveedor.addActionListener(e -> new ReporteComprasVentasProveedorFrame().setVisible(true));
        menuReportes.add(itemComprasVentasProveedor);

        JMenuItem itemMedicamentosAgotados = new JMenuItem("Medicamentos agotados o con stock mínimo (<10)");
        itemMedicamentosAgotados.addActionListener(e -> new ReporteStockMinimoFrame().setVisible(true));
        menuReportes.add(itemMedicamentosAgotados);

        JMenuItem itemMasVendidos = new JMenuItem("Medicamentos más vendidos en el último mes");
        itemMasVendidos.addActionListener(e -> new ReporteMasVendidosFrame().setVisible(true));
        menuReportes.add(itemMasVendidos);

        JMenuItem itemAlertasProveedor = new JMenuItem("Alertas de vencimiento por proveedor");
        itemAlertasProveedor.addActionListener(e -> new ReporteAlertasProveedorFrame().setVisible(true));
        menuReportes.add(itemAlertasProveedor);

    }
}
