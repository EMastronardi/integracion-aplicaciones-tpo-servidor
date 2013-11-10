package xml;

public class ItemXML {
	
	private String codigo;
	private int cantidad;
	
	public ItemXML(int nroArticulo, int cantidad2) {
		this.codigo = String.valueOf(nroArticulo);
		this.cantidad = cantidad2;
	}

	public String getCodigo() {
		return codigo;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public int getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
}