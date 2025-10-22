package Entidades;

public class EquipajeDespachado extends  Equipaje {
    public EquipajeDespachado(double pesoKg) {
        super(pesoKg);
    }

    @Override
    public double calcularCosto() {
        return getPesoKg()*10;
    }

    @Override
    public String toString(){
        return "EquipajeDespachado["+super.toString()+"]";
    }
}
