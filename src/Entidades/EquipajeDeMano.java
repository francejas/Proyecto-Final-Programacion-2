package Entidades;

public class EquipajeDeMano extends Equipaje {

    public EquipajeDeMano() {
        super();

    }

    @Override
    public double calcularCosto(Vuelo vuelo) {
        // siempre es gratis.
        return 0.0;
    }

    @Override
    public String toString() {
        return "EquipajeDeMano[" + super.toString() + "]";
    }
}