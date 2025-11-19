package Vistas;

import Entidades.*;
import Servicios.*;
import Excepciones.*;
import Enum.TipoClase;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuCliente {

    private Usuario clienteLogueado;
    private Scanner scanner;
    // Gestores necesarios
    private GestorVuelos gestorVuelos;
    private GestorReservas gestorReservas;
    private GestorUsuarios gestorUsuarios;
    private GestorAeropuertos gestorAeropuertos; // Necesario para validaciones o info extra

    public MenuCliente(Usuario usuario, Scanner scanner, GestorVuelos gv,
                       GestorReservas gr, GestorUsuarios gu, GestorAeropuertos gap) {
        this.clienteLogueado = usuario;
        this.scanner = scanner;
        this.gestorVuelos = gv;
        this.gestorReservas = gr;
        this.gestorUsuarios = gu;
        this.gestorAeropuertos = gap;
    }

    public void mostrar() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n══════════════════════════════════════════════════");
            System.out.println("   BIENVENIDO: " + clienteLogueado.getNombre().toUpperCase());
            System.out.println("══════════════════════════════════════════════════");
            System.out.println("1. 🔎 Buscar Vuelos y Reservar");
            System.out.println("2. 🎫 Mis Reservas (Ver, Modificar, Cancelar)");
            System.out.println("3. 👤 Mi Perfil");
            System.out.println("4. ✈️  Consultar Mis Millas");
            System.out.println("5. 🚪 Cerrar Sesión");
            System.out.println("══════════════════════════════════════════════════");

            int opcion = pedirIntRango("Seleccione una opción", 1, 5);

            switch (opcion) {
                case 1: flujoBuscarVuelo(); break;
                case 2: flujoMisReservas(); break;
                case 3: flujoMiPerfil(); break;
                case 4: consultarMillas(); break;
                case 5: salir = true; break;
            }
        }
    }

    // ========================================================================
    // 1. FLUJO DE BÚSQUEDA Y RESERVA
    // ========================================================================

    private void flujoBuscarVuelo() {
        System.out.println("\n--- 🔎 BUSCAR VUELOS ---");
        System.out.println("1. Solo Ida");
        System.out.println("2. Ida y Vuelta");
        System.out.println("0. Volver");

        int tipoViaje = pedirIntRango("Opción", 0, 2);
        if (tipoViaje == 0) return;

        try {
            // 1. Pedir datos de búsqueda (Admite ciudad o código)
            System.out.print("Origen (Ciudad o Código, ej: 'Buenos Aires'): ");
            String origen = scanner.nextLine().trim();
            if (origen.equals("0")) return;

            System.out.print("Destino (Ciudad o Código, ej: 'Miami'): ");
            String destino = scanner.nextLine().trim();
            if (destino.equals("0")) return;

            LocalDate fechaIda = pedirFecha("Fecha Ida");
            if (fechaIda == null) return;

            System.out.println("\n⏳ Buscando mejores opciones...");

            // Llama al gestor que ahora soporta búsqueda amplia (por ciudad)
            List<Itinerario> resultadosIda = gestorVuelos.buscarItinerarios(origen, destino, fechaIda);

            // Selección de Ida
            Itinerario itinerarioIda = seleccionarItinerario(resultadosIda);
            if (itinerarioIda == null) return; // Usuario canceló la selección

            // 2. Buscar Vuelta (si corresponde)
            Itinerario itinerarioVuelta = null;
            if (tipoViaje == 2) {
                LocalDate fechaVuelta = pedirFecha("Fecha Vuelta");
                if (fechaVuelta == null) return;

                // Validación: Vuelta no puede ser antes que Ida
                while (fechaVuelta.isBefore(fechaIda)) {
                    System.out.println("❌ La fecha de vuelta no puede ser anterior a la ida.");
                    fechaVuelta = pedirFecha("Fecha Vuelta");
                    if (fechaVuelta == null) return;
                }

                System.out.println("\n⏳ Buscando opciones de VUELTA...");
                // Invertimos origen y destino para la vuelta
                List<Itinerario> resultadosVuelta = gestorVuelos.buscarItinerarios(destino, origen, fechaVuelta);
                itinerarioVuelta = seleccionarItinerario(resultadosVuelta);
                if (itinerarioVuelta == null) return;
            }

            // 3. Cargar Pasajeros (Autocompletado Inteligente)
            List<Pasajero> pasajeros = cargarDatosPasajeros();
            if (pasajeros == null) return;

            // 4. Configurar Reserva (Asientos y Equipaje)
            List<Itinerario> itinerariosReserva = new ArrayList<>();
            itinerariosReserva.add(itinerarioIda);
            if (itinerarioVuelta != null) itinerariosReserva.add(itinerarioVuelta);

            List<Pasaje> todosLosPasajes = configurarDetallesViaje(itinerariosReserva, pasajeros);
            if (todosLosPasajes == null) return; // Cancelación durante la configuración

            // 5. Confirmación Final y Pago
            confirmarYCrearReserva(itinerariosReserva, todosLosPasajes);

        } catch (ItinerarioNoEncontradoException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Ocurrió un error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Itinerario seleccionarItinerario(List<Itinerario> itinerarios) {
        System.out.println("\n--- OPCIONES DISPONIBLES ---");
        for (int i = 0; i < itinerarios.size(); i++) {
            Itinerario it = itinerarios.get(i);
            Duration duracion = it.getDuracionTotal();


            System.out.println("------------------------------------------------");
            System.out.println((i + 1) + ". 🛫 " + it.getOrigenFinal().getCodigoIATA() + " ➔ 🛬 " + it.getDestinoFinal().getCodigoIATA());
            System.out.println("    Aerolínea: " + it.getSegmentos().get(0).getAerolinea().getNombre());
            System.out.println("    Escalas: " + (it.getCantidadEscalas() == 0 ? "Directo" : it.getCantidadEscalas()));
            System.out.println("    Duración: " + duracion.toHours() + "h " + (duracion.toMinutes() % 60) + "m");
            System.out.println("    💰 Precio Base por persona: $" + it.getPrecioTotal());
        }
        System.out.println("------------------------------------------------");

        int op = pedirIntRango("Seleccione un vuelo (0 para cancelar)", 0, itinerarios.size());
        if (op == 0) return null;
        return itinerarios.get(op - 1);
    }

    private List<Pasajero> cargarDatosPasajeros() {
        System.out.println("\n--- DATOS DE PASAJEROS ---");
        int cant = pedirIntRango("Cantidad de pasajeros (1-9)", 1, 9);

        List<Pasajero> lista = new ArrayList<>();

        System.out.print("¿Usted (" + clienteLogueado.getNombre() + ") es uno de los pasajeros? (S/N): ");
        String respuesta = scanner.nextLine().trim().toUpperCase();
        boolean usuarioViaja = respuesta.equals("S");

        for (int i = 0; i < cant; i++) {
            System.out.println("\n>> Pasajero " + (i + 1));

            if (i == 0 && usuarioViaja) {
                // Autocompletado usando los datos de la cuenta
                Cliente c = (Cliente) clienteLogueado;
                System.out.println("   ✅ Usando datos de su cuenta: " + c.getNombre() + " (DNI: " + c.getDNI() + ")");
                lista.add(new Pasajero(c.getNombre(), c.getDNI(), c.getFechaDeNacimiento()));
            } else {
                System.out.print("   Nombre Completo: ");
                String nombre = scanner.nextLine();
                System.out.print("   DNI: ");
                String dni = scanner.nextLine();
                LocalDate fecha = pedirFechaNacimiento("   Fecha Nacimiento");
                if (fecha == null) return null; // Cancelar

                lista.add(new Pasajero(nombre, dni, fecha));
            }
        }
        return lista;
    }

    private List<Pasaje> configurarDetallesViaje(List<Itinerario> itinerarios, List<Pasajero> pasajeros) {
        List<Pasaje> pasajes = new ArrayList<>();

        for (Itinerario itinerario : itinerarios) {
            System.out.println("\n══════════════════════════════════════════════════");
            System.out.println(" CONFIGURANDO VIAJE: " + itinerario.getOrigenFinal().getCiudad() +
                    " ➔ " + itinerario.getDestinoFinal().getCiudad());
            System.out.println("══════════════════════════════════════════════════");

            for (Vuelo vuelo : itinerario.getSegmentos()) {
                System.out.println("\n✈️  TRAMO: " + vuelo.getOrigen().getCodigoIATA() + " -> " +
                        vuelo.getDestino().getCodigoIATA() + " (" + vuelo.getAerolinea().getNombre() + ")");

                for (Pasajero pax : pasajeros) {
                    System.out.println("\n   👤 Configurando para: " + pax.getNombreCompleto());

                    // 1. Clase
                    System.out.println("      Seleccione Clase:");
                    System.out.println("      1. Economy ($" + vuelo.getPrecioBase() + ")");
                    System.out.println("      2. Business ($" + (vuelo.getPrecioBase() * 1.8) + ")");
                    int opClase = pedirIntRango("      Opción", 1, 2);
                    TipoClase clase = (opClase == 2) ? TipoClase.BUSINESS : TipoClase.ECONOMY;

                    // 2. Asiento
                    String asiento = elegirAsiento(vuelo);
                    if (asiento == null) return null; // Cancelar todo

                    Pasaje pasaje = new Pasaje(pax, vuelo, asiento, clase);

                    // 3. Equipaje
                    agregarEquipajeAPasaje(pasaje, vuelo);

                    pasajes.add(pasaje);
                }
            }
        }
        return pasajes;
    }

    private String elegirAsiento(Vuelo vuelo) {
        // 1. Mostrar el mapa visual antes de pedir el input
        imprimirMapaDeAsientos(vuelo);

        // 2. Bucle de selección
        while (true) {
            System.out.print("      Ingrese Asiento (o '0' para cancelar): ");
            String asiento = scanner.nextLine().toUpperCase().trim();

            if (asiento.equals("0")) return null;

            if (vuelo.isAsientoLibre(asiento)) {
                // Ocupamos el asiento inmediatamente en el vuelo maestro para evitar errores
                vuelo.ocuparAsiento(asiento);
                return asiento;
            } else {
                System.out.println("      ❌ Asiento ocupado o inexistente. Pruebe otro o ingrese '0'.");
            }
        }
    }

    /**
     * Genera una representación en caracteres del mapa de asientos del vuelo.
     * Muestra [ ] para asientos libres y [O] para ocupados.
     */
    private void imprimirMapaDeAsientos(Vuelo vuelo) {
        // Obtenemos el mapa de disponibilidad del vuelo
        java.util.Map<String, Boolean> asientos = vuelo.getAsientosDisponibles();
        char[] columnas = {'A', 'B', 'C', 'D', 'E', 'F'};
        int maxFila = 0;

        // 1. Encontrar la fila máxima para el bucle (necesario para saber hasta dónde iterar)
        for (String codigo : asientos.keySet()) {
            try {
                //  el formato es siempre [Número][Letra]
                int fila = Integer.parseInt(codigo.substring(0, codigo.length() - 1));
                if (fila > maxFila) maxFila = fila;
            } catch (NumberFormatException e) {
                // Ignorar asientos mal formateados si los hay
            }
        }

        System.out.println("\n        --- MAPA DE ASIENTOS DEL AVIÓN ---");
        System.out.println("        [O]: Ocupado | [ ]: Libre");
        System.out.printf("        %-5s %-4s %-4s %-4s %-4s %-4s %-4s%n", "FILA", "A", "B", "C", "D", "E", "F");
        System.out.println("        -----------------------------------------");

        // 2. Imprimir fila por fila
        for (int fila = 1; fila <= maxFila; fila++) {
            System.out.printf("        %-5d", fila); // Imprimir Número de fila

            for (char col : columnas) {
                String codigo = fila + "" + col;

                if (asientos.containsKey(codigo)) {
                    // Si la clave existe, verificamos el estado (true = Libre -> [ ], false = Ocupado -> [O])
                    String estado = asientos.get(codigo) ? "[ ]" : "[O]";
                    System.out.printf(" %-3s", estado);
                } else {
                    // Si la clave no existe (ej. no hay fila F en cabina ejecutiva), imprimimos vacío
                    System.out.printf(" %-3s", " - ");
                }
            }
            System.out.println(); // Nueva línea para la siguiente fila
        }
        System.out.println("        -----------------------------------------");
    }

    private void agregarEquipajeAPasaje(Pasaje pasaje, Vuelo vuelo) {
        boolean seguir = true;
        Aerolinea aero = vuelo.getAerolinea();

        while (seguir) {
            System.out.println("\n      --- AGREGAR EQUIPAJE ---");
            System.out.println("      1. Artículo Personal (Gratis)");

            String pCarry = vuelo.isCarryOnGratis() ? "GRATIS" : "$" + aero.getCostoCarryOn();
            System.out.println("      2. Carry On [" + pCarry + "]");
            System.out.println("      3. Despachado [$" + aero.getCostoEquipajeDespachado() + "]");
            System.out.println("      4. Especial [$" + aero.getCostoEquipajeEspecial() + "]");
            System.out.println("      5. Finalizar carga de equipaje");

            int op = pedirIntRango("      Opción", 1, 5);

            // Lógica de PRECIO CONGELADO: Pasamos el precio actual al constructor
            switch (op) {
                case 1:
                    pasaje.agregarEquipaje(new EquipajeDeMano(0.0));
                    System.out.println("      ✅ Agregado.");
                    break;
                case 2:
                    double costoC = vuelo.isCarryOnGratis() ? 0.0 : aero.getCostoCarryOn();
                    pasaje.agregarEquipaje(new CarryOn(costoC));
                    System.out.println("      ✅ Agregado ($" + costoC + ").");
                    break;
                case 3:
                    pasaje.agregarEquipaje(new EquipajeDespachado(aero.getCostoEquipajeDespachado()));
                    System.out.println("      ✅ Agregado ($" + aero.getCostoEquipajeDespachado() + ").");
                    break;
                case 4:
                    pasaje.agregarEquipaje(new EquipajeEspecial(aero.getCostoEquipajeEspecial()));
                    System.out.println("      ✅ Agregado ($" + aero.getCostoEquipajeEspecial() + ").");
                    break;
                case 5:
                    seguir = false;
                    break;
            }
        }
    }

    private void confirmarYCrearReserva(List<Itinerario> itinerarios, List<Pasaje> pasajes) {
        Cliente cliente = (Cliente) clienteLogueado;
        Reserva nuevaReserva = new Reserva(cliente, itinerarios, pasajes);

        System.out.println("\n══════════════════════════════════════════════════");
        System.out.println("              RESUMEN FINAL DE COMPRA             ");
        System.out.println("══════════════════════════════════════════════════");
        System.out.println("Itinerarios: " + itinerarios.size());
        System.out.println("Pasajeros: " + (pasajes.size() / itinerarios.size()));
        System.out.printf("COSTO TOTAL: $%.2f%n", nuevaReserva.getCostoTotal());
        System.out.println("Sus Millas Acumuladas: " + cliente.getMillas());

        System.out.print("\n¿Confirmar y Pagar? (S/N): ");
        if (scanner.nextLine().equalsIgnoreCase("S")) {
            gestorReservas.crearReserva(nuevaReserva);
            System.out.println("\n✅ ¡RESERVA CREADA EXITOSAMENTE!");
            System.out.println("Código de Reserva: " + nuevaReserva.getIdReserva());
        } else {
            System.out.println("\n❌ Operación cancelada.");
        }
        System.out.println("Presione Enter para volver...");
        scanner.nextLine();
    }

    // ========================================================================
    // 2. FLUJO MIS RESERVAS (Detalles, Modificar, Cancelar)
    // ========================================================================

    private void flujoMisReservas() {
        List<Reserva> misReservas = gestorReservas.buscarReservasPorCliente(clienteLogueado.getId());

        if (misReservas.isEmpty()) {
            System.out.println("\nℹ️  No tiene reservas activas.");
            System.out.println("Presione Enter para volver...");
            scanner.nextLine();
            return;
        }

        System.out.println("\n════════════════════ MIS RESERVAS ════════════════════");
        System.out.printf("%-12s | %-12s | %-25s | %-10s%n", "CÓDIGO", "FECHA", "RUTA", "ESTADO");
        System.out.println("------------------------------------------------------------------");

        for (Reserva r : misReservas) {
            Itinerario ida = r.getItinerarios().get(0);
            String ruta = ida.getOrigenFinal().getCodigoIATA() + " -> " + ida.getDestinoFinal().getCodigoIATA();
            if (r.getItinerarios().size() > 1) ruta += " (I/V)";

            System.out.printf("%-12s | %-12s | %-25s | %-10s%n",
                    r.getIdReserva(), r.getFechaCreacion(), ruta, r.getEstado());
        }
        System.out.println("══════════════════════════════════════════════════════════");

        System.out.print("Ingrese el CÓDIGO de reserva (o '0' volver): ");
        String id = scanner.nextLine().trim().toUpperCase();
        if (id.equals("0")) return;

        Reserva reservaSeleccionada = gestorReservas.buscarReservaPorId(id);

        if (reservaSeleccionada != null && reservaSeleccionada.getCliente().getId() == clienteLogueado.getId()) {
            gestionarReservaIndividual(reservaSeleccionada);
        } else {
            System.out.println("❌ Reserva no encontrada.");
        }
    }

    private void gestionarReservaIndividual(Reserva reserva) {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- GESTIONANDO RESERVA " + reserva.getIdReserva() + " ---");
            System.out.println("1. ℹ️  Ver Detalles Completos");
            System.out.println("2. ✏️  Modificar Reserva");
            System.out.println("3. ❌ Cancelar Reserva");
            System.out.println("4. ↩️  Volver");

            int op = pedirIntRango("Opción", 1, 4);

            switch (op) {
                case 1: imprimirReservaDetallada(reserva); break;
                case 2: flujoModificarReserva(reserva); break;
                case 3:
                    System.out.print("¿Cancelar definitivamente? (S/N): ");
                    if (scanner.nextLine().equalsIgnoreCase("S")) {
                        gestorReservas.cancelarReserva(reserva.getIdReserva());
                        System.out.println("✅ Reserva cancelada.");
                        volver = true;
                    }
                    break;
                case 4: volver = true; break;
            }
        }
    }

    private void imprimirReservaDetallada(Reserva r) {
        System.out.println("\n--------------- DETALLE DE RESERVA ---------------");
        System.out.println("Código: " + r.getIdReserva() + " | Estado: " + r.getEstado());
        System.out.println("Costo Total: $" + r.getCostoTotal());

        int i = 1;
        for (Itinerario it : r.getItinerarios()) {
            System.out.println("\n>>> Viaje " + (i++) + ":");
            for (Vuelo v : it.getSegmentos()) {
                System.out.println("  ✈️ " + v.getOrigen().getCodigoIATA() + " -> " + v.getDestino().getCodigoIATA() +
                        " | " + v.getFechaHoraSalida().toString().replace("T", " "));
            }
        }
        System.out.println("\n>>> Pasajeros:");
        for (Pasaje p : r.getPasajes()) {
            System.out.println("  👤 " + p.getPasajero().getNombreCompleto() + " [" + p.getIdPasaje() + "]");
            System.out.println("     Asiento: " + p.getAsiento() + " | Clase: " + p.getClase());

            // Resumen de equipaje
            System.out.print("     Equipaje: ");
            if (p.getEquipajeContratado().isEmpty()) System.out.print("Ninguno");
            else {
                for (Equipaje e : p.getEquipajeContratado()) {
                    System.out.print("[" + e.getClass().getSimpleName().replace("Equipaje", "") + "] ");
                }
            }
            System.out.println();
        }
        System.out.println("--------------------------------------------------");
        System.out.println("Presione Enter...");
        scanner.nextLine();
    }

    private void flujoModificarReserva(Reserva reserva) {
        if (!reserva.isActiva()) {
            System.out.println("⚠️ No se puede modificar una reserva cancelada.");
            return;
        }

        System.out.println("\n--- MODIFICAR RESERVA ---");
        System.out.println("1. Agregar Equipaje Extra");
        System.out.println("2. Cambiar Asiento");
        System.out.println("3. Cancelar un Pasajero");
        System.out.println("4. Volver");

        int op = pedirIntRango("Opción", 1, 4);
        if (op == 4) return;

        System.out.print("Ingrese ID del Pasaje (ver en detalles) o '0' para cancelar: ");
        String idPasaje = scanner.nextLine();
        if (idPasaje.equals("0")) return;

        Pasaje pasaje = reserva.buscarPasaje(idPasaje);
        if (pasaje == null) {
            System.out.println("❌ Pasaje no encontrado.");
            return;
        }

        try {
            switch (op) {
                case 1: // Equipaje
                    System.out.println("   Seleccione tipo (1. CarryOn / 2. Despachado / 3. Especial): ");
                    int tipo = pedirIntRango("   Opción", 1, 3);

                    Aerolinea aero = pasaje.getVuelo().getAerolinea();
                    Equipaje nuevo = null;

                    // Lógica de precio congelado
                    switch (tipo) {
                        case 1:
                            // CarryOn (verificar si es gratis en el vuelo)
                            double costoC = pasaje.getVuelo().isCarryOnGratis() ? 0.0 : aero.getCostoCarryOn();
                            nuevo = new CarryOn(costoC);
                            break;
                        case 2:
                            nuevo = new EquipajeDespachado(aero.getCostoEquipajeDespachado());
                            break;
                        case 3:
                            nuevo = new EquipajeEspecial(aero.getCostoEquipajeEspecial());
                            break;
                    }

                    // Llamar al gestor con el objeto REAL
                    if (nuevo != null) {
                        gestorReservas.agregarEquipaje(reserva, pasaje, nuevo);
                        System.out.println("✅ Equipaje agregado. Nuevo total reserva: $" + reserva.getCostoTotal());
                    }
                    break;
                case 2: // Asiento
                    System.out.print("Nuevo Asiento: ");
                    String asiento = scanner.nextLine().toUpperCase();
                    gestorReservas.cambiarAsiento(reserva.getIdReserva(), idPasaje, asiento);
                    System.out.println("✅ Asiento cambiado.");
                    break;
                case 3: // Cancelar Pasaje
                    gestorReservas.cancelarPasaje(reserva.getIdReserva(), idPasaje);
                    System.out.println("✅ Pasajero cancelado.");
                    break;
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    // ========================================================================
    // 3. MI PERFIL
    // ========================================================================

    private void flujoMiPerfil() {
        System.out.println("\n--- MI PERFIL ---");
        System.out.println("1. Cambiar Contraseña");
        System.out.println("2. Modificar Email");
        System.out.println("3. Modificar Datos Personales (Nombre, DNI, Fecha Nac.)");
        System.out.println("4. Volver");

        int op = pedirIntRango("Opción", 1, 4);
        if (op == 4) return; // Volver

        try {
            if (op == 1) {
                // --- CAMBIAR CONTRASEÑA ---
                System.out.print("Contraseña Actual: ");
                String actual = scanner.nextLine();

                System.out.println(">> Ingrese NUEVA contraseña:");
                String nueva = pedirPasswordValida();
                if (nueva == null) return;

                gestorUsuarios.cambiarPassword(clienteLogueado.getId(), actual, nueva);
                System.out.println("✅ Contraseña actualizada correctamente.");

            } else if (op == 2) {
                // --- CAMBIAR EMAIL ---
                System.out.println(">> Ingrese el NUEVO email:");
                String nuevoEmail = pedirEmailValido();
                if (nuevoEmail == null) return;

                gestorUsuarios.modificarEmail(clienteLogueado.getId(), nuevoEmail);
                System.out.println("✅ Email actualizado. Use el nuevo email para ingresar.");

            } else if (op == 3) {
                // --- CAMBIAR DATOS PERSONALES  ---
                System.out.println(">> Modificar Datos Personales");

                // Pedimos los datos nuevos (pueden ser los mismos si el usuario los vuelve a escribir)
                System.out.print("Nuevo Nombre Completo: ");
                String nuevoNombre = scanner.nextLine();
                if (nuevoNombre.isEmpty()) {
                    System.out.println("⚠️ El nombre no puede estar vacío.");
                    return;
                }

                System.out.print("Nuevo DNI: ");
                String nuevoDNI = scanner.nextLine();
                if (nuevoDNI.isEmpty()) {
                    System.out.println("⚠️ El DNI no puede estar vacío.");
                    return;
                }


                LocalDate nuevaFecha = pedirFechaNacimiento("Nueva Fecha de Nacimiento");
                if (nuevaFecha == null) return; // Canceló

                // Llamamos al gestor
                gestorUsuarios.modificarDatosPersonales(clienteLogueado.getId(), nuevoNombre, nuevaFecha, nuevoDNI);

                System.out.println("✅ Datos personales actualizados exitosamente.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private void consultarMillas() {
        Cliente c = (Cliente) clienteLogueado;
        System.out.println("\n✈️  TIENES " + c.getMillas() + " MILLAS.");
        System.out.println("Presione Enter...");
        scanner.nextLine();
    }

    // ========================================================================
    // HELPERS ROBUSTOS
    // ========================================================================

    private int pedirIntRango(String mensaje, int min, int max) {
        while (true) {
            System.out.print(mensaje + " (" + min + "-" + max + "): ");
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input >= min && input <= max) return input;
                System.out.println("❌ Opción fuera de rango.");
            } catch (NumberFormatException e) {
                System.out.println("❌ Debe ingresar un número.");
            }
        }
    }

    private LocalDate pedirFecha(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (AAAA-MM-DD) o '0' cancelar: ");
            String input = scanner.nextLine().trim();
            if (input.equals("0")) return null;
            try {
                LocalDate fecha = LocalDate.parse(input);
                if (fecha.isBefore(LocalDate.now())) {
                    System.out.println("❌ La fecha no puede ser en el pasado.");
                } else {
                    return fecha;
                }
            } catch (DateTimeParseException e) {
                System.out.println("❌ Formato inválido.");
            }
        }
    }

    private LocalDate pedirFechaNacimiento(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (AAAA-MM-DD) o '0' cancelar: ");
            String input = scanner.nextLine().trim();
            if (input.equals("0")) return null;
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("❌ Formato inválido.");
            }
        }
    }

    private String pedirEmailValido() {
        while (true) {
            System.out.print("Email (o '0' cancelar): ");
            String email = scanner.nextLine().trim();
            if (email.equals("0")) return null;

            if (!email.contains("@")) {
                System.out.println("   ❌ Falta el '@'.");
            } else if (!email.contains(".")) {
                System.out.println("   ❌ Falta el dominio (ej: .com).");
            } else if (email.contains(" ")) {
                System.out.println("   ❌ No puede tener espacios.");
            } else {
                return email;
            }
        }
    }

    private String pedirPasswordValida() {
        while (true) {
            System.out.print("Contraseña (o '0' cancelar): ");
            String pass = scanner.nextLine();
            if (pass.equals("0")) return null;

            boolean largo = pass.length() >= 6;
            boolean mayuscula = pass.chars().anyMatch(Character::isUpperCase);
            boolean numero = pass.chars().anyMatch(Character::isDigit);

            if (!largo) System.out.println("   ❌ Mínimo 6 caracteres.");
            else if (!mayuscula) System.out.println("   ❌ Falta una Mayúscula.");
            else if (!numero) System.out.println("   ❌ Falta un Número.");
            else return pass;
        }
    }
}