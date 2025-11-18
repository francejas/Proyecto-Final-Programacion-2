package Persistencia;

import Entidades.Aeropuerto;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

public class JsonManagerAeropuertos {

    private final String nombreArchivo = "aeropuertos.json";

    public JsonManagerAeropuertos() {
    }

    public void guardarLista(List<Aeropuerto> listaAeropuertos) {
        try {
            JSONArray jsonArray = serializarLista(listaAeropuertos);
            OperacionesLectoEscritura.grabar(nombreArchivo, jsonArray);
        } catch (RuntimeException e) {
            System.err.println("Error al guardar la lista de aeropuertos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private JSONArray serializarLista(List<Aeropuerto> listaAeropuertos) {
        try {
            JSONArray jsonArray = new JSONArray();
            for (Aeropuerto aeropuerto : listaAeropuertos) {
                jsonArray.put(aeropuerto.toJSON());
            }
            return jsonArray;
        } catch (JSONException e) {
            throw new RuntimeException("Error al serializar la lista de aeropuertos.", e);
        }
    }

    public List<Aeropuerto> leerLista() {
        JSONTokener tokener = OperacionesLectoEscritura.leer(nombreArchivo);
        if (tokener == null) {
            // El archivo no existe, devuelve una lista vacía.
            return new ArrayList<>();
        }

        try {
            // 1. Esto puede lanzar JSONException si el archivo está mal formateado
            JSONArray jsonArray = new JSONArray(tokener);

            // 2. Esto ahora lanza JSONException si el contenido es incorrecto
            return deserializarLista(jsonArray);

        } catch (JSONException e) {
            // Un solo catch para todos los errores de formato JSON.
            System.err.println("Error de formato JSON al leer la lista de aeropuertos: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // Devuelve lista vacía para no crashear
        }
    }


    private List<Aeropuerto> deserializarLista(JSONArray jsonArray) throws JSONException {
        List<Aeropuerto> listaAeropuertos = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonAeropuerto = jsonArray.getJSONObject(i);
            Aeropuerto aeropuerto = new Aeropuerto(jsonAeropuerto);
            listaAeropuertos.add(aeropuerto);
        }
        return listaAeropuertos;
    }
}