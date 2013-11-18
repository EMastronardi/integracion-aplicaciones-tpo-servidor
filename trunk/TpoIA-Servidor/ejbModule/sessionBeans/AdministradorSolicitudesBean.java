package sessionBeans;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import valueObjects.ArticuloVO;
import valueObjects.ItemSolicitudVO;
import valueObjects.ModuloVO;
import valueObjects.OrdenDespachoVO;
import valueObjects.SolicitudVO;
import entities.ItemSolicitud;
import entities.OrdenDespacho;
import entities.Solicitud;

/**
 * Session Bean implementation class AdministradorSolicitudesBean
 */
@Stateless
@LocalBean
public class AdministradorSolicitudesBean implements AdministradorSolicitudes {

	@PersistenceContext
	private EntityManager em;
    /**
     * Default constructor. 
     */
    public AdministradorSolicitudesBean() {
        // TODO Auto-generated constructor stub
    }

	@Override
	public ArrayList<SolicitudVO> getAllSolicitudes() {
		// TODO Auto-generated method stub
		Query q = em.createQuery("from Solicitud");
		ArrayList<Solicitud> solicitudes = (ArrayList<Solicitud>) q.getResultList();
		ArrayList<SolicitudVO> rslt = new ArrayList<SolicitudVO>();
		for (Solicitud sol : solicitudes) {
			SolicitudVO solvo = new SolicitudVO();
			solvo.setIdSolicitud(sol.getIdSolicitud());
			List<ItemSolicitudVO> listitm = new ArrayList<ItemSolicitudVO>();
			for (ItemSolicitud itm : sol.getItems()) {
				ItemSolicitudVO itmvo = new ItemSolicitudVO();
				itmvo.setCantidad(itm.getCantidad());
				itmvo.setCantidadRecibida(itm.getCantidadRecibida());
				ArticuloVO artvo = new ArticuloVO();
				artvo.setNombre(itm.getArticulo().getNombre());
				artvo.setNroArticulo(itm.getArticulo().getNroArticulo());
				
				//No se MAPEA modulos para los articulos!!!
				ModuloVO depovo = new ModuloVO();
				depovo.setCodigo(itm.getArticulo().getModulo().getCodigo());
				depovo.setIdModulo(itm.getArticulo().getModulo().getIdModulo());
				depovo.setNombre(itm.getArticulo().getModulo().getNombre());
				artvo.setDeposito(depovo);
				itmvo.setArticulo(artvo);
				listitm.add(itmvo);
			}
			solvo.setItems(listitm);
			rslt.add(solvo);
		}
		return rslt;
	}

	@Override
	public SolicitudVO getSolicitudById(int idsolicitud) {
		// TODO Auto-generated method stub
		Query q = em.createQuery("from Solicitud s where s.id=:value");
		q.setParameter("value",idsolicitud);
		
		Solicitud sol = (Solicitud) q.getSingleResult();
		SolicitudVO rslt = new SolicitudVO();
	
		rslt.setIdSolicitud(sol.getIdSolicitud());
		List<ItemSolicitudVO> listitm = new ArrayList<ItemSolicitudVO>();
		for (ItemSolicitud itm : sol.getItems()) {
			ItemSolicitudVO itmvo = new ItemSolicitudVO();
			itmvo.setCantidad(itm.getCantidad());
			itmvo.setCantidadRecibida(itm.getCantidadRecibida());
			ArticuloVO artvo = new ArticuloVO();
			artvo.setNombre(itm.getArticulo().getNombre());
			artvo.setNroArticulo(itm.getArticulo().getNroArticulo());
			ModuloVO depovo = new ModuloVO();
			depovo.setCodigo(itm.getArticulo().getModulo().getCodigo());
			depovo.setIdModulo(itm.getArticulo().getModulo().getIdModulo());
			depovo.setNombre(itm.getArticulo().getModulo().getNombre());
			artvo.setDeposito(depovo);
			itmvo.setArticulo(artvo);
			listitm.add(itmvo);
		}
		rslt.setItems(listitm);
		return rslt;
	}

	@Override
	public ArrayList<SolicitudVO> searchSolicitudes(String filtro, int valor) {
		// TODO Auto-generated method stub
		Query q = em.createQuery("from Solicitud where id =:vlue");
		q.setParameter("value",valor);
		ArrayList<Solicitud> solicitudes = (ArrayList<Solicitud>) q.getResultList();
		ArrayList<SolicitudVO> rslt = new ArrayList<SolicitudVO>();
		for (Solicitud sol : solicitudes) {
			SolicitudVO solvo = new SolicitudVO();
			solvo.setIdSolicitud(sol.getIdSolicitud());
			List<ItemSolicitudVO> listitm = new ArrayList<ItemSolicitudVO>();
			for (ItemSolicitud itm : sol.getItems()) {
				ItemSolicitudVO itmvo = new ItemSolicitudVO();
				itmvo.setCantidad(itm.getCantidad());
				itmvo.setCantidadRecibida(itm.getCantidadRecibida());
				ArticuloVO artvo = new ArticuloVO();
				artvo.setNombre(itm.getArticulo().getNombre());
				artvo.setNroArticulo(itm.getArticulo().getNroArticulo());
				ModuloVO depovo = new ModuloVO();
				depovo.setCodigo(itm.getArticulo().getModulo().getCodigo());
				depovo.setIdModulo(itm.getArticulo().getModulo().getIdModulo());
				depovo.setNombre(itm.getArticulo().getModulo().getNombre());
				artvo.setDeposito(depovo);
				itmvo.setArticulo(artvo);
				listitm.add(itmvo);
			}
			solvo.setItems(listitm);
			rslt.add(solvo);
		}
		return rslt;
	}

}
