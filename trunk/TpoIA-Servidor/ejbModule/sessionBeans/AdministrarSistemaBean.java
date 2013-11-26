package sessionBeans;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import entities.SistemaConfiguracion;

/**
 * Session Bean implementation class AdministrarSistemaBean
 */
@Stateless
@LocalBean
public class AdministrarSistemaBean implements AdministrarSistema {

	@PersistenceContext
	EntityManager em;
	Logger logger = Logger.getLogger(AdministrarSistemaBean.class);

	public AdministrarSistemaBean() {
	}

	@Override
	public String getModuloId() {
		logger.info("Ingresando a GetmoduloID");
		Query q = em.createQuery("Select top 1 * from SistemaConfiguracion");
		SistemaConfiguracion sist = (SistemaConfiguracion) q.getSingleResult();
		if (sist != null)
			return String.valueOf(sist.getModuloId());
		else
			logger.warn("getModuloId - Id no encontrado");
		return "id no encontrado";
	}

}
