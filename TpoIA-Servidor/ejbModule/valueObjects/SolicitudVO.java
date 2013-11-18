package valueObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SolicitudVO implements Serializable{
	private int id;
	private List<ItemSolicitudVO> items;

	public SolicitudVO() {
		this.items = new ArrayList<ItemSolicitudVO>();
	}
	public void setItems(List<ItemSolicitudVO> items) {
		this.items = items;
	}

	public int getIdSolicitud() {
		return id;
	}
	public void setIdSolicitud(int idSolicitud) {
		this.id = idSolicitud;
	}

	public List<ItemSolicitudVO> getItems() {
		return items;
	}

	public void setItems(ArrayList<ItemSolicitudVO> items) {
		this.items = items;
	}
	
	public void agregarItemsSolicitudArticulo(List<ItemSolicitudVO> lista) {
		this.items.addAll(lista);

	}

}
