package servicesCaller;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jboss.logging.Logger;

import com.thoughtworks.xstream.XStream;

import sessionBeans.AdministradorArticulosBean;
import utils.Constantes;
import xml.RespuestaXML;

public class notificarEntregaDespacho {
	Logger logger = Logger.getLogger(notificarEntregaDespacho.class);

	public notificarEntregaDespacho() {
		super();
	}

	public String notificarEntregaDespachoLogistica(int nroOrdenDespacho,
			String direccion) throws Exception {

		String json = "{\"nroDespacho\": " + String.valueOf(nroOrdenDespacho)
				+ "}";
		logger.info("Notificando Entrega Despacho a Logisitca");
		logger.info("JSON: "+json);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost("http://" + direccion);
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.setEntity(new StringEntity(json));
		CloseableHttpResponse response = httpclient.execute(httpPost);
		HttpEntity entity = response.getEntity();
		String respuesta = EntityUtils.toString(entity);
		EntityUtils.consume(entity);
		response.close();
		logger.info("Respuesta: " + respuesta);
		return respuesta;
	}

	public RespuestaXML notificarEntregaDespachoPortal(int nroVenta,
			String direccionIP) throws Exception {
		try {
			String direccion = "http://" + direccionIP
					+ Constantes.getWsEnviarNotiPortalWeb();
			ServidorEstadoEntregaBean service = new ServidorEstadoEntregaBeanServiceLocator()
					.getServidorEstadoEntregaBeanPort(direccion);

			String respuesta = service.notificarEntregaDespacho(nroVenta);
			logger.info("Recibiendo xml al notificarEntregaDespacho");
			logger.info("XML: " + respuesta);
			XStream xStream = new XStream();

			xStream.processAnnotations(new Class[] { RespuestaXML.class });

			RespuestaXML respuestaXml = (RespuestaXML) xStream
					.fromXML(respuesta);
			return respuestaXml;
		} catch (Exception e) {
			logger.error("Error al recibir xml al notificarEntregaDespacho");
			return null;
		}
	}
}
