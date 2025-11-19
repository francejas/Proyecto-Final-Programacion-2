package Entidades;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.UUID;

public abstract class Equipaje {

    private final String idEquipaje;
    private final double precioCongelado;

    /**
     * Constructor para un nuevo equipaje.
     * Recibe el precio calculado y lo congela para siempre.
     */
    public Equipaje(double precio) {
        this.idEquipaje = "EQP-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        this.precioCongelado = precio;
    }

    /**
     * Constructor para DESERIALIZAR desde JSON.
     */
    public Equipaje(JSONObject json) throws JSONException {
        this.idEquipaje = json.getString("idEquipaje");
        this.precioCongelado = json.getDouble("precioCongelado"); // Recupera el precio
    }

    /**
     * Convierte el objeto Equipaje a formato JSON.
     * Guarda ID, Tipo y PRECIO.
     */
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("idEquipaje", this.idEquipaje);
        jsonObject.put("precioCongelado", this.precioCongelado); // Guarda el precio
        jsonObject.put("tipoEquipaje", this.getClass().getSimpleName()); // Guarda el tipo
        return jsonObject;
    }


    public double getCosto() {
        return precioCongelado;
    }

    public String getIdEquipaje() {
        return idEquipaje;
    }

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
        return "[" + this.getClass().getSimpleName() + " | $" + precioCongelado + "]";
    }
}