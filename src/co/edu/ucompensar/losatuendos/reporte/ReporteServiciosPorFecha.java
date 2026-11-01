package co.edu.ucompensar.losatuendos.reporte;

import co.edu.ucompensar.losatuendos.modelo.ServicioAlquiler;
import co.edu.ucompensar.losatuendos.util.Constantes;
import java.time.LocalDate;
import java.util.List;

public class ReporteServiciosPorFecha extends Reporte {

    private final LocalDate fecha;
    private final List<ServicioAlquiler> servicios;

    public ReporteServiciosPorFecha(FormatoSalida formato, LocalDate fecha, List<ServicioAlquiler> servicios) {
        super(formato);
        this.fecha = fecha;
        this.servicios = servicios;
    }

    @Override
    protected String cuerpo() {
        StringBuilder texto = new StringBuilder(
                formato.titulo("Servicios para el " + fecha.format(Constantes.FORMATO_FECHA)));
        if (servicios.isEmpty()) {
            texto.append(formato.aviso("No hay servicios de alquiler para esa fecha."));
        }
        servicios.forEach(servicio -> texto.append(datosServicio(servicio)));
        return texto.toString();
    }
}
