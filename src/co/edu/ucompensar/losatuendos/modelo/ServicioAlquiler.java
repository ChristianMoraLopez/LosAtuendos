package co.edu.ucompensar.losatuendos.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Servicio de alquiler. Se construye únicamente con {@link Builder}, que verifica que el servicio
 * quede completo antes de existir.
 */
public final class ServicioAlquiler {

    private final int numero;
    private final LocalDate fechaSolicitud;
    private final LocalDate fechaAlquiler;
    private final Cliente cliente;
    private final Empleado empleado;
    private final GrupoPrendas prendas;

    private ServicioAlquiler(Builder builder) {
        this.numero = builder.numero;
        this.fechaSolicitud = builder.fechaSolicitud;
        this.fechaAlquiler = builder.fechaAlquiler;
        this.cliente = builder.cliente;
        this.empleado = builder.empleado;
        this.prendas = builder.prendas;
    }

    public static Builder builder() {
        return new Builder();
    }

    public int getNumero() {
        return numero;
    }

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public LocalDate getFechaAlquiler() {
        return fechaAlquiler;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public List<Prenda> getPrendas() {
        return prendas.getPrendas();
    }

    public BigDecimal getValorTotal() {
        return prendas.getValorAlquiler();
    }

    /**
     * Builder del servicio de alquiler.
     */
    public static final class Builder {

        private int numero;
        private LocalDate fechaSolicitud = LocalDate.now();
        private LocalDate fechaAlquiler;
        private Cliente cliente;
        private Empleado empleado;
        private GrupoPrendas prendas;

        private Builder() {
        }

        public Builder numero(int numero) {
            this.numero = numero;
            return this;
        }

        public Builder fechaSolicitud(LocalDate fechaSolicitud) {
            this.fechaSolicitud = fechaSolicitud;
            return this;
        }

        public Builder fechaAlquiler(LocalDate fechaAlquiler) {
            this.fechaAlquiler = fechaAlquiler;
            return this;
        }

        public Builder cliente(Cliente cliente) {
            this.cliente = cliente;
            return this;
        }

        public Builder empleado(Empleado empleado) {
            this.empleado = empleado;
            return this;
        }

        public Builder prenda(Prenda prenda) {
            if (prendas == null) {
                prendas = new GrupoPrendas("Prendas del servicio");
            }
            prendas.agregar(prenda);
            return this;
        }

        /**
         * @throws IllegalStateException si falta algún dato obligatorio; la fachada valida antes
         *         de llegar aquí, así que indica un error de programación.
         */
        public ServicioAlquiler build() {
            if (numero <= 0 || fechaSolicitud == null || fechaAlquiler == null || cliente == null
                    || empleado == null || prendas == null || prendas.estaVacio()) {
                throw new IllegalStateException("Servicio de alquiler incompleto");
            }
            return new ServicioAlquiler(this);
        }
    }
}
