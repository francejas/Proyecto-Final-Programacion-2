package Entidades;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Reserva {

    private String idReserva;
    private Cliente cliente;
    private Itinerario itinerario;
    private List<Pasaje> pasajes;
    private LocalDate fechaCreacion;
    private EstadoReserva estado;
    private double costoTotal;
    private boolean activa;


    public Reserva(Cliente cliente, Itinerario itinerario, List<Pasaje> pasajes) {
        this.idReserva = "RES-" + UUID.randomUUID().toString().substring(0, 6);
        this.cliente = cliente;
        this.itinerario = itinerario;
        this.pasajes = pasajes;
        this.fechaCreacion = LocalDate.now();
        this.estado = EstadoReserva.CONFIRMADA;
        this.activa = true;
        calcularCostoTotal();
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

    public Itinerario getItinerario() {
        return itinerario;
    }

    public void setItinerario(Itinerario itinerario) {
        this.itinerario = itinerario;
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


    public void calcularCostoTotal() {
        double total = 0.0;
        if (this.pasajes != null) {
            for (Pasaje pasaje : this.pasajes) {
                total += pasaje.calcularCostoFinal();
            }
        }
        this.costoTotal = total;
    }
}