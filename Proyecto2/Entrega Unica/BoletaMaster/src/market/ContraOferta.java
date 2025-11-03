package market;

import java.time.LocalDateTime;

import Usuarios.Cliente;
import enums.EstadoContraOferta;

public class ContraOferta {
    private final int idContra;
    private final int idOferta;
    private final Cliente comprador;
    private final double precioPropuesto;
    private EstadoContraOferta estado;
    private final LocalDateTime fecha;

    public ContraOferta(int idContra, int idOferta, Cliente comprador, double precioPropuesto) {
        this.idContra = idContra;
        this.idOferta = idOferta;
        this.comprador = comprador;
        this.precioPropuesto = precioPropuesto;
        this.estado = EstadoContraOferta.PROPUESTA;
        this.fecha = LocalDateTime.now();
    }

    public int getIdContra() { return idContra; }
    public int getIdOferta() { return idOferta; }
    public Cliente getComprador() { return comprador; }
    public double getPrecioPropuesto() { return precioPropuesto; }
    public EstadoContraOferta getEstado() { return estado; }
    public void setEstado(EstadoContraOferta estado) { this.estado = estado; }
    public LocalDateTime getFecha() { return fecha; }
}
