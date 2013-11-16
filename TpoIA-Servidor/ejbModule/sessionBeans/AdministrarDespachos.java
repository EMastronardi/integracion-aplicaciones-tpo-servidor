package sessionBeans;

import javax.ejb.Local;

import xml.RespuestaXML;

@Local
public interface AdministrarDespachos {
	public String procesarSolicitudDespacho(String valorXml);
	public RespuestaXML recibirArticulos(String jsonData);

}