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
 *
 * @version 1.1
 * @since 2025-11-05
 */
public class GestorVuelos implements Gestionable<Vuelo, String> {

    // --- Atributos ---

    /**
     * Lista maestra en memoria de TODOS los vuelos.
     */
    private List<Vuelo> vuelos;

    /**
     * Inyección de Dependencia.
     * Referencia al Gestor de Aeropuertos (creado por la clase Aplicacion).
     * Se necesita para validar los aeropuertos en la búsqueda.
     */
    private GestorAeropuertos gestorAeropuertos;

    /**
     * Se usa para leer y escribir en vuelos.json.
     */
    private JsonManagerVuelos jsonManager;

    // --- Constructor ---

    /**
     * Constructor que recibe sus dependencias (DI).
     * Carga la lista de vuelos desde el JSON.
     *
     * @param ga El Gestor de Aeropuertos (para validar aeropuertos).
     */
    public GestorVuelos(GestorAeropuertos ga) {
        this.jsonManager = new JsonManagerVuelos();
        this.gestorAeropuertos = ga;
        this.vuelos = jsonManager.leerLista();
    }

    /**
     * Helper privado para persistir la lista actual en el JSON.
     */
    private void guardarEnJson() {
        jsonManager.guardarLista(this.vuelos);
    }

    // --- Métodos de Lógica de Negocio (Búsqueda) ---

    /**
     * Busca itinerarios (directos o con 1 escala) para el cliente.
     * @param origen El código IATA del aeropuerto de origen.
     * @param destino El código IATA del aeropuerto de destino.
     * @param fecha La fecha de salida.
     * @return Una lista de Itinerarios (directos y/o con escala).
     * @throws ItinerarioNoEncontradoException Si no se encuentra ningún resultado.
     */
    public List<Itinerario> buscarItinerarios(String inputOrigen, String inputDestino, LocalDate fecha)
            throws ItinerarioNoEncontradoException {

        // 1. Traducir el texto del usuario a listas de aeropuertos reales
        // Ej: "Buenos Aires" -> [EZE, AEP]
        List<Aeropuerto> origenesPosibles = gestorAeropuertos.buscarCoincidencias(inputOrigen);
        List<Aeropuerto> destinosPosibles = gestorAeropuertos.buscarCoincidencias(inputDestino);

        if (origenesPosibles.isEmpty() || destinosPosibles.isEmpty()) {
            throw new ItinerarioNoEncontradoException("No se encontraron aeropuertos válidos para '" + inputOrigen + "' o '" + inputDestino + "'.");
        }

        List<Itinerario> resultados = new ArrayList<>();

        // 2. Buscar VUELOS DIRECTOS
        for (Vuelo vuelo : vuelos) {
            if (!vuelo.isActivo() || vuelo.getFechaHoraSalida() == null) continue;

            // ¿El vuelo sale de ALGUNO de los orígenes posibles?
            boolean origenMatch = origenesPosibles.contains(vuelo.getOrigen());
            // ¿El vuelo llega a ALGUNO de los destinos posibles?
            boolean destinoMatch = destinosPosibles.contains(vuelo.getDestino());

            boolean fechaMatch = vuelo.getFechaHoraSalida().toLocalDate().equals(fecha);

            if (origenMatch && destinoMatch && fechaMatch) {
                resultados.add(new Itinerario(List.of(vuelo)));
            }
        }

        // 3. Buscar VUELOS CON 1 ESCALA
        for (Vuelo v1 : vuelos) {
            // Filtros básicos para v1
            if (!v1.isActivo() || v1.getFechaHoraSalida() == null) continue;
            if (!v1.getFechaHoraSalida().toLocalDate().equals(fecha)) continue;

            // v1 debe salir de ALGUNO de los orígenes posibles
            if (!origenesPosibles.contains(v1.getOrigen())) continue;

            Aeropuerto escala = v1.getDestino();

            // Optimización: La escala no puede ser uno de los destinos finales
            if (destinosPosibles.contains(escala)) continue;

            for (Vuelo v2 : vuelos) {
                if (!v2.isActivo() || v2.getFechaHoraSalida() == null) continue;

                // Conexión estricta: v2 debe salir de donde llegó v1
                if (!v2.getOrigen().equals(escala)) continue;

                // v2 debe llegar a ALGUNO de los destinos finales posibles
                if (!destinosPosibles.contains(v2.getDestino())) continue;

                // Validar tiempo (v2 sale después de que v1 llega)
                if (v1.getFechaHoraLlegada() != null &&
                        v2.getFechaHoraSalida().isAfter(v1.getFechaHoraLlegada().plusHours(2))) {

                    List<Vuelo> segmentos = new ArrayList<>();
                    segmentos.add(v1);
                    segmentos.add(v2);
                    resultados.add(new Itinerario(segmentos));
                }
            }
        }

        if (resultados.isEmpty()) {
            throw new ItinerarioNoEncontradoException("No se encontraron itinerarios disponibles.");
        }
        return resultados;
    }

    // --- Métodos de la Interfaz Gestionable (ABMCL del Admin) ---

    /**
     * Agrega un nuevo vuelo al sistema.
     * Implementa la firma completa de Gestionable.
     *
     * @param vuelo El Vuelo a agregar (ya creado por el MenuAdmin).
     * @throws DatoInvalidoException Si los datos del vuelo son ilógicos (precio negativo, fechas cruzadas).
     * @throws EmailYaRegistradoException (Declarada por la interfaz, pero no usada aquí).
     * @throws PasswordInvalidaException (Declarada por la interfaz, pero no usada aquí).
     */
    @Override
    public void alta(Vuelo vuelo)
            throws EmailYaRegistradoException, PasswordInvalidaException, DatoInvalidoException {

        // 'alta' ahora valida los datos ---
        if (vuelo.getPrecioBase() < 0) {
            throw new DatoInvalidoException("El precio base no puede ser negativo.");
        }
        if (vuelo.getFechaHoraLlegada().isBefore(vuelo.getFechaHoraSalida())) {
            throw new DatoInvalidoException("La fecha de llegada no puede ser anterior a la de salida.");
        }


        this.vuelos.add(vuelo);
        guardarEnJson();

        // Esta clase NUNCA lanzará EmailYaRegistradoException o PasswordInvalidaException,
        // pero DEBE declararlas en la firma para cumplir con el contrato de Gestionable.
    }

    /**
     * ---  'baja' ahora es LÓGICA ---
     * Desactiva un vuelo (baja lógica). No lo borra.
     * @param idVuelo El ID del vuelo a desactivar.
     */
    @Override
    public void baja(String idVuelo) {
        Vuelo v = this.consulta(idVuelo);
        if (v != null) {
            v.setActivo(false);
            guardarEnJson();
        }
    }


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

        // 3) Sobrescribir los datos del vuelo original con los nuevos
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
        // Se modifica solo desde GestorReservas (al cancelar o cambiar asiento).

        // 4) Guardar
        guardarEnJson();
    }

    @Override
    public Vuelo consulta(String idVuelo) {
        for (Vuelo v : this.vuelos) {
            // Compara IDs de forma segura
            if (v.getIdVuelo().equals(idVuelo)) {
                return v;
            }
        }
        return null;
    }

    @Override
    public List<Vuelo> listar() {
        return new ArrayList<>(this.vuelos); // Devuelve una copia
    }
}
