package logica;

public class Venue {
	private String ubicacion;
	private int capacidadMax;
	private String idVenue;
	private String nombre;
	private boolean aprobado;
	
	public Venue(String ubicacion, int capacidadMax, String idVenue, String nombre, boolean aprobado) {
		this.ubicacion = ubicacion;
		this.capacidadMax = capacidadMax;
		this.idVenue = idVenue;
		this.nombre = nombre;
		this.aprobado = aprobado;
	}
	public boolean esAprobado() {
		return aprobado;
	}
	public void setAprobado(boolean aprobado) {
		this.aprobado = aprobado;
	}
	public String getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}
	public int getCapacidadMax() {
		return capacidadMax;
	}
	public void setCapacidadMax(int capacidadMax) {
		this.capacidadMax = capacidadMax;
	}
	public String getIdvenue() {
		return idVenue;
	}
	public void setIdvenue(String idvenue) {
		this.idVenue = idvenue;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public boolean isAprobado() {
		return aprobado;
	}
    @Override
    public String toString() {
        return "Venue{" +
                "ID='" + idVenue + '\'' +
                ", nombre='" + nombre + '\'' +
                ", ubicación='" + ubicacion + '\'' +
                ", capacidadMax=" + capacidadMax +
                ", estado=" + aprobado +
                '}';
    }
	
	
}
