package co.edu.ucompensar.losatuendos.fabrica;

import co.edu.ucompensar.losatuendos.excepciones.NegocioException;
import co.edu.ucompensar.losatuendos.modelo.Prenda;
import co.edu.ucompensar.losatuendos.modelo.TrajeCaballero;
import java.math.BigDecimal;

class FabricaTrajeCaballero extends FabricaPrenda {

    @Override
    protected Prenda crearPrenda(String referencia, String color, String marca, String talla, BigDecimal valor,
            DatosPrenda datos) throws NegocioException {
        if (datos.getTipoTraje() == null || datos.getAccesorio() == null) {
            throw new NegocioException("Seleccione el tipo de traje y el accesorio.");
        }
        return new TrajeCaballero(referencia, color, marca, talla, valor, datos.getTipoTraje(), datos.getAccesorio());
    }
}
