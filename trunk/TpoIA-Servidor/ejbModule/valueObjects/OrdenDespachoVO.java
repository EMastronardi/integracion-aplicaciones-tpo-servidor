package valueObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrdenDespachoVO implements Serializable {
	private int id;
	private int nroVenta;
	private ModuloVO modulo;
	private Date fecha;
	private List<SolicitudVO> solicitudes;
	private String estado;
	
	
	public OrdenDespachoVO() {
		solicitudes = new ArrayList<SolicitudVO>();
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
	public ModuloVO getModulo() {
		return modulo;
	}
	public void setModulo(ModuloVO modulo) {
		this.modulo = modulo;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public List<SolicitudVO> getSolicitudes() {
		return this.solicitudes;
	}
	public void setSolicitudes(ArrayList<SolicitudVO> solicitudes) {
		if(this.solicitudes == null)
			this.solicitudes = new ArrayList<SolicitudVO>();
		this.solicitudes = solicitudes;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public void agregarSolicitudArticulo(SolicitudVO solicitud) {
		this.solicitudes.add(solicitud);
	}
}
