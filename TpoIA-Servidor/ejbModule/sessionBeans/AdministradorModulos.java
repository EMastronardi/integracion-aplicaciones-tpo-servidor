package sessionBeans;

import java.util.ArrayList;

import javax.ejb.Remote;

import valueObjects.ModuloVO;
import entities.Deposito;
import entities.Modulo;

@Remote
public interface AdministradorModulos {
	public Modulo getModulo (int idModulo);
	public boolean createModulo(String tipo, int idModulo, String ip, String nombre, String codigo, String usuario, String password, String jmsDestination);
	public boolean deleteModulo(int idModulo);
	public boolean updateModulo(String tipo, int idModulo, String ip, String nombre,String codigo, String usuario, String password,String jmsDestination);
	public Deposito getDeposito(int idDeposito);
	public String getCadena(int idModulo);
	public ArrayList<ModuloVO> getAllModulos();
}
