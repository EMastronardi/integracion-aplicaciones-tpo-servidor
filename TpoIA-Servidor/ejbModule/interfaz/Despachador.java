package interfaz;

import javax.ejb.Remote;

@Remote
public interface Despachador {
	String procesarOrdenDespacho(String orden);
}
