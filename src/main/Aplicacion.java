package main;

import Servicios.GestorAerolineas;
import Servicios.GestorAeropuertos;
import Servicios.GestorReservas;
import Servicios.GestorUsuarios;
import Servicios.GestorVuelos;

import Vistas.MenuPrincipal;

import java.util.Scanner;

/**
 * Clase principal que orquesta toda la aplicación.
 * Es la dueña de las instancias de los gestores
 * y del scanner, y se encarga de inyectar estas dependencias en los menús.
 *
 * @version 1.0
 * @since 2025-11-05
 */
public class Aplicacion {

    // --- Atributos  ---
    // Son 'final' porque se instancian UNA VEZ y nunca cambian.

    // Gestores de "Solo Lectura" (Inventario)
    private final GestorAeropuertos gestorAeropuertos;
    private final GestorAerolineas gestorAerolineas;

    // Gestores con ABMCL (Gestión de Admin)
    private final GestorUsuarios gestorUsuarios;
    private final GestorVuelos gestorVuelos;

    // Gestor de Lógica de Negocio (Reservas)
    private final GestorReservas gestorReservas;

    // Herramienta de I/O compartida
    private final Scanner scanner;

    // --- Constructor ---

    /**
     * Constructor de la Aplicación.
     * Inicializa todos los gestores en el orden correcto de sus dependencias.
     * Aquí es donde se leen todos los archivos JSON y se carga
     * la aplicación en memoria.
     */
    public Aplicacion() {
        // 1. Inicializa el Scanner que se compartirá
        this.scanner = new Scanner(System.in);

        // 2. Inicializa los gestores que NO tienen dependencias
        this.gestorAeropuertos = new GestorAeropuertos(); // Lee aeropuertos.json
        this.gestorAerolineas = new GestorAerolineas(); // Lee aerolineas.json
        this.gestorUsuarios = new GestorUsuarios();     // Lee usuarios.json e inicializa el contador de IDs

        // 3. Inicializa los gestores que SÍ tienen dependencias

        // GestorVuelos necesita a GestorAeropuertos para validar aeropuertos
        this.gestorVuelos = new GestorVuelos(this.gestorAeropuertos);

        // GestorReservas necesita a Usuarios (para millas/re-linkeo) y Vuelos (para asientos)
        this.gestorReservas = new GestorReservas(this.gestorUsuarios, this.gestorVuelos);


    }

    /**
     * Inicia el bucle principal de la aplicación.
     * Crea el primer menú (MenuPrincipal) y le pasa todas las
     * dependencias (gestores y scanner) que este necesitará
     * para operar y pasar a los siguientes menús.
     */
    public void iniciar() {

        System.out.println("Iniciando AeroReserva...");

        // 1. Crea el primer menú, "inyectando" todas las dependencias
        MenuPrincipal menuPrincipal = new MenuPrincipal(
                scanner,
                gestorUsuarios,
                gestorVuelos,
                gestorReservas,
                gestorAeropuertos,
                gestorAerolineas
        );

        // 2. Le pasa el control al menú principal
        menuPrincipal.mostrar();

        // 3. Cuando menuPrincipal.mostrar() termina (porque el usuario eligió "Salir"),
        // el metodo iniciar() finaliza.
        System.out.println("\nGracias por usar AeroReserva. ¡Hasta luego!");

        // 4. Cierra el recurso del scanner al final de la vida de la aplicación.
        scanner.close();
    }
}