package sessionBeans;

import javax.ejb.Remote;

import entities.Deposito;
import entities.Modulo;

@Remote
public interface AdministradorModulos {
	public Modulo getModulo (int idModulo);
	public boolean createModulo(String tipo, int idModulo, String ip, String nombre, String codigo, String usuario, String password, String jmsDestination);
	public boolean deleteModulo(int idModulo);
	public boolean updateModulo(Modulo modulo);
	public Deposito getDeposito(int idDeposito);
	public String getCadena(int idModulo);
}
