package Vistas;

import Entidades.Usuario;
import Servicios.*;
import Excepciones.*;
import Enum.RolUsuario;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;


public class MenuPrincipal {

    private Scanner scanner;
    private GestorUsuarios gestorUsuarios;
    private GestorVuelos gestorVuelos;
    private GestorReservas gestorReservas;
    private GestorAeropuertos gestorAeropuertos;
    private GestorAerolineas gestorAerolineas;

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

    public void mostrar() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n===== BIENVENIDO A AeroReserva =====");
            System.out.println("1. Iniciar Sesión");
            System.out.println("2. Registrarse");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                int opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1: ejecutarLogin(); break;
                    case 2: ejecutarRegistro(); break;
                    case 3: salir = true; break;
                    default: System.out.println("❌ Opción no válida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("❌ Error: Debe ingresar un número.");
                scanner.nextLine();
            }
        }
    }

    private void ejecutarLogin() {
        System.out.println("\n--- INICIAR SESIÓN ---");


        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Contraseña: ");
        String password = scanner.nextLine();

        try {
            Usuario usuario = gestorUsuarios.login(email, password);
            System.out.println("✅ ¡Bienvenido, " + usuario.getNombre() + "!");

            if (usuario.getRol() == RolUsuario.ADMINISTRADOR) {
                MenuAdmin menuAdmin = new MenuAdmin(usuario, scanner,
                        gestorUsuarios, gestorVuelos, gestorReservas,
                        gestorAeropuertos, gestorAerolineas);
                menuAdmin.mostrar();
            } else {
                MenuCliente menuCliente = new MenuCliente(usuario, scanner,
                        gestorVuelos, gestorReservas, gestorUsuarios, gestorAeropuertos);
                menuCliente.mostrar();
            }

        } catch (LoginFallidoException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private void ejecutarRegistro() {
        System.out.println("\n--- REGISTRO DE NUEVO CLIENTE ---");
        System.out.println("(Ingrese '0' en cualquier momento para cancelar)");

        try {
            System.out.print("Nombre completo: ");
            String nombre = scanner.nextLine();
            if (nombre.equals("0")) return;

            System.out.print("DNI: ");
            String dni = scanner.nextLine();
            if (dni.equals("0")) return;

            LocalDate fechaNac = pedirFecha("Fecha de Nacimiento");
            if (fechaNac == null) return;

            // --- VALIDACIÓN DE EMAIL EN TIEMPO REAL ---
            String email = pedirEmailValido();
            if (email == null) return;

            // --- VALIDACIÓN DE PASSWORD EN TIEMPO REAL ---
            String password = pedirPasswordValida();
            if (password == null) return;

            // Si llegó acá, los datos tienen el formato correcto.
            // El gestor hará la validación final (ej: si el email ya existe en la base).
            gestorUsuarios.registrarUsuario(nombre, email, password, dni, fechaNac);
            System.out.println("✅ ¡Registro exitoso! Ya puede iniciar sesión.");

        } catch (EmailYaRegistradoException | DatoInvalidoException | PasswordInvalidaException e) {
            System.out.println("❌ Error en el registro: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error inesperado: " + e.getMessage());
        }
    }

    // --- HELPERS DE VALIDACIÓN ---

    private String pedirEmailValido() {
        while (true) {
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();
            if (email.equals("0")) return null;

            boolean tieneArroba = email.contains("@");
            boolean tienePunto = email.contains(".");

            if (!tieneArroba) {
                System.out.println("   ❌ Formato inválido: Falta el '@'.");
            } else if (!tienePunto) {
                System.out.println("   ❌ Formato inválido: Falta el dominio (ej: .com).");
            } else if (email.contains(" ")) {
                System.out.println("   ❌ Formato inválido: No puede tener espacios.");
            } else {
                return email;
            }
        }
    }

    private String pedirPasswordValida() {
        while (true) {
            System.out.print("Contraseña: ");
            String pass = scanner.nextLine();
            if (pass.equals("0")) return null;

            // Reglas: Min 6, 1 Mayúscula, 1 Número
            boolean largo = pass.length() >= 6;
            boolean mayuscula = pass.chars().anyMatch(Character::isUpperCase);
            boolean numero = pass.chars().anyMatch(Character::isDigit);

            if (!largo) {
                System.out.println("   ❌ Error: Debe tener al menos 6 caracteres.");
            } else if (!mayuscula) {
                System.out.println("   ❌ Error: Debe tener al menos una Mayúscula.");
            } else if (!numero) {
                System.out.println("   ❌ Error: Debe tener al menos un Número.");
            } else {
                return pass; // ¡Válida!
            }
        }
    }

    private LocalDate pedirFecha(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (AAAA-MM-DD): ");
            String input = scanner.nextLine().trim();
            if (input.equals("0")) return null;
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("   ❌ Formato fecha inválido.");
            }
        }
    }
}