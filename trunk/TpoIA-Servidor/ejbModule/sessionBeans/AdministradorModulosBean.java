package sessionBeans;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import entities.Deposito;
import entities.Logistica;
import entities.Modulo;
import entities.PortalWeb;

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

	@Override
	public boolean createModulo(String tipo, int idModulo, String ip, String nombre, String codigo, String usuario, String password, String jmsDestination) {
		// TODO Auto-generated method stub
		
		try {
			if(tipo.equals("deposito")){
				Deposito mod = new Deposito(idModulo, ip, nombre, codigo, usuario, password, jmsDestination);
				em.persist(mod);
			}else if(tipo.equals("logistica")){
				Logistica mod = new Logistica(idModulo, ip, nombre, codigo, usuario, password, jmsDestination);
				em.persist(mod);
			}else{
				PortalWeb mod = new PortalWeb(idModulo, ip, nombre, codigo, usuario, password, jmsDestination);
				em.persist(mod);
			}
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
	public boolean updateModulo(Modulo modulo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Deposito getDeposito(int idDeposito) {
		// TODO Auto-generated method stub
		Deposito depo = null;
		try {
			depo = em.find(Deposito.class, idDeposito);
			//Query q = em.createQuery("from Modulo m where m.idModulo = :deposito");
			//q.setParameter("deposito",idDeposito);
			//depo = (Deposito)q.getSingleResult();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return depo;
	}

	@Override
	public String getCadena(int idModulo) {
		Deposito depo = em.find(Deposito.class, idModulo);
		if(depo != null)
			return "getCodigo:"+depo.getCodigo()+"getIP"+ depo.getIp();
		return "NADAAA";
	}

}