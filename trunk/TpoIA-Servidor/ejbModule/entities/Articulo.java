package entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="idModulo")
	private Modulo modulo;
	
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
	public Modulo getModulo() {
		return modulo;
	}
	public void setDeposito(Modulo modulo) {
		this.modulo = modulo;
	}
	public Articulo(int nroArticulo, String nombre, Modulo modulo) {
		this.nroArticulo = nroArticulo;
		this.nombre = nombre;
		this.modulo = modulo;
	}
	
}
