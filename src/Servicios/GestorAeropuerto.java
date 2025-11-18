package Servicios;

import Entidades.Aeropuerto;
import Excepciones.DatoInvalidoException;
import Excepciones.EmailYaRegistradoException;
import Excepciones.PasswordInvalidaException;
import Persistencia.JsonManagerAeropuertos;

import java.util.ArrayList;
import java.util.List;

public class GestorAeropuerto implements Gestionable<String, Aeropuerto> {
    private List<Aeropuerto> aeropuertos;

    private final JsonManagerAeropuertos jsonManager;

    public GestorAeropuerto() {
        this.jsonManager = new JsonManagerAeropuertos();
        this.aeropuertos = jsonManager.leerLista();
    }

    private void guardarJson(){
        jsonManager.guardarLista(this.aeropuertos);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }


    public void alta(Aeropuerto aeropuerto) throws EmailYaRegistradoException, PasswordInvalidaException, DatoInvalidoException {
        this.aeropuertos.add(aeropuerto);
        guardarJson();
    }

    public void baja(String codigoIATA) {
        Aeropuerto aeropuerto = this.consulta(codigoIATA);
        if(aeropuerto!=null){
            this.aeropuertos.remove(aeropuerto);
            guardarJson();
        }
    }




    public void modificacion(Aeropuerto aeropuerto) throws DatoInvalidoException {
        Aeropuerto aeropuertoAaux = this.consulta(aeropuerto.getCodigoIATA());
        if(aeropuertoAaux == null) return;
        aeropuertoAaux.setCiudad(aeropuerto.getCiudad());
        aeropuertoAaux.setNombre(aeropuerto.getNombre());
        guardarJson();
    }


    public Aeropuerto consulta(String codigoIata) {
        for(Aeropuerto aeropuerto : this.aeropuertos){
            if(aeropuerto.getCodigoIATA().equals(codigoIata)){
                return aeropuerto;
            }
        }
        return null;
    }

    public List<Aeropuerto> listar() {
        return new ArrayList<>(this.aeropuertos);
    }
}