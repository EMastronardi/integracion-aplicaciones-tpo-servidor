package integration;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * Session Bean implementation class Facade
 */
@Stateless
@LocalBean
public class Facade implements FacadeRemote {

    /**
     * Default constructor. 
     */
    public Facade() {
        // TODO Auto-generated constructor stub
    }

	@Override
	public boolean createUser() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean loginUser(String username, String password) {
		// TODO Auto-generated method stub
		return false;
	}

}
