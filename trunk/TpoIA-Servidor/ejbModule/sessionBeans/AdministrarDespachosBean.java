package sessionBeans;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import xml.OrdenDespachoXML;
import xml.itemXML;

import com.thoughtworks.xstream.XStream;

import entities.Articulo;
import entities.ItemOrdenDespacho;
import entities.OrdenDespacho;


/**
 * Session Bean implementation class Despacho
 */
@Stateless
public class AdministrarDespachosBean implements AdministrarDespachos {

	@PersistenceContext
	private EntityManager em;

	/**
	 * Default constructor.
	 */
	public AdministrarDespachosBean() {
		// TODO Auto-generated constructor stub
	}

	public String procesarSolicitudDespacho(String valorXml) {
		// Aca hay que procesar el xml y hacer lo que haya que hacer.
		
		XStream xstream = new XStream();
		OrdenDespachoXML odXml = (OrdenDespachoXML)xstream.fromXML(valorXml); //tengo el objeto puro
		OrdenDespacho od = new OrdenDespacho();
		od.setNroDespacho(odXml.getNroDespacho());
		od.setNroVenta(odXml.getNroVenta());
		od.setFecha(odXml.getFecha());
		od.setEstado("Pendiente");
		
		//Armar ItemsOrdenDespacho
		List<itemXML> itmsXml  = odXml.getItems();
		
		ArrayList<ItemOrdenDespacho> itemsDespacho = new ArrayList<ItemOrdenDespacho>();
		for (itemXML itm : itmsXml) {
						
			Articulo art = (Articulo)em.find(Articulo.class, itm.getCodigoArticulo());
			if(art != null){
				ItemOrdenDespacho iod = new ItemOrdenDespacho();
				iod.setArticulo(art);
				iod.setCantidad(itm.getCantidad());
				//iod.setSolicitudArticulo(); para que es este solicitud articulo??
				itemsDespacho.add(iod);
				String resultado = SolicitarADeposito(art,itm.getCantidad());
			}
		}
		
		od.setItemsDespacho(itemsDespacho);
		
		em.persist(od);
		System.out.println("idModulo: " +odXml.getIdModulo());
		//OrdenDespacho od = (OrdenDespacho) xstream.fromXML(xml);
		
		return "";
	}

	//Aca hay que mandar al deposito correspondiente al Articulo el pedido del mismo, con su cantidad
	private String SolicitarADeposito(Articulo art, int cantidad) {
		return "";
		
	}

}
