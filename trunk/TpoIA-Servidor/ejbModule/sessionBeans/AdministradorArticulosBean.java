package sessionBeans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entities.Articulo;
import entities.Deposito;

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
	public boolean createArticulo(int codigo, String nombre, int idModulo) {
		// TODO Auto-generated method stub
		try {
			Deposito dep = em.find(Deposito.class, idModulo);
			Articulo art = new Articulo();
			art.setDeposito(dep);
			art.setNombre(nombre);
			art.setNroArticulo(codigo);
			em.persist(art);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return true;
		}
	}
    
}
