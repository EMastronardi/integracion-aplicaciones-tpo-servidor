package sessionBeans;

import java.util.ArrayList;

import javax.ejb.Remote;

import valueObjects.SolicitudVO;

@Remote
public interface AdministradorSolicitudes {
	public ArrayList<SolicitudVO> getAllSolicitudes();
	public SolicitudVO getSolicitudById(int idsolicitud);
	public ArrayList<SolicitudVO> searchSolicitudes(String filtro, int valor);
	
}
