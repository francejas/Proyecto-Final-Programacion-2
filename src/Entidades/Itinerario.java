package Entidades;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Itinerario {

    private String idItinerario;
    private List<Vuelo> segmentos;


    public Itinerario(List<Vuelo> segmentos) {
        if (segmentos == null || segmentos.isEmpty()) {
            throw new IllegalArgumentException("La lista de segmentos no puede ser nula ni vacía.");
        }
        this.idItinerario = "ITN-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        this.segmentos = segmentos;
    }

    // --- Getters y Setters ---

    public String getIdItinerario() {
        return idItinerario;
    }

    public void setIdItinerario(String idItinerario) {
        this.idItinerario = idItinerario;
    }


    public List<Vuelo> getSegmentos() {
        return new ArrayList<>(this.segmentos);
    }

    public void setSegmentos(List<Vuelo> segmentos) {
        this.segmentos = segmentos;
    }

    public Aeropuerto getOrigenFinal() {
        return this.segmentos.get(0).getOrigen();
    }


    public Aeropuerto getDestinoFinal() {
        return this.segmentos.get(this.segmentos.size() - 1).getDestino();
    }


    public Duration getDuracionTotal() {
        LocalDateTime inicioViaje = this.segmentos.get(0).getFechaHoraSalida();
        LocalDateTime finViaje = this.segmentos.get(this.segmentos.size() - 1).getFechaHoraLlegada();
        return Duration.between(inicioViaje, finViaje);
    }


    public double getPrecioTotal() {
        double total = 0.0;
        for (Vuelo vuelo : this.segmentos) {
            total += vuelo.getPrecioBase();
        }
        return total;
    }

    public int getCantidadEscalas() {
        return this.segmentos.size() - 1;
    }
}