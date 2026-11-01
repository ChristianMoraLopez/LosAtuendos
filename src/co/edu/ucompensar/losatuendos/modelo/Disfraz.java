package co.edu.ucompensar.losatuendos.modelo;

import java.math.BigDecimal;

public class Disfraz extends Prenda {

    private final String nombreDisfraz;

    public Disfraz(String referencia, String color, String marca, String talla, BigDecimal valorAlquiler,
            String nombreDisfraz) {
        super(referencia, color, marca, talla, valorAlquiler);
        this.nombreDisfraz = nombreDisfraz;
    }

    @Override
    public TipoPrenda getTipo() {
        return TipoPrenda.DISFRAZ;
    }

    @Override
    public String getDetalle() {
        return nombreDisfraz;
    }

    public String getNombreDisfraz() {
        return nombreDisfraz;
    }
}
