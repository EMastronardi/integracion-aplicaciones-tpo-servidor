package sessionBeans;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;

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
	Logger logger = Logger.getLogger(AdministradorSolicitudesBean.class);

	public AdministradorSolicitudesBean() {
	}

	public ArrayList<SolicitudVO> getAllSolicitudes() {
		try {
			logger.info("Obteniendo todas las solicitudesVO");
			Query q = em.createQuery("from Solicitud");
			ArrayList<Solicitud> solicitudes = (ArrayList<Solicitud>) q
					.getResultList();
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

					// No se MAPEA modulos para los articulos!!!
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
		} catch (Exception e) {
			logger.info("Error obteniendo todas las solicitudesVO");
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public SolicitudVO getSolicitudById(int idsolicitud) {

		try {
			logger.info("Obteniendo solicitudVO por id");
			Solicitud sol = em.find(Solicitud.class, idsolicitud);
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
		} catch (Exception e) {
			logger.error("Error obteniendo solicitudVO por id");
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<SolicitudVO> searchSolicitudes(String filtro, int valor) {

		try {
			logger.info("Obteniendo listado de SolicitudesVO");
			Query q = em
					.createQuery("select s from Solicitud s where s.id =:value");
			q.setParameter("value", valor);
			ArrayList<Solicitud> solicitudes = (ArrayList<Solicitud>) q
					.getResultList();
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
					depovo.setIdModulo(itm.getArticulo().getModulo()
							.getIdModulo());
					depovo.setNombre(itm.getArticulo().getModulo().getNombre());
					artvo.setDeposito(depovo);
					itmvo.setArticulo(artvo);
					listitm.add(itmvo);
				}
				solvo.setItems(listitm);
				rslt.add(solvo);
			}
			return rslt;
		} catch (Exception e) {
			logger.error("Error obteniend listado de SolicitudesVO");
			e.printStackTrace();
			return null;
		}
	}

	public Solicitud getSolicitud(int idSolicitud) {
		try {
			logger.info("Obteniendo solicitud por id");
			Solicitud sa = em.find(Solicitud.class, idSolicitud);
			return sa;
		} catch (NoResultException e) {
			logger.error("Error al obtener solicitud por id");
			return null;
		}
	}

}
