package sessionBeans;

import java.util.ArrayList;

import javax.ejb.Local;

import valueObjects.ArticuloVO;

@Local
public interface AdministradorArticulos {
	public boolean createArticulo(int codigo, String nombre, int idModulo);
	public ArrayList<ArticuloVO> getAllArticulos();
	public ArrayList<ArticuloVO> searchBy(String filtro, int value);
}
