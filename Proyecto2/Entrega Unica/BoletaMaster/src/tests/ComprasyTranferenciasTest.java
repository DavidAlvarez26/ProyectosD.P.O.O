package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import Finanzas.Compra;
import Tiquetes.TiqueteBasico;
import Usuarios.Cliente;
import Usuarios.Organizador;
import logica.BoletaMaster;
import logica.Evento;
import logica.Localidad;
import logica.Venue;
import persistencia.PersistenciaDatos;

class ComprasyTranferenciasTest {

	private BoletaMaster sistema;
    private Cliente c1, c2;
    private Evento evento;
    private Localidad loc;

    private void setUp() {
        sistema = new BoletaMaster(null, null, null, null, null, new PersistenciaDatos());

        c1 = new Cliente("cli1","x","Juan","j@x.com", 100000, new ArrayList<>());
        c2 = new Cliente("cli2","x","Laura","l@x.com",  50000, new ArrayList<>());
        Organizador org = new Organizador("org1","x","María","m@x.com",0,null);
        sistema.registrarUsuario(c1);
        sistema.registrarUsuario(c2);
        sistema.registrarUsuario(org);

        loc = new Localidad("VIP", 20000, true, 10, new ArrayList<>(), null);
        Venue v = new Venue("Centro", 1000, "V1","Arena", true);
        evento = new Evento("E200","Show", Date.valueOf("2026-03-01"),
                Time.valueOf("20:00:00"), "Música", v, org, new ArrayList<>(),"Activo");
        evento.getLocalidades().add(loc);
        sistema.agregarEvento(evento);
        for (int i = 1; i <= 3; i++) {
            loc.getTiquetes().add(new TiqueteBasico(
                0, 0, i, evento, null, loc, "Disponible", true, "A"+i));
        }
    }

    @Test
    @DisplayName("Comprar reduce saldo, marca tiquetes vendidos y registra compra")
    void comprarTiquetes() {
        setUp();

        int antesDisponibles = loc.obtenerTiquetesDisponibles().size();
        sistema.comprarTiquete(c1, evento, loc, 2);
        int despuesDisponibles = loc.obtenerTiquetesDisponibles().size();

        assertEquals(antesDisponibles - 2, despuesDisponibles);
        assertEquals(2, c1.getTiquetes().size());
        assertTrue(c1.getSaldo() < 100000);

        Compra comp = new Compra("C1", c1, c1.getTiquetes(), new Date(System.currentTimeMillis()), 0,"Saldo");
        sistema.registrarCompra(comp);
    }

    @Test
    @DisplayName("Transferir mueve el tiquete al destino y cambia estado")
    void transferirTiquete() {
        setUp();
        sistema.comprarTiquete(c1, evento, loc, 1);
        var t = c1.getTiquetes().get(0);

        sistema.transferirTiquete(c1, c2, t);

        assertFalse(c1.getTiquetes().contains(t));
        assertTrue(c2.getTiquetes().contains(t));
        assertEquals(c2, t.getComprador());
        assertEquals("Transferido", t.getEstado());
    }

}
