package co.edu.ucompensar.losatuendos.persistencia;

import co.edu.ucompensar.losatuendos.excepciones.NegocioException;
import co.edu.ucompensar.losatuendos.modelo.Prenda;
import co.edu.ucompensar.losatuendos.modelo.ServicioAlquiler;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServicioAlquilerDAO extends DaoBase {

    private final ClienteDAO clienteDAO;
    private final EmpleadoDAO empleadoDAO;
    private final PrendaDAO prendaDAO;

    public ServicioAlquilerDAO(ClienteDAO clienteDAO, EmpleadoDAO empleadoDAO, PrendaDAO prendaDAO) {
        this.clienteDAO = clienteDAO;
        this.empleadoDAO = empleadoDAO;
        this.prendaDAO = prendaDAO;
    }

    public int siguienteNumero() throws NegocioException {
        try (Statement st = conexion().createStatement();
                ResultSet rs = st.executeQuery("SELECT COALESCE(MAX(numero), 0) + 1 FROM servicio_alquiler")) {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw error("generar el número del servicio", e);
        }
    }

    /** Guarda el servicio y sus prendas en una sola transacción. */
    public void insertar(ServicioAlquiler servicio) throws NegocioException {
        Connection con = conexion();
        try {
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO servicio_alquiler"
                    + " (numero, fecha_solicitud, fecha_alquiler, id_cliente, id_empleado) VALUES (?, ?, ?, ?, ?)")) {
                ps.setInt(1, servicio.getNumero());
                ps.setDate(2, Date.valueOf(servicio.getFechaSolicitud()));
                ps.setDate(3, Date.valueOf(servicio.getFechaAlquiler()));
                ps.setString(4, servicio.getCliente().getIdentificacion());
                ps.setString(5, servicio.getEmpleado().getIdentificacion());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO servicio_prenda (numero_servicio, referencia) VALUES (?, ?)")) {
                for (Prenda prenda : servicio.getPrendas()) {
                    ps.setInt(1, servicio.getNumero());
                    ps.setString(2, prenda.getReferencia());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            con.commit();
        } catch (SQLException e) {
            deshacer(con);
            throw error("registrar el servicio de alquiler", e);
        } finally {
            restaurarAutoCommit(con);
        }
    }

    public Optional<ServicioAlquiler> buscar(int numero) throws NegocioException {
        return consultar("SELECT * FROM servicio_alquiler WHERE numero = ?", numero).stream().findFirst();
    }

    public List<ServicioAlquiler> listarVigentesPorCliente(String identificacion, LocalDate desde)
            throws NegocioException {
        return consultar("SELECT * FROM servicio_alquiler WHERE id_cliente = ? AND fecha_alquiler >= ?"
                + " ORDER BY fecha_alquiler, numero", identificacion, Date.valueOf(desde));
    }

    public List<ServicioAlquiler> listarPorFecha(LocalDate fechaAlquiler) throws NegocioException {
        return consultar("SELECT * FROM servicio_alquiler WHERE fecha_alquiler = ? ORDER BY numero",
                Date.valueOf(fechaAlquiler));
    }

    private List<ServicioAlquiler> consultar(String sql, Object... parametros) throws NegocioException {
        List<ServicioAlquiler> servicios = new ArrayList<>();
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            for (int i = 0; i < parametros.length; i++) {
                ps.setObject(i + 1, parametros[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    servicios.add(convertir(rs));
                }
            }
        } catch (SQLException e) {
            throw error("consultar los servicios de alquiler", e);
        }
        return servicios;
    }

    private ServicioAlquiler convertir(ResultSet rs) throws SQLException, NegocioException {
        int numero = rs.getInt("numero");
        ServicioAlquiler.Builder builder = ServicioAlquiler.builder()
                .numero(numero)
                .fechaSolicitud(rs.getDate("fecha_solicitud").toLocalDate())
                .fechaAlquiler(rs.getDate("fecha_alquiler").toLocalDate())
                .cliente(clienteDAO.buscar(rs.getString("id_cliente")).orElseThrow())
                .empleado(empleadoDAO.buscar(rs.getString("id_empleado")).orElseThrow());
        for (Prenda prenda : prendaDAO.listarPorServicio(numero)) {
            builder.prenda(prenda);
        }
        return builder.build();
    }

    private static void deshacer(Connection con) {
        try {
            con.rollback();
        } catch (SQLException e) {
            System.err.println("No se pudo deshacer la transacción: " + e.getMessage());
        }
    }

    private static void restaurarAutoCommit(Connection con) {
        try {
            con.setAutoCommit(true);
        } catch (SQLException e) {
            System.err.println("No se pudo restaurar el modo autocommit: " + e.getMessage());
        }
    }
}
