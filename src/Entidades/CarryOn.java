package Entidades;

import org.json.JSONException;
import org.json.JSONObject;

public class CarryOn extends Equipaje {

    /**
     * Constructor. Recibe el precio calculado en ese momento.
     */
    public CarryOn(double precio) {
        super(precio);
    }

    /**
     * Constructor JSON.
     */
    public CarryOn(JSONObject json) throws JSONException {
        super(json);
    }

}