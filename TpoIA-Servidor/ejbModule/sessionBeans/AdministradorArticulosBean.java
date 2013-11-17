package sessionBeans;

import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
			Modulo dep = em.find(Modulo.class, idModulo);
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

	@Override
	public ArrayList<ArticuloVO> getAllArticulos() {
		// TODO Auto-generated method stub
		Query q = em.createQuery("from Articulo");
		ArrayList<Articulo> vecArt = (ArrayList<Articulo>) q.getResultList();
		ArrayList<ArticuloVO> rslt = new ArrayList<ArticuloVO> ();
		
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
	}

	@Override
	public ArrayList<ArticuloVO> searchBy(String filtro, int value) {
		// TODO Auto-generated method stub
		Query q =  null;
		if(filtro.equals("codigo")){
			q = em.createQuery("select a from Articulo a where a.nroArticulo=:value");
		}else{
			q = em.createQuery("select a from Articulo a, Modulo m where m = a.deposito AND m.idModulo=:value");
		}
		q.setParameter("value", value);
		ArrayList<Articulo> vecArt = (ArrayList<Articulo>) q.getResultList();
		ArrayList<ArticuloVO> rslt = new ArrayList<ArticuloVO> ();
		
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
	}
    
}
