package Entidades;


import Enum.EstadoReserva;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Reserva {

    private String idReserva;
    private Cliente cliente;
    private List<Itinerario> itinerarios;
    private List<Pasaje> pasajes;
    private LocalDate fechaCreacion;
    private EstadoReserva estado;
    private double costoTotal;
    private boolean activa;

    // Atributo temporal para la deserialización
    private int clienteIdTemporal;

    /**
     * Constructor para una nueva Reserva.
     */
    public Reserva(Cliente cliente, List<Itinerario> itinerarios, List<Pasaje> pasajes) {
        this.idReserva = "RES-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        this.cliente = cliente;
        this.itinerarios = itinerarios;
        this.pasajes = pasajes;
        this.fechaCreacion = LocalDate.now();
        this.estado = EstadoReserva.CONFIRMADA;
        this.activa = true;
        // Calcula el costo total al momento de crear la reserva
        this.calcularCostoTotal();
    }

    /**
     * Constructor para DESERIALIZAR desde JSON.
     * Lee los IDs y los objetos/listas anidados.
     */
    public Reserva(JSONObject json) throws JSONException {
        this.idReserva = json.getString("idReserva");

        // Guarda solo el ID del cliente. El GestorReservas será responsable de
        // usar este ID para buscar y setear el objeto Cliente completo.
        this.clienteIdTemporal = json.getInt("clienteId");
        this.cliente = null; // Se setea después por el Gestor

        this.fechaCreacion = LocalDate.parse(json.getString("fechaCreacion"));
        this.estado = EstadoReserva.valueOf(json.getString("estado"));
        this.costoTotal = json.getDouble("costoTotal");
        this.activa = json.getBoolean("activa");

        // Deserializar la lista de Itinerarios
        this.itinerarios = new ArrayList<>();
        JSONArray jsonItinerarios = json.getJSONArray("itinerarios");
        for (int i = 0; i < jsonItinerarios.length(); i++) {
            // Asume que Itinerario.java tiene un constructor (JSONObject)
            this.itinerarios.add(new Itinerario(jsonItinerarios.getJSONObject(i)));
        }

        // Deserializar la lista de Pasajes
        this.pasajes = new ArrayList<>();
        JSONArray jsonPasajes = json.getJSONArray("pasajes");
        for (int i = 0; i < jsonPasajes.length(); i++) {
            // Asume que Pasaje.java tiene un constructor (JSONObject)
            this.pasajes.add(new Pasaje(jsonPasajes.getJSONObject(i)));
        }
    }

    /**
     * Convierte el objeto Reserva a formato JSON.
     * Serializa recursivamente sus listas.
     */
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("idReserva", this.idReserva);

        // Para evitar referencias circulares (Cliente -> Reserva -> Cliente),
        // guardamos solo el ID del cliente.
        if (this.cliente != null) {
            // Asume que Cliente.java tiene un metodo getId() que devuelve int
            jsonObject.put("clienteId", this.cliente.getId());
        }

        jsonObject.put("fechaCreacion", this.fechaCreacion.toString());
        jsonObject.put("estado", this.estado.toString());
        jsonObject.put("costoTotal", this.costoTotal);
        jsonObject.put("activa", this.activa);

        // Serializar lista de Itinerarios
        JSONArray jsonItinerarios = new JSONArray();
        for (Itinerario itinerario : this.itinerarios) {
            // Asume que Itinerario.java tiene un metodo toJSON()
            jsonItinerarios.put(itinerario.toJSON());
        }
        jsonObject.put("itinerarios", jsonItinerarios);

        // Serializar lista de Pasajes
        JSONArray jsonPasajes = new JSONArray();
        for (Pasaje pasaje : this.pasajes) {
            // Asume que Pasaje.java tiene un método toJSON()
            jsonPasajes.put(pasaje.toJSON());
        }
        jsonObject.put("pasajes", jsonPasajes);

        return jsonObject;
    }

    // --- Métodos de Lógica ---

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

    // --- Getters y Setters ---

    // Getter especial para el ID temporal
    public int getClienteIdTemporal() {
        return clienteIdTemporal;
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

    // --- equals(), hashCode() y toString() ---

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
                // Muestra el nombre o el ID temporal si el cliente no está cargado
                ", cliente=" + (cliente != null ? cliente.getNombre() : "ID: " + clienteIdTemporal) +
                ", itinerarios=" + (itinerarios != null ? itinerarios.size() : 0) +
                ", pasajes=" + (pasajes != null ? pasajes.size() : 0) +
                ", costoTotal=" + costoTotal +
                ", estado=" + estado +
                ", activa=" + activa +
                '}';
    }
}