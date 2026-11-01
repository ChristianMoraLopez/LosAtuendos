package co.edu.ucompensar.losatuendos.lavanderia;

public class PrendaDelicada extends DecoradorLavanderia {

    public PrendaDelicada(ItemLavanderia decorado) {
        super(decorado);
    }

    @Override
    protected String getIndicacion() {
        return "Delicada: lavado en seco o a mano";
    }
}
