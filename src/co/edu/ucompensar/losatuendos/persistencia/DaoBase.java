package co.edu.ucompensar.losatuendos.persistencia;

import co.edu.ucompensar.losatuendos.excepciones.NegocioException;
import java.sql.Connection;
import java.sql.SQLException;

abstract class DaoBase {

    private static final String ESTADO_LLAVE_DUPLICADA = "23505";

    protected Connection conexion() throws NegocioException {
        return ConexionBD.getInstancia().getConexion();
    }

    protected NegocioException error(String accion, SQLException e) {
        if (ESTADO_LLAVE_DUPLICADA.equals(e.getSQLState())) {
            return new NegocioException("No se pudo " + accion + ": el registro ya existe.", e);
        }
        return new NegocioException("No se pudo " + accion + ": " + e.getMessage(), e);
    }
}
