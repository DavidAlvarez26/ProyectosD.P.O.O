package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import logica.Evento;
import logica.Localidad;
import logica.Venue;

class OrganizadorTest extends baseDatosTest{

    @BeforeEach
    void setup() { SistemaBasico(); }

    @Test
    @DisplayName("RF1 Organizador: propone venue y el admin lo aprueba")
    void rf1_proponerVenue() {
        Venue v = new Venue("Calle 100 #45-10", 2_000, "V010", "Centro Uniandes", false);
        sistema.proponerVenue(org, v);
        assertTrue(sistema.getVenues().contains(v));
        assertFalse(v.esAprobado());

        sistema.aprobarVenue(admin, v, true);
        assertTrue(v.esAprobado());
    }

    @Test
    @DisplayName("RF2 Organizador: crea evento con localidades y disponibilidad")
    void rf2_crearEvento() {
        Venue v = new Venue("Av 1", 4000, "V011", "Teatro Colón", true);
        Evento e = new Evento("E777", "Obra Hamlet", Date.valueOf("2025-12-15"),
                Time.valueOf("19:30:00"), "Teatro", v, org, new ArrayList<>(), "Activo");
        e.getLocalidades().add(new Localidad("Preferencial", 80_000, false, 300, new ArrayList<>(), null));

        sistema.crearEvento(org, e);

        assertNotNull(sistema.buscarEvento("E777"));
        assertEquals("Obra Hamlet", sistema.buscarEvento("E777").getNombre());
    }





}
