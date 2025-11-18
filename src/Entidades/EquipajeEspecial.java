package Entidades;

import org.json.JSONException;
import org.json.JSONObject;

public class EquipajeEspecial extends Equipaje {

    /**
     * Constructor para un nuevo EquipajeEspecial.
     */
    public EquipajeEspecial(double precio) {
        super(precio);
    }

    /**
     * Constructor para DESERIALIZAR desde JSON.
     */
    public EquipajeEspecial(JSONObject json) throws JSONException {
        super(json);
    }


}

