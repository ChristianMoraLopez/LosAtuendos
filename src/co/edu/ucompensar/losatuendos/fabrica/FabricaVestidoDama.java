package co.edu.ucompensar.losatuendos.fabrica;

import co.edu.ucompensar.losatuendos.excepciones.NegocioException;
import co.edu.ucompensar.losatuendos.modelo.Prenda;
import co.edu.ucompensar.losatuendos.modelo.VestidoDama;
import co.edu.ucompensar.losatuendos.util.Validador;
import java.math.BigDecimal;

class FabricaVestidoDama extends FabricaPrenda {

    @Override
    protected Prenda crearPrenda(String referencia, String color, String marca, String talla, BigDecimal valor,
            DatosPrenda datos) throws NegocioException {
        int piezas = Validador.enteroPositivo(datos.getCantidadPiezas(), "Cantidad de piezas");
        return new VestidoDama(referencia, color, marca, talla, valor, datos.isPedreria(), datos.isLargo(), piezas);
    }
}
