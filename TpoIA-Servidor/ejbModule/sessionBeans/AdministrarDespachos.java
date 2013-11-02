package sessionBeans;

import javax.ejb.Remote;

@Remote
public interface AdministrarDespachos {
	public String procesarSolicitudDespacho(String xml);

}