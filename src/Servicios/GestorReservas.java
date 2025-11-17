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

    private List<Reserva> reservas; // Lista maestra de TODAS las reservas
    private final JsonManagerReservas jsonManager;

    // Dependencias de otros gestores (inyectadas)
    private final GestorUsuarios gestorUsuarios;
    private final GestorVuelos gestorVuelos;


    public GestorReservas(GestorUsuarios gestorUsuarios, GestorVuelos gestorVuelos) {
        this.jsonManager = new JsonManagerReservas();
        this.gestorUsuarios = gestorUsuarios; // Almacena la dependencia
        this.gestorVuelos = gestorVuelos;   // Almacena la dependencia

        // 1. Carga la lista desde el JSON.
        // En este punto, todas las reservas tienen (cliente = null)
        this.reservas = jsonManager.leerLista();

        // 2. Re Linkeo
        // Itera sobre la lista cargada y "arregla" la conexión del cliente.
        for (Reserva r : this.reservas) {
            // Busca en el gestor de usuarios al cliente por el ID temporal
            Cliente clienteDeLaReserva = (Cliente) gestorUsuarios.consulta(r.getClienteIdTemporal());

            // Reconecta el objeto Cliente completo
            r.setCliente(clienteDeLaReserva);

            //  Poblar el historial del cliente
            if (clienteDeLaReserva != null && r.isActiva()) {
                clienteDeLaReserva.getHistorialDeReservas().add(r);
            }
        }
    }

    /**
     * Metodo privado para centralizar el guardado en JSON.
     * Llama al JsonManager para guardar la lista actual.
     */
    private void guardarEnJson() {
        jsonManager.guardarLista(this.reservas);
    }

    // --- Métodos Públicos de Lógica de Negocio ---

    public void crearReserva(Reserva nuevaReserva) {
        this.reservas.add(nuevaReserva);

        // Actualizar millas ( 1 milla por cada $10 gastados)
        Cliente cliente = nuevaReserva.getCliente();
        int millasGanadas = (int) (nuevaReserva.getCostoTotal() / 10);
        gestorUsuarios.actualizarMillas(cliente.getId(), cliente.getMillas() + millasGanadas);

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
            reserva.setActiva(false);
            reserva.setEstado(EstadoReserva.CANCELADA);

            // ¡CRUCIAL! Liberar todos los asientos
            for (Pasaje pasaje : reserva.getPasajes()) {
                // Buscamos el vuelo "maestro" en el GestorVuelos, no la copia del JSON
                Vuelo vueloMaestro = gestorVuelos.consulta(pasaje.getVuelo().getIdVuelo());
                if (vueloMaestro != null) {
                    vueloMaestro.liberarAsiento(pasaje.getAsiento());
                }
            }

            // Guardar los cambios en AMBOS archivos
            guardarEnJson(); // Guarda reservas.json
            gestorVuelos.guardarLista(); // Guarda vuelos.json (con asientos libres)
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
                gestorVuelos.guardarLista(); // Persistir cambio en vuelos.json
            }

            // 2. Quitar el pasaje de la lista
            reserva.getPasajes().remove(pasajeACancelar);

            // 3. Recalcular el costo
            reserva.calcularCostoTotal();

            // 4. Guardar la reserva actualizada
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
        pasaje.agregarEquipaje(nuevoEquipaje);
        reserva.calcularCostoTotal();
        guardarEnJson();
    }

    /**
     * Cambia el asiento de un pasaje específico, validando disponibilidad.
     * @param idReserva El ID de la reserva.
     * @param idPasaje El ID del pasaje a modificar.
     * @param nuevoAsiento El codigo del nuevo asiento.
     * @throws AsientoOcupadoException Si el nuevo asiento no está disponible.
     * @throws DatoInvalidoException Si la reserva o pasaje no se encuentran.
     */
    public void cambiarAsiento(String idReserva, String idPasaje, String nuevoAsiento)
            throws AsientoOcupadoException, DatoInvalidoException {

        Reserva reserva = buscarReservaPorId(idReserva);
        if (reserva == null) throw new DatoInvalidoException("Reserva no encontrada.");

        Pasaje pasaje = reserva.buscarPasaje(idPasaje);
        if (pasaje == null) throw new DatoInvalidoException("Pasaje no encontrado.");

        Vuelo vueloMaestro = gestorVuelos.consulta(pasaje.getVuelo().getIdVuelo());
        if (vueloMaestro == null) throw new DatoInvalidoException("El vuelo asociado ya no existe.");

        // 1. Verificar si el nuevo asiento está libre
        if (!vueloMaestro.isAsientoLibre(nuevoAsiento)) {
            throw new AsientoOcupadoException("El asiento " + nuevoAsiento + " ya está ocupado.");
        }

        // 2. Realizar el cambio (liberar el viejo, ocupar el nuevo)
        vueloMaestro.liberarAsiento(pasaje.getAsiento());
        vueloMaestro.ocuparAsiento(nuevoAsiento);

        // 3. Actualizar el pasaje
        pasaje.setAsiento(nuevoAsiento);

        // 4. Persistir ambos cambios
        guardarEnJson(); // Guarda reservas.json (con el nuevo nro de asiento)
        gestorVuelos.guardarLista(); // Guarda vuelos.json (con el mapa de asientos actualizado)
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

        // Modifica el objeto Pasajero
        pasaje.getPasajero().setNombreCompleto(nuevoNombre);
        pasaje.getPasajero().setDNI(nuevoDNI);

        guardarEnJson();
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

        if (pasaje.getClase() == nuevaClase) {
            throw new DatoInvalidoException("El pasaje ya es de clase " + nuevaClase);
        }

        // VALIDACIÓN DE DISPONIBILIDAD (Simplificada)
        // Un sistema real comprobaría la capacidad vs. los pasajes vendidos en esa clase.
        // Aquí asumimos que la validación de asientos es suficiente.
        // Si quisieras ser más estricto, deberías contar los pasajes de esa clase.

        // 1. Cambiar la clase
        pasaje.setClase(nuevaClase);

        // 2. Recalcular el costo (ahora será más caro o más barato)
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