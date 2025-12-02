
package modelo;

public class DetalleVenta {
    private int idVenta;
    private int idLote;
    private int cantidad;

    public DetalleVenta(int idVenta, int idLote, int cantidad) {
        this.idVenta = idVenta;
        this.idLote = idLote;
        this.cantidad = cantidad;
    }

    public int getIdVenta() { return idVenta; }
    public int getIdLote() { return idLote; }
    public int getCantidad() { return cantidad; }
}
