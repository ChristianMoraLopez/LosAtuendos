package co.edu.ucompensar.losatuendos.vista;

import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Acceso a las imágenes del paquete {@code recursos}. Si una imagen falta, la interfaz sigue
 * funcionando sin ella.
 */
final class Recursos {

    private static final String CARPETA = "/co/edu/ucompensar/losatuendos/recursos/";
    private static final int[] TAMANOS_VENTANA = {16, 24, 32, 48, 64, 128, 256};

    private Recursos() {
    }

    static URL url(String archivo) {
        return Recursos.class.getResource(CARPETA + archivo);
    }

    static Icon icono(String archivo, int lado) {
        URL url = url(archivo);
        if (url == null) {
            return null;
        }
        Image escalada = new ImageIcon(url).getImage().getScaledInstance(lado, lado, Image.SCALE_SMOOTH);
        return new ImageIcon(escalada);
    }

    static List<Image> iconosVentana() {
        List<Image> iconos = new ArrayList<>();
        for (int lado : TAMANOS_VENTANA) {
            URL url = url("icono_" + lado + ".png");
            if (url != null) {
                iconos.add(new ImageIcon(url).getImage());
            }
        }
        return iconos;
    }
}
