
package modelo;
import java.util.Date;

public class Compra {
    private int idCompra;
    private Date fecha;
    private int idProveedor;

    public Compra(int idCompra, Date fecha, int idProveedor) {
        this.idCompra = idCompra;
        this.fecha = fecha;
        this.idProveedor = idProveedor;
    }

    public int getIdCompra() { return idCompra; }
    public Date getFecha() { return fecha; }
    public int getIdProveedor() { return idProveedor; }
}
