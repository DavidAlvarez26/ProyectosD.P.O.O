package presentacion;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import gui.VentanaPrincipal;
import logica.BoletaMaster;
import logica.Evento;
import logica.Localidad;
import logica.Tiquete;
import logica.Venue;
import market.OfertaMarket;
import Usuarios.Administrador;
import Usuarios.Cliente;
import Usuarios.Organizador;
import persistencia.PersistenciaDatos;

public class consolaPrincipal {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            PersistenciaDatos persistencia = new PersistenciaDatos();

            BoletaMaster sistema = new BoletaMaster(
                    null, 
                    null,  
                    null, 
                    null,  
                    null,   
                    persistencia,
                    null    
            );
            cargarDatosPrueba(sistema);

            VentanaPrincipal ventana = new VentanaPrincipal(sistema);
            ventana.setVisible(true);
        });
    }

    /**
     * Carga en memoria datos de prueba basados en los .txt:
     * - Clientes: cliente01 (Juan), cliente02 (Laura)
     * - Venues: Movistar Arena, Teatro Colón, Cine Andino
     * - Eventos: Concierto Coldplay, Obra Hamlet, Festival de Cine
     * - Tiquetes: TQ001–TQ004 (con mismos precios/compradores que en tiquetes.txt)
     */
    private static void cargarDatosPrueba(BoletaMaster sistema) {
        // ===== 1. Listas de tiquetes por cliente =====
        List<Tiquete> tiquetesJuan = new ArrayList<>();
        List<Tiquete> tiquetesLaura = new ArrayList<>();

        // ===== 2. CLIENTES (sacados de usuarios.txt) =====
        Cliente juan = new Cliente(
                "cliente01",          // login
                "pass",               // contraseña
                "Juan Rodríguez",     // nombre
                "juanr@gmail.com",    // correo
                120_000,              // saldo
                tiquetesJuan
        );

        Cliente laura = new Cliente(
                "cliente02",
                "123",
                "Laura Torres",
                "laurita@hotmail.com",
                95_000,
                tiquetesLaura
        );

        // ===== 3. ADMIN y ORGANIZADOR (usuarios.txt) =====
        Administrador admin = new Administrador(
                "admin01",
                "1234",
                "Carlos Pérez",
                "admin@boleta.com",
                0, 0, null
        );

        Organizador organizador = new Organizador(
                "organizador01",
                "abcd",
                "María Gómez",
                "maria@eventos.com",
                5_000, null
        );

        // Registrar todos en BoletaMaster
        sistema.registrarUsuario(juan);
        sistema.registrarUsuario(laura);
        sistema.registrarUsuario(admin);
        sistema.registrarUsuario(organizador);
        sistema.setAdministrador(admin);

        // ===== 4. VENUES (basado en venues.txt) =====
        Venue movistar = new Venue("Bogotá", 12000, "V001", "Movistar Arena", true);
        Venue teatroColon = new Venue("Bogotá", 1500, "V002", "Teatro Colón", true);
        Venue cineAndino = new Venue("Bogotá", 8000, "V006", "Cine Andino", true); // id inventado

        sistema.getVenues().add(movistar);
        sistema.getVenues().add(teatroColon);
        sistema.getVenues().add(cineAndino);

        // ===== 5. LOCALIDADES + EVENTOS (basados en eventos.txt) =====

        // --- Coldplay ---
        Date fechaColdplay = Date.valueOf("2025-11-30");
        Time horaColdplay = Time.valueOf("20:00:00");
        List<Localidad> locsColdplay = new ArrayList<>();

        Localidad locVip = new Localidad("VIP", 150_000, false, 5000, null, null);
        Localidad locGeneral = new Localidad("General", 90_000, false, 7000, null, null);
        Localidad locPlatea = new Localidad("Platea", 110_000, true, 2000, null, null);

        locsColdplay.add(locVip);
        locsColdplay.add(locGeneral);
        locsColdplay.add(locPlatea);
        Evento conciertoColdplay = new Evento(
                "E001",
                "Concierto Coldplay",
                fechaColdplay,
                horaColdplay,
                "Música",
                movistar,
                organizador,
                locsColdplay,
                "Activo"
        );
        sistema.agregarEvento(conciertoColdplay);
        organizador.getEventosOrganizados().add(conciertoColdplay);

        // --- Hamlet ---
        Date fechaHamlet = Date.valueOf("2025-12-15");
        Time horaHamlet = Time.valueOf("19:30:00");
        List<Localidad> locsHamlet = new ArrayList<>();

        Localidad locPreferencial = new Localidad("Preferencial", 120_000, false, 800, null, null);
        Localidad locBalcon = new Localidad("Balcón", 80_000, true, 700, null, null);

        locsHamlet.add(locPreferencial);
        locsHamlet.add(locBalcon);

        Evento obraHamlet = new Evento(
                "E002",
                "Obra Hamlet",
                fechaHamlet,
                horaHamlet,
                "Teatro",
                teatroColon,
                organizador,    
                locsHamlet,
                "Activo"
        );
        sistema.agregarEvento(obraHamlet);
        organizador.getEventosOrganizados().add(obraHamlet);

        // --- Festival Cine ---
        Date fechaFestivalCine = Date.valueOf("2025-12-10");
        Time horaFestivalCine = Time.valueOf("18:00:00");
        List<Localidad> locsCine = new ArrayList<>();

        Localidad locSala1 = new Localidad("Sala1", 80_000, true, 200, null, null);
        Localidad locSala2 = new Localidad("Sala2", 60_000, true, 200, null, null);

        locsCine.add(locSala1);
        locsCine.add(locSala2);

        Evento festivalCine = new Evento(
                "E003",
                "Festival de Cine",
                fechaFestivalCine,
                horaFestivalCine,
                "Cine",
                cineAndino,
                organizador,
                locsCine,
                "Activo"
        );
        sistema.agregarEvento(festivalCine);
        organizador.getEventosOrganizados().add(festivalCine);

        // ===== 6. TIQUETES (basados en tiquetes.txt TQ001–TQ004) =====

        Tiquete tq1 = new Tiquete(
                5_000,              // cargoServicio
                2_000,              // cuotaAdicional
                1,                  // idTiquete
                conciertoColdplay,
                juan,
                locVip,
                "Vendido",
                true
        ) {
            @Override
            public double calcularPrecioTotal() {
                return getPrecioBase() + getCargoServicio() + getCuotaAdicional();
            }
            @Override
            public boolean esTransferible() {
                return isTransferible();
            }
            @Override
            public void marcarComoTranferido() {
                setTransferible(false);
                setEstado("TRANSFERIDO");
            }
        };

        Tiquete tq2 = new Tiquete(
                4_000,
                1_500,
                2,
                conciertoColdplay,
                laura,
                locGeneral,
                "Vendido",
                true
        ) {
            @Override
            public double calcularPrecioTotal() {
                return getPrecioBase() + getCargoServicio() + getCuotaAdicional();
            }
            @Override
            public boolean esTransferible() {
                return isTransferible();
            }
            @Override
            public void marcarComoTranferido() {
                setTransferible(false);
                setEstado("TRANSFERIDO");
            }
        };

        Tiquete tq3 = new Tiquete(
                4_000,
                2_000,
                3,
                obraHamlet,
                juan,
                locPreferencial,
                "Vendido",
                false
        ) {
            @Override
            public double calcularPrecioTotal() {
                return getPrecioBase() + getCargoServicio() + getCuotaAdicional();
            }
            @Override
            public boolean esTransferible() {
                return isTransferible();
            }
            @Override
            public void marcarComoTranferido() {
                setTransferible(false);
                setEstado("TRANSFERIDO");
            }
        };

        Tiquete tq4 = new Tiquete(
                3_000,
                1_000,
                4,
                festivalCine,
                juan,
                locSala1,
                "Vendido",
                true
        ) {
            @Override
            public double calcularPrecioTotal() {
                return getPrecioBase() + getCargoServicio() + getCuotaAdicional();
            }
            @Override
            public boolean esTransferible() {
                return isTransferible();
            }
            @Override
            public void marcarComoTranferido() {
                setTransferible(false);
                setEstado("TRANSFERIDO");
            }
        };

        juan.agregarTiquete(tq1);
        juan.agregarTiquete(tq3);
        juan.agregarTiquete(tq4);

        laura.agregarTiquete(tq2);

        sistema.getTiquetes().add(tq1);
        sistema.getTiquetes().add(tq2);
        sistema.getTiquetes().add(tq3);
        sistema.getTiquetes().add(tq4);

        locVip.getTiquetes().add(tq1);
        locGeneral.getTiquetes().add(tq2);
        locPreferencial.getTiquetes().add(tq3);
        locSala1.getTiquetes().add(tq4);
        // ===== 7. Ofertas de Marketplace de PRUEBA =====
        // Esto automáticamente genera logs internos del marketplace.

        // Oferta 1: Juan revende su tiquete VIP de Coldplay (tq1)
        OfertaMarket of1 = sistema.crearOfertaReventa(
                juan,                      // vendedor
                tq1.getIdTiquete(),        // id del tiquete
                200_000                    // precio de reventa
        );

        // Oferta 2: Laura revende su tiquete General de Coldplay (tq2)
        OfertaMarket of2 = sistema.crearOfertaReventa(
                laura,
                tq2.getIdTiquete(),
                95_000
        );

        // Oferta 3: Juan revende su tiquete de Festival de Cine (tq4)
        OfertaMarket of3 = sistema.crearOfertaReventa(
                juan,
                tq4.getIdTiquete(),
                90_000
        );

        //Generar un log de eliminación de oferta por parte del admin:
        sistema.borrarOfertaReventaAdmin(
                admin,                     // administrador de prueba
                of3.getIdOferta(),         // id de la oferta 3
                "Oferta de prueba eliminada automáticamente"
        );
    }
    }

