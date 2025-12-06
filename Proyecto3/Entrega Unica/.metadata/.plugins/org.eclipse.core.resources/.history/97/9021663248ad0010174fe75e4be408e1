package persistencia;

import java.io.BufferedReader;
import java.util.List;
import java.io.*;
import java.sql.Date;
import java.util.*;

import logica.*;
import Usuarios.Cliente;
import Tiquetes.TiqueteBasico;

import logica.Tiquete;

public class PersistenciaTiquetes implements IPersistenciaTiquetes {

    @Override
    public List<Tiquete> cargarTiquetes(String archivo) {
        List<Tiquete> tiquetes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] partes = linea.split(";");
                if (partes.length < 10) continue;

                String id = partes[0];
                String nombreEvento = partes[1];
                Date fechaEvento = Date.valueOf(partes[2]);
                String nombreLocalidad = partes[3];
                String nombreCliente = partes[4];
                double precioBase = Double.parseDouble(partes[5]);
                double cargoServicio = Double.parseDouble(partes[6]);
                double cuotaAdicional = Double.parseDouble(partes[7]);
                boolean transferible = Boolean.parseBoolean(partes[8]);
                String estado = partes[9];

                Evento e = new Evento(nombreEvento, nombreEvento, fechaEvento, null, "", null, null, new ArrayList<>(), "Activo");
                Localidad l = new Localidad(nombreLocalidad, precioBase, false, 0, new ArrayList<>(), null);
                Cliente c = new Cliente(nombreCliente, "123", nombreCliente, nombreCliente + "@mail.com", 0, new ArrayList<>());

                int idNum = Integer.parseInt(id.replaceAll("\\D", ""));
                TiqueteBasico t = new TiqueteBasico(
                        cargoServicio, cuotaAdicional, idNum,
                        e, c, l, estado, transferible, ""
                );

                tiquetes.add(t);
            }

        } catch (IOException e) {
            System.out.println("Error al cargar los tiquetes: " + e.getMessage());
        }

        return tiquetes;
    }

    @Override
    public void guardarTiquetes(String archivo, List<Tiquete> tiquetes) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            for (Tiquete t : tiquetes) {
                pw.println(
                    "TQ" + t.getIdTiquete() + ";" +
                    t.getEvento().getNombre() + ";" +
                    t.getFechaEvento() + ";" +
                    t.getLocalidad().getNombre() + ";" +
                    t.getComprador().getNombre() + ";" +
                    t.getLocalidad().getPrecioLocalidad() + ";" +
                    t.getCargoServicio() + ";" +
                    t.getCuotaAdicional() + ";" +
                    t.esTransferible() + ";" +
                    t.getEstado()
                );
            }
            System.out.println("Archivo de tiquetes guardado correctamente en: " + archivo);
        } catch (IOException e) {
            System.out.println("Error al guardar los tiquetes: " + e.getMessage());
        }
    }
}
