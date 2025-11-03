package tests;

import Tiquetes.TiqueteBasico;
import Usuarios.Cliente;
import logica.BoletaMaster;
import logica.Evento;
import logica.Localidad;
import persistencia.PersistenciaDatos;
import logica.Tiquete;

public class TestCompraTiquetes {
    public static void main(String[] args) {
        PersistenciaDatos persistencia = new PersistenciaDatos();
        BoletaMaster sistema = new BoletaMaster(null, null, null, null, null, persistencia);
        sistema.cargarDatos();

        Cliente cliente = (Cliente) sistema.buscarUsuario("cliente01");
        Evento evento = sistema.buscarEvento("E001");
        Localidad localidad = evento.getLocalidades().get(0);
        for (int i = 1; i <= 5; i++) {
            Tiquete t = new TiqueteBasico(10000, 2000, i, evento, null, localidad, "Disponible", true, "A" + i);
            localidad.getTiquetes().add(t);
        }
        System.out.println("\n===== SIMULACIÓN DE COMPRA DE TIQUETES =====");
        System.out.println("Cliente: " + cliente.getNombre());
        System.out.println("Evento: " + evento.getNombre());
        System.out.println("Localidad: " + localidad.getNombre());
        System.out.println("Saldo inicial: $" + cliente.getSaldo());
        System.out.println("Tiquetes disponibles antes de compra: " + localidad.getTiquetes().size());
        System.out.println("---------------------------------------------");

        sistema.comprarTiquete(cliente, evento, localidad, 2);

        System.out.println("\n===== RESULTADOS DE LA COMPRA =====");
        System.out.println("Nuevo saldo del cliente: $" + cliente.getSaldo());
        System.out.println("Tiquetes comprados:");

        for (Tiquete t : cliente.getTiquetes()) {
            System.out.println("ID: " + t.getIdTiquete()
                    + " | Asiento: " + ((t instanceof TiqueteBasico) ? ((TiqueteBasico) t).getNumeroAsiento() : "N/A")
                    + " | Estado: " + t.getEstado()
                    + " | Precio total: $" + t.calcularPrecioTotal());
        }

        System.out.println("\n ===== ESTADO FINAL DE LOCALIDAD =====");
        for (Tiquete t : localidad.getTiquetes()) {
            System.out.println("Tiquete " + t.getIdTiquete()
                    + " | Estado: " + t.getEstado()
                    + " | Comprador: " + (t.getComprador() != null ? t.getComprador().getNombre() : "Ninguno"));
        }

        System.out.println("\nTiquetes disponibles después de la compra: "
                + localidad.obtenerTiquetesDisponibles().size());
        System.out.println("=============================================\n");
    }
}
