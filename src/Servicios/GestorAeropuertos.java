package Servicios;

import Entidades.Aeropuerto;
import Persistencia.JsonManagerAeropuertos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Maneja la lógica y la lista en memoria de aeropuertos.
 *
 * Esta clase carga los datos maestros "hardcodeados" desde el JSON al inicio.
 * NO implementa Gestionable y no permite al Admin modificar el inventario.
 * Su única responsabilidad es proveer métodos de consulta (lectura)
 * para los otros gestores y menus.
 *
 * @version 2.0
 * @since 2025-11-05
 */
public class GestorAeropuertos {

    // --- Atributos ---

    /**
     * Colección en memoria de TODOS los aeropuertos.
     * Se usa un Set para garantizar que no haya códigos IATA duplicados
     * y para que las búsquedas (validarAeropuerto) sean instantáneas (O(1)).
     */
    private Set<Aeropuerto> aeropuertosDisponibles;

    // --- Constructor ---

    /**
     * Constructor del GestorAeropuertos.
     * Crea una instancia local de JsonManagerAeropuertos,
     * llama a leerLista() para obtener los datos,
     * y puebla el Set aeropuertosDisponibles.
     */
    public GestorAeropuertos() {
        // 1. Crea el manager de JSON como una variable local
        JsonManagerAeropuertos managerLocal = new JsonManagerAeropuertos();

        // 2. Carga los datos desde el archivo JSON como una Lista
        List<Aeropuerto> listaLeida = managerLocal.leerLista();

        // 3. Convierte la Lista en un HashSet
        // Esto elimina duplicados automáticamente si los hubiera
        this.aeropuertosDisponibles = new HashSet<>(listaLeida);

        // 'managerLocal' se destruye aquí (recolectado por el Garbage Collector)
    }

    // --- Métodos de Consulta (Solo Lectura) ---

    /**
     * Busca y devuelve un aeropuerto por su código IATA.
     * Usado por GestorVuelos para obtener los objetos de origen/destino.
     *
     * @param codigoIATA El código (ej: "EZE") a buscar (ignora mayús/minús).
     * @return El Aeropuerto encontrado, o null si no existe.
     */
    public Aeropuerto consulta(String codigoIATA) {
        // Itera sobre el set para encontrarlo por su código
        for (Aeropuerto aeropuerto : this.aeropuertosDisponibles) {
            if (aeropuerto.getCodigoIATA().equalsIgnoreCase(codigoIATA)) {
                return aeropuerto;
            }
        }
        return null; // No se encontró
    }

    /**
     * Devuelve una copia de la lista de todos los aeropuertos.
     * Usado por el MenuAdmin para mostrar las opciones al crear un Vuelo.
     *
     * @return Una nueva List<Aeropuerto>.
     */
    public List<Aeropuerto> listar() {
        // Devuelve una copia para que el Set interno no pueda ser modificado
        return new ArrayList<>(this.aeropuertosDisponibles);
    }

    /**
     * Valida de forma rápida si un código IATA existe en el sistema.
     * Este metodo es usado por GestorVuelos y MenuAdmin.
     *
     * @param codigoIATA El código a verificar.
     * @return true si el aeropuerto existe, false en caso contrario.
     */
    public boolean validarAeropuerto(String codigoIATA) {
        // Crea un objeto temporal solo para la búsqueda.
        // Gracias a que Aeropuerto.equals/hashCode se basa en codigoIATA,
        // el metodo Set.contains() es instantáneo.
        Aeropuerto temp = new Aeropuerto(codigoIATA, "", "");
        return this.aeropuertosDisponibles.contains(temp);
    }
}