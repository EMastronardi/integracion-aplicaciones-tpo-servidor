package mensajeria;

import integration.FacadeRemote;
import integration.FacadeRemoteBean;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.jboss.logging.Logger;

import sessionBeans.AdministradorArticulosBean;
import sessionBeans.AdministradorModulos;
import xml.ArticuloXML;

import com.thoughtworks.xstream.XStream;

import entities.Articulo;
import entities.Modulo;

/**
 * Message-Driven Bean implementation class for: ArtNuevoMsg
 */
@MessageDriven(

activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/nuevosArticulos") }, mappedName = "queue/nuevosArticulos")
public class ArtNuevoMsg implements MessageListener {
	/**
	 * Default constructor.
	 */
	@EJB
	private FacadeRemote fachada;

	@EJB
	private AdministradorModulos adminMod;
	public ArtNuevoMsg() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see MessageListener#onMessage(Message)
	 */
	public void onMessage(Message message) {
        // TODO Auto-generated method stub
    	TextMessage textmessage = (TextMessage) message;
    	String str;
    	Logger logger = Logger.getLogger(ArtNuevoMsg.class);

    	
    	try {
			str = textmessage.getText();
			logger.info("Reciviendo nuevo Articulo.");
			logger.info("XML: "+str);
			XStream xstream = new XStream();
			xstream.ignoreUnknownElements();
			xstream.alias("articulo", ArticuloXML.class);
			ArticuloXML artXml = (ArticuloXML)xstream.fromXML(str);
			fachada.addArticulo(artXml.getCodigo(),artXml.getNombre(), artXml.getIdModulo());
		} catch (JMSException e) {
			logger.error("Error en nuevo articulo.");
			e.printStackTrace();
		}
    	
    }
}
