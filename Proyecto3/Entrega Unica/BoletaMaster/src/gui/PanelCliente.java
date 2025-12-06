package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

import logica.BoletaMaster;
import logica.Tiquete;
import Usuarios.Cliente;

@SuppressWarnings("serial")
public class PanelCliente extends JFrame {

    private BoletaMaster sistema;
    private Cliente cliente;

    private JTextArea areaBoletas;
    private JLabel lblSaldo;

    public PanelCliente(BoletaMaster sistema, Cliente cliente) {
        this.sistema = sistema;
        this.cliente = cliente;
        setTitle("Cliente - BoletaMaster");
        setSize(600, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    @SuppressWarnings("unused")
	private void initComponents() {
        // --------- PANEL SUPERIOR ----------
        JPanel panelTop = new JPanel(new BorderLayout());
        lblSaldo = new JLabel("Saldo: $" + cliente.getSaldo());
        JButton btnActualizar = new JButton("Actualizar Boletas");
        panelTop.add(lblSaldo, BorderLayout.WEST);
        panelTop.add(btnActualizar, BorderLayout.EAST);

        // --------- CENTRO: LISTA DE BOLETAS ----------
        areaBoletas = new JTextArea();
        areaBoletas.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaBoletas);

        // --------- PANEL INFERIOR: BOTONES ----------
        JPanel panelBottom = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton btnImprimir = new JButton("Imprimir Boleta");
        JButton btnMarketplace = new JButton("Marketplace");
        JButton btnCargarSaldo = new JButton("Cargar Saldo");
        JButton btnSalir = new JButton("Cerrar Sesión");

        panelBottom.add(btnImprimir);
        panelBottom.add(btnMarketplace);
        panelBottom.add(btnCargarSaldo);
        panelBottom.add(btnSalir);

        // --------- ARMAR FRAME ----------
        add(panelTop, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelBottom, BorderLayout.SOUTH);

        // --------- LISTENERS ----------
        btnActualizar.addActionListener(this::actualizarBoletas);
        btnImprimir.addActionListener(this::imprimirBoleta);
        btnMarketplace.addActionListener(e -> new PanelMarketplace(sistema, cliente).setVisible(true));
        btnCargarSaldo.addActionListener(this::cargarSaldo);
        btnSalir.addActionListener(e -> {
            dispose();
            new VentanaPrincipal(sistema).setVisible(true);
        });

        // Cargar info inicial
        actualizarBoletas(null);
    }

    private void actualizarBoletas(ActionEvent e) {
        List<Tiquete> tiquetes = sistema.boletasDeCliente(cliente);

        if (tiquetes == null || tiquetes.isEmpty()) {
            areaBoletas.setText("No tienes tiquetes actualmente.\n");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Tiquete t : tiquetes) {
                String nombreEvento = (t.getEvento() != null) ? t.getEvento().getNombre() : "Sin evento";
                sb.append("ID: ").append(t.getIdTiquete())
                  .append(" | Evento: ").append(nombreEvento)
                  .append(" | Valor: $").append(t.calcularPrecioTotal())
                  .append(" | Impreso: ").append(t.estaImpreso() ? "Sí" : "No")
                  .append("\n");
            }
            areaBoletas.setText(sb.toString());
        }

        lblSaldo.setText("Saldo: $" + cliente.getSaldo());
    }

    private void imprimirBoleta(ActionEvent e) {
        abrirVentanaImpresion();
    }

    private void abrirVentanaImpresion() {
        JFrame frame = new JFrame("Impresión de tiquetes");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(this);

        PanelImpresionTiquetes panel = new PanelImpresionTiquetes(
                sistema,
                cliente,
                frame::dispose
        );

        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    private void cargarSaldo(ActionEvent e) {
        String input = JOptionPane.showInputDialog(this, "Ingrese valor a cargar:");
        if (input == null) return;
        try {
            double valor = Double.parseDouble(input);
            sistema.cargarSaldo(cliente.getLogin(), valor);
            JOptionPane.showMessageDialog(this, "Saldo cargado exitosamente");
            actualizarBoletas(null);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor inválido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
