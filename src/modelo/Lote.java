
package modelo;
import java.util.Date;

public class Lote {
    private int idLote;
    private int idMedicamento;
    private Date fechaCaducidad;
    private int cantidad;

    public Lote(int idLote, int idMedicamento, Date fechaCaducidad, int cantidad) {
        this.idLote = idLote;
        this.idMedicamento = idMedicamento;
        this.fechaCaducidad = fechaCaducidad;
        this.cantidad = cantidad;
    }

    public int getIdLote() { return idLote; }
    public int getIdMedicamento() { return idMedicamento; }
    public Date getFechaCaducidad() { return fechaCaducidad; }
    public int getCantidad() { return cantidad; }
}
