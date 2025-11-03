package tests;

import Tiquetes.TiqueteBasico;
import Usuarios.Cliente;
import logica.BoletaMaster;
import logica.Evento;
import logica.Localidad;
import logica.Tiquete;
import persistencia.PersistenciaDatos;

public class TestTransferenciaTiquetes {
    public static void main(String[] args) {
        PersistenciaDatos persistencia = new PersistenciaDatos();
        BoletaMaster sistema = new BoletaMaster(null, null, null, null, null, persistencia);
        sistema.cargarDatos();

        Cliente origen = (Cliente) sistema.buscarUsuario("cliente01"); 
        Cliente destino = (Cliente) sistema.buscarUsuario("cliente02");
        Evento evento = sistema.buscarEvento("E003"); 
        Localidad localidad = evento.getLocalidades().get(0);
        Tiquete t = new TiqueteBasico(100000, 5000, 7001, evento, origen, localidad, "Vendido", true, "A1");
        origen.getTiquetes().add(t);

        System.out.println("\n===== TRANSFERENCIA DE TIQUETE =====");
        System.out.println("Origen: " + origen.getNombre());
        System.out.println("Destino: " + destino.getNombre());
        System.out.println("Tiquete ID: " + t.getIdTiquete() + " | Estado: " + t.getEstado());
        System.out.println("Comprador original: " + t.getComprador().getNombre());
        sistema.transferirTiquete(origen, destino, t);
        System.out.println("\n===== RESULTADO DE TRANSFERENCIA =====");
        System.out.println("Tiquete ahora pertenece a: " + t.getComprador().getNombre());
        System.out.println("Nuevo estado del tiquete: " + t.getEstado());
        System.out.println("Tiquetes del cliente origen: " + origen.getTiquetes().size());
        System.out.println("Tiquetes del cliente destino: " + destino.getTiquetes().size());
    }
}