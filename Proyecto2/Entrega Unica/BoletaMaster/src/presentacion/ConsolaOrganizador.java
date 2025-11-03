package presentacion;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import Finanzas.Oferta;
import logica.BoletaMaster;
import logica.Evento;
import logica.Localidad;
import logica.Venue;
import Usuarios.Organizador;

public class ConsolaOrganizador {
    private final Scanner sc = new Scanner(System.in);
    private final BoletaMaster sistema;
    private final Organizador org;

    public ConsolaOrganizador(BoletaMaster sistema, Organizador org) {
        this.sistema = sistema;
        this.org = org;
    }

    private int pedirEntero(String msg, int min, int max) {
        while (true) {
            System.out.print(msg + " ");
            String s = sc.nextLine();
            try {
                int v = Integer.parseInt(s);
                if (v < min || v > max) continue;
                return v;
            } catch (Exception e) {}
        }
    }

    private double pedirDouble(String msg, double min) {
        while (true) {
            System.out.print(msg + " ");
            String s = sc.nextLine();
            try {
                double v = Double.parseDouble(s);
                if (v < min) continue;
                return v;
            } catch (Exception e) {}
        }
    }

    private String pedirTexto(String msg) {
        while (true) {
            System.out.print(msg + " ");
            String s = sc.nextLine();
            if (!s.trim().isEmpty()) return s.trim();
        }
    }

    public void mostrarMenu() {
        while (true) {
            System.out.println("=== Organizador ===");
            System.out.println("1. Proponer venue");
            System.out.println("2. Crear evento");
            System.out.println("3. Aplicar oferta a localidad");
            System.out.println("4. Reporte financiero propio");
            System.out.println("5. Comprar como cliente");
            System.out.println("6. Salir");
            int op = pedirEntero("Opción:", 1, 6);
            if (op == 6) break;

            if (op == 1) {
                String id = pedirTexto("ID venue:");
                String nombre = pedirTexto("Nombre venue:");
                String ubic = pedirTexto("Ubicación:");
                int cap = pedirEntero("Capacidad:", 1, Integer.MAX_VALUE);
                Venue v = new Venue(ubic, cap, id, nombre, false);
                sistema.proponerVenue(org, v);
            }
            else if (op == 2) {
                String idE = pedirTexto("ID evento:");
                String nombre = pedirTexto("Nombre:");
                Date fecha = Date.valueOf(pedirTexto("Fecha (YYYY-MM-DD):"));
                Time hora = Time.valueOf(pedirTexto("Hora (HH:MM:SS):"));
                String tipo = pedirTexto("Tipo:");
                String idVenue = pedirTexto("ID venue aprobado:");
                Venue v = sistema.buscarVenue(idVenue);
                List<Localidad> locs = new ArrayList<>();
                int n = pedirEntero("N° localidades:", 1, 20);
                for (int i = 0; i < n; i++) {
                    String nomL = pedirTexto("Localidad " + (i+1) + " nombre:");
                    double pb = pedirDouble("Precio base:", 0);
                    boolean num = pedirEntero("Numerada? 1=Si 2=No:", 1, 2) == 1;
                    int cap = pedirEntero("Capacidad:", 1, Integer.MAX_VALUE);
                    locs.add(new Localidad(nomL, pb, num, cap, new ArrayList<>(), null));
                }
                Evento e = new Evento(idE, nombre, fecha, hora, tipo, v, org, locs, "Activo");
                sistema.crearEvento(org, e);
            }
            else if (op == 3) {
                String idE = pedirTexto("ID evento:");
                Evento e = sistema.buscarEvento(idE);
                if (e == null) continue;
                System.out.println("Localidades:");
                for (int i = 0; i < e.getLocalidades().size(); i++)
                    System.out.println((i+1) + ". " + e.getLocalidades().get(i).getNombre());
                int idx = pedirEntero("Seleccione localidad:", 1, e.getLocalidades().size()) - 1;
                double por = pedirDouble("Descuento (% 0-100):", 0);
                Date fi = Date.valueOf(pedirTexto("Inicio (YYYY-MM-DD):"));
                Date ff = Date.valueOf(pedirTexto("Fin (YYYY-MM-DD):"));
                Oferta of = new Oferta(por, fi, ff);
                sistema.aplicarOferta(e, e.getLocalidades().get(idx), of);
            }
            else if (op == 4) {
                var rep = sistema.generarReporteOrganizador(org);
                System.out.println("Total: " + rep.getGananciasTotales());
            }

    }
}}