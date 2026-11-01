package co.edu.ucompensar.losatuendos.lavanderia;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Adaptado: simula el sistema del proveedor de lavandería, con una API que no se puede modificar y
 * que trabaja con arreglos de referencias y banderas de urgencia.
 */
public class LavanderiaExterna {

    private static final AtomicInteger CONSECUTIVO_TANDAS = new AtomicInteger();

    /**
     * @return número de tanda asignado por el proveedor
     * @throws IllegalArgumentException si la tanda está vacía o los arreglos no coinciden
     */
    public int recibirTanda(String[] referencias, boolean[] urgentes) {
        if (referencias.length == 0 || referencias.length != urgentes.length) {
            throw new IllegalArgumentException("Tanda inválida");
        }
        return CONSECUTIVO_TANDAS.incrementAndGet();
    }
}
