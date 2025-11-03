package persistencia;

import java.util.List;

import logica.Evento;
import java.io.*;
import java.util.*;
import logica.Venue;
import Usuarios.Organizador;
import logica.Localidad;
import java.sql.Date;
import java.sql.Time;
public class PersistenciaEventos implements IPersistenciaEventos{


    @Override
    public List<Evento> cargarEventos(String archivo) {
        List<Evento> eventos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 9) {
                    String id = partes[0];
                    String nombre = partes[1];
                    Date fecha = Date.valueOf(partes[2]);
                    Time hora = Time.valueOf(partes[3]);
                    String tipo = partes[4];
                    String nombreVenue = partes[5];
                    String nombreOrganizador = partes[6];
                    String listaLocalidades = partes[7];
                    String estado = partes[8];

                    Venue venue = new Venue(nombreVenue, 0, "V0", nombreVenue, true);
                    Organizador organizador = new Organizador(nombreOrganizador, "", nombreOrganizador, "", 0, new ArrayList<>());

                    List<Localidad> localidades = new ArrayList<>();
                    if (!listaLocalidades.isEmpty()) {
                        String[] nombresLoc = listaLocalidades.split(",");
                        for (String n : nombresLoc) {
                            localidades.add(new Localidad(n.trim(), 0, false, 0, new ArrayList<>(), null));
                        }
                    }

                    Evento evento = new Evento(id, nombre, fecha, hora, tipo, venue, organizador, localidades, estado);
                    eventos.add(evento);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar eventos: " + e.getMessage());
        }
        return eventos;
    }

    @Override
    public void guardarEventos(String archivo, List<Evento> eventos) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            for (Evento e : eventos) {
                StringBuilder locs = new StringBuilder();
                for (Localidad l : e.getLocalidades()) {
                    locs.append(l.getNombre()).append(",");
                }
                if (locs.length() > 0) locs.deleteCharAt(locs.length() - 1);

                pw.println(e.getIdEvento() + ";" + e.getNombre() + ";" +
                           e.getFecha() + ";" + e.getHora() + ";" + e.getTipo() + ";" +
                           e.getVenue().getNombre() + ";" +
                           e.getOrganizador().getNombre() + ";" +
                           locs.toString() + ";" + e.getEstado());
            }
        } catch (IOException e) {
            System.err.println("Error al guardar eventos: " + e.getMessage());
        }
    }
}


