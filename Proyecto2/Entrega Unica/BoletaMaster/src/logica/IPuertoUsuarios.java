package logica;

public interface IPuertoUsuarios {
	public Usuario buscarUsuario( String login);
	public void cargarSaldo(String login, double valor);
	public boolean descontarSaldo(String login, double valor);
}
