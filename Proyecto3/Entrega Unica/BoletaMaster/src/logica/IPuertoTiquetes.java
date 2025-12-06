package logica;

import Usuarios.Cliente;

public interface IPuertoTiquetes {
	public Tiquete buscarTiquete(int idTiquete);
	public void reservarTiquete(int idTiquete);
	public void liberarTiquete(int idTiquete);
	public void marcarTiqueteVendido(int idTiquete);
	public void transferirTiquete(Cliente vendedor, Cliente comprador, Tiquete tiquete);

	
}
