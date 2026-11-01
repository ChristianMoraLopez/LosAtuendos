package co.edu.ucompensar.losatuendos.modelo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Compuesto del patrón Composite. Agrupa prendas u otros grupos: las prendas de un servicio de
 * alquiler, o el inventario de una talla separado por tipo.
 */
public class GrupoPrendas implements ComponenteAlquiler {

    private final String nombre;
    private final List<ComponenteAlquiler> hijos = new ArrayList<>();

    public GrupoPrendas(String nombre) {
        this.nombre = nombre;
    }

    public void agregar(ComponenteAlquiler componente) {
        hijos.add(componente);
    }

    public List<ComponenteAlquiler> getHijos() {
        return Collections.unmodifiableList(hijos);
    }

    public boolean estaVacio() {
        return hijos.isEmpty();
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public BigDecimal getValorAlquiler() {
        return hijos.stream()
                .map(ComponenteAlquiler::getValorAlquiler)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<Prenda> getPrendas() {
        List<Prenda> prendas = new ArrayList<>();
        hijos.forEach(hijo -> prendas.addAll(hijo.getPrendas()));
        return prendas;
    }
}
