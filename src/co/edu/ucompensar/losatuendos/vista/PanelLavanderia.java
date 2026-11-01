package co.edu.ucompensar.losatuendos.vista;

import co.edu.ucompensar.losatuendos.excepciones.NegocioException;
import co.edu.ucompensar.losatuendos.lavanderia.EnvioLavanderia;
import co.edu.ucompensar.losatuendos.reporte.FormatoTexto;
import co.edu.ucompensar.losatuendos.reporte.ReporteLavanderia;
import co.edu.ucompensar.losatuendos.util.Validador;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Registro de prendas para lavado, consulta de la lista y envío de tandas.
 */
class PanelLavanderia extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final String TITULO_LISTA = "Prendas en lista para lavandería";

    private final transient VentanaPrincipal ventana;

    PanelLavanderia(VentanaPrincipal ventana) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.ventana = ventana;
        add(formularioRegistro());
        add(formularioEnvio());
    }

    private Formulario formularioRegistro() {
        Formulario f = new Formulario("Registrar prenda para lavandería");
        JTextField referencia = f.texto("Referencia");
        JCheckBox manchada = f.agregar("Condición", new JCheckBox("Llegó manchada"));
        JCheckBox delicada = f.agregar("", new JCheckBox("Es delicada"));
        JCheckBox prioridad = f.agregar("", new JCheckBox("Prioridad del administrador"));
        f.boton("Agregar a la lista", e -> ventana.ejecutar(() -> {
            ventana.fachada().registrarEnLavanderia(referencia.getText(), manchada.isSelected(),
                    delicada.isSelected(), prioridad.isSelected());
            mostrarLista();
            referencia.setText("");
            manchada.setSelected(false);
            delicada.setSelected(false);
            prioridad.setSelected(false);
        }));
        f.boton("Ver lista de lavandería", e -> ventana.ejecutar(this::mostrarLista));
        return f;
    }

    private Formulario formularioEnvio() {
        Formulario f = new Formulario("Enviar tanda a lavandería");
        JTextField cantidad = f.texto("Cantidad de prendas", "1");
        f.boton("Enviar", e -> ventana.ejecutar(() -> {
            EnvioLavanderia envio = ventana.fachada()
                    .enviarALavanderia(Validador.enteroPositivo(cantidad.getText(), "Cantidad de prendas"));
            notificar(new ReporteLavanderia(new FormatoTexto(), "Notificación de envío a lavandería",
                    envio.getGuia(), envio.getPrendas()).generar());
            mostrarLista();
        }));
        return f;
    }

    private void mostrarLista() throws NegocioException {
        ventana.mostrar(new ReporteLavanderia(ventana.formatoPantalla(), TITULO_LISTA, null,
                ventana.fachada().consultarListaLavanderia()).generar());
    }

    private void notificar(String texto) {
        JTextArea area = new JTextArea(texto, 14, 70);
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JOptionPane.showMessageDialog(ventana, new JScrollPane(area), "Envío a lavandería",
                JOptionPane.INFORMATION_MESSAGE, ventana.logoDialogo());
    }
}
