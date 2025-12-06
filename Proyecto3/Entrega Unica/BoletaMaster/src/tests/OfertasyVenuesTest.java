package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


import org.junit.jupiter.api.DisplayName;


import Usuarios.Administrador;
import Usuarios.Organizador;
import logica.BoletaMaster;

import logica.Venue;
import persistencia.PersistenciaDatos;
class OfertasyVenuesTest {

	 @Test
	    @DisplayName("Proponer y aprobar Venue")
	    void proponerYAprobarVenue() {
	        BoletaMaster sistema = new BoletaMaster(null, null, null, null, null, new PersistenciaDatos(), null);
	        Organizador org = new Organizador("org","x","María","m@x.com",0,null);
	        Administrador admin = new Administrador("adm","x","Carlos","c@x.com",0,1000,null);

	        sistema.registrarUsuario(org);

	        Venue v = new Venue("Centro", 2000, "V3", "Centro Cultural", false);
	        sistema.proponerVenue(org, v);
	        assertFalse(v.esAprobado());

	        sistema.aprobarVenue(admin, v, true);
	        assertTrue(v.esAprobado());
	    }



}
