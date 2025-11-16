package Persistencia;

import Entidades.Avion;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

public class JsonManagerAviones {
    private final String nombreArchivo = "aviones.json";

    public JsonManagerAviones() {
    }

    public void guardarLista(List<Avion> aviones) {
        try{
            JSONArray jsonArray = serializarLista(aviones);
            OperacionesLectoEscritura.grabar(nombreArchivo, jsonArray);
        } catch (RuntimeException e) {
            System.out.println("Error al guardar la lista de aviones: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public JSONArray serializarLista(List<Avion> aviones) {
        try{
            JSONArray jsonArray = new JSONArray();
            for (Avion avion : aviones) {
                jsonArray.put(avion.toJSON());
            }
            return jsonArray;
        }catch(JSONException e){
            throw new RuntimeException("Error al serializar la lista de aviones: ",e);
        }
    }

    public List<Avion> leerLista() {
        JSONTokener tokener = OperacionesLectoEscritura.leer(nombreArchivo);
        if (tokener == null) {
            return new ArrayList<>();
        }
        
        try {
            JSONArray jsonArray = new JSONArray(tokener);
            return deserializarLista(jsonArray); 
            
        } catch (JSONException e) {
            // 1. Captura el error específico de "JSON mal formateado"
            System.err.println("Error de formato JSON al leer la lista de vuelos: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
            
        } catch (RuntimeException e) {
            // 2. Captura cualquier otro error inesperado 
            // (ejemplo un NullPointerException si hay un bug en el codigo)
            System.err.println("Error inesperado (RuntimeException) al leer la lista de vuelos: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    
    public List<Avion> deserializarLista(JSONArray jsonArray) throws JSONException {
        List<Avion> aviones = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonAvion = jsonArray.getJSONObject(i);
            Avion a = new Avion(jsonAvion); 
            aviones.add(a);
        }
        return aviones;
    }

}
