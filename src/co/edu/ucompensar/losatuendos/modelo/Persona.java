package co.edu.ucompensar.losatuendos.modelo;

/**
 * Datos comunes de clientes y empleados.
 */
public abstract class Persona {

    private final String identificacion;
    private final String nombre;
    private final String direccion;
    private final String telefono;

    protected Persona(String identificacion, String nombre, String direccion, String telefono) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    @Override
    public String toString() {
        return nombre + " (" + identificacion + ")";
    }
}
