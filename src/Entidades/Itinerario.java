package Entidades;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Itinerario {
    private String idItinerario;
    private List<Vuelo> segmentos;

    public Itinerario(List<Vuelo> segmentos) {
        if (segmentos == null || segmentos.isEmpty()) {
            throw new IllegalArgumentException("Un itinerario no puede crearse sin al menos un segmento de vuelo.");
        }
        this.idItinerario = "ITN-" + UUID.randomUUID().toString();
        this.segmentos = segmentos;
    }

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

        // Duration.between calcula el tiempo exacto entre dos momentos
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Itinerario that = (Itinerario) o;
        return Objects.equals(idItinerario, that.idItinerario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idItinerario);
    }

    @Override
    public String toString() {
        String origen = getOrigenFinal().getCodigoIATA();
        String destino = getDestinoFinal().getCodigoIATA();
        int escalas = getCantidadEscalas();

        return "Itinerario " + idItinerario + " [" +
                origen + " -> " + destino +
                " | Escalas: " + escalas + "]";
    }
}