package Entidades;

public class EquipajeEspecial extends Equipaje<Vuelo>{
    public EquipajeEspecial(double pesoKg) {
        super(pesoKg);
    }

    @Override
    public double calcularCosto(Vuelo vuelo) {
        return vuelo.getAerolinea().getCostoEquipajeEspecial();
    }

    @Override
    public String toString(){
        return "EquipajeEspecial["+super.toString()+"]";
    }
}
