package sessionBeans;

import java.util.ArrayList;

import javax.ejb.Remote;

import valueObjects.ArticuloVO;

@Remote
public interface AdministradorArticulos {
	public boolean createArticulo(int codigo, String nombre, int idModulo);
	public ArrayList<ArticuloVO> getAllArticulos();
	public ArrayList<ArticuloVO> searchBy(String filtro, int value);
}
