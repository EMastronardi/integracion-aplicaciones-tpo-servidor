package sessionBeans;

import java.util.ArrayList;

import javax.ejb.Local;

import valueObjects.ModuloVO;
import entities.Modulo;

@Local
public interface AdministradorModulos {
	public Modulo getModulo (int idModulo);
	public Modulo getModulo (String tipo);
	public boolean createModulo(String tipo, int idModulo, String ip, String nombre, String codigo);
	public boolean deleteModulo(int idModulo);
	public boolean updateModulo(String tipo, int idModulo, String ip, String nombre, String codigo);
	public ArrayList<ModuloVO> getAllModulos();
}
