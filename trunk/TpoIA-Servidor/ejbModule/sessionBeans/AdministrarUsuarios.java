package sessionBeans;

import java.util.ArrayList;

import javax.ejb.Local;

import valueObjects.UsuarioVO;

@Local
public interface AdministrarUsuarios {
	public boolean agregarUsuario(String username, String password);
	public boolean eliminarUsuario(int idUsuario);
	public boolean validarUsuario(String username, String password);
	public boolean actualizarUsuario(int idUsuario, String username, String password);
	public ArrayList<UsuarioVO> getUsers();
}
