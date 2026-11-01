package co.edu.ucompensar.losatuendos.persistencia;

import co.edu.ucompensar.losatuendos.excepciones.NegocioException;
import co.edu.ucompensar.losatuendos.util.Constantes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Singleton que administra la única conexión JDBC de la aplicación con la base de datos H2 y crea
 * el esquema la primera vez que se abre.
 */
public final class ConexionBD {

    private static final String[] ESQUEMA = {
        "CREATE TABLE IF NOT EXISTS cliente ("
            + " identificacion VARCHAR(20) PRIMARY KEY,"
            + " nombre VARCHAR(100) NOT NULL,"
            + " direccion VARCHAR(100) NOT NULL,"
            + " telefono VARCHAR(20) NOT NULL,"
            + " correo VARCHAR(100) NOT NULL)",
        "CREATE TABLE IF NOT EXISTS empleado ("
            + " identificacion VARCHAR(20) PRIMARY KEY,"
            + " nombre VARCHAR(100) NOT NULL,"
            + " direccion VARCHAR(100) NOT NULL,"
            + " telefono VARCHAR(20) NOT NULL,"
            + " cargo VARCHAR(100) NOT NULL)",
        "CREATE TABLE IF NOT EXISTS prenda ("
            + " referencia VARCHAR(20) PRIMARY KEY,"
            + " tipo VARCHAR(20) NOT NULL,"
            + " color VARCHAR(100) NOT NULL,"
            + " marca VARCHAR(100) NOT NULL,"
            + " talla VARCHAR(20) NOT NULL,"
            + " valor_alquiler DECIMAL(12,2) NOT NULL,"
            + " pedreria BOOLEAN,"
            + " largo BOOLEAN,"
            + " cantidad_piezas INT,"
            + " tipo_traje VARCHAR(20),"
            + " accesorio VARCHAR(20),"
            + " nombre_disfraz VARCHAR(100))",
        "CREATE TABLE IF NOT EXISTS servicio_alquiler ("
            + " numero INT PRIMARY KEY,"
            + " fecha_solicitud DATE NOT NULL,"
            + " fecha_alquiler DATE NOT NULL,"
            + " id_cliente VARCHAR(20) NOT NULL REFERENCES cliente(identificacion),"
            + " id_empleado VARCHAR(20) NOT NULL REFERENCES empleado(identificacion))",
        "CREATE TABLE IF NOT EXISTS servicio_prenda ("
            + " numero_servicio INT NOT NULL REFERENCES servicio_alquiler(numero),"
            + " referencia VARCHAR(20) NOT NULL REFERENCES prenda(referencia),"
            + " PRIMARY KEY (numero_servicio, referencia))",
        "CREATE TABLE IF NOT EXISTS lavanderia ("
            + " orden BIGINT AUTO_INCREMENT PRIMARY KEY,"
            + " referencia VARCHAR(20) NOT NULL REFERENCES prenda(referencia),"
            + " manchada BOOLEAN NOT NULL,"
            + " delicada BOOLEAN NOT NULL,"
            + " prioridad_administrador BOOLEAN NOT NULL)"
    };

    private static ConexionBD instancia;

    private Connection conexion;

    private ConexionBD() {
    }

    public static synchronized ConexionBD getInstancia() {
        if (instancia == null) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    public synchronized Connection getConexion() throws NegocioException {
        try {
            if (conexion == null || conexion.isClosed()) {
                String url = System.getProperty(Constantes.PROPIEDAD_URL_BD, Constantes.URL_BD_POR_DEFECTO);
                conexion = DriverManager.getConnection(url, Constantes.USUARIO_BD, Constantes.CLAVE_BD);
                crearEsquema();
            }
            return conexion;
        } catch (SQLException e) {
            throw new NegocioException("No fue posible conectarse a la base de datos: " + e.getMessage(), e);
        }
    }

    public synchronized void cerrar() {
        if (conexion == null) {
            return;
        }
        try {
            conexion.close();
        } catch (SQLException e) {
            System.err.println("No se pudo cerrar la conexión: " + e.getMessage());
        }
        conexion = null;
    }

    private void crearEsquema() throws SQLException {
        try (Statement sentencia = conexion.createStatement()) {
            for (String sql : ESQUEMA) {
                sentencia.execute(sql);
            }
        }
    }
}
