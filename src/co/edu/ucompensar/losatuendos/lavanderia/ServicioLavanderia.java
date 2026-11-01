package co.edu.ucompensar.losatuendos.lavanderia;

import co.edu.ucompensar.losatuendos.excepciones.NegocioException;
import java.util.List;

/**
 * Objetivo del patrón Adapter: la interfaz con la que la aplicación entrega una tanda de prendas
 * a la lavandería.
 */
public interface ServicioLavanderia {

    /** @return código de la guía de envío que entrega la lavandería */
    String enviar(List<ItemLavanderia> tanda) throws NegocioException;
}
