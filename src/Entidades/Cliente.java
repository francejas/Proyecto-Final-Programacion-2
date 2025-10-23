package Entidades;

import java.util.List;
import java.util.Scanner;

public class Cliente extends Usuario {
    private int millasAcumuladas;// se acumulan con cada vuelo mediante el costo final de la reserva
    // private List<Reserva> historialreservas;
    private Scanner sc = new Scanner(System.in);

    // constructores
    public Cliente() {
        super();
        this.millasAcumuladas = 0;
        // this.historialreservas = historialreservas;
    }

    // constructor con parametros
    public Cliente(String nombre, String email, String contrasena) {
        super(nombre, email, contrasena);
        this.millasAcumuladas = 0;
        // this.historialreservas = historialreservas;
    }

    /// getters y setters
    public int getMillasAcumuladas() {
        return millasAcumuladas;
    }

    public void setMillasAcumuladas(int millasAcumuladas) {
        this.millasAcumuladas = millasAcumuladas;
    }

    // public List<Reserva> getHistorialreservas() {
    // return historialreservas;
    // }

    // public void setHistorialreservas(List<Reserva> historialreservas) {
    // this.historialreservas = historialreservas;
    // }

    /// metodos

    // public void verHistorialReservas();
    @Override
    public void mostrarMenuInicio() {
        System.out.println("----- Bienvenido a la Agencia de Viajes -----");
        System.out.println("1. Registrarse");
        System.out.println("2. Iniciar Sesion");
        System.out.println("3. Salir");
        System.out.print("Seleccione una opcion: ");
        int opcion = sc.nextInt();
        sc.nextLine(); // Limpiar el buffer
        switch (opcion) {
            case 1:
                registrarse();
                break;
            case 2:
                iniciarSesion();
                break;
            case 3:
                System.out.println("Gracias por visitar la Agencia de Viajes. ¡Hasta luego!");
                break;
            default:
                System.out.println("Opcion invalida. Por favor, intente de nuevo.");
                mostrarMenuInicio();
                break;
        }
    }

    @Override
    public void mostrarMenu() {
        System.out.println("----- Menu Cliente -----");
        System.out.println("1. Buscar Vuelos");
        System.out.println("2. Ver Historial de Reservas");
        System.out.println("3. Actualizar Perfil");
        System.out.println("4. Eliminar Cuenta");
        System.out.println("5. Cerrar Sesion");
        System.out.print("Seleccione una opcion: ");
        int opcion = sc.nextInt();
        sc.nextLine(); // Limpiar el buffer
        switch (opcion) {
            case 1:
                // buscarVuelos();
                break;
            case 2:
                // verHistorialReservas();
                break;
            case 3:
                actualizarPerfil();
                break;
            case 4:
                eliminarCuenta();
                break;
            case 5:
                cerrarSesion();
                break;
            default:
                System.out.println("Opcion invalida. Por favor, intente de nuevo.");
                mostrarMenu();
                break;
        }

    }

    public boolean validaremail(String email) {
        int arroba = 0;
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '@') {
                arroba++;
                if (arroba > 1) {
                    System.out.println("email invalido, intente de nuevo");
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public void registrarse() {
        String nombre = null, email = null, contrasena = null;
        System.out.println("ingrese sus datos para registrarse\n");
        System.out.println("nombre: ");
        nombre = sc.nextLine();
        do {
            System.out.println("email: ");// validar que no exista ya en el sistema y que sea un email valido
            email = sc.nextLine();
        } while (validaremail(email)==false);
        System.out.println("contrasena: ");
        contrasena = sc.nextLine();
        Cliente clienteNuevo = new Cliente(nombre, email, contrasena);
        System.out.println("registro exitoso, ya puede iniciar sesion");
        // mostrar menu cliente
        mostrarMenu();

    }

    @Override
    public void iniciarSesion() {
        String email = null, contrasena = null;
        System.out.println("ingrese sus datos para iniciar sesion\n");
        System.out.println("email: ");// validar que exista en el sistema y que sea un email valido
        email = sc.nextLine();
        System.out.println("contrasena: ");
        contrasena = sc.nextLine();
        // validar que la contrasena sea correcta
        System.out.println("sesion iniciada correctamente");
        // mostrar menu cliente
        mostrarMenu();

    }

    @Override
    public void cerrarSesion() {
        System.out.println("sesion cerrada correctamente");
        // volver a la pantalla de inicio
        mostrarMenuInicio();
    }

    @Override
    public void actualizarPerfil() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actualizarPerfil'");
    }

    @Override
    public void eliminarCuenta() {
        System.out.println("para eliminar su cuenta, por favor ingrese " + "CONFIRMAR" + " Y su contrasena: ");
        String confirmar = sc.nextLine();
        if (!confirmar.equals("CONFIRMAR")) {
            System.out.println("eliminacion de cuenta cancelada");
            return;
        }
        String contrasena = sc.nextLine();
        // validar que la contrasena sea correcta

        //en el array de usuarios, cambiar el atributo activo a false
        System.out.println("su cuenta ha sido eliminada correctamente");
        // volver a la pantalla de inicio
        // mostrarMenuInicio();
    }

    @Override
    public void mostrarDatosUsuario() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mostrarDatosUsuario'");
    }

    @Override
    public String toString() {
        return "Cliente [millasAcumuladas=" + millasAcumuladas +
                super.toString() + "]";
    }

}
