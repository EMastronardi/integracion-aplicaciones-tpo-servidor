package servicesCaller2;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.4.6
 * 2013-12-07T19:09:23.903-03:00
 * Generated source version: 2.4.6
 * 
 */
@WebServiceClient(name = "ServidorEstadoEntregaBeanService", 
                  wsdlLocation = "http://localhost:8080/portalEstadoEntrega/ServidorEstadoEntregaBeanService/ServidorEstadoEntregaBean?wsdl",
                  targetNamespace = "http://portalEstadoEntrega/") 
public class ServidorEstadoEntregaBeanService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://portalEstadoEntrega/", "ServidorEstadoEntregaBeanService");
    public final static QName ServidorEstadoEntregaBeanPort = new QName("http://portalEstadoEntrega/", "ServidorEstadoEntregaBeanPort");
    static {
        URL url = null;
        try {
            url = new URL("http://localhost:8080/portalEstadoEntrega/ServidorEstadoEntregaBeanService/ServidorEstadoEntregaBean?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(ServidorEstadoEntregaBeanService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://192.168.1.109:8080/portalEstadoEntrega/ServidorEstadoEntregaBeanService/ServidorEstadoEntregaBean?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public ServidorEstadoEntregaBeanService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public ServidorEstadoEntregaBeanService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ServidorEstadoEntregaBeanService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public ServidorEstadoEntregaBeanService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public ServidorEstadoEntregaBeanService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public ServidorEstadoEntregaBeanService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns ServidorEstadoEntregaBean
     */
    @WebEndpoint(name = "ServidorEstadoEntregaBeanPort")
    public ServidorEstadoEntregaBean getServidorEstadoEntregaBeanPort() {
        return super.getPort(ServidorEstadoEntregaBeanPort, ServidorEstadoEntregaBean.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ServidorEstadoEntregaBean
     */
    @WebEndpoint(name = "ServidorEstadoEntregaBeanPort")
    public ServidorEstadoEntregaBean getServidorEstadoEntregaBeanPort(WebServiceFeature... features) {
        return super.getPort(ServidorEstadoEntregaBeanPort, ServidorEstadoEntregaBean.class, features);
    }

}
