package entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value="deposito")
public class Deposito extends Modulo {
	public Deposito(int idModulo, String ip, String nombre, String codigo) {
		super(idModulo, ip, nombre, codigo);
		
	}

}
