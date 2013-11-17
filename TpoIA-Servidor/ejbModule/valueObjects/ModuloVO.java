package valueObjects;

import java.io.Serializable;

public class ModuloVO implements Serializable{
	private int idModulo;
	private String ip;
	private String nombre;
	private String tipo;
	private String codigo;
	private String usuario;
	private String password;
	private String jmsDestination;
	private String restDestinationLogisticaCambioEstado;
	
	public ModuloVO(){}

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

	public String getJmsDestination() {
		return jmsDestination;
	}

	public void setJmsDestination(String jmsDestination) {
		this.jmsDestination = jmsDestination;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getRestDestinationLogisticaCambioEstado() {
		return restDestinationLogisticaCambioEstado;
	}

	public void setRestDestinationLogisticaCambioEstado(
			String restDestinationLogisticaCambioEstado) {
		this.restDestinationLogisticaCambioEstado = restDestinationLogisticaCambioEstado;
	}
	
	
}
