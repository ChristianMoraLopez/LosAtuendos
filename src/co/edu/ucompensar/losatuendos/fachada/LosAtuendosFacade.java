package co.edu.ucompensar.losatuendos.fachada;

import co.edu.ucompensar.losatuendos.excepciones.NegocioException;
import co.edu.ucompensar.losatuendos.fabrica.DatosPrenda;
import co.edu.ucompensar.losatuendos.fabrica.FabricaPrenda;
import co.edu.ucompensar.losatuendos.lavanderia.AdaptadorLavanderiaExterna;
import co.edu.ucompensar.losatuendos.lavanderia.EnvioLavanderia;
import co.edu.ucompensar.losatuendos.lavanderia.ItemLavanderia;
import co.edu.ucompensar.losatuendos.lavanderia.LavanderiaExterna;
import co.edu.ucompensar.losatuendos.lavanderia.ServicioLavanderia;
import co.edu.ucompensar.losatuendos.modelo.Cliente;
import co.edu.ucompensar.losatuendos.modelo.Empleado;
import co.edu.ucompensar.losatuendos.modelo.GrupoPrendas;
import co.edu.ucompensar.losatuendos.modelo.Prenda;
import co.edu.ucompensar.losatuendos.modelo.ServicioAlquiler;
import co.edu.ucompensar.losatuendos.modelo.TipoPrenda;
import co.edu.ucompensar.losatuendos.persistencia.ClienteDAO;
import co.edu.ucompensar.losatuendos.persistencia.ConexionBD;
import co.edu.ucompensar.losatuendos.persistencia.EmpleadoDAO;
import co.edu.ucompensar.losatuendos.persistencia.LavanderiaDAO;
import co.edu.ucompensar.losatuendos.persistencia.PrendaDAO;
import co.edu.ucompensar.losatuendos.persistencia.ServicioAlquilerDAO;
import co.edu.ucompensar.losatuendos.util.Constantes;
import co.edu.ucompensar.losatuendos.util.Validador;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Fachada del negocio. La interfaz gráfica solo conoce esta clase; detrás quedan las fábricas,
 * los DAO, la lista de lavandería y el adaptador del proveedor de lavado.
 */
public class LosAtuendosFacade {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private final PrendaDAO prendaDAO = new PrendaDAO();
    private final ServicioAlquilerDAO servicioDAO = new ServicioAlquilerDAO(clienteDAO, empleadoDAO, prendaDAO);
    private final LavanderiaDAO lavanderiaDAO = new LavanderiaDAO(prendaDAO);
    private final ServicioLavanderia servicioLavanderia;

    public LosAtuendosFacade() {
        this(new AdaptadorLavanderiaExterna(new LavanderiaExterna()));
    }

    public LosAtuendosFacade(ServicioLavanderia servicioLavanderia) {
        this.servicioLavanderia = servicioLavanderia;
    }

    public Cliente registrarCliente(String identificacion, String nombre, String direccion, String telefono,
            String correo) throws NegocioException {
        Cliente cliente = new Cliente(Validador.codigo(identificacion, "Identificación"),
                Validador.texto(nombre, "Nombre"), Validador.texto(direccion, "Dirección"),
                Validador.codigo(telefono, "Teléfono"), Validador.correo(correo));
        clienteDAO.insertar(cliente);
        return cliente;
    }

    public Empleado registrarEmpleado(String identificacion, String nombre, String direccion, String telefono,
            String cargo) throws NegocioException {
        Empleado empleado = new Empleado(Validador.codigo(identificacion, "Identificación"),
                Validador.texto(nombre, "Nombre"), Validador.texto(direccion, "Dirección"),
                Validador.codigo(telefono, "Teléfono"), Validador.texto(cargo, "Cargo"));
        empleadoDAO.insertar(empleado);
        return empleado;
    }

    public Prenda registrarPrenda(DatosPrenda datos) throws NegocioException {
        if (datos.getTipo() == null) {
            throw new NegocioException("Seleccione el tipo de prenda.");
        }
        Prenda prenda = FabricaPrenda.para(datos.getTipo()).crear(datos);
        prendaDAO.insertar(prenda);
        return prenda;
    }

