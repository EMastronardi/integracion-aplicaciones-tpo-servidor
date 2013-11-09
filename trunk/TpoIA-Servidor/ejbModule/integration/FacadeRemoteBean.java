package integration;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import sessionBeans.AdministrarDespachos;
import sessionBeans.AdministrarSistema;
import sessionBeans.AdministrarUsuarios;
import valueObjects.UsuarioVO;
import xml.OrdenDespachoXML;

import sessionBeans.AdministradorArticulos;
import sessionBeans.AdministradorModulos;
import entities.Articulo;
import entities.Modulo;

/**
 * Session Bean implementation class Facade
 */
@Stateless
@WebService
public class FacadeRemoteBean implements FacadeRemote {
	
	@EJB(beanName = "AdministrarUsuariosBean")
	private AdministrarUsuarios adminUser;

	@EJB(beanName = "AdministrarDespachosBean")
	private AdministrarDespachos adminOD;
	
	@EJB(beanName = "AdministrarSistema")
	private AdministrarSistema adminSis;
	
	@EJB(beanName = "AdministradorModulosBean")
	private AdministradorModulos adminMod;
	
	@EJB(beanName = "AdministradorArticulos")
	private AdministradorArticulos amdinArt;

	public FacadeRemoteBean() {
		// TODO Auto-generated constructor stub
	}

	@WebMethod(exclude=true)
	public boolean validarUsuario(String username, String password) {
		// validar el usuario que intenta loguear en el sistema
		return adminUser.validarUsuario(username, password);
	}

	@WebMethod(exclude=true)
	public boolean createUser(String username, String password) {
		// TODO Auto-generated method stub
		return adminUser.agregarUsuario(username, password);
	}

	// WebServices
	@WebMethod
	public String procesarOrdenDespacho(String  valorXml) {
		try {
			return adminOD.procesarSolicitudDespacho(valorXml);

		} catch (Exception e) {
			return "Error al procesarOrdenDespacho";
		}
	}


	@WebMethod(exclude=true)
	public boolean validarUsuarioLogueado(String usuario) {

		return false;
	}

	@Override
	public Modulo getModulo(int idModulo) {
		// TODO Auto-generated method stub
		return this.adminMod.getModulo(idModulo);
	}

	@Override
	public boolean addArticulo(Articulo articulo) {
		// TODO Auto-generated method stub
		return this.addArticulo(articulo);
	}

}
