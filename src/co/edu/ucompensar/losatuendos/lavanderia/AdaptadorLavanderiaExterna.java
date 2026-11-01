package co.edu.ucompensar.losatuendos.lavanderia;

import co.edu.ucompensar.losatuendos.excepciones.NegocioException;
import java.time.LocalDate;
import java.util.List;

/**
 * Adaptador: traduce la lista de {@link ItemLavanderia} a los arreglos que espera
 * {@link LavanderiaExterna} y convierte el número de tanda en un código de guía.
 */
public class AdaptadorLavanderiaExterna implements ServicioLavanderia {

    private final LavanderiaExterna lavanderia;

    public AdaptadorLavanderiaExterna(LavanderiaExterna lavanderia) {
        this.lavanderia = lavanderia;
    }

    @Override
    public String enviar(List<ItemLavanderia> tanda) throws NegocioException {
        String[] referencias = new String[tanda.size()];
        boolean[] urgentes = new boolean[tanda.size()];
        for (int i = 0; i < tanda.size(); i++) {
            referencias[i] = tanda.get(i).getPrenda().getReferencia();
            urgentes[i] = tanda.get(i).esPrioritario();
        }
        try {
            int numeroTanda = lavanderia.recibirTanda(referencias, urgentes);
            return String.format("LAV-%s-%04d", LocalDate.now().toString().replace("-", ""), numeroTanda);
        } catch (IllegalArgumentException e) {
            throw new NegocioException("La lavandería rechazó la tanda: " + e.getMessage(), e);
        }
    }
}
