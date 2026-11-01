package co.edu.ucompensar.losatuendos.modelo;

public enum TipoTraje {
    CONVENCIONAL("Convencional"),
    FRAC("Frac"),
    SACOLEVA("Sacoleva"),
    OTRO("Otro");

    private final String descripcion;

    TipoTraje(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
