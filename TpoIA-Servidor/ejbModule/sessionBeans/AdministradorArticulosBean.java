package sessionBeans;

import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import valueObjects.ArticuloVO;
import valueObjects.ModuloVO;
import entities.Articulo;
import entities.Modulo;

/**
 * Session Bean implementation class AdministradorArticulosBean
 */
@Stateless
public class AdministradorArticulosBean implements AdministradorArticulos {
	@PersistenceContext
	EntityManager em;
	Logger logger = Logger.getLogger(AdministradorArticulosBean.class);

	public AdministradorArticulosBean() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean createArticulo(int codigo, String nombre, int idModulo) {
		logger.info("Ingreso de nuevo articulo");
		try {
			Modulo dep = em.find(Modulo.class, idModulo);
			Articulo art = new Articulo();
			art.setDeposito(dep);
			art.setNombre(nombre);
			art.setNroArticulo(codigo);
			em.persist(art);
			return true;
		} catch (Exception e) {
			logger.error("Error creando articulo nuevo");
			e.printStackTrace();
			return true;
		}
	}

	@Override
	public ArrayList<ArticuloVO> getAllArticulos() {

		logger.info("Ingreso al metodo getAllArticulos");
		try {
			Query q = em.createQuery("from Articulo");
			ArrayList<Articulo> vecArt = (ArrayList<Articulo>) q
					.getResultList();
			ArrayList<ArticuloVO> rslt = new ArrayList<ArticuloVO>();

			for (Articulo articulo : vecArt) {
				ArticuloVO artVO = new ArticuloVO();
				ModuloVO mod = new ModuloVO();
				mod.setCodigo(articulo.getModulo().getCodigo());
				mod.setIdModulo(articulo.getModulo().getIdModulo());
				mod.setIp(articulo.getModulo().getIp());
				mod.setJmsDestination(articulo.getModulo().getJmsDestination());
				mod.setNombre(articulo.getModulo().getNombre());
				mod.setPassword(articulo.getModulo().getPassword());
				mod.setTipo(articulo.getModulo().getTipo());
				mod.setUsuario(articulo.getModulo().getUsuario());
				artVO.setDeposito(mod);
				artVO.setNombre(articulo.getNombre());
				artVO.setNroArticulo(articulo.getNroArticulo());
				rslt.add(artVO);
			}
			return rslt;

		} catch (Exception e) {
			logger.info("Error obteniendo todos los articulos");
			return null;
		}
	}

	public ArrayList<ArticuloVO> searchBy(String filtro, int value) {
		logger.info("Buscando articulo por valor");
		try {
			Query q = null;
			if (filtro.equals("codigo")) {
				q = em.createQuery("select a from Articulo a where a.nroArticulo=:value");
			} else {
				q = em.createQuery("select a from Articulo a, Modulo m where m = a.deposito AND m.idModulo=:value");
			}
			q.setParameter("value", value);
			ArrayList<Articulo> vecArt = (ArrayList<Articulo>) q
					.getResultList();
			ArrayList<ArticuloVO> rslt = new ArrayList<ArticuloVO>();

			for (Articulo articulo : vecArt) {
				ArticuloVO artVO = new ArticuloVO();
				ModuloVO mod = new ModuloVO();
				mod.setCodigo(articulo.getModulo().getCodigo());
				mod.setIdModulo(articulo.getModulo().getIdModulo());
				mod.setIp(articulo.getModulo().getIp());
				mod.setJmsDestination(articulo.getModulo().getJmsDestination());
				mod.setNombre(articulo.getModulo().getNombre());
				mod.setPassword(articulo.getModulo().getPassword());
				mod.setTipo(articulo.getModulo().getTipo());
				mod.setUsuario(articulo.getModulo().getUsuario());
				artVO.setDeposito(mod);
				artVO.setNombre(articulo.getNombre());
				artVO.setNroArticulo(articulo.getNroArticulo());
				rslt.add(artVO);
			}
			return rslt;
		} catch (Exception e) {
			logger.error("Error buscando articulo");
			return null;
		}

	}
}
