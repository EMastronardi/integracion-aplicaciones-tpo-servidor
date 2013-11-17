package servicesCaller;

import java.io.UnsupportedEncodingException;

import javax.ejb.EJB;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import sessionBeans.AdministradorModulos;


public class notificarEntregaDespacho {
	
	public notificarEntregaDespacho() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String notificarEntregaDespachoLogistica(int nroOrdenDespacho, String direccion) throws Exception {
		
		String json = "{\"nroDespacho\": " + String.valueOf(nroOrdenDespacho) + "}";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost("http://" + direccion + "/LogisticaMonitoreoWEB/rest/LogisticaMonitoreo/notificarEntregaDespacho");
		httpPost.addHeader("Content-Type","application/json");
		httpPost.setEntity(new StringEntity(json));
		CloseableHttpResponse  response = httpclient.execute(httpPost);
	    HttpEntity entity = response.getEntity();
	    String respuesta = EntityUtils.toString(entity);
	    EntityUtils.consume(entity);
		response.close();
		return respuesta;
	}
}
