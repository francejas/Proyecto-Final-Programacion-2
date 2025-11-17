package Servicios;

import Entidades.Aerolinea;
import Persistencia.JsonManagerAerolineas;

import java.util.ArrayList;
import java.util.List;

/**
 * Maneja la lógica de negocio para la gestión de Aerolíneas.
 *
 * (Versión 2.0 - Diseño Simplificado)
 * Esta clase ahora actúa como un servicio de "gestión de estado y precios".
 * Carga las aerolíneas "hardcodeadas" (pobladas) desde el JSON al inicio.
 * Ya NO implementa 'Gestionable' (no permite alta/baja/modificación genérica).
 *
 * @version 2.0
 * @since 2025-11-05
 */
public class GestorAerolineas {

    // --- Atributos ---

    /**
     * Lista maestra en memoria de TODAS las aerolíneas.
     * Se carga al inicio desde el JSON.
     */
    private List<Aerolinea> aerolineas;

    /**
     * Especialista en persistencia (paquete Persistencia).
     * Se usa para leer y escribir en aerolineas.json.
     * Es 'final' porque se asigna una vez en el constructor.
     */
    private final JsonManagerAerolineas jsonManager;

    // --- Constructor ---

    /**
     * Constructor del GestorAerolineas.
     * Carga la lista de aerolíneas desde el archivo JSON al iniciar.
     */
    public GestorAerolineas() {
        this.jsonManager = new JsonManagerAerolineas();
        // Carga los datos maestros desde el archivo
        this.aerolineas = jsonManager.leerLista();
    }

    /**
     * Metodo helper privado para centralizar el guardado en JSON.
     * Llama al JsonManager para guardar la lista 'aerolineas' actual.
     * Es llamado por los métodos de modificación (activar, desactivar, modificarPrecios).
     */
    private void guardarEnJson() {
        jsonManager.guardarLista(this.aerolineas);
    }

    // --- Métodos de Lógica de Negocio (Gestión de Admin) ---

    /**
     * Realiza una baja lógica de una aerolínea (la desactiva).
     * @param codigo El código IATA (ej: "AR") de la aerolínea a desactivar.
     */
    public void desactivarAerolinea(String codigo) {
        Aerolinea aerolinea = this.consulta(codigo);
        if (aerolinea != null && aerolinea.isActiva()) {
            aerolinea.setActiva(false); // Cambia el estado
            guardarEnJson(); // Persiste el cambio
        }
    }

    /**
     * Realiza una alta lógica de una aerolínea (la reactiva).
     * @param codigo El código IATA (ej: "AR") de la aerolínea a activar.
     */
    public void activarAerolinea(String codigo) {
        Aerolinea aerolinea = this.consulta(codigo);
        if (aerolinea != null && !aerolinea.isActiva()) {
            aerolinea.setActiva(true); // Cambia el estado
            guardarEnJson(); // Persiste el cambio
        }
    }

    /**
     * Modifica los precios de equipaje de una aerolínea específica.
     *
     * @param codigo El código de la aerolínea a modificar.
     * @param costoCarryOn El nuevo precio para el CarryOn.
     * @param costoDespachado El nuevo precio para el Equipaje Despachado.
     * @param costoEspecial El nuevo precio para el Equipaje Especial.
     */
    public void modificarPreciosEquipaje(String codigo, double costoCarryOn, double costoDespachado, double costoEspecial) {
        Aerolinea aerolinea = this.consulta(codigo);

        if (aerolinea != null) {
            // Actualiza los precios usando los setters de la entidad
            aerolinea.setCostoCarryOn(costoCarryOn);
            aerolinea.setCostoEquipajeDespachado(costoDespachado);
            aerolinea.setCostoEquipajeEspecial(costoEspecial);

            // Persiste los cambios
            guardarEnJson();
        }
    }

    // --- Métodos de Consulta (Solo Lectura) ---

    /**
     * Busca y devuelve una aerolínea por su código IATA.
     * Usado por GestorVuelos y por el MenuAdmin.
     *
     * @param codigo El código (ej: "AR") a buscar.
     * @return La Aerolinea encontrada, o null si no existe.
     */
    public Aerolinea consulta(String codigo) {
        for (Aerolinea aerolinea : this.aerolineas) {
            if (aerolinea.getCodigo().equalsIgnoreCase(codigo)) {
                return aerolinea;
            }
        }
        return null;
    }

    /**
     * Devuelve una copia de la lista de todas las aerolíneas.
     * Usado por el MenuAdmin para mostrar las opciones al crear un Vuelo.
     *
     * @return Una nueva List<Aerolinea>.
     */
    public List<Aerolinea> listar() {
        return new ArrayList<>(this.aerolineas); // Devuelve una copia
    }
}