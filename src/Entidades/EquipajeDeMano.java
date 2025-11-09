package Entidades;

public class EquipajeDeMano extends Equipaje<Void>{
    public EquipajeDeMano(double pesoKg) {
        super(pesoKg);
    }
    public EquipajeDeMano(){}

    @Override
    public double calcularCosto(Void element) {
        return 0.0;
    }

    @Override
    public String toString(){
        return "EquipajeDeMano["+super.toString()+"]";
    }
}
