package presentacion;



import java.util.Scanner;
import logica.BoletaMaster;
import persistencia.PersistenciaDatos;
import Usuarios.Cliente;
import Usuarios.Organizador;
import Usuarios.Administrador;

public class consolaPrincipal {
    private final Scanner sc = new Scanner(System.in);
    private BoletaMaster sistema;

    public void ConsolaPrincipal() {
        PersistenciaDatos p = new PersistenciaDatos();
        this.sistema = new BoletaMaster(null, null, null, null, null, p, null);
        sistema.cargarDatos();
    }

    private int pedirEntero(String msg, int min, int max) {
        while (true) {
            System.out.print(msg + " ");
            String s = sc.nextLine();
            try {
                int v = Integer.parseInt(s);
                if (v < min || v > max) continue;
                return v;
            } catch (Exception e) {}
        }
    }

    private String pedirTexto(String msg) {
        while (true) {
            System.out.print(msg + " ");
            String s = sc.nextLine();
            if (!s.trim().isEmpty()) return s.trim();
        }
    }

    private void mostrarMenu() {
        while (true) {
            System.out.println("=== BoletaMaster ===");
            System.out.println("1. Ingresar como Cliente");
            System.out.println("2. Ingresar como Organizador");
            System.out.println("3. Ingresar como Administrador");
            System.out.println("4. Guardar y Salir");
            int op = pedirEntero("Opción:", 1, 4);
            if (op == 4) {
                sistema.guardarDatos();
                System.out.println("OK");
                break;
            }
            String login = pedirTexto("Login:");
            String pass = pedirTexto("Password:");
            if (op == 1) {
                Cliente c = (Cliente) sistema.buscarUsuario(login);
                if (c != null && c.autenticar(pass)) new ConsolaCliente(sistema, c).mostrarMenu();
            } else if (op == 2) {
                Organizador o = (Organizador) sistema.buscarUsuario(login);
                if (o != null && o.autenticar(pass)) new ConsolaOrganizador(sistema, o).mostrarMenu();
            } else if (op == 3) {
                Administrador a = (Administrador) sistema.buscarUsuario(login);
                if (a != null && a.autenticar(pass)) new ConsolaAdministrador(sistema, a).mostrarMenu();
            }
        }
    }

    public static void main(String[] args) {
        new consolaPrincipal().mostrarMenu();
    }}