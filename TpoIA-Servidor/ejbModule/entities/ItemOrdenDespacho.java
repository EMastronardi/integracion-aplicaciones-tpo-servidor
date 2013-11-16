package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity(name="ItemsOrdenesDespacho")
public class ItemOrdenDespacho {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private int iditemOrdenDespacho;
	@ManyToOne
    @JoinColumn(name="idArticulo")
	private Articulo articulo;
	private int cantidad;
	private int idSolicitud;
	
	
	public ItemOrdenDespacho() {
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
	public int getSolicitudArticulo() {
		return idSolicitud;
	}
	public void setSolicitudArticulo(int solicitudArticulo) {
		this.idSolicitud = solicitudArticulo;
	}
	public ItemOrdenDespacho(Articulo articulo, int cantidad,
			int solicitudArticulo) {
		this.articulo = articulo;
		this.cantidad = cantidad;
		this.idSolicitud = solicitudArticulo;
	}
}
