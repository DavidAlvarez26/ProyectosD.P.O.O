package Finanzas;

import java.sql.Date;

import logica.Tiquete;
import logica.Usuario;

public class Rembolso {
	private String idReembolso;
	private Usuario usuario;
	private Tiquete tiquete;
	private double valor;
	private Date fecha;
	private String motivo;
	
	public Rembolso(String idReembolso, Usuario usuario, Tiquete tiquete, double valor, Date fecha, String motivo) {
		
		this.idReembolso = idReembolso;
		this.usuario = usuario;
		this.tiquete = tiquete;
		this.valor = valor;
		this.fecha = new Date(System.currentTimeMillis());
		this.motivo = motivo;
	}
    public double calcularValorReembolso() {
        double precioBase = tiquete.getPrecioBase();
        double cargoServicio = tiquete.getCargoServicio();
        double cuotaEmision = tiquete.getCuotaAdicional();
        
        if (motivo.equalsIgnoreCase("Cancelacion Administrador")) {
            this.valor = (precioBase + cargoServicio) - cuotaEmision;
        } else if (motivo.equalsIgnoreCase("Cancelacion Organizador")) {
            this.valor = precioBase;
        } else if (motivo.equalsIgnoreCase("Calamidad")) {
            this.valor = precioBase * 0.8;
        } else {
            this.valor = 0;
        }

        return valor;
    }

    public String generarComprobante() {
        StringBuilder sb = new StringBuilder();
        sb.append("----- COMPROBANTE DE REEMBOLSO -----\n");
        sb.append("ID Reembolso: ").append(idReembolso).append("\n");
        sb.append("Usuario: ").append(usuario.getNombre()).append("\n");
        sb.append("Evento: ").append(tiquete.getEvento().getNombre()).append("\n");
        sb.append("Fecha: ").append(fecha).append("\n");
        sb.append("Motivo: ").append(motivo).append("\n");
        sb.append("Valor reembolsado: $").append(valor).append("\n");
        sb.append("------------------------------------\n");
        return sb.toString();
    }
	
	
}
