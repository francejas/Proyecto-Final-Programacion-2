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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pasajero pasajero)) return false;
        return Objects.equals(nombreCompleto, pasajero.nombreCompleto) && Objects.equals(DNI, pasajero.DNI) && Objects.equals(fechaNacimiento, pasajero.fechaNacimiento);
    }
    @Override
    public int hashCode() {
        return Objects.hash(nombreCompleto, DNI, fechaNacimiento);
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
