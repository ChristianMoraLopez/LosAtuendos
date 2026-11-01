package co.edu.ucompensar.losatuendos.modelo;

public class Cliente extends Persona {

    private final String correo;

    public Cliente(String identificacion, String nombre, String direccion, String telefono, String correo) {
        super(identificacion, nombre, direccion, telefono);
        this.correo = correo;
    }

    public String getCorreo() {
        return correo;
    }
}
