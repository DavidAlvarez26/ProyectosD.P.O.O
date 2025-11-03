package Usuarios;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import Finanzas.Oferta;
import Finanzas.ReporteFinanciero;
import logica.BoletaMaster;
import logica.Evento;
import logica.Localidad;
import logica.Usuario;
import logica.Venue;

public class Organizador extends Usuario{
	private List<Evento> eventosOrganizados;

	public Organizador(String login, String contrasena, String nombre, String correo, double saldo,List<Evento> eventosOrganizados) {
		super(login, contrasena, nombre, correo, saldo);
		this.eventosOrganizados=(eventosOrganizados != null) ? eventosOrganizados : new ArrayList<>();
	}

	@Override
	protected boolean autenticar(String contrasena) {
		return this.getContrasena().equals(contrasena);
	}

	@Override
	public double consultarSaldo() {
		return getSaldo();
	}

	@Override
	public void recargarSaldo(double monto) {
		this.saldo += monto;
		
	}
    public List<Evento> getEventosOrganizados() {
        return eventosOrganizados;
    }

    public void agregarEvento(Evento evento) {
        if (evento != null && !eventosOrganizados.contains(evento)) {
            eventosOrganizados.add(evento);
        }
    }
	public void proponerVenue(BoletaMaster sistema, Venue venue) {
		sistema.proponerVenue(this, venue);
	}
    public void crearEvento(BoletaMaster sistema,String idEvento, String nombre, Date fecha, Time hora, String tipo,
            Venue venue, List<Localidad> localidades ) {
        Evento nuevoEvento = new Evento(idEvento, nombre, fecha, hora, tipo, venue, this, localidades, "Activo");
        sistema.crearEvento(this, nuevoEvento);
    }

	public ReporteFinanciero consultarEstadoFinancieros (BoletaMaster sistema) {
		return sistema.generarReporteOrganizador(this);
	}
	public Oferta crearOferta(Localidad localidad,double descuento, Date fechaInicio,Date fechaFin){
		return new Oferta(descuento, fechaFin, fechaFin);
	}
	public void comprarComoCliente(BoletaMaster sistema,Evento evento, Localidad localidad, int cantidad) {
	    Cliente compradorTemporal = new Cliente(
	            this.getLogin(),
	            this.getContrasena(),
	            this.getNombre(),
	            this.getCorreo(),
	            this.getSaldo(),
	            new ArrayList<>()
	        );

	        sistema.comprarTiquete(compradorTemporal, evento, localidad, cantidad);
	}
}
