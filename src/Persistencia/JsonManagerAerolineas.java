package Persistencia;

import Entidades.Aerolinea;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

public class JsonManagerAerolineas {

    private final String nombreArchivo = "aerolineas.json";

    public JsonManagerAerolineas() {
    }

    public void guardarLista(List<Aerolinea> listaAerolineas) {
        try {
            // 1. Serializa la lista de Java a un JSONArray
            JSONArray jsonArray = serializarLista(listaAerolineas);

            // 2. Llama a la utilidad de I/O para grabar el JSONArray en el disco
            OperacionesLectoEscritura.grabar(nombreArchivo, jsonArray);

        } catch (RuntimeException e) {
            // Maneja la RuntimeException lanzada por serializarLista
            System.err.println("Error al guardar la lista de aerolíneas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private JSONArray serializarLista(List<Aerolinea> listaAerolineas) {
        try {
            JSONArray jsonArray = new JSONArray();
            for (Aerolinea aerolinea : listaAerolineas) {
                // Llama al metodo toJSON() de la entidad Aerolinea
                jsonArray.put(aerolinea.toJSON());
            }
            return jsonArray;
        } catch (JSONException e) {
            // Si toJSON() falla, lanzamos una RuntimeException
            throw new RuntimeException("Error al serializar la lista de aerolíneas.", e);
        }
    }

    public List<Aerolinea> leerLista() {
        // 1. Llama a la utilidad de I/O para leer el archivo
        JSONTokener tokener = OperacionesLectoEscritura.leer(nombreArchivo);
        if (tokener == null) {
            // El archivo no existe o no se pudo leer
            return new ArrayList<>();
        }

        try {
            // 2. Esto puede lanzar JSONException si el archivo está mal formateado
            JSONArray jsonArray = new JSONArray(tokener);

            // 3. Esto ahora lanza JSONException si el contenido es incorrecto
            return deserializarLista(jsonArray);

        } catch (JSONException e) {
            // Un solo catch para todos los errores de formato JSON
            System.err.println("Error de formato JSON al leer la lista de aerolíneas: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // Devuelve lista vacía para no crashear
        } catch (RuntimeException e) {
            // Catch genérico por si otra cosa falla
            System.err.println("Error inesperado al leer la lista de aerolíneas: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    private List<Aerolinea> deserializarLista(JSONArray jsonArray) throws JSONException {
        List<Aerolinea> listaAerolineas = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonAerolinea = jsonArray.getJSONObject(i);
            // Llama al constructor JSON de la entidad Aerolinea
            Aerolinea aerolinea = new Aerolinea(jsonAerolinea);
            listaAerolineas.add(aerolinea);
        }
        return listaAerolineas;
    }
}