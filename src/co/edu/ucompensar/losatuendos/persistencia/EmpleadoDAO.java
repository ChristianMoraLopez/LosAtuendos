package co.edu.ucompensar.losatuendos.persistencia;

import co.edu.ucompensar.losatuendos.excepciones.NegocioException;
import co.edu.ucompensar.losatuendos.modelo.Empleado;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class EmpleadoDAO extends DaoBase {

    public void insertar(Empleado empleado) throws NegocioException {
        String sql = "INSERT INTO empleado (identificacion, nombre, direccion, telefono, cargo) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            ps.setString(1, empleado.getIdentificacion());
            ps.setString(2, empleado.getNombre());
            ps.setString(3, empleado.getDireccion());
            ps.setString(4, empleado.getTelefono());
            ps.setString(5, empleado.getCargo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw error("registrar el empleado " + empleado.getIdentificacion(), e);
        }
    }

    public Optional<Empleado> buscar(String identificacion) throws NegocioException {
        String sql = "SELECT * FROM empleado WHERE identificacion = ?";
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            ps.setString(1, identificacion);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new Empleado(rs.getString("identificacion"), rs.getString("nombre"),
                        rs.getString("direccion"), rs.getString("telefono"), rs.getString("cargo")));
            }
        } catch (SQLException e) {
            throw error("consultar el empleado", e);
        }
    }
}
