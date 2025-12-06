package persistencia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import Usuarios.Cliente;
import logica.Usuario;

public class PersistenciaUsuarios implements IPersistenciaUsuarios {

    @Override
    public List<Usuario> cargarUsuarios(String archivo) {
        List<Usuario> usuarios = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 5) {
                    String login = partes[0];
                    String contrasena = partes[1];
                    String nombre = partes[2];
                    String correo = partes[3];
                    double saldo = Double.parseDouble(partes[4]);

                    Usuario u = new Cliente(login, contrasena, nombre, correo, saldo, new ArrayList<>());
                    usuarios.add(u);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    @Override
    public void guardarUsuarios(String archivo, List<Usuario> usuarios) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            for (Usuario u : usuarios) {
                pw.println(u.getLogin() + ";" + u.getContrasena() + ";" +
                           u.getNombre() + ";" + u.getCorreo() + ";" + u.getSaldo());
            }
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios: " + e.getMessage());
        }
    }
}
