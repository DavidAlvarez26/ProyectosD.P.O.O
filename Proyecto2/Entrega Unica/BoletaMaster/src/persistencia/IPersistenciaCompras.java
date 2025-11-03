package persistencia;

import java.util.List;

import Finanzas.Compra;

public interface IPersistenciaCompras {
    List<Compra> cargarCompras(String archivo);
    void guardarCompras(String archivo, List<Compra> compras);
}
