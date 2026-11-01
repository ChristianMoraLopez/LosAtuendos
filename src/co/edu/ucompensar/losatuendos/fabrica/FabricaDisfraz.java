package co.edu.ucompensar.losatuendos.fabrica;

import co.edu.ucompensar.losatuendos.excepciones.NegocioException;
import co.edu.ucompensar.losatuendos.modelo.Disfraz;
import co.edu.ucompensar.losatuendos.modelo.Prenda;
import co.edu.ucompensar.losatuendos.util.Validador;
import java.math.BigDecimal;

class FabricaDisfraz extends FabricaPrenda {

    @Override
    protected Prenda crearPrenda(String referencia, String color, String marca, String talla, BigDecimal valor,
            DatosPrenda datos) throws NegocioException {
        String nombre = Validador.texto(datos.getNombreDisfraz(), "Nombre del disfraz");
        return new Disfraz(referencia, color, marca, talla, valor, nombre);
    }
}
