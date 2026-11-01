package co.edu.ucompensar.losatuendos.vista;

import co.edu.ucompensar.losatuendos.excepciones.NegocioException;
import co.edu.ucompensar.losatuendos.fachada.LosAtuendosFacade;
import co.edu.ucompensar.losatuendos.reporte.FormatoHtml;
import co.edu.ucompensar.losatuendos.reporte.FormatoSalida;
import co.edu.ucompensar.losatuendos.util.Constantes;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

/**
 * Ventana principal: pestañas de operación a la izquierda y visor de resultados a la derecha.
 */
public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    /** Operación de la interfaz que puede fallar con un error de negocio. */
    @FunctionalInterface
    interface Accion {
        void ejecutar() throws NegocioException;
    }

    private final transient LosAtuendosFacade fachada;
    private final transient FormatoSalida formatoPantalla = new FormatoHtml();
    private final JEditorPane visor = new JEditorPane("text/html", "");

    public VentanaPrincipal(LosAtuendosFacade fachada) {
        super(Constantes.NOMBRE_NEGOCIO + " - Administración de alquiler de vestuario");
        this.fachada = fachada;

        visor.setEditable(false);
        visor.setBackground(Color.WHITE);
        JScrollPane panelVisor = new JScrollPane(visor);
        panelVisor.setBorder(BorderFactory.createTitledBorder("Resultado"));

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Registros", envolver(new PanelRegistros(this)));
        pestanas.addTab("Alquileres", envolver(new PanelAlquileres(this)));
        pestanas.addTab("Lavandería", envolver(new PanelLavanderia(this)));

        JSplitPane division = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pestanas, panelVisor);
        division.setDividerLocation(440);
        getContentPane().add(cabecera(), BorderLayout.NORTH);
        getContentPane().add(division, BorderLayout.CENTER);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                fachada.cerrar();
            }
        });
        setMinimumSize(new Dimension(980, 640));
        setLocationRelativeTo(null);
        mostrar(formatoPantalla.documento(formatoPantalla.titulo(Constantes.NOMBRE_NEGOCIO)
                + formatoPantalla.aviso("Seleccione una operación en las pestañas de la izquierda.")));
    }

    LosAtuendosFacade fachada() {
        return fachada;
    }

    FormatoSalida formatoPantalla() {
        return formatoPantalla;
    }

    void mostrar(String html) {
        visor.setText(html);
        visor.setCaretPosition(0);
    }

    void informar(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, Constantes.NOMBRE_NEGOCIO, JOptionPane.INFORMATION_MESSAGE);
    }

    /** Ejecuta la acción y convierte cualquier error en un mensaje, sin cerrar la aplicación. */
    void ejecutar(Accion accion) {
        try {
            accion.ejecutar();
        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Operación no realizada", JOptionPane.WARNING_MESSAGE);
        } catch (RuntimeException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado: " + e,
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static JPanel cabecera() {
        JLabel titulo = new JLabel(Constantes.NOMBRE_NEGOCIO);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 24f));
        titulo.setForeground(Color.WHITE);
        JLabel subtitulo = new JLabel("Alquiler de vestidos de dama, trajes de caballero y disfraces");
        subtitulo.setForeground(Color.decode(Constantes.COLOR_SECUNDARIO));

        JPanel cabecera = new JPanel(new BorderLayout(0, 2));
        cabecera.setBackground(Color.decode(Constantes.COLOR_PRINCIPAL));
        cabecera.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));
        cabecera.add(titulo, BorderLayout.NORTH);
        cabecera.add(subtitulo, BorderLayout.SOUTH);
        return cabecera;
    }

    private static JScrollPane envolver(JPanel panel) {
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.add(panel, BorderLayout.NORTH);
        JScrollPane desplazable = new JScrollPane(contenedor);
        desplazable.setBorder(null);
        desplazable.getVerticalScrollBar().setUnitIncrement(16);
        return desplazable;
    }
}
