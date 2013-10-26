package sessionBeans;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import valueObjects.UsuarioVO;

/**
 * Session Bean implementation class AdministrarUsuarios
 */
@Stateless
@LocalBean
public class AdministrarUsuariosBean implements AdministrarUsuarios {
    @PersistenceContext
	private EntityManager em; 
	/**
     * Default constructor. 
     */
	
    public AdministrarUsuariosBean() {
        // TODO Auto-generated constructor stub
    }

	@Override
	public boolean agregarUsuario(UsuarioVO usuario) {
		// TODO Auto-generated method stub
		
		return false;
	}

	@Override
	public boolean eliminarUsuario(UsuarioVO usuario) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean loginUsuario(String username, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean actualizarUsuario(UsuarioVO usuario) {
		// TODO Auto-generated method stub
		return false;
	}

}
