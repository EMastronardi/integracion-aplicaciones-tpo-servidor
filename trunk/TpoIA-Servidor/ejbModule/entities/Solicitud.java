package entities;

import java.util.ArrayList;

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
@Table(name= "Solicitudes")
public class Solicitud {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int idSolicitud;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "idSolicitud")
	private ArrayList<ItemSolicitud> items;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idDeposito")
	private Deposito deposito;
	
	public Deposito getDeposito() {
		return deposito;
	}
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	public int getIdSolicitud() {
		return idSolicitud;
	}
	public void setIdSolicitud(int idSolicitud) {
		this.idSolicitud = idSolicitud;
	}
	public ArrayList<ItemSolicitud> getItems() {
		return items;
	}
	public void setItems(ArrayList<ItemSolicitud> items) {
		this.items = items;
	}
	public Solicitud(int idSolicitud, ArrayList<ItemSolicitud> items) {
		this.idSolicitud = idSolicitud;
		this.items = items;
	}
	public Solicitud() {
	}

}
