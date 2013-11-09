package integration;

import javax.ejb.Remote;

import entities.Articulo;
import entities.Modulo;

@Remote
public interface FacadeRemote {
	public boolean createUser(String username, String password);
	public boolean validarUsuario(String usuario, String password);
	public boolean validarUsuarioLogueado(String usuario);
	public Modulo getModulo(int idModulo);
	public boolean addArticulo(Articulo articulo);
}
