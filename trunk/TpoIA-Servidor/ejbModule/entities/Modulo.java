package entities;

public abstract class Modulo {
	private int idModulo;
	private String ip;
	private String nombre;
	private String codigo;
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
	public Modulo(int idModulo, String ip, String nombre, String codigo) {
		this.idModulo = idModulo;
		this.ip = ip;
		this.nombre = nombre;
		this.codigo = codigo;
	}
	
}
