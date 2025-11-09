package Entidades;

public class EquipajeDespachado extends  Equipaje<Vuelo> {
    public EquipajeDespachado(double pesoKg) {
        super(pesoKg);
    }

    @Override
    public double calcularCosto(Vuelo vuelo) {
        return vuelo.getAerolinea().getCostoEquipajeDespachado();
    }

    @Override
    public String toString(){
        return "EquipajeDespachado["+super.toString()+"]";
    }
}
