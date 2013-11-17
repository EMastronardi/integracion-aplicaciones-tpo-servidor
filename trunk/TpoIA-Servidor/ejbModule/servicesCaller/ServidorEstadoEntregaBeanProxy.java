package servicesCaller;

public class ServidorEstadoEntregaBeanProxy implements servicesCaller.ServidorEstadoEntregaBean {
  private String _endpoint = null;
  private servicesCaller.ServidorEstadoEntregaBean servidorEstadoEntregaBean = null;
  
  public ServidorEstadoEntregaBeanProxy() {
    _initServidorEstadoEntregaBeanProxy();
  }
  
  public ServidorEstadoEntregaBeanProxy(String endpoint) {
    _endpoint = endpoint;
    _initServidorEstadoEntregaBeanProxy();
  }
  
  private void _initServidorEstadoEntregaBeanProxy() {
    try {
      servidorEstadoEntregaBean = (new servicesCaller.ServidorEstadoEntregaBeanServiceLocator()).getServidorEstadoEntregaBeanPort();
      if (servidorEstadoEntregaBean != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)servidorEstadoEntregaBean)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)servidorEstadoEntregaBean)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (servidorEstadoEntregaBean != null)
      ((javax.xml.rpc.Stub)servidorEstadoEntregaBean)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public servicesCaller.ServidorEstadoEntregaBean getServidorEstadoEntregaBean() {
    if (servidorEstadoEntregaBean == null)
      _initServidorEstadoEntregaBeanProxy();
    return servidorEstadoEntregaBean;
  }
  
  public java.lang.String notificarEntregaDespacho(int arg0) throws java.rmi.RemoteException{
    if (servidorEstadoEntregaBean == null)
      _initServidorEstadoEntregaBeanProxy();
    return servidorEstadoEntregaBean.notificarEntregaDespacho(arg0);
  }
  
  
}