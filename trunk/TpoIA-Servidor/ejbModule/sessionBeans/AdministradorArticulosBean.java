package sessionBeans;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entities.Articulo;

/**
 * Session Bean implementation class AdministradorArticulosBean
 */
@Stateless
public class AdministradorArticulosBean implements AdministradorArticulos {
	@PersistenceContext
	EntityManager em;
    /**
     * Default constructor. 
     */
    public AdministradorArticulosBean() {
        // TODO Auto-generated constructor stub
    }

	@Override
	public boolean createArticulo(Articulo art) {
		// TODO Auto-generated method stub
		try {
			em.persist(art);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return true;
		}
	}
    
}
