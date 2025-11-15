package Entidades;

import org.json.JSONException;
import org.json.JSONObject;

public class EquipajeEspecial extends Equipaje {

    /**
     * Constructor para un nuevo EquipajeEspecial.
     */
    public EquipajeEspecial() {
        super();
    }

    /**
     * Constructor para DESERIALIZAR desde JSON.
     */
    public EquipajeEspecial(JSONObject json) throws JSONException {
        super(json);
    }

    /**
     * Convierte el objeto EquipajeEspecial a formato JSON.
     */
    public JSONObject toJSON() throws JSONException {
        // Llama al metodo del padre (que guarda ID y "tipoEquipaje")
        JSONObject jsonObject = super.toJSON();

        return jsonObject;
    }

    @Override
    public double calcularCosto(Vuelo vuelo) {
        return vuelo.getAerolinea().getCostoEquipajeEspecial();
    }

    @Override
    public String toString() {
        return "EquipajeEspecial[" + super.toString() + "]";
    }
}