package co.edu.ucompensar.losatuendos.reporte;

import co.edu.ucompensar.losatuendos.lavanderia.ItemLavanderia;
import java.util.List;

public class ReporteLavanderia extends Reporte {

    private final String titulo;
    private final String guia;
    private final List<ItemLavanderia> items;

    /**
     * @param guia código de la guía de envío, o {@code null} si solo se está consultando la lista
     */
    public ReporteLavanderia(FormatoSalida formato, String titulo, String guia, List<ItemLavanderia> items) {
        super(formato);
        this.titulo = titulo;
        this.guia = guia;
        this.items = items;
    }

    @Override
    protected String cuerpo() {
        StringBuilder texto = new StringBuilder(formato.titulo(titulo));
        if (guia != null) {
            texto.append(formato.campo("Guía de envío", guia));
        }
        texto.append(formato.campo("Prendas", String.valueOf(items.size())));
        if (items.isEmpty()) {
            texto.append(formato.aviso("No hay prendas en la lista."));
        }
        int posicion = 1;
        for (ItemLavanderia item : items) {
            String indicaciones = item.getIndicaciones().isEmpty()
                    ? "lavado normal" : String.join("; ", item.getIndicaciones());
            texto.append(formato.elemento(posicion++ + ". " + (item.esPrioritario() ? "[PRIORITARIA] " : "")
                    + item.getPrenda() + " - " + indicaciones));
        }
        return texto.toString();
    }
}
