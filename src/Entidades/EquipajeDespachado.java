package Entidades;

import org.json.JSONException;
import org.json.JSONObject;

public class EquipajeDespachado extends Equipaje {

    /**
     * Constructor para un nuevo EquipajeDespachado.
     */
    public EquipajeDespachado(double precio) {
        super(precio);
    }
    /**
     * Constructor para DESERIALIZAR desde JSON.
     */
    public EquipajeDespachado(JSONObject json) throws JSONException {
        super(json);
    }

}