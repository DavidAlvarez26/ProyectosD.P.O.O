package Finanzas;

import java.sql.Date;
import java.util.List;

import Usuarios.Cliente;
import logica.Tiquete;

public class Compra {
	private String idCompra;
	private Cliente cliente;
	private List<Tiquete> tiquetesComprados;
	private Date fechaCompra;
	private	double valorTotal;
	private	String medioPago;
	
	public Compra(String idCompra, Cliente cliente, List<Tiquete> tiquetesComprados, Date fechaCompra,
			double valorTotal, String medioPago) {
		this.idCompra = idCompra;
		this.cliente = cliente;
		this.tiquetesComprados = tiquetesComprados;
		this.fechaCompra = new Date(System.currentTimeMillis());
		this.valorTotal = calcularTotal();
		this.medioPago = medioPago;
	}
	
	public String getIdCompra() {
		return idCompra;
	}

	public void setIdCompra(String idCompra) {
		this.idCompra = idCompra;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<Tiquete> getTiquetesComprados() {
		return tiquetesComprados;
	}

	public void setTiquetesComprados(List<Tiquete> tiquetesComprados) {
		this.tiquetesComprados = tiquetesComprados;
	}

	public Date getFechaCompra() {
		return fechaCompra;
	}

	public void setFechaCompra(Date fechaCompra) {
		this.fechaCompra = fechaCompra;
	}

	public double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public String getMedioPago() {
		return medioPago;
	}

	public void setMedioPago(String medioPago) {
		this.medioPago = medioPago;
	}

	public double calcularTotal() {
        double total = 0;
        for (Tiquete t : tiquetesComprados) {
        	if(t!=null) {
            total += t.calcularPrecioTotal();
        }	}
        this.valorTotal = total;
        return total;
	}
	public void agregarTiquete(Tiquete tiquete) {
        if (tiquete != null && !tiquetesComprados.contains(tiquete)) {
            tiquetesComprados.add(tiquete);
            calcularTotal();
        }
	}
    public String obtenerDetalleCompra() {
        StringBuilder sb = new StringBuilder();
        sb.append("Compra ID: ").append(idCompra).append("\n");
        sb.append("Cliente: ").append(cliente.getNombre()).append("\n");
        sb.append("Fecha: ").append(fechaCompra).append("\n");
        sb.append("Medio de pago: ").append(medioPago).append("\n");
        sb.append("Tiquetes adquiridos:\n");

        for (Tiquete t : tiquetesComprados) {
            sb.append("  - Evento: ").append(t.getEvento().getNombre())
              .append(" | Localidad: ").append(t.getLocalidad().getNombre())
              .append(" | Precio: $").append(t.calcularPrecioTotal()).append("\n");
        }

        sb.append("Valor total: $").append(valorTotal);
        return sb.toString();
    }
	

}
