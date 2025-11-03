package logica;

public abstract class Usuario {
	protected String login;
	protected String contrasena;
	protected String nombre;
	protected String correo;
	protected double saldo;
	
	public Usuario(String login, String contrasena, String nombre, String correo, double saldo) {
	
		this.login = login;
		this.contrasena = contrasena;
		this.nombre = nombre;
		this.correo = correo;
		this.saldo = saldo;
	}
	
	
	
	
	public String getLogin() {
		return login;
	}




	public void setLogin(String login) {
		this.login = login;
	}




	public String getContrasena() {
		return contrasena;
	}




	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}




	public String getNombre() {
		return nombre;
	}




	public void setNombre(String nombre) {
		this.nombre = nombre;
	}




	public String getCorreo() {
		return correo;
	}




	public void setCorreo(String correo) {
		this.correo = correo;
	}




	public double getSaldo() {
		return saldo;
	}




	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}




	protected abstract boolean autenticar(String contrasena);
	public abstract double consultarSaldo();
	public abstract void  recargarSaldo(double monto);
	
	
	
	
	
	
	
}
