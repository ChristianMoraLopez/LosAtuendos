package co.edu.ucompensar.losatuendos.modelo;

public enum TipoPrenda {
    VESTIDO_DAMA("Vestido de dama"),
    TRAJE_CABALLERO("Traje de caballero"),
    DISFRAZ("Disfraz");

    private final String descripcion;

    TipoPrenda(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
