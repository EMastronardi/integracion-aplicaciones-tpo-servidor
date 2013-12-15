package interfaz;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

import sessionBeans.AdministrarDespachos;

@Stateless
@WebService(
		name = "despachador", 
		serviceName = "services", 
		targetNamespace = "http://bean.webservice.interfaz.tpia.uade.edu.ar/"
)
public class DespachadorBean implements Despachador {
	@EJB
	private AdministrarDespachos adminOD;

	public String procesarOrdenDespacho(String valorXml) {
		try {
			return adminOD.procesarSolicitudDespacho(valorXml);

		} catch (Exception e) {
			return "<resultado> <estado>ERROR</estado> <mensaje>Error al procesar Despacho</mensaje> </resultado>";
		}
	}
}
