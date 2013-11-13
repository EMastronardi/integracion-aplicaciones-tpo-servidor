package sessionBeans;

import javax.ejb.Remote;

import entities.Articulo;

@Remote
public interface AdministradorArticulos {
	public boolean createArticulo(int codigo, String nombre, int idModulo);
}
