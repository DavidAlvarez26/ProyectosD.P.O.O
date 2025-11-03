package logica;

public interface IPuertoTiquetes {
	public Tiquete buscarTiquete(String idTiquete);
	public void reservarTiquete(String idTiquete);
	public void liberarTiquete(String idTiquete);
	public void marcarTiqueteVendido(String idTiquete);
	
}
