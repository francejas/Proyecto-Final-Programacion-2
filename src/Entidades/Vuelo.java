package Entidades;

import java.util.UUID;

public class Vuelo {
    private final String idVuelo;
    private Provincias origen;
    private Provincias destino;
    private ClaseVuelo claseVuelo;
    private Avion avion;
    private Aerolineas aerolineas;

    public Vuelo(Provincias origen, Provincias destino, ClaseVuelo claseVuelo, Avion avion, Aerolineas aerolineas) {
        this.idVuelo = UUID.randomUUID().toString();
        this.origen = origen;
        this.destino = destino;
        this.claseVuelo = claseVuelo;
        this.avion = avion;
        this.aerolineas = aerolineas;
    }

    public String getIdVuelo() {
            return idVuelo;
        }

    public Provincias getOrigen() {
            return origen;
        }

        public void setOrigen(Provincias origen) {
            this.origen = origen;
        }

        public Provincias getDestino() {
            return destino;
        }

        public void setDestino(Provincias destino) {
            this.destino = destino;
        }

        public ClaseVuelo getClaseVuelo() {
            return claseVuelo;
        }

        public void setClaseVuelo(ClaseVuelo claseVuelo) {
            this.claseVuelo = claseVuelo;
        }

        public Avion getAvion() {
            return avion;
        }

        public void setAvion(Avion avion) {
            this.avion = avion;
        }

        public Aerolineas getAerolineas() {
            return aerolineas;
        }

        public void setAerolineas(Aerolineas aerolineas) {
            this.aerolineas = aerolineas;
        }

        @Override
        public String toString() {
            return "Vuelo{" +
                    "idVuelo='" + idVuelo + '\'' +
                    ", origen=" + origen +
                    ", destino=" + destino +
                    ", claseVuelo=" + claseVuelo +
                    ", avion=" + avion +
                    ", aerolineas=" + aerolineas +
                    '}';
    }
}
