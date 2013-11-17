package entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import xml.ItemXML;

@Entity
@Table(name="ItemsSolicitudes")
public class ItemSolicitud implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private int cantidad;
	private int cantidadRecibida;
	
	
	@OneToOne
	@JoinColumn(name = "idArticulo")
	private Articulo articulo;
	
	public int getCantidadRecibida() {
		return cantidadRecibida;
	}
	public void setCantidadRecibida(int cantidadRecibida) {
		this.cantidadRecibida = cantidadRecibida;
	}
	
	

	public ItemSolicitud() {
	
	}
	
	
	public Articulo getArticulo() {
		return articulo;
	}
	public void setArticulo(Articulo articulo) {
		this.articulo = articulo;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	
	public ItemSolicitud(ItemXML itm, Articulo art) {
		this.setArticulo(art);
		this.setCantidad(itm.getCantidad());
		this.setCantidadRecibida(0);
	}
}
