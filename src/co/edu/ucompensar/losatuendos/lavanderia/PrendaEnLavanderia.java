package co.edu.ucompensar.losatuendos.lavanderia;

import co.edu.ucompensar.losatuendos.modelo.Prenda;
import java.util.List;

/**
 * Componente concreto: prenda devuelta que espera lavado normal, sin prioridad.
 */
public class PrendaEnLavanderia implements ItemLavanderia {

    private final long ordenLlegada;
    private final Prenda prenda;

    public PrendaEnLavanderia(long ordenLlegada, Prenda prenda) {
        this.ordenLlegada = ordenLlegada;
        this.prenda = prenda;
    }

    @Override
    public long getOrdenLlegada() {
        return ordenLlegada;
    }

    @Override
    public Prenda getPrenda() {
        return prenda;
    }

    @Override
    public boolean esPrioritario() {
        return false;
    }

    @Override
    public List<String> getIndicaciones() {
        return List.of();
    }
}
