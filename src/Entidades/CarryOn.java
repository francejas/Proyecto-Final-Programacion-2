package Entidades;

public class CarryOn extends Equipaje{
    private Dimensiones dimensiones;
    public CarryOn(double pesoKg, Dimensiones dimensiones) {
        super(pesoKg);
        this.dimensiones = dimensiones;
    }

    @Override
    public double calcularCosto() {
        double volumen = dimensiones.getAltoCm() * dimensiones.getAnchoCm() * dimensiones.getProfundidadCm();

        double costoBase = 50.0;
        double recargoXpeso = getPesoKg() * 1.5;

        double recargoVolumen = 0;
        if(volumen > 60000){
            recargoVolumen = (volumen - 60000)/1000;
        }

        return costoBase + recargoXpeso + recargoVolumen;
    }

    @Override
    public String toString() {
        return "CarryOn["+super.toString()+", "+dimensiones.toString()+"]";
    }
}
