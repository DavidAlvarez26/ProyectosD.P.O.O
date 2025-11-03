package presentacion;

import java.util.List;
import java.util.Scanner;
import Finanzas.ReporteFinanciero;
import logica.BoletaMaster;
import logica.Evento;
import logica.Venue;
import market.Marketplace;
import market.OfertaMarket;
import Usuarios.Administrador;

public class ConsolaAdministrador {
    private final Scanner sc = new Scanner(System.in);
    private final BoletaMaster sistema;
    private final Administrador admin;
    private final Marketplace mp;

    public ConsolaAdministrador(BoletaMaster sistema, Administrador admin) {
        this.sistema = sistema;
        this.admin = admin;
        this.mp = sistema.getMarketplace();
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
            System.out.println("=== Administrador ===");
            System.out.println("1. Modificar % servicio por tipo");
            System.out.println("2. Fijar costo fijo de emisión");
            System.out.println("3. Aprobar/Rechazar venue");
            System.out.println("4. Cancelar evento");
            System.out.println("5. Reporte de ganancias");
            System.out.println("6. Marketplace: listar ofertas");
            System.out.println("7. Marketplace: aceptar contraoferta");
            System.out.println("8. Marketplace: ver log");
            System.out.println("9. Salir");
            int op = pedirEntero("Opción:", 1, 9);
            if (op == 9) break;

            if (op == 1) {
                String tipo = pedirTexto("Tipo de evento:");
                double p = pedirDouble("Nuevo % servicio:", 0);
                admin.modificarPorcentajeServicio(tipo, p);
            }
            else if (op == 2) {
                double v = pedirDouble("Nuevo costo fijo de emisión:", 0);
                admin.fijarCostoEmision(v);
            }
            else if (op == 3) {
                String id = pedirTexto("ID venue:");
                Venue v = sistema.buscarVenue(id);
                if (v == null) continue;
                boolean apr = pedirEntero("1=Aprobar 2=Rechazar:", 1, 2) == 1;
                sistema.aprobarVenue(admin, v, apr);
            }
            else if (op == 4) {
                String idE = pedirTexto("ID evento:");
                Evento e = sistema.buscarEvento(idE);
                if (e == null) continue;
                String causa = pedirTexto("Causa:");
                sistema.cancelarEvento(admin, e, causa);
            }
            else if (op == 5) {
                ReporteFinanciero r = sistema.generarReporteFinanciero(admin);
                System.out.println("Total: " + r.getGananciasTotales());
                r.getGananciasxEvento().forEach((ev, val) -> System.out.println(ev.getNombre() + " = " + val));
            }
            else if (op == 6) {
                List<OfertaMarket> ofertas = mp.listarOfertas();
                for (OfertaMarket o : ofertas)
                    System.out.println(o.getIdOferta() + " | " + o.getTiquete() + " | " + o.getVendedor() + " | " + o.getPrecio() + " | " + o.getEstado());
            }

            else if (op == 7) {
                var registros = mp.consultarLog(null);
                for (var lg : registros)
                    System.out.println(lg.getFecha() + " | " + lg.getAccion() + " | " + lg.getActor() + " | " + lg.getIdOferta() + " | " + lg.getDetalle());
            }
        }
    }
}