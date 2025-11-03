package persistencia;

import java.util.List;

import Finanzas.Compra;
import java.io.*;
import java.util.*;
import Usuarios.Cliente;
import logica.Tiquete;
import java.sql.Date;
public class PersistenciaCompras implements IPersistenciaCompras {


    @Override
    public List<Compra> cargarCompras(String archivo) {
        List<Compra> compras = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 6) {
                    String idCompra = partes[0];
                    String nombreCliente = partes[1];
                    String tiquetesStr = partes[2];
                    Date fecha = Date.valueOf(partes[3]);
                    double valorTotal = Double.parseDouble(partes[4]);
                    String medioPago = partes[5];

                    Cliente cliente = new Cliente(nombreCliente, "", nombreCliente, "", 0, new ArrayList<>());
                    List<Tiquete> tiquetes = new ArrayList<>();
                    if (!tiquetesStr.isEmpty()) {
                        String[] ids = tiquetesStr.split(",");
                        for (String _ : ids) {
                            Tiquete t = null;
                            tiquetes.add(t);
                        }
                    }

                    Compra compra = new Compra(idCompra, cliente, tiquetes, fecha, valorTotal, medioPago);
                    compras.add(compra);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar compras: " + e.getMessage());
        }
        return compras;
    }

    @Override
    public void guardarCompras(String archivo, List<Compra> compras) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            for (Compra c : compras) {
                if (c == null) continue;

                StringBuilder tiqs = new StringBuilder();
                if (c.getTiquetesComprados() != null) {
                    for (Tiquete t : c.getTiquetesComprados()) {
                        if (t != null) { 
                            tiqs.append(t.getIdTiquete()).append(",");
                        }
                    }
                }

                if (tiqs.length() > 0) tiqs.deleteCharAt(tiqs.length() - 1);
                String nombreCliente = (c.getCliente() != null) ? c.getCliente().getNombre() : "SIN_CLIENTE";
                pw.println(
                    c.getIdCompra() + ";" +
                    nombreCliente + ";" +
                    tiqs.toString() + ";" +
                    c.getFechaCompra() + ";" +
                    c.getValorTotal() + ";" +
                    c.getMedioPago()
                );
            }

            System.out.println("Compras guardadas correctamente en " + archivo);

        } catch (IOException e) {
            System.err.println("Error al guardar compras: " + e.getMessage());
        }
    }

}
