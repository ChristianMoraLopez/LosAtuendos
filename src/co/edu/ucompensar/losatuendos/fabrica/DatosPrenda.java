package co.edu.ucompensar.losatuendos.fabrica;

import co.edu.ucompensar.losatuendos.modelo.Accesorio;
import co.edu.ucompensar.losatuendos.modelo.TipoPrenda;
import co.edu.ucompensar.losatuendos.modelo.TipoTraje;

/**
 * Datos sin validar de una prenda, tal como llegan del formulario o de la base de datos. Cada
 * fábrica toma los campos que le corresponden según el tipo.
 */
public class DatosPrenda {

    private final TipoPrenda tipo;
    private String referencia;
    private String color;
    private String marca;
    private String talla;
    private String valorAlquiler;
    private boolean pedreria;
    private boolean largo;
    private String cantidadPiezas;
    private TipoTraje tipoTraje;
    private Accesorio accesorio;
    private String nombreDisfraz;

    public DatosPrenda(TipoPrenda tipo) {
        this.tipo = tipo;
    }

    public DatosPrenda comunes(String referencia, String color, String marca, String talla, String valorAlquiler) {
        this.referencia = referencia;
        this.color = color;
        this.marca = marca;
        this.talla = talla;
        this.valorAlquiler = valorAlquiler;
        return this;
    }

    public DatosPrenda vestido(boolean pedreria, boolean largo, String cantidadPiezas) {
        this.pedreria = pedreria;
        this.largo = largo;
        this.cantidadPiezas = cantidadPiezas;
        return this;
    }

    public DatosPrenda traje(TipoTraje tipoTraje, Accesorio accesorio) {
        this.tipoTraje = tipoTraje;
        this.accesorio = accesorio;
        return this;
    }

    public DatosPrenda disfraz(String nombreDisfraz) {
        this.nombreDisfraz = nombreDisfraz;
        return this;
    }

    public TipoPrenda getTipo() {
        return tipo;
    }

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

    public String getValorAlquiler() {
        return valorAlquiler;
    }

    public boolean isPedreria() {
        return pedreria;
    }

    public boolean isLargo() {
        return largo;
    }

    public String getCantidadPiezas() {
        return cantidadPiezas;
    }

    public TipoTraje getTipoTraje() {
        return tipoTraje;
    }

    public Accesorio getAccesorio() {
        return accesorio;
    }

    public String getNombreDisfraz() {
        return nombreDisfraz;
    }
}
