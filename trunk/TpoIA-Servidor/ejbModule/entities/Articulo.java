package entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name= "Articulos")
public class Articulo implements Serializable {
	@Id
	@Column(name="idArticulo")
	private int nroArticulo;
	private String nombre;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="idDeposito")
	private Deposito deposito;
	
	public Articulo(){
		
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
