package Entidades;

public class CarryOn extends Equipaje {

    public CarryOn() {
        super();
    }

    @Override
    public double calcularCosto(Vuelo vuelo) {
        if (vuelo.isCarryOnGratis()) {
            return 0.0;
        } else {
            return vuelo.getAerolinea().getCostoCarryOn();
        }
    }

    @Override
    public String toString() {
        return "CarryOn[" + super.toString() + "]";
    }
}