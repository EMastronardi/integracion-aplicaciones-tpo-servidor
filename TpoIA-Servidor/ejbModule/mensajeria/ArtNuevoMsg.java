package mensajeria;

import integration.FacadeRemoteBean;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import xml.ArticuloXML;

import com.thoughtworks.xstream.XStream;

import entities.Articulo;
import entities.Deposito;

/**
 * Message-Driven Bean implementation class for: ArtNuevoMsg
 */
@MessageDriven(

		activationConfig = { @ActivationConfigProperty(
				propertyName = "destinationType", propertyValue = "javax.jms.Queue"), 
					@ActivationConfigProperty(
				propertyName = "destination", propertyValue = "queue/nuevosArticulos") }, 
				mappedName = "queue/nuevosArticulos")
public class ArtNuevoMsg implements MessageListener {
    /**
     * Default constructor. 
     */
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
			ArticuloXML artXml = (ArticuloXML)xstream.fromXML(str);
			Articulo art = new Articulo();
			FacadeRemoteBean fachada = new FacadeRemoteBean();
			art.setDeposito((Deposito) fachada.getModulo(artXml.getIdModulo()));
			art.setNombre(artXml.getNombre());
			art.setNroArticulo(artXml.getCodigo());
			fachada.addArticulo(art);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}
