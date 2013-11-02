package sessionBeans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.thoughtworks.xstream.XStream;

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
    public String procesarSolicitudDespacho(String xml){
    	//Aca hay que procesar el xml y hacer lo que haya que hacer.
    	XStream xstream = new XStream();
    	OrdenDespacho od = (OrdenDespacho)xstream.fromXML(xml);
    	return "";
    }

}
