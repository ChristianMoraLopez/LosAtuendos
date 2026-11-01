package co.edu.ucompensar.losatuendos.lavanderia;

public class PrioridadAdministrador extends DecoradorLavanderia {

    public PrioridadAdministrador(ItemLavanderia decorado) {
        super(decorado);
    }

    @Override
    protected String getIndicacion() {
        return "Prioridad asignada por el administrador";
    }
}
