package sessionBeans;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import entities.SistemaConfiguracion;
import entities.Solicitud;

/**
 * Session Bean implementation class AdministrarSistemaBean
 */
@Stateless
@LocalBean
public class AdministrarSolicitudesBean implements AdministrarSolicitudes {

	/**
	 * Default constructor.
	 */
	@PersistenceContext
	EntityManager em;

	public AdministrarSolicitudesBean() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Solicitud getSolicitud(int idSolicitud) {
		try{
		Solicitud sa = em.find(Solicitud.class, idSolicitud);
		return sa;
		} catch (NoResultException e) {
			return null;
		}
	}

}
