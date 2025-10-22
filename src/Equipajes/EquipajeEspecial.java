package Equipajes;

public class EquipajeEspecial extends Equipaje{
    public EquipajeEspecial(double pesoKg) {
        super(pesoKg);
    }

    @Override
    public double calcularCosto() {
        return getPesoKg()*2+500;
    }

    @Override
    public String toString(){
        return "EquipajeEspecial["+super.toString()+"]";
    }
}
