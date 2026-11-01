package co.edu.ucompensar.losatuendos.lavanderia;

import java.util.List;

/**
 * Resultado de enviar una tanda: la guía que entregó la lavandería y las prendas que salieron.
 */
public class EnvioLavanderia {

    private final String guia;
    private final List<ItemLavanderia> prendas;

    public EnvioLavanderia(String guia, List<ItemLavanderia> prendas) {
        this.guia = guia;
        this.prendas = List.copyOf(prendas);
    }

    public String getGuia() {
        return guia;
    }

    public List<ItemLavanderia> getPrendas() {
        return prendas;
    }
}
