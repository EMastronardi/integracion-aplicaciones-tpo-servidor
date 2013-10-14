package sessionBeans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Session Bean implementation class Despacho
 */
@Stateless
public class Despacho implements DespachoRemote {

	@PersistenceContext
	private EntityManager em;
	/**
	 * Default constructor.
	 */
    public Despacho() {
        // TODO Auto-generated constructor stub
    }

}
