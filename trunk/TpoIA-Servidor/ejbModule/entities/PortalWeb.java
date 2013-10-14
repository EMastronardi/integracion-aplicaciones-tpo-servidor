package entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue( value = "portalWeb")
public class PortalWeb extends Modulo {
	public PortalWeb(int idModulo, String ip, String nombre, String codigo) {
		super(idModulo, ip, nombre, codigo);
		// TODO Auto-generated constructor stub
	}

}
