package xml;

public class ItemXML {
	
	private String codigoArticulo;
	private int cantidad;
	
	public ItemXML(int nroArticulo, int cantidad2) {
		this.codigoArticulo = String.valueOf(nroArticulo);
		this.cantidad = cantidad2;
	}

	public String getCodigo() {
		return codigoArticulo;
	}
	
	public void setCodigo(String codigo) {
		this.codigoArticulo = codigo;
	}
	
	public int getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
}