package valueObjects;

import java.io.Serializable;

public class ItemSolicitudVO implements Serializable {
	private int id;
	private int cantidad;
	private int cantidadRecibida;
	private ArticuloVO articulo;
	
	public ItemSolicitudVO() {	
	}

	public int getCantidadRecibida() {
		return cantidadRecibida;
	}
	public void setCantidadRecibida(int cantidadRecibida) {
		this.cantidadRecibida = cantidadRecibida;
	}

	public ArticuloVO getArticulo() {
		return articulo;
	}
	public void setArticulo(ArticuloVO articulo) {
		this.articulo = articulo;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
}
