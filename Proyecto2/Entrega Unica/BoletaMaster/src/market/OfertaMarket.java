package market;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Usuarios.Cliente;
import enums.EstadoOferta;
import logica.Tiquete;

public class OfertaMarket {
    private final int idOferta;
    private final Tiquete tiquete;    
    private final Cliente vendedor;
    private double precio;
    private EstadoOferta estado;
    private final LocalDateTime publicada;
    private final List<ContraOferta> contraofertas = new ArrayList<>();

    public OfertaMarket(int idOferta, Tiquete tiquete, Cliente vendedor, double precio) {
        this.idOferta = idOferta;
        this.tiquete = tiquete;
        this.vendedor = vendedor;
        this.precio = precio;
        this.estado = EstadoOferta.PUBLICADA;
        this.publicada = LocalDateTime.now();
    }
    public boolean estaActiva() {
        return estado == EstadoOferta.PUBLICADA;
    }
    public int getIdOferta() { return idOferta; }
    public Tiquete getTiquete() { return tiquete; }
    public Cliente getVendedor() { return vendedor; }
    public double getPrecio() { return precio; }
    public void setPrecio(double p) { this.precio = p; }
    public EstadoOferta getEstado() { return estado; }
    public void setEstado(EstadoOferta estado) { this.estado = estado; }
    public List<ContraOferta> getContraofertas() { return contraofertas; }
    public LocalDateTime getPublicada() { return publicada; }

    public void cancelarPorVendedor() { this.estado = EstadoOferta.BORRADA_VENDEDOR; }
    public void cancelarPorAdmin()    { this.estado = EstadoOferta.BORRADA_ADMIN; }
    public void marcarVendida()       { this.estado = EstadoOferta.VENDIDA; }

    public void agregarContraOferta(ContraOferta c) { this.contraofertas.add(c); }
}
