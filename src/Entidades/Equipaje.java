package Entidades;

import java.util.UUID;

public abstract class Equipaje<T> {
    private final UUID idEquipaje;
    private double pesoKg;

    public Equipaje(double pesoKg) {
        this.idEquipaje = UUID.randomUUID();
        this.pesoKg = pesoKg;
    }
    public Equipaje(){
        this.idEquipaje = UUID.randomUUID();
    }

    public UUID getIdEquipaje() {
        return idEquipaje;
    }
    public double getPesoKg() {
        return pesoKg;
    }
    public void setPesoKg(double pesoKg) {
        this.pesoKg = pesoKg;
    }

    abstract double calcularCosto(T element);

    @Override
    public String toString() {
        return "[idEquipaje: "+idEquipaje+", pesoKg: "+pesoKg+"]";
    }
}