package Persistencia;

import Entidades.Reserva;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

public class JsonManagerReservas {
    private final String nombreArchivo = "reservas.json";

    public JsonManagerReservas() {}

    public void guardarLista(List<Reserva> listaReserva){
        try {
            JSONArray jsonArray = serializarLista(listaReserva);
            OperacionesLectoEscritura.grabar(nombreArchivo, jsonArray);
        } catch (RuntimeException e) {
            System.err.println("Error al guardar la lista de reservas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public JSONArray serializarLista(List<Reserva> listaReserva){
        try{
            JSONArray jsonArray = new JSONArray();
            for(Reserva r : listaReserva){
                jsonArray.put(r.toJSON());
            }
            return jsonArray;
        }catch(JSONException e){
            throw new RuntimeException("No se ha podido serializar la lista de reservas. ",e);
        }
    }

    public List<Reserva> leerLista() {
        JSONTokener tokener = OperacionesLectoEscritura.leer(nombreArchivo);

        if (tokener == null) {
            // El archivo no existe, devuelve una lista vacía.
            return new ArrayList<>();
        }

        try {
            // Esto puede lanzar JSONException si el archivo está mal formateado
            JSONArray jsonArray = new JSONArray(tokener);

            // Esto ahora lanza JSONException si el contenido es incorrecto
            return deserializarLista(jsonArray);

        } catch (JSONException e) {
            // Un solo catch para todos los errores de formato JSON.
            System.err.println("Error de formato JSON al leer la lista de reservas: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // Devuelve lista vacía para no crashear
        }
    }


    public List<Reserva> deserializarLista(JSONArray jsonArray) throws JSONException {
        List<Reserva> listaReserva = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            // Llama al constructor JSON, que puede lanzar JSONException
            Reserva r = new Reserva(jsonArray.getJSONObject(i));
            listaReserva.add(r);
        }
        return  listaReserva;
    }
}