package co.edu.ucompensar.losatuendos.reporte;

/**
 * Texto plano, para diálogos de notificación y la consola.
 */
public class FormatoTexto implements FormatoSalida {

    private static final String SALTO = System.lineSeparator();

    @Override
    public String titulo(String texto) {
        return texto.toUpperCase() + SALTO + "=".repeat(texto.length()) + SALTO;
    }

    @Override
    public String seccion(String texto) {
        return SALTO + texto + SALTO + "-".repeat(texto.length()) + SALTO;
    }

    @Override
    public String campo(String etiqueta, String valor) {
        return etiqueta + ": " + valor + SALTO;
    }

    @Override
    public String elemento(String texto) {
        return "  * " + texto + SALTO;
    }

    @Override
    public String aviso(String texto) {
        return "(" + texto + ")" + SALTO;
    }

    @Override
    public String documento(String cuerpo) {
        return cuerpo;
    }
}
