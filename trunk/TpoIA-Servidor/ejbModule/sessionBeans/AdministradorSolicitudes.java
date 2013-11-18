package sessionBeans;

import java.util.ArrayList;

import javax.ejb.Remote;

import entities.Solicitud;
import valueObjects.SolicitudVO;

@Remote
public interface AdministradorSolicitudes {
	public ArrayList<SolicitudVO> getAllSolicitudes();
	public SolicitudVO getSolicitudById(int idsolicitud);
	public ArrayList<SolicitudVO> searchSolicitudes(String filtro, int valor);
	public Solicitud getSolicitud(int idSolicitud);
}