    /**
     * Registra un servicio de alquiler con número consecutivo y fecha de solicitud del sistema.
     * Cliente, empleado y prendas deben existir, y cada prenda debe estar libre en la fecha pedida.
     */
    public ServicioAlquiler registrarServicio(String identificacionCliente, String identificacionEmpleado,
            List<String> referencias, LocalDate fechaAlquiler) throws NegocioException {
        LocalDate hoy = LocalDate.now();
        if (fechaAlquiler == null || fechaAlquiler.isBefore(hoy)) {
            throw new NegocioException("La fecha de alquiler no puede ser anterior a hoy ("
                    + hoy.format(Constantes.FORMATO_FECHA) + ").");
        }
        Set<String> sinRepetir = new LinkedHashSet<>();
        for (String referencia : referencias) {
            if (referencia != null && !referencia.isBlank()) {
                sinRepetir.add(referencia.trim());
            }
        }
        if (sinRepetir.isEmpty()) {
            throw new NegocioException("Indique al menos una referencia de prenda.");
        }

        ServicioAlquiler.Builder builder = ServicioAlquiler.builder()
                .fechaSolicitud(hoy)
                .fechaAlquiler(fechaAlquiler)
                .cliente(consultarCliente(identificacionCliente))
                .empleado(empleadoDAO.buscar(identificacionEmpleado).orElseThrow(() -> new NegocioException(
                        "El empleado " + identificacionEmpleado + " no está registrado.")));
        for (String referencia : sinRepetir) {
            Prenda prenda = buscarPrenda(referencia);
            if (!prendaDAO.estaDisponible(referencia, fechaAlquiler)) {
                throw new NegocioException("La prenda " + referencia + " ya está alquilada para el "
                        + fechaAlquiler.format(Constantes.FORMATO_FECHA) + ".");
            }
            builder.prenda(prenda);
        }
        ServicioAlquiler servicio = builder.numero(servicioDAO.siguienteNumero()).build();
        servicioDAO.insertar(servicio);
        return servicio;
    }

    public ServicioAlquiler consultarServicio(int numero) throws NegocioException {
        return servicioDAO.buscar(numero)
                .orElseThrow(() -> new NegocioException("No existe el servicio de alquiler N.º " + numero + "."));
    }

    public Cliente consultarCliente(String identificacion) throws NegocioException {
        return clienteDAO.buscar(identificacion)
                .orElseThrow(() -> new NegocioException("El cliente " + identificacion + " no está registrado."));
    }

    /** Servicios con fecha de alquiler igual o posterior a hoy, ordenados por esa fecha. */
    public List<ServicioAlquiler> consultarServiciosVigentes(String identificacionCliente) throws NegocioException {
        return servicioDAO.listarVigentesPorCliente(identificacionCliente, LocalDate.now());
    }

    public List<ServicioAlquiler> consultarServiciosPorFecha(LocalDate fechaAlquiler) throws NegocioException {
        return servicioDAO.listarPorFecha(fechaAlquiler);
    }

    /** Arma un árbol Composite: la talla como raíz, un grupo por tipo y las prendas como hojas. */
    public GrupoPrendas consultarPrendasPorTalla(String talla) throws NegocioException {
        String tallaValida = Validador.codigo(talla, "Talla").toUpperCase();
        List<Prenda> prendas = prendaDAO.listarPorTalla(tallaValida);
        GrupoPrendas inventario = new GrupoPrendas("Prendas de talla " + tallaValida);
        for (TipoPrenda tipo : TipoPrenda.values()) {
            GrupoPrendas grupo = new GrupoPrendas(tipo.getDescripcion());
            prendas.stream().filter(prenda -> prenda.getTipo() == tipo).forEach(grupo::agregar);
            if (!grupo.estaVacio()) {
                inventario.agregar(grupo);
            }
        }
        return inventario;
    }

    public void registrarEnLavanderia(String referencia, boolean manchada, boolean delicada,
            boolean prioridadAdministrador) throws NegocioException {
        String ref = buscarPrenda(Validador.codigo(referencia, "Referencia")).getReferencia();
        if (lavanderiaDAO.estaPendiente(ref)) {
            throw new NegocioException("La prenda " + ref + " ya está en la lista de lavandería.");
        }
        lavanderiaDAO.insertar(ref, manchada, delicada, prioridadAdministrador);
    }

    /** Lista en el orden en que saldrían las prendas; consultarla no las envía. */
    public List<ItemLavanderia> consultarListaLavanderia() throws NegocioException {
        List<ItemLavanderia> items = new ArrayList<>(lavanderiaDAO.listar());
        items.sort(ItemLavanderia.ORDEN_DE_ENVIO);
        return items;
    }

    public EnvioLavanderia enviarALavanderia(int cantidad) throws NegocioException {
        if (cantidad <= 0) {
            throw new NegocioException("La cantidad de prendas a enviar debe ser mayor que cero.");
        }
        List<ItemLavanderia> pendientes = consultarListaLavanderia();
        if (pendientes.isEmpty()) {
            throw new NegocioException("No hay prendas en la lista de lavandería.");
        }
        List<ItemLavanderia> tanda = pendientes.subList(0, Math.min(cantidad, pendientes.size()));
        String guia = servicioLavanderia.enviar(tanda);
        lavanderiaDAO.eliminar(tanda);
        return new EnvioLavanderia(guia, tanda);
    }

    public boolean hayPrendasRegistradas() throws NegocioException {
        return prendaDAO.hayPrendas();
    }

    public void cerrar() {
        ConexionBD.getInstancia().cerrar();
    }

    private Prenda buscarPrenda(String referencia) throws NegocioException {
        return prendaDAO.buscar(referencia)
                .orElseThrow(() -> new NegocioException("La prenda " + referencia + " no está registrada."));
    }
}
