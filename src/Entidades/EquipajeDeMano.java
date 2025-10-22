package Entidades;

public class EquipajeDeMano extends Equipaje{
    private Dimensiones dimensiones;
    public EquipajeDeMano(double pesoKg, Dimensiones dimensiones) {
        super(pesoKg);
        this.dimensiones = dimensiones;
    }

    @Override
    public double calcularCosto() {
        double volumen = dimensiones.getAltoCm() * dimensiones.getAnchoCm() * dimensiones.getProfundidadCm();

        // El getPesoKg <= 8, se puede modificar, seria como un costa extra por si se excede de peso
        if(volumen <= 40000 && getPesoKg() <= 8){
            return 0;
        }else{
            return 25;
        }
    }

    @Override
    public String toString(){
        return "EquipajeDeMano["+super.toString()+", "+dimensiones.toString()+"]";
    }
}
