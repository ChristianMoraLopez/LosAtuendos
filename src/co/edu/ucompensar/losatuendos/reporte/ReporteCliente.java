package co.edu.ucompensar.losatuendos.reporte;

import co.edu.ucompensar.losatuendos.modelo.Cliente;
import co.edu.ucompensar.losatuendos.modelo.ServicioAlquiler;
import java.util.List;

public class ReporteCliente extends Reporte {

    private final Cliente cliente;
    private final List<ServicioAlquiler> serviciosVigentes;

    public ReporteCliente(FormatoSalida formato, Cliente cliente, List<ServicioAlquiler> serviciosVigentes) {
        super(formato);
        this.cliente = cliente;
        this.serviciosVigentes = serviciosVigentes;
    }

    @Override
    protected String cuerpo() {
        StringBuilder texto = new StringBuilder(formato.titulo("Servicios del cliente " + cliente.getNombre()))
                .append(datosCliente(cliente))
                .append(formato.seccion("Servicios vigentes (" + serviciosVigentes.size() + ")"));
        if (serviciosVigentes.isEmpty()) {
            texto.append(formato.aviso("El cliente no tiene servicios con fecha igual o posterior a hoy."));
        }
        serviciosVigentes.forEach(servicio -> texto.append(datosServicio(servicio)));
        return texto.toString();
    }
}
