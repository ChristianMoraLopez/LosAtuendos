package co.edu.ucompensar.losatuendos;

import co.edu.ucompensar.losatuendos.excepciones.NegocioException;
import co.edu.ucompensar.losatuendos.fachada.LosAtuendosFacade;
import co.edu.ucompensar.losatuendos.util.Constantes;
import co.edu.ucompensar.losatuendos.vista.VentanaPrincipal;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Punto de entrada de la aplicación de administración de alquiler de vestuario.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((hilo, error) -> {
            error.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ocurrió un error inesperado: " + error,
                    Constantes.NOMBRE_NEGOCIO, JOptionPane.ERROR_MESSAGE);
        });
        SwingUtilities.invokeLater(Main::iniciar);
    }

    private static void iniciar() {
        aplicarEstilo();
        LosAtuendosFacade fachada = new LosAtuendosFacade();
        try {
            DatosDemostracion.cargarSiVacio(fachada);
        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), Constantes.NOMBRE_NEGOCIO, JOptionPane.ERROR_MESSAGE);
            fachada.cerrar();
            return;
        }
        new VentanaPrincipal(fachada).setVisible(true);
    }

    private static void aplicarEstilo() {
        FlatLaf.setGlobalExtraDefaults(Map.of("@accentColor", Constantes.COLOR_PRINCIPAL));
        UIManager.put("Component.arc", 10);
        UIManager.put("Button.arc", 12);
        UIManager.put("TextComponent.arc", 10);
        UIManager.put("TabbedPane.tabHeight", 34);
        UIManager.put("TabbedPane.showTabSeparators", true);
        UIManager.put("ScrollBar.thumbArc", 999);
        if (!FlatLightLaf.setup()) {
            System.err.println("No se pudo aplicar FlatLaf; se usará la apariencia por defecto.");
        }
        UIManager.put("TitledBorder.titleColor", Color.decode(Constantes.COLOR_PRINCIPAL));
    }
}
