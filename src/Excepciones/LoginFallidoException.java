package Excepciones;

/**
 * Se lanza cuando el GestorUsuarios no puede autenticar a un usuario,
 * ya sea por contraseña incorrecta o porque la cuenta está inactiva.
 *
 * @version 1.0
 * @since 2025-11-05
 */
public class LoginFallidoException extends Exception {

    /**
     * Constructor que acepta un mensaje de error.
     * @param message El mensaje que se mostrará al usuario (ej: "Email o contraseña incorrectos").
     */
    public LoginFallidoException(String message) {
        super(message);
    }
}