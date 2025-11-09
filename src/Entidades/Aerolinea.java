package Entidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Aerolinea {
    private String codigo;
    private String nombre;
    private List<Avion> flotaDeAviones;
    private double costoCarryOn;
    private double costoEquipajeDespachado;
    private double costoEquipajeEspecial;

    public Aerolinea(String codigo, String nombre, double costoCarryOn,
                     double costoEquipajeDespachado, double costoEquipajeEspecial) {

        this.codigo = codigo;
        this.nombre = nombre;
        this.costoCarryOn = costoCarryOn;
        this.costoEquipajeDespachado = costoEquipajeDespachado;
        this.costoEquipajeEspecial = costoEquipajeEspecial;
        // La flota se inicializa vacía. Los aviones se deben agregar
        // por separado a través del menú del administrador.
        this.flotaDeAviones = new ArrayList<>();
    }

    public void agregarAvionAlaFlota(Avion avion) {
        this.flotaDeAviones.add(avion);
    }

    public List<Avion> getFlotaDeAviones() {
        // Devuelve una copia para que la lista original no pueda ser modificada desde fuera.
        return new ArrayList<>(this.flotaDeAviones);
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFlotaDeAviones(List<Avion> flotaDeAviones) {
        this.flotaDeAviones = flotaDeAviones;
    }

    public double getCostoCarryOn() {
        return costoCarryOn;
    }

    public void setCostoCarryOn(double costoCarryOn) {
        this.costoCarryOn = costoCarryOn;
    }

    public double getCostoEquipajeDespachado() {
        return costoEquipajeDespachado;
    }

    public void setCostoEquipajeDespachado(double costoEquipajeDespachado) {
        this.costoEquipajeDespachado = costoEquipajeDespachado;
    }

    public double getCostoEquipajeEspecial() {
        return costoEquipajeEspecial;
    }

    public void setCostoEquipajeEspecial(double costoEquipajeEspecial) {
        this.costoEquipajeEspecial = costoEquipajeEspecial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aerolinea aerolinea = (Aerolinea) o;
        return Objects.equals(codigo, aerolinea.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return nombre + " (" + codigo + ")";
    }
}
