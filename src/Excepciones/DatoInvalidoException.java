package Excepciones;

/**
 * Excepción genérica para errores de lógica de negocio y validación.
 * Se usa cuando el admin ingresa datos ilógicos (ej. precio negativo)
 * o intenta una operación prohibida (ej. modificar un vuelo con reservas
 * o cancelar el último pasaje de una reserva).
 *
 * @version 1.0
 * @since 2025-11-05
 */
public class DatoInvalidoException extends Exception {

    /**
     * Constructor que acepta un mensaje de error.
     * @param message El mensaje que se mostrará al usuario (ej: "El precio no puede ser negativo").
     */
    public DatoInvalidoException(String message) {
        super(message);
    }
}