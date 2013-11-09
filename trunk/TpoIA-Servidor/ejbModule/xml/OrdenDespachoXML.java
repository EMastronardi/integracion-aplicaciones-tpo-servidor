package xml;

import java.util.Date;
import java.util.List;

public class OrdenDespachoXML {
	private int nroDespacho;
	private int nroVenta;
	private int idModulo;
	private Date fecha;
	private List<ItemXML> items;
	public int getNroDespacho() {
		return nroDespacho;
	}
	public void setNroDespacho(int nroDespacho) {
		this.nroDespacho = nroDespacho;
	}
	public int getNroVenta() {
		return nroVenta;
	}
	public void setNroVenta(int nroVenta) {
		this.nroVenta = nroVenta;
	}
	public int getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(int idModulo) {
		this.idModulo = idModulo;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public List<ItemXML> getItems() {
		return items;
	}
	public void setItems(List<ItemXML> items) {
		this.items = items;
	}

}
