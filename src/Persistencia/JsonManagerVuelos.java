package Persistencia;

import Entidades.Vuelo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

public class JsonManagerVuelos {
    private final String nombreArchivo = "vuelos.json";

    public JsonManagerVuelos() {
    }

    public void guardarLista(List<Vuelo> listaVuelos){
        try{
            JSONArray jsonArray = serializarLista(listaVuelos);
            OperacionesLectoEscritura.grabar(nombreArchivo, jsonArray);
        }catch(RuntimeException e){
            System.err.println("Error al guardar la lista de vuelos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public JSONArray serializarLista(List<Vuelo> listaVuelos){
        try{
            JSONArray jsonArray = new JSONArray();
            for(Vuelo vuelo : listaVuelos){
                jsonArray.put(vuelo.toJSON());
            }
            return jsonArray;
        } catch (JSONException e) {
            throw new RuntimeException("Error al serializar la lista de vuelos: ",e);
        }
    }

    public List<Vuelo> leerLista() {
        JSONTokener tokener = OperacionesLectoEscritura.leer(nombreArchivo);
        if (tokener == null) {
            return new ArrayList<>();
        }
        
        try {
            JSONArray jsonArray = new JSONArray(tokener);
            return deserializarLista(jsonArray); 
            
        } catch (JSONException e) {
            // Un solo catch para todos los errores de formato JSON
            System.err.println("Error de formato JSON al leer la lista de vuelos: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } catch (RuntimeException e) {
            //  Un catch genérico por si otra cosa falla
            System.err.println("Error inesperado al leer la lista de vuelos: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    
    public List<Vuelo> deserializarLista(JSONArray jsonArray) throws JSONException {
        List<Vuelo> listaVuelos = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonVuelo = jsonArray.getJSONObject(i);
            Vuelo v = new Vuelo(jsonVuelo); 
            listaVuelos.add(v);
        }
        return listaVuelos;
    }
}
