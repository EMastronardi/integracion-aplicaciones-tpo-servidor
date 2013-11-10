package xml;

import java.util.ArrayList;
import java.util.List;

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
	private List<ItemXML> articulos;
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
	public List<ItemXML> getArticulos() {
		return articulos;
	}
	public void setArticulos(List<ItemXML> articulos) {
		this.articulos = articulos;
	}
	public void addArticulo(int nroArticulo, int cantidad) {
		if(articulos != null){
			articulos = new ArrayList<ItemXML>();
		}
		articulos.add(new ItemXML(nroArticulo, cantidad));
	}
	
}
