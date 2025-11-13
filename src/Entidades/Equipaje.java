package Entidades;

import java.util.Objects;
import java.util.UUID;

public abstract class Equipaje {

    private final String idEquipaje;

    public Equipaje() {
        this.idEquipaje = "EQP-" + UUID.randomUUID().toString();
    }

    public String getIdEquipaje() {
        return idEquipaje;
    }

    public abstract double calcularCosto(Vuelo vuelo);

    // --- equals() y hashCode() basados en el ID ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipaje equipaje = (Equipaje) o;
        return Objects.equals(idEquipaje, equipaje.idEquipaje);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEquipaje);
    }

    @Override
    public String toString() {
        return "[idEquipaje: " + idEquipaje + "]";
    }
}