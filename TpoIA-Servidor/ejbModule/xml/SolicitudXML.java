package xml;

import java.util.ArrayList;
import java.util.List;

import entities.ItemSolicitud;
import entities.Solicitud;

public class SolicitudXML {
	/*
	<solicitudArticulos>
	<idSolicitud></idSolicitud>
	<idModulo></idModulo>
	<articulos>
		<articulo>
			<codigo>String</codigo>
			<cantidad>int</cantidad>
		</articulo>
	<articulos>
</solicitudArticulos>*/

	private String idSolicitud;
	private String idModulo;
	private List<ItemSolicitudXML> articulos;
	public SolicitudXML(Solicitud solicitud) {
		
		this.idSolicitud = String.valueOf(solicitud.getIdSolicitud());
		this.idModulo = "12";
		this.articulos = new ArrayList<ItemSolicitudXML>();
		for (ItemSolicitud itmSol : solicitud.getItems()) {
			ItemSolicitudXML itmXml = new ItemSolicitudXML(itmSol.getArticulo().getNroArticulo(), itmSol.getCantidad());
			articulos.add(itmXml);
		}
		
	}
	public String getIdSolicitud() {
		return idSolicitud;
	}
	public void setIdSolicitud(String idSolicitud) {
		this.idSolicitud = idSolicitud;
	}
	public String getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(String idModulo) {
		this.idModulo = idModulo;
	}
	public List<ItemSolicitudXML> getArticulos() {
		return articulos;
	}
	public void setArticulos(List<ItemSolicitudXML> articulos) {
		this.articulos = articulos;
	}
	public void addArticulo(int nroArticulo, int cantidad) {
		if(articulos == null){
			articulos = new ArrayList<ItemSolicitudXML>();
		}
		articulos.add(new ItemSolicitudXML(nroArticulo, cantidad));
	}
	
}
