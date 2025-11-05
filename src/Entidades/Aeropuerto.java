package Entidades;

import java.util.Objects;

public class Aeropuerto {
    private String codigoIATA;
    private String nombre;
    private String ciudad;

    public Aeropuerto(String codigoIATA, String nombre, String ciudad) {
        // Guardar el código en mayúsculas
        this.codigoIATA = (codigoIATA != null) ? codigoIATA.toUpperCase() : null;
        this.nombre = nombre;
        this.ciudad = ciudad;
    }

    public String getCodigoIATA() {
        return codigoIATA;
    }

    public void setCodigoIATA(String codigoIATA) {
        this.codigoIATA = (codigoIATA != null) ? codigoIATA.toUpperCase() : null;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aeropuerto that = (Aeropuerto) o;
        return Objects.equals(codigoIATA, that.codigoIATA);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoIATA);
    }

    @Override
    public String toString() {
        return nombre + " (" + ciudad + ") - [" + codigoIATA + "]";
    }
}