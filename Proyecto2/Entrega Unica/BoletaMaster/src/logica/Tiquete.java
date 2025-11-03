package logica;

import java.sql.Date;

import Usuarios.Cliente;

public abstract class Tiquete {
	protected double precioBase;
	protected double cargoServicio;
	protected double cuotaAdicional;
	protected int idTiquete;
	protected Date fechaEvento;
	protected Evento evento;
	protected Cliente comprador;
	protected Localidad localidad;
	protected String estado;
	protected boolean transferible;
	
	public Tiquete(double cargoServicio, double cuotaAdicional, int idTiquete,
			Evento evento, Cliente comprador, Localidad localidad, String estado, boolean tranferible) {
		this.precioBase = localidad.getPrecioLocalidad();
		this.cargoServicio = cargoServicio;
		this.cuotaAdicional = cuotaAdicional;
		this.idTiquete = idTiquete;
		this.fechaEvento = evento.getFecha();
		this.evento = evento;
		this.comprador = comprador;
		this.localidad = localidad;
		this.estado = estado;
		this.transferible = tranferible;
	}
	public abstract double calcularPrecioTotal();
	public abstract boolean esTransferible();
	public abstract void marcarComoTranferido();
	public double getPrecioBase() {
		return precioBase;
	}
	public void setPrecioBase(double precioBase) {
		this.precioBase = precioBase;
	}
	public double getCargoServicio() {
		return cargoServicio;
	}
	public void setCargoServicio(double cargoServicio) {
		this.cargoServicio = cargoServicio;
	}
	public double getCuotaAdicional() {
		return cuotaAdicional;
	}
	public void setCuotaAdicional(double cuotaAdicional) {
		this.cuotaAdicional = cuotaAdicional;
	}
	public int getIdTiquete() {
		return idTiquete;
	}
	public void setIdTiquete(int idTiquete) {
		this.idTiquete = idTiquete;
	}
	public Date getFechaEvento() {
		return fechaEvento;
	}
	public void setFechaEvento(Date fechaEvento) {
		this.fechaEvento = fechaEvento;
	}
	public Evento getEvento() {
		return evento;
	}
	public void setEvento(Evento evento) {
		this.evento = evento;
	}
	public Cliente getComprador() {
		return comprador;
	}
	public void setComprador(Cliente comprador) {
		this.comprador = comprador;
	}
	public Localidad getLocalidad() {
		return localidad;
	}
	public void setLocalidad(Localidad localidad) {
		this.localidad = localidad;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;//    DISPONIBLE,VENDIDO,TRANSFERIDO,USADO,CANCELADO,REEMBOLSADO,VENCIDO
	}
	public boolean isTransferible() {
		return transferible;
	}
	public void setTransferible(boolean transferible) {
		this.transferible = transferible;
	}
	
	
}
