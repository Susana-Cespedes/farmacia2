
package modelo;
import java.util.Date;

public class Venta {
    private int idVenta;
    private Date fecha;

    public Venta(int idVenta, Date fecha) {
        this.idVenta = idVenta;
        this.fecha = fecha;
    }

    public int getIdVenta() { return idVenta; }
    public Date getFecha() { return fecha; }
}
