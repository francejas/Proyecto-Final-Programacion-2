package Vistas;

// Importa las entidades, enums y excepciones
import Entidades.Usuario;
import Enum.RolUsuario;
import Excepciones.DatoInvalidoException;
import Excepciones.EmailYaRegistradoException;
import Excepciones.LoginFallidoException;
import Excepciones.PasswordInvalidaException;


import Servicios.GestorAerolineas;
import Servicios.GestorAeropuertos;
import Servicios.GestorReservas;
import Servicios.GestorUsuarios;
import Servicios.GestorVuelos;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Maneja la pantalla de bienvenida (Login y Registro).
 * Actúa como el "bifurcador" que decide qué menú mostrar a continuación.
 *
 * @version 1.0
 * @since 2025-11-05
 */
public class MenuPrincipal {

    // --- Atributos (Dependencias Inyectadas) ---

    private Scanner scanner;
    // Gestores para Login/Registro
    private GestorUsuarios gestorUsuarios;

    // Gestores para "pasar" a los siguientes menús
    private GestorVuelos gestorVuelos;
    private GestorReservas gestorReservas;
    private GestorAeropuertos gestorAeropuertos;
    private GestorAerolineas gestorAerolineas;

    // --- Constructor (Inyección de Dependencias) ---

    /**
     * Constructor que recibe todas las dependencias necesarias
     * desde la clase 'Aplicacion'.
     */
    public MenuPrincipal(Scanner scanner, GestorUsuarios gestorUsuarios,
                         GestorVuelos gestorVuelos, GestorReservas gestorReservas,
                         GestorAeropuertos gestorAeropuertos, GestorAerolineas gestorAerolineas) {

        this.scanner = scanner;
        this.gestorUsuarios = gestorUsuarios;
        this.gestorVuelos = gestorVuelos;
        this.gestorReservas = gestorReservas;
        this.gestorAeropuertos = gestorAeropuertos;
        this.gestorAerolineas = gestorAerolineas;
    }

    // --- Metodo Principal del Menú ---

    /**
     * Muestra el menú principal y maneja la navegación
     * hasta que el usuario decida "Salir".
     */
    public void mostrar() {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n===== BIENVENIDO A AeroReserva =====");
            System.out.println("1. Iniciar Sesión");
            System.out.println("2. Registrarse");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                // Lee la opción numérica
                int opcion = scanner.nextInt();
                // Limpia el buffer del scanner (el "Enter" pendiente)
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        ejecutarLogin();
                        break;
                    case 2:
                        ejecutarRegistro();
                        break;
                    case 3:
                        salir = true;
                        break;
                    default:
                        System.out.println("Error: Opción no válida. Por favor, intente de nuevo.");
                }
            } catch (InputMismatchException e) {
                // Captura si el usuario escribe "hola" en lugar de un número
                System.out.println("Error: Debe ingresar un número (1, 2 o 3).");
                scanner.nextLine(); // Limpia el buffer del scanner
            }
        }
    }

    // --- Flujos Privados ---

    /**
     * Orquesta el flujo de inicio de sesión.
     * Pide credenciales, llama al gestor y delega al menú correspondiente.
     */
    private void ejecutarLogin() {
        System.out.println("\n--- INICIAR SESIÓN ---");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();

        try {
            // Llama al gestor para autenticar. Lanza LoginFallidoException si falla.
            Usuario usuario = gestorUsuarios.login(email, password);
            System.out.println("¡Login exitoso! Bienvenido, " + usuario.getNombre() + ".");

            // DELEGACIÓN: Decide qué menú mostrar basado en el rol
            if (usuario.getRol() == RolUsuario.ADMINISTRADOR) {
                // Crea e inyecta las dependencias al MenuAdmin
                MenuAdmin menuAdmin = new MenuAdmin(usuario, scanner,
                        gestorUsuarios, gestorVuelos, gestorReservas,
                        gestorAeropuertos, gestorAerolineas);
                menuAdmin.mostrar(); // Le pasa el control

            } else { // Si es CLIENTE
                // Crea e inyecta las dependencias al MenuCliente
                MenuCliente menuCliente = new MenuCliente(usuario, scanner,
                        gestorVuelos, gestorReservas, gestorUsuarios);
                menuCliente.mostrar(); // Le pasa el control
            }

        } catch (LoginFallidoException e) {
            // Captura el error de negocio (ej. "Clave incorrecta")
            System.out.println("Error de Login: " + e.getMessage());
        }
    }

    /**
     * Orquesta el flujo de registro de un nuevo Cliente.
     * Pide los datos y llama al gestor para la validación y guardado.
     */
    private void ejecutarRegistro() {
        System.out.println("\n--- REGISTRO DE NUEVO CLIENTE ---");
        try {
            // Pide todos los datos necesarios
            System.out.print("Nombre completo: ");
            String nombre = scanner.nextLine();
            System.out.print("DNI: ");
            String dni = scanner.nextLine();
            System.out.print("Fecha de Nacimiento (AAAA-MM-DD): ");
            // Lee la fecha como texto y la convierte (parsea)
            LocalDate fechaNac = LocalDate.parse(scanner.nextLine());

            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Contraseña (min 6 car, 1 mayús, 1 num, 1 símb): ");
            String password = scanner.nextLine();

            // Llama al gestor para que valide y registre al usuario
            gestorUsuarios.registrarUsuario(nombre, email, password, dni, fechaNac);
            System.out.println("¡Registro exitoso! Ya puede iniciar sesión.");

        } catch (DateTimeParseException e) {
            // Captura si el formato de la fecha es incorrecto
            System.out.println("Error: El formato de la fecha es incorrecto. Debe ser AAAA-MM-DD.");
        } catch (PasswordInvalidaException | EmailYaRegistradoException | DatoInvalidoException e) {
            // Captura los errores de negocio (contraseña débil, email duplicado, formato email)
            System.out.println("Error en el registro: " + e.getMessage());
        } catch (Exception e) {
            // Captura genérica para cualquier otro error
            System.out.println("Error inesperado durante el registro: " + e.getMessage());
            e.printStackTrace();
        }
    }
}