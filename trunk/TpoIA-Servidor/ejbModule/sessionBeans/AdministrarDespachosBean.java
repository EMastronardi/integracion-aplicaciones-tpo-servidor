package sessionBeans;

import java.util.ArrayList;
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

import org.jboss.logging.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import utils.Constantes;
import utils.ItemSAJson;
import valueObjects.ArticuloVO;
import valueObjects.ItemSolicitudVO;
import valueObjects.ModuloVO;
import valueObjects.OrdenDespachoVO;
import valueObjects.SolicitudVO;
import xml.ItemSolicitudXML;
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
//import org.hornetq.utils.json.JSONArray;
//import org.hornetq.utils.json.JSONException;
//import org.hornetq.utils.json.JSONObject;
import servicesCaller.notificarEntregaDespacho;

/**
 * Session Bean implementation class Despacho
 */
@Stateless
public class AdministrarDespachosBean implements AdministrarDespachos {

	@PersistenceContext
	private EntityManager em;

	@EJB
	private AdministradorModulos am;

	@EJB
	private AdministradorSolicitudes as;

	Logger logger = Logger.getLogger(AdministrarDespachosBean.class);

	public AdministrarDespachosBean() {
	}

	public String procesarSolicitudDespacho(String valorXml) {

		logger.info("Ingreso a procesarSolicitudDesapacho");
		logger.info("Llega el XML: " + valorXml);

		try {
			// Aca hay que procesar el xml y hacer lo que haya que hacer.
			String resultado = "";
			XStream xstream = new XStream();
			xstream.alias("despacho", OrdenDespachoXML.class);
			xstream.alias("item", ItemXML.class);
			xstream.registerConverter(new DateConverter("yyyy-MM-dd HH:mm",
					new String[] { "yyyy-MM-dd HH:mm:ss" }));
			xstream.ignoreUnknownElements();
			OrdenDespachoXML odXml = (OrdenDespachoXML) xstream
					.fromXML(valorXml);
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
							// Creo una nueva Lista de solicitudes y la agrego a
							// la
							// tabla
							tabla.put(art.getModulo(),
									new ArrayList<ItemSolicitud>());
						}

						tabla.get(art.getModulo()).add(
								new ItemSolicitud(itm, art));

					} else {
						System.out.println("El articulo nro: "
								+ itm.getCodigo() + " no existe.");
					}
				}

				if (!tabla.isEmpty()) {

					for (Modulo modulo : tabla.keySet()) {
						Solicitud solicitud = new Solicitud();
						solicitud.agregarItemsSolicitudArticulo(tabla
								.get(modulo));

						od.agregarSolicitudArticulo(solicitud);
					}

					em.persist(od);

					// od = em.find(OrdenDespacho.class,
					// od.getIdOrdenDespacho());

					// Envio a cada deposito la solicitud correspondiente
					for (Solicitud s : od.getSolicitudes()) {
						System.out.println(s.getIdSolicitud());
						solicitarADeposito(s);
					}
					resultado = "<resultado> <estado>OK</estado> <mensaje>Se proceso correctamente la solicitud</mensaje> </resultado>";

				} else {
					resultado = "<resultado> <estado>ERROR</estado> <mensaje>No se genera la OrdenDespacho, ningun Articulo existente</mensaje> </resultado>";
					System.out
							.println("No se genera la OrdenDespacho, ningun Articulo existente");
				}
			} else {
				resultado = "<resultado> <estado>ERROR</estado> <mensaje>Error no encuentra el modulo correcto</mensaje> </resultado>";
				System.out.println(resultado);
			}

			return resultado;
		} catch (NumberFormatException e) {
			logger.error("Error general en procesarSolicitudDespacho");
			e.printStackTrace();
			return "<resultado> <estado>ERROR</estado> <mensaje>Se proceso incorrectamente la solicitud</mensaje> </resultado>";
		}

	}

	// Aca hay que mandar al deposito correspondiente al Articulo el pedido del
	// mismo, con su cantidad

	private String solicitarADeposito(Solicitud solicitud) {
		String result = "";
		
		//Mensaje a enviar
		XStream xstream = new XStream();
		SolicitudXML solXml = new SolicitudXML(solicitud);
		xstream.ignoreUnknownElements();
		xstream.alias("solicitudArticulos", SolicitudXML.class);
		xstream.alias("articulo", ItemSolicitudXML.class);

		String xml = xstream.toXML(solXml);
		logger.info("Enviando al Deposito : "
				+ solicitud.getItems().get(0).getArticulo().getModulo()
						.getNombre());
		logger.info("XML: " + xml);
		
		
		// a que deposito
		logger.info("Enviando solicitud a deposito");
		String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
		String DEFAULT_DESTINATION = Constantes.getJmsEnviarADepDest();
		String DEFAULT_USERNAME = Constantes.getJmsEnviarADepUser();
		String DEFAULT_PASSWORD = Constantes.getJmsEnviarADepPass();
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
		/*try {
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

		
			message.setText(xml);

			producer.send(message);
			connection.close();
			result="OK";
			return result;
		} catch (JMSException e) {
			result ="Error al enviar a deposito, JMSException";
			logger.error(result);
			e.printStackTrace();
			return result;
			
		} catch (NamingException e) {
			result = "Error al enviar a deposito, NamingException";
			logger.error(result);
			e.printStackTrace();
			return result;
		}
		
		*/
		return result;
		
		
		
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
		logger.info("Recibiendo stock para solicitud");
		logger.info("JSON: "+jsonData);
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
				logger.error("Error en recibirArticulos: El modulo no existe");
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
					logger.error("Error en recibirArticulos: No existe orden de despacho asociada");
					return respuesta;
				} else {
					if (od.getEstado().equals("Completa")) {
						respuesta.setEstado("ERROR");
						respuesta
								.setMensaje("La orden de despacho esta en estatus Completa");
						logger.error("Error en recibirArticulos: La orden de despacho esta en estatus Completa");
						return respuesta;
					}
				}
			} else {
				respuesta.setEstado("ERROR");
				respuesta.setMensaje("La solicitud de articulos no existe");
				logger.error("Error en recibirArticulos: La solicitud de articulos no existe");
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
										logger.error("Error en recibirArticulos: Se excede la cantidad pedida del articulo");
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
					logger.info("recibirArticulos: OD completa se informa estado");
					respuesta.setEstado("OK");
					respuesta.setMensaje("OD completa");
					notificarEntregaExitosa(od);
				} else {
					od.setEstado("Parcial");
					em.merge(od);
					logger.info("recibirArticulos: OD parcialmente cumplida");
					respuesta.setEstado("OK");
					respuesta.setMensaje("OD parcialmente cumplida");
				}
			} else {
				respuesta.setEstado("ERROR");
				respuesta.setMensaje("No existen Items a Recibir");
				logger.error("Error en recibirArticulos: No existen Items a Recibir");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Error al recibir articulos de OD desde Despacho");
			e.printStackTrace();
		}
		return respuesta;
	}

	private void notificarEntregaExitosa(OrdenDespacho od) {
		// Notificar Entrega a Log&Monit
		notificarEntregaDespacho ned = new notificarEntregaDespacho();
		try {
			String jsonData = ned.notificarEntregaDespachoLogistica(
					od.getIdOrdenDespacho(), am.getModulo("logistica").getIp()
							+ Constantes.getRestEnviarNotiLog());
			JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonData);
			String estado = json.getString("estado");
			String mensaje = json.getString("mensaje");
			// Guardar en el log interno
			logger.info("Notificar cambio de estado de OD a Logistica: "
					+ String.valueOf(estado));
			logger.info("Notificar cambio de estado de OD a Logistica: "
					+ String.valueOf(mensaje));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Error al notificar cambio de estado de OD a Logistica");
			e.printStackTrace();
		}

		// Notificar Entrega a Portal Web
		try {
			RespuestaXML respuesta = ned.notificarEntregaDespachoPortal(
					od.getNroVenta(), od.getModulo().getIp());
			// Guardar en el log interno
			logger.info("Notificar cambio de estado de OD a PortalWeb: "
					+ respuesta.getEstado());
			logger.info("Notificar cambio de estado de OD a PortalWeb: "
					+ respuesta.getMensaje());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Error al notificar cambio de estado de OD a PortalWeb");
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
			mvo.setNombre(od.getModulo().getNombre());
			mvo.setTipo(od.getModulo().getTipo());
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
			mvo.setNombre(od.getModulo().getNombre());
			mvo.setTipo(od.getModulo().getTipo());
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
		mvo.setNombre(od.getModulo().getNombre());
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
