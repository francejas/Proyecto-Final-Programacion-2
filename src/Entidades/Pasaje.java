package Entidades;

import Enum.TipoClase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Pasaje {

    private String idPasaje;
    private Pasajero pasajero; // La persona que viaja
    private Vuelo vuelo;       // El segmento de vuelo (tramo)
    private String asiento;   // El asiento asignado (ej: "24A")
    private TipoClase clase; // ECONOMY o BUSINESS
    private List<Equipaje> equipajeContratado;

    public Pasaje(Pasajero pasajero, Vuelo vuelo, String asiento, TipoClase clase) {
        this.idPasaje = "PAS-" + UUID.randomUUID().toString();
        this.pasajero = pasajero;
        this.vuelo = vuelo;
        this.asiento = asiento;
        this.clase = clase;
        // Se inicializa la lista vacía. El equipaje se agrega después.
        this.equipajeContratado = new ArrayList<>();
    }

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
                // Se le pasa el VUELO, no la AEROLINEA
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