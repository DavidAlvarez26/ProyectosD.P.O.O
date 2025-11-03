package Tiquetes;

import java.sql.Date;

public class Beneficio {
	private String descripcion;
	private Date fechaLimite;
	public Beneficio(String descripcion, Date fechaLimite) {
		
		this.descripcion = descripcion;
		this.fechaLimite = fechaLimite;
	}
	public boolean esValido() {
        Date hoy = new Date(System.currentTimeMillis());
        return fechaLimite == null || fechaLimite.after(hoy);
    }
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
}
