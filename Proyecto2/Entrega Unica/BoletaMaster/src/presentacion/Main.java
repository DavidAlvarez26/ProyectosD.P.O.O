package presentacion;



import logica.*;
import persistencia.PersistenciaDatos;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PersistenciaDatos persistencia = new PersistenciaDatos();
        BoletaMaster sistema = new BoletaMaster(null, null, null, null, null, persistencia);
        sistema.cargarDatos();

        System.out.println("=== BoletaMaster ===");
        System.out.println("1. Consultar catálogo");
        System.out.println("4. Salir");

        int opcion = sc.nextInt();
        switch (opcion) {
            case 1 -> { sistema.consultarCatalogoEventos(null, null, null)
                .forEach(e -> System.out.println(" - " + e.getNombre())); 

            }

            default -> System.out.println("Adios");
        }
        if(opcion==1) {
        System.out.println("\n¿Deseas guardar los cambios en los archivos? (si/no)");
        String respuesta = sc.next();

        if (respuesta.equalsIgnoreCase("si")) {
            sistema.guardarDatos();
       
        }
        }
        sc.close();
    }
}
