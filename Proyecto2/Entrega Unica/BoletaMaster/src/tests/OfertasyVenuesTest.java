package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;

import Finanzas.Compra;
import Finanzas.Oferta;
import Tiquetes.TiqueteBasico;
import Usuarios.Administrador;
import Usuarios.Cliente;
import Usuarios.Organizador;
import logica.BoletaMaster;
import logica.Evento;
import logica.Localidad;
import logica.Venue;
import persistencia.PersistenciaDatos;
class OfertasyVenuesTest {

	 @Test
	    @DisplayName("Proponer y aprobar Venue")
	    void proponerYAprobarVenue() {
	        BoletaMaster sistema = new BoletaMaster(null, null, null, null, null, new PersistenciaDatos());
	        Organizador org = new Organizador("org","x","María","m@x.com",0,null);
	        Administrador admin = new Administrador("adm","x","Carlos","c@x.com",0,1000,null);

	        sistema.registrarUsuario(org);

	        Venue v = new Venue("Centro", 2000, "V3", "Centro Cultural", false);
	        sistema.proponerVenue(org, v);
	        assertFalse(v.esAprobado());

	        sistema.aprobarVenue(admin, v, true);
	        assertTrue(v.esAprobado());
	    }

	    @Test
	    @DisplayName("Aplicar oferta en una localidad afecta el precio base de los tiquetes nuevos")
	    void aplicarOfertaEnLocalidad() {
	        BoletaMaster sistema = new BoletaMaster(null, null, null, null, null, new PersistenciaDatos());
	        Organizador org = new Organizador("org","x","María","m@x.com",0,null);
	        sistema.registrarUsuario(org);

	        Venue v = new Venue("C", 1000, "V","Coliseo", true);
	        Localidad loc = new Localidad("General", 20000, false, 50, new ArrayList<>(), null);
	        Evento e = new Evento("E10","Show", Date.valueOf("2026-05-01"), Time.valueOf("20:00:00"),
	                "Música", v, org, new ArrayList<>(),"Activo");
	        e.getLocalidades().add(loc);
	        sistema.agregarEvento(e);

	        Oferta oferta = new Oferta(0.25, Date.valueOf("2026-04-01"), Date.valueOf("2026-06-01"));
	        sistema.aplicarOferta(e, loc, oferta);

	        assertEquals(oferta, loc.getDescuento());
	    }

}
