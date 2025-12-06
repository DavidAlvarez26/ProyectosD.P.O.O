package tests;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import Tiquetes.TiqueteBasico;
import logica.Tiquete;

class AdministradorTest extends baseDatosTest{

	   @BeforeEach
	    void setup() { SistemaBasico(); }

	    @Test
	    @DisplayName("RF1 Admin: modificar porcentaje de servicio impacta precio de venta")
	    void rf1_modificarPorcentajeServicio() {
	        sistema.comprarTiquete(c1, evento, loc, 1);
	        admin.modificarPorcentajeServicio(evento.getTipo(), 0.50); 
	        Tiquete nuevo = new TiqueteBasico( 5_000, 500,
	                999, evento, null, loc, "Disponible", true, "A999");
	        double precio = nuevo.calcularPrecioTotal();
	        assertTrue(precio > 10_000, "Con 50% de servicio, el precio total debe subir");
	    }




}
