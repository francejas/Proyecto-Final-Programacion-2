package Persistencia;

import Entidades.Usuario;
import Entidades.Cliente;
import Entidades.Administrador;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

public class JsonManagerUsuarios {

    private final String nombreArchivo = "usuarios.json";

    public JsonManagerUsuarios() {
    }

    public void guardarLista(List<Usuario> listaUsuarios) {
        try {
            // 1. Serializa la lista de Java a un JSONArray
            JSONArray jsonArray = serializarLista(listaUsuarios);

            // 2. Llama a la utilidad de I/O para grabar el JSONArray en el disco
            OperacionesLectoEscritura.grabar(nombreArchivo, jsonArray);

        } catch (RuntimeException e) {
            // Maneja la RuntimeException lanzada por serializarLista
            System.err.println("Error al guardar la lista de usuarios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private JSONArray serializarLista(List<Usuario> listaUsuarios) {
        try {
            JSONArray jsonArray = new JSONArray();
            for (Usuario usuario : listaUsuarios) {
                // Llama al metodo toJSON() de la entidad (Cliente o Admin)
                jsonArray.put(usuario.toJSON());
            }
            return jsonArray;
        } catch (JSONException e) {
            // Si toJSON() falla, lanzamos una RuntimeException
            throw new RuntimeException("Error al serializar la lista de usuarios.", e);
        }
    }

    public List<Usuario> leerLista() {
        // 1. Llama a la utilidad de I/O para leer el archivo
        JSONTokener tokener = OperacionesLectoEscritura.leer(nombreArchivo);
        if (tokener == null) {
            // El archivo no existe, devuelve una lista vacía.
            return new ArrayList<>();
        }

        try {
            // 2. Esto puede lanzar JSONException si el archivo está mal formateado
            JSONArray jsonArray = new JSONArray(tokener);

            // 3. Esto ahora lanza JSONException si el contenido es incorrecto
            return deserializarLista(jsonArray);

        } catch (JSONException e) {
            // Un solo catch para todos los errores de formato JSON
            System.err.println("Error de formato JSON al leer la lista de usuarios: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // Devuelve lista vacía para no crashear
        }
    }

    private List<Usuario> deserializarLista(JSONArray jsonArray) throws JSONException {
        List<Usuario> listaUsuarios = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonUsuario = jsonArray.getJSONObject(i);

            // 1. Leemos el campo "rol" que guardamos en Usuario.toJSON()
            String rol = jsonUsuario.getString("rol");

            Usuario usuario = null;

            // 2. Decidimos qué clase instanciar basado en el rol
            if (rol.equals("CLIENTE")) {
                usuario = new Cliente(jsonUsuario);
            } else if (rol.equals("ADMINISTRADOR")) {
                usuario = new Administrador(jsonUsuario);
            }

            if (usuario != null) {
                listaUsuarios.add(usuario);
            } else {
                //manejar un rol desconocido
                System.err.println("Error: Rol desconocido '" + rol + "' en usuarios.json");
            }
        }
        return listaUsuarios;
    }
}