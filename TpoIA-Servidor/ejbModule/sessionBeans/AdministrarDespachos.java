package sessionBeans;

import javax.ejb.Local;

@Local
public interface AdministrarDespachos {
	public String procesarSolicitudDespacho(String valorXml);

}