package presentacion;

import java.util.List;
import java.util.Scanner;
import logica.BoletaMaster;
import logica.Evento;
import logica.Localidad;
import logica.Tiquete;
import market.Marketplace;
import market.OfertaMarket;
import Usuarios.Cliente;

public class ConsolaCliente {
    private final Scanner sc = new Scanner(System.in);
    private final BoletaMaster sistema;
    private final Cliente cliente;
    private final Marketplace mp;

    public ConsolaCliente(BoletaMaster sistema, Cliente cliente) {
        this.sistema = sistema;
        this.cliente = cliente;
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
    private int pedirNumero(String mensaje) {
        while (true) {
            System.out.print(mensaje + " ");

            try {
                String entrada = sc.nextLine().trim();
                return Integer.parseInt(entrada);
            } 
            catch (NumberFormatException e) {
                System.out.println("❌ Entrada inválida. Ingresa un número válido.");
            }
        }
    }
    

    public void mostrarMenu() {
        while (true) {
            System.out.println("=== Cliente ===");
            System.out.println("1. Consultar catálogo");
            System.out.println("2. Comprar tiquete");
            System.out.println("3. Ver tiquetes activos");
            System.out.println("4. Transferir tiquete");
            System.out.println("5. Marketplace: publicar oferta");
            System.out.println("6. Marketplace: listar ofertas");
            System.out.println("7. Marketplace: hacer contraoferta");
            System.out.println("8. Marketplace: comprar oferta");
            System.out.println("9. Salir");
            int op = pedirEntero("Opción:", 1, 9);
            if (op == 9) break;

            if (op == 1) {
                List<Evento> lista = sistema.consultarCatalogoEventos(null, null, null);
                for (Evento e : lista) System.out.println(e.getIdEvento() + " | " + e.getNombre() + " | " + e.getTipo());
            }
            else if (op == 2) {
                String idE = pedirTexto("ID evento:");
                Evento e = sistema.buscarEvento(idE);
                if (e == null) continue;
                System.out.println("Localidades:");
                for (int i = 0; i < e.getLocalidades().size(); i++)
                    System.out.println((i+1) + ". " + e.getLocalidades().get(i).getNombre());
                int idx = pedirEntero("Seleccione localidad:", 1, e.getLocalidades().size()) - 1;
                int cant = pedirEntero("Cantidad:", 1, 20);
                Localidad loc = e.getLocalidades().get(idx);
                sistema.comprarTiquete(cliente, e, loc, cant);
            }
            else if (op == 3) {
                List<Tiquete> activos = cliente.consultarTiquetesActivos();
                for (Tiquete t : activos)
                    System.out.println(t.getIdTiquete() + " | " + t.getEvento().getNombre() + " | " + t.getEstado());
            }
            else if (op == 4) {
                int idT = pedirNumero("ID tiquete a transferir:");
                String loginDestino = pedirTexto("Login destino:");
                Tiquete t = sistema.buscarTiquete(idT);
                if (t == null) continue;
                var destino = sistema.buscarUsuario(loginDestino);
                if (destino instanceof Cliente d) sistema.transferirTiquete(cliente, d, t);
            }
            else if (op == 5) {
                int idT = pedirNumero("ID tiquete a publicar:");
                double precio = pedirDouble("Precio:", 1);
                mp.crearOferta(cliente, idT, precio);
            }
            else if (op == 6) {
                List<OfertaMarket> ofertas = mp.listarOfertas();
                for (OfertaMarket o : ofertas)
                    System.out.println(o.getIdOferta() + " | " + o.getTiquete() + " | " + o.getVendedor() + " | " + o.getPrecio() + " | " + o.getEstado());
            }

            }
        }
    }
