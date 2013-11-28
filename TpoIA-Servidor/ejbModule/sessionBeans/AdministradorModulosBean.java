package sessionBeans;

import java.util.ArrayList;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;

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

	Logger logger = Logger.getLogger(AdministradorModulosBean.class);

	public AdministradorModulosBean() {
	}

	public Modulo getModulo(int idModulo) {

		try {
			logger.info("Buscando modulo por id");
			Modulo mod = (Modulo) em.find(Modulo.class, idModulo);
			return mod;
		} catch (Exception e) {
			logger.error("Error al buscar modulo por id");
			e.printStackTrace();
			return null;
		}
	}

	public Modulo getModulo(String tipo) {
		logger.info("Buscando modulo por tipo");
		try {
			Query q = em
					.createQuery("from Modulo m where m.tipo = :tipodeposito");
			q.setParameter("tipodeposito", tipo);
			Modulo mod = (Modulo) q.getSingleResult();
			return mod;
		} catch (Exception e) {
			logger.error("Error buscando modulo por tipo");
			e.printStackTrace();
			return null;
		}
	}

	public boolean createModulo(String tipo, int idModulo, String ip,
			String nombre, String codigo) {

		try {
			logger.info("Creando modulo nuevo");
			Modulo mod = new Modulo();
			mod.setTipo(tipo);
			mod.setIdModulo(idModulo);
			mod.setIp(ip);
			mod.setNombre(nombre);
			mod.setCodigo(codigo);
			em.persist(mod);
			return true;
		} catch (Exception e) {
			logger.error("Error creando modulo");
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteModulo(int idModulo) {
		try {
			logger.info("Borrando modulo por id");
			Modulo mod = (Modulo) em.find(Modulo.class, idModulo);
			em.remove(mod);
			return true;
		} catch (Exception e) {
			logger.error("Error borrando modulo");
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateModulo(String tipo, int idModulo, String ip,
			String nombre, String codigo) {
		try {
			logger.info("Actualizando Modulo");
			Modulo mod = (Modulo) em.find(Modulo.class, idModulo);
			mod.setCodigo(codigo);
			mod.setIdModulo(idModulo);
			mod.setIp(ip);
			mod.setNombre(nombre);
			mod.setTipo(tipo);

			em.persist(mod);
			return true;
		} catch (Exception e) {
			logger.error("Error actualizando Modulo");
			e.printStackTrace();
			return false;
		}
	}

	public ArrayList<ModuloVO> getAllModulos() {
		logger.info("Obteniendo todos los modulos");
		try {
			ArrayList<Modulo> vecAux = null;
			ArrayList<ModuloVO> rslt = new ArrayList<ModuloVO>();
			Query q = em.createQuery("from Modulo m");
			vecAux = (ArrayList<Modulo>) q.getResultList();

			for (Modulo modulo : vecAux) {
				ModuloVO mod = new ModuloVO();
				mod.setCodigo(modulo.getCodigo());
				mod.setIdModulo(modulo.getIdModulo());
				mod.setIp(modulo.getIp());				
				mod.setNombre(modulo.getNombre());
				mod.setTipo(modulo.getTipo());
				rslt.add(mod);
			}
			return rslt;
		} catch (Exception e) {
			logger.error("Error obteniendo todos los modulos");
			e.printStackTrace();
			return null;
		}
	}

}
