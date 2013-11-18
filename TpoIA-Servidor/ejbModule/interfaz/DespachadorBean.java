package interfaz;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

import sessionBeans.AdministrarDespachos;

@Stateless
@WebService(name = "despachador", serviceName = "services")
public class DespachadorBean implements Despachador {
	@EJB
	private AdministrarDespachos adminOD;

	public String procesarOrdenDespacho(String valorXml) {
		try {
			return adminOD.procesarSolicitudDespacho(valorXml);

		} catch (Exception e) {
			return "Error al procesarOrdenDespacho";
		}
	}
}
