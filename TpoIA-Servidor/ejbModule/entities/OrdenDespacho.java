package entities;

import java.io.Serializable;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name ="OrdenesDespacho")
public class OrdenDespacho implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private int id;
	@Column(name="idVenta")
	private int nroVenta;
	@ManyToOne
	@JoinColumn(name="idModulo")
	private Modulo modulo;
	private Date fecha;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "idOrdenDespacho")
	private List<Solicitud> solicitudes;
	private String estado;
	
	private String estadoEnvioPortal;
	private String estadoEnvioLogistica;
	
	
	public String getEstadoEnvioPortal() {
		return estadoEnvioPortal;
	}

	public void setEstadoEnvioPortal(String estadoEnvioPortal) {
		this.estadoEnvioPortal = estadoEnvioPortal;
	}

	public String getEstadoEnvioLogistica() {
		return estadoEnvioLogistica;
	}

	public void setEstadoEnvioLogistica(String estadoEnvioLogistica) {
		this.estadoEnvioLogistica = estadoEnvioLogistica;
	}

	public OrdenDespacho() {
		//Esto lo agrego EmmaT para solucionar el problema de id=0 cuando utilizabamos la OD dentro del mismo metodo que la generamos.
		id = -1;
		solicitudes = new ArrayList<Solicitud>();
	}

	public int getIdOrdenDespacho() {
		return id;
	}

	public void setIdOrdenDespacho(int nroDespacho) {
		this.id = nroDespacho;
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
		if(this.solicitudes == null)
			this.solicitudes = new ArrayList<Solicitud>();
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
		this.id = nroDespacho;
		this.nroVenta = nroVenta;
		this.modulo = modulo;
		this.fecha = fecha;
		this.solicitudes = solicitudes;
		this.estado = estado;
	}

	public void agregarSolicitudArticulo(Solicitud solicitud) {
		this.solicitudes.add(solicitud);
	}
	
}
