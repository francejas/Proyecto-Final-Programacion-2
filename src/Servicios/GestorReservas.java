package Servicios;

import Entidades.*;
import Enum.EstadoReserva;
import Enum.TipoClase;
import Excepciones.AsientoOcupadoException;
import Excepciones.DatoInvalidoException;
import Persistencia.JsonManagerReservas;

import java.util.ArrayList;
import java.util.List;

/**
 * Maneja toda la lógica de negocio relacionada con la creación,
 * modificación y cancelación de Reservas.
 * No implementa Gestionable porque su lógica es de negocio específico.
 *
 * @version 1.0
 * @since 2025-11-05
 */
public class GestorReservas {

    // --- Atributos ---

    /**
     * Lista maestra en memoria de TODAS las reservas del sistema.
     */
    private List<Reserva> reservas;

    /**
     * Especialista en persistencia (paquete Persistencia).
     * Se usa para leer y escribir en reservas.json. Es 'final'
     * porque se instancia una vez en el constructor.
     */
    private final JsonManagerReservas jsonManager;

    /**
     * Inyección de Dependencia.
     * Referencia al Gestor de Usuarios (creado por la clase Aplicacion).
     * Se necesita para actualizar las millas de un cliente y para el re-linkeo.
     */
    private final GestorUsuarios gestorUsuarios;

    /**
     * Inyección de Dependencia.
     * Referencia al Gestor de Vuelos (creado por la clase Aplicacion).
     * Se necesita para consultar vuelos "maestros" y para liberar/ocupar asientos.
     */
    private final GestorVuelos gestorVuelos;

    // --- Constructor ---

    /**
     * Constructor del GestorReservas.
     * Recibe sus dependencias (otros gestores) al ser creado por la clase Aplicacion.
     * Carga las reservas desde el JSON y realiza el re-linkeo de los Clientes.
     *
     * @param gestorUsuarios (Inyectado) Referencia al gestor de usuarios principal.
     * @param gestorVuelos (Inyectado) Referencia al gestor de vuelos principal.
     */
    public GestorReservas(GestorUsuarios gestorUsuarios, GestorVuelos gestorVuelos) {
        this.jsonManager = new JsonManagerReservas();
        this.gestorUsuarios = gestorUsuarios; // Almacena la dependencia
        this.gestorVuelos = gestorVuelos;   // Almacena la dependencia

        // 1. Carga la lista desde el JSON.
        // En este punto, todas las reservas tienen (cliente = null)
        // y (clienteIdTemporal = 123)
        this.reservas = jsonManager.leerLista();

        // 2. re-linkeo
        // Itera sobre la lista cargada y arregla la conexión del cliente
        // para evitar referencias circulares en el JSON.
        for (Reserva r : this.reservas) {
            // Busca en el gestor de usuarios al cliente por el ID temporal
            Cliente clienteDeLaReserva = (Cliente) gestorUsuarios.consulta(r.getClienteIdTemporal());

            // Re-conecta el objeto Cliente completo
            r.setCliente(clienteDeLaReserva);

            // Poblar el historial del cliente
            if (clienteDeLaReserva != null && r.isActiva()) {
                clienteDeLaReserva.getHistorialDeReservas().add(r);
            }
        }
    }

    /**
     * Metodo helper privado para centralizar el guardado en JSON.
     * Llama al JsonManager para guardar la lista 'reservas' actual.
     * Se llama después de cualquier operación que modifique la lista de reservas.
     */
    private void guardarEnJson() {
        jsonManager.guardarLista(this.reservas);
    }

    // --- Métodos Públicos de Lógica de Negocio ---

    /**
     * Agrega una nueva reserva a la lista, actualiza las millas del cliente
     * y persiste los cambios.
     * @param nuevaReserva La reserva (ya creada y con pasajes) a agregar.
     */
    public void crearReserva(Reserva nuevaReserva) {
        this.reservas.add(nuevaReserva);

        // Lógica de negocio: Actualizar millas (ej. 1 milla por cada $10 gastados)
        Cliente cliente = nuevaReserva.getCliente();
        int millasGanadas = (int) (nuevaReserva.getCostoTotal() / 10);
        // Delega la actualización de millas al gestor correspondiente
        gestorUsuarios.actualizarMillas(cliente.getId(), cliente.getMillas() + millasGanadas);

        // Guarda el estado de la lista de reservas en el JSON
        guardarEnJson();
    }

