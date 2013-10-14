package entities;

public class ItemOrdenDespacho {
	private Articulo articulo;
	private int cantidad;
	private int solicitudArticulo;
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
		return solicitudArticulo;
	}
	public void setSolicitudArticulo(int solicitudArticulo) {
		this.solicitudArticulo = solicitudArticulo;
	}
	public ItemOrdenDespacho(Articulo articulo, int cantidad,
			int solicitudArticulo) {
		this.articulo = articulo;
		this.cantidad = cantidad;
		this.solicitudArticulo = solicitudArticulo;
	}
	

}
