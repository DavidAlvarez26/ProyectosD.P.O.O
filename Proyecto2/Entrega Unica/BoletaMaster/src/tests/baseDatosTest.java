package tests;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import Finanzas.Oferta;
import Tiquetes.TiqueteBasico;
import Usuarios.Administrador;
import Usuarios.Cliente;
import Usuarios.Organizador;
import logica.BoletaMaster;
import logica.Evento;
import logica.Localidad;
import logica.Tiquete;
import logica.Venue;
import persistencia.PersistenciaDatos;

public class baseDatosTest {
    protected BoletaMaster sistema;
    protected Administrador admin;
    protected Organizador org;
    protected Cliente c1, c2;
    protected Venue venueAprobado;
    protected Evento evento;
    protected Localidad loc;

    protected void SistemaBasico() {
        sistema = new BoletaMaster(null, null, null, null, null, new PersistenciaDatos(), null);

        // Usuarios
        admin = new Administrador("admin","123","Admin","admin@mail.com",0, 1000, null);
        org   = new Organizador("org","xxx","María Gómez","maria@mail.com",0,new ArrayList<>());
        c1    = new Cliente("cli1","p1","Juan Rodríguez","juan@mail.com",120_000,new ArrayList<>());
        c2    = new Cliente("cli2","p2","Laura Torres","laura@mail.com",95_000,new ArrayList<>());

        sistema.registrarUsuario(admin);
        sistema.registrarUsuario(org);
        sistema.registrarUsuario(c1);
        sistema.registrarUsuario(c2);

        // Venue aprobado
        venueAprobado = new Venue("Calle 1 #2-3", 10_000, "V001", "MovistarArena", true);
        // Evento con una localidad
        loc = new Localidad("VIP", 10_000, true, 100, new ArrayList<>(), null);
        List<Localidad> locs = new ArrayList<>();
        locs.add(loc);
        evento = new Evento("E001","Concierto Coldplay",
                Date.valueOf("2025-11-30"), Time.valueOf("20:00:00"),
                "Música", venueAprobado, org, locs, "Activo");
        sistema.agregarEvento(evento);
        for (int i = 1; i <= 5; i++) {
            Tiquete t = new TiqueteBasico(1_000, 500, i, evento, null, loc, "Disponible", true, "A"+i);
            loc.getTiquetes().add(t);
        }
    }

    protected double precioTiquete(Tiquete t) {
        return t.calcularPrecioTotal();
    }

    protected Oferta ofertaVigente25() {
        return new Oferta(0.25, Date.valueOf("2025-11-01"), Date.valueOf("2025-12-30"));
    }
}
