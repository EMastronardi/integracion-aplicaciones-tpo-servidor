package sessionBeans;

import javax.ejb.Remote;

import entities.Modulo;

@Remote
public interface AdministradorModulos {
	public Modulo getModulo (int idModulo);
}
