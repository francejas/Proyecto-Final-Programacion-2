package Equipajes;

public class Dimensiones {
    private double altoCm;
    private double anchoCm;
    private double profundidadCm;

    public Dimensiones(double altoCm, double anchoCm, double profundidadCm) {
        this.altoCm = altoCm;
        this.anchoCm = anchoCm;
        this.profundidadCm = profundidadCm;
    }
    public Dimensiones() {}

    public double getAltoCm() {
        return altoCm;
    }
    public void setAltoCm(double altoCm) {
        this.altoCm = altoCm;
    }
    public double getAnchoCm() {
        return anchoCm;
    }
    public void setAnchoCm(double anchoCm) {
        this.anchoCm = anchoCm;
    }
    public double getProfundidadCm() {
        return profundidadCm;
    }
    public void setProfundidadCm(double profundidadCm) {
        this.profundidadCm = profundidadCm;
    }

    @Override
    public String toString(){
        return "Dimensiones[Altura (cm): "+altoCm+", Anchura (cm): "+anchoCm+", Profundidad (cm): "+profundidadCm+"]";
    }
}
