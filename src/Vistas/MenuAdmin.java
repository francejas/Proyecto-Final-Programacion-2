package Vistas;

import Entidades.*;
import Servicios.*;
import Excepciones.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MenuAdmin {

    private Usuario adminLogueado;
    private Scanner scanner;
    private GestorUsuarios gestorUsuarios;
    private GestorVuelos gestorVuelos;
    private GestorReservas gestorReservas;
    private GestorAeropuertos gestorAeropuertos;
    private GestorAerolineas gestorAerolineas;

    public MenuAdmin(Usuario admin, Scanner scanner, GestorUsuarios gu, GestorVuelos gv,
                     GestorReservas gr, GestorAeropuertos gap, GestorAerolineas gal) {
        this.adminLogueado = admin;
        this.scanner = scanner;
        this.gestorUsuarios = gu;
        this.gestorVuelos = gv;
        this.gestorReservas = gr;
        this.gestorAeropuertos = gap;
        this.gestorAerolineas = gal;
    }

    public void mostrar() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n══════════════════════════════════════════════════");
            System.out.println("   PANEL DE ADMINISTRADOR: " + adminLogueado.getNombre().toUpperCase());
            System.out.println("══════════════════════════════════════════════════");
            System.out.println("1. ✈️  Gestionar Vuelos");
            System.out.println("2. 👤  Gestionar Usuarios");
            System.out.println("3. 🏢  Gestionar Aerolíneas (Precios y Estado)");
            System.out.println("4. 📊  Ver Reporte de Todas las Reservas");
            System.out.println("5. 🚪  Cerrar Sesión");
            System.out.println("══════════════════════════════════════════════════");
            System.out.print("Seleccione una opción: ");

            try {
                int opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1: flujoGestionarVuelos(); break;
                    case 2: flujoGestionarUsuarios(); break;
                    case 3: flujoGestionarAerolineas(); break;
                    case 4: flujoReporteReservas(); break;
                    case 5: salir = true; break;
                    default: System.out.println("❌ Opción no válida. Intente nuevamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("❌ Error: Debe ingresar un número.");
                scanner.nextLine();
            }
        }
    }

    // ========================================================================
    // 1. GESTIÓN DE VUELOS
    // ========================================================================

    private void flujoGestionarVuelos() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- GESTIÓN DE VUELOS ---");
            System.out.println("1. Agregar Nuevo Vuelo");
            System.out.println("2. Modificar Vuelo Existente");
            System.out.println("3. Baja (Desactivar) Vuelo");
            System.out.println("4. Listar Vuelos Activos (Detallado)");
            System.out.println("5. Volver");
            System.out.print("Opción: ");

            try {
                int op = scanner.nextInt(); scanner.nextLine();
                switch (op) {
                    case 1: crearVuelo(); break;
                    case 2: modificarVuelo(); break;
                    case 3: desactivarVuelo(); break;
                    case 4: listarVuelosDetallado(); break;
                    case 5: volver = true; break;
                    default: System.out.println("❌ Opción no válida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("❌ Error: Ingrese un número.");
                scanner.nextLine();
            }
        }
    }

    private void listarVuelosDetallado() {
        List<Vuelo> vuelos = gestorVuelos.listar();
        System.out.println("\n══════════════ LISTADO DE VUELOS ACTIVOS ══════════════");

        boolean hayVuelos = false;
        for (Vuelo v : vuelos) {
            if (v.isActivo()) {
                hayVuelos = true;

                Duration duracion = Duration.between(v.getFechaHoraSalida(), v.getFechaHoraLlegada());
                long horas = duracion.toHours();
                long minutos = duracion.toMinutes() % 60;

                System.out.println("✈️  VUELO: " + v.getIdVuelo() + " | " + v.getAerolinea().getNombre());
                System.out.println("    Origen:  " + v.getOrigen().getCodigoIATA() + " - " + v.getOrigen().getCiudad());
                System.out.println("    Destino: " + v.getDestino().getCodigoIATA() + " - " + v.getDestino().getCiudad());
                System.out.println("    Salida:  " + v.getFechaHoraSalida().toString().replace("T", " "));
                System.out.println("    Llegada: " + v.getFechaHoraLlegada().toString().replace("T", " "));
                System.out.println("    Duración: " + horas + "h " + minutos + "m");
                System.out.println("    Avión: " + v.getAvion().getModelo() + " (Matrícula: " + v.getAvion().getMatricula() + ")");
                System.out.println("    💰 Precio Base: $" + v.getPrecioBase());
                System.out.println("    🍽️  Comida: " + (v.isTieneServicioDeComida() ? "Sí" : "No"));
                System.out.println("    🎒 CarryOn Gratis: " + (v.isCarryOnGratis() ? "Sí" : "No"));
                System.out.println("═══════════════════════════════════════════════════════");
            }
        }

        if (!hayVuelos) System.out.println("   (No hay vuelos activos registrados)");
        System.out.println("Presione Enter para volver...");
        scanner.nextLine();
    }

    private void crearVuelo() {
        try {
            System.out.println("\n--- CREANDO NUEVO VUELO ---");

            List<Aeropuerto> listaAeropuertos = gestorAeropuertos.listar();
            if (listaAeropuertos.isEmpty()) {
                System.out.println("⚠️ ¡ERROR! No hay aeropuertos cargados. Ejecute 'PobladorDeDatos' primero.");
                return;
            }

            System.out.println("--- Aeropuertos Disponibles ---");
            for (Aeropuerto a : listaAeropuertos) System.out.println("- " + a);
            System.out.println("-------------------------------");

            Aeropuerto origen = pedirAeropuerto("Origen");
            if (origen == null) return;

            Aeropuerto destino = pedirAeropuerto("Destino");
            if (destino == null) return;

            if (origen.equals(destino)) {
                System.out.println("❌ Error: Origen y Destino no pueden ser iguales.");
                return;
            }

            System.out.println("\n--- Aerolíneas Disponibles ---");
            for (Aerolinea a : gestorAerolineas.listar()) {
                if(a.isActiva()) System.out.println("- " + a);
            }
            Aerolinea aerolinea = pedirAerolinea();
            if (aerolinea == null) return;

            Avion avion = pedirAvionDeFlota(aerolinea);
            if (avion == null) return;

            LocalDateTime salida = pedirFechaHora("Fecha y Hora Salida");
            if (salida == null) return;
            LocalDateTime llegada = pedirFechaHora("Fecha y Hora Llegada");

            while (llegada.isBefore(salida)) {
                System.out.println("❌ Error: La llegada no puede ser antes que la salida.");
                llegada = pedirFechaHora("Fecha y Hora Llegada");
            }

            double precio = pedirDouble("Precio Base");
            if (precio == -1) return;

            boolean comida = pedirBooleano("¿Tiene servicio de comida?");
            boolean carryOnGratis = pedirBooleano("¿CarryOn Gratis?");

            Vuelo nuevoVuelo = new Vuelo(origen, destino, salida, llegada, aerolinea, avion, precio, comida, carryOnGratis);
            gestorVuelos.alta(nuevoVuelo);
            System.out.println("✅ ¡Vuelo creado con éxito!");

        } catch (DatoInvalidoException e) {
            System.out.println("❌ Error de validación: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void modificarVuelo() {
        System.out.print("Ingrese ID del vuelo a modificar (o 0 para volver): ");
        String id = scanner.nextLine();
        if (id.equals("0")) return;

        Vuelo vuelo = gestorVuelos.consulta(id);
        if (vuelo == null) {
            System.out.println("❌ Vuelo no encontrado.");
            return;
        }

        System.out.println("Modificando Vuelo " + vuelo.getIdVuelo());
        System.out.println("Precio Base Actual: " + vuelo.getPrecioBase());

        double nuevoPrecio = pedirDouble("Nuevo Precio Base");
        if (nuevoPrecio == -1) return;

        vuelo.setPrecioBase(nuevoPrecio);

        try {
            gestorVuelos.modificacion(vuelo);
            System.out.println("✅ Vuelo modificado exitosamente.");
        } catch (DatoInvalidoException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private void desactivarVuelo() {
        System.out.print("Ingrese ID del vuelo a desactivar (o 0 para volver): ");
        String id = scanner.nextLine();
        if (id.equals("0")) return;

        gestorVuelos.baja(id);
        System.out.println("Operación realizada (si el ID existía, el vuelo ahora es inactivo).");
    }

    // ========================================================================
    // 2. GESTIÓN DE USUARIOS
    // ========================================================================

    private void flujoGestionarUsuarios() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- GESTIÓN DE USUARIOS ---");
            System.out.println("1. Listar Usuarios");
            System.out.println("2. Dar de Baja (Desactivar)");
            System.out.println("3. Dar de Alta (Reactivar)");
            System.out.println("4. Volver");
            System.out.print("Opción: ");

            try {
                int op = scanner.nextInt(); scanner.nextLine();
                switch (op) {
                    case 1:
                        listarUsuariosDetallado();
                        break;
                    case 2:
                        gestionarEstadoUsuario(false); // false = desactivar
                        break;
                    case 3:
                        gestionarEstadoUsuario(true); // true = activar
                        break;
                    case 4: volver = true; break;
                    default: System.out.println("❌ Opción inválida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("❌ Error: Ingrese un número.");
                scanner.nextLine();
            }
        }
    }

    private void listarUsuariosDetallado() {
        System.out.println("\n════════════════════ LISTADO DE USUARIOS ════════════════════");
        System.out.printf("%-5s | %-20s | %-25s | %-12s | %-10s%n", "ID", "NOMBRE", "EMAIL", "ROL", "ESTADO");
        System.out.println("--------------------------------------------------------------------------------");

        for (Usuario u : gestorUsuarios.listar()) {
            String estado = u.isActivo() ? "ACTIVO" : "INACTIVO";
            System.out.printf("%-5d | %-20s | %-25s | %-12s | %-10s%n",
                    u.getId(), u.getNombre(), u.getEmail(), u.getRol(), estado);
        }
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("Presione Enter para continuar...");
        scanner.nextLine();
    }

    private void gestionarEstadoUsuario(boolean activar) {
        String accion = activar ? "REACTIVAR" : "DESACTIVAR";

        System.out.println("\n--- Seleccione un Usuario para " + accion + " ---");
        System.out.printf("%-5s | %-20s | %-10s%n", "ID", "NOMBRE", "ESTADO ACTUAL");

        boolean hayOpciones = false;
        for(Usuario u : gestorUsuarios.listar()) {
            if(u.isActivo() != activar) {
                String estado = u.isActivo() ? "ACTIVO" : "INACTIVO";
                System.out.printf("%-5d | %-20s | %s%n", u.getId(), u.getNombre(), estado);
                hayOpciones = true;
            }
        }
        if (!hayOpciones) {
            System.out.println("   (No hay usuarios disponibles para esta acción)");
            return;
        }
        System.out.println("----------------------------------------------");

        while (true) {
            System.out.print("Ingrese ID del usuario (o 0 para cancelar): ");
            String input = scanner.nextLine().trim();

            if (input.equals("0")) return;

            try {
                int id = Integer.parseInt(input);
                if (id <= 0) {
                    System.out.println("❌ Error: ID inválido.");
                    continue;
                }

                Usuario u = gestorUsuarios.consulta(id);
                if (u == null) {
                    System.out.println("❌ Error: Usuario no encontrado.");
                    continue;
                }

                if (!activar && u.equals(adminLogueado)) {
                    System.out.println("⚠️ ¡No puedes desactivar tu propia cuenta!");
                    return;
                }

                if (u.isActivo() == activar) {
                    System.out.println("⚠️ Este usuario ya está " + (activar ? "ACTIVO" : "INACTIVO") + ".");
                    continue;
                }

                try {
                    Usuario modificado = u;
                    modificado.setActivo(activar);
                    gestorUsuarios.modificacion(modificado);
                    System.out.println("✅ Usuario '" + u.getNombre() + "' ha sido " + (activar ? "ACTIVADO" : "DESACTIVADO") + ".");
                    return;
                } catch (DatoInvalidoException e) {
                    System.out.println("Error al guardar: " + e.getMessage());
                }

            } catch (NumberFormatException e) {
                System.out.println("❌ Error: Debe ingresar un número.");
            }
        }
    }

    // ========================================================================
    // 3. GESTIÓN DE AEROLÍNEAS
    // ========================================================================

    private void flujoGestionarAerolineas() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- GESTIÓN DE AEROLÍNEAS ---");
            System.out.println("1. Listar Aerolíneas");
            System.out.println("2. Activar Aerolínea");
            System.out.println("3. Desactivar Aerolínea");
            System.out.println("4. Modificar Precios de Equipaje");
            System.out.println("5. Volver");
            System.out.print("Opción: ");

            try {
                int op = scanner.nextInt(); scanner.nextLine();
                switch (op) {
                    case 1:
                        listarAerolineasSimple();
                        System.out.println("Presione Enter..."); scanner.nextLine();
                        break;
                    case 2:
                        listarAerolineasSimple();

                        Aerolinea aActivar = pedirCodigoAerolineaRobusto("ACTIVAR");
                        if (aActivar != null) {
                            gestorAerolineas.activarAerolinea(aActivar.getCodigo());
                            System.out.println("✅ Aerolínea activada.");
                        }
                        break;
                    case 3:
                        listarAerolineasSimple();

                        Aerolinea aDesactivar = pedirCodigoAerolineaRobusto("DESACTIVAR");
                        if (aDesactivar != null) {
                            gestorAerolineas.desactivarAerolinea(aDesactivar.getCodigo());
                            System.out.println("✅ Aerolínea desactivada.");
                        }
                        break;
                    case 4:
                        listarAerolineasSimple();
                        modificarPreciosAerolinea();
                        break;
                    case 5: volver = true; break;
                    default: System.out.println("❌ Opción no válida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("❌ Error: Ingrese un número.");
                scanner.nextLine();
            }
        }
    }

    private void listarAerolineasSimple() {
        System.out.println("\n--- Aerolíneas Registradas ---");
        for (Aerolinea a : gestorAerolineas.listar()) {
            System.out.println(a);
        }
        System.out.println("------------------------------");
    }

    private void modificarPreciosAerolinea() {

        Aerolinea a = pedirCodigoAerolineaRobusto("MODIFICAR PRECIOS");
        if (a == null) return; // Canceló

        System.out.println("--- Precios Actuales de " + a.getNombre() + " ---");
        System.out.println("CarryOn: $" + a.getCostoCarryOn());
        System.out.println("Despachado: $" + a.getCostoEquipajeDespachado());
        System.out.println("Especial: $" + a.getCostoEquipajeEspecial());
        System.out.println("--------------------------------------");

        double pCarry = pedirDouble("Nuevo precio CarryOn");
        if (pCarry == -1) return;

        double pDesp = pedirDouble("Nuevo precio Despachado");
        if (pDesp == -1) return;

        double pEsp = pedirDouble("Nuevo precio Especial");
        if (pEsp == -1) return;

        gestorAerolineas.modificarPreciosEquipaje(a.getCodigo(), pCarry, pDesp, pEsp);
        System.out.println("✅ Precios actualizados.");
    }

    // ========================================================================
    // 4. REPORTE DE RESERVAS
    // ========================================================================

    private void flujoReporteReservas() {
        System.out.println("\n══════════════════════════════════════════════════════");
        System.out.println("          REPORTE DE TODAS LAS RESERVAS");
        System.out.println("══════════════════════════════════════════════════════");

        List<Reserva> reservas = gestorReservas.listarTodasLasReservas();

        if (reservas.isEmpty()) {
            System.out.println("   (No hay reservas en el sistema)");
        } else {
            for (Reserva r : reservas) {
                imprimirReservaDetallada(r);
            }
        }
        System.out.println("══════════════════════════════════════════════════════");
        System.out.println("Total de Reservas: " + reservas.size());
        System.out.println("Presione Enter para volver...");
        scanner.nextLine();
    }

    private void imprimirReservaDetallada(Reserva r) {
        System.out.println("📂 RESERVA: " + r.getIdReserva() + "  [" + r.getEstado() + "]");
        System.out.println("   📅 Fecha Compra: " + r.getFechaCreacion());

        if (r.getCliente() != null) {
            System.out.println("   👤 Cliente: " + r.getCliente().getNombre() + " (DNI: " + r.getCliente().getDNI() + ")");
        } else {
            System.out.println("   👤 Cliente: ID " + r.getClienteIdTemporal() + " (Datos no cargados)");
        }

        System.out.println("   💰 Costo Total: $" + r.getCostoTotal());
        System.out.println("   --- Itinerarios ---");

        if (r.getItinerarios() != null) {
            int i = 1;
            for (Itinerario it : r.getItinerarios()) {
                System.out.println("   Viaje " + i++ + ":");
                for (Vuelo v : it.getSegmentos()) {
                    System.out.println("     ✈️ " + v.getOrigen().getCodigoIATA() + " -> " + v.getDestino().getCodigoIATA() +
                            " (" + v.getAerolinea().getNombre() + ") | " + v.getFechaHoraSalida().toString().replace("T", " "));
                }
            }
        }

        System.out.println("   --- Pasajeros y Detalle ---");
        if (r.getPasajes() != null) {
            for (Pasaje p : r.getPasajes()) {
                System.out.println("     🙍‍♂️ " + p.getPasajero().getNombreCompleto() + " (" + p.getPasajero().getDNI() + ")");
                System.out.println("        Tramo: " + p.getVuelo().getOrigen().getCodigoIATA() + "-" + p.getVuelo().getDestino().getCodigoIATA());
                System.out.println("        Asiento: " + p.getAsiento() + " | Clase: " + p.getClase());

                List<Equipaje> equipajes = p.getEquipajeContratado();
                if (equipajes == null || equipajes.isEmpty()) {
                    System.out.println("        🎒 Equipaje: Ninguno");
                } else {
                    System.out.print("        🎒 Equipaje: ");
                    int mano = 0, carry = 0, desp = 0, esp = 0;
                    for(Equipaje e : equipajes) {
                        if(e instanceof EquipajeDeMano) mano++;
                        else if(e instanceof CarryOn) carry++;
                        else if(e instanceof EquipajeDespachado) desp++;
                        else if(e instanceof EquipajeEspecial) esp++;
                    }
                    if(mano > 0) System.out.print("[" + mano + "x Mano] ");
                    if(carry > 0) System.out.print("[" + carry + "x CarryOn] ");
                    if(desp > 0) System.out.print("[" + desp + "x Despachado] ");
                    if(esp > 0) System.out.print("[" + esp + "x Especial]");
                    System.out.println();
                }
            }
        }
        System.out.println("------------------------------------------------------");
    }

    // ========================================================================
    // HELPERS GENERALES
    // ========================================================================

    private Aeropuerto pedirAeropuerto(String tipo) {
        while (true) {
            System.out.print("Ingrese código IATA " + tipo + " (o '0' para cancelar): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("0")) return null;

            Aeropuerto a = gestorAeropuertos.consulta(input);
            if (a != null) return a;
            System.out.println("❌ Error: No existe ningún aeropuerto con el código '" + input + "'.");
        }
    }

    /**
     * Helper ROBUSTO para pedir y validar el código de una Aerolínea.
     */
    private Aerolinea pedirCodigoAerolineaRobusto(String accion) {
        while (true) {
            System.out.print("Ingrese Código Aerolínea para " + accion + " (o '0' para cancelar): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("0")) return null; // Opción de cancelación

            Aerolinea a = gestorAerolineas.consulta(input);
            if (a != null) {
                return a; // Éxito. La devuelve
            }
            System.out.println("❌ Error: No existe aerolínea con código '" + input + "'. Intente de nuevo.");
        }
    }

    // Helper para Crear Vuelo (advierte si está inactiva)
    private Aerolinea pedirAerolinea() {
        while (true) {
            System.out.print("Ingrese código de Aerolínea (o '0' para cancelar): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("0")) return null;

            Aerolinea a = gestorAerolineas.consulta(input);
            if (a != null) {
                if (!a.isActiva()) {
                    System.out.print("⚠️ Advertencia: Esta aerolínea está INACTIVA. ¿Usar igual? (S/N): ");
                    if (!scanner.nextLine().equalsIgnoreCase("S")) continue;
                }
                return a;
            }
            System.out.println("❌ Aerolínea no encontrada.");
        }
    }

    private Avion pedirAvionDeFlota(Aerolinea aerolinea) {
        List<Avion> flota = aerolinea.getFlotaDeAviones();
        if (flota.isEmpty()) {
            System.out.println("❌ Esta aerolínea no tiene aviones cargados.");
            return null;
        }

        System.out.println("\n--- Flota de " + aerolinea.getNombre() + " ---");
        for (Avion av : flota) System.out.println("- " + av);

        while (true) {
            System.out.print("Ingrese matrícula del avión (o '0' para cancelar): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("0")) return null;

            for (Avion av : flota) {
                if (av.getMatricula().equalsIgnoreCase(input)) return av;
            }
            System.out.println("❌ Avión no encontrado en esta flota.");
        }
    }

    private LocalDateTime pedirFechaHora(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (AAAA-MM-DDTHH:MM): ");
            String input = scanner.nextLine().trim();
            if (input.equals("0")) return null;

            try {
                return LocalDateTime.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("❌ Formato inválido. Ejemplo: 2025-12-25T10:30");
            }
        }
    }

    private double pedirDouble(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (o -1 para cancelar): ");
            try {
                double valor = Double.parseDouble(scanner.nextLine());
                if (valor == -1) return -1;
                if (valor < 0) System.out.println("❌ El valor no puede ser negativo.");
                else return valor;
            } catch (NumberFormatException e) {
                System.out.println("❌ Debe ingresar un número válido.");
            }
        }
    }

    private boolean pedirBooleano(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (S/N): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("S")) return true;
            if (input.equals("N")) return false;
            System.out.println("❌ Ingrese S o N.");
        }
    }
}