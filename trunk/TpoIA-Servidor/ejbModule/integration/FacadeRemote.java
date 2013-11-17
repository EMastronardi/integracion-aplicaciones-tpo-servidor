package integration;

import java.util.ArrayList;

import javax.ejb.Remote;

import valueObjects.ArticuloVO;
import valueObjects.ModuloVO;
import valueObjects.UsuarioVO;
import xml.RespuestaXML;
import entities.Articulo;
import entities.Modulo;

@Remote
public interface FacadeRemote {
	public boolean createUser(String username, String password);
	public boolean updateUser(int idUser, String username, String password);
	public boolean deleteUser(int idUser);
	public boolean validarUsuario(String usuario, String password);
	public boolean validarUsuarioLogueado(String usuario);
	public Modulo getModulo(int idModulo);
	public boolean addArticulo(Articulo articulo);
	public boolean createModulo(String tipo, int idModulo, String ip, String nombre, String codigo, String usuario, String password, String jmsDestination);
	public boolean deleteModulo(int idModulo);
	public boolean updateModulo(String tipo, int idModulo, String ip, String nombre,String codigo, String usuario, String password, String jmsDestination);
	public boolean addArticulo(int codigo, String nombre, int idModulo);
	public ArrayList<ArticuloVO> getArticulos();
	public ArrayList<ArticuloVO> searchArticulos(String filtro, int valor);
	public ArrayList<UsuarioVO> getUsers();
	public ArrayList<ModuloVO> getAllModulos();	
	public RespuestaXML recibirArticulos(String jsonData);
}