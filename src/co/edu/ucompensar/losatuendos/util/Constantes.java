package co.edu.ucompensar.losatuendos.util;

import java.time.format.DateTimeFormatter;

/**
 * Valores fijos de la aplicación: conexión a la base de datos, formatos y límites.
 */
public final class Constantes {

    public static final String NOMBRE_NEGOCIO = "Los Atuendos";
    public static final String COLOR_PRINCIPAL = "#1f3a68";
    public static final String COLOR_SECUNDARIO = "#c9a227";

    /** Propiedad del sistema que permite apuntar a otra base de datos, por ejemplo en pruebas. */
    public static final String PROPIEDAD_URL_BD = "losatuendos.bd";
    public static final String URL_BD_POR_DEFECTO = "jdbc:h2:./datos/losatuendos;AUTO_SERVER=TRUE";
    public static final String USUARIO_BD = "sa";
    public static final String CLAVE_BD = "";

    public static final String PATRON_FECHA = "yyyy-MM-dd";
    public static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern(PATRON_FECHA);

    public static final int LONGITUD_MAXIMA_TEXTO = 100;
    public static final int LONGITUD_MAXIMA_CODIGO = 20;

    private Constantes() {
    }
}
