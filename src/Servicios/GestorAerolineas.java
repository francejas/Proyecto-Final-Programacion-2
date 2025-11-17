public class GestorAerolineas implements Gestionable<Aerolinea, String> {

    private List<Aerolinea> aerolineas;
    private JsonManagerAerrolineas jsonManager;

    public GestorAerolineas() {
        this.jsonManager = new JsonManagerAerrolineas();
        this.aerolineas = jsonManager.leerLista();
    }

    public List<Aerolinea> getAerolineas() {
        return aerolineas;
    }

    @Override
    public void baja(String id) {
        aerolineas.removeIf(aerolinea -> aerolinea.getCodigo().equals(id));
        jsonManager.escribirLista(aerolineas);
    }

    @Override
    public void modificacion(Aerolinea aerolineaModificada) throws  {
        for (int i = 0; i < aerolineas.size(); i++) {
            if (aerolineas.get(i).getCodigo().equals(objeto.getCodigo())) {
                aerolineas.set(i, objeto);
                break;
            }
        }
        jsonManager.escribirLista(aerolineas);
    }

}
