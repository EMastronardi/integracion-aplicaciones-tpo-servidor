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

import utils.Constantes;
import xml.ItemXML;
import xml.OrdenDespachoXML;
import xml.SolicitudXML;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;

import entities.Articulo;
import entities.Deposito;
import entities.ItemSolicitud;
import entities.Modulo;
import entities.OrdenDespacho;
import entities.Solicitud;

/**
 * Session Bean implementation class Despacho
 */
@Stateless
public class AdministrarDespachosBean implements AdministrarDespachos {

	@PersistenceContext
	private EntityManager em;

	@EJB(beanName = "AdministrarSistemaBean")
	private AdministrarSistema as;

	public AdministrarDespachosBean() {
		// TODO Auto-generated constructor stub
	}

	public String procesarSolicitudDespacho(String valorXml) {
		// Aca hay que procesar el xml y hacer lo que haya que hacer.

		XStream xstream = new XStream();
		xstream.alias("despacho", OrdenDespachoXML.class);
		xstream.alias("item", ItemXML.class);
		String[] formats = { "yyyy-MM-dd HH:mm" };
		xstream.registerConverter(new DateConverter("yyyy-MM-dd HH:mm", formats));

		OrdenDespachoXML odXml = (OrdenDespachoXML) xstream.fromXML(valorXml);
		OrdenDespacho od = new OrdenDespacho();
		od.setNroOrdenDespacho(odXml.getNroDespacho());
		od.setNroVenta(odXml.getNroVenta());
		od.setFecha(odXml.getFecha());
		od.setEstado("Pendiente");

		// Armar Solicitudes y ItemsOrdenDespacho
		List<ItemXML> itmsXml = odXml.getItems();

		// la hash table tienen <idDeposito, Solicitud>
		Hashtable<Integer, Solicitud> tabla = new Hashtable<Integer, Solicitud>();
		for (ItemXML itm : itmsXml) {

			Articulo art = (Articulo) em.find(Articulo.class,
					Integer.parseInt(itm.getCodigo()));
			if (art != null) {// TODO: que hacer cuando es null???
				// Ya esta el iddeposito de ese articulo en la tabla??? //

				ItemSolicitud iSol = new ItemSolicitud();
				iSol.setArticulo(art);
				iSol.setCantidad(itm.getCantidad());
				iSol.setCantidadRecibida(0);

				if (tabla.containsKey(art.getDeposito().getIdModulo())) {
					// agrego el art a esa solicitd
					Solicitud sol = tabla.get(art.getDeposito().getIdModulo());
					sol.AddArticulo(iSol);

				} else {// Creo una nueva solicitud y la agrego a la hashtable
					Solicitud sol = new Solicitud();
					sol.setDeposito(art.getDeposito());
					sol.AddArticulo(iSol);

					// grabo la nueva sol en la tabla
					tabla.put(art.getDeposito().getIdModulo(), sol);
				}
			} else {
				System.out.println("El articulo nro: " + itm.getCodigo()
						+ " no existe.");
			}
		}

		if (!tabla.isEmpty()) {
			ArrayList<Solicitud> solicitudes = new ArrayList<Solicitud>();
			Enumeration<Solicitud> enumeration = tabla.elements();
			while (enumeration.hasMoreElements()) {
				solicitudes.add(enumeration.nextElement());
				// enviarADeposito(itemsDespacho, od.getNroOrdenDespacho());

			}

			od.setSolicitudes(solicitudes);
			em.persist(od);
		}else{
			System.out.println("No se genera la OrdenDespacho, ningun Articulo existente");
		}

		
		return "";

	}

	// Aca hay que mandar al deposito correspondiente al Articulo el pedido del
	// mismo, con su cantidad

	private void enviarPedido(SolicitudXML sol) {

		// a que deposito

		String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
		String DEFAULT_DESTINATION = "queue/procesarOrdenDespacho";// "jms/queue/test";
		String DEFAULT_USERNAME = "prod";// art.getDeposito().getUsuario();//"test";
		String DEFAULT_PASSWORD = "prod1234";// art.getDeposito().getPassword();//"test123";
		String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
		String PROVIDER_URL = "remote://" + obtenerIPModulo(sol.getIdModulo())
				+ ":4447";

		ConnectionFactory connectionFactory = null;
		Connection connection = null;
		Session session = null;
		MessageProducer producer = null;
		Destination destination = null;
		Context context = null;

		// Set up the context for the JNDI lookup
		final Properties env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
		env.put(Context.PROVIDER_URL,
				System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));
		env.put(Context.SECURITY_PRINCIPAL,
				System.getProperty("username", DEFAULT_USERNAME));
		env.put(Context.SECURITY_CREDENTIALS,
				System.getProperty("password", DEFAULT_PASSWORD));
		try {
			context = new InitialContext(env);

			// Perform the JNDI lookups
			String connectionFactoryString = System.getProperty(
					"connection.factory", DEFAULT_CONNECTION_FACTORY);
			connectionFactory = (ConnectionFactory) context
					.lookup(connectionFactoryString);

			String destinationString = System.getProperty("destination",
					DEFAULT_DESTINATION);
			destination = (Destination) context.lookup(destinationString);

			// Create the JMS connection, session, producer, and consumer

			connection = connectionFactory.createConnection(
					System.getProperty("username", DEFAULT_USERNAME),
					System.getProperty("password", DEFAULT_PASSWORD));

			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			producer = session.createProducer(destination);
			connection.start();
			// crear un mensaje de tipo text y setearle el contenido
			TextMessage message = session.createTextMessage();
			// creo el xml
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
		Modulo m = (Modulo) em.find(Modulo.class, idModulo);
		if (m != null && m.getIp() != null && !m.getIp().isEmpty())
			return m.getIp();
		return "";
	}

	private String SolicitarADeposito(Articulo art, int cantidad,
			int nroDespacho) throws NamingException, JMSException {

		// a que deposito

		String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
		String DEFAULT_DESTINATION = "queue/procesarOrdenDespacho";// "jms/queue/test";
		String DEFAULT_USERNAME = "prod";// art.getDeposito().getUsuario();//"test";
		String DEFAULT_PASSWORD = "prod1234";// art.getDeposito().getPassword();//"test123";
		String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
		String PROVIDER_URL = "remote://" + art.getDeposito().getIp() + ":4447";

		ConnectionFactory connectionFactory = null;
		Connection connection = null;
		Session session = null;
		MessageProducer producer = null;
		Destination destination = null;
		Context context = null;

		// Set up the context for the JNDI lookup
		final Properties env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
		env.put(Context.PROVIDER_URL,
				System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));
		env.put(Context.SECURITY_PRINCIPAL,
				System.getProperty("username", DEFAULT_USERNAME));
		env.put(Context.SECURITY_CREDENTIALS,
				System.getProperty("password", DEFAULT_PASSWORD));
		context = new InitialContext(env);

		// Perform the JNDI lookups
		String connectionFactoryString = System.getProperty(
				"connection.factory", DEFAULT_CONNECTION_FACTORY);
		connectionFactory = (ConnectionFactory) context
				.lookup(connectionFactoryString);

		String destinationString = System.getProperty("destination",
				DEFAULT_DESTINATION);
		destination = (Destination) context.lookup(destinationString);

		// Create the JMS connection, session, producer, and consumer
		connection = connectionFactory.createConnection(
				System.getProperty("username", DEFAULT_USERNAME),
				System.getProperty("password", DEFAULT_PASSWORD));
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
		// foreach()
		sxml.setArticulos(articulos);

		// message.setText(xml);
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
