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
	public boolean eliminarUsuario(int idUsuario) {
		// TODO Auto-generated method stub
		try {
			Usuario usuario = (Usuario) em.find(Usuario.class,idUsuario);
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
	public ArrayList<UsuarioVO> getUsers() {
		// TODO Auto-generated method stub
		ArrayList<UsuarioVO> rslt = new ArrayList<UsuarioVO>();
		Query q = em.createQuery("from Usuario u");
		ArrayList<Usuario> users = (ArrayList<Usuario>) q.getResultList();
		
		for (Usuario usuario : users) {
			if(usuario.getNombre() != null){
				UsuarioVO vo = new UsuarioVO(usuario.getIdUsuario(), usuario.getNombre(), usuario.getPassword());
				rslt.add(vo);
			} 
		}
		return rslt;
	}

	@Override
	public boolean actualizarUsuario(int idUsuario, String username,
			String password) {
		try {
			Usuario user = (Usuario) em.find(Usuario.class, idUsuario);
			user.setNombre(username);
			user.setPassword(password);
			em.persist(user);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
