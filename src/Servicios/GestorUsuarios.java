package Servicios;

import Entidades.Usuario;
import Entidades.Cliente;
import Excepciones.DatoInvalidoException;
import Excepciones.EmailYaRegistradoException;
import Excepciones.LoginFallidoException;
import Excepciones.PasswordInvalidaException;
import Persistencia.JsonManagerUsuarios;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Maneja la lógica de negocio para la gestión de Usuarios.
 * Implementa la interfaz Gestionable para el ABMCL del administrador
 * y añade métodos específicos para el login y la autogestión del cliente.
 */
public class GestorUsuarios implements Gestionable<Usuario, Integer> {

    private List<Usuario> usuarios; // Lista maestra en memoria de TODOS los usuarios
    private final JsonManagerUsuarios jsonManager;

    // Expresión regular (Regex) para validar un email
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    // --- Constructor ---
    public GestorUsuarios() {
        this.jsonManager = new JsonManagerUsuarios();

        // 1. Carga la lista desde el JSON
        this.usuarios = jsonManager.leerLista();

        // 2. Iinicializar el contador ID
        // Busca el ID más alto en la lista cargada
        int maxId = 0;
        for (Usuario u : this.usuarios) {
            if (u.getId() > maxId) {
                maxId = u.getId();
            }
        }
        // 3. Pasa el ID más alto a la clase Usuario
        // Esto asegura que el 'static contadorId' de Usuario
        // no empiece en 0 y genere IDs duplicados.
        Usuario.inicializarContadorId(maxId);
    }

    /**
     * Metodo helper privado para centralizar el guardado en JSON.
     */
    private void guardarEnJson() {
        jsonManager.guardarLista(this.usuarios);
    }

    // --- Métodos de Lógica de Negocio (Login/Registro) ---

    public Usuario login(String email, String password) throws LoginFallidoException {
        // Busca al usuario por email (usando el helper privado)
        Usuario u = buscarPorEmail(email);

        if (u != null) { // Si el email existe...
            if (u.getPassword().equals(password)) { // ...comprueba la contraseña
                if (u.isActivo()) {
                    return u; // Login exitoso
                } else {
                    // El usuario existe pero está desactivado (baja lógica)
                    throw new LoginFallidoException("La cuenta está desactivada. Contacte al administrador.");
                }
            } else {
                // Contraseña incorrecta
                throw new LoginFallidoException("Email o contraseña incorrectos.");
            }
        }
        // El email no existe
        throw new LoginFallidoException("Email o contraseña incorrectos.");
    }

    public void registrarUsuario(String nombre, String email, String password, String DNI, LocalDate fechaNac)
            throws EmailYaRegistradoException, PasswordInvalidaException, DatoInvalidoException {

        // 1. Validar el formato del email
        validarFormatoEmail(email); // Llama al nuevo metodo

        // 2. Validar que el email no exista
        if (buscarPorEmail(email) != null) {
            throw new EmailYaRegistradoException("El email '" + email + "' ya está en uso.");
        }

        // 3. Validar la contraseña
        validarPassword(password);

        // 4. Crear y guardar
        // El ID se genera automáticamente en el constructor de Usuario
        Cliente nuevoCliente = new Cliente(nombre, email, password, true, 0, DNI, fechaNac);
        this.alta(nuevoCliente); // Llama al 'alta' de Gestionable
    }

    // --- Métodos Helper Privados (Validadores) ---

    /**
     * Metodo helper privado para validar el formato de un email.
     * @param email El email a verificar.
     * @throws DatoInvalidoException Si el formato no es válido (no contiene @ o .com, etc.)
     */
    private void validarFormatoEmail(String email) throws DatoInvalidoException {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new DatoInvalidoException("El formato del email no es válido.");
        }
    }

    private void validarPassword(String password) throws PasswordInvalidaException {
        StringBuilder errores = new StringBuilder();
        boolean tieneMayuscula = false;
        boolean tieneNumero = false;
        boolean tieneSimbolo = false;
        final int LONGITUD_MINIMA = 6;

        if (password.length() < LONGITUD_MINIMA) {
            errores.append("Debe tener al menos ").append(LONGITUD_MINIMA).append(" caracteres. ");
        }

        // Itera una sola vez para chequear todo
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) tieneMayuscula = true;
            if (Character.isDigit(c)) tieneNumero = true;
            if (!Character.isLetterOrDigit(c)) tieneSimbolo = true;
        }

        if (!tieneMayuscula) errores.append("Debe contener al menos una mayúscula. ");
        if (!tieneNumero) errores.append("Debe contener al menos un número. ");
        if (!tieneSimbolo) errores.append("Debe contener al menos un símbolo. ");

        // Si se acumuló algún error, se lanza la excepción
        if (errores.length() > 0) {
            throw new PasswordInvalidaException(errores.toString().trim());
        }
    }

    private Usuario buscarPorEmail(String email) {
        for (Usuario u : this.usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    // --- Métodos de Lógica (Autogestión del Cliente) ---

    public void actualizarMillas(int idCliente, int nuevasMillas) {
        Usuario usuario = this.consulta(idCliente);
        // Verifica que el usuario sea un Cliente (y no un Admin)
        if (usuario instanceof Cliente) {
            ((Cliente) usuario).setMillas(nuevasMillas);
            guardarEnJson(); // Persiste el cambio
        }
    }

    public void cambiarPassword(int idUsuario, String passwordActual, String passwordNueva)
            throws LoginFallidoException, PasswordInvalidaException {

        Usuario usuario = this.consulta(idUsuario);
        if (usuario == null) return;

        // 1. Validar la contraseña actual
        if (!usuario.getPassword().equals(passwordActual)) {
            throw new LoginFallidoException("La contraseña actual es incorrecta.");
        }

        // 2. Validar la nueva contraseña
        validarPassword(passwordNueva);

        // 3. Actualizar y guardar
        usuario.setPassword(passwordNueva);
        guardarEnJson();
    }

    /**
     * Permite a un usuario cambiar su email de login.
     * @throws DatoInvalidoException Si el formato del nuevo email es incorrecto.
     */
    public void modificarEmail(int idUsuario, String nuevoEmail)
            throws EmailYaRegistradoException, DatoInvalidoException {

        Usuario usuario = this.consulta(idUsuario);
        if (usuario == null) return;

        // 1. Validar el formato del nuevo email
        validarFormatoEmail(nuevoEmail);

        // 2. Validar que el nuevo email no este en uso por otro usuario
        Usuario existente = buscarPorEmail(nuevoEmail);
        if (existente != null && existente.getId() != idUsuario) {
            throw new EmailYaRegistradoException("El email '" + nuevoEmail + "' ya está en uso.");
        }

        // 3. Actualizar y guardar
        usuario.setEmail(nuevoEmail);
        guardarEnJson();
    }

    public void modificarDatosPersonales(int idUsuario, String nuevoNombre, LocalDate nuevaFechaNac, String nuevoDNI) {
        Usuario usuario = this.consulta(idUsuario);
        if (usuario instanceof Cliente) {
            usuario.setNombre(nuevoNombre);
            ((Cliente) usuario).setDNI(nuevoDNI);
            ((Cliente) usuario).setFechaDeNacimiento(nuevaFechaNac);
            guardarEnJson();
        }
    }


    // --- Métodos de la Interfaz Gestionable (Para el Admin) ---

    @Override
    public void alta(Usuario usuario) {
        // Este metodo 'alta' es genérico.
        // Es llamado por 'registrarUsuario' y también podría ser llamado
        // por el admin para crear un usuario (ej. otro admin)
        this.usuarios.add(usuario);
        guardarEnJson();
    }

    @Override
    public void baja(Integer id) {
        Usuario usuario = this.consulta(id);
        if (usuario != null) {
            usuario.setActivo(false); // Baja lógica
            guardarEnJson();
        }
    }

    /**
     * Modificación de un usuario por parte de un Administrador.
     * @throws DatoInvalidoException
     */
    @Override
    public void modificacion(Usuario usuarioModificado)
            throws DatoInvalidoException {

        Usuario usuarioOriginal = this.consulta(usuarioModificado.getId());
        if (usuarioOriginal == null) return;

        // El admin solo puede cambiar datos de perfil, rol y estado.
        usuarioOriginal.setNombre(usuarioModificado.getNombre());
        usuarioOriginal.setRol(usuarioModificado.getRol());
        usuarioOriginal.setActivo(usuarioModificado.isActivo());

        // Si es un cliente, actualizar también sus datos
        if (usuarioOriginal instanceof Cliente && usuarioModificado instanceof Cliente) {
            ((Cliente) usuarioOriginal).setDNI(((Cliente) usuarioModificado).getDNI());
            ((Cliente) usuarioOriginal).setFechaDeNacimiento(((Cliente) usuarioModificado).getFechaDeNacimiento());
            ((Cliente) usuarioOriginal).setMillas(((Cliente) usuarioModificado).getMillas());
        }
        guardarEnJson();
    }

    @Override
    public Usuario consulta(Integer id) {
        for (Usuario u : this.usuarios) {
            if (u.getId() == id) {
                return u;
            }
        }
        return null;
    }

    @Override
    public List<Usuario> listar() {
        return new ArrayList<>(this.usuarios); // Devuelve una copia
    }
}