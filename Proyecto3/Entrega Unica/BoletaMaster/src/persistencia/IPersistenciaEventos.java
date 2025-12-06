package persistencia;

import java.util.List;

import logica.Evento;

public interface IPersistenciaEventos {
    List<Evento> cargarEventos(String archivo);
    void guardarEventos(String archivo, List<Evento> eventos);
}