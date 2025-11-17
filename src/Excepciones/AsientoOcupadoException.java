package Excepciones;

/**
 * Se lanza cuando un usuario (cliente o admin) intenta
 * seleccionar un asiento que ya está ocupado.
 *
 * @version 1.0
 * @since 2025-11-05
 */
public class AsientoOcupadoException extends Exception {

    /**
     * Constructor que acepta un mensaje de error.
     * @param message El mensaje que se mostrará al usuario (ej: "El asiento 24A ya está ocupado").
     */
    public AsientoOcupadoException(String message) {
        super(message);
    }
}