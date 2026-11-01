package co.edu.ucompensar.losatuendos.fabrica;

import co.edu.ucompensar.losatuendos.excepciones.NegocioException;
import co.edu.ucompensar.losatuendos.modelo.Prenda;
import co.edu.ucompensar.losatuendos.modelo.TipoPrenda;
import co.edu.ucompensar.losatuendos.util.Validador;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

/**
 * Creador del patrón Factory Method. Valida los datos comunes y deja a cada subclase la decisión
 * de qué prenda concreta instanciar.
 */
public abstract class FabricaPrenda {

    private static final Map<TipoPrenda, FabricaPrenda> FABRICAS = new EnumMap<>(TipoPrenda.class);

    static {
        FABRICAS.put(TipoPrenda.VESTIDO_DAMA, new FabricaVestidoDama());
        FABRICAS.put(TipoPrenda.TRAJE_CABALLERO, new FabricaTrajeCaballero());
        FABRICAS.put(TipoPrenda.DISFRAZ, new FabricaDisfraz());
    }

    public static FabricaPrenda para(TipoPrenda tipo) {
        return FABRICAS.get(tipo);
    }

    public final Prenda crear(DatosPrenda datos) throws NegocioException {
        String referencia = Validador.codigo(datos.getReferencia(), "Referencia");
        String color = Validador.texto(datos.getColor(), "Color");
        String marca = Validador.texto(datos.getMarca(), "Marca");
        String talla = Validador.codigo(datos.getTalla(), "Talla").toUpperCase();
        BigDecimal valor = Validador.valorPositivo(datos.getValorAlquiler(), "Valor del alquiler");
        return crearPrenda(referencia, color, marca, talla, valor, datos);
    }

    /** Método fábrica: cada subclase valida sus campos propios y crea su tipo de prenda. */
    protected abstract Prenda crearPrenda(String referencia, String color, String marca, String talla,
            BigDecimal valor, DatosPrenda datos) throws NegocioException;
}
