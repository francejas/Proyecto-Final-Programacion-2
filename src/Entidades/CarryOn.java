package Entidades;

import org.json.JSONException;
import org.json.JSONObject;

public class CarryOn extends Equipaje {

    /**
     * Constructor para un nuevo CarryOn.
     */
    public CarryOn() {
        super();
    }

    /**
     * Constructor para DESERIALIZAR desde JSON.
     * Llama al constructor de la clase padre.
     */
    public CarryOn(JSONObject json) throws JSONException {
        super(json);
    }

    /**
     * Convierte el objeto CarryOn a formato JSON.
     * Llama al metodo del padre (que guarda ID y "tipoEquipaje").
     */
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = super.toJSON();
        return jsonObject;
    }

    /**
     * Calcula el costo del CarryOn.
     * Primero, verifica si el Vuelo específico lo ofrece gratis.
     * Si no es gratis, le pregunta el precio a la Aerolínea.
     */
    @Override
    public double calcularCosto(Vuelo vuelo) {
        if (vuelo.isCarryOnGratis()) {
            return 0.0;
        } else {
            return vuelo.getAerolinea().getCostoCarryOn();
        }
    }

    @Override
    public String toString() {
        return "CarryOn[" + super.toString() + "]";
    }
}