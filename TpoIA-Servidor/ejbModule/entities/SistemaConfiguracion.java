package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="SistemaConfiguraciones")
public class SistemaConfiguracion {
	@Id
	private int idModulo;

	public int getModuloId() {
		return idModulo;
	}

	public void setModuloId(int idModulo) {
		this.idModulo = idModulo;
	}

	public SistemaConfiguracion() {
	}
	
	
	
}
