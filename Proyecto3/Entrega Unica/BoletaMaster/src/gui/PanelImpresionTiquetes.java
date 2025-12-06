package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import Usuarios.Cliente;
import logica.BoletaMaster;
import logica.Tiquete;

public class PanelImpresionTiquetes extends JPanel {

    private static final long serialVersionUID = 1L;

    private final BoletaMaster sistema;
    private final Cliente cliente;
    private final Runnable volverAlMenu;

    private DefaultListModel<Tiquete> modeloLista;
    private JList<Tiquete> listaTiquetes;
    private JTextArea txtDetalle;
    private JLabel lblQr;

    public PanelImpresionTiquetes(BoletaMaster sistema, Cliente cliente, Runnable volverAlMenu) {
        this.sistema = sistema;
        this.cliente = cliente;
        this.volverAlMenu = volverAlMenu;
        inicializarComponentes();
    }

    @SuppressWarnings("unused")
	private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // -------- NORTE: título ----------
        JLabel lblTitulo = new JLabel("Impresión de Tiquetes - Cliente: " + cliente.getNombre());
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        JPanel pnlTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlTitulo.add(lblTitulo);
        add(pnlTitulo, BorderLayout.NORTH);

        // -------- OESTE: lista de tiquetes ----------
        modeloLista = new DefaultListModel<>();
        listaTiquetes = new JList<>(modeloLista);
        listaTiquetes.setBorder(BorderFactory.createTitledBorder("Tiquetes del cliente"));

        // Renderer para mostrar info útil en cada fila
        listaTiquetes.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    javax.swing.JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Tiquete t) {
                    String texto = "ID " + t.getIdTiquete() +
                                   " - " + (t.getEvento() != null ? t.getEvento().getNombre() : "Sin evento") +
                                   " - Estado: " + t.getEstado() +
                                   (t.estaImpreso() ? " [IMPRESO]" : "");
                    setText(texto);
                }
                return this;
            }
        });

        JScrollPane scrollLista = new JScrollPane(listaTiquetes);
        scrollLista.setPreferredSize(new java.awt.Dimension(320, 400));
        add(scrollLista, BorderLayout.WEST);

        // -------- CENTRO: detalle + QR ----------
        JPanel pnlCentro = new JPanel(new BorderLayout(5, 5));

        txtDetalle = new JTextArea(10, 40);
        txtDetalle.setEditable(false);
        txtDetalle.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollDetalle = new JScrollPane(txtDetalle);
        scrollDetalle.setBorder(BorderFactory.createTitledBorder("Detalle del tiquete"));

        lblQr = new JLabel();
        lblQr.setHorizontalAlignment(JLabel.CENTER);
        lblQr.setBorder(BorderFactory.createTitledBorder("Código QR"));

        pnlCentro.add(scrollDetalle, BorderLayout.CENTER);
        pnlCentro.add(lblQr, BorderLayout.EAST);

        add(pnlCentro, BorderLayout.CENTER);

        // -------- SUR: botones ----------
        JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnCargar = new JButton("Cargar tiquetes");
        JButton btnVerDetalle = new JButton("Ver detalle");
        JButton btnImprimir = new JButton("Imprimir tiquete");
        JButton btnVolver = new JButton("Volver");

        pnlSur.add(btnCargar);
        pnlSur.add(btnVerDetalle);
        pnlSur.add(btnImprimir);
        pnlSur.add(btnVolver);

        add(pnlSur, BorderLayout.SOUTH);

        // Listeners
        btnCargar.addActionListener(e -> cargarTiquetesCliente());
        btnVerDetalle.addActionListener(e -> mostrarDetalleSeleccionado());
        btnImprimir.addActionListener(e -> imprimirSeleccionado());
        btnVolver.addActionListener(e -> {
            if (volverAlMenu != null) volverAlMenu.run();
        });
    }

    // =========================================================
    // LÓGICA
    // =========================================================

    private void cargarTiquetesCliente() {
        modeloLista.clear();
        List<Tiquete> lista = cliente.getTiquetes();

        if (lista == null || lista.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "El cliente no tiene tiquetes asociados.",
                "Sin tiquetes",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        for (Tiquete t : lista) {
            modeloLista.addElement(t);
        }
    }

    private Tiquete getSeleccionado() {
        Tiquete t = listaTiquetes.getSelectedValue();
        if (t == null) {
            JOptionPane.showMessageDialog(
                this,
                "Debes seleccionar un tiquete de la lista.",
                "Sin selección",
                JOptionPane.WARNING_MESSAGE
            );
        }
        return t;
    }

    private void mostrarDetalleSeleccionado() {
        Tiquete t = getSeleccionado();
        if (t == null) return;

        txtDetalle.setText("");
        txtDetalle.append("===== DETALLE TIQUETE =====\n");
        txtDetalle.append("ID: " + t.getIdTiquete() + "\n");
        txtDetalle.append("Evento: " + (t.getEvento() != null ? t.getEvento().getNombre() : "N/A") + "\n");
        txtDetalle.append("Fecha evento: " + t.getFechaEvento() + "\n");
        txtDetalle.append("Localidad: " +
                (t.getLocalidad() != null ? t.getLocalidad().getNombre() : "N/A") + "\n");
        txtDetalle.append("Estado: " + t.getEstado() + "\n");
        txtDetalle.append("Transferible: " + (t.esTransferible() ? "Sí" : "No") + "\n");
        txtDetalle.append("Impreso: " + (t.estaImpreso() ? "Sí" : "No") + "\n");
        txtDetalle.append(String.format("Precio total: $%.2f%n", t.calcularPrecioTotal()));
    }

    private void imprimirSeleccionado() {
        Tiquete t = getSeleccionado();
        if (t == null) return;
        if (t.estaImpreso()) {
            JOptionPane.showMessageDialog(
                this,
                "Este tiquete YA fue impreso previamente. No se puede reimprimir.",
                "Impresión bloqueada",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            String eventoNombre = (t.getEvento() != null ? t.getEvento().getNombre() : "N/A");
            String fechaEvento = (t.getFechaEvento() != null ? t.getFechaEvento().toString() : "N/A");
            LocalDateTime ahora = LocalDateTime.now();
            String fechaImpresion = ahora.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            String contenidoQR = "Evento=" + eventoNombre +
                                 ";ID=" + t.getIdTiquete() +
                                 ";FechaEvento=" + fechaEvento +
                                 ";FechaImpresion=" + fechaImpresion;

            BufferedImage imgQR = generarCodigoQR(contenidoQR, 250, 250);
            lblQr.setIcon(new ImageIcon(imgQR));

            txtDetalle.setText("");
            txtDetalle.append("===== TIQUETE IMPRESO =====\n");
            txtDetalle.append("Evento: " + eventoNombre + "\n");
            txtDetalle.append("ID tiquete: " + t.getIdTiquete() + "\n");
            txtDetalle.append("Fecha evento: " + fechaEvento + "\n");
            txtDetalle.append("Fecha impresión: " + fechaImpresion + "\n");
            txtDetalle.append(String.format("Precio total: $%.2f%n", t.calcularPrecioTotal()));
            txtDetalle.append("\n(El código QR contiene todos estos datos.)\n");

            t.marcarComoImpreso();

            JOptionPane.showMessageDialog(
                this,
                "Tiquete impreso correctamente. Se ha bloqueado la reimpresión y la reventa.",
                "Impresión exitosa",
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (WriterException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Error al generar el código QR: " + ex.getMessage(),
                "Error QR",
                JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Error al imprimir el tiquete: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private BufferedImage generarCodigoQR(String texto, int ancho, int alto) throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(texto, BarcodeFormat.QR_CODE, ancho, alto);

        BufferedImage image = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < ancho; x++) {
            for (int y = 0; y < alto; y++) {
                boolean negro = matrix.get(x, y);
                int color = negro ? 0x000000 : 0xFFFFFF;
                image.setRGB(x, y, color);
            }
        }

        return image;
    }
}

