package sessionBeans;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import entities.SistemaConfiguracion;

/**
 * Session Bean implementation class AdministrarSistemaBean
 */
@Stateless
@LocalBean
public class AdministrarSistemaBean implements AdministrarSistema {

    /**
     * Default constructor. 
     */
	@PersistenceContext
	EntityManager em;
    public AdministrarSistemaBean() {
        // TODO Auto-generated constructor stub
    }

	@Override
	public String getModuloId() {
		Query q = em.createQuery("Select top 1 * from SistemaConfiguracion");
		SistemaConfiguracion sist =   (SistemaConfiguracion)q.getSingleResult();
		if(sist != null)
			return String.valueOf(sist.getModuloId());
		else
			return "id no encontrado";
	}

	public String getModuloIp() {
		// TODO Auto-generated method stub
		return "";
	}

}
