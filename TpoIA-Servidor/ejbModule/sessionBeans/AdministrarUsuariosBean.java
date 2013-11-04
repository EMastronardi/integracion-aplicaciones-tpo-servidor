package sessionBeans;

import java.util.ArrayList;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import valueObjects.UsuarioVO;
import entities.Usuario;

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
	public boolean agregarUsuario(String username, String password) {
		// TODO Auto-generated method stub

		Usuario usuario = new Usuario(username, password);
		try {
			em.persist(usuario);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean eliminarUsuario(UsuarioVO usuarioVo) {
		// TODO Auto-generated method stub
		try {
			Usuario usuario = (Usuario) em.find(Usuario.class,
					usuarioVo.getIdUsuario());
			em.remove(usuario);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean validarUsuario(String username, String password) {
		Query q = em
				.createQuery("SELECT OBJECT(u) FROM Usuario u WHERE u.nombre = :nombre and u.password = :password");
		q.setParameter("nombre", username);
		q.setParameter("password", password);
		Usuario u =  (Usuario) q.getSingleResult();
		if(u != null)
			return true;
		return false;
	}

	@Override
	public boolean actualizarUsuario(UsuarioVO usuario) {
		// TODO Auto-generated method stub
		return false;
	}

}
