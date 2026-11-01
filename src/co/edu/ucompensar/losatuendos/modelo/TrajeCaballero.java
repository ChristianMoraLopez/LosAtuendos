package co.edu.ucompensar.losatuendos.modelo;

import java.math.BigDecimal;

public class TrajeCaballero extends Prenda {

    private final TipoTraje tipoTraje;
    private final Accesorio accesorio;

    public TrajeCaballero(String referencia, String color, String marca, String talla, BigDecimal valorAlquiler,
            TipoTraje tipoTraje, Accesorio accesorio) {
        super(referencia, color, marca, talla, valorAlquiler);
        this.tipoTraje = tipoTraje;
        this.accesorio = accesorio;
    }

    @Override
    public TipoPrenda getTipo() {
        return TipoPrenda.TRAJE_CABALLERO;
    }

    @Override
    public String getDetalle() {
        return tipoTraje + " con " + accesorio.toString().toLowerCase();
    }

    public TipoTraje getTipoTraje() {
        return tipoTraje;
    }

    public Accesorio getAccesorio() {
        return accesorio;
    }
}
