package sessionBeans;

import javax.ejb.Local;

import valueObjects.UsuarioVO;

@Local
public interface AdministrarUsuarios {
	public boolean agregarUsuario(String username, String password);
	public boolean eliminarUsuario(UsuarioVO usuario);
	public boolean loginUsuario(String username, String password);
	public boolean actualizarUsuario(UsuarioVO usuario);
}
