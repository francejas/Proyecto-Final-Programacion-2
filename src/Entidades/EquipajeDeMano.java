package Entidades;

import org.json.JSONException;
import org.json.JSONObject;

public class EquipajeDeMano extends Equipaje {

    /**
     * Constructor para un nuevo EquipajeDeMano.
     */
    public EquipajeDeMano(double precio) {
        super(precio);
    }

    /**
     * Constructor para DESERIALIZAR desde JSON.
     */
    public EquipajeDeMano(JSONObject json) throws JSONException {
        super(json);
    }

}