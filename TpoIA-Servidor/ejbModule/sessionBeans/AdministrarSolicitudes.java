package sessionBeans;

import javax.ejb.Local;

import entities.Solicitud;

@Local
public interface AdministrarSolicitudes {
	public Solicitud getSolicitud(int idSolicitud);
}
