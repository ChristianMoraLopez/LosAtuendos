package co.edu.ucompensar.losatuendos.reporte;

import co.edu.ucompensar.losatuendos.modelo.ComponenteAlquiler;
import co.edu.ucompensar.losatuendos.modelo.GrupoPrendas;
import co.edu.ucompensar.losatuendos.modelo.Prenda;

/**
 * Recorre el árbol talla → tipo → prenda que arma la fachada con el patrón Composite.
 */
public class ReportePrendasPorTalla extends Reporte {

    private final GrupoPrendas inventario;

    public ReportePrendasPorTalla(FormatoSalida formato, GrupoPrendas inventario) {
        super(formato);
        this.inventario = inventario;
    }

    @Override
    protected String cuerpo() {
        StringBuilder texto = new StringBuilder(formato.titulo(inventario.getNombre()));
        if (inventario.estaVacio()) {
            return texto.append(formato.aviso("No hay prendas registradas con esa talla.")).toString();
        }
        for (ComponenteAlquiler componente : inventario.getHijos()) {
            texto.append(formato.seccion(componente.getNombre() + " (" + componente.getPrendas().size() + ")"));
            for (Prenda prenda : componente.getPrendas()) {
                texto.append(formato.elemento(prenda.toString()));
            }
        }
        return texto.toString();
    }
}
