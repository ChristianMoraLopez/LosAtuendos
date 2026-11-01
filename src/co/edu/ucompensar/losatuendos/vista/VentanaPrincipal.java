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
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.Icon;
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
    private static final int ICONO_PESTANA = 20;
    private static final int LOGO_CABECERA = 56;
    private static final int LOGO_DIALOGO = 64;

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

        setIconImages(Recursos.iconosVentana());
        visor.setEditable(false);
        visor.setBackground(Color.WHITE);
        JScrollPane panelVisor = new JScrollPane(visor);
        panelVisor.setBorder(BorderFactory.createTitledBorder("Resultado"));

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Registros", Recursos.icono("icono_registros.png", ICONO_PESTANA),
                envolver(new PanelRegistros(this)));
        pestanas.addTab("Alquileres", Recursos.icono("icono_alquileres.png", ICONO_PESTANA),
                envolver(new PanelAlquileres(this)));
        pestanas.addTab("Lavandería", Recursos.icono("icono_lavanderia.png", ICONO_PESTANA),
                envolver(new PanelLavanderia(this)));

        JSplitPane division = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pestanas, panelVisor);
        division.setDividerLocation(470);
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
        mostrarBienvenida();
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
        JOptionPane.showMessageDialog(this, mensaje, Constantes.NOMBRE_NEGOCIO, JOptionPane.INFORMATION_MESSAGE,
                logoDialogo());
    }

    Icon logoDialogo() {
        return Recursos.icono("logo.png", LOGO_DIALOGO);
    }

    private void mostrarBienvenida() {
        URL logo = Recursos.url("logo.png");
        String imagen = logo == null ? "" : "<img src='" + logo + "' width='220' height='220'><br>";
        mostrar("<html><body style='font-family:sans-serif'><div align='center'><br><br>" + imagen
                + "<h1 style='color:" + Constantes.COLOR_OSCURO + "'>" + Constantes.NOMBRE_NEGOCIO + "</h1>"
                + "<p style='color:#555555'>Seleccione una operación en las pestañas de la izquierda.</p>"
                + "</div></body></html>");
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
        JLabel logo = new JLabel(Recursos.icono("logo_blanco.png", LOGO_CABECERA));
        logo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 14));
        JLabel titulo = new JLabel(Constantes.NOMBRE_NEGOCIO);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 24f));
        titulo.setForeground(Color.WHITE);
        JLabel subtitulo = new JLabel("Alquiler de vestidos de dama, trajes de caballero y disfraces");
        subtitulo.setForeground(Color.decode(Constantes.COLOR_SECUNDARIO));

        JPanel textos = new JPanel(new BorderLayout(0, 2));
        textos.setOpaque(false);
        textos.add(titulo, BorderLayout.NORTH);
        textos.add(subtitulo, BorderLayout.SOUTH);

        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBackground(Color.decode(Constantes.COLOR_OSCURO));
        cabecera.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        cabecera.add(logo, BorderLayout.WEST);
        cabecera.add(textos, BorderLayout.CENTER);
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
