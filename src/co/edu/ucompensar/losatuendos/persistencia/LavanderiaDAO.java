package co.edu.ucompensar.losatuendos.persistencia;

import co.edu.ucompensar.losatuendos.excepciones.NegocioException;
import co.edu.ucompensar.losatuendos.lavanderia.ItemLavanderia;
import co.edu.ucompensar.losatuendos.lavanderia.PrendaDelicada;
import co.edu.ucompensar.losatuendos.lavanderia.PrendaEnLavanderia;
import co.edu.ucompensar.losatuendos.lavanderia.PrendaManchada;
import co.edu.ucompensar.losatuendos.lavanderia.PrioridadAdministrador;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lista de prendas pendientes de lavado. Cada fila guarda las condiciones especiales y, al leerla,
 * se reconstruye el ítem envolviéndolo en los decoradores que correspondan.
 */
public class LavanderiaDAO extends DaoBase {

    private final PrendaDAO prendaDAO;

    public LavanderiaDAO(PrendaDAO prendaDAO) {
        this.prendaDAO = prendaDAO;
    }

    public void insertar(String referencia, boolean manchada, boolean delicada, boolean prioridadAdministrador)
            throws NegocioException {
        String sql = "INSERT INTO lavanderia (referencia, manchada, delicada, prioridad_administrador)"
                + " VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            ps.setString(1, referencia);
            ps.setBoolean(2, manchada);
            ps.setBoolean(3, delicada);
            ps.setBoolean(4, prioridadAdministrador);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw error("registrar la prenda en la lista de lavandería", e);
        }
    }

    public boolean estaPendiente(String referencia) throws NegocioException {
        try (PreparedStatement ps = conexion().prepareStatement("SELECT 1 FROM lavanderia WHERE referencia = ?")) {
            ps.setString(1, referencia);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw error("consultar la lista de lavandería", e);
        }
    }

    public List<ItemLavanderia> listar() throws NegocioException {
        List<ItemLavanderia> items = new ArrayList<>();
        try (PreparedStatement ps = conexion().prepareStatement("SELECT * FROM lavanderia ORDER BY orden");
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ItemLavanderia item = new PrendaEnLavanderia(rs.getLong("orden"),
                        prendaDAO.buscar(rs.getString("referencia")).orElseThrow());
                if (rs.getBoolean("manchada")) {
                    item = new PrendaManchada(item);
                }
                if (rs.getBoolean("delicada")) {
                    item = new PrendaDelicada(item);
                }
                if (rs.getBoolean("prioridad_administrador")) {
                    item = new PrioridadAdministrador(item);
                }
                items.add(item);
            }
        } catch (SQLException e) {
            throw error("consultar la lista de lavandería", e);
        }
        return items;
    }

    public void eliminar(List<ItemLavanderia> enviados) throws NegocioException {
        try (PreparedStatement ps = conexion().prepareStatement("DELETE FROM lavanderia WHERE orden = ?")) {
            for (ItemLavanderia item : enviados) {
                ps.setLong(1, item.getOrdenLlegada());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw error("retirar las prendas enviadas de la lista de lavandería", e);
        }
    }
}
