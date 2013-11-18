package sessionBeans;

import java.util.ArrayList;

import javax.ejb.Local;

import valueObjects.OrdenDespachoVO;
import xml.RespuestaXML;

@Local
public interface AdministrarDespachos {
	public String procesarSolicitudDespacho(String valorXml);
	public RespuestaXML recibirArticulos(String jsonData);
	public ArrayList<OrdenDespachoVO> getAllOrdenesDespacho();
	public ArrayList<OrdenDespachoVO> searchOrdenesDespacho(String filtro, int valor);
	public OrdenDespachoVO getOrdenDespacho(int nroOrdenDespacho);


}