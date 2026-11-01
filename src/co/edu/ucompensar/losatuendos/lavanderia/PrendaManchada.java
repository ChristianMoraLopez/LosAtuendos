package co.edu.ucompensar.losatuendos.lavanderia;

public class PrendaManchada extends DecoradorLavanderia {

    public PrendaManchada(ItemLavanderia decorado) {
        super(decorado);
    }

    @Override
    protected String getIndicacion() {
        return "Manchada: tratar manchas antes del lavado";
    }
}
