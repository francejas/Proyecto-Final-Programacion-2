package Entidades;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.Objects;

public class Pasajero {
    private String nombreCompleto;
    private String DNI;
    private LocalDate fechaNacimiento;

    /**
     * Constructor principal.
     */
    public Pasajero(String nombreCompleto, String DNI, LocalDate fechaNacimiento) {
        this.nombreCompleto = nombreCompleto;
        this.DNI = DNI;
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Constructor para DESERIALIZAR desde JSON.
     */

    public Pasajero(JSONObject json) throws JSONException {
        this.nombreCompleto = json.getString("nombreCompleto");
        this.DNI = json.getString("DNI");
        this.fechaNacimiento = LocalDate.parse(json.getString("fechaNacimiento"));
    }

    /**
     * Convierte el objeto Pasajero a formato JSON.
     */
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nombreCompleto", this.nombreCompleto);
        jsonObject.put("DNI", this.DNI);
        jsonObject.put("fechaNacimiento", this.fechaNacimiento.toString());
        return jsonObject;
    }

    // --- Getters y Setters ---

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    // --- equals() y hashCode() (basados solo en DNI) ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pasajero pasajero = (Pasajero) o;
        return Objects.equals(DNI, pasajero.DNI);
    }

    @Override
    public int hashCode() {
        return Objects.hash(DNI);
    }

    @Override
    public String toString() {
        return "Pasajero{" +
                "nombreCompleto='" + nombreCompleto + '\'' +
                ", DNI='" + DNI + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                '}';
    }
}