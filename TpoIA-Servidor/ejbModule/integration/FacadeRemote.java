package integration;

import javax.ejb.Remote;

import valueObjects.UsuarioVO;

@Remote
public interface FacadeRemote {
	public boolean createUser(String username, String password);
	public boolean loginUser(String username, String password);
}
