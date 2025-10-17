package Entidades;

public class Avion {
    private static int idCount = 0;
    private final int idAvion;
    private int cantLugaresFirstClass;
    private int cantLugaresBusiness;
    private int cantLugaresPremiumEconomy;
    private int cantLugaresEconomy;
    private boolean disponibilidadAvion;

    public Avion(int cantLugaresFirstClass, int cantLugaresBusiness, int cantLugaresPremiumEconomy, int cantLugaresEconomy, boolean disponibilidadAvion) {
        idCount++;
        this.idAvion = idCount;
        this.cantLugaresFirstClass = cantLugaresFirstClass;
        this.cantLugaresBusiness = cantLugaresBusiness;
        this.cantLugaresPremiumEconomy = cantLugaresPremiumEconomy;
        this.cantLugaresEconomy = cantLugaresEconomy;
        this.disponibilidadAvion = disponibilidadAvion;
    }
    public int getIdAvion() {
        return idAvion;
    }

    public int getCantLugaresFirstClass() {
        return cantLugaresFirstClass;
    }

    public void setCantLugaresFirstClass(int cantLugaresFirstClass) {
        this.cantLugaresFirstClass = cantLugaresFirstClass;
    }

    public int getCantLugaresBusiness() {
        return cantLugaresBusiness;
    }

    public void setCantLugaresBusiness(int cantLugaresBusiness) {
        this.cantLugaresBusiness = cantLugaresBusiness;
    }

    public int getCantLugaresPremiumEconomy() {
        return cantLugaresPremiumEconomy;
    }

    public void setCantLugaresPremiumEconomy(int cantLugaresPremiumEconomy) {
        this.cantLugaresPremiumEconomy = cantLugaresPremiumEconomy;
    }

    public int getCantLugaresEconomy() {
        return cantLugaresEconomy;
    }

    public void setCantLugaresEconomy(int cantLugaresEconomy) {
        this.cantLugaresEconomy = cantLugaresEconomy;
    }

    public boolean isDisponibilidadAvion() {
        return disponibilidadAvion;
    }

    public void setDisponibilidadAvion(boolean disponibilidadAvion) {
        this.disponibilidadAvion = disponibilidadAvion;
    }

    public boolean verificarDisponibilidad(ClaseVuelo clase){
        return switch (clase){
            case FIRTS_CLASS -> cantLugaresFirstClass > 0;
            case BUSINESS -> cantLugaresBusiness > 0;
            case PREMIUM_ECONOMY -> cantLugaresPremiumEconomy > 0;
            case ECONOMY -> cantLugaresEconomy > 0;
        };
    }

    public boolean reservarLugar(ClaseVuelo clase){
        if(!verificarDisponibilidad(clase)){
            System.out.println("No hay más lugares disponibles en la clase " + clase);
            return false;
        }

        switch(clase){
            case FIRTS_CLASS -> cantLugaresFirstClass--;
            case BUSINESS -> cantLugaresBusiness--;
            case PREMIUM_ECONOMY -> cantLugaresPremiumEconomy--;
            case ECONOMY -> cantLugaresEconomy--;
        }
        System.out.println("Lugar reservado en la clase " + clase);
        return true;
    }

    public enum ClaseVuelo {
        FIRTS_CLASS,
        BUSINESS,
        PREMIUM_ECONOMY,
        ECONOMY
    }
}
