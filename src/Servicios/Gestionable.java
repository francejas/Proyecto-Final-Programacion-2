package Servicios;

import Excepciones.DatoInvalidoException;
import Excepciones.EmailYaRegistradoException;
import Excepciones.PasswordInvalidaException;

import java.util.List;

/**
 * Interfaz genérica que define el contrato estándar para las operaciones
 * de ABMCL (Alta, Baja, Modificación, Consulta, Listado).
 */
public interface Gestionable<T, ID> {

    /**
     * Agrega un nuevo objeto al sistema.
     * Puede lanzar excepciones de validación.
     */
    void alta(T objeto) throws EmailYaRegistradoException, PasswordInvalidaException, DatoInvalidoException;

    /**
     * Realiza la baja (lógica) de un objeto.
     */
    void baja(ID id);

    /**
     * Reemplaza un objeto existente con uno modificado.
     */
    void modificacion(T objeto) throws DatoInvalidoException;

    /**
     * Busca y devuelve un objeto por su ID.
     */
    T consulta(ID id);

    /**
     * Devuelve una lista de todos los objetos.
     */
    List<T> listar();

}