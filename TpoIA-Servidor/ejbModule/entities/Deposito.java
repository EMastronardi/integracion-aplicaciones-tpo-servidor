package entities;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value="deposito")
public class Deposito extends Modulo implements Serializable {

	public Deposito(int idModulo, String ip, String nombre, String codigo, String usuario, String password, String jmsDestination) {
		super(idModulo, ip, nombre, codigo, usuario, password, jmsDestination);
		this.tipo = "deposito";
	}
	
	public Deposito(){
		
	}
}
