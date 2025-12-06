package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import logica.BoletaMaster;
import logica.Usuario;
import Usuarios.Administrador;
import Usuarios.Cliente;
import Usuarios.Organizador;

public class VentanaPrincipal extends JFrame {

    private JTextField txtLogin;
    private JPasswordField txtPassword;
    private JComboBox<String> comboRol;
    private final BoletaMaster sistema;

    public VentanaPrincipal(BoletaMaster sistema) {
        this.sistema = sistema;

        setTitle("BoletaMaster - Login");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        txtLogin = new JTextField();
        txtPassword = new JPasswordField();
        comboRol = new JComboBox<>(new String[]{"Cliente", "Organizador", "Administrador"});
        JButton btnIngresar = new JButton("Ingresar");

        panel.add(new JLabel("Login:"));
        panel.add(txtLogin);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtPassword);
        panel.add(new JLabel("Rol:"));
        panel.add(comboRol);
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.add(btnIngresar);

        getContentPane().setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);

        btnIngresar.addActionListener(this::ingresar);
    }

    private void ingresar(ActionEvent e) {
        String login = txtLogin.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();
        String rol = (String) comboRol.getSelectedItem();

        if (login.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes ingresar login y contraseña");
            return;
        }

        if (sistema == null) {
            JOptionPane.showMessageDialog(this, "Error interno: sistema no inicializado");
            return;
        }

        Usuario u = sistema.buscarUsuario(login);

        if (u == null || !u.getContrasena().equals(pass)) {
            JOptionPane.showMessageDialog(this, "Login o contraseña incorrectos");
            return;
        }

        switch (rol) {
        case "Cliente" -> {
            if (u instanceof Cliente c) {
                SwingUtilities.invokeLater(() -> {
                    new PanelCliente(sistema, c).setVisible(true);
                });
                dispose();
            } else {
                showError();
            }
        }
        case "Organizador" -> {
            if (u instanceof Organizador o) {
                SwingUtilities.invokeLater(() -> {
                    JFrame frame = new JFrame("Organizador - BoletaMaster");
                    PanelOrganizador panel = new PanelOrganizador(
                            sistema,
                            o,
                            () -> {
                                frame.dispose();
                                new VentanaPrincipal(sistema).setVisible(true);
                            }
                    );
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setContentPane(panel);
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                });
                dispose();
            } else {
                showError();
            }
        }
        case "Administrador" -> {
            if (u instanceof Administrador a) {
                SwingUtilities.invokeLater(() -> {
                    JFrame frame = new JFrame("Administrador - BoletaMaster");
                    PanelAdministrador panel = new PanelAdministrador(
                            sistema,
                            a,
                            () -> {
                                frame.dispose();
                                new VentanaPrincipal(sistema).setVisible(true);
                            }
                    );
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setContentPane(panel);
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                });
                dispose();
            } else {
                showError();
            }
        }
    }
        }
    

    private void showError() {
        JOptionPane.showMessageDialog(this, "El usuario no tiene el rol seleccionado");
    }


    }
