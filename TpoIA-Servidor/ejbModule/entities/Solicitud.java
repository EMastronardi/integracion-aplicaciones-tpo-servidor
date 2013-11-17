package entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Solicitudes")
public class Solicitud {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "idSolicitud")
	private List<ItemSolicitud> items;

	public void setItems(List<ItemSolicitud> items) {
		this.items = items;
	}

	public int getIdSolicitud() {
		return id;
	}
	public void setIdSolicitud(int idSolicitud) {
		this.id = idSolicitud;
	}

	public List<ItemSolicitud> getItems() {
		return items;
	}

	public void setItems(ArrayList<ItemSolicitud> items) {
		this.items = items;
	}
	public Solicitud(int idSolicitud, ArrayList<ItemSolicitud> items) {
		this.id = idSolicitud;
		this.items = items;
	}

	public Solicitud() {
		this.items = new ArrayList<ItemSolicitud>();
	}
	
	public void agregarItemsSolicitudArticulo(List<ItemSolicitud> lista) {
		this.items.addAll(lista);

	}

}
