package sessionBeans;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.ejb.EJB;
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

import xml.ItemXML;
import xml.OrdenDespachoXML;
import xml.SolicitudXML;

import com.thoughtworks.xstream.XStream;

import entities.Articulo;
import entities.Deposito;
import entities.ItemOrdenDespacho;
import entities.Modulo;
import entities.OrdenDespacho;


/**
 * Session Bean implementation class Despacho
 */
@Stateless
public class AdministrarDespachosBean implements AdministrarDespachos {

	@PersistenceContext
	private EntityManager em;

	@EJB(beanName="AdministrarSistemaBean")
	private AdministrarSistema as;
	public AdministrarDespachosBean() {
		// TODO Auto-generated constructor stub
	}

	public String procesarSolicitudDespacho(String valorXml) {
		// Aca hay que procesar el xml y hacer lo que haya que hacer.
		
		XStream xstream = new XStream();
		OrdenDespachoXML odXml = (OrdenDespachoXML)xstream.fromXML(valorXml); //tengo el objeto puro
		OrdenDespacho od = new OrdenDespacho();
		od.setNroOrdenDespacho(odXml.getNroDespacho());
		od.setNroVenta(odXml.getNroVenta());
		od.setFecha(odXml.getFecha());
		od.setEstado("Pendiente");
		
		
		//Armar ItemsOrdenDespacho
		List<ItemXML> itmsXml  = odXml.getItems();
		
		ArrayList<ItemOrdenDespacho> itemsDespacho = new ArrayList<ItemOrdenDespacho>();
		for (ItemXML itm : itmsXml) {
						
			Articulo art = (Articulo)em.find(Articulo.class, itm.getCodigo());
			if(art != null){
				ItemOrdenDespacho iod = new ItemOrdenDespacho();
				iod.setArticulo(art);
				iod.setCantidad(itm.getCantidad());
				//iod.setSolicitudArticulo(); para que es este solicitud articulo??
				itemsDespacho.add(iod);
			}
		}
		enviarADeposito(itemsDespacho, od.getNroOrdenDespacho());
		od.setItemsDespacho(itemsDespacho);
		
		em.persist(od);
		System.out.println("idModulo: " +odXml.getIdModulo());
		//OrdenDespacho od = (OrdenDespacho) xstream.fromXML(xml);
		
		return "";
	}

	//Aca hay que mandar al deposito correspondiente al Articulo el pedido del mismo, con su cantidad
	private void enviarADeposito(ArrayList<ItemOrdenDespacho> itemsDespacho , int nroOrdenDespacho) {

		if(itemsDespacho != null){
			
			Hashtable<String, SolicitudXML> tabla = new Hashtable<String, SolicitudXML>();
			
			for (ItemOrdenDespacho itms : itemsDespacho) {
				if(tabla.containsKey(itms.getArticulo().getDeposito().getIdModulo())){
					//hay que agregarle un itemArticulo solamente
					SolicitudXML sol = tabla.get(itms.getArticulo().getDeposito().getIdModulo());
					sol.addArticulo(itms.getArticulo().getNroArticulo(),itms.getCantidad());

				}else{
					SolicitudXML sol = new SolicitudXML();
					sol.setIdModulo(as.getModuloId());
					sol.setIdSolicitud(String.valueOf(nroOrdenDespacho));
					sol.addArticulo(itms.getArticulo().getNroArticulo(),itms.getCantidad());
					tabla.put(sol.getIdModulo(), sol);
					}
			}
			
			//recorrer hashtable y enviar a cada deposito lo correspondiente
			Enumeration<SolicitudXML> enumeration = tabla.elements();
			while(enumeration.hasMoreElements()){
				enviarPedido(enumeration.nextElement());
			}


		}
		
	}

	private void enviarPedido(SolicitudXML sol) {
		
	//a que deposito
		
		String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
		String DEFAULT_DESTINATION = "queue/procesarOrdenDespacho";//"jms/queue/test";
		String DEFAULT_USERNAME = "prod";//art.getDeposito().getUsuario();//"test";
		String DEFAULT_PASSWORD = "prod1234";//art.getDeposito().getPassword();//"test123";
		String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
		String PROVIDER_URL = "remote://"+obtenerIPModulo(sol.getIdModulo())+":4447";
		
		
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
        try {
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
        //creo el xml
        XStream xstream = new XStream();

        xstream.alias("SolicitudXML", SolicitudXML.class);
        String xml = xstream.toXML(sol);
        
        message.setText(xml);

        producer.send(message);
		connection.close();
        } catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String obtenerIPModulo(String idModulo) {
		Modulo m = (Modulo)em.find(Modulo.class, idModulo);
		if(m != null && m.getIp()!= null && !m.getIp().isEmpty())
			return m.getIp();
		return "";
	}

	private String SolicitarADeposito(Articulo art, int cantidad, int nroDespacho) throws NamingException, JMSException {
		
		//a que deposito
		
		String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
		String DEFAULT_DESTINATION = "queue/procesarOrdenDespacho";//"jms/queue/test";
		String DEFAULT_USERNAME = "prod";//art.getDeposito().getUsuario();//"test";
		String DEFAULT_PASSWORD = "prod1234";//art.getDeposito().getPassword();//"test123";
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
		
        XStream xstream = new XStream();
        SolicitudXML sxml = new SolicitudXML();
        
        
        sxml.setIdModulo(as.getModuloId());
        sxml.setIdSolicitud(String.valueOf(nroDespacho));
        List<ItemXML> articulos = new ArrayList<ItemXML>();
        //foreach()
        sxml.setArticulos(articulos);
        
       //	message.setText(xml);
		// enviar el mensaje
		producer.send(message);
		connection.close();

		return "";

	}

	private String getIdModulo() {
		// TODO Auto-generated method stub
		return null;
	}

}
