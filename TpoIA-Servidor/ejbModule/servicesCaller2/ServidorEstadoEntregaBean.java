package servicesCaller2;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.4.6
 * 2013-12-07T19:09:23.838-03:00
 * Generated source version: 2.4.6
 * 
 */
@WebService(targetNamespace = "http://portalEstadoEntrega/", name = "ServidorEstadoEntregaBean")
@XmlSeeAlso({ObjectFactory.class})
public interface ServidorEstadoEntregaBean {

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "notificarEntregaDespacho", targetNamespace = "http://portalEstadoEntrega/", className = "servicesCaller2.NotificarEntregaDespacho")
    @WebMethod
    @ResponseWrapper(localName = "notificarEntregaDespachoResponse", targetNamespace = "http://portalEstadoEntrega/", className = "servicesCaller2.NotificarEntregaDespachoResponse")
    public java.lang.String notificarEntregaDespacho(
        @WebParam(name = "arg0", targetNamespace = "")
        int arg0
    );
}
