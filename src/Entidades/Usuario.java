package Entidades;

import Enum.RolUsuario;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public abstract class Usuario {

    private static int contadorId = 0;
    private int id;
    private String nombre;
    private String email;
    private String password;
    private RolUsuario rol;
    private boolean activo;


    protected Usuario(String nombre, String email, String password, RolUsuario rol, boolean activo) {
        this.id = ++contadorId;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.activo = activo;
    }

    /**
     * Constructor para DESERIALIZAR desde JSON.
     * Asigna el ID leído del archivo, NO genera uno nuevo.
     */
    protected Usuario(JSONObject json) throws JSONException {
        this.id = json.getInt("id");
        this.nombre = json.getString("nombre");
        this.email = json.getString("email");
        this.password = json.getString("password");
        this.rol = RolUsuario.valueOf(json.getString("rol"));
        this.activo = json.getBoolean("activo");
    }

    /**
     * Inicializa el contador estático.
     * Debe llamarse al inicio de la app después de leer el JSON.
     */
    public static void inicializarContadorId(int maxIdEncontrado) {
        contadorId = maxIdEncontrado;
    }

    /**
     * Convierte el objeto Usuario a formato JSON.
     * Este metodo sera llamado por las clases hijas super.toJSON()
     */
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.id);
        jsonObject.put("nombre", this.nombre);
        jsonObject.put("email", this.email);
        jsonObject.put("password", this.password);
        jsonObject.put("rol", this.rol.toString());
        jsonObject.put("activo", this.activo);
        return jsonObject;
    }

    // --- Getters y Setters ---

    public static int getContadorId() {
        return contadorId;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    // --- equals() y hashCode() ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(email, usuario.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", rol=" + rol +
                ", activo=" + activo +
                '}';
    }
}