package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Finanzas.Oferta;
import Finanzas.ReporteFinanciero;
import Usuarios.Organizador;
import logica.BoletaMaster;
import logica.Evento;
import logica.Localidad;
import logica.Venue;

public class PanelOrganizador extends JPanel {

    private static final long serialVersionUID = 1L;

    private final BoletaMaster sistema;
    private final Organizador organizador;
    private final Runnable volverAlMenu; // callback para regresar al menú principal

    private JTextArea txtSalida;

    public PanelOrganizador(BoletaMaster sistema, Organizador organizador, Runnable volverAlMenu) {
        this.sistema = sistema;
        this.organizador = organizador;
        this.volverAlMenu = volverAlMenu;
        inicializarComponentes();
    }

    @SuppressWarnings("unused")
	private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // -------- NORTE: TÍTULO --------
        JLabel lblTitulo = new JLabel("Panel de Organizador - BoletaMaster");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        JPanel pnlTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlTitulo.add(lblTitulo);
        add(pnlTitulo, BorderLayout.NORTH);

        // -------- CENTRO: ÁREA DE SALIDA --------
        txtSalida = new JTextArea(20, 70);
        txtSalida.setEditable(false);
        txtSalida.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(txtSalida);
        scroll.setBorder(BorderFactory.createTitledBorder("Salida / Resultados"));
        add(scroll, BorderLayout.CENTER);

        // -------- SUR: BOTONES --------
        JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnVerEventos = new JButton("Ver mis eventos");
        JButton btnCrearEvento = new JButton("Crear nuevo evento");
        JButton btnVerReporte = new JButton("Ver reporte financiero");
        JButton btnCrearOferta = new JButton("Crear oferta en localidad");
        JButton btnVolver = new JButton("Volver al menú");

        pnlSur.add(btnVerEventos);
        pnlSur.add(btnCrearEvento);
        pnlSur.add(btnVerReporte);
        pnlSur.add(btnCrearOferta);
        pnlSur.add(btnVolver);

        add(pnlSur, BorderLayout.SOUTH);

        btnVerEventos.addActionListener(this::accionVerMisEventos);
        btnCrearEvento.addActionListener(this::accionCrearEvento);
        btnVerReporte.addActionListener(this::accionVerReporteFinanciero);
        btnCrearOferta.addActionListener(this::accionCrearOferta);
        btnVolver.addActionListener(e -> {
            if (volverAlMenu != null) volverAlMenu.run();
        });
    }
    private void accionVerMisEventos(ActionEvent e) {
        txtSalida.setText("");
        try {
            List<Evento> todos = sistema.consultarCatalogoEventos(null, null, null);
            List<Evento> mios = new ArrayList<>();

            for (Evento ev : todos) {
                if (ev.getOrganizador() != null &&
                    ev.getOrganizador().getLogin().equals(organizador.getLogin())) {
                    mios.add(ev);
                }
            }

            if (mios.isEmpty()) {
                txtSalida.append("No tienes eventos registrados.\n");
                return;
            }

            txtSalida.append("===== MIS EVENTOS =====\n");
            for (Evento ev : mios) {
                txtSalida.append(
                    String.format(
                        "ID=%s | Nombre=%s | Fecha=%s %s | Tipo=%s | Venue=%s | Estado=%s%n",
                        ev.getIdEvento(),
                        ev.getNombre(),
                        ev.getFecha(),
                        ev.getHora(),
                        ev.getTipo(),
                        (ev.getVenue() != null ? ev.getVenue().getNombre() : "N/A"),
                        ev.getEstado()
                    )
                );
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Error al consultar tus eventos: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    private void accionCrearEvento(ActionEvent e) {
        try {
            String id = JOptionPane.showInputDialog(this, "ID del evento:", "E010");
            if (id == null || id.trim().isEmpty()) return;

            String nombre = JOptionPane.showInputDialog(this, "Nombre del evento:", "Nuevo Evento");
            if (nombre == null || nombre.trim().isEmpty()) return;

            String fechaStr = JOptionPane.showInputDialog(this, "Fecha (AAAA-MM-DD):", "2026-01-10");
            if (fechaStr == null || fechaStr.trim().isEmpty()) return;
            Date fecha = Date.valueOf(fechaStr.trim());

            String horaStr = JOptionPane.showInputDialog(this, "Hora (HH:MM:SS):", "20:00:00");
            if (horaStr == null || horaStr.trim().isEmpty()) return;
            Time hora = Time.valueOf(horaStr.trim());

            String tipo = JOptionPane.showInputDialog(this, "Tipo de evento (Música, Teatro, etc.):", "Música");
            if (tipo == null || tipo.trim().isEmpty()) return;

            String idVenue = JOptionPane.showInputDialog(this, "ID del Venue existente:", "V001");
            if (idVenue == null || idVenue.trim().isEmpty()) return;
            Venue venue = sistema.buscarVenue(idVenue.trim());
            if (venue == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "No existe un venue con ese ID.",
                    "Venue no encontrado",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            if (!venue.esAprobado()) {
                JOptionPane.showMessageDialog(
                    this,
                    "El venue seleccionado NO está aprobado.",
                    "Venue no aprobado",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String nombreLoc = JOptionPane.showInputDialog(this, "Nombre de localidad inicial:", "General");
            if (nombreLoc == null || nombreLoc.trim().isEmpty()) return;

            String precioStr = JOptionPane.showInputDialog(this, "Precio base localidad:", "50000");
            if (precioStr == null || precioStr.trim().isEmpty()) return;
            double precio = Double.parseDouble(precioStr.trim());

            Localidad loc = new Localidad(nombreLoc.trim(), precio, false, 100, new ArrayList<>(), null);
            List<Localidad> locs = new ArrayList<>();
            locs.add(loc);

            Evento nuevo = new Evento(id.trim(), nombre.trim(), fecha, hora, tipo.trim(), venue, organizador, locs, "Activo");

            sistema.crearEvento(organizador, nuevo);

            txtSalida.setText("");
            txtSalida.append("Evento creado exitosamente:\n");
            txtSalida.append(
                String.format(
                    "ID=%s | Nombre=%s | Fecha=%s %s | Tipo=%s | Venue=%s | Localidad=%s (%.2f)\n",
                    nuevo.getIdEvento(),
                    nuevo.getNombre(),
                    nuevo.getFecha(),
                    nuevo.getHora(),
                    nuevo.getTipo(),
                    nuevo.getVenue().getNombre(),
                    loc.getNombre(),
                    loc.getPrecioLocalidad()
                )
            );

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Error en los datos (fecha/hora/número inválido): " + ex.getMessage(),
                "Datos inválidos",
                JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Error al crear evento: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    private void accionVerReporteFinanciero(ActionEvent e) {
        txtSalida.setText("");
        try {
            ReporteFinanciero rep = sistema.generarReporteOrganizador(organizador);

            if (rep == null) {
                txtSalida.append("No hay información financiera disponible.\n");
                return;
            }

            txtSalida.append("===== REPORTE FINANCIERO ORGANIZADOR =====\n");
            txtSalida.append("Organizador: " + organizador.getNombre() + "\n\n");
            txtSalida.append(String.format("Ganancias totales: $%.2f%n%n", rep.getGananciasTotales()));

            txtSalida.append("Ganancias por evento:\n");
            rep.getGananciasxEvento().forEach((evento, valor) -> {
                txtSalida.append(
                    String.format(" - %s: $%.2f%n", evento.getNombre(), valor)
                );
            });

            txtSalida.append("\n(Nota: las ganancias por organizador ya están calculadas en el reporte general.)\n");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Error al generar reporte financiero: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void accionCrearOferta(ActionEvent e) {
        try {
            String idEvento = JOptionPane.showInputDialog(this, "ID del evento:", "E001");
            if (idEvento == null || idEvento.trim().isEmpty()) return;

            Evento ev = sistema.buscarEvento(idEvento.trim());
            if (ev == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "No existe evento con ese ID.",
                    "Evento no encontrado",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            if (ev.getOrganizador() == null ||
                !ev.getOrganizador().getLogin().equals(organizador.getLogin())) {
                JOptionPane.showMessageDialog(
                    this,
                    "Ese evento no pertenece a este organizador.",
                    "Acceso denegado",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String nombreLoc = JOptionPane.showInputDialog(this, "Nombre de la localidad:", "General");
            if (nombreLoc == null || nombreLoc.trim().isEmpty()) return;

            Localidad loc = null;
            for (Localidad l : ev.getLocalidades()) {
                if (l.getNombre().equalsIgnoreCase(nombreLoc.trim())) {
                    loc = l;
                    break;
                }
            }
            if (loc == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "La localidad no existe en ese evento.",
                    "Localidad no encontrada",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String descStr = JOptionPane.showInputDialog(this, "Descuento (por ejemplo 0.2 para 20%):", "0.2");
            if (descStr == null || descStr.trim().isEmpty()) return;
            double descuento = Double.parseDouble(descStr.trim());

            String fiStr = JOptionPane.showInputDialog(this, "Fecha inicio (AAAA-MM-DD):", "2025-11-01");
            if (fiStr == null || fiStr.trim().isEmpty()) return;
            Date fi = Date.valueOf(fiStr.trim());

            String ffStr = JOptionPane.showInputDialog(this, "Fecha fin (AAAA-MM-DD):", "2025-11-30");
            if (ffStr == null || ffStr.trim().isEmpty()) return;
            Date ff = Date.valueOf(ffStr.trim());

            Oferta oferta = new Oferta(descuento, fi, ff);
            sistema.aplicarOferta(ev, loc, oferta);

            txtSalida.setText("");
            txtSalida.append("Oferta aplicada correctamente:\n");
            txtSalida.append(
                String.format(
                    "Evento: %s | Localidad: %s | Descuento: %.2f (desde %s hasta %s)%n",
                    ev.getNombre(), loc.getNombre(), descuento, fi, ff
                )
            );

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Error en los datos (fecha/número inválido): " + ex.getMessage(),
                "Datos inválidos",
                JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Error al crear oferta: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
