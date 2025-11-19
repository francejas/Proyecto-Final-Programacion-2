package Persistencia;


import Entidades.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * CLASE TEMPORAL para "sembrar" (seed) los archivos JSON con datos maestros.
 * Se ejecuta UNA SOLA VEZ para crear los archivos.
 * Luego debe ser eliminada o comentada para no sobrescribir datos nuevos.
 */
public class PobladorDeDatos {

    public static void main(String[] args) {

        System.out.println("--- INICIANDO POBLADO DE DATOS (SEEDING) ---");

        // --- 1. Crear los Gestores de JSON (Managers) ---
        JsonManagerAeropuertos jm_aeropuertos = new JsonManagerAeropuertos();
        JsonManagerAerolineas jm_aerolineas = new JsonManagerAerolineas();
        JsonManagerUsuarios jm_usuarios = new JsonManagerUsuarios();
        JsonManagerVuelos jm_vuelos = new JsonManagerVuelos();
        // (Reservas se deja vacío para que lo llene la app con el uso real)


        // --- 2. Crear los Objetos en Java ---

        // ======================================================================
        // AEROPUERTOS
        // ======================================================================
        System.out.println("Creando Aeropuertos...");
        List<Aeropuerto> aeropuertos = new ArrayList<>();

        // IMPORTANTE: Declaramos variables para los aeropuertos que usaremos en Vuelos
        Aeropuerto eze = new Aeropuerto("EZE", "Aeropuerto Internacional Ministro Pistarini", "Ezeiza");
        Aeropuerto aep = new Aeropuerto("AEP", "Aeroparque Jorge Newbery", "Buenos Aires");
        Aeropuerto cor = new Aeropuerto("COR", "Aeropuerto Internacional Ingeniero Taravella", "Córdoba");
        Aeropuerto mdq = new Aeropuerto("MDQ", "Aeropuerto Internacional Astor Piazzolla", "Mar del Plata");
        Aeropuerto brc = new Aeropuerto("BRC", "Aeropuerto Internacional Teniente Luis Candelaria", "Bariloche");
        Aeropuerto mdz = new Aeropuerto("MDZ", "Aeropuerto Internacional El Plumerillo", "Mendoza");
        Aeropuerto sla = new Aeropuerto("SLA", "Aeropuerto Internacional Martín Miguel de Güemes", "Salta");
        Aeropuerto igr = new Aeropuerto("IGR", "Aeropuerto Internacional de Puerto Iguazú", "Puerto Iguazú");
        Aeropuerto ush = new Aeropuerto("USH", "Aeropuerto Internacional Malvinas Argentinas", "Ushuaia");

        // Agregamos las variables a la lista
        aeropuertos.add(eze);
        aeropuertos.add(aep);
        aeropuertos.add(cor);
        aeropuertos.add(mdq);
        aeropuertos.add(brc);
        aeropuertos.add(mdz);
        aeropuertos.add(sla);
        aeropuertos.add(igr);
        aeropuertos.add(ush);

        // Agregamos "relleno" (estos no se usan en vuelos de prueba, pueden ser anónimos)
        aeropuertos.add(new Aeropuerto("FTE", "Aeropuerto Comandante Armando Tola", "El Calafate"));
        aeropuertos.add(new Aeropuerto("REL", "Aeropuerto Almirante Marco Andrés Zar", "Trelew"));
        aeropuertos.add(new Aeropuerto("NQN", "Aeropuerto Internacional Presidente Perón", "Neuquén"));
        aeropuertos.add(new Aeropuerto("TUC", "Aeropuerto Internacional Teniente Benjamín Matienzo", "San Miguel de Tucumán"));
        aeropuertos.add(new Aeropuerto("ROS", "Aeropuerto Internacional Rosario Islas Malvinas", "Rosario"));
        aeropuertos.add(new Aeropuerto("BHI", "Aeropuerto Comandante Espora", "Bahía Blanca"));

        System.out.println("Total de aeropuertos creados: " + aeropuertos.size());


        // ======================================================================
        // AEROLINEAS Y SU FLOTA (AVIONES)
        // ======================================================================
        System.out.println("Creando Aerolíneas y asignando Aviones...");
        List<Aerolinea> aerolineas = new ArrayList<>();

        // --- 1. Aerolíneas Argentinas (AR) ---
        Aerolinea ar = new Aerolinea("AR", "Aerolíneas Argentinas", 50.0, 70.0, 150.0);
        // Variables de aviones para usar en vuelos
        Avion avion1_AR = new Avion("LV-FNI", "Airbus A330", 240, 24);
        Avion avion2_AR = new Avion("LV-GKE", "Boeing 737-800", 150, 12);
        Avion avion3_AR = new Avion("LV-HQB", "Airbus A330-200", 245, 24);

        ar.agregarAvionAlaFlota(avion1_AR);
        ar.agregarAvionAlaFlota(avion2_AR);
        ar.agregarAvionAlaFlota(avion3_AR);
        ar.agregarAvionAlaFlota(new Avion("LV-KFW", "Boeing 737-MAX 8", 160, 12));
        ar.agregarAvionAlaFlota(new Avion("LV-JYR", "Embraer 190", 90, 6));
        aerolineas.add(ar);

        // --- 2. JetSMART (WJ) ---
        Aerolinea wj = new Aerolinea("WJ", "JetSMART", 65.0, 80.0, 180.0);
        // Variables de aviones para usar en vuelos
        Avion avion1_WJ = new Avion("LV-KJA", "Airbus A320", 186, 0); // Solo Economy
        Avion avion2_WJ = new Avion("LV-KJD", "Airbus A320neo", 186, 0);

        wj.agregarAvionAlaFlota(avion1_WJ);
        wj.agregarAvionAlaFlota(avion2_WJ);
        aerolineas.add(wj);

        // --- 3. Flybondi (FO) ---
        Aerolinea fo = new Aerolinea("FO", "Flybondi", 60.0, 75.0, 175.0);
        Avion avion1_FO = new Avion("LV-KAY", "Boeing 737-800", 189, 0);
        fo.agregarAvionAlaFlota(avion1_FO);
        fo.agregarAvionAlaFlota(new Avion("LV-KEF", "Boeing 737-800", 189, 0));
        aerolineas.add(fo);

        // --- 4. LADE (LD) ---
        Aerolinea ld = new Aerolinea("LD", "LADE", 70.0, 90.0, 200.0);
        ld.agregarAvionAlaFlota(new Avion("LV-CDA", "Fokker F28", 70, 8));
        aerolineas.add(ld);

        // --- 5. Andes (AN) ---
        Aerolinea an = new Aerolinea("AN", "Andes Líneas Aéreas", 55.0, 70.0, 160.0);
        an.agregarAvionAlaFlota(new Avion("LV-WGN", "Boeing 737-800", 180, 15));
        aerolineas.add(an);

        System.out.println("Total de aerolíneas creadas: " + aerolineas.size());


        // ======================================================================
        // VUELOS
        // ======================================================================
        System.out.println("Creando Vuelos...");
        List<Vuelo> vuelos = new ArrayList<>();

        // 1. EZE -> COR (AR) - Ida
        Vuelo v1 = new Vuelo(eze, cor,
                LocalDateTime.parse("2025-12-15T10:30:00"),
                LocalDateTime.parse("2025-12-15T12:00:00"),
                ar, avion1_AR, 150.0, true, true);
        vuelos.add(v1);

        // 2. COR -> EZE (AR) - Vuelta
        Vuelo v2 = new Vuelo(cor, eze,
                LocalDateTime.parse("2025-12-20T18:00:00"),
                LocalDateTime.parse("2025-12-20T19:30:00"),
                ar, avion1_AR, 150.0, true, true);
        vuelos.add(v2);

        // 3. AEP -> MDQ (AR) - Escapada
        Vuelo v3 = new Vuelo(aep, mdq,
                LocalDateTime.parse("2025-12-31T14:00:00"),
                LocalDateTime.parse("2025-12-31T15:00:00"),
                ar, avion2_AR, 80.0, false, true);
        vuelos.add(v3);

        // 4. AEP -> COR (WJ) - Low Cost
        Vuelo v4 = new Vuelo(aep, cor,
                LocalDateTime.parse("2025-12-15T08:00:00"),
                LocalDateTime.parse("2025-12-15T09:30:00"),
                wj, avion1_WJ, 90.0, false, false); // Sin comida, CarryOn pago
        vuelos.add(v4);

        // 5. COR -> AEP (WJ) - Vuelta Low Cost
        Vuelo v5 = new Vuelo(cor, aep,
                LocalDateTime.parse("2025-12-20T20:00:00"),
                LocalDateTime.parse("2025-12-20T21:30:00"),
                wj, avion1_WJ, 90.0, false, false);
        vuelos.add(v5);

        // 6. EZE -> MDZ (FO) - Flybondi a Mendoza
        Vuelo v6 = new Vuelo(eze, mdz,
                LocalDateTime.parse("2025-12-16T06:00:00"),
                LocalDateTime.parse("2025-12-16T08:00:00"),
                fo, avion1_FO, 75.0, false, false);
        vuelos.add(v6);

        // 7. EZE -> USH (AR) - Vuelo Largo a Ushuaia
        Vuelo v7 = new Vuelo(eze, ush,
                LocalDateTime.parse("2025-12-18T09:00:00"),
                LocalDateTime.parse("2025-12-18T12:40:00"),
                ar, avion3_AR, 200.0, true, true);
        vuelos.add(v7);

        // 8. USH -> EZE (AR) - Vuelta Ushuaia
        Vuelo v8 = new Vuelo(ush, eze,
                LocalDateTime.parse("2025-12-25T14:00:00"),
                LocalDateTime.parse("2025-12-25T17:40:00"),
                ar, avion3_AR, 200.0, true, true);
        vuelos.add(v8);

        // 9. AEP -> IGR (WJ) - Cataratas
        Vuelo v9 = new Vuelo(aep, igr,
                LocalDateTime.parse("2025-12-10T11:00:00"),
                LocalDateTime.parse("2025-12-10T12:50:00"),
                wj, avion2_WJ, 110.0, false, false);
        vuelos.add(v9);

        // 10. IGR -> AEP (WJ) - Vuelta Cataratas
        Vuelo v10 = new Vuelo(igr, aep,
                LocalDateTime.parse("2025-12-17T16:00:00"),
                LocalDateTime.parse("2025-12-17T17:50:00"),
                wj, avion2_WJ, 110.0, false, false);
        vuelos.add(v10);

        // ... podemos agregar mas vuelos de prueba

        System.out.println("Total de vuelos creados: " + vuelos.size());


        // ======================================================================
        // USUARIOS
        // ======================================================================
        System.out.println("Creando Usuarios...");
        List<Usuario> usuarios = new ArrayList<>();

        // Admin
        usuarios.add(new Administrador("Admin Principal", "admin@aeropro.com", "AdminPass1!", true));

        // Clientes
        usuarios.add(new Cliente("Juan Perez", "juan@mail.com", "JuanPass1!", true, 500, "25111222", LocalDate.parse("1980-05-15")));
        usuarios.add(new Cliente("Maria Garcia", "maria@mail.com", "MariaPass1!", true, 1200, "30333444", LocalDate.parse("1990-10-20")));
        usuarios.add(new Cliente("Carlos Lopez", "carlos@mail.com", "CarlosPass1!", true, 0, "32555666", LocalDate.parse("1995-02-10")));
        usuarios.add(new Cliente("Ana Martinez", "ana@mail.com", "AnaPass1!", true, 200, "28777888", LocalDate.parse("1985-11-30")));

        System.out.println("Total de usuarios creados: " + usuarios.size());


        // --- 3. Guardar todo en los archivos ---
        System.out.println("Guardando archivos JSON...");

        jm_aeropuertos.guardarLista(aeropuertos);
        jm_aerolineas.guardarLista(aerolineas); // Guarda aviones anidados
        jm_usuarios.guardarLista(usuarios);
        jm_vuelos.guardarLista(vuelos);         // Guarda vuelos

        System.out.println("--- ¡POBLADO DE DATOS COMPLETADO! ---");
        System.out.println("Verificá que existan 4 archivos .json en la carpeta raíz.");
    }
}