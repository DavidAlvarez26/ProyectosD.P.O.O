package persistencia;

import java.util.List;

import logica.Venue;

public interface IPersistenciaVenues {
    List<Venue> cargarVenues(String archivo);
    void guardarVenues(String archivo, List<Venue> Venues);
}
