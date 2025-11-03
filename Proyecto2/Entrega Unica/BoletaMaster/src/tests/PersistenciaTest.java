package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import logica.BoletaMaster;
import persistencia.PersistenciaDatos;

class PersistenciaTest {

    @Test
    public void testCargarDatos() {
        PersistenciaDatos persistencia = new PersistenciaDatos();
        BoletaMaster sistema = new BoletaMaster(null, null, null, null, null, persistencia);
        sistema.cargarDatos();
        
        assertNotNull(sistema.getUsuarios(), "Usuarios no cargados");
        assertTrue(sistema.getUsuarios().size() > 0, "No se cargaron usuarios");

        assertNotNull(sistema.getEventos(), "Eventos no cargados");
        assertTrue(sistema.getEventos().size() > 0, "No se cargaron eventos");

        assertNotNull(sistema.getVenues(), "Venues no cargados");
        assertTrue(sistema.getVenues().size() > 0, "No se cargaron venues");

        assertNotNull(sistema.getTiquetes(), "Tiquetes no cargados");
        assertTrue(sistema.getTiquetes().size() > 0, "No se cargaron tiquetes");

        System.out.println("Test de carga de datos exitoso");
    }
    @Test
    public void testGuardarYCargarDatos() {
        PersistenciaDatos persistencia = new PersistenciaDatos();
        BoletaMaster sistemaOriginal = new BoletaMaster(null, null, null, null, null, persistencia);
        
        sistemaOriginal.cargarDatos();
        sistemaOriginal.guardarDatos();
        BoletaMaster sistemaCopia = new BoletaMaster(null, null, null, null, null, persistencia);
        sistemaCopia.cargarDatos();
        assertEquals(sistemaOriginal.getUsuarios().size(), sistemaCopia.getUsuarios().size());
        assertEquals(sistemaOriginal.getEventos().size(), sistemaCopia.getEventos().size());
        assertEquals(sistemaOriginal.getTiquetes().size(), sistemaCopia.getTiquetes().size());
        assertEquals(sistemaOriginal.getVenues().size(), sistemaCopia.getVenues().size());

        System.out.println("Test de guardar + cargar exitoso");
    }
}
