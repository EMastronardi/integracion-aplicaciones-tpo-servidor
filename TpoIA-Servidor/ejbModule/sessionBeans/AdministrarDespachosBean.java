package sessionBeans;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javax.persistence.Query;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;
import net.sf.json.JSONObject;

//import org.hornetq.utils.json.JSONArray;
//import org.hornetq.utils.json.JSONException;
//import org.hornetq.utils.json.JSONObject;

import servicesCaller.notificarEntregaDespacho;
import utils.ItemSAJson;
import xml.ItemXML;
import xml.OrdenDespachoXML;
import xml.RespuestaXML;
import xml.SolicitudXML;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;

import entities.Articulo;
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

	@EJB(beanName = "AdministradorModulosBean")
	private AdministradorModulos am;

	public AdministrarDespachosBean() {
		// TODO Auto-generated constructor stub
	}

	public String procesarSolicitudDespacho(String valorXml) {
		// Aca hay que procesar el xml y hacer lo que haya que hacer.
		String resultado = "";
		XStream xstream = new XStream();
		xstream.alias("despacho", OrdenDespachoXML.class);
		xstream.alias("item", ItemXML.class);
		String[] formats = { "yyyy-MM-dd HH:mm" };
		xstream.registerConverter(new DateConverter("yyyy-MM-dd HH:mm", formats));

		OrdenDespachoXML odXml = (OrdenDespachoXML) xstream.fromXML(valorXml);
		OrdenDespacho od = new OrdenDespacho();
		od.setIdOrdenDespacho(odXml.getNroDespacho());
		od.setNroVenta(odXml.getNroVenta());
		od.setFecha(odXml.getFecha());
		od.setEstado("Pendiente");

		Modulo m = (Modulo) em.find(Modulo.class, odXml.getIdModulo());
		if (m != null) {
			od.setModulo(m);

			// Armar Solicitudes y ItemsOrdenDespacho
			List<ItemXML> itmsXml = odXml.getItems();

			
			// la hash table tienen <idDeposito, Solicitud>
			Map<Modulo, List<ItemSolicitud>> tabla = new HashMap<Modulo, List<ItemSolicitud>>();
			
			for (ItemXML itm : itmsXml) {

				Articulo art = (Articulo) em.find(Articulo.class, Integer.parseInt(itm.getCodigo()));
				if (art != null) {// TODO: que hacer cuando es null???
					// Ya esta el deposito de ese articulo en la tabla??? //

					if (!tabla.containsKey(art.getModulo())) {
						// Creo una nueva Lista de solicitudes y la agrego a la tabla
						tabla.put(art.getModulo(),new ArrayList<ItemSolicitud>());
					}

					tabla.get(art.getModulo()).add(new ItemSolicitud(itm, art));

				} else {
					System.out.println("El articulo nro: " + itm.getCodigo() + " no existe.");
				}
			}

			if (!tabla.isEmpty()) {

				for (Modulo modulo : tabla.keySet()) {
					Solicitud solicitud = new Solicitud();
					solicitud.agregarItemsSolicitudArticulo(tabla.get(modulo));
				
					od.agregarSolicitudArticulo(solicitud);
					
				}
				
				em.merge(od);
				resultado = "OK";
				
			} else {
				resultado = "No se genera la OrdenDespacho, ningun Articulo existente";
				System.out
						.println("No se genera la OrdenDespacho, ningun Articulo existente");
			}
		} else {
			resultado = "Error no encuentra el modulo correcto";
			System.out.println(resultado);
		}

		return resultado;

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
		String PROVIDER_URL = "remote://" + art.getModulo().getIp() + ":4447";

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

		//sxml.setIdModulo(as.getModuloId());
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

	private OrdenDespacho buscarODporSA(int idSolicitud) {
		Query query = em.createQuery("select od from OrdenDespacho od join od.solicitudes s where s.idSolicitud = ?");
		query.setParameter(1, idSolicitud);
		
		OrdenDespacho od = (OrdenDespacho) query.getSingleResult();
		//OrdenDespacho od = query.
		return od;
	}

	public RespuestaXML recibirArticulos(String jsonData) {
		RespuestaXML respuesta = new RespuestaXML();
		try {
			JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonData);
			JSONArray items_sol = json.getJSONArray("items");
			OrdenDespacho od = null;
			boolean odReady = true;
			ArrayList<ItemSAJson> itemsJson = new ArrayList<ItemSAJson>();

			int index = 0;
			// Recupera el idModulo
			int idModulo = json.getInt("idModulo");

			if (idModulo == 0) {
				respuesta.setEstado("ERROR");
				respuesta.setMensaje("El modulo no existe");
				return respuesta;
			}

			// Recupera el idSolicitud
			int idSolicitud = json.getInt("idSolicitud");
			if (idSolicitud != 0) {
				/*
				 * Busca si existe la solicitud de art�culos y levanta la orden
				 * de despacho
				 */
				od = buscarODporSA(idSolicitud);
				if (od == null) {
					respuesta.setEstado("ERROR");
					respuesta.setMensaje("No existe orden de despacho asociada");
					return respuesta;
				}
			} else {
				respuesta.setEstado("ERROR");
				respuesta.setMensaje("La solicitud de articulos no existe");
				return respuesta;
			}

			while (index<items_sol.size()) {
				JSONObject item = (JSONObject) items_sol.get(index);
				// Se obtiene C�digo de Art�culo y cantidad recibida
				int codigo = item.getInt("codigo");
				int cantidad = item.getInt("cantidad");

				itemsJson.add(new ItemSAJson(codigo, cantidad));
				index = index++;
			}

			if (!itemsJson.isEmpty()) {
				ArrayList<Solicitud> solicitudes = (ArrayList<Solicitud>) od
						.getSolicitudes();
				/*
				 * Recorre las solicitudes para determinar si la OD ha sido
				 * cumplida
				 */
				for (Solicitud so : solicitudes) {
					if (so.getIdSolicitud() == idSolicitud) {
						ArrayList<ItemSolicitud> items = (ArrayList<ItemSolicitud>) so
								.getItems();

						// Recorre los items recibidos
						for (ItemSAJson isaj : itemsJson)
							// Recorre los items de la Solicitud de Art�culos
							for (ItemSolicitud is : items) {
								if (is.getArticulo().getNroArticulo() == isaj
										.getCodigo()) {
									int cantRec = is.getCantidadRecibida()
											+ isaj.getCantidad();
									// Setea cantidad recibida
									is.setCantidadRecibida(cantRec);
									if (cantRec != is.getCantidad()) {
										odReady = false;
									}
									break;
								}
							}
						/*
						 * Una vez terminado el Update de cantidades recibidas
						 * en la SA Se dispone a chequear si todas sus
						 * posiciones fueron cumplidas Si odReady es false, ya
						 * se sabe que algo est� pendiente
						 */
						if (odReady)
							for (ItemSolicitud is : items)
								if (is.getCantidadRecibida() != is
										.getCantidad()) {
									odReady = false;
									break;
								}
					} else {
						for (ItemSolicitud iso : so.getItems()) {
							if (iso.getCantidad() != iso.getCantidadRecibida()) {
								odReady = false;
								break;
							}
						}
					}
				}
				/*
				 * Si la OD contiene todas sus Solicitudes de Art�culos
				 * completas, env�a Log al PortalWeb y Log&Monit
				 */
				if (odReady) {
					od.setEstado("Completa");
					em.merge(od);
					
					notificarEntregaExitosa(od);
					respuesta.setEstado("OK");
					respuesta.setMensaje("OD completa");
				} else {
					od.setEstado("Parcial");
					em.merge(od);
					
					respuesta.setEstado("OK");
					respuesta.setMensaje("OD parcialmente cumplida");
				}
			} else {
				respuesta.setEstado("ERROR");
				respuesta.setMensaje("No existen Items a Recibir");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return respuesta;
	}

	private void notificarEntregaExitosa(OrdenDespacho od) {
		// Notificar Entrega a Log&Monit
		notificarEntregaDespacho ned = new notificarEntregaDespacho();
		try {
			String jsonData = ned.notificarEntregaDespachoLogistica(od.getIdOrdenDespacho(),am.getModulo("logistica").getRestDestinationLogisticaCambioEstado());
			JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonData);
			String estado = json.getString("estado");
			String mensaje = json.getString("mensaje");
			//Guardar en el log interno
			System.out.println(String.valueOf(estado));
			System.out.println(String.valueOf(mensaje));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Notificar Entrega a Portal Web
		try {
			RespuestaXML respuesta = ned.notificarEntregaDespachoPortal(od.getNroVenta(), od.getModulo().getIp());
			//Guardar en el log interno
			System.out.println(respuesta.getEstado());
			System.out.println(respuesta.getMensaje());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
