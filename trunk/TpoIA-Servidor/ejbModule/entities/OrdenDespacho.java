package entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name ="OrdenesDespacho")
public class OrdenDespacho {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name ="idDespacho")
	private int nroDespacho;
	@Column(name="idVenta")
	private int nroVenta;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="idModulo")
	private Modulo modulo;
	private Date fecha;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "idDespacho")
	private List<ItemOrdenDespacho> itemsDespacho;
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
	public List<ItemOrdenDespacho> getItemsDespacho() {
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
