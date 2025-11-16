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

    public List<Avion> leerLista(){
        JSONTokener tokener = OperacionesLectoEscritura.leer(nombreArchivo);
        if(tokener==null){
            return new ArrayList<>();
        }

        try{
            JSONArray jsonArray = new JSONArray(tokener);
            return deserializarLista(jsonArray);
        }catch(JSONException e){
            System.out.println("Error ded formato JSON al leer la lista de aviones: "+e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public List<Avion> deserializarLista(JSONArray jsonArray){
        List<Avion> aviones = new ArrayList<>();

        for(int i=0;i<jsonArray.length();i++){
            JSONObject jsonAvion =  jsonArray.getJSONObject(i);
            Avion a = new Avion(jsonAvion);
            aviones.add(a);
        }
        return aviones;
    }

}
