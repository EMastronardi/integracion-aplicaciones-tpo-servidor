package sessionBeans;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;

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

	Logger logger = Logger.getLogger(AdministrarUsuariosBean.class);

	public AdministrarUsuariosBean() {
	}

	public boolean agregarUsuario(String username, String password) {
		logger.info("Agregando usuario");
		Usuario usuario = new Usuario(username, password);
		try {
			em.persist(usuario);
			return true;
		} catch (Exception e) {
			logger.error("Error al agregar un usuario nuevo");
			e.printStackTrace();
			return false;
		}
	}

	public boolean eliminarUsuario(int idUsuario) {
		logger.info("Eliminando usuario por id");
		try {
			Usuario usuario = (Usuario) em.find(Usuario.class, idUsuario);
			em.remove(usuario);
			return true;
		} catch (Exception e) {
			logger.error("Error eliminando usuario por id");
			e.printStackTrace();
			return false;
		}
	}

	public boolean validarUsuario(String username, String password) {
		logger.info("Validando usuario");
		Query q = em
				.createQuery("SELECT u FROM Usuario u WHERE u.nombre = :nombre and u.password = :password");
		q.setParameter("nombre", username);
		q.setParameter("password", password);

		List<Usuario> u = (List<Usuario>) q.getResultList();
		if (u != null && u.size() >= 1) {
			return true;
		}
		logger.warn("ValidarUsuario - usuario no encontrado");
		return false;
	}

	public ArrayList<UsuarioVO> getUsers() {
		logger.info("Obtener todos los UsersVOs");
		try {
			ArrayList<UsuarioVO> rslt = new ArrayList<UsuarioVO>();
			Query q = em.createQuery("from Usuario u");
			ArrayList<Usuario> users = (ArrayList<Usuario>) q.getResultList();

			for (Usuario usuario : users) {
				if (usuario.getNombre() != null) {
					UsuarioVO vo = new UsuarioVO(usuario.getIdUsuario(),
							usuario.getNombre(), usuario.getPassword());
					rslt.add(vo);
				}
			}
			return rslt;
		} catch (Exception e) {
			logger.error("Error al obtener todos los usersVOs");
			e.printStackTrace();
			return null;
		}
	}

	public boolean actualizarUsuario(int idUsuario, String username,
			String password) {
		logger.info("Actualizando Usuario");
		try {
			Usuario user = (Usuario) em.find(Usuario.class, idUsuario);
			user.setNombre(username);
			user.setPassword(password);
			em.persist(user);
			return true;
		} catch (Exception e) {
			logger.error("Error actualizando usuario");
			e.printStackTrace();
			return false;
		}
	}
}
