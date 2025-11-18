package Excepciones;

/**
 * Se lanza cuando el GestorVuelos no encuentra ningún itinerario
 * (directo o con escalas) que coincida con la búsqueda del cliente.
 *
 * @version 1.0
 * @since 2025-11-05
 */
public class ItinerarioNoEncontradoException extends Exception {

    /**
     * Constructor que acepta un mensaje de error.
     * @param message El mensaje que se mostrará al usuario (ej: "No se encontraron vuelos para esa ruta/fecha.").
     */
    public ItinerarioNoEncontradoException(String message) {
        super(message);
    }
}