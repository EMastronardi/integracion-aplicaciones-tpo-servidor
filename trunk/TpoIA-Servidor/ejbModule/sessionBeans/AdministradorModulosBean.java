package sessionBeans;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entities.Modulo;

/**
 * Session Bean implementation class AdministradorModulosBean
 */
@Stateless
@LocalBean
public class AdministradorModulosBean implements AdministradorModulos {
	@PersistenceContext
	EntityManager em;
    /**
     * Default constructor. 
     */
    public AdministradorModulosBean() {
        // TODO Auto-generated constructor stub
    }

	@Override
	public Modulo getModulo(int idModulo) {
		// TODO Auto-generated method stub
		Modulo mod = (Modulo) em.find(Modulo.class, idModulo);
		return mod;
	}

}
