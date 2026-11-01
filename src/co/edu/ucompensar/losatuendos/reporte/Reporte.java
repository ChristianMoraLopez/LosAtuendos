package co.edu.ucompensar.losatuendos.reporte;

import co.edu.ucompensar.losatuendos.modelo.Cliente;
import co.edu.ucompensar.losatuendos.modelo.Prenda;
import co.edu.ucompensar.losatuendos.modelo.ServicioAlquiler;
import co.edu.ucompensar.losatuendos.util.Constantes;

/**
 * Abstracción del patrón Bridge. Cada subclase decide qué datos muestra y delega en un
 * {@link FormatoSalida} cómo se presentan, de modo que reportes y formatos crecen por separado.
 */
public abstract class Reporte {

    protected final FormatoSalida formato;

    protected Reporte(FormatoSalida formato) {
        this.formato = formato;
    }

    public final String generar() {
        return formato.documento(cuerpo());
    }

    protected abstract String cuerpo();

    protected String datosCliente(Cliente cliente) {
        return formato.campo("Identificación", cliente.getIdentificacion())
                + formato.campo("Nombre", cliente.getNombre())
                + formato.campo("Dirección", cliente.getDireccion())
                + formato.campo("Teléfono", cliente.getTelefono())
                + formato.campo("Correo", cliente.getCorreo());
    }

    protected String datosServicio(ServicioAlquiler servicio) {
        StringBuilder texto = new StringBuilder()
                .append(formato.seccion("Servicio N.º " + servicio.getNumero()))
                .append(formato.campo("Fecha de solicitud", servicio.getFechaSolicitud().format(Constantes.FORMATO_FECHA)))
                .append(formato.campo("Fecha de alquiler", servicio.getFechaAlquiler().format(Constantes.FORMATO_FECHA)))
                .append(formato.campo("Cliente", servicio.getCliente().toString()))
                .append(formato.campo("Empleado", servicio.getEmpleado() + ", " + servicio.getEmpleado().getCargo()))
                .append(formato.campo("Valor total", "$" + servicio.getValorTotal().toPlainString()))
                .append(formato.campo("Prendas", String.valueOf(servicio.getPrendas().size())));
        for (Prenda prenda : servicio.getPrendas()) {
            texto.append(formato.elemento(prenda.toString()));
        }
        return texto.toString();
    }
}
