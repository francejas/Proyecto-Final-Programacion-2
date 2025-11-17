package Servicios;

import Entidades.Avion;
import Persistencia.JsonManagerAviones;

import java.util.ArrayList;
import java.util.List;

public class GestorAviones implements Gestionable<Avion, String> {
    private List<Avion> aviones;
    private JsonManagerAviones jsonManager;

    public GestorAviones() {
        this.jsonManager = new JsonManagerAviones();

        this.aviones = jsonManager.leerLista();
    }

    private void guardarEnJson(){
        jsonManager.guardarLista(this.aviones);
    }

    @Override
    public void alta(Avion avion) {
        this.aviones.add(avion);
        guardarEnJson();
    }

    @Override
    public void baja(String matricula){
        Avion avionOriginal = this.consulta(matricula);
        if(avionOriginal != null) {
            this.aviones.remove(avionOriginal);
            guardarEnJson();
        }
    }

    @Override
    public void modificacion(Avion avionModificado){
        Avion avionOriginal = this.consulta(avionModificado.getMatricula());
        if(avionOriginal == null) return;

        avionOriginal.setMatricula(avionModificado.getMatricula());
        avionOriginal.setModelo(avionModificado.getModelo());
        avionOriginal.setCapacidadBusiness(avionModificado.getCapacidadBusiness());
        avionOriginal.setCapacidadEconomy(avionModificado.getCapacidadEconomy());

        guardarEnJson();
    }

    @Override
    public Avion consulta(String matricula){
        for(Avion avion : this.aviones){
            if(avion.getMatricula().equals(matricula)){
                return avion;
            }
        }
        return null;
    }

    @Override
    public List<Avion> listar(){
        return new ArrayList<>(this.aviones);
    }
}
