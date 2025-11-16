package Entidades;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.UUID;

public abstract class Equipaje {

    private final String idEquipaje;

    /**
     * Constructor para un nuevo equipaje.
     */
    public Equipaje() {
        this.idEquipaje = "EQP-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    /**
     * Constructor para DESERIALIZAR desde JSON.
     * Lee el ID del objeto JSON.
     */
    public Equipaje(JSONObject json) throws JSONException {
        this.idEquipaje = json.getString("idEquipaje");
    }

    public abstract double calcularCosto(Vuelo vuelo);

    /**
     * Convierte el objeto Equipaje a formato JSON.
     */
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("idEquipaje", this.idEquipaje);

        // Guarda el nombre simple de la clase (ej: "CarryOn", "EquipajeDeMano")
        // Esto es vital para que el deserializador sepa qué clase crear.
        jsonObject.put("tipoEquipaje", this.getClass().getSimpleName());

        return jsonObject;
    }

    // --- Getter ---
    public String getIdEquipaje() {
        return idEquipaje;
    }

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