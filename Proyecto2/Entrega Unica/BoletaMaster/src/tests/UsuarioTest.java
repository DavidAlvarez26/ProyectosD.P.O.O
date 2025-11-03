package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import logica.BoletaMaster;
import persistencia.PersistenciaDatos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import Usuarios.Cliente;
import logica.Usuario;

public class UsuarioTest {

    private BoletaMaster sistema;

    @BeforeEach
    public void setUp() {
        PersistenciaDatos persistencia = new PersistenciaDatos();
        sistema = new BoletaMaster(null, null, null, null, null, persistencia);
    }

    private Cliente nuevoCliente(String login) {
        return new Cliente(login, "pass", "Nombre " + login, login + "@mail.com", 0.0, null);
    }

    @Test
    @DisplayName("Registrar usuario nuevo: debe quedar accesible por buscarUsuario y aumentar el tamaño")
    public void testRegistrarUsuarioNuevo() {
        Cliente c1 = nuevoCliente("user1");

        sistema.registrarUsuario(c1);

        assertNotNull(sistema.buscarUsuario("user1"), "El usuario recién registrado no se encuentra");
        assertEquals(1, sistema.getUsuarios().size(), "El tamaño de la lista de usuarios no coincide");
    }

    @Test
    @DisplayName("Registrar usuario duplicado por login: NO debe duplicarse")
    public void testRegistrarUsuarioDuplicado() {
        Cliente c1 = nuevoCliente("user1");
        Cliente c2 = nuevoCliente("user1"); 

        sistema.registrarUsuario(c1);
        sistema.registrarUsuario(c2); 

        assertNotNull(sistema.buscarUsuario("user1"), "El usuario con login user1 debería existir");
        assertEquals(1, sistema.getUsuarios().size(), "No se debe permitir duplicar el mismo login");
    }

    @Test
    @DisplayName("Buscar usuario: existente vs. inexistente")
    public void testBuscarUsuarioExistenteYNoExistente() {
        sistema.registrarUsuario(nuevoCliente("juan"));
        sistema.registrarUsuario(nuevoCliente("ana"));

        Usuario uCorrecto = sistema.buscarUsuario("juan");
        Usuario uMiss = sistema.buscarUsuario("noexiste");

        assertNotNull(uCorrecto, "Debería encontrar a juan");
        assertEquals("juan", uCorrecto.getLogin(), "El login recuperado no coincide");

        assertNull(uMiss, "No debería encontrar un login inexistente");
    }

    @Test
    @DisplayName("Eliminar usuario: borra por login y ya no se puede buscar")
    public void testEliminarUsuario() {
        sistema.registrarUsuario(nuevoCliente("userX"));
        assertEquals(1, sistema.getUsuarios().size(), "Debe haber 1 usuario antes de eliminar");

        sistema.eliminarUsuario("userX");

        assertNull(sistema.buscarUsuario("userX"), "El usuario eliminado no debe encontrarse");
        assertEquals(0, sistema.getUsuarios().size(), "La lista de usuarios debe quedar vacía");
    }

    @Test
    @DisplayName("Eliminar usuario inexistente: no debe lanzar excepción ni cambiar la lista")
    public void testEliminarUsuarioInexistenteNoRompe() {
        sistema.registrarUsuario(nuevoCliente("userA"));
        int antes = sistema.getUsuarios().size();

        sistema.eliminarUsuario("noexiste");

        int despues = sistema.getUsuarios().size();
        assertEquals(antes, despues, "Eliminar un login inexistente no debe cambiar la lista");
    }
}
