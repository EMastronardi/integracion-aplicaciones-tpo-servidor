package valueObjects;

import java.io.Serializable;

public class UsuarioVO implements Serializable{
	private int idUsuario;
	private String nombre;
	private String password;
	public UsuarioVO(){}
	public UsuarioVO(int idUsuario, String nombre, String password) {
		this.idUsuario = idUsuario;
		this.nombre = nombre;
		this.password = password;
	}
	public int getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
