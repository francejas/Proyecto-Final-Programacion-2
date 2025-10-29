package Entidades;

import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class OperacionesLectoEscritura {

    public OperacionesLectoEscritura() {
    }
    //guardan en un archivo json
    public static void grabar(String nombreArchivo, JSONObject jsonObject){
        try (FileWriter fileWriter = new FileWriter(nombreArchivo)){
    fileWriter.write(jsonObject.toString(3));
        } catch (IOException e) {
           e.printStackTrace();// throw new RuntimeException(e);
        }
    }

public static void grabar(String nombreArchivo, JSONArray jsonArray){
        try (FileWriter fileWriter=new FileWriter(nombreArchivo)){
            fileWriter.write(jsonArray.toString(4));
        } catch (IOException e) {
           e.printStackTrace();// throw new RuntimeException(e);
        }
}
    // Metodo para leer un archivo JSON y devolver un JSONTokener
public static JSONTokener leer(String nombreArchivo){
        JSONTokener tokener=null;
        try {
            tokener=new JSONTokener(new FileReader(nombreArchivo));
        } catch (FileNotFoundException | JSONException e) {
            throw new RuntimeException(e);
        }
        return tokener;
}




}
