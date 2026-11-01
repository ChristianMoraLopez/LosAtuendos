package co.edu.ucompensar.losatuendos.lavanderia;

import co.edu.ucompensar.losatuendos.modelo.Prenda;
import java.util.Comparator;
import java.util.List;

/**
 * Componente del patrón Decorator: una prenda en la lista de lavandería a la que se le pueden
 * sumar condiciones (manchada, delicada, prioridad del administrador).
 */
public interface ItemLavanderia {

    /** Las prendas con prioridad van primero; dentro de cada grupo se respeta el orden de llegada. */
    Comparator<ItemLavanderia> ORDEN_DE_ENVIO = Comparator
            .comparing(ItemLavanderia::esPrioritario).reversed()
            .thenComparingLong(ItemLavanderia::getOrdenLlegada);

    long getOrdenLlegada();

    Prenda getPrenda();

    boolean esPrioritario();

    List<String> getIndicaciones();
}
