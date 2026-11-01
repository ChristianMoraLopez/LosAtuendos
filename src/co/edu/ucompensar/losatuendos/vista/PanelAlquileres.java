package co.edu.ucompensar.losatuendos.vista;

import co.edu.ucompensar.losatuendos.fachada.LosAtuendosFacade;
import co.edu.ucompensar.losatuendos.modelo.Cliente;
import co.edu.ucompensar.losatuendos.modelo.ServicioAlquiler;
import co.edu.ucompensar.losatuendos.reporte.ReporteCliente;
import co.edu.ucompensar.losatuendos.reporte.ReportePrendasPorTalla;
import co.edu.ucompensar.losatuendos.reporte.ReporteServicio;
import co.edu.ucompensar.losatuendos.reporte.ReporteServiciosPorFecha;
import co.edu.ucompensar.losatuendos.util.Constantes;
import co.edu.ucompensar.losatuendos.util.Validador;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Registro de servicios de alquiler y las consultas de servicios y prendas.
 */
class PanelAlquileres extends JPanel {

    private static final long serialVersionUID = 1L;

    private final transient VentanaPrincipal ventana;

    PanelAlquileres(VentanaPrincipal ventana) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.ventana = ventana;
        add(formularioRegistro());
        add(formularioConsultas());
    }

    private Formulario formularioRegistro() {
        Formulario f = new Formulario("Registrar servicio de alquiler");
        JTextField cliente = f.texto("Identificación cliente");
        JTextField empleado = f.texto("Identificación empleado");
        JTextField referencias = f.texto("Referencias (separadas por coma)");
        JTextField fecha = f.texto("Fecha de alquiler (" + Constantes.PATRON_FECHA + ")",
                LocalDate.now().format(Constantes.FORMATO_FECHA));
        f.boton("Registrar servicio", e -> ventana.ejecutar(() -> {
            List<String> refs = Arrays.asList(referencias.getText().split(","));
            ServicioAlquiler servicio = ventana.fachada().registrarServicio(cliente.getText().trim(),
                    empleado.getText().trim(), refs, Validador.fecha(fecha.getText(), "Fecha de alquiler"));
            ventana.mostrar(new ReporteServicio(ventana.formatoPantalla(), servicio).generar());
            ventana.informar("Servicio de alquiler N.º " + servicio.getNumero() + " registrado.");
            referencias.setText("");
        }));
        return f;
    }

    private Formulario formularioConsultas() {
        Formulario f = new Formulario("Consultas");
        LosAtuendosFacade fachada = ventana.fachada();

        JTextField numero = f.texto("Número de servicio");
        f.boton("Consultar servicio", e -> ventana.ejecutar(() -> {
            ServicioAlquiler servicio = fachada.consultarServicio(Validador.enteroPositivo(numero.getText(), "Número"));
            ventana.mostrar(new ReporteServicio(ventana.formatoPantalla(), servicio).generar());
        }));

        JTextField idCliente = f.texto("Identificación cliente");
        f.boton("Consultar servicios del cliente", e -> ventana.ejecutar(() -> {
            Cliente cliente = fachada.consultarCliente(idCliente.getText().trim());
            List<ServicioAlquiler> vigentes = fachada.consultarServiciosVigentes(cliente.getIdentificacion());
            ventana.mostrar(new ReporteCliente(ventana.formatoPantalla(), cliente, vigentes).generar());
        }));

        JTextField fecha = f.texto("Fecha de alquiler (" + Constantes.PATRON_FECHA + ")",
                LocalDate.now().format(Constantes.FORMATO_FECHA));
        f.boton("Consultar servicios por fecha", e -> ventana.ejecutar(() -> {
            LocalDate dia = Validador.fecha(fecha.getText(), "Fecha de alquiler");
            ventana.mostrar(new ReporteServiciosPorFecha(ventana.formatoPantalla(), dia,
                    fachada.consultarServiciosPorFecha(dia)).generar());
        }));

        JTextField talla = f.texto("Talla");
        f.boton("Consultar prendas por talla", e -> ventana.ejecutar(() -> ventana.mostrar(
                new ReportePrendasPorTalla(ventana.formatoPantalla(), fachada.consultarPrendasPorTalla(talla.getText()))
                        .generar())));
        return f;
    }
}
