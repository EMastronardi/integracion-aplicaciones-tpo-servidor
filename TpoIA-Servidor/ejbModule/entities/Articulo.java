package entities;

public class Articulo {

	private int nroArticulo;
	private String nombre;
	private Deposito deposito;
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
	public Deposito getDeposito() {
		return deposito;
	}
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	public Articulo(int nroArticulo, String nombre, Deposito deposito) {
		this.nroArticulo = nroArticulo;
		this.nombre = nombre;
		this.deposito = deposito;
	}
	
}
