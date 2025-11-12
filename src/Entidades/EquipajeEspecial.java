package Entidades;

public class EquipajeEspecial extends Equipaje {

    public EquipajeEspecial() {
        super();
    }

    @Override
    public double calcularCosto(Vuelo vuelo) {
        return vuelo.getAerolinea().getCostoEquipajeEspecial();
    }

    @Override
    public String toString() {
        return "EquipajeEspecial[" + super.toString() + "]";
    }
}