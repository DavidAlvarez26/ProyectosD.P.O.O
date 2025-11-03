package market;

import java.time.LocalDateTime;

import enums.TipoLog;

public class Log {
    public LocalDateTime fecha;
    private TipoLog accion;
    private String actor;
    private int idOferta;
    private String detalle;
    public LocalDateTime getFecha() {
        return fecha;
    }
    
    public Log(TipoLog accion, String actor, int idOferta, String detalle) {
		
		this.fecha = LocalDateTime.now();
		this.accion = accion;
		this.actor = actor;
		this.idOferta = idOferta;
		this.detalle = detalle;
	}

	public TipoLog getAccion() {
        return accion;
    }
    public void setAccion(TipoLog accion) {
        this.accion = accion;
    }
    public String getActor() {
        return actor;
    }
    public void setActor(String actor) {
        this.actor = actor;
    }
    public int getIdOferta() {
        return idOferta;
    }
    public void setIdOferta(int idOferta) {
        this.idOferta = idOferta;
    }
    public String getDetalle() {
        return detalle;
    }
    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    @Override
    public String toString() {
        return "[" + fecha + "] " + accion + " | " + actor + " | Oferta: " + idOferta + " | " + detalle;
    }
}

