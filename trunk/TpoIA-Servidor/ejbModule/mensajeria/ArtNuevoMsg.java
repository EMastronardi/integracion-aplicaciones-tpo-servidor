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
    	
    	try {
			str = textmessage.getText();
			XStream xstream = new XStream(); 
			xstream.alias("articulo", ArticuloXML.class);
			ArticuloXML artXml = (ArticuloXML)xstream.fromXML(str);
			fachada.addArticulo(artXml.getCodigo(),artXml.getNombre(), artXml.getIdModulo());
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}
