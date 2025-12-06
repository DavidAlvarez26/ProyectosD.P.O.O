package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Usuarios.Administrador;
import logica.BoletaMaster;
import market.Log;
import market.OfertaMarket;

public class PanelAdministrador extends JPanel {

    private static final long serialVersionUID = 1L;

    private final BoletaMaster sistema;
    private final Administrador admin;
    private final Runnable volverAlMenu; 

    private JTextArea txtSalida;
    private JTextField txtIdOferta;
    private JTextField txtMotivo;



    public PanelAdministrador(BoletaMaster sistema, Administrador admin, Runnable volverAlMenu) {
        this.sistema = sistema;
        this.admin = admin;
        this.volverAlMenu = volverAlMenu;

        inicializarComponentes();
    }
	

	@SuppressWarnings("unused")
	private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ------------------ NORTE: título ------------------
        JLabel lblTitulo = new JLabel("Panel de Administrador - BoletaMaster");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        JPanel pnlTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlTitulo.add(lblTitulo);
        add(pnlTitulo, BorderLayout.NORTH);

        // ------------------ CENTRO: área de texto ------------------
        txtSalida = new JTextArea(20, 70);
        txtSalida.setEditable(false);
        txtSalida.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(txtSalida);
        scroll.setBorder(BorderFactory.createTitledBorder("Salida / Resultados"));
        add(scroll, BorderLayout.CENTER);

        // ------------------ SUR: controles (botones + eliminar oferta) ------------------
        JPanel pnlSur = new JPanel(new BorderLayout());

        // zona botones
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnVerLogs = new JButton("Ver logs marketplace");
        JButton btnVerOfertas = new JButton("Ver ofertas activas");
        JButton btnVolver = new JButton("Volver al menú");

        pnlBotones.add(btnVerLogs);
        pnlBotones.add(btnVerOfertas);
        pnlBotones.add(btnVolver);

        pnlSur.add(pnlBotones, BorderLayout.NORTH);

        // zona eliminación de oferta
        JPanel pnlEliminar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlEliminar.setBorder(BorderFactory.createTitledBorder("Eliminar oferta"));

        pnlEliminar.add(new JLabel("ID oferta:"));
        txtIdOferta = new JTextField(6);
        pnlEliminar.add(txtIdOferta);

        pnlEliminar.add(new JLabel("Motivo:"));
        txtMotivo = new JTextField(20);
        pnlEliminar.add(txtMotivo);

        JButton btnEliminar = new JButton("Eliminar oferta");
        pnlEliminar.add(btnEliminar);

        pnlSur.add(pnlEliminar, BorderLayout.CENTER);

        add(pnlSur, BorderLayout.SOUTH);

        // ------------------ Listeners ------------------
        btnVerLogs.addActionListener(this::accionVerLogs);
        btnVerOfertas.addActionListener(this::accionVerOfertas);
        btnEliminar.addActionListener(this::accionEliminarOferta);
        btnVolver.addActionListener(e -> {
            if (volverAlMenu != null) volverAlMenu.run();
        });
    }

    // ================== ACCIONES ==================

    private void accionVerLogs(ActionEvent e) {
        txtSalida.setText("");
        try {
            List<Log> logs = sistema.consultarLogsMarketplace(admin);

            if (logs == null || logs.isEmpty()) {
                txtSalida.append("No hay registros en el log.\n");
                return;
            }

            txtSalida.append("===== LOGS DEL MARKETPLACE =====\n");
            for (Log log : logs) {
                txtSalida.append(
                    String.format(
                        "[%s] %-20s | oferta=%d | actor=%s | %s%n",
                        log.getFecha(),
                        log.getAccion(),
                        log.getIdOferta(),
                        log.getActor(),
                        log.getDetalle()
                    )
                );
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Error al consultar logs: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void accionVerOfertas(ActionEvent e) {
        txtSalida.setText("");
        try {
            List<OfertaMarket> ofertas = sistema.listarOfertasReventa();

            if (ofertas == null || ofertas.isEmpty()) {
                txtSalida.append("No hay ofertas activas en el marketplace.\n");
                return;
            }

            txtSalida.append("===== OFERTAS ACTIVAS =====\n");
            for (OfertaMarket o : ofertas) {
                txtSalida.append(
                    String.format(
                        "ID=%d | Tiquete=%s | Vendedor=%s | Precio=%.2f | Estado=%s%n",
                        o.getIdOferta(),
                        (o.getTiquete() != null ? o.getTiquete().getIdTiquete() : "N/A"),
                        (o.getVendedor() != null ? o.getVendedor().getLogin() : "N/A"),
                        o.getPrecio(),
                        o.getEstado()
                    )
                );
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Error al listar ofertas: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void accionEliminarOferta(ActionEvent e) {
        String idTexto = txtIdOferta.getText().trim();
        String motivo = txtMotivo.getText().trim();

        if (idTexto.isEmpty() || motivo.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Debes ingresar el ID de la oferta y un motivo.",
                "Datos incompletos",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idTexto);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                this,
                "El ID de la oferta debe ser un número entero.",
                "Formato inválido",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        boolean ok;
        try {
            ok = sistema.borrarOfertaReventaAdmin(admin, id, motivo);
        } catch (Exception ex) { 

            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Error al eliminar oferta: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (ok) {
            txtSalida.append(String.format("Oferta %d eliminada correctamente. Motivo: %s%n", id, motivo));
            txtIdOferta.setText("");
            txtMotivo.setText("");
        } else {
            JOptionPane.showMessageDialog(
                this,
                "No se pudo eliminar la oferta (puede no existir o no estar activa).",
                "Operación no realizada",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
}
