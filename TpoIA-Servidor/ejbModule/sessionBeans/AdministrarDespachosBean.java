package sessionBeans;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
				try {
					String resultado = SolicitarADeposito(art,itm.getCantidad());
				} catch (NamingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		od.setItemsDespacho(itemsDespacho);
		
		em.persist(od);
		System.out.println("idModulo: " +odXml.getIdModulo());
		//OrdenDespacho od = (OrdenDespacho) xstream.fromXML(xml);
		
		return "";
	}

	//Aca hay que mandar al deposito correspondiente al Articulo el pedido del mismo, con su cantidad
	private String SolicitarADeposito(Articulo art, int cantidad) throws NamingException, JMSException {
		
		//a que deposito
		
		String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
		String DEFAULT_DESTINATION = art.getDeposito().getJmsDestination();//"jms/queue/test";
		String DEFAULT_USERNAME = art.getDeposito().getUsuario();//"test";
		String DEFAULT_PASSWORD = art.getDeposito().getPassword();//"test123";
		String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
		String PROVIDER_URL = "remote://"+art.getDeposito().getIp()+":4447";
		
		
		ConnectionFactory connectionFactory = null;
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        Destination destination = null;
        Context context = null;

        // Set up the context for the JNDI lookup
        final Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
        env.put(Context.PROVIDER_URL, System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));
        env.put(Context.SECURITY_PRINCIPAL, System.getProperty("username", DEFAULT_USERNAME));
        env.put(Context.SECURITY_CREDENTIALS, System.getProperty("password", DEFAULT_PASSWORD));
        context = new InitialContext(env);

        // Perform the JNDI lookups
        String connectionFactoryString = System.getProperty("connection.factory", DEFAULT_CONNECTION_FACTORY);
        connectionFactory = (ConnectionFactory) context.lookup(connectionFactoryString);

        String destinationString = System.getProperty("destination", DEFAULT_DESTINATION);
        destination = (Destination) context.lookup(destinationString);

        // Create the JMS connection, session, producer, and consumer
        connection = connectionFactory.createConnection(System.getProperty("username", DEFAULT_USERNAME), System.getProperty("password", DEFAULT_PASSWORD));
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        producer = session.createProducer(destination);
        connection.start();
		// crear un mensaje de tipo text y setearle el contenido
		TextMessage message = session.createTextMessage();
		message.setText("Hola Mundoooooooo");
		// enviar el mensaje
		producer.send(message);
		connection.close();

		return "";

	}

}
