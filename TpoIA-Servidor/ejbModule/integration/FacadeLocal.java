package integration;

import javax.ejb.Local;

@Local
public interface FacadeLocal {
	public boolean createUser(String username, String password);
	public boolean loginUser(String username, String password);

}
