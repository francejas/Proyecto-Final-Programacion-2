package Entidades;

import Enum.RolUsuario;

public abstract class Usuario {
    private static int contadorUsuarios = 0;
    private final int id;
    private String nombre;
    private String email;
    private String contrasena;
    private RolUsuario rol;
    private boolean activo;

    public Usuario() {
        this.id = ++contadorUsuarios;
    }

    public Usuario(String nombre, String email, String contrasena, RolUsuario rol) {
        this.id = ++contadorUsuarios;
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.rol = rol;
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


    public abstract void mostrarMenu();

    public void mostrarInfo() {
        System.out.println("ID: " + id);
        System.out.println("Nombre: " + nombre);
        System.out.println("Email: " + email);
    }


}

