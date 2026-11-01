package co.edu.ucompensar.losatuendos.persistencia;

import co.edu.ucompensar.losatuendos.excepciones.NegocioException;
import co.edu.ucompensar.losatuendos.fabrica.DatosPrenda;
import co.edu.ucompensar.losatuendos.fabrica.FabricaPrenda;
import co.edu.ucompensar.losatuendos.modelo.Accesorio;
import co.edu.ucompensar.losatuendos.modelo.Disfraz;
import co.edu.ucompensar.losatuendos.modelo.Prenda;
import co.edu.ucompensar.losatuendos.modelo.TipoPrenda;
import co.edu.ucompensar.losatuendos.modelo.TipoTraje;
import co.edu.ucompensar.losatuendos.modelo.TrajeCaballero;
import co.edu.ucompensar.losatuendos.modelo.VestidoDama;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Persistencia de prendas en una sola tabla con columna {@code tipo}; al leer, la fila se
 * convierte en objeto con la fábrica que corresponde a ese tipo.
 */
public class PrendaDAO extends DaoBase {

    public void insertar(Prenda prenda) throws NegocioException {
        String sql = "INSERT INTO prenda (referencia, tipo, color, marca, talla, valor_alquiler, pedreria, largo,"
                + " cantidad_piezas, tipo_traje, accesorio, nombre_disfraz) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            ps.setString(1, prenda.getReferencia());
            ps.setString(2, prenda.getTipo().name());
            ps.setString(3, prenda.getColor());
            ps.setString(4, prenda.getMarca());
            ps.setString(5, prenda.getTalla());
            ps.setBigDecimal(6, prenda.getValorAlquiler());
            ps.setNull(7, Types.BOOLEAN);
            ps.setNull(8, Types.BOOLEAN);
            ps.setNull(9, Types.INTEGER);
            ps.setNull(10, Types.VARCHAR);
            ps.setNull(11, Types.VARCHAR);
            ps.setNull(12, Types.VARCHAR);
            if (prenda instanceof VestidoDama) {
                VestidoDama vestido = (VestidoDama) prenda;
                ps.setBoolean(7, vestido.tienePedreria());
                ps.setBoolean(8, vestido.esLargo());
                ps.setInt(9, vestido.getCantidadPiezas());
            } else if (prenda instanceof TrajeCaballero) {
                TrajeCaballero traje = (TrajeCaballero) prenda;
                ps.setString(10, traje.getTipoTraje().name());
                ps.setString(11, traje.getAccesorio().name());
            } else if (prenda instanceof Disfraz) {
                ps.setString(12, ((Disfraz) prenda).getNombreDisfraz());
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw error("registrar la prenda " + prenda.getReferencia(), e);
        }
    }

    public Optional<Prenda> buscar(String referencia) throws NegocioException {
        List<Prenda> encontradas = consultar("SELECT * FROM prenda WHERE referencia = ?", referencia);
        return encontradas.stream().findFirst();
    }

    public List<Prenda> listarPorTalla(String talla) throws NegocioException {
        return consultar("SELECT * FROM prenda WHERE UPPER(talla) = UPPER(?) ORDER BY tipo, referencia", talla);
    }

    public List<Prenda> listarPorServicio(int numeroServicio) throws NegocioException {
        return consultar("SELECT p.* FROM prenda p JOIN servicio_prenda sp ON sp.referencia = p.referencia"
                + " WHERE sp.numero_servicio = ? ORDER BY p.referencia", numeroServicio);
    }

    public boolean hayPrendas() throws NegocioException {
        try (PreparedStatement ps = conexion().prepareStatement("SELECT 1 FROM prenda LIMIT 1");
                ResultSet rs = ps.executeQuery()) {
            return rs.next();
        } catch (SQLException e) {
            throw error("consultar el inventario", e);
        }
    }

    /** Una prenda está disponible si ningún otro servicio la tiene reservada para la misma fecha. */
    public boolean estaDisponible(String referencia, LocalDate fechaAlquiler) throws NegocioException {
        String sql = "SELECT COUNT(*) FROM servicio_prenda sp JOIN servicio_alquiler s ON s.numero = sp.numero_servicio"
                + " WHERE sp.referencia = ? AND s.fecha_alquiler = ?";
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            ps.setString(1, referencia);
            ps.setDate(2, Date.valueOf(fechaAlquiler));
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            throw error("verificar la disponibilidad de la prenda " + referencia, e);
        }
    }

    private List<Prenda> consultar(String sql, Object parametro) throws NegocioException {
        List<Prenda> prendas = new ArrayList<>();
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            ps.setObject(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    prendas.add(convertir(rs));
                }
            }
        } catch (SQLException e) {
            throw error("consultar las prendas", e);
        }
        return prendas;
    }

    private Prenda convertir(ResultSet rs) throws SQLException, NegocioException {
        TipoPrenda tipo = TipoPrenda.valueOf(rs.getString("tipo"));
        DatosPrenda datos = new DatosPrenda(tipo).comunes(rs.getString("referencia"), rs.getString("color"),
                rs.getString("marca"), rs.getString("talla"), rs.getBigDecimal("valor_alquiler").toPlainString());
        switch (tipo) {
            case VESTIDO_DAMA:
                datos.vestido(rs.getBoolean("pedreria"), rs.getBoolean("largo"), rs.getString("cantidad_piezas"));
                break;
            case TRAJE_CABALLERO:
                datos.traje(TipoTraje.valueOf(rs.getString("tipo_traje")), Accesorio.valueOf(rs.getString("accesorio")));
                break;
            default:
                datos.disfraz(rs.getString("nombre_disfraz"));
        }
        return FabricaPrenda.para(tipo).crear(datos);
    }
}
