package Servicios;

import Entidades.Aeropuerto;
import Persistencia.JsonManagerAeropuertos;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Maneja la lógica y la lista en memoria de aeropuertos.
 *
 * Esta clase carga los datos maestros "hardcodeados" desde el JSON al inicio.
 * NO implementa Gestionable y no permite al Admin modificar el inventario.
 * Su única responsabilidad es proveer métodos de consulta (lectura)
 * para los otros gestores y menús.
 *
 * @version 2.1 (Con búsqueda normalizada)
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
        // (Se destruye al terminar el constructor para ahorrar memoria)
        JsonManagerAeropuertos managerLocal = new JsonManagerAeropuertos();

        // 2. Carga los datos desde el archivo JSON como una Lista
        List<Aeropuerto> listaLeida = managerLocal.leerLista();

        // 3. Convierte la Lista en un HashSet
        // Esto elimina duplicados automáticamente si los hubiera
        this.aeropuertosDisponibles = new HashSet<>(listaLeida);
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
        for (Aeropuerto aeropuerto : this.aeropuertosDisponibles) {
            if (aeropuerto.getCodigoIATA().equalsIgnoreCase(codigoIATA)) {
                return aeropuerto;
            }
        }
        return null;
    }

    /**
     * Busca todos los aeropuertos que coincidan con el texto ingresado.
     * Puede coincidir con el Código IATA (ej: "EZE") o parte de la Ciudad (ej: "Buenos").
     * Ignora mayúsculas, minúsculas y tildes (acentos).
     *
     * @param texto El texto de búsqueda ingresado por el usuario.
     * @return Lista de aeropuertos que coinciden.
     */
    public List<Aeropuerto> buscarCoincidencias(String texto) {
        List<Aeropuerto> resultados = new ArrayList<>();
        if (texto == null) return resultados;

        // 1. Normalizamos el texto buscado (quitamos tildes y pasamos a minúscula)
        String busqueda = normalizarTexto(texto);

        for (Aeropuerto a : this.aeropuertosDisponibles) {
            // 2. Normalizamos los datos del aeropuerto para comparar
            String codigoNorm = normalizarTexto(a.getCodigoIATA());
            String ciudadNorm = normalizarTexto(a.getCiudad());
            String nombreNorm = normalizarTexto(a.getNombre());

            // 3. Comparamos (si coincide código O ciudad O nombre)
            if (codigoNorm.contains(busqueda) || ciudadNorm.contains(busqueda) || nombreNorm.contains(busqueda)) {
                resultados.add(a);
            }
        }
        return resultados;
    }

    /**
     * Metodo helper privado para quitar acentos y caracteres especiales.
     * Ej: "Córdoba" -> "cordoba", "Mendoza" -> "mendoza"
     */
    private String normalizarTexto(String input) {
        if (input == null) return "";
        // Normaliza a formato NFD (separa letras de tildes)
        String normalizado = Normalizer.normalize(input, Normalizer.Form.NFD);
        // Elimina los signos diacríticos (tildes) usando Regex
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalizado).replaceAll("").toLowerCase();
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
        // el método Set.contains() es instantáneo.
        Aeropuerto temp = new Aeropuerto(codigoIATA, "", "");
        return this.aeropuertosDisponibles.contains(temp);
    }
}