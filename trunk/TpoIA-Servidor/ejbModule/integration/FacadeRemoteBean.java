package integration;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import sessionBeans.AdministrarUsuarios;
import valueObjects.UsuarioVO;

/**
 * Session Bean implementation class Facade
 */
@Stateless
@LocalBean
public class FacadeRemoteBean implements FacadeRemote {
	@EJB(beanName = "AdministrarUsuariosBean")
	private AdministrarUsuarios adminUser;
    /**
     * Default constructor. 
     */
    public FacadeRemoteBean() {
        // TODO Auto-generated constructor stub
    }
	@Override
	public boolean loginUser(String username, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createUser(String username, String password) {
		// TODO Auto-generated method stub
		return adminUser.agregarUsuario(username,password);
	}

}
