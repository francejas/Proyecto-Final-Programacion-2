package Excepciones;

/**
 * Se lanza cuando la contraseña proporcionada durante el registro
 * no cumple con las reglas de formato (mayúscula, número, longitud, etc.).
 *
 * @version 1.0
 * @since 2025-11-05
 */
public class PasswordInvalidaException extends Exception {

    /**
     * Constructor que acepta un mensaje de error.
     * @param message El mensaje que se mostrará al usuario (ej: "La contraseña debe tener...").
     */
    public PasswordInvalidaException(String message) {
        super(message);
    }
}