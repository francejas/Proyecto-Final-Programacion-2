package Entidades;

public class CarryOn extends Equipaje<Vuelo>{
    public CarryOn(double pesoKg) {
        super(pesoKg);
    }
    public CarryOn(){}

    @Override
    public double calcularCosto(Vuelo vuelo) {
        if(vuelo.isCarryOnGratis()){
            return 0.0;
        }
        return vuelo.getAerolinea().getCostoCarryOn();
    }

    @Override
    public String toString() {
        return "CarryOn["+super.toString()+" ]";
    }
}