package co.edu.ucompensar.losatuendos.modelo;

public class Empleado extends Persona {

    private final String cargo;

    public Empleado(String identificacion, String nombre, String direccion, String telefono, String cargo) {
        super(identificacion, nombre, direccion, telefono);
        this.cargo = cargo;
    }

    public String getCargo() {
        return cargo;
    }
}
