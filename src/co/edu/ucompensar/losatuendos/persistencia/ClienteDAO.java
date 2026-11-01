package co.edu.ucompensar.losatuendos.persistencia;

import co.edu.ucompensar.losatuendos.excepciones.NegocioException;
import co.edu.ucompensar.losatuendos.modelo.Cliente;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ClienteDAO extends DaoBase {

    public void insertar(Cliente cliente) throws NegocioException {
        String sql = "INSERT INTO cliente (identificacion, nombre, direccion, telefono, correo) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            ps.setString(1, cliente.getIdentificacion());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getDireccion());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getCorreo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw error("registrar el cliente " + cliente.getIdentificacion(), e);
        }
    }

    public Optional<Cliente> buscar(String identificacion) throws NegocioException {
        String sql = "SELECT * FROM cliente WHERE identificacion = ?";
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            ps.setString(1, identificacion);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new Cliente(rs.getString("identificacion"), rs.getString("nombre"),
                        rs.getString("direccion"), rs.getString("telefono"), rs.getString("correo")));
            }
        } catch (SQLException e) {
            throw error("consultar el cliente", e);
        }
    }
}
