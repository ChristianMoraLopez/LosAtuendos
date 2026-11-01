package co.edu.ucompensar.losatuendos;

import co.edu.ucompensar.losatuendos.excepciones.NegocioException;
import co.edu.ucompensar.losatuendos.fabrica.DatosPrenda;
import co.edu.ucompensar.losatuendos.fachada.LosAtuendosFacade;
import co.edu.ucompensar.losatuendos.modelo.Accesorio;
import co.edu.ucompensar.losatuendos.modelo.TipoPrenda;
import co.edu.ucompensar.losatuendos.modelo.TipoTraje;
import java.time.LocalDate;
import java.util.List;

/**
 * Inventario, personas y servicios de ejemplo para que la aplicación tenga datos la primera vez
 * que se abre. Todo entra por la fachada, con las mismas validaciones que el registro manual.
 */
final class DatosDemostracion {

    private DatosDemostracion() {
    }

    static void cargarSiVacio(LosAtuendosFacade fachada) throws NegocioException {
        if (fachada.hayPrendasRegistradas()) {
            return;
        }
        fachada.registrarEmpleado("1001", "Laura Rincón", "Cra 7 # 45-10", "3104567890", "Asesora comercial");
        fachada.registrarEmpleado("1002", "Andrés Patiño", "Cl 80 # 20-15", "3001237788", "Administrador");
        fachada.registrarCliente("52300111", "Marcela Ortiz", "Cl 26 # 68-35", "3159871234", "marcela.ortiz@correo.com");
        fachada.registrarCliente("80100222", "Julián Herrera", "Av 68 # 68B-45", "3207654321", "julian.herrera@correo.com");

        fachada.registrarPrenda(new DatosPrenda(TipoPrenda.VESTIDO_DAMA)
                .comunes("VD-001", "Rojo", "Atelier Bogotá", "M", "180000").vestido(true, true, "2"));
        fachada.registrarPrenda(new DatosPrenda(TipoPrenda.VESTIDO_DAMA)
                .comunes("VD-002", "Azul noche", "Gala Moda", "S", "250000").vestido(false, false, "1"));
        fachada.registrarPrenda(new DatosPrenda(TipoPrenda.TRAJE_CABALLERO)
                .comunes("TC-001", "Negro", "Sastrería Real", "M", "150000").traje(TipoTraje.FRAC, Accesorio.CORBATIN));
        fachada.registrarPrenda(new DatosPrenda(TipoPrenda.TRAJE_CABALLERO)
                .comunes("TC-002", "Gris", "Caballero Fino", "L", "120000").traje(TipoTraje.CONVENCIONAL, Accesorio.CORBATA));
        fachada.registrarPrenda(new DatosPrenda(TipoPrenda.DISFRAZ)
                .comunes("DF-001", "Verde", "Disfraces Halloween", "M", "60000").disfraz("Peter Pan"));
        fachada.registrarPrenda(new DatosPrenda(TipoPrenda.DISFRAZ)
                .comunes("DF-002", "Negro", "Disfraces Halloween", "L", "70000").disfraz("Zorro"));

        LocalDate hoy = LocalDate.now();
        fachada.registrarServicio("52300111", "1001", List.of("VD-001", "TC-001"), hoy.plusDays(3));
        fachada.registrarServicio("52300111", "1001", List.of("DF-001"), hoy.plusDays(1));
        fachada.registrarServicio("80100222", "1002", List.of("TC-002", "DF-002"), hoy.plusDays(3));
    }
}
