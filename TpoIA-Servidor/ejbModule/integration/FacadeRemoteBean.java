package integration;

import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;

import sessionBeans.AdministradorArticulos;
import sessionBeans.AdministradorModulos;
import sessionBeans.AdministradorSolicitudes;
import sessionBeans.AdministrarDespachos;
import sessionBeans.AdministrarSistema;
import sessionBeans.AdministrarUsuarios;
import valueObjects.ArticuloVO;
import valueObjects.ModuloVO;
import valueObjects.OrdenDespachoVO;
import valueObjects.SolicitudVO;
import valueObjects.UsuarioVO;
import xml.RespuestaXML;
import entities.Articulo;
import entities.Modulo;

/**
 * Session Bean implementation class Facade
 */
@Stateless
@WebService
public class FacadeRemoteBean implements FacadeRemote {
	
	@EJB
	private AdministrarUsuarios adminUser;

	@EJB
	private AdministrarDespachos adminOD;
	
	@EJB
	private AdministrarSistema adminSis;
	
	@EJB
	private AdministradorModulos adminMod;
	
	@EJB
	private AdministradorArticulos adminArt;

	@EJB
	private AdministradorSolicitudes adminSol;
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
			String jmsDestination, String restDestinationLogisticaCambioEstado) {
		// TODO Auto-generated method stub
		return adminMod.createModulo(tipo, idModulo, ip, nombre, codigo, usuario, password, jmsDestination, restDestinationLogisticaCambioEstado);
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
			String jmsDestination, String restDestinationLogisticaCambioEstado){ 
		// TODO Auto-generated method stub
		return adminMod.updateModulo(tipo, idModulo, ip, nombre, codigo,  usuario,  password, jmsDestination, restDestinationLogisticaCambioEstado);
	}

	@Override
	public boolean addArticulo(int codigo, String nombre, int idModulo) {
		// TODO Auto-generated method stub
		return adminArt.createArticulo(codigo, nombre, idModulo);
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
	public RespuestaXML recibirArticulos(String jsonData){
		
		return adminOD.recibirArticulos(jsonData);
	}

	@Override
	public ArrayList<ArticuloVO> searchArticulos(String filtro, int valor) {
		// TODO Auto-generated method stub
		
		return adminArt.searchBy(filtro, valor);
	}

	@Override
	public ArrayList<ArticuloVO> getArticulos() {
		
		return adminArt.getAllArticulos();
	}

	@Override
	public ArrayList<ModuloVO> getAllModulos() {
		// TODO Auto-generated method stub
		ArrayList<ModuloVO> vo = adminMod.getAllModulos();
		
		return vo;
	}

	@Override
	public ArrayList<OrdenDespachoVO> getAllOrdenes() {
		// TODO Auto-generated method stub
		return adminOD.getAllOrdenesDespacho();
	}

	@Override
	public ArrayList<OrdenDespachoVO> searchOrdenes(String filtro, int valor) {
		// TODO Auto-generated method stub
		return adminOD.searchOrdenesDespacho(filtro, valor);
	}

	@Override
	public OrdenDespachoVO getOrdenDespachoById(int nroOrdenDespacho) {
		// TODO Auto-generated method stub
		return adminOD.getOrdenDespacho(nroOrdenDespacho);
	}

	@Override
	public ArrayList<SolicitudVO> getAllSolicitudes() {
		// TODO Auto-generated method stub
		ArrayList<SolicitudVO> sol =adminSol.getAllSolicitudes();
		return sol;
	}

	@Override
	public ArrayList<SolicitudVO> searchSolicitudes(String filtro, int valor) {
		// TODO Auto-generated method stub
		return adminSol.searchSolicitudes(filtro, valor);
	}

	@Override
	public SolicitudVO getSolicitudById(int idsolicitud) {
		// TODO Auto-generated method stub
		return adminSol.getSolicitudById(idsolicitud);
	}

}
