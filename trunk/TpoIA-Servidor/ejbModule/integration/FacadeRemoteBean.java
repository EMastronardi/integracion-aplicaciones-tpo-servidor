package integration;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;

import sessionBeans.AdministrarDespachos;
import sessionBeans.AdministrarUsuarios;
import valueObjects.UsuarioVO;

/**
 * Session Bean implementation class Facade
 */
@Stateless
@WebService
public class FacadeRemoteBean implements FacadeRemote {
	@EJB(beanName = "AdministrarUsuariosBean")
	private AdministrarUsuarios adminUser;
	
	@EJB(beanName = "AdministrarDespachosBean")
	private AdministrarDespachos adminOD;
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
	//WebServices
	@WebMethod
	public String procesarOrdenDespacho(String orden){
		try {
			return adminOD.procesarSolicitudDespacho(orden);
			
		} catch (Exception e) {
			return "Error al procesarOrdenDespacho";
		}
	}
}
