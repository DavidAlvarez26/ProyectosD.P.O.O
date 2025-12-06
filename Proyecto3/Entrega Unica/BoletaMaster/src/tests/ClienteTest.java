package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import logica.Evento;
import logica.Tiquete;

class ClienteTest extends baseDatosTest{

    @BeforeEach
    void setup() { SistemaBasico(); }

    @Test
    @DisplayName("RF1 Cliente: consulta de catálogo devuelve eventos con info clave")
    void rf1_catalogoCliente() {
        List<Evento> cat = sistema.consultarCatalogoEventos(null, null, null);
        assertFalse(cat.isEmpty());
        Evento e = cat.get(0);
        assertNotNull(e.getNombre());
        assertNotNull(e.getFecha());
        assertNotNull(e.getVenue());
        assertNotNull(e.getLocalidades());
    }

    @Test
    @DisplayName("RF2 Cliente: compra de tiquetes descuenta saldo y marca tiquetes como vendidos")
    void rf2_compraCliente() {
        double saldoInicial = c1.getSaldo();
        sistema.comprarTiquete(c1, evento, loc, 2);

        assertTrue(c1.getSaldo() < saldoInicial, "Debe descontar saldo");
        long vendidos = loc.getTiquetes().stream().filter(t -> "Vendido".equalsIgnoreCase(t.getEstado())).count();
        assertEquals(2, vendidos);
        assertEquals(3, loc.obtenerTiquetesDisponibles().size());
        assertEquals(2, c1.getTiquetes().size());
    }


    @Test
    @DisplayName("RF5 Cliente: consulta de saldo refleja compras y reembolsos")
    void rf5_saldoCliente() {
        double s0 = c1.consultarSaldo();
        sistema.comprarTiquete(c1, evento, loc, 1);
        double s1 = c1.consultarSaldo();
        assertTrue(s1 < s0, "Debe disminuir tras compra");
        Tiquete t = c1.getTiquetes().get(0);
        sistema.procesarReembolso(t, c1, "Calamidad"); 
        double s2 = c1.consultarSaldo();
        assertTrue(s2 > s1, "Debe aumentar tras reembolso");
    }


}
