package Entidades;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Aeropuerto {
    private String codigoIATA;
    private String nombre;
    private String ciudad;


    public Aeropuerto(String codigoIATA, String nombre, String ciudad) {
        this.codigoIATA = (codigoIATA != null) ? codigoIATA.toUpperCase() : null;
        this.nombre = nombre;
        this.ciudad = ciudad;
    }

    /**
     * Constructor para DESERIALIZAR desde JSON.
     */
    public Aeropuerto(JSONObject json) throws JSONException {
        this.codigoIATA = json.getString("codigoIATA");
        this.nombre = json.getString("nombre");
        this.ciudad = json.getString("ciudad");
    }

    /**
     * Convierte el objeto Aeropuerto a formato JSON.
     */
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("codigoIATA", this.codigoIATA);
        jsonObject.put("nombre", this.nombre);
        jsonObject.put("ciudad", this.ciudad);
        return jsonObject;
    }

    // --- Getters y Setters ---

    public String getCodigoIATA() {
        return codigoIATA;
    }

    public void setCodigoIATA(String codigoIATA) {
        this.codigoIATA = (codigoIATA != null) ? codigoIATA.toUpperCase() : null;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    // --- equals(), hashCode() y toString() ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aeropuerto that = (Aeropuerto) o;
        return Objects.equals(codigoIATA, that.codigoIATA);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoIATA);
    }

    @Override
    public String toString() {
        return nombre + " (" + ciudad + ") - [" + codigoIATA + "]";
    }
}