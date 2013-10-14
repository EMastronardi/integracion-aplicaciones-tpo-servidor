package entities;

import java.util.ArrayList;
import java.util.Date;

public class OrdenDespacho {
	private int nroDespacho;
	private int nroVenta;
	private Modulo modulo;
	private Date fecha;
	private ArrayList<ItemOrdenDespacho> itemsDespacho;
	private String estado;
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
	public Modulo getModulo() {
		return modulo;
	}
	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public ArrayList<ItemOrdenDespacho> getItemsDespacho() {
		return itemsDespacho;
	}
	public void setItemsDespacho(ArrayList<ItemOrdenDespacho> itemsDespacho) {
		this.itemsDespacho = itemsDespacho;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public OrdenDespacho(int nroDespacho, int nroVenta, Modulo modulo,
			Date fecha, ArrayList<ItemOrdenDespacho> itemsDespacho,
			String estado) {
		this.nroDespacho = nroDespacho;
		this.nroVenta = nroVenta;
		this.modulo = modulo;
		this.fecha = fecha;
		this.itemsDespacho = itemsDespacho;
		this.estado = estado;
	}
	
}
