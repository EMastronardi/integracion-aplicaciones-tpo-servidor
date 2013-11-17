package xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="resultado")
@XmlAccessorType(XmlAccessType.FIELD)
public class RespuestaXML implements Serializable{
	
	@XmlAttribute
	private String estado;
	@XmlAttribute
	private String mensaje;
	
	public RespuestaXML() {
	
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
}
