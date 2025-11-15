package Servicios;

import java.util.List;

public interface Gestionable<T, ID> {

    void baja(ID id);
    void modificacion(T objeto);
    T consulta(ID id);
    List<T> listar();
}