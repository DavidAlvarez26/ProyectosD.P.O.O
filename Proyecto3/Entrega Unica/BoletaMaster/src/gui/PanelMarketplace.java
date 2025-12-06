package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

import Usuarios.Cliente;
import logica.BoletaMaster;
import market.Marketplace;
import market.OfertaMarket;

public class PanelMarketplace extends JFrame {

    private final BoletaMaster sistema;
    private final Cliente cliente;
    private final Marketplace marketplace;

    private JTextArea areaOfertas;

    public PanelMarketplace(BoletaMaster sistema, Cliente cliente) {
        this.sistema = sistema;
        this.cliente = cliente;
        this.marketplace = sistema.getMarketplace();
        setTitle("Marketplace - BoletaMaster");
        setSize(700, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        areaOfertas = new JTextArea();
        areaOfertas.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaOfertas);

        JPanel panelBtns = new JPanel(new GridLayout(2, 3, 10, 10));
        JButton btnVerOfertas = new JButton("Ver Ofertas");
        JButton btnPublicar = new JButton("Publicar Boleta");
        JButton btnContraoferta = new JButton("Hacer Contraoferta");
        JButton btnAceptar = new JButton("Aceptar Contraoferta");
        JButton btnBorrar = new JButton("Borrar Mi Oferta");
        JButton btnCerrar = new JButton("Volver");

        panelBtns.add(btnVerOfertas);
        panelBtns.add(btnPublicar);
        panelBtns.add(btnContraoferta);
        panelBtns.add(btnAceptar);
        panelBtns.add(btnBorrar);
        panelBtns.add(btnCerrar);

        add(scroll, BorderLayout.CENTER);
        add(panelBtns, BorderLayout.SOUTH);

        btnVerOfertas.addActionListener(this::verOfertas);
        btnPublicar.addActionListener(this::publicarOferta);
        btnContraoferta.addActionListener(this::hacerContraoferta);
        btnAceptar.addActionListener(this::aceptarContraoferta);
        btnBorrar.addActionListener(this::borrarOferta);
        btnCerrar.addActionListener(e -> dispose());

        verOfertas(null);
    }

    private void verOfertas(ActionEvent e) {
        List<OfertaMarket> ofertas = marketplace.listarOfertas();
        StringBuilder sb = new StringBuilder();
        for (OfertaMarket o : ofertas) {
            sb.append("ID Oferta: ").append(o.getIdOferta())
              .append(" | Tiquete ID: ").append(o.getTiquete().getIdTiquete())
              .append(" | Evento: ").append(o.getTiquete().getEvento().getNombre())
              .append(" | Precio: $").append(o.getPrecio()).append("\n");
        }
        areaOfertas.setText(sb.toString());
    }

    private void publicarOferta(ActionEvent e) {
        try {
            String idStr = JOptionPane.showInputDialog(this, "ID del tiquete a publicar:");
            String precioStr = JOptionPane.showInputDialog(this, "Precio a ofrecer:");
            if (idStr == null || precioStr == null) return;

            int id = Integer.parseInt(idStr);
            double precio = Double.parseDouble(precioStr);
            marketplace.crearOferta(cliente, id, precio);
            JOptionPane.showMessageDialog(this, "Oferta publicada exitosamente");
            verOfertas(null);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void hacerContraoferta(ActionEvent e) {
        try {
            String idOfertaStr = JOptionPane.showInputDialog(this, "ID de la oferta:");
            String precioStr = JOptionPane.showInputDialog(this, "Precio que propones:");
            if (idOfertaStr == null || precioStr == null) return;

            int idOferta = Integer.parseInt(idOfertaStr);
            double precio = Double.parseDouble(precioStr);
            marketplace.contraOfertar(idOferta, cliente, precio);
            JOptionPane.showMessageDialog(this, "Contraoferta enviada");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void aceptarContraoferta(ActionEvent e) {
        try {
            String idOfertaStr = JOptionPane.showInputDialog(this, "ID de tu oferta:");
            String idContraStr = JOptionPane.showInputDialog(this, "ID de la contraoferta a aceptar:");
            if (idOfertaStr == null || idContraStr == null) return;

            int idOferta = Integer.parseInt(idOfertaStr);
            int idContra = Integer.parseInt(idContraStr);
            boolean ok = marketplace.aceptarContraOferta(idOferta, idContra, cliente);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Contraoferta aceptada");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo aceptar la contraoferta");
            }
            verOfertas(null);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void borrarOferta(ActionEvent e) {
        try {
            String idStr = JOptionPane.showInputDialog(this, "ID de tu oferta a eliminar:");
            if (idStr == null) return;
            int id = Integer.parseInt(idStr);
            boolean ok = marketplace.borrarOfertaPorVendedor(id, cliente);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Oferta eliminada");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar la oferta");
            }
            verOfertas(null);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
