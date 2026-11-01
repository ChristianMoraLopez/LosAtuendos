package co.edu.ucompensar.losatuendos.vista;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Panel de dos columnas (etiqueta y campo) con botones al final.
 */
class Formulario extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int COLUMNAS_CAMPO = 14;

    private int fila;

    Formulario(String titulo) {
        super(new GridBagLayout());
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(titulo),
                BorderFactory.createEmptyBorder(4, 6, 6, 6)));
    }

    JTextField texto(String etiqueta) {
        JTextField campo = new JTextField(COLUMNAS_CAMPO);
        agregar(etiqueta, campo);
        return campo;
    }

    JTextField texto(String etiqueta, String valorInicial) {
        JTextField campo = texto(etiqueta);
        campo.setText(valorInicial);
        return campo;
    }

    <T extends JComponent> T agregar(String etiqueta, T campo) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = fila++;
        c.insets = new Insets(3, 3, 3, 3);
        c.anchor = GridBagConstraints.WEST;
        add(new JLabel(etiqueta), c);
        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(campo, c);
        return campo;
    }

    void boton(String texto, ActionListener accion) {
        JButton boton = new JButton(texto);
        boton.addActionListener(accion);
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = fila++;
        c.gridx = 1;
        c.insets = new Insets(6, 3, 3, 3);
        c.anchor = GridBagConstraints.EAST;
        add(boton, c);
    }
}
