package persistencia;

import java.util.List;

import logica.Usuario;

public interface IPersistenciaUsuarios {
	    List<Usuario> cargarUsuarios(String archivo);
	    void guardarUsuarios(String archivo, List<Usuario> usuarios);
	}

