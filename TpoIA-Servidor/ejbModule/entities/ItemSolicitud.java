package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity(name="ItemsOrdenesDespacho")
public class ItemSolicitud {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private int iditemOrdenDespacho;
	@ManyToOne
    @JoinColumn(name="idArticulo")
	private Articulo articulo;
	private int cantidad;
	private int cantidadRecibida;
	public int getCantidadRecibida() {
		return cantidadRecibida;
	}
	public void setCantidadRecibida(int cantidadRecibida) {
		this.cantidadRecibida = cantidadRecibida;
	}
	private int idSolicitud;
	
	
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
	
	public int getIditemOrdenDespacho() {
		return iditemOrdenDespacho;
	}
	public void setIditemOrdenDespacho(int iditemOrdenDespacho) {
		this.iditemOrdenDespacho = iditemOrdenDespacho;
	}
	public int getIdSolicitud() {
		return idSolicitud;
	}
	public void setIdSolicitud(int idSolicitud) {
		this.idSolicitud = idSolicitud;
	}
	public void setSolicitudArticulo(int solicitudArticulo) {
		this.idSolicitud = solicitudArticulo;
	}
	public ItemSolicitud(Articulo articulo, int cantidad,
			int solicitudArticulo) {
		this.articulo = articulo;
		this.cantidad = cantidad;
		this.idSolicitud = solicitudArticulo;
	}
}
