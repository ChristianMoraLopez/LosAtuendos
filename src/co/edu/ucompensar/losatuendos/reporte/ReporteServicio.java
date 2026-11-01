package co.edu.ucompensar.losatuendos.reporte;

import co.edu.ucompensar.losatuendos.modelo.ServicioAlquiler;

public class ReporteServicio extends Reporte {

    private final ServicioAlquiler servicio;

    public ReporteServicio(FormatoSalida formato, ServicioAlquiler servicio) {
        super(formato);
        this.servicio = servicio;
    }

    @Override
    protected String cuerpo() {
        return formato.titulo("Consulta de servicio de alquiler") + datosServicio(servicio);
    }
}
