package Tiquetes;

import java.util.List;

import Usuarios.Cliente;
import logica.Evento;
import logica.Localidad;
import logica.Tiquete;

public class TiqueteTemporada extends Tiquete{

	private List<Evento> eventos;
	private double precioTemporada;
	
	public TiqueteTemporada(double cargoServicio, double cuotaAdicional, int idTiquete, Evento evento,
			Cliente comprador, Localidad localidad, String estado, boolean tranferible,List<Evento> eventos,double precioTemporada) {
		super(cargoServicio, cuotaAdicional, idTiquete, evento, comprador, localidad, estado, tranferible);
		this.eventos=eventos;
		this.precioTemporada=precioTemporada;
	}

	@Override
	public double calcularPrecioTotal() {
        return precioTemporada;
    }

	@Override
	public boolean esTransferible() {
		return transferible && !estado.equalsIgnoreCase("Vencido");
	}

	@Override
	public void marcarComoTranferido() {
		this.estado = "Transferido";
		
	}
	public void agregarEvento(Evento evento) {
		if (evento != null && !eventos.contains(evento)) {
            eventos.add(evento);
        }
	}
	public double calcularPrecioTemporada() {
		return precioTemporada;
	}
	

	public List<Evento> getEventos() {
        return eventos;
    }

    public double getPrecioTemporada() {
        return precioTemporada;
    }

}
