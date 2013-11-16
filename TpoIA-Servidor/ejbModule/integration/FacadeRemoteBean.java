package integration;

import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;

import sessionBeans.AdministradorArticulos;
import sessionBeans.AdministradorModulos;
import sessionBeans.AdministrarDespachos;
import sessionBeans.AdministrarSistema;
import sessionBeans.AdministrarUsuarios;
import valueObjects.ModuloVO;
import valueObjects.UsuarioVO;
import entities.Articulo;
import entities.Deposito;
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
	
	@EJB(beanName = "AdministrarSistemaBean")
	private AdministrarSistema adminSis;
	
	@EJB(beanName = "AdministradorModulosBean")
	private AdministradorModulos adminMod;
	
	@EJB(beanName = "AdministradorArticulosBean")
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
	@WebMethod(exclude=true)
	public Modulo getModulo(int idModulo) {
		// TODO Auto-generated method stub
		return this.adminMod.getModulo(idModulo);
	}

	
	@Override
	@WebMethod(exclude=true)
	public boolean addArticulo(Articulo articulo) {
		// TODO Auto-generated method stub
		return this.addArticulo(articulo);
	}

	@Override
	@WebMethod(exclude=true)
	public boolean createModulo(String tipo, int idModulo, String ip, String nombre,
			String codigo, String usuario, String password,
			String jmsDestination) {
		// TODO Auto-generated method stub
		return adminMod.createModulo(tipo, idModulo, ip, nombre, codigo, usuario, password, jmsDestination);
	}

	@Override
	@WebMethod(exclude=true)
	public boolean deleteModulo(int idModulo) {
		// TODO Auto-generated method stub
		return adminMod.deleteModulo(idModulo);
	}

	@Override
	public boolean updateModulo(String tipo, int idModulo, String ip, String nombre,
			String codigo, String usuario, String password,
			String jmsDestination){ 
		// TODO Auto-generated method stub
		return adminMod.updateModulo(tipo, idModulo, ip, nombre, codigo,  usuario,  password, jmsDestination);
	}

	@Override
	public Deposito getDeposito(int idDeposito) {
		// TODO Auto-generated method stub
		Deposito dep = this.adminMod.getDeposito(idDeposito);
		return dep;
	}

	@Override
	public boolean addArticulo(int codigo, String nombre, int idModulo) {
		// TODO Auto-generated method stub
		return amdinArt.createArticulo(codigo, nombre, idModulo);
	}

	@Override
	public ArrayList<UsuarioVO> getUsers() {
		// TODO Auto-generated method stub
		return adminUser.getUsers();
	}

	@Override
	public boolean updateUser(int idUser, String username, String password) {
		// TODO Auto-generated method stub
		
		return adminUser.actualizarUsuario(idUser, username, password);
	}

	@Override
	public boolean deleteUser(int idUser) {
		// TODO Auto-generated method stub
		return adminUser.eliminarUsuario(idUser);
	}

	@Override
	public ArrayList<ModuloVO> getAllModulos() {
		// TODO Auto-generated method stub
		ArrayList<ModuloVO> vo = adminMod.getAllModulos();
		
		return vo;
	}

}
