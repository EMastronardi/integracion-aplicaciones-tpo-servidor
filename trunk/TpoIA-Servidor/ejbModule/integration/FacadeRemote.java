package integration;

import javax.ejb.Remote;

@Remote
public interface FacadeRemote {

	public boolean createUser();
	public boolean loginUser(String username, String password);
	
}
