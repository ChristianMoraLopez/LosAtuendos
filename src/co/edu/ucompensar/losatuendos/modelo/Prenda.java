package co.edu.ucompensar.losatuendos.modelo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Hoja del Composite. Las subclases solo se crean desde las fábricas del paquete
 * {@code fabrica}, que validan los datos antes de instanciarlas.
 */
public abstract class Prenda implements ComponenteAlquiler {

    private final String referencia;
    private final String color;
    private final String marca;
    private final String talla;
    private final BigDecimal valorAlquiler;

    protected Prenda(String referencia, String color, String marca, String talla, BigDecimal valorAlquiler) {
        this.referencia = referencia;
        this.color = color;
        this.marca = marca;
        this.talla = talla;
        this.valorAlquiler = valorAlquiler;
    }

    public abstract TipoPrenda getTipo();

    /** Atributos propios del tipo de prenda, en una línea legible. */
    public abstract String getDetalle();

    public String getReferencia() {
        return referencia;
    }

    public String getColor() {
        return color;
    }

    public String getMarca() {
        return marca;
    }

    public String getTalla() {
        return talla;
    }

    @Override
    public String getNombre() {
        return getTipo() + " " + referencia;
    }

    @Override
    public BigDecimal getValorAlquiler() {
        return valorAlquiler;
    }

    @Override
    public List<Prenda> getPrendas() {
        return List.of(this);
    }

    @Override
    public String toString() {
        return referencia + " - " + getTipo() + ", " + color + ", " + marca + ", talla " + talla
                + ", $" + valorAlquiler.toPlainString() + " (" + getDetalle() + ")";
    }
}
