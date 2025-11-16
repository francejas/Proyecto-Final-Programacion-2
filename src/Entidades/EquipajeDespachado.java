package Entidades;

import org.json.JSONException;
import org.json.JSONObject;

public class EquipajeDespachado extends Equipaje {

    /**
     * Constructor para un nuevo EquipajeDespachado.
     */
    public EquipajeDespachado() {
        super();
    }

    /**
     * Constructor para DESERIALIZAR desde JSON.
     */
    public EquipajeDespachado(JSONObject json) throws JSONException {
        super(json);
    }

    /**
     * Convierte el objeto EquipajeDespachado a formato JSON.
     */
    public JSONObject toJSON() throws JSONException {
        // Llama al metodo del padre (que guarda ID y "tipoEquipaje")
        JSONObject jsonObject = super.toJSON();

        return jsonObject;
    }

    @Override
    public double calcularCosto(Vuelo vuelo) {
        return vuelo.getAerolinea().getCostoEquipajeDespachado();
    }

    @Override
    public String toString() {
        return "EquipajeDespachado[" + super.toString() + "]";
    }
}