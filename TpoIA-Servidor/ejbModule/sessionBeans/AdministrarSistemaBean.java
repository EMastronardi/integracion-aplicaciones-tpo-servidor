package sessionBeans;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * Session Bean implementation class AdministrarSistemaBean
 */
@Stateless
@LocalBean
public class AdministrarSistemaBean implements AdministrarSistema {

    /**
     * Default constructor. 
     */
    public AdministrarSistemaBean() {
        // TODO Auto-generated constructor stub
    }

	@Override
	public int getModuloId() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getModuloIp() {
		// TODO Auto-generated method stub
		return "";
	}

}
