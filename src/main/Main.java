package main;

/**
 * Punto de entrada principal de la aplicación de reserva de vuelos (AeroReserva).
 *
 * Esta clase tiene la única responsabilidad de instanciar y lanzar
 * la clase 'Aplicacion', que es la que contiene la lógica principal
 * de orquestación del programa.
 *
 * @version 1.0
 * @since 2025-11-05
 */
public class Main {

    public static void main(String[] args) {

        // 1. Crea la instancia de la aplicación
        Aplicacion app = new Aplicacion();

        // 2. Le cede el control total a la aplicación para que inicie
        app.iniciar();
    }
}