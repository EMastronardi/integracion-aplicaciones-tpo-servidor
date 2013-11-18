package sessionBeans;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
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
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
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
import valueObjects.ArticuloVO;
import valueObjects.ItemSolicitudVO;
import valueObjects.ModuloVO;
import valueObjects.OrdenDespachoVO;
import valueObjects.SolicitudVO;
import xml.ItemXML;
import xml.OrdenDespachoXML;
import xml.RespuestaXML;
import xml.SolicitudXML;

import com.sun.xml.internal.bind.v2.TODO;
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

	@EJB(beanName = "AdministrarSolicitudesBean")
	private AdministrarSolicitudes as;

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

				Articulo art = (Articulo) em.find(Articulo.class,
						Integer.parseInt(itm.getCodigo()));
				if (art != null) {// TODO: que hacer cuando es null???
					// Ya esta el deposito de ese articulo en la tabla??? //

					if (!tabla.containsKey(art.getModulo())) {
						// Creo una nueva Lista de solicitudes y la agrego a la
						// tabla
						tabla.put(art.getModulo(),
								new ArrayList<ItemSolicitud>());
					}

					tabla.get(art.getModulo()).add(new ItemSolicitud(itm, art));

				} else {
					System.out.println("El articulo nro: " + itm.getCodigo()
							+ " no existe.");
				}
			}

			if (!tabla.isEmpty()) {

				for (Modulo modulo : tabla.keySet()) {
					Solicitud solicitud = new Solicitud();
					solicitud.agregarItemsSolicitudArticulo(tabla.get(modulo));

					od.agregarSolicitudArticulo(solicitud);
				}

				em.merge(od);

				// Envio a cada deposito la solicitud correspondiente
				for (Solicitud s : od.getSolicitudes()) {
					try {
						solicitarADeposito2(s);
					} catch (JMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NamingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
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

	private void solicitarADeposito2(Solicitud solicitud) throws JMSException,
			NamingException {
		QueueConnection qCon = null;
		QueueSession qSession = null;

		// Obtengo la parametrizacion del servicio
		// int numeroOdvAsignada = adminParam.getNumeroOdvAsignada();
		// String urlProviderKey = Constantes.COLA_SOL_COMPRA_PROVIDER_URL + "_"
		// + numeroOdvAsignada;
		String urlProvider = "remote://localhost:4447 ";
		// String queueNameKey = Constantes.COLA_RECIBIR_SOL_COMPRA + "_" +
		// numeroOdvAsignada;
		String queueName = "queue/procesarOrdenDespacho";

		// Inicializo el contexto
		Hashtable<String, String> props = new Hashtable<String, String>();
		props.put(InitialContext.INITIAL_CONTEXT_FACTORY,
				"org.jnp.interfaces.NamingContextFactory");
		props.put("java.naming.factory.initial",
				"org.jboss.naming.remote.client.InitialContextFactory");
		props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");

		props.put(InitialContext.PROVIDER_URL, urlProvider);
		Context ctx = new InitialContext(props);

		// buscar la Connection Factory en JNDI
		QueueConnectionFactory qfactory = (QueueConnectionFactory) ctx
				.lookup("ConnectionFactory");
		// buscar la Cola en JNDI
		Queue queue = (Queue) ctx.lookup(queueName);

		// crear la connection y la session a partir de la connection
		qCon = qfactory.createQueueConnection();
		qSession = qCon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

		// crear un producer para enviar mensajes usando la session
		QueueSender qSender = qSession.createSender(queue);
		// crear un mensaje de tipo text y setearle el contenido
		TextMessage message = qSession.createTextMessage();
		XStream xstream = new XStream();
		SolicitudXML solXml = new SolicitudXML(solicitud);

		xstream.alias("SolicitudArticulos", SolicitudXML.class);
		xstream.alias("articulos", ItemXML.class);

		String xml = xstream.toXML(solXml);

		message.setText(xml);

		// enviar el mensaje
		qSender.send(message);

		// Cerrar conexiones y sesiones
		if (qCon != null) {
			qCon.close();
		}

		if (qSession != null) {
			qSession.close();
		}
	}

	private void solicitarADeposito(Solicitud solicitud) {

		// a que deposito

		String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
		String DEFAULT_DESTINATION = "queue/procesarOrdenDespacho";
		String DEFAULT_USERNAME = "prod";
		String DEFAULT_PASSWORD = "prod1234";
		String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
		String PROVIDER_URL = "remote://"
				+ solicitud.getItems().get(0).getArticulo().getModulo().getIp()
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
			// TODO: arreglar esta mierda que sigue...

			XStream xstream = new XStream();
			SolicitudXML solXml = new SolicitudXML(solicitud);

			xstream.alias("SolicitudArticulos", SolicitudXML.class);
			xstream.alias("articulos", ItemXML.class);

			String xml = xstream.toXML(solXml);

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

	private OrdenDespacho buscarODporSA(int idSolicitud) {
		Solicitud sa = as.getSolicitud(idSolicitud);
		if (sa == null)
			return null;

		Query query = em
				.createQuery("select o from OrdenDespacho o join o.solicitudes s where s = :solicitud");

		query.setParameter("solicitud", sa);

		OrdenDespacho ordenDespacho = (OrdenDespacho) query.getSingleResult();
		return ordenDespacho;
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
				 * Busca si existe la solicitud de artículos y levanta la orden
				 * de despacho
				 */
				od = buscarODporSA(idSolicitud);
				if (od == null) {
					respuesta.setEstado("ERROR");
					respuesta
							.setMensaje("No existe orden de despacho asociada");
					return respuesta;
				} else {
					if (od.getEstado().equals("Completa")) {
						respuesta.setEstado("ERROR");
						respuesta
								.setMensaje("La orden de despacho esta en estatus Completa");
						return respuesta;
					}
				}
			} else {
				respuesta.setEstado("ERROR");
				respuesta.setMensaje("La solicitud de articulos no existe");
				return respuesta;
			}

			while (index < items_sol.size()) {
				JSONObject item = (JSONObject) items_sol.get(index);
				// Se obtiene Código de Artículo y cantidad recibida
				int codigo = item.getInt("codigo");
				int cantidad = item.getInt("cantidad");

				itemsJson.add(new ItemSAJson(codigo, cantidad));
				index = index + 1;
			}

			if (!itemsJson.isEmpty()) {
				/*
				 * Recorre las solicitudes para determinar si la OD ha sido
				 * cumplida
				 */
				for (Solicitud so : od.getSolicitudes()) {
					if (so.getIdSolicitud() == idSolicitud) {
						// Recorre los items recibidos
						for (ItemSAJson isaj : itemsJson)
							// Recorre los items de la Solicitud de Artículos
							for (ItemSolicitud is : so.getItems()) {
								if (is.getArticulo().getNroArticulo() == isaj
										.getCodigo()) {
									int cantRec = is.getCantidadRecibida()
											+ isaj.getCantidad();
									// Setea cantidad recibida, siempre y cuando
									// no haya más recibido que lo pedido
									if (cantRec > is.getCantidad()) {
										respuesta.setEstado("ERROR");
										respuesta
												.setMensaje("Se excede la cantidad pedida del articulo: "
														+ is.getArticulo()
																.getNombre());
										return respuesta;
									}

									is.setCantidadRecibida(cantRec);
									if (cantRec < is.getCantidad()) {
										odReady = false;
									}
									break;
								}
							}
						/*
						 * Una vez terminado el Update de cantidades recibidas
						 * en la SA Se dispone a chequear si todas sus
						 * posiciones fueron cumplidas Si odReady es false, ya
						 * se sabe que algo está pendiente
						 */
						if (odReady)
							for (ItemSolicitud is : so.getItems())
								if (is.getCantidadRecibida() != is
										.getCantidad()) {
									odReady = false;
									break;
								}
					} else {
						/*
						 * Se recorren además las solicitudes que posee la OD,
						 * si es que existen
						 */
						if (odReady)
							for (ItemSolicitud is : so.getItems()) {
								if (is.getCantidad() != is
										.getCantidadRecibida()) {
									odReady = false;
									break;
								}
							}
					}
				}
				/*
				 * Si la OD contiene todas sus Solicitudes de Artículos
				 * completas, envía Log al PortalWeb y Log&Monit
				 */
				if (odReady) {
					od.setEstado("Completa");
					em.merge(od);

					// notificarEntregaExitosa(od);
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
			String jsonData = ned.notificarEntregaDespachoLogistica(od
					.getIdOrdenDespacho(), am.getModulo("logistica")
					.getRestDestinationLogisticaCambioEstado());
			JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonData);
			String estado = json.getString("estado");
			String mensaje = json.getString("mensaje");
			// Guardar en el log interno
			System.out.println(String.valueOf(estado));
			System.out.println(String.valueOf(mensaje));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Notificar Entrega a Portal Web
		try {
			RespuestaXML respuesta = ned.notificarEntregaDespachoPortal(
					od.getNroVenta(), od.getModulo().getIp());
			// Guardar en el log interno
			System.out.println(respuesta.getEstado());
			System.out.println(respuesta.getMensaje());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public ArrayList<OrdenDespachoVO> getAllOrdenesDespacho() {
		// TODO Auto-generated method stub
		Query q = em.createQuery("from OrdenDespacho");

		ArrayList<OrdenDespacho> vec = (ArrayList<OrdenDespacho>) q
				.getResultList();
		ArrayList<OrdenDespachoVO> rslt = new ArrayList<OrdenDespachoVO>();
		for (OrdenDespacho od : vec) {
			OrdenDespachoVO vo = new OrdenDespachoVO();
			vo.setEstado(od.getEstado());
			vo.setFecha(od.getFecha());
			vo.setIdOrdenDespacho(od.getIdOrdenDespacho());
			vo.setNroVenta(od.getNroVenta());
			// Conversion Modulo > ModuloVO
			ModuloVO mvo = new ModuloVO();
			mvo.setCodigo(od.getModulo().getCodigo());
			mvo.setIdModulo(od.getModulo().getIdModulo());
			mvo.setIp(od.getModulo().getIp());
			mvo.setJmsDestination(od.getModulo().getJmsDestination());
			mvo.setNombre(od.getModulo().getNombre());
			mvo.setPassword(od.getModulo().getPassword());
			mvo.setRestDestinationLogisticaCambioEstado(od.getModulo()
					.getRestDestinationLogisticaCambioEstado());
			mvo.setTipo(od.getModulo().getTipo());
			mvo.setUsuario(od.getModulo().getUsuario());
			vo.setModulo(mvo);
			ArrayList<SolicitudVO> vecsol = new ArrayList<SolicitudVO>();
			// /
			for (Solicitud sol : od.getSolicitudes()) {
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
					itmvo.setArticulo(artvo);
					// No se MAPEA modulos para los articulos!!!
					listitm.add(itmvo);
				}
				solvo.setItems(listitm);
				vecsol.add(solvo);
			}
			vo.setSolicitudes(vecsol);
			rslt.add(vo);
		}
		return rslt;
	}

	@Override
	public ArrayList<OrdenDespachoVO> searchOrdenesDespacho(String filtro,
			int valor) {
		// TODO Auto-generated method stub
		Query q = null;
		if (filtro.equals("nrorden")) {
			q = em.createQuery("select od from OrdenDespacho od where od.id=:value");
		} else {
			q = em.createQuery("select od from OrdenDespacho od, Modulo m where m = od.modulo AND m.idModulo=:value");
		}
		q.setParameter("value", valor);
		ArrayList<OrdenDespacho> vec = (ArrayList<OrdenDespacho>) q
				.getResultList();
		ArrayList<OrdenDespachoVO> rslt = new ArrayList<OrdenDespachoVO>();
		for (OrdenDespacho od : vec) {
			OrdenDespachoVO vo = new OrdenDespachoVO();
			vo.setEstado(od.getEstado());
			vo.setFecha(od.getFecha());
			vo.setIdOrdenDespacho(od.getIdOrdenDespacho());
			vo.setNroVenta(od.getNroVenta());
			// Conversion Modulo > ModuloVO
			ModuloVO mvo = new ModuloVO();
			mvo.setCodigo(od.getModulo().getCodigo());
			mvo.setIdModulo(od.getModulo().getIdModulo());
			mvo.setIp(od.getModulo().getIp());
			mvo.setJmsDestination(od.getModulo().getJmsDestination());
			mvo.setNombre(od.getModulo().getNombre());
			mvo.setPassword(od.getModulo().getPassword());
			mvo.setRestDestinationLogisticaCambioEstado(od.getModulo()
					.getRestDestinationLogisticaCambioEstado());
			mvo.setTipo(od.getModulo().getTipo());
			mvo.setUsuario(od.getModulo().getUsuario());
			vo.setModulo(mvo);
			ArrayList<SolicitudVO> vecsol = new ArrayList<SolicitudVO>();
			// /
			for (Solicitud sol : od.getSolicitudes()) {
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
					itmvo.setArticulo(artvo);
					// No se MAPEA modulos para los articulos!!!
					listitm.add(itmvo);
				}
				solvo.setItems(listitm);
				vecsol.add(solvo);
			}
			vo.setSolicitudes(vecsol);
			rslt.add(vo);
		}
		return rslt;
	}

	@Override
	public OrdenDespachoVO getOrdenDespacho(int nroOrdenDespacho) {
		// TODO Auto-generated method stub
		Query q = em
				.createQuery("select od from OrdenDespacho od where od.id=:value");

		q.setParameter("value", nroOrdenDespacho);
		OrdenDespacho od = (OrdenDespacho) q.getSingleResult();

		OrdenDespachoVO vo = new OrdenDespachoVO();
		vo.setEstado(od.getEstado());
		vo.setFecha(od.getFecha());
		vo.setIdOrdenDespacho(od.getIdOrdenDespacho());
		vo.setNroVenta(od.getNroVenta());
		// Conversion Modulo > ModuloVO
		ModuloVO mvo = new ModuloVO();
		mvo.setCodigo(od.getModulo().getCodigo());
		mvo.setIdModulo(od.getModulo().getIdModulo());
		mvo.setIp(od.getModulo().getIp());
		mvo.setJmsDestination(od.getModulo().getJmsDestination());
		mvo.setNombre(od.getModulo().getNombre());
		mvo.setPassword(od.getModulo().getPassword());
		mvo.setRestDestinationLogisticaCambioEstado(od.getModulo()
				.getRestDestinationLogisticaCambioEstado());
		mvo.setTipo(od.getModulo().getTipo());
		mvo.setUsuario(od.getModulo().getUsuario());
		vo.setModulo(mvo);
		ArrayList<SolicitudVO> vecsol = new ArrayList<SolicitudVO>();
		for (Solicitud sol : od.getSolicitudes()) {
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
				itmvo.setArticulo(artvo);
				// No se MAPEA modulos para los articulos!!!
				listitm.add(itmvo);
			}
			solvo.setItems(listitm);
			vecsol.add(solvo);
		}
		vo.setSolicitudes(vecsol);
		return vo;
	}

}
