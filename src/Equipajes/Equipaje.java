package Equipajes;

public abstract class Equipaje {
    private static int contador=0;
    private int idEquipaje;
    private double pesoKg;

    public Equipaje(double pesoKg) {
        contador++;
        this.idEquipaje = contador;
        this.pesoKg = pesoKg;
    }
    public Equipaje(){}

    public int getIdEquipaje() {
        return idEquipaje;
    }
    public double getPesoKg() {
        return pesoKg;
    }
    public void setPesoKg(double pesoKg) {
        this.pesoKg = pesoKg;
    }

    abstract double calcularCosto();

    @Override
    public String toString() {
        return "[idEquipaje: "+idEquipaje+", pesoKg: "+pesoKg+"]";
    }
}
