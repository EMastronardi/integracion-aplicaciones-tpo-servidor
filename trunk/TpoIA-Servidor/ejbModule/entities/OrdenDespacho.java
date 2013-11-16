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
	private int nroOrdenDespacho;
	@Column(name="idVenta")
	private int nroVenta;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="idModulo")
	private Modulo modulo;
	private Date fecha;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "idDespacho")
	private List<Solicitud> solicitudes;
	private String estado;
	
	
	public OrdenDespacho() {
	}

	public int getNroOrdenDespacho() {
		return nroOrdenDespacho;
	}

	public void setNroOrdenDespacho(int nroDespacho) {
		this.nroOrdenDespacho = nroDespacho;
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
	public List<Solicitud> getSolicitudes() {
		return this.solicitudes;
	}
	public void setSolicitudes(ArrayList<Solicitud> solicitudes) {
		this.solicitudes = solicitudes;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public OrdenDespacho(int nroDespacho, int nroVenta, Modulo modulo,
			Date fecha, ArrayList<Solicitud> solicitudes,
			String estado) {
		this.nroOrdenDespacho = nroDespacho;
		this.nroVenta = nroVenta;
		this.modulo = modulo;
		this.fecha = fecha;
		this.solicitudes = solicitudes;
		this.estado = estado;
	}
	
}