    /**
     * Realiza la baja lógica de una reserva.
     * Libera todos los asientos asociados a esa reserva.
     * @param idReserva El ID de la reserva a cancelar.
     */
    public void cancelarReserva(String idReserva) {
        Reserva reserva = buscarReservaPorId(idReserva);
        if (reserva != null && reserva.isActiva()) {
            // 1. Marca la reserva como inactiva (Baja Lógica)
            reserva.setActiva(false);
            reserva.setEstado(EstadoReserva.CANCELADA);

            // 2. Liberar todos los asientos
            for (Pasaje pasaje : reserva.getPasajes()) {
                // Busca el vuelo "maestro" en GestorVuelos (no la copia en el Pasaje)
                Vuelo vueloMaestro = gestorVuelos.consulta(pasaje.getVuelo().getIdVuelo());
                if (vueloMaestro != null) {
                    vueloMaestro.liberarAsiento(pasaje.getAsiento());

                    // Informa al GestorVuelos que este vuelo ha sido modificado
                    // para que GestorVuelos se encargue de persistir su propio archivo.
                    try {
                        // Llama al metodo PÚBLICO de GestorVuelos
                        gestorVuelos.modificacion(vueloMaestro);
                    } catch (DatoInvalidoException e) {
                        // Esta excepción no debería ocurrir si la lógica es correcta
                        System.err.println("Error al persistir la liberación de asientos: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            // 3. Guarda el cambio en reservas.json
            guardarEnJson();
        }
    }

    /**
     * Cancela un único pasaje (pasajero) de una reserva.
     * No funciona si es el último pasaje de la reserva.
     * @param idReserva El ID de la reserva.
     * @param idPasaje El ID del pasaje a cancelar.
     * @throws DatoInvalidoException Si se intenta cancelar el último pasaje.
     */
    public void cancelarPasaje(String idReserva, String idPasaje) throws DatoInvalidoException {
        Reserva reserva = buscarReservaPorId(idReserva);
        if (reserva == null || !reserva.isActiva()) {
            throw new DatoInvalidoException("La reserva no existe o ya está cancelada.");
        }

        // Regla de negocio: No se puede cancelar el último pasaje
        if (reserva.getPasajes().size() <= 1) {
            throw new DatoInvalidoException("No se puede cancelar el último pasaje. Cancele la reserva completa.");
        }

        Pasaje pasajeACancelar = reserva.buscarPasaje(idPasaje);
        if (pasajeACancelar != null) {
            // 1. Liberar el asiento
            Vuelo vueloMaestro = gestorVuelos.consulta(pasajeACancelar.getVuelo().getIdVuelo());
            if (vueloMaestro != null) {
                vueloMaestro.liberarAsiento(pasajeACancelar.getAsiento());

                // Informa al GestorVuelos que el mapa de asientos de este vuelo cambió
                try {
                    gestorVuelos.modificacion(vueloMaestro);
                } catch (DatoInvalidoException e) {
                    e.printStackTrace();
                }
            }

            // 2. Quitar el pasaje de la lista de la reserva
            reserva.getPasajes().remove(pasajeACancelar);

            // 3. Recalcular el costo total de la reserva
            reserva.calcularCostoTotal();

            // 4. Guardar la reserva actualizada (con un pasaje menos y nuevo precio)
            guardarEnJson();
        }
    }

    /**
     * Agrega una nueva pieza de equipaje a un pasaje existente
     * y recalcula el costo total de la reserva.
     * @param reserva La reserva que se está modificando.
     * @param pasaje El pasaje al que se le agrega el equipaje.
     * @param nuevoEquipaje El objeto Equipaje a agregar.
     */
    public void agregarEquipaje(Reserva reserva, Pasaje pasaje, Equipaje nuevoEquipaje) {
        pasaje.agregarEquipaje(nuevoEquipaje); // Agrega al pasaje
        reserva.calcularCostoTotal(); // Actualiza el total de la reserva
        guardarEnJson(); // Persiste el cambio
    }

    /**
     * Cambia el asiento de un pasaje específico, validando disponibilidad.
     * @param idReserva El ID de la reserva.
     * @param idPasaje El ID del pasaje a modificar.
     * @param nuevoAsiento El código del nuevo asiento (ej. "28B").
     * @throws AsientoOcupadoException Si el nuevo asiento no está disponible.
     * @throws DatoInvalidoException Si la reserva, pasaje o vuelo no se encuentran.
     */
    public void cambiarAsiento(String idReserva, String idPasaje, String nuevoAsiento)
            throws AsientoOcupadoException, DatoInvalidoException {

        // 1. Validar y obtener todos los objetos necesarios
        Reserva reserva = buscarReservaPorId(idReserva);
        if (reserva == null) throw new DatoInvalidoException("Reserva no encontrada.");

        Pasaje pasaje = reserva.buscarPasaje(idPasaje);
        if (pasaje == null) throw new DatoInvalidoException("Pasaje no encontrado.");

        // Busca el Vuelo "maestro" (de la lista de GestorVuelos)
        Vuelo vueloMaestro = gestorVuelos.consulta(pasaje.getVuelo().getIdVuelo());
        if (vueloMaestro == null) throw new DatoInvalidoException("El vuelo asociado ya no existe.");

        // 2. Validar la regla de negocio (disponibilidad)
        if (!vueloMaestro.isAsientoLibre(nuevoAsiento)) {
            throw new AsientoOcupadoException("El asiento " + nuevoAsiento + " ya está ocupado.");
        }

        // 3. Realizar el cambio (liberar el viejo, ocupar el nuevo en el Vuelo "maestro")
        vueloMaestro.liberarAsiento(pasaje.getAsiento());
        vueloMaestro.ocuparAsiento(nuevoAsiento);

        // 4. Actualizar el pasaje dentro de la reserva
        pasaje.setAsiento(nuevoAsiento);

        // 5. Persistir ambos cambios (cada gestor el suyo)
        guardarEnJson(); // Guarda reservas.json (con el nuevo nro de asiento)

        // Informa al GestorVuelos que el mapa de asientos cambió
        try {
            gestorVuelos.modificacion(vueloMaestro); // Esto guarda vuelos.json
        } catch (DatoInvalidoException e) {
            // Esta excepción no debería ocurrir aquí si la lógica es correcta
            e.printStackTrace();
        }
    }

    /**
     * Corrige los datos personales de un pasajero en una reserva.
     * @param idReserva El ID de la reserva.
     * @param idPasaje El ID del pasaje a modificar.
     * @param nuevoNombre El nuevo nombre.
     * @param nuevoDNI El nuevo DNI.
     */
    public void corregirDatosPasajero(String idReserva, String idPasaje, String nuevoNombre, String nuevoDNI)
            throws DatoInvalidoException {

        Reserva reserva = buscarReservaPorId(idReserva);
        if (reserva == null) throw new DatoInvalidoException("Reserva no encontrada.");

        Pasaje pasaje = reserva.buscarPasaje(idPasaje);
        if (pasaje == null) throw new DatoInvalidoException("Pasaje no encontrado.");

        // Modifica el objeto Pasajero (que está dentro del Pasaje)
        pasaje.getPasajero().setNombreCompleto(nuevoNombre);
        pasaje.getPasajero().setDNI(nuevoDNI);

        guardarEnJson(); // Persiste el cambio en reservas.json
    }

    /**
     * Cambia la clase de un pasaje (ej. de ECONOMY a BUSINESS).
     * @param idReserva El ID de la reserva.
     * @param idPasaje El ID del pasaje a modificar.
     * @param nuevaClase El enum TipoClase (ej. TipoClase.BUSINESS).
     * @throws DatoInvalidoException Si la nueva clase no tiene disponibilidad.
     */
    public void cambiarClaseDePasaje(String idReserva, String idPasaje, TipoClase nuevaClase)
            throws DatoInvalidoException {

        Reserva reserva = buscarReservaPorId(idReserva);
        if (reserva == null) throw new DatoInvalidoException("Reserva no encontrada.");

        Pasaje pasaje = reserva.buscarPasaje(idPasaje);
        if (pasaje == null) throw new DatoInvalidoException("Pasaje no encontrado.");

        // Regla de negocio: no hacer un cambio innecesario
        if (pasaje.getClase() == nuevaClase) {
            throw new DatoInvalidoException("El pasaje ya es de clase " + nuevaClase);
        }


        // 1. Cambiar la clase
        pasaje.setClase(nuevaClase);

        // 2. Recalcular el costo (el pasaje ahora cuesta más/menos)
        reserva.calcularCostoTotal();

        // 3. Persistir
        guardarEnJson();
    }


    // --- Métodos de Consulta ---

    /**
     * Busca y devuelve una reserva específica por su ID.
     * @param idReserva El ID a buscar.
     * @return La Reserva encontrada, o null si no existe.
     */
    public Reserva buscarReservaPorId(String idReserva) {
        for (Reserva r : this.reservas) {
            if (r.getIdReserva().equals(idReserva)) {
                return r;
            }
        }
        return null;
    }


    /**
     * Devuelve una lista de todas las reservas activas de un cliente.
     * @param idCliente El ID del cliente.
     * @return Una nueva lista con las reservas del cliente.
     */
    public List<Reserva> buscarReservasPorCliente(int idCliente) {
        // 1. Crear una lista vacía para los resultados
        List<Reserva> reservasDelCliente = new ArrayList<>();

        // 2. Recorrer la lista maestra de TODAS las reservas
        for (Reserva r : this.reservas) {

            // 3. Verificar todas las condiciones con un 'if'
            // (Asegurarse de que el cliente no sea nulo antes de llamar a getId)
            if (r.getCliente() != null &&
                    r.getCliente().getId() == idCliente &&
                    r.isActiva())
            {
                // 4. Si cumple, agregarla a la lista de resultados
                reservasDelCliente.add(r);
            }
        }

        // 5. Devolver la lista filtrada
        return reservasDelCliente;
    }

    /**
     * Devuelve una lista de TODAS las reservas del sistema (activas e inactivas).
     * Usado por el MenuAdmin para reportes.
     * @return Una copia de la lista maestra de reservas.
     */
    public List<Reserva> listarTodasLasReservas() {
        return new ArrayList<>(this.reservas); // Devuelve una copia
    }
}