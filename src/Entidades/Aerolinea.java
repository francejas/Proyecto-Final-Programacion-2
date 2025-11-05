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
        this.flotaDeAviones = new ArrayList<>();
        this.costoCarryOn = costoCarryOn;
        this.costoEquipajeDespachado = costoEquipajeDespachado;
        this.costoEquipajeEspecial = costoEquipajeEspecial;
    }

    public void agregarAvionAlaFlota(Avion avion) {
        this.flotaDeAviones.add(avion);
    }

    public List<Avion> getFlotaDeAviones() {
        return new ArrayList<>(this.flotaDeAviones);
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
}
