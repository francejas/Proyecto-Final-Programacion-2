package Servicios;

import Entidades.Aeropuerto;
import Entidades.Itinerario;
import Entidades.Vuelo;
import Excepciones.DatoInvalidoException;
import Excepciones.ItinerarioNoEncontradoException;
import Persistencia.JsonManagerVuelos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GestorVuelos implements Gestionable<Vuelo, String>{
    private List<Vuelo> vuelos;
    private GestorAeropuerto gestorAeropuerto;
    private JsonManagerVuelos jsonManager;

    public GestorVuelos(GestorAeropuerto ga){
        this.jsonManager = new JsonManagerVuelos();
        this.gestorAeropuerto = ga;

        this.vuelos = jsonManager.leerLista();
    }

    private void guardarEnJson(){
        jsonManager.guardarLista(this.vuelos);
    }

    public List<Itinerario> buscarItinerarios(String origen, String destino, LocalDate fecha) throws ItinerarioNoEncontradoException {
        Aeropuerto aOrigen = gestorAeropuerto.consulta(origen);
        Aeropuerto aDestino = gestorAeropuerto.consulta(destino);

        List<Itinerario> resultados = new ArrayList<>();

        // 1) Validacion de aeropuertos
        if(aOrigen == null || aDestino == null){
            return resultados; // Vacio
        }

        // 2) Buscar vuelos directos
        for(Vuelo vuelo: vuelos){
            boolean mismoOrigen = vuelo.getOrigen().getCodigoIATA().equals(origen);
            boolean mismoDestino = vuelo.getDestino().getCodigoIATA().equals(destino);
            boolean mismaFecha = vuelo.getFechaHoraSalida().toLocalDate().equals(fecha);

            if(mismoOrigen && mismoDestino && mismaFecha){
                resultados.add(new Itinerario(List.of(vuelo)));
            }
        }

        // 3) Buscar vuelos con 1 escala
        for(Vuelo v1: vuelos){
            if(!v1.isActivo()) continue;

            if(v1.getOrigen() == null || !v1.getOrigen().getCodigoIATA().equalsIgnoreCase(origen)) continue;
            if(v1.getFechaHoraSalida() == null || !v1.getFechaHoraSalida().toLocalDate().equals(fecha)) continue;

            Aeropuerto aeropuertoEscala = v1.getDestino();
            if(aeropuertoEscala == null) continue;

            for(Vuelo v2: vuelos){
                if(!v2.isActivo()) continue;

                if(v2.getOrigen() == null || !v2.getOrigen().getCodigoIATA().equalsIgnoreCase(aeropuertoEscala.getCodigoIATA())) continue;
                if(v2.getDestino() == null || !v2.getDestino().getCodigoIATA().equalsIgnoreCase(destino)) continue;
                if(v2.getFechaHoraSalida() == null || !v2.getFechaHoraSalida().toLocalDate().equals(fecha)) continue;

                // Validar que conexion sea posible
                if(v1.getFechaHoraLlegada() == null) continue;
                if(v2.getFechaHoraSalida().isBefore(v1.getFechaHoraLlegada())) continue;

                List<Vuelo> segmentos = new ArrayList<>();
                segmentos.add(v1); segmentos.add(v2);
                resultados.add(new Itinerario(segmentos));
            }
        }

        if(resultados.isEmpty()){
            throw new ItinerarioNoEncontradoException("No se encontraron itinerarios.");
        }

        return resultados;
    }

    @Override
    public void alta(Vuelo v){
        this.vuelos.add(v);
        guardarEnJson();
    }

    @Override
    public void baja(String idVuelo){
        Vuelo v = this.consulta(idVuelo);
        if(v == null) return;
        this.vuelos.remove(v);
        guardarEnJson();
    }

    @Override
    public void modificacion(Vuelo vueloModificado) throws DatoInvalidoException{
        // 1) Buscar vuelo original
        Vuelo vueloOriginal = this.consulta(vueloModificado.getIdVuelo());
        if(vueloOriginal == null) throw new DatoInvalidoException("El vuelo que desea modificar no existe.");

        // 2) Modificar el vuelo original con el nuevo
        vueloOriginal.setOrigen(vueloModificado.getOrigen());
        vueloOriginal.setDestino(vueloModificado.getDestino());
        vueloOriginal.setFechaHoraSalida(vueloModificado.getFechaHoraSalida());
        vueloOriginal.setFechaHoraLlegada(vueloModificado.getFechaHoraLlegada());
        vueloOriginal.setAerolinea(vueloModificado.getAerolinea());
        vueloOriginal.setAvion(vueloModificado.getAvion());
        vueloOriginal.setPrecioBase(vueloModificado.getPrecioBase());
        vueloOriginal.setActivo(vueloModificado.isActivo());
        vueloOriginal.setTieneServicioDeComida(vueloModificado.isTieneServicioDeComida());
        vueloOriginal.setCarryOnGratis(vueloModificado.isCarryOnGratis());

        // 3) Guardar
        guardarEnJson();
    }

    @Override
    public Vuelo consulta(String idVuelo){
        for(Vuelo v: this.vuelos){
            if(v.getIdVuelo().equals(idVuelo)){
                return v;
            }
        }
        return null;
    }

    @Override
    public List<Vuelo> listar(){
        return new ArrayList<>(this.vuelos);
    }
}
