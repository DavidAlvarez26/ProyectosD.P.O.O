package market;

import java.util.ArrayList;
import java.util.List;

import Usuarios.Administrador;
import Usuarios.Cliente;
import enums.EstadoContraOferta;
import enums.TipoLog;
import logica.IPuertoTiquetes;
import logica.IPuertoUsuarios;
import logica.Tiquete;

public class Marketplace {

	private List<OfertaMarket> ofertas;
	private List<ContraOferta> contraofertas;
	private List<Log> logs;

	private IPuertoUsuarios usuarios;
	private IPuertoTiquetes tiquetes;

	private int nextIdOferta = 1;
	private int nextIdContra = 1;

	public Marketplace(IPuertoUsuarios usuarios, IPuertoTiquetes tiquetes) {
		this.ofertas = new ArrayList<>();
		this.contraofertas = new ArrayList<>();
		this.logs = new ArrayList<>();
		this.usuarios = usuarios;
		this.tiquetes = tiquetes;
	}

	private void log(TipoLog tipo, String actor, int id, String detalle) {
		logs.add(new Log(tipo, actor, id, detalle));
	}

	private OfertaMarket buscarOferta(int id) {
		for (OfertaMarket o : ofertas)
			if (o.getIdOferta() == id)
				return o;
		return null;
	}

	public OfertaMarket crearOferta(Cliente vendedor, int idTiquete, double precio) {
		Tiquete t = tiquetes.buscarTiquete(idTiquete);

		if (t == null)
			throw new IllegalArgumentException("Tiquete no existe");
		if (!t.getComprador().equals(vendedor))
			throw new IllegalArgumentException("No eres el dueño");
		if (!t.esTransferible())
			throw new IllegalArgumentException("Tiquete no transferible");

		tiquetes.reservarTiquete(idTiquete);

		OfertaMarket o = new OfertaMarket(nextIdOferta++, t, vendedor, precio);
		ofertas.add(o);

		log(TipoLog.CREAR_OFERTA, vendedor.getLogin(), o.getIdOferta(),
				"Publica tiquete " + idTiquete + " por $" + precio);

		return o;
	}

	public boolean borrarOfertaPorVendedor(int idOferta, Cliente vendedor) {
		OfertaMarket o = buscarOferta(idOferta);

		if (o == null || !o.estaActiva() || !o.getVendedor().equals(vendedor))
			return false;

		o.cancelarPorVendedor();
		tiquetes.liberarTiquete(o.getTiquete().getIdTiquete());

		log(TipoLog.BORRAR_OFERTA_VENDEDOR, vendedor.getLogin(), idOferta, "Vendedor eliminó oferta");

		return true;
	}

	public boolean borrarOfertaPorAdmin(int idOferta, Administrador admin, String motivo) {
		OfertaMarket o = buscarOferta(idOferta);

		if (o == null || !o.estaActiva())
			return false;

		o.cancelarPorAdmin();
		tiquetes.liberarTiquete(o.getTiquete().getIdTiquete());

		log(TipoLog.BORRAR_OFERTA_ADMIN, admin.getLogin(), idOferta, "Admin borró oferta. Motivo: " + motivo);

		return true;
	}

	public boolean comprarOferta(int idOferta, Cliente comprador) {
		OfertaMarket o = buscarOferta(idOferta);

		if (o == null || !o.estaActiva())
			return false;
		if (o.getVendedor().equals(comprador))
			return false;

		double precio = o.getPrecio();

		if (!usuarios.descontarSaldo(comprador.getLogin(), precio))
			return false;

		usuarios.cargarSaldo(o.getVendedor().getLogin(), precio);

		tiquetes.transferirTiquete(o.getVendedor(), comprador, o.getTiquete());
		o.marcarVendida();

		log(TipoLog.COMPRAR_OFERTA, comprador.getLogin(), idOferta, "Compra directa por $" + precio);

		return true;
	}

	public ContraOferta contraOfertar(int idOferta, Cliente comprador, double precio) {
		OfertaMarket o = buscarOferta(idOferta);

		if (o == null || !o.estaActiva())
			throw new IllegalArgumentException("Oferta inactiva");
		if (o.getVendedor().equals(comprador))
			throw new IllegalArgumentException("No puedes ofertarte a ti mismo");

		ContraOferta c = new ContraOferta(nextIdContra++, idOferta, comprador, precio);
		o.agregarContraOferta(c);
		contraofertas.add(c);

		log(TipoLog.CONTRAOFERTA, comprador.getLogin(), idOferta, "Propone $" + precio);

		return c;
	}

	public boolean aceptarContraOferta(int idOferta, int idContra, Cliente vendedor) {
		OfertaMarket o = buscarOferta(idOferta);

		if (o == null || !o.estaActiva() || !o.getVendedor().equals(vendedor))
			return false;

		ContraOferta c = o.getContraofertas().stream().filter(x -> x.getIdContra() == idContra).findFirst()
				.orElse(null);

		if (c == null || c.getEstado() != EstadoContraOferta.PROPUESTA)
			return false;

		double precio = c.getPrecioPropuesto();

		if (!usuarios.descontarSaldo(c.getComprador().getLogin(), precio))
			return false;

		usuarios.cargarSaldo(vendedor.getLogin(), precio);

		tiquetes.transferirTiquete(vendedor, c.getComprador(), o.getTiquete());
		c.setEstado(EstadoContraOferta.ACEPTADA);
		o.marcarVendida();

		log(TipoLog.ACEPTAR_CONTRAOFERTA, vendedor.getLogin(), idOferta, "Acepta $" + precio);

		return true;
	}

	public List<OfertaMarket> listarOfertas() {
		List<OfertaMarket> activos = new ArrayList<>();
		for (OfertaMarket o : ofertas)
			if (o.estaActiva())
				activos.add(o);
		return activos;
	}

	public List<Log> consultarLog(Object usuario) {
		if (!usuarios.esAdmin(usuario))
			throw new IllegalArgumentException("Solo admin puede ver logs");

		return logs;
	}

}
