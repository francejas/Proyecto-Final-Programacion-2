package Entidades;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Aerolinea {
    private String codigo;
    private String nombre;
    private List<Avion> flotaDeAviones;
    private double costoCarryOn;
    private double costoEquipajeDespachado;
    private double costoEquipajeEspecial;

    /**
     * Constructor principal.
     */
    public Aerolinea(String codigo, String nombre, double costoCarryOn,
                     double costoEquipajeDespachado, double costoEquipajeEspecial) {

        this.codigo = codigo;
        this.nombre = nombre;
        this.costoCarryOn = costoCarryOn;
        this.costoEquipajeDespachado = costoEquipajeDespachado;
        this.costoEquipajeEspecial = costoEquipajeEspecial;
        // La flota se inicializa vacía.
        this.flotaDeAviones = new ArrayList<>();
    }

    /**
     * Constructor para DESERIALIZAR desde JSON .
     */

    public Aerolinea(JSONObject json) throws JSONException {
        this.codigo = json.getString("codigo");
        this.nombre = json.getString("nombre");
        this.costoCarryOn = json.getDouble("costoCarryOn");
        this.costoEquipajeDespachado = json.getDouble("costoEquipajeDespachado");
        this.costoEquipajeEspecial = json.getDouble("costoEquipajeEspecial");

        // Deserializar la lista de aviones
        this.flotaDeAviones = new ArrayList<>();
        JSONArray jsonFlota = json.getJSONArray("flotaDeAviones");
        for (int i = 0; i < jsonFlota.length(); i++) {
            JSONObject jsonAvion = jsonFlota.getJSONObject(i);
            // Asume que la clase Avion también tiene un constructor JSON
            this.flotaDeAviones.add(new Avion(jsonAvion));
        }
    }

    /**
     * Convierte el objeto Aerolinea a formato JSON.
     */
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("codigo", this.codigo);
        jsonObject.put("nombre", this.nombre);
        jsonObject.put("costoCarryOn", this.costoCarryOn);
        jsonObject.put("costoEquipajeDespachado", this.costoEquipajeDespachado);
        jsonObject.put("costoEquipajeEspecial", this.costoEquipajeEspecial);

        // Serializar la lista de aviones
        JSONArray jsonFlota = new JSONArray();
        for (Avion avion : this.flotaDeAviones) {
            // Asume que la clase Avion tambien tiene un metodo JSON
            jsonFlota.put(avion.toJSON());
        }
        jsonObject.put("flotaDeAviones", jsonFlota);

        return jsonObject;
    }

    // --- Métodos de Lógica ---

    public void agregarAvionAlaFlota(Avion avion) {
        this.flotaDeAviones.add(avion);
    }

    public List<Avion> getFlotaDeAviones() {
        // Devuelve una copia para que la lista original no pueda ser modificada desde fuera.
        return new ArrayList<>(this.flotaDeAviones);
    }

    // --- Getters y Setters  ---

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFlotaDeAviones(List<Avion> flotaDeAviones) {
        this.flotaDeAviones = flotaDeAviones;
    }

    public double getCostoCarryOn() {
        return costoCarryOn;
    }

    public void setCostoCarryOn(double costoCarryOn) {
        this.costoCarryOn = costoCarryOn;
    }

    public double getCostoEquipajeDespachado() {
        return costoEquipajeDespachado;
    }

    public void setCostoEquipajeDespachado(double costoEquipajeDespachado) {
        this.costoEquipajeDespachado = costoEquipajeDespachado;
    }

    public double getCostoEquipajeEspecial() {
        return costoEquipajeEspecial;
    }

    public void setCostoEquipajeEspecial(double costoEquipajeEspecial) {
        this.costoEquipajeEspecial = costoEquipajeEspecial;
    }

    // --- equals(), hashCode() y toString() ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aerolinea aerolinea = (Aerolinea) o;
        return Objects.equals(codigo, aerolinea.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return nombre + " (" + codigo + ")";
    }
}