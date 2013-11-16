package utils;

public class ItemSAJson {

	private int codigo;
	private int cantidad;
	
	public ItemSAJson(int codigo, int cantidad) {
		super();
		this.codigo = codigo;
		this.cantidad = cantidad;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
}
