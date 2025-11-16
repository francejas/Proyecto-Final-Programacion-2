package Entidades;

import Enum.RolUsuario;
import org.json.JSONException;
import org.json.JSONObject;

public class Administrador extends Usuario {


    public Administrador(String nombre, String email, String password, boolean activo) {
        super(nombre, email, password, RolUsuario.ADMINISTRADOR, activo);
    }

    /**
     * Constructor para DESERIALIZAR desde JSON.
     */
    public Administrador(JSONObject json) throws JSONException {
        super(json); // Llama al constructor JSON del padre
    }

    /**
     * Convierte el objeto Administrador a formato JSON.
     */
    @Override
    public JSONObject toJSON() throws JSONException {
        // Llama al toJSON() del padre y lo devuelve, ya que Administrador no tiene atributos propios.
        return super.toJSON();
    }

    @Override
    public String toString() {
        return "Administrador{" +
                "usuario_info=" + super.toString() +
                '}';
    }
}