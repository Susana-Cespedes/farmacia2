
package modelo;

public class Medicamento {
    private int idMedicamento;
    private String nombre;
    private String presentacion;
    private String principioActivo;

    public Medicamento() {}

    public Medicamento(int idMedicamento, String nombre, String presentacion, String principioActivo) {
        this.idMedicamento = idMedicamento;
        this.nombre = nombre;
        this.presentacion = presentacion;
        this.principioActivo = principioActivo;
    }

    // Getters y Setters
    public int getIdMedicamento() { return idMedicamento; }
    public void setIdMedicamento(int idMedicamento) { this.idMedicamento = idMedicamento; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPresentacion() { return presentacion; }
    public void setPresentacion(String presentacion) { this.presentacion = presentacion; }

    public String getPrincipioActivo() { return principioActivo; }
    public void setPrincipioActivo(String principioActivo) { this.principioActivo = principioActivo; }
}
