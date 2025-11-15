package Persistencia;

import Entidades.Reserva;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

public class JsonManagerReservas {
    private final String nombreArchivo = "reservas.json";

    public JsonManagerReservas() {}

    public void guardarLista(List<Reserva> listaReserva){
        // Si serializarLista() falla, lanzara RuntimeException.
        // Este metodo no la captura; lo debera capturar, su llamador.
        OperacionesLectoEscritura.grabar(nombreArchivo, serializarLista(listaReserva));
    }
    public JSONArray serializarLista(List<Reserva> listaReserva){
        try{
            JSONArray jsonArray = new JSONArray();
            for(Reserva r : listaReserva){
                // Serializamos cada reserva, y la agregamos al JSONArray
                jsonArray.put(r.toJSON());
            }
            return jsonArray;
        }catch(JSONException e){
            throw new RuntimeException("No se ha podido serializar la lista. ",e);
        }

    }

    public List<Reserva> leerLista() {
        JSONTokener tokener = OperacionesLectoEscritura.leer(nombreArchivo);
        try {
            return deserializarLista(new JSONArray(tokener));
        } catch (RuntimeException e) {
            System.err.println("Error al leer la lista: " + e.getMessage());
            return new ArrayList<>(); // Esto evita un posible NullPointerException
        }
    }
    public List<Reserva> deserializarLista(JSONArray jsonArray){
        List<Reserva> listaReserva = new ArrayList<>();
        try{
            for(int i = 0; i < jsonArray.length(); i++){
                Reserva r = new Reserva(jsonArray.getJSONObject(i));
                listaReserva.add(r);
            }
            return  listaReserva;
        }catch(JSONException e){
            throw new RuntimeException("No se ha podido deserializar la lista. ",e);
        }
    }

}
