package Entidades;
import Enum.EstadoReserva;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Reserva {

    private String idReserva;
    private Cliente cliente;
    private List<Itinerario> itinerarios;
    private List<Pasaje> pasajes;
    private LocalDate fechaCreacion;
    private EstadoReserva estado;
    private double costoTotal;
    private boolean activa;

    public Reserva(Cliente cliente, List<Itinerario> itinerarios, List<Pasaje> pasajes) {
        this.idReserva = "RES-" + UUID.randomUUID().toString();
        this.cliente = cliente;
        this.itinerarios = itinerarios;
        this.pasajes = pasajes;
        this.fechaCreacion = LocalDate.now();
        this.estado = EstadoReserva.CONFIRMADA;
        this.activa = true;
        // Calcula el costo total al momento de crear la reserva
        this.calcularCostoTotal();
    }


    public void calcularCostoTotal() {
        double total = 0.0;
        if (this.pasajes != null) {
            for (Pasaje pasaje : this.pasajes) {
                total += pasaje.calcularCostoFinal();
            }
        }
        this.costoTotal = total;
    }


    public Pasaje buscarPasaje(String idPasaje) {
        if (this.pasajes == null) {
            return null;
        }
        for (Pasaje pasaje : this.pasajes) {
            if (pasaje.getIdPasaje().equals(idPasaje)) {
                return pasaje;
            }
        }
        return null;
    }


    public String getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(String idReserva) {
        this.idReserva = idReserva;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Itinerario> getItinerarios() {
        return itinerarios;
    }

    public void setItinerarios(List<Itinerario> itinerarios) {
        this.itinerarios = itinerarios;
    }

    public List<Pasaje> getPasajes() {
        return pasajes;
    }

    public void setPasajes(List<Pasaje> pasajes) {
        this.pasajes = pasajes;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    public double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserva reserva = (Reserva) o;
        return Objects.equals(idReserva, reserva.idReserva);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idReserva);
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "idReserva='" + idReserva + '\'' +
                ", cliente=" + (cliente != null ? cliente.getNombre() : "N/A") +
                ", itinerarios=" + (itinerarios != null ? itinerarios.size() : 0) +
                ", pasajes=" + (pasajes != null ? pasajes.size() : 0) +
                ", costoTotal=" + costoTotal +
                ", estado=" + estado +
                ", activa=" + activa +
                '}';
    }
}