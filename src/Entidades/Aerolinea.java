package Entidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Representa una aerolínea en el sistema.
 * Contiene los datos de la compañía, su flota de aviones
 * y las reglas de negocio (precios de equipaje y estado de activación).
 *
 * @version 2.1 (Diseño con 'baja lógica')
 * @since 2025-11-05
 */
public class Aerolinea {
    private String codigo;
    private String nombre;
    private List<Avion> flotaDeAviones;
    private double costoCarryOn;
    private double costoEquipajeDespachado;
    private double costoEquipajeEspecial;

    /**
     * Define si la aerolínea está activa (true) o inactiva (false).
     * Esto permite al administrador gestionarla con alta/baja lógica.
     */
    private boolean activa;

    /**
     * Constructor principal (usado por el PobladorDeDatos).
     * Una aerolínea siempre se crea como 'activa' por defecto.
     */
    public Aerolinea(String codigo, String nombre, double costoCarryOn,
                     double costoEquipajeDespachado, double costoEquipajeEspecial) {

        this.codigo = codigo;
        this.nombre = nombre;
        this.costoCarryOn = costoCarryOn;
        this.costoEquipajeDespachado = costoEquipajeDespachado;
        this.costoEquipajeEspecial = costoEquipajeEspecial;
        this.flotaDeAviones = new ArrayList<>();

        /**
         * Se inicializa como 'true' por defecto.
         */
        this.activa = true;
    }

    /**
     * Constructor para DESERIALIZAR desde JSON.
     * Lee el atributo 'activa'.
     */
    public Aerolinea(JSONObject json) throws JSONException {
        this.codigo = json.getString("codigo");
        this.nombre = json.getString("nombre");
        this.costoCarryOn = json.getDouble("costoCarryOn");
        this.costoEquipajeDespachado = json.getDouble("costoEquipajeDespachado");
        this.costoEquipajeEspecial = json.getDouble("costoEquipajeEspecial");

        this.activa = json.getBoolean("activa");

        // Deserializar la lista de aviones
        this.flotaDeAviones = new ArrayList<>();
        JSONArray jsonFlota = json.getJSONArray("flotaDeAviones");
        for (int i = 0; i < jsonFlota.length(); i++) {
            JSONObject jsonAvion = jsonFlota.getJSONObject(i);

            this.flotaDeAviones.add(new Avion(jsonAvion));
        }
    }

    /**
     * Convierte el objeto Aerolinea a formato JSON.
     * Ahora también guarda el atributo 'activa'.
     */
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("codigo", this.codigo);
        jsonObject.put("nombre", this.nombre);
        jsonObject.put("costoCarryOn", this.costoCarryOn);
        jsonObject.put("costoEquipajeDespachado", this.costoEquipajeDespachado);
        jsonObject.put("costoEquipajeEspecial", this.costoEquipajeEspecial);

        /**
         * Guarda el estado 'activa' en el JSON.
         */
        jsonObject.put("activa", this.activa);

        // Serializar la lista de aviones
        JSONArray jsonFlota = new JSONArray();
        for (Avion avion : this.flotaDeAviones) {

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
        return new ArrayList<>(this.flotaDeAviones);
    }

    // --- Getters y Setters ---

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

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
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
        return nombre + " (" + codigo + ") - " + (activa ? "Activa" : "Inactiva");
    }
}
