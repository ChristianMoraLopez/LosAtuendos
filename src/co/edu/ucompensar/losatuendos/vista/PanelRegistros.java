package co.edu.ucompensar.losatuendos.vista;

import co.edu.ucompensar.losatuendos.fabrica.DatosPrenda;
import co.edu.ucompensar.losatuendos.modelo.Accesorio;
import co.edu.ucompensar.losatuendos.modelo.Cliente;
import co.edu.ucompensar.losatuendos.modelo.Empleado;
import co.edu.ucompensar.losatuendos.modelo.Prenda;
import co.edu.ucompensar.losatuendos.modelo.TipoPrenda;
import co.edu.ucompensar.losatuendos.modelo.TipoTraje;
import co.edu.ucompensar.losatuendos.reporte.FormatoSalida;
import java.awt.CardLayout;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Registro de clientes, empleados y prendas.
 */
class PanelRegistros extends JPanel {

    private static final long serialVersionUID = 1L;

    private final transient VentanaPrincipal ventana;

    PanelRegistros(VentanaPrincipal ventana) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.ventana = ventana;
        add(formularioCliente());
        add(formularioEmpleado());
        add(formularioPrenda());
    }

    private Formulario formularioCliente() {
        Formulario f = new Formulario("Cliente");
        JTextField id = f.texto("Identificación");
        JTextField nombre = f.texto("Nombre");
        JTextField direccion = f.texto("Dirección");
        JTextField telefono = f.texto("Teléfono");
        JTextField correo = f.texto("Correo electrónico");
        f.boton("Registrar cliente", e -> ventana.ejecutar(() -> {
            Cliente cliente = ventana.fachada().registrarCliente(id.getText(), nombre.getText(),
                    direccion.getText(), telefono.getText(), correo.getText());
            confirmar("Cliente registrado", cliente.toString());
            limpiar(id, nombre, direccion, telefono, correo);
        }));
        return f;
    }

    private Formulario formularioEmpleado() {
        Formulario f = new Formulario("Empleado");
        JTextField id = f.texto("Identificación");
        JTextField nombre = f.texto("Nombre");
        JTextField direccion = f.texto("Dirección");
        JTextField telefono = f.texto("Teléfono");
        JTextField cargo = f.texto("Cargo");
        f.boton("Registrar empleado", e -> ventana.ejecutar(() -> {
            Empleado empleado = ventana.fachada().registrarEmpleado(id.getText(), nombre.getText(),
                    direccion.getText(), telefono.getText(), cargo.getText());
            confirmar("Empleado registrado", empleado + ", " + empleado.getCargo());
            limpiar(id, nombre, direccion, telefono, cargo);
        }));
        return f;
    }

    private Formulario formularioPrenda() {
        Formulario f = new Formulario("Prenda");
        JComboBox<TipoPrenda> tipo = f.agregar("Tipo", new JComboBox<>(TipoPrenda.values()));
        JTextField referencia = f.texto("Referencia");
        JTextField color = f.texto("Color");
        JTextField marca = f.texto("Marca");
        JTextField talla = f.texto("Talla");
        JTextField valor = f.texto("Valor del alquiler");

        Formulario vestido = new Formulario("Vestido de dama");
        JCheckBox pedreria = vestido.agregar("Pedrería", new JCheckBox("Tiene pedrería"));
        JCheckBox largo = vestido.agregar("Largo", new JCheckBox("Es largo"));
        JTextField piezas = vestido.texto("Cantidad de piezas", "1");

        Formulario traje = new Formulario("Traje de caballero");
        JComboBox<TipoTraje> tipoTraje = traje.agregar("Tipo de traje", new JComboBox<>(TipoTraje.values()));
        JComboBox<Accesorio> accesorio = traje.agregar("Accesorio", new JComboBox<>(Accesorio.values()));

        Formulario disfraz = new Formulario("Disfraz");
        JTextField nombreDisfraz = disfraz.texto("Nombre");

        CardLayout tarjetas = new CardLayout();
        JPanel especificos = new JPanel(tarjetas);
        especificos.add(vestido, TipoPrenda.VESTIDO_DAMA.name());
        especificos.add(traje, TipoPrenda.TRAJE_CABALLERO.name());
        especificos.add(disfraz, TipoPrenda.DISFRAZ.name());
        tipo.addActionListener(e -> tarjetas.show(especificos, ((TipoPrenda) tipo.getSelectedItem()).name()));
        f.agregar("", especificos);

        f.boton("Registrar prenda", e -> ventana.ejecutar(() -> {
            DatosPrenda datos = new DatosPrenda((TipoPrenda) tipo.getSelectedItem())
                    .comunes(referencia.getText(), color.getText(), marca.getText(), talla.getText(), valor.getText())
                    .vestido(pedreria.isSelected(), largo.isSelected(), piezas.getText())
                    .traje((TipoTraje) tipoTraje.getSelectedItem(), (Accesorio) accesorio.getSelectedItem())
                    .disfraz(nombreDisfraz.getText());
            Prenda prenda = ventana.fachada().registrarPrenda(datos);
            confirmar("Prenda registrada", prenda.toString());
            limpiar(referencia, color, marca, talla, valor, nombreDisfraz);
        }));
        return f;
    }

    private void confirmar(String titulo, String detalle) {
        FormatoSalida formato = ventana.formatoPantalla();
        ventana.mostrar(formato.documento(formato.titulo(titulo) + formato.elemento(detalle)));
    }

    private static void limpiar(JTextField... campos) {
        for (JTextField campo : campos) {
            campo.setText("");
        }
    }
}
