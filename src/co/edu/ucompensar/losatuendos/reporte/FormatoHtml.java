package co.edu.ucompensar.losatuendos.reporte;

import co.edu.ucompensar.losatuendos.util.Constantes;

/**
 * HTML sencillo que entiende el {@code JEditorPane} de Swing.
 */
public class FormatoHtml implements FormatoSalida {

    @Override
    public String titulo(String texto) {
        return "<h2 style='color:" + Constantes.COLOR_OSCURO + "'>" + escapar(texto) + "</h2>";
    }

    @Override
    public String seccion(String texto) {
        return "<h3 style='color:" + Constantes.COLOR_PRINCIPAL + "'>" + escapar(texto) + "</h3>";
    }

    @Override
    public String campo(String etiqueta, String valor) {
        return "<div><b>" + escapar(etiqueta) + ":</b> " + escapar(valor) + "</div>";
    }

    @Override
    public String elemento(String texto) {
        return "<ul style='margin-top:0;margin-bottom:0'><li>" + escapar(texto) + "</li></ul>";
    }

    @Override
    public String aviso(String texto) {
        return "<p><i>" + escapar(texto) + "</i></p>";
    }

    @Override
    public String documento(String cuerpo) {
        return "<html><body style='font-family:sans-serif;font-size:11px'>" + cuerpo + "</body></html>";
    }

    private static String escapar(String texto) {
        return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
