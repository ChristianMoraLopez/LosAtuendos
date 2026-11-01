package co.edu.ucompensar.losatuendos;

import co.edu.ucompensar.losatuendos.excepciones.NegocioException;
import co.edu.ucompensar.losatuendos.fabrica.DatosPrenda;
import co.edu.ucompensar.losatuendos.fachada.LosAtuendosFacade;
import co.edu.ucompensar.losatuendos.lavanderia.EnvioLavanderia;
import co.edu.ucompensar.losatuendos.lavanderia.ItemLavanderia;
import co.edu.ucompensar.losatuendos.modelo.GrupoPrendas;
import co.edu.ucompensar.losatuendos.modelo.ServicioAlquiler;
import co.edu.ucompensar.losatuendos.modelo.TipoPrenda;
import co.edu.ucompensar.losatuendos.reporte.FormatoTexto;
import co.edu.ucompensar.losatuendos.reporte.ReporteCliente;
import co.edu.ucompensar.losatuendos.reporte.ReporteLavanderia;
import co.edu.ucompensar.losatuendos.reporte.ReportePrendasPorTalla;
import co.edu.ucompensar.losatuendos.util.Constantes;
import java.time.LocalDate;
import java.util.List;

/**
 * Recorre cada funcionalidad del enunciado contra una base de datos en memoria e imprime el
 * resultado de cada caso. Termina con código 1 si alguno falla.
 */
public final class PruebaFuncional {

    private static int fallos;

    private PruebaFuncional() {
    }

    @FunctionalInterface
    private interface Caso {
        void ejecutar() throws Exception;
    }

    public static void main(String[] args) throws NegocioException {
        System.setProperty(Constantes.PROPIEDAD_URL_BD, "jdbc:h2:mem:prueba;DB_CLOSE_DELAY=-1");
        LosAtuendosFacade fachada = new LosAtuendosFacade();
        DatosDemostracion.cargarSiVacio(fachada);
        LocalDate hoy = LocalDate.now();

        caso("Registro de prenda por fábrica", () -> verificar(
                fachada.registrarPrenda(new DatosPrenda(TipoPrenda.DISFRAZ)
                        .comunes("DF-003", "Rojo", "Fiesta", "m", "50000").disfraz("Caperucita"))
                        .getTalla().equals("M")));
        caso("Rechaza prenda sin valor", () -> esperarError(() -> fachada.registrarPrenda(
                new DatosPrenda(TipoPrenda.DISFRAZ).comunes("DF-004", "Azul", "Fiesta", "M", "").disfraz("Pirata"))));
        caso("Rechaza cliente duplicado", () -> esperarError(() -> fachada.registrarCliente(
                "52300111", "Otra", "Cl 1", "300", "otra@correo.com")));
        caso("Rechaza correo inválido", () -> esperarError(() -> fachada.registrarCliente(
                "999", "Ana", "Cl 1", "300", "sin-arroba")));

        caso("Servicio con número consecutivo", () -> {
            ServicioAlquiler s = fachada.registrarServicio("80100222", "1001", List.of("VD-002"), hoy.plusDays(5));
            verificar(s.getNumero() == 4 && s.getFechaSolicitud().equals(hoy));
        });
        caso("Rechaza prenda ocupada en la fecha", () -> esperarError(() ->
                fachada.registrarServicio("80100222", "1001", List.of("VD-001"), hoy.plusDays(3))));
        caso("Acepta la misma prenda otro día", () -> verificar(
                fachada.registrarServicio("80100222", "1001", List.of("VD-001"), hoy.plusDays(4)).getNumero() == 5));
        caso("Rechaza cliente no registrado", () -> esperarError(() ->
                fachada.registrarServicio("000", "1001", List.of("DF-002"), hoy.plusDays(9))));
        caso("Rechaza empleado no registrado", () -> esperarError(() ->
                fachada.registrarServicio("52300111", "000", List.of("DF-002"), hoy.plusDays(9))));
        caso("Rechaza prenda no registrada", () -> esperarError(() ->
                fachada.registrarServicio("52300111", "1001", List.of("XX-999"), hoy.plusDays(9))));
        caso("Rechaza fecha pasada", () -> esperarError(() ->
                fachada.registrarServicio("52300111", "1001", List.of("DF-002"), hoy.minusDays(1))));

        caso("Consulta servicio con valor total (Composite)", () -> verificar(
                fachada.consultarServicio(1).getValorTotal().intValue() == 330000));
        caso("Servicios vigentes ordenados por fecha", () -> {
            List<ServicioAlquiler> vigentes = fachada.consultarServiciosVigentes("52300111");
            imprimir(new ReporteCliente(new FormatoTexto(), fachada.consultarCliente("52300111"), vigentes).generar());
            verificar(vigentes.size() == 2 && vigentes.get(0).getNumero() == 2);
        });
        caso("Servicios por fecha", () -> verificar(fachada.consultarServiciosPorFecha(hoy.plusDays(3)).size() == 2));
        caso("Prendas por talla separadas por tipo", () -> {
            GrupoPrendas talla = fachada.consultarPrendasPorTalla("m");
            imprimir(new ReportePrendasPorTalla(new FormatoTexto(), talla).generar());
            verificar(talla.getHijos().size() == 3 && talla.getPrendas().size() == 4);
        });

        caso("Lista de lavandería con prioridad (Decorator)", () -> {
            fachada.registrarEnLavanderia("TC-002", false, false, false);
            fachada.registrarEnLavanderia("DF-001", false, false, false);
            fachada.registrarEnLavanderia("VD-001", true, true, false);
            fachada.registrarEnLavanderia("DF-002", false, false, true);
            List<ItemLavanderia> lista = fachada.consultarListaLavanderia();
            imprimir(new ReporteLavanderia(new FormatoTexto(), "Lista", null, lista).generar());
            verificar(lista.get(0).getPrenda().getReferencia().equals("VD-001")
                    && lista.get(0).getIndicaciones().size() == 2
                    && lista.get(1).getPrenda().getReferencia().equals("DF-002")
                    && lista.get(2).getPrenda().getReferencia().equals("TC-002"));
        });
        caso("Rechaza prenda repetida en lavandería", () -> esperarError(() ->
                fachada.registrarEnLavanderia("TC-002", false, false, false)));
        caso("Ver la lista no envía prendas", () -> verificar(fachada.consultarListaLavanderia().size() == 4));
        caso("Envío por tandas (Adapter)", () -> {
            EnvioLavanderia envio = fachada.enviarALavanderia(3);
            imprimir(new ReporteLavanderia(new FormatoTexto(), "Envío", envio.getGuia(), envio.getPrendas()).generar());
            verificar(envio.getPrendas().size() == 3 && envio.getGuia().startsWith("LAV-")
                    && fachada.consultarListaLavanderia().size() == 1);
        });
        caso("Envío mayor a lo pendiente envía lo que hay", () ->
                verificar(fachada.enviarALavanderia(10).getPrendas().size() == 1));
        caso("Rechaza envío con lista vacía", () -> esperarError(() -> fachada.enviarALavanderia(1)));

        fachada.cerrar();
        System.out.println(fallos == 0 ? "\nTODAS LAS PRUEBAS PASARON" : "\nPRUEBAS FALLIDAS: " + fallos);
        System.exit(fallos == 0 ? 0 : 1);
    }

    private static void caso(String nombre, Caso caso) {
        try {
            caso.ejecutar();
            System.out.println("[OK]    " + nombre);
        } catch (Exception | AssertionError e) {
            fallos++;
            System.out.println("[FALLA] " + nombre + " -> " + e);
        }
    }

    private static void verificar(boolean condicion) {
        if (!condicion) {
            throw new AssertionError("resultado inesperado");
        }
    }

    private static void esperarError(Caso caso) throws Exception {
        try {
            caso.ejecutar();
        } catch (NegocioException e) {
            System.out.println("        mensaje: " + e.getMessage());
            return;
        }
        throw new AssertionError("se esperaba NegocioException");
    }

    private static void imprimir(String texto) {
        System.out.println(texto.replaceAll("(?m)^", "        "));
    }
}
