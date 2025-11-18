package Excepciones;

/**
 * Se lanza cuando se intenta registrar un usuario con un email
 * que ya existe en el sistema.
 *
 * @version 1.0
 * @since 2025-11-05
 */
public class EmailYaRegistradoException extends Exception {

    /**
     * Constructor que acepta un mensaje de error.
     * @param message El mensaje que se mostrará al usuario (ej: "El email '...' ya está en uso").
     */
    public EmailYaRegistradoException(String message) {
        super(message);
    }
}