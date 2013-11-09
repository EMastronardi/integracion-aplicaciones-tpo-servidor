package entities;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

@Entity
@Table(name="Modulos")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="tipo", discriminatorType= DiscriminatorType.STRING)

public class Modulo {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int idModulo;
	private String ip;
	private String nombre;
	private String codigo;
	private String usuario;
	private String password;
	private String jmsDestination;
	
	public String getJmsDestination() {
		return jmsDestination;
	}

	public void setJmsDestination(String jmsDestination) {
		this.jmsDestination = jmsDestination;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Modulo() {
	}
	
	public int getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(int idModulo) {
		this.idModulo = idModulo;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public Modulo(int idModulo, String ip, String nombre, String codigo, String usuario, String password, String jmsDestination) {
		this.idModulo = idModulo;
		this.ip = ip;
		this.nombre = nombre;
		this.codigo = codigo;
		this.usuario = usuario;
		this.password = password;
		this.jmsDestination = jmsDestination;
	}
	
}
