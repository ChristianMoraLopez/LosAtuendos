package co.edu.ucompensar.losatuendos.lavanderia;

import co.edu.ucompensar.losatuendos.modelo.Prenda;
import java.util.ArrayList;
import java.util.List;

/**
 * Decorador base. Toda condición especial da prelación en el envío y agrega una indicación para
 * la lavandería; los decoradores se pueden apilar sobre la misma prenda.
 */
public abstract class DecoradorLavanderia implements ItemLavanderia {

    private final ItemLavanderia decorado;

    protected DecoradorLavanderia(ItemLavanderia decorado) {
        this.decorado = decorado;
    }

    protected abstract String getIndicacion();

    @Override
    public long getOrdenLlegada() {
        return decorado.getOrdenLlegada();
    }

    @Override
    public Prenda getPrenda() {
        return decorado.getPrenda();
    }

    @Override
    public boolean esPrioritario() {
        return true;
    }

    @Override
    public List<String> getIndicaciones() {
        List<String> indicaciones = new ArrayList<>(decorado.getIndicaciones());
        indicaciones.add(getIndicacion());
        return indicaciones;
    }
}
