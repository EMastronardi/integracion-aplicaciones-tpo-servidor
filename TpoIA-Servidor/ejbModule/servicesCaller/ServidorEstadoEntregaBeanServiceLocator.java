/**
 * ServidorEstadoEntregaBeanServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package servicesCaller;

public class ServidorEstadoEntregaBeanServiceLocator extends org.apache.axis.client.Service implements servicesCaller.ServidorEstadoEntregaBeanService {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1307717858703493083L;

	public ServidorEstadoEntregaBeanServiceLocator() {
    }


    public ServidorEstadoEntregaBeanServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ServidorEstadoEntregaBeanServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ServidorEstadoEntregaBeanPort
    private java.lang.String ServidorEstadoEntregaBeanPort_address = "http://172.16.174.38:8080/portalEstadoEntrega/ServidorEstadoEntregaBean";

    public java.lang.String getServidorEstadoEntregaBeanPortAddress() {
        return ServidorEstadoEntregaBeanPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ServidorEstadoEntregaBeanPortWSDDServiceName = "ServidorEstadoEntregaBeanPort";

    public java.lang.String getServidorEstadoEntregaBeanPortWSDDServiceName() {
        return ServidorEstadoEntregaBeanPortWSDDServiceName;
    }

    public void setServidorEstadoEntregaBeanPortWSDDServiceName(java.lang.String name) {
        ServidorEstadoEntregaBeanPortWSDDServiceName = name;
    }

    public servicesCaller.ServidorEstadoEntregaBean getServidorEstadoEntregaBeanPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ServidorEstadoEntregaBeanPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getServidorEstadoEntregaBeanPort(endpoint);
    }
    
    public servicesCaller.ServidorEstadoEntregaBean getServidorEstadoEntregaBeanPort(String direccion) throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
         try {
             endpoint = new java.net.URL(direccion);
         }
         catch (java.net.MalformedURLException e) {
             throw new javax.xml.rpc.ServiceException(e);
         }
         return getServidorEstadoEntregaBeanPort(endpoint);
     }
    
    public servicesCaller.ServidorEstadoEntregaBean getServidorEstadoEntregaBeanPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            servicesCaller.ServidorEstadoEntregaBeanServiceSoapBindingStub _stub = new servicesCaller.ServidorEstadoEntregaBeanServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getServidorEstadoEntregaBeanPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setServidorEstadoEntregaBeanPortEndpointAddress(java.lang.String address) {
        ServidorEstadoEntregaBeanPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (servicesCaller.ServidorEstadoEntregaBean.class.isAssignableFrom(serviceEndpointInterface)) {
                servicesCaller.ServidorEstadoEntregaBeanServiceSoapBindingStub _stub = new servicesCaller.ServidorEstadoEntregaBeanServiceSoapBindingStub(new java.net.URL(ServidorEstadoEntregaBeanPort_address), this);
                _stub.setPortName(getServidorEstadoEntregaBeanPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("ServidorEstadoEntregaBeanPort".equals(inputPortName)) {
            return getServidorEstadoEntregaBeanPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://integracion/", "ServidorEstadoEntregaBeanService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://integracion/", "ServidorEstadoEntregaBeanPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ServidorEstadoEntregaBeanPort".equals(portName)) {
            setServidorEstadoEntregaBeanPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
