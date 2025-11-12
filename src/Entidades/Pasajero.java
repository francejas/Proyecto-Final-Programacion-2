package Entidades;

import java.time.LocalDate;
import java.util.Objects;

public class Pasajero {
    private String nombreCompleto;
    private String DNI;
    private LocalDate fechaNacimiento;

    public Pasajero(String nombreCompleto, String DNI, LocalDate fechaNacimiento) {
        this.nombreCompleto = nombreCompleto;
        this.DNI = DNI;
        this.fechaNacimiento = fechaNacimiento;
    }

    public Pasajero(){}

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

    // --- equals() y hashCode() basados solo en DNI ---

    /**
     * Compara dos objetos Pasajero basándose únicamente en su DNI.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pasajero pasajero = (Pasajero) o;
        // La identidad se define SOLO por el DNI
        return Objects.equals(DNI, pasajero.DNI);
    }

    /**
     * Genera un hashCode basado únicamente en el DNI.
     */
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