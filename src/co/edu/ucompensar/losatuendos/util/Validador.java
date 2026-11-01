package co.edu.ucompensar.losatuendos.util;

import co.edu.ucompensar.losatuendos.excepciones.NegocioException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Conversión y validación de los datos que llegan desde la interfaz.
 */
public final class Validador {

    private Validador() {
    }

    public static String texto(String valor, String campo) throws NegocioException {
        return texto(valor, campo, Constantes.LONGITUD_MAXIMA_TEXTO);
    }

    public static String codigo(String valor, String campo) throws NegocioException {
        return texto(valor, campo, Constantes.LONGITUD_MAXIMA_CODIGO);
    }

    private static String texto(String valor, String campo, int maximo) throws NegocioException {
        if (valor == null || valor.isBlank()) {
            throw new NegocioException("El campo \"" + campo + "\" es obligatorio.");
        }
        String limpio = valor.trim();
        if (limpio.length() > maximo) {
            throw new NegocioException("El campo \"" + campo + "\" admite máximo " + maximo + " caracteres.");
        }
        return limpio;
    }

    public static String correo(String valor) throws NegocioException {
        String limpio = texto(valor, "Correo electrónico");
        if (!limpio.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) {
            throw new NegocioException("El correo electrónico \"" + limpio + "\" no es válido.");
        }
        return limpio;
    }

    public static BigDecimal valorPositivo(String valor, String campo) throws NegocioException {
        try {
            BigDecimal numero = new BigDecimal(texto(valor, campo).replace(",", ""));
            if (numero.signum() <= 0) {
                throw new NegocioException("El campo \"" + campo + "\" debe ser mayor que cero.");
            }
            return numero;
        } catch (NumberFormatException e) {
            throw new NegocioException("El campo \"" + campo + "\" debe ser numérico.", e);
        }
    }

    public static int enteroPositivo(String valor, String campo) throws NegocioException {
        try {
            int numero = Integer.parseInt(texto(valor, campo));
            if (numero <= 0) {
                throw new NegocioException("El campo \"" + campo + "\" debe ser mayor que cero.");
            }
            return numero;
        } catch (NumberFormatException e) {
            throw new NegocioException("El campo \"" + campo + "\" debe ser un número entero.", e);
        }
    }

    public static LocalDate fecha(String valor, String campo) throws NegocioException {
        try {
            return LocalDate.parse(texto(valor, campo), Constantes.FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            throw new NegocioException("El campo \"" + campo + "\" debe tener el formato "
                    + Constantes.PATRON_FECHA + ".", e);
        }
    }
}
