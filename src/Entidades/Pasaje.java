package Entidades;

import Enum.TipoClase;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Pasaje {

    private String idPasaje;
    private Pasajero pasajero; // La persona que viaja
    private Vuelo vuelo;       // El segmento de vuelo (tramo)
    private String asiento;   // El asiento asignado (ej: "24A")
    private TipoClase clase; // ECONOMY o BUSINESS
    private List<Equipaje> equipajeContratado;

    /**
     * Constructor principal.
     */
    public Pasaje(Pasajero pasajero, Vuelo vuelo, String asiento, TipoClase clase) {
        this.idPasaje = "PAS-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        this.pasajero = pasajero;
        this.vuelo = vuelo;
        this.asiento = asiento;
        this.clase = clase;
        this.equipajeContratado = new ArrayList<>();
    }

    /**
     * Constructor para DESERIALIZAR desde JSON.
     * Maneja la deserialización polimórfica de Equipaje.
     */
    public Pasaje(JSONObject json) throws JSONException {
        this.idPasaje = json.getString("idPasaje");
        this.pasajero = new Pasajero(json.getJSONObject("pasajero"));
        this.vuelo = new Vuelo(json.getJSONObject("vuelo"));
        this.asiento = json.getString("asiento");
        this.clase = TipoClase.valueOf(json.getString("clase"));

        // --- CORRECCIÓN AQUÍ ---
        // Deserializar la lista polimórfica de Equipaje
        this.equipajeContratado = new ArrayList<>();
        JSONArray jsonEquipajes = json.getJSONArray("equipajeContratado");

        for (int i = 0; i < jsonEquipajes.length(); i++) {
            JSONObject jsonEqui = jsonEquipajes.getJSONObject(i);
            // Lee el campo "tipoEquipaje" (guardado por Equipaje.toJSON())
            String tipo = jsonEqui.getString("tipoEquipaje");

            // Switch para instanciar la clase correcta
            switch (tipo) {
                case "EquipajeDeMano":
                    this.equipajeContratado.add(new EquipajeDeMano(jsonEqui));
                    break;
                case "CarryOn":
                    this.equipajeContratado.add(new CarryOn(jsonEqui));
                    break;
                case "EquipajeDespachado":
                    this.equipajeContratado.add(new EquipajeDespachado(jsonEqui));
                    break;
                case "EquipajeEspecial":
                    this.equipajeContratado.add(new EquipajeEspecial(jsonEqui));
                    break;
                default:
                    // Manejar un tipo desconocido si es necesario
                    System.err.println("Tipo de equipaje desconocido: " + tipo);
                    break;
            }
        }
    }

    /**
     * Convierte el objeto Pasaje a formato JSON.
     * Maneja la serialización de Equipaje.
     */
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("idPasaje", this.idPasaje);
        jsonObject.put("pasajero", this.pasajero.toJSON());
        jsonObject.put("vuelo", this.vuelo.toJSON());
        jsonObject.put("asiento", this.asiento);
        jsonObject.put("clase", this.clase.toString());

        // Serializar la lista de Equipaje
        JSONArray jsonEquipajes = new JSONArray();
        for (Equipaje equipaje : this.equipajeContratado) {
            // Llama al .toJSON() de la clase hija (CarryOn, etc.)
            jsonEquipajes.put(equipaje.toJSON());
        }
        jsonObject.put("equipajeContratado", jsonEquipajes);

        return jsonObject;
    }

    // --- Métodos de Lógica ---

    public double calcularCostoFinal() {
        if (this.vuelo == null) {
            return 0.0;
        }

        // 2. Calcular el costo del asiento (base + clase)
        double costoAsiento = this.vuelo.getPrecioBase();
        if (this.clase == TipoClase.BUSINESS) {
            //1.8x para Business
            costoAsiento *= 1.8;
        }

        // 3. Calcular el costo del equipaje
        double costoEquipaje = 0.0;
        if (this.equipajeContratado != null) {
            for (Equipaje equipaje : this.equipajeContratado) {
                // Se le pasa el VUELO
                costoEquipaje += equipaje.calcularCosto(this.vuelo);
            }
        }

        // 4. Devolver el total
        return costoAsiento + costoEquipaje;
    }

    public void agregarEquipaje(Equipaje equipaje) {
        if (this.equipajeContratado == null) {
            this.equipajeContratado = new ArrayList<>();
        }
        this.equipajeContratado.add(equipaje);
    }

    // --- Getters y Setters ---

    public String getIdPasaje() {
        return idPasaje;
    }

    public void setIdPasaje(String idPasaje) {
        this.idPasaje = idPasaje;
    }

    public Pasajero getPasajero() {
        return pasajero;
    }

    public void setPasajero(Pasajero pasajero) {
        this.pasajero = pasajero;
    }

    public Vuelo getVuelo() {
        return vuelo;
    }

    public void setVuelo(Vuelo vuelo) {
        this.vuelo = vuelo;
    }

    public String getAsiento() {
        return asiento;
    }

    public void setAsiento(String asiento) {
        this.asiento = asiento;
    }

    public TipoClase getClase() {
        return clase;
    }

    public void setClase(TipoClase clase) {
        this.clase = clase;
    }

    public List<Equipaje> getEquipajeContratado() {
        return equipajeContratado;
    }

    public void setEquipajeContratado(List<Equipaje> equipajeContratado) {
        this.equipajeContratado = equipajeContratado;
    }

    // --- equals(), hashCode() y toString() ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pasaje pasaje = (Pasaje) o;
        return Objects.equals(idPasaje, pasaje.idPasaje);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPasaje);
    }

    @Override
    public String toString() {
        return "Pasaje " + idPasaje + " [" +
                (pasajero != null ? pasajero.getNombreCompleto() : "N/A") +
                " | Vuelo: " + (vuelo != null ? vuelo.getIdVuelo() : "N/A") +
                " | Asiento: " + asiento +
                " | Clase: " + clase +
                "]";
    }
}