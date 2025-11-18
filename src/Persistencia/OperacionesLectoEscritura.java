package Persistencia;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public final class OperacionesLectoEscritura {

    private OperacionesLectoEscritura() {
    }

    public static void grabar(String nombreArchivo, JSONObject jsonObject){
        try (FileWriter fileWriter = new FileWriter(nombreArchivo)){
            fileWriter.write(jsonObject.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void grabar(String nombreArchivo, JSONArray jsonArray){
        try (FileWriter fileWriter = new FileWriter(nombreArchivo)){
            fileWriter.write(jsonArray.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JSONTokener leer(String nombreArchivo){
        JSONTokener tokener = null;
        try {
            tokener = new JSONTokener(new FileReader(nombreArchivo));
        } catch (FileNotFoundException e) {
            // la primera vez qe corra no va a encotnrara el archivo, informamos esto
            System.out.println("Nota: No se encontró el archivo '" + nombreArchivo + "'. Se creará uno nuevo al guardar.");
        }
        return tokener;
    }
}