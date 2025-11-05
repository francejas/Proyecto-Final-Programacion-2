package Entidades;

import Enum.RolUsuario;

public abstract class Usuario {
    private static int contadorUsuarios = 0;
    private final int id;
    private String nombre;
    private String email;
    private String contrasena;
    private RolUsuario rol;//admin o cliente
    private boolean activo;

    
    public Usuario() {
        this.id = ++contadorUsuarios;
    }

    public Usuario(String nombre, String email, String contrasena) {
        this.id = ++contadorUsuarios;
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.rol = RolUsuario.CLIENTE ;
        this.activo = true;
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
    public String getContrasena() {
        return contrasena;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
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



///metodos
    public abstract void mostrarMenu();

    public abstract void registrarse();//alta
    public abstract void iniciarSesion();
    public abstract void cerrarSesion();
    public abstract void actualizarPerfil();//modificacion
    public abstract void eliminarCuenta();//baja
    public abstract void mostrarDatosUsuario(); 
    public abstract void mostrarMenuInicio();



@Override
    public String toString() {
        return "Usuario [id=" + id +
         ", nombre=" + nombre +
          ", email=" + email +
           ", rol=" + rol + 
           ", activo=" + activo + "]";
        
    }


}

