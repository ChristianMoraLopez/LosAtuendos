package co.edu.ucompensar.losatuendos.modelo;

public enum Accesorio {
    CORBATA("Corbata"),
    CORBATIN("Corbatín"),
    PLASTRON("Plastrón");

    private final String descripcion;

    Accesorio(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
