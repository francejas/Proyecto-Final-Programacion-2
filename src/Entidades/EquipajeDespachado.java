package Entidades;

public class EquipajeDespachado extends Equipaje {

    public EquipajeDespachado() {
        super();
    }

    @Override
    public double calcularCosto(Vuelo vuelo) {
        return vuelo.getAerolinea().getCostoEquipajeDespachado();
    }

    @Override
    public String toString() {
        return "EquipajeDespachado[" + super.toString() + "]";
    }
}