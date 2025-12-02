
package modelo;

public class Usuario {
    private int idUsuario;
    private String username;
    private int idRol;

    public Usuario(int idUsuario, String username, int idRol) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.idRol = idRol;
    }

    public int getIdUsuario() { return idUsuario; }
    public String getUsername() { return username; }
    public int getIdRol() { return idRol; }
}
