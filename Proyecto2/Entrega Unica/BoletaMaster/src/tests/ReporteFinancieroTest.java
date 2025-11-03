package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Finanzas.Compra;
import Finanzas.ReporteFinanciero;
import Tiquetes.TiqueteBasico;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;

import Usuarios.Cliente;
import Usuarios.Organizador;
import logica.BoletaMaster;
import logica.Evento;
import logica.Localidad;
import logica.Tiquete;
import logica.Venue;
import persistencia.PersistenciaDatos;
class ReporteFinancieroTest {

	@Test
    @DisplayName("Reporte: totales, por evento y por organizador")
    void reporteBasico() {
        BoletaMaster sistema = new BoletaMaster(null, null, null, null, null, new PersistenciaDatos());

        Organizador org = new Organizador("org1","x","María","m@x.com",0,null);
        Cliente c = new Cliente("cli1","x","Juan","j@x.com",100000, new ArrayList<>());
        sistema.registrarUsuario(org);
        sistema.registrarUsuario(c);

        Venue v = new Venue("C", 1000, "V1","Arena", true);
        Localidad loc = new Localidad("Gen", 15000, false, 10, new ArrayList<>(), null);
        Evento e = new Evento("E1","Evento", Date.valueOf("2026-04-01"), Time.valueOf("18:00:00"),
                "Música", v, org, new ArrayList<>(),"Activo");
        e.getLocalidades().add(loc);
        sistema.agregarEvento(e);

        // crear 2 tiquetes vendidos a mano y compra
        var t1 = new TiqueteBasico(0,0,1,e,c,loc,"Vendido",true,"A1");
        var t2 = new TiqueteBasico(0,0,2,e,c,loc,"Vendido",true,"A2");
        List<Tiquete> lista = new ArrayList<>();
        lista.add(t1); lista.add(t2);

        Compra comp = new Compra("C1", c, lista, new Date(System.currentTimeMillis()), 0, "Saldo");
        sistema.registrarCompra(comp);

        ReporteFinanciero r = sistema.generarReporteFinanciero(new Usuarios.Administrador("a","x","Admin","a@x",0,0,null));
        assertNotNull(r);
        assertTrue(r.getGananciasTotales() > 0);
        assertTrue(r.getGananciasxEvento().containsKey(e));
        assertTrue(r.getGananciasxOrganizador().containsKey(org));
    }

}
