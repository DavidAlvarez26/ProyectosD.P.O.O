package persistencia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import logica.Venue;

public class PersistenciaVenues implements IPersistenciaVenues {

	@Override
	public List<Venue> cargarVenues(String archivo) {
        List<Venue> venues = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;

                String[] partes = linea.split(";");
                if (partes.length < 5) continue;

                String idVenue = partes[0];
                String nombre = partes[1];
                String ubicacion = partes[2];
                int capacidadMax = Integer.parseInt(partes[3]);
                boolean aprobado = Boolean.parseBoolean(partes[4]);

                Venue venue = new Venue(ubicacion, capacidadMax, idVenue, nombre, aprobado);
                venues.add(venue);
            }
        } catch (IOException e) {
            System.out.println("Error cargando venues: " + e.getMessage());
        }

        return venues;
    }
	@Override
    public void guardarVenues(String archivo, List<Venue> venues) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            for (Venue v : venues) {
                pw.println(v.getIdvenue() + ";" +
                           v.getNombre() + ";" +
                           v.getUbicacion() + ";" +
                           v.getCapacidadMax() + ";" +
                           v.esAprobado());
            }
        } catch (IOException e) {
            System.out.println("Error guardando venues: " + e.getMessage());
        }
    }

}
