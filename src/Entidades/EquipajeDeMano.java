package Entidades;

import org.json.JSONException;
import org.json.JSONObject;

public class EquipajeDeMano extends Equipaje {

    /**
     * Constructor para un nuevo EquipajeDeMano.
     */
    public EquipajeDeMano() {
        super();
    }

    /**
     * Constructor para DESERIALIZAR desde JSON.
     */
    public EquipajeDeMano(JSONObject json) throws JSONException {
        super(json);
    }

    /**
     * Convierte el objeto EquipajeDeMano a formato JSON.
     */
    public JSONObject toJSON() throws JSONException {
        // Llama al metodo del padre (que guarda ID y "tipoEquipaje")
        JSONObject jsonObject = super.toJSON();
        return jsonObject;
    }

    @Override
    public double calcularCosto(Vuelo vuelo) {
        // siempre es gratis.
        return 0.0;
    }

    @Override
    public String toString() {
        return "EquipajeDeMano[" + super.toString() + "]";
    }
}