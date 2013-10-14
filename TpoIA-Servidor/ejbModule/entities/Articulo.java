package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name= "Articulos")
public class Articulo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int nroArticulo;
	private String nombre;
	@OneToOne
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
