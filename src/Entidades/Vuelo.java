package Entidades;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Vuelo {

    private String idVuelo;
    private Aeropuerto origen;
    private Aeropuerto destino;
    private LocalDateTime fechaHoraSalida;
    private LocalDateTime fechaHoraLlegada;
    private Aerolinea aerolinea;
    private Avion avion;
    private Map<String, Boolean> asientosDisponibles;
    private double precioBase;
    private boolean activo;
    private boolean tieneServicioDeComida;
    private boolean carryOnGratis;

    public Vuelo(Aeropuerto origen, Aeropuerto destino, LocalDateTime fechaHoraSalida,
                 LocalDateTime fechaHoraLlegada, Aerolinea aerolinea, Avion avion,
                 double precioBase, boolean tieneServicioDeComida, boolean carryOnGratis) {

        this.idVuelo = "VL-" + UUID.randomUUID().toString();
        this.origen = origen;
        this.destino = destino;
        this.fechaHoraSalida = fechaHoraSalida;
        this.fechaHoraLlegada = fechaHoraLlegada;
        this.aerolinea = aerolinea;
        this.avion = avion;
        this.precioBase = precioBase;
        this.tieneServicioDeComida = tieneServicioDeComida;
        this.carryOnGratis = carryOnGratis;
        this.activo = true;
        // Inicializa el mapa de asientos automáticamente
        this.asientosDisponibles = new HashMap<>();
        inicializarMapaDeAsientos();
    }

    private void inicializarMapaDeAsientos() {
        if (this.avion == null) return;

        char[] asientosPorFila = {'A', 'B', 'C', 'D', 'E', 'F'}; // Configuración 3-3
        int filaActual = 1;

        // Generar asientos Business (Filas 1 a N)
        // Asume que la capacidad es un múltiplo de 6
        int filasBusiness = this.avion.getCapacidadBusiness() / 6;
        for (int fila = 1; fila <= filasBusiness; fila++) {
            for (char asiento : asientosPorFila) {
                this.asientosDisponibles.put(fila + "" + asiento, true); // true = libre
            }
            filaActual++;
        }

        // Generar asientos Economy (Filas N+1 a M)
        int filasEconomy = this.avion.getCapacidadEconomy() / 6;
        int filaFinalEconomy = filaActual + filasEconomy;
        for (int fila = filaActual; fila < filaFinalEconomy; fila++) {
            for (char asiento : asientosPorFila) {
                this.asientosDisponibles.put(fila + "" + asiento, true);
            }
        }
    }

    public boolean isAsientoLibre(String codigoAsiento) {
        // .getOrDefault() previene un NullPointerException si el asiento no existe
        // y devuelve 'false' (no libre) si la clave no se encuentra.
        return this.asientosDisponibles.getOrDefault(codigoAsiento, false);
    }

    public boolean ocuparAsiento(String codigoAsiento) {
        if (isAsientoLibre(codigoAsiento)) {
            this.asientosDisponibles.put(codigoAsiento, false);
            return true;
        }
        return false;
    }

    public void liberarAsiento(String codigoAsiento) {
        // Solo intenta liberar si el asiento existe en el mapa
        if (this.asientosDisponibles.containsKey(codigoAsiento)) {
            this.asientosDisponibles.put(codigoAsiento, true); // true = libre
        }
    }

    public String getIdVuelo() {
        return idVuelo;
    }

    public void setIdVuelo(String idVuelo) {
        this.idVuelo = idVuelo;
    }

    public Aeropuerto getOrigen() {
        return origen;
    }

    public void setOrigen(Aeropuerto origen) {
        this.origen = origen;
    }

    public Aeropuerto getDestino() {
        return destino;
    }

    public void setDestino(Aeropuerto destino) {
        this.destino = destino;
    }

    public LocalDateTime getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public void setFechaHoraSalida(LocalDateTime fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    public LocalDateTime getFechaHoraLlegada() {
        return fechaHoraLlegada;
    }

    public void setFechaHoraLlegada(LocalDateTime fechaHoraLlegada) {
        this.fechaHoraLlegada = fechaHoraLlegada;
    }

    public Aerolinea getAerolinea() {
        return aerolinea;
    }

    public void setAerolinea(Aerolinea aerolinea) {
        this.aerolinea = aerolinea;
    }

    public Avion getAvion() {
        return avion;
    }

    public void setAvion(Avion avion) {
        this.avion = avion;
    }

    public Map<String, Boolean> getAsientosDisponibles() {
        return asientosDisponibles;
    }

    public void setAsientosDisponibles(Map<String, Boolean> asientosDisponibles) {
        this.asientosDisponibles = asientosDisponibles;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public boolean isTieneServicioDeComida() {
        return tieneServicioDeComida;
    }

    public void setTieneServicioDeComida(boolean tieneServicioDeComida) {
        this.tieneServicioDeComida = tieneServicioDeComida;
    }

    public boolean isCarryOnGratis() {
        return carryOnGratis;
    }

    public void setCarryOnGratis(boolean carryOnGratis) {
        this.carryOnGratis = carryOnGratis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vuelo vuelo = (Vuelo) o;
        return Objects.equals(idVuelo, vuelo.idVuelo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVuelo);
    }

    @Override
    public String toString() {
        return "Vuelo " + idVuelo + " [" +
                (aerolinea != null ? aerolinea.getCodigo() : "N/A") + "] " +
                (origen != null ? origen.getCodigoIATA() : "N/A") + " -> " +
                (destino != null ? destino.getCodigoIATA() : "N/A") +
                " (" + fechaHoraSalida + ")";
    }
}