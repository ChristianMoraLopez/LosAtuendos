package co.edu.ucompensar.losatuendos.modelo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Componente del patrón Composite: una prenda suelta o un grupo de prendas se tratan igual al
 * calcular el valor de un alquiler o al listar el inventario.
 */
public interface ComponenteAlquiler {

    String getNombre();

    BigDecimal getValorAlquiler();

    /** Prendas individuales contenidas en el componente; una prenda se devuelve a sí misma. */
    List<Prenda> getPrendas();
}
