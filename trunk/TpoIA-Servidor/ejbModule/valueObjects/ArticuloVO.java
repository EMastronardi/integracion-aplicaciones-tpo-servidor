package valueObjects;

import java.io.Serializable;

public class ArticuloVO  implements Serializable {
	private int nroArticulo;
	private String nombre;
	private ModuloVO deposito;
	
	public ArticuloVO(){
		
	}
	public int getNroArticulo() {
		return nroArticulo;
	}
	public void setNroArticulo(int nroArticulo) {
		this.nroArticulo = nroArticulo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public ModuloVO getDeposito() {
		return deposito;
	}
	public void setDeposito(ModuloVO deposito) {
		this.deposito = deposito;
	}
	
}
