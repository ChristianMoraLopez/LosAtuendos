package co.edu.ucompensar.losatuendos.modelo;

import java.math.BigDecimal;

public class VestidoDama extends Prenda {

    private final boolean pedreria;
    private final boolean largo;
    private final int cantidadPiezas;

    public VestidoDama(String referencia, String color, String marca, String talla, BigDecimal valorAlquiler,
            boolean pedreria, boolean largo, int cantidadPiezas) {
        super(referencia, color, marca, talla, valorAlquiler);
        this.pedreria = pedreria;
        this.largo = largo;
        this.cantidadPiezas = cantidadPiezas;
    }

    @Override
    public TipoPrenda getTipo() {
        return TipoPrenda.VESTIDO_DAMA;
    }

    @Override
    public String getDetalle() {
        return (largo ? "largo" : "corto") + ", " + (pedreria ? "con" : "sin") + " pedrería, "
                + cantidadPiezas + (cantidadPiezas == 1 ? " pieza" : " piezas");
    }

    public boolean tienePedreria() {
        return pedreria;
    }

    public boolean esLargo() {
        return largo;
    }

    public int getCantidadPiezas() {
        return cantidadPiezas;
    }
}
