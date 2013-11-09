package entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue( value = "portalWeb")
public class PortalWeb extends Modulo {
	public PortalWeb(int idModulo, String ip, String nombre, String codigo, String usuario, String password, String jmsDestination) {
		super(idModulo, ip, nombre, codigo, usuario, password, jmsDestination);
		// TODO Auto-generated constructor stub
	}

}
