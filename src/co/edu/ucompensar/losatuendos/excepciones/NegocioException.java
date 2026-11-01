package co.edu.ucompensar.losatuendos.excepciones;

/**
 * Error esperado del negocio (dato inválido, registro inexistente, prenda ocupada, fallo de la base
 * de datos) cuyo mensaje se puede mostrar directamente al usuario.
 */
public class NegocioException extends Exception {

    private static final long serialVersionUID = 1L;

    public NegocioException(String mensaje) {
        super(mensaje);
    }

    public NegocioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
