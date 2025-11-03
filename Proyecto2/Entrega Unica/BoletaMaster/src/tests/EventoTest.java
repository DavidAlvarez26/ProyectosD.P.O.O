package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import Usuarios.Organizador;
import logica.BoletaMaster;
import logica.Evento;
import logica.Localidad;
import logica.Venue;
import persistencia.PersistenciaDatos;

class EventoTest {

	 private BoletaMaster nuevoSistemaVacio() {
	        return new BoletaMaster(null, null, null, null, null, new PersistenciaDatos());
	    }

	    private Evento crearEventoBasico(Organizador org, Venue venue) {
	        List<Localidad> localidades = new ArrayList<>();
	        localidades.add(new Localidad("General", 10000, false, 100, new ArrayList<>(), null));
	        return new Evento("E100", "Concierto Prueba",
	                Date.valueOf("2026-01-10"), Time.valueOf("20:00:00"),
	                "Música", venue, org, localidades, "Activo");
	    }

	    @Test
	    @DisplayName("Agregar y buscar un evento")
	    void agregarYBuscarEvento() {
	        BoletaMaster sistema = nuevoSistemaVacio();

	        Organizador org = new Organizador("org1","123","María","m@a.com",0,null);
	        Venue venue = new Venue("Centro", 5000, "V001", "Arena", true);

	        sistema.registrarUsuario(org);
	        Evento e = crearEventoBasico(org, venue);

	        sistema.agregarEvento(e);

	        Evento hallado = sistema.buscarEvento("E100");
	        assertNotNull(hallado);
	        assertEquals("Concierto Prueba", hallado.getNombre());
	    }

	    @Test
	    @DisplayName("Consultar catálogo sin filtros devuelve todos los activos")
	    void consultarCatalogoBasico() {
	        BoletaMaster sistema = nuevoSistemaVacio();
	        Organizador org = new Organizador("org1","123","María","m@a.com",0,null);
	        sistema.registrarUsuario(org);

	        sistema.agregarEvento(
	            new Evento("E1","A", Date.valueOf("2026-01-01"), Time.valueOf("18:00:00"),
	                "Teatro", new Venue("C",100,"V", "Teatro", true), org, new ArrayList<>(),"Activo"));
	        sistema.agregarEvento(
	            new Evento("E2","B", Date.valueOf("2026-02-01"), Time.valueOf("18:00:00"),
	                "Música", new Venue("C",100,"V2","Coliseo", true), org, new ArrayList<>(),"Cancelado"));

	        List<Evento> lista = sistema.consultarCatalogoEventos(null,null,null);
	        boolean encontrado = false;
	        for (Evento ev : lista) {
	            if ("E1".equals(ev.getIdEvento())) {
	                encontrado = true;
	                break;
	            }
	        }
	        assertTrue(encontrado);
	    }
}
