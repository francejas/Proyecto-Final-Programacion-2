package Entidades;

import Enum.RolUsuario;

public class Administrador extends Usuario {

    public Administrador(String nombre, String email, String password, boolean activo) {
        super(nombre, email, password, RolUsuario.ADMINISTRADOR, activo);
    }

    @Override
    public String toString() {
        return "Administrador{" +
                "usuario_info=" + super.toString() +
                '}';
    }
}