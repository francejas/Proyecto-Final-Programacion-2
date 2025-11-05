package Entidades;
import java.util.Objects;

public class Avion {
    private String matricula;
    private String modelo;
    private int capacidadEconomy;
    private int capacidadBusiness;

    public Avion(String matricula, String modelo, int capacidadEconomy, int capacidadBusiness) {
        this.matricula = matricula;
        this.modelo = modelo;
        this.capacidadEconomy = capacidadEconomy;
        this.capacidadBusiness = capacidadBusiness;
    }

    public int getCapacidadTotal() {
        return this.capacidadEconomy + this.capacidadBusiness;
    }


    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getCapacidadEconomy() {
        return capacidadEconomy;
    }

    public void setCapacidadEconomy(int capacidadEconomy) {
        this.capacidadEconomy = capacidadEconomy;
    }

    public int getCapacidadBusiness() {
        return capacidadBusiness;
    }

    public void setCapacidadBusiness(int capacidadBusiness) {
        this.capacidadBusiness = capacidadBusiness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Avion avion = (Avion) o;
        return Objects.equals(matricula, avion.matricula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matricula);
    }

    @Override
    public String toString() {
        return modelo + " (" + matricula + ")";
    }
}