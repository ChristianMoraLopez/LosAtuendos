package co.edu.ucompensar.losatuendos.reporte;

/**
 * Implementador del patrón Bridge: sabe cómo se ve cada pieza de un reporte, pero no qué datos
 * contiene.
 */
public interface FormatoSalida {

    String titulo(String texto);

    String seccion(String texto);

    String campo(String etiqueta, String valor);

    String elemento(String texto);

    String aviso(String texto);

    String documento(String cuerpo);
}
