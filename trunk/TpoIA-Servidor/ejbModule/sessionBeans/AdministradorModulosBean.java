package sessionBeans;

import java.util.ArrayList;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import valueObjects.ModuloVO;
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
	
	public Modulo getModulo(String tipo) {
		Query q = em.createQuery("from Modulo m where m.tipo = :tipodeposito");
		q.setParameter("tipodeposito",tipo);
		Modulo mod = (Modulo)q.getSingleResult();
		return mod;
	}

	@Override
	public boolean createModulo(String tipo, int idModulo, String ip, String nombre, String codigo, String usuario, String password, String jmsDestination, String restDestinationLogisticaCambioEstado) {
		// TODO Auto-generated method stub
		
		try {
			Modulo mod = new Modulo(idModulo, ip, nombre, codigo, usuario, password, jmsDestination, tipo, restDestinationLogisticaCambioEstado);
			em.persist(mod);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteModulo(int idModulo) {
		// TODO Auto-generated method stub
		try {
			Modulo mod = (Modulo) em.find(Modulo.class, idModulo);
			em.remove(mod);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateModulo(String tipo, int idModulo, String ip, String nombre,
			String codigo, String usuario, String password,
			String jmsDestination, String restDestinationLogisticaCambioEstado){
		// TODO Auto-generated method stub
				try {
					Modulo mod = (Modulo) em.find(Modulo.class, idModulo);
					mod.setCodigo(codigo);
					mod.setIdModulo(idModulo);
					mod.setIp(ip);
					mod.setJmsDestination(jmsDestination);
					mod.setNombre(nombre);
					mod.setUsuario(usuario);
					mod.setTipo(tipo);
					mod.setPassword(password);
					
					em.persist(mod);
					return true;
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					return false;
				}
	}

	@Override
	public ArrayList<ModuloVO> getAllModulos() {
		// TODO Auto-generated method stub
		ArrayList<Modulo> vecAux = null;
		ArrayList<ModuloVO> rslt = new ArrayList<ModuloVO>();
		Query q = em.createQuery("from Modulo m");
		vecAux = (ArrayList<Modulo>)q.getResultList();
		
		for (Modulo modulo : vecAux) {
			ModuloVO mod = new ModuloVO();
			mod.setCodigo(modulo.getCodigo());
			mod.setIdModulo(modulo.getIdModulo());
			mod.setIp(modulo.getIp());
			mod.setJmsDestination(modulo.getJmsDestination());
			mod.setPassword(modulo.getPassword());
			mod.setUsuario(modulo.getUsuario());
			mod.setNombre(modulo.getNombre());
			mod.setTipo(modulo.getTipo());
			mod.setRestDestinationLogisticaCambioEstado(modulo.getRestDestinationLogisticaCambioEstado());
			rslt.add(mod);			
		}
		return rslt;
	}

	@Override
	public Modulo getDeposito(int idDeposito) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCadena(int idModulo) {
		// TODO Auto-generated method stub
		return null;
	}

}
