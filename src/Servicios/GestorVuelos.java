package Servicios;

import Entidades.Aeropuerto;
import Entidades.Itinerario;
import Entidades.Vuelo;
import Excepciones.DatoInvalidoException;
import Excepciones.EmailYaRegistradoException;
import Excepciones.ItinerarioNoEncontradoException;
import Excepciones.PasswordInvalidaException;
import Persistencia.JsonManagerVuelos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Maneja la lógica de negocio para la gestión de Vuelos e Itinerarios.
 * Implementa la interfaz Gestionable para el ABMCL.
 */
public class GestorVuelos implements Gestionable<Vuelo, String>{
    private List<Vuelo> vuelos; // Lista maestra en memoria
    private GestorAeropuertos gestorAeropuertos;
    private JsonManagerVuelos jsonManager;

    /**
     * Constructor que recibe sus dependencias (DI).
     * Carga la lista de vuelos desde el JSON.
     * @param ga El Gestor de Aeropuertos (para validar aeropuertos).
     */
    public GestorVuelos(GestorAeropuerto ga){
        this.jsonManager = new JsonManagerVuelos();
        this.gestorAeropuerto = ga; // Asigna la dependencia
        this.vuelos = jsonManager.leerLista(); // Carga los datos
    }

    /**
     * Helper privado para persistir la lista actual en el JSON.
     */
    private void guardarEnJson(){
        jsonManager.guardarLista(this.vuelos);
    }

    /**
     * Busca itinerarios (directos o con 1 escala) para el cliente.
     */
    public List<Itinerario> buscarItinerarios(String origen, String destino, LocalDate fecha) throws ItinerarioNoEncontradoException {
        // Valida que los códigos IATA existan usando el gestor inyectado
        boolean origenValido = gestorAeropuertos.validarAeropuerto(origen);
        boolean destinoValido = gestorAeropuertos.validarAeropuerto(destino);

        List<Itinerario> resultados = new ArrayList<>();

        // 1) Validacion de aeropuertos
        if (!origenValido || !destinoValido) {
            // No lanza excepción, solo devuelve una lista vacía si los aeropuertos no existen
            throw new ItinerarioNoEncontradoException("El aeropuerto de origen o destino no es válido.");
        }

        // 2) Buscar vuelos directos
        for (Vuelo vuelo : vuelos) {
            // Solo buscar en vuelos activos
            if (!vuelo.isActivo()) continue; 
            
            boolean mismoOrigen = vuelo.getOrigen().getCodigoIATA().equalsIgnoreCase(origen);
            boolean mismoDestino = vuelo.getDestino().getCodigoIATA().equalsIgnoreCase(destino);
            boolean mismaFecha = vuelo.getFechaHoraSalida().toLocalDate().equals(fecha);

            if (mismoOrigen && mismoDestino && mismaFecha) {
                resultados.add(new Itinerario(List.of(vuelo)));
            }
        }

        // 3) Buscar vuelos con 1 escala
        for (Vuelo v1 : vuelos) {
            // Ignora vuelos inactivos o que no coinciden con la salida
            if (!v1.isActivo() || 
                !v1.getOrigen().getCodigoIATA().equalsIgnoreCase(origen) || 
                !v1.getFechaHoraSalida().toLocalDate().equals(fecha)) {
                continue;
            }

            Aeropuerto aeropuertoEscala = v1.getDestino();

            for (Vuelo v2 : vuelos) {
                // Ignora vuelos inactivos o que no son la conexión correcta
                if (!v2.isActivo() || 
                    !v2.getOrigen().equals(aeropuertoEscala) || 
                    !v2.getDestino().getCodigoIATA().equalsIgnoreCase(destino)) {
                    continue;
                }

                // Validar que la conexión sea posible (ej. al menos 2 horas de espera)
                // Se asegura de que v2 salga DESPUÉS de que v1 llegue.
                if (v2.getFechaHoraSalida().isAfter(v1.getFechaHoraLlegada().plusHours(2))) {
                    List<Vuelo> segmentos = new ArrayList<>();
                    segmentos.add(v1);
                    segmentos.add(v2);
                    resultados.add(new Itinerario(segmentos));
                }
            }
        }

        // Si, después de todo, no hay resultados, lanza la excepción
        if (resultados.isEmpty()) {
            throw new ItinerarioNoEncontradoException("No se encontraron vuelos ni conexiones para la ruta y fecha seleccionadas.");
        }

        return resultados;
    }

    /**
     * Agrega un nuevo vuelo al sistema.
     * Este metodo implementa la firma completa de Gestionable.
     *
     * @throws DatoInvalidoException Si los datos del vuelo son ilógicos.
     * @throws EmailYaRegistradoException (Declarada por la interfaz, pero no usada aquí).
     * @throws PasswordInvalidaException (Declarada por la interfaz, pero no usada aquí).
     */
    @Override
    public void alta(Vuelo vuelo)
            throws EmailYaRegistradoException, PasswordInvalidaException, DatoInvalidoException
    {
        // 1. Validación de reglas de negocio
        if (vuelo.getPrecioBase() < 0) {
            throw new DatoInvalidoException("El precio base no puede ser negativo.");
        }
        if (vuelo.getFechaHoraLlegada().isBefore(vuelo.getFechaHoraSalida())) {
            throw new DatoInvalidoException("La fecha de llegada no puede ser anterior a la de salida.");
        }

        // 2. Lógica de alta
        this.vuelos.add(vuelo);
        guardarEnJson();

        // Esta clase NUNCA lanzará EmailYaRegistradoException o PasswordInvalidaException,
        // pero DEBE declararlas en la firma para cumplir con el contrato de Gestionable.
    }

   /**
     * Desactiva un vuelo (baja lógica).
     * @param idVuelo El ID del vuelo a desactivar.
     */
    @Override
    public void baja(String idVuelo) {
        Vuelo v = this.consulta(idVuelo);
        if (v != null) {
            // No se borra, se marca como inactivo
            v.setActivo(false); 
            guardarEnJson();
        }
    }

  /**
     * Modifica un vuelo existente.
     * Asume que la validación de "no modificar si tiene reservas"
     * se hace en la capa de Vistas (MenuAdmin) antes de llamar a este método).
     */
    @Override
    public void modificacion(Vuelo vueloModificado) throws DatoInvalidoException {
        // 1) Buscar vuelo original
        Vuelo vueloOriginal = this.consulta(vueloModificado.getIdVuelo());
        if (vueloOriginal == null) {
            throw new DatoInvalidoException("El vuelo que desea modificar no existe.");
        }

        // 2) Validar los nuevos datos
        if (vueloModificado.getPrecioBase() < 0) {
            throw new DatoInvalidoException("El precio base no puede ser negativo.");
        }
        if (vueloModificado.getFechaHoraLlegada().isBefore(vueloModificado.getFechaHoraSalida())) {
            throw new DatoInvalidoException("La fecha de llegada no puede ser anterior a la de salida.");
        }

        // 3) Modificar el vuelo original con el nuevo
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
        // El mapa de asientos (asientosDisponibles) no se toca aquí.
        // Se modifica solo desde GestorReservas.

        // 4) Guardar
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
