package entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "logistica")
public class Logistica extends Modulo {

	public Logistica(int idModulo, String ip, String nombre, String codigo, String usuario, String password, String jmsDestination) {
		super(idModulo, ip, nombre, codigo, usuario, password, jmsDestination);
		// TODO Auto-generated constructor stub
	}


}
