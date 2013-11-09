package sessionBeans;

import javax.ejb.Local;

@Local
public interface AdministrarSistema {
	public int getModuloId();
	public String getModuloIp();

}
