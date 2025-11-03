package Usuarios;

import java.util.HashMap;
import java.util.Map;

import Finanzas.ReporteFinanciero;
import logica.BoletaMaster;
import logica.Evento;
import logica.Usuario;
import logica.Venue;

public class Administrador extends Usuario{
	private double cuotaFija;
	private Map<String,Double> porcentajeServicioxTipo;


	public Administrador(String login, String contrasena, String nombre, String correo, double saldo, double cuotaFija, Map<String, Double> porcentajeServicioxTipo) {
		super(login, contrasena, nombre, correo, saldo);
		this.cuotaFija=cuotaFija;
		this.porcentajeServicioxTipo=(porcentajeServicioxTipo != null) ? porcentajeServicioxTipo : new HashMap<>();
		
	}

	@Override
	protected boolean autenticar(String contrasena) {
		return this.getContrasena().equals(contrasena);
	}

	@Override
	public double consultarSaldo() {
		// El administrador no utiliza saldo
		return 0;
	}

	@Override
	public void recargarSaldo(double monto) {
		// no aplica para el administradoR
		
	}
	
    public double getCuotaFija() {
		return cuotaFija;
	}

	public void setCuotaFija(double cuotaFija) {
		this.cuotaFija = cuotaFija;
	}

	public Map<String, Double> getPorcentajeServicioxTipo() {
		return porcentajeServicioxTipo;
	}

	public void setPorcentajeServicioxTipo(Map<String, Double> porcentajeServicioxTipo) {
		this.porcentajeServicioxTipo = porcentajeServicioxTipo;
	}

	public void modificarPorcentajeServicio(String tipoEvento, double nuevoPorcentaje) {
        porcentajeServicioxTipo.put(tipoEvento, nuevoPorcentaje);
    }

    public void fijarCostoEmision(double nuevoValor) {
        this.cuotaFija = nuevoValor;//es igual para todos los tiquetes sin importar tipo
    }

    public void aprobarVenue(BoletaMaster sistema,Venue venue, boolean aprobar) {
    	if (venue != null) {
    		sistema.aprobarVenue(this,venue,aprobar);
        }
    }

    public void cancelarEvento(Evento evento, String causa) {
        if (evento != null) {
            evento.setEstado("Cancelado");
        }
    }

    public ReporteFinanciero consultarGanancias(BoletaMaster sistema) {
        return sistema.generarReporteFinanciero(this);
    }
}
