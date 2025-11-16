package Entidades;

import Enum.RolUsuario;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario {
    private int millas;
    private String DNI;
    private LocalDate fechaDeNacimiento;
    private List<Reserva> historialDeReservas; // No se serializa


    public Cliente(String nombre, String email, String password, boolean activo,
                   int millas, String DNI, LocalDate fechaDeNacimiento) {

        super(nombre, email, password, RolUsuario.CLIENTE, activo);
        this.millas = millas;
        this.DNI = DNI;
        this.fechaDeNacimiento = fechaDeNacimiento;
        this.historialDeReservas = new ArrayList<>();
    }

    /**
     * Constructor para DESERIALIZAR desde JSON.
     */
    public Cliente(JSONObject json) throws JSONException {
        super(json); // Llama al constructor JSON del padre
        this.millas = json.getInt("millas");
        this.DNI = json.getString("DNI");
        this.fechaDeNacimiento = LocalDate.parse(json.getString("fechaDeNacimiento"));
        this.historialDeReservas = new ArrayList<>(); // Se inicializa vacío (se carga en runtime)
    }

    /**
     * Convierte el objeto Cliente a formato JSON.
     * El historialDeReservas no se incluye para evitar referencias circulares.
     */
    @Override
    public JSONObject toJSON() throws JSONException {
        // 1. Obtiene el JSON del padre (id, nombre, email, etc.)
        JSONObject jsonObject = super.toJSON();

        // 2. Agrega los atributos propios de Cliente
        jsonObject.put("millas", this.millas);
        jsonObject.put("DNI", this.DNI);
        jsonObject.put("fechaDeNacimiento", this.fechaDeNacimiento.toString());

        // NOTA: historialDeReservas no se serializa aquí.

        return jsonObject;
    }

    // --- Getters y Setters ---

    public int getMillas() {
        return millas;
    }

    public void setMillas(int millas) {
        this.millas = millas;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public LocalDate getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(LocalDate fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public List<Reserva> getHistorialDeReservas() {
        return historialDeReservas;
    }

    public void setHistorialDeReservas(List<Reserva> historialDeReservas) {
        this.historialDeReservas = historialDeReservas;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "usuario_info=" + super.toString() +
                ", millas=" + millas +
                ", DNI='" + DNI + '\'' +
                ", fechaDeNacimiento=" + fechaDeNacimiento +
                '}';
    }
}