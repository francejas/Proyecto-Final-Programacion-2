package Persistencia;

import Entidades.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// ESTA CLASE ES TEMPORAL Y SE DEBE BORRAR ANTES DE ENTREGAR
public class PobladorDeDatos {

    public static void main(String[] args) {

        System.out.println("--- INICIANDO POBLADO DE DATOS (SEEDING) ---");

        // --- 1. Crear los Gestores de JSON (Managers) ---
        JsonManagerAeropuertos jm_aeropuertos = new JsonManagerAeropuertos();
        JsonManagerAviones jm_aviones = new JsonManagerAviones();
        JsonManagerAerolineas jm_aerolineas = new JsonManagerAerolineas();
        JsonManagerUsuarios jm_usuarios = new JsonManagerUsuarios();


        // --- 2. Crear los Objetos en Java ---

        // ======================================================================
        // AEROPUERTOS
        // ======================================================================
        System.out.println("Creando Aeropuertos...");
        List<Aeropuerto> aeropuertos = new ArrayList<>();

        aeropuertos.add(new Aeropuerto("EZE", "Aeropuerto Internacional Ministro Pistarini", "Ezeiza"));
        aeropuertos.add(new Aeropuerto("AEP", "Aeroparque Jorge Newbery", "Buenos Aires"));
        aeropuertos.add(new Aeropuerto("COR", "Aeropuerto Internacional Ingeniero Taravella", "Córdoba"));
        aeropuertos.add(new Aeropuerto("ALL", "Aeroclub Allen", "Allen"));
        aeropuertos.add(new Aeropuerto("ARR", "Aeropuerto Alto Río Senguer", "Alto Río Senguer"));
        aeropuertos.add(new Aeropuerto("ZUL", "Aeródromo de Azul", "Azul"));
        aeropuertos.add(new Aeropuerto("BHI", "Aeropuerto Comandante Espora", "Bahía Blanca"));
        aeropuertos.add(new Aeropuerto("BRC", "Aeropuerto Internacional Teniente Luis Candelaria", "Bariloche"));
        aeropuertos.add(new Aeropuerto("CVI", "Aeródromo Caleta Olivia", "Caleta Olivia"));
        aeropuertos.add(new Aeropuerto("CCT", "Aeropuerto Colonia Catriel", "Catriel"));
        aeropuertos.add(new Aeropuerto("CVH", "Aeropuerto de Caviahue", "Caviahue"));
        aeropuertos.add(new Aeropuerto("CRR", "Aeropuerto Ceres", "Ceres"));
        aeropuertos.add(new Aeropuerto("GOR", "Aeropuerto Gobernador Gordillo", "Chamical"));
        aeropuertos.add(new Aeropuerto("ITO", "Aeropuerto Chilecito", "Chilecito"));
        aeropuertos.add(new Aeropuerto("HOS", "Aeropuerto de Chos Malal", "Chos Malal"));
        aeropuertos.add(new Aeropuerto("CLX", "Aeropuerto Clorinda", "Clorinda"));
        aeropuertos.add(new Aeropuerto("CRD", "Aeropuerto Internacional General Enrique Mosconi", "Comodoro Rivadavia"));
        aeropuertos.add(new Aeropuerto("COC", "Aeropuerto Comodoro Pierrestegui", "Concordia"));
        aeropuertos.add(new Aeropuerto("CSZ", "Aeroclub Brigadier Hector Eduardo Ruiz", "Coronel Suárez"));
        aeropuertos.add(new Aeropuerto("CNQ", "Aeropuerto Internacional Doctor Fernando Piragine Niveyro", "Corrientes"));
        aeropuertos.add(new Aeropuerto("UZU", "Aeropuerto de Curuzú Cuatiá", "Curuzú Cuatiá"));
        aeropuertos.add(new Aeropuerto("CUT", "Aeropuerto de Cutral-Co", "Cutral-Co"));
        aeropuertos.add(new Aeropuerto("DOT", "Aeródromo de Don Torcuato (Cerrado)", "Don Torcuato"));
        aeropuertos.add(new Aeropuerto("EHL", "Aeropuerto de El Bolson", "El Bolsón"));
        aeropuertos.add(new Aeropuerto("FTE", "Aeropuerto Comandante Armando Tola", "El Calafate"));
        aeropuertos.add(new Aeropuerto("EMX", "Aeropuerto de El Maitén", "El Maitén"));
        aeropuertos.add(new Aeropuerto("EPA", "Aeropuerto El Palomar", "El Palomar"));
        aeropuertos.add(new Aeropuerto("ELO", "Aeroclub Alto Paraná Eldorado", "Eldorado"));
        aeropuertos.add(new Aeropuerto("EQS", "Aeropuerto Brigadier General Antonio Parodi", "Esquel"));
        aeropuertos.add(new Aeropuerto("FMA", "Aeropuerto Internacional de Formosa", "Formosa"));
        aeropuertos.add(new Aeropuerto("GPO", "Aeropuerto de General Pico", "General Pico"));
        aeropuertos.add(new Aeropuerto("GNR", "Aeropuerto de General Roca", "General Roca"));
        aeropuertos.add(new Aeropuerto("GGS", "Aeródromo Gobernador Gregores", "Gobernador Gregores"));
        aeropuertos.add(new Aeropuerto("OYA", "Aeropuerto Dr. Diego Nicolás Díaz Colodrero", "Goya"));
        aeropuertos.add(new Aeropuerto("GHU", "Aeropuerto de Gualeguaychú", "Gualeguaychú"));
        aeropuertos.add(new Aeropuerto("IGB", "Aeropuerto de Ingeniero Jacobacci", "Ingeniero Jacobacci"));
        aeropuertos.add(new Aeropuerto("ENO", "Aeropuerto Mariano Moreno", "José C. Paz"));
        aeropuertos.add(new Aeropuerto("JSM", "Aeropuerto de José de San Martín", "José de San Martín"));
        aeropuertos.add(new Aeropuerto("JNI", "Aeropuerto de Junín", "Junín"));
        aeropuertos.add(new Aeropuerto("LCM", "Aeropuerto La Cumbre", "La Cumbre"));
        aeropuertos.add(new Aeropuerto("LPG", "Aeropuerto de La Plata", "La Plata"));
        aeropuertos.add(new Aeropuerto("IRJ", "Aeropuerto Capitán Vicente Almandos Almonacid", "La Rioja"));
        aeropuertos.add(new Aeropuerto("LHS", "Aeropuerto Las Heras", "Las Heras"));
        aeropuertos.add(new Aeropuerto("LLS", "Aeródromo Alférez Armando Rodríguez", "Las Lomitas"));
        aeropuertos.add(new Aeropuerto("LCP", "Aeropuerto Teniente La Rufa", "Loncopué"));
        aeropuertos.add(new Aeropuerto("LGS", "Aeropuerto Internacional Comodoro Ricardo Salomón", "Malargüe"));
        aeropuertos.add(new Aeropuerto("MDQ", "Aeropuerto Internacional Astor Piazzolla", "Mar del Plata"));
        aeropuertos.add(new Aeropuerto("MDZ", "Aeropuerto Internacional El Plumerillo", "Mendoza"));
        aeropuertos.add(new Aeropuerto("MDX", "Aeropuerto del Iberá", "Mercedes"));
        aeropuertos.add(new Aeropuerto("RLO", "Aeropuerto Internacional Valle del Conlara", "Merlo"));
        aeropuertos.add(new Aeropuerto("MJR", "Aeródromo Juan Domingo Perón", "Miramar"));
        aeropuertos.add(new Aeropuerto("MCS", "Aeropuerto de Monte Caseros", "Monte Caseros"));
        aeropuertos.add(new Aeropuerto("NEC", "Aeropuerto Edgardo Hugo Yelpo", "Necochea"));
        aeropuertos.add(new Aeropuerto("NQN", "Aeropuerto Internacional Presidente Perón", "Neuquén"));
        aeropuertos.add(new Aeropuerto("OVR", "Aeropuerto de Olavarría", "Olavarría"));
        aeropuertos.add(new Aeropuerto("PRA", "Aeropuerto General Justo José de Urquiza", "Paraná"));
        aeropuertos.add(new Aeropuerto("AOL", "Aeropuerto Internacional de Paso de los Libres", "Paso de los Libres"));
        aeropuertos.add(new Aeropuerto("PEH", "Aeropuerto Comodoro P. Zanni", "Pehuajó"));
        aeropuertos.add(new Aeropuerto("JUJ", "Aeropuerto Internacional Gobernador Horacio Guzmán", "Perico"));
        aeropuertos.add(new Aeropuerto("PMQ", "Aeropuerto Perito Moreno", "Perito Moreno"));
        aeropuertos.add(new Aeropuerto("PSS", "Aeropuerto Internacional Libertador General José de San Martín", "Posadas"));
        aeropuertos.add(new Aeropuerto("PUD", "Aeropuerto Puerto Deseado", "Puerto Deseado"));
        aeropuertos.add(new Aeropuerto("IGR", "Aeropuerto Internacional de Puerto Iguazú", "Puerto Iguazú"));
        aeropuertos.add(new Aeropuerto("PMY", "Aeropuerto El Tehuelche", "Puerto Madryn"));
        aeropuertos.add(new Aeropuerto("ULA", "Aeropuerto Capitán José Daniel Vázquez", "Puerto San Julián"));
        aeropuertos.add(new Aeropuerto("RZA", "Aeropuerto de Puerto Santa Cruz", "Puerto Santa Cruz"));
        aeropuertos.add(new Aeropuerto("PRQ", "Aeropuerto de Presidencia Roque Sáenz Peña", "Presidencia Roque Saenz Peña"));
        aeropuertos.add(new Aeropuerto("RAF", "Aeródromo de Rafaela", "Rafaela"));
        aeropuertos.add(new Aeropuerto("RCQ", "Aeropuerto Daniel Jurkic", "Reconquista"));
        aeropuertos.add(new Aeropuerto("RES", "Aeropuerto Internacional de Resistencia", "Resistencia"));
        aeropuertos.add(new Aeropuerto("RDS", "Aeropuerto Rincón de los Sauces", "Rincón de los Sauces"));
        aeropuertos.add(new Aeropuerto("RCU", "Aeropuerto de Río Cuarto", "Río Cuarto"));
        aeropuertos.add(new Aeropuerto("RGL", "Aeropuerto Internacional Piloto Civil Norberto Fernández", "Río Gallegos"));
        aeropuertos.add(new Aeropuerto("RGA", "Aeropuerto Internacional Gob. Ramón Trejo Noel", "Río Grande"));
        aeropuertos.add(new Aeropuerto("ROY", "Aeropuerto de Río Mayo", "Río Mayo"));
        aeropuertos.add(new Aeropuerto("RYO", "Aeropuerto Río Turbio", "Río Turbio/Veintiocho de Noviembre"));
        aeropuertos.add(new Aeropuerto("ROS", "Aeropuerto Internacional Rosario Islas Malvinas", "Rosario"));
        aeropuertos.add(new Aeropuerto("SLA", "Aeropuerto Internacional Martín Miguel de Güemes", "Salta"));
        aeropuertos.add(new Aeropuerto("OES", "Aeródromo Saint Exupery", "San Antonio Oeste"));
        aeropuertos.add(new Aeropuerto("FDO", "Aeropuerto Internacional de San Fernando", "San Fernando"));
        aeropuertos.add(new Aeropuerto("CTC", "Aeropuerto Coronel Felipe Varela", "San Fernando del Valle de Catamarca"));
        aeropuertos.add(new Aeropuerto("UAQ", "Aeropuerto Domingo Faustino Sarmiento", "San Juan"));
        aeropuertos.add(new Aeropuerto("LUQ", "Aeropuerto Brigadier Mayor Cesar Raúl Ojeda", "San Luis"));
        aeropuertos.add(new Aeropuerto("AFA", "Aeropuerto Internacional Suboficial Ayudante Santiago Germano", "San Rafael"));
        aeropuertos.add(new Aeropuerto("ORA", "Aero Club Orán", "San Ramón de la Nueva Orán"));
        aeropuertos.add(new Aeropuerto("TUC", "Aeropuerto Internacional Teniente Benjamín Matienzo", "San Miguel de Tucumán"));
        aeropuertos.add(new Aeropuerto("RSA", "Aeropuerto de Santa Rosa", "Santa Rosa"));
        aeropuertos.add(new Aeropuerto("SST", "Aeropuerto de Santa Teresita", "Santa Teresita"));
        aeropuertos.add(new Aeropuerto("SDE", "Aeropuerto Vicecomodoro Ángel de la Paz Aragonés", "Santiago del Estero"));
        aeropuertos.add(new Aeropuerto("CPC", "Aeropuerto Aviador Carlos Campos", "San Martín de los Andes"));
        aeropuertos.add(new Aeropuerto("SFN", "Aeropuerto de Sauce Viejo", "Sauce Viejo"));
        aeropuertos.add(new Aeropuerto("SGV", "Aeródromo Sierra Grande", "Sierra Grande"));
        aeropuertos.add(new Aeropuerto("NCJ", "Aeropuerto de Sunchales", "Sunchales"));
        aeropuertos.add(new Aeropuerto("TDL", "Aeropuerto de Tandil", "Tandil"));
        aeropuertos.add(new Aeropuerto("TTG", "Aeropuerto de Tartagal", "Tartagal"));
        aeropuertos.add(new Aeropuerto("RHD", "Aeropuerto Internacional Termas de Río Hondo", "Termas de Río Hondo"));
        aeropuertos.add(new Aeropuerto("REL", "Aeropuerto Almirante Marco Andrés Zar/Base Aeronaval Almirante Zar", "Trelew"));
        aeropuertos.add(new Aeropuerto("OYO", "Aeropuerto Municipal Primer Teniente Héctor Ricardo Volponi", "Tres Arroyos"));
        aeropuertos.add(new Aeropuerto("USH", "Aeropuerto Internacional Malvinas Argentinas", "Ushuaia"));
        aeropuertos.add(new Aeropuerto("VCF", "Aeródromo de Valcheta", "Valcheta"));
        aeropuertos.add(new Aeropuerto("VNO", "Aeródromo Municipal Tomás B. Kenny", "Venado Tuerto"));
        aeropuertos.add(new Aeropuerto("VDM", "Aeropuerto Gobernador Edgardo Castello", "Viedma"));
        aeropuertos.add(new Aeropuerto("VDR", "Aeropuerto de Villa Dolores", "Villa Dolores"));
        aeropuertos.add(new Aeropuerto("VLG", "Aeropuerto de Villa Gesell", "Villa Gesell"));
        aeropuertos.add(new Aeropuerto("VMR", "Aeropuerto Regional Presidente Néstor Kirchner", "Villa María"));
        aeropuertos.add(new Aeropuerto("RYD", "Aeropuerto de Villa Reynolds", "Villa Reynolds"));
        aeropuertos.add(new Aeropuerto("APZ", "Aeropuerto de Zapala", "Zapala"));

        System.out.println("Total de aeropuertos creados: " + aeropuertos.size());

        // ======================================================================
        // AVIONES
        // ======================================================================
        System.out.println("Creando Aviones...");
        List<Avion> aviones = new ArrayList<>();

        // Definición de todos los aviones
        Avion avion1_AR = new Avion("LV-FNI", "Airbus A330", 240, 24);
        Avion avion2_AR = new Avion("LV-GKE", "Boeing 737-800", 150, 12);
        Avion avion1_WJ = new Avion("LV-JYR", "Embraer 190", 90, 6); // Asignado a AR
        Avion avion3_AR = new Avion("LV-HQB", "Airbus A330-200", 245, 24);
        Avion avion4_AR = new Avion("LV-KFW", "Boeing 737-MAX 8", 160, 12);
        Avion avion2_WJ = new Avion("LV-KJA", "Airbus A320", 186, 15);
        Avion avion3_WJ = new Avion("LV-KJD", "Airbus A320neo", 186, 15);
        Avion avion1_FO = new Avion("LV-KAY", "Boeing 737-800", 189, 15);
        Avion avion2_FO = new Avion("LV-KEF", "Boeing 737-800", 189, 15);
        Avion avion1_LD = new Avion("LV-CDA", "Fokker F28", 70, 8);
        Avion avion2_LD = new Avion("LV-BZO", "Saab 340", 34, 4);
        Avion avion1_AJ = new Avion("LV-JMQ", "Embraer ERJ-145", 50, 5);
        Avion avion1_AN = new Avion("LV-CCU", "McDonnell Douglas MD-83", 165, 12);
        Avion avion2_AN = new Avion("LV-WGN", "Boeing 737-800", 180, 15);
        Avion avion1_HU = new Avion("LV-ZGT", "Let L-410 Turbolet", 19, 2);

        // Agregar TODOS los aviones a la lista principal
        aviones.add(avion1_AR);
        aviones.add(avion2_AR);
        aviones.add(avion1_WJ);
        aviones.add(avion3_AR);
        aviones.add(avion4_AR);
        aviones.add(avion2_WJ);
        aviones.add(avion3_WJ);
        aviones.add(avion1_FO);
        aviones.add(avion2_FO);
        aviones.add(avion1_LD);
        aviones.add(avion2_LD);
        aviones.add(avion1_AJ);
        aviones.add(avion1_AN);
        aviones.add(avion2_AN);
        aviones.add(avion1_HU);

        System.out.println("Total de aviones creados: " + aviones.size());


        // ======================================================================
        // AEROLINEAS
        // ======================================================================
        System.out.println("Creando Aerolíneas...");
        List<Aerolinea> aerolineas = new ArrayList<>();

        // Definir y configurar cada aerolínea
        Aerolinea ar = new Aerolinea("AR", "Aerolíneas Argentinas", 50.0, 70.0, 150.0);
        ar.agregarAvionAlaFlota(avion1_AR);
        ar.agregarAvionAlaFlota(avion2_AR);
        ar.agregarAvionAlaFlota(avion3_AR);
        ar.agregarAvionAlaFlota(avion4_AR);
        ar.agregarAvionAlaFlota(avion1_WJ);

        Aerolinea wj = new Aerolinea("WJ", "JetSMART", 65.0, 80.0, 180.0);
        wj.agregarAvionAlaFlota(avion2_WJ);
        wj.agregarAvionAlaFlota(avion3_WJ);

        Aerolinea fo = new Aerolinea("FO", "Flybondi", 60.0, 75.0, 175.0);
        fo.agregarAvionAlaFlota(avion1_FO);
        fo.agregarAvionAlaFlota(avion2_FO);

        Aerolinea ld = new Aerolinea("LD", "LADE - Líneas Aéreas Del Estado", 70.0, 90.0, 200.0);
        ld.agregarAvionAlaFlota(avion1_LD);
        ld.agregarAvionAlaFlota(avion2_LD);

        Aerolinea aj = new Aerolinea("AJ", "American Jet", 80.0, 100.0, 220.0);
        aj.agregarAvionAlaFlota(avion1_AJ);

        Aerolinea an = new Aerolinea("AN", "Andes Líneas Aéreas", 55.0, 70.0, 160.0);
        an.agregarAvionAlaFlota(avion1_AN);
        an.agregarAvionAlaFlota(avion2_AN);

        Aerolinea hu = new Aerolinea("HU", "Humming Airways", 75.0, 85.0, 190.0);
        hu.agregarAvionAlaFlota(avion1_HU);

        // Agregar TODAS las aerolíneas a la lista principal
        aerolineas.add(ar);
        aerolineas.add(wj);
        aerolineas.add(fo);
        aerolineas.add(ld);
        aerolineas.add(aj);
        aerolineas.add(an);
        aerolineas.add(hu);

        System.out.println("Total de aerolíneas creadas: " + aerolineas.size());


        // --- 3. Guardar todo en los archivos ---
        System.out.println("Guardando archivos JSON...");
        jm_aeropuertos.guardarLista(aeropuertos);
        jm_aviones.guardarLista(aviones);
        jm_aerolineas.guardarLista(aerolineas);
        // Aún no se crean usuarios en este poblador

        System.out.println("--- ¡POBLADO DE DATOS COMPLETADO! ---");
        System.out.println("--- Ahora podés BORRAR esta clase (PobladorDeDatos.java) ---");
    }
}