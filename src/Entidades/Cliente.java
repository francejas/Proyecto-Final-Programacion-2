package Entidades;

import Enum.RolUsuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario {
    private int millas;
    private String DNI;
    private LocalDate fechaDeNacimiento;
    private List<Reserva> historialDeReservas;

    public Cliente(String nombre, String email, String password, boolean activo,
                   int millas, String DNI, LocalDate fechaDeNacimiento) {

        super(nombre, email, password, RolUsuario.CLIENTE, activo);
        this.millas = millas;
        this.DNI = DNI;
        this.fechaDeNacimiento = fechaDeNacimiento;
        this.historialDeReservas = new ArrayList<>();
    }

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