
package sinusadvancedsearchmachineservice.sinusadvancedsearchmachineservicecontracts;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "SinusAdvancedSearchMachineServiceImplementation", targetNamespace = "http://sinusadvancedsearchmachineservice/sinusadvancedsearchmachineservicecontracts", wsdlLocation = "http://82.103.112.243/SinusAdvancedSearchMachineService/service?wsdl")
public class SinusAdvancedSearchMachineServiceImplementation
    extends Service
{

    private final static URL SINUSADVANCEDSEARCHMACHINESERVICEIMPLEMENTATION_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(sinusadvancedsearchmachineservice.sinusadvancedsearchmachineservicecontracts.SinusAdvancedSearchMachineServiceImplementation.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = sinusadvancedsearchmachineservice.sinusadvancedsearchmachineservicecontracts.SinusAdvancedSearchMachineServiceImplementation.class.getResource(".");
            url = new URL(baseUrl, "http://82.103.112.243/SinusAdvancedSearchMachineService/service?wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'http://82.103.112.243/SinusAdvancedSearchMachineService/service?wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        SINUSADVANCEDSEARCHMACHINESERVICEIMPLEMENTATION_WSDL_LOCATION = url;
    }

    public SinusAdvancedSearchMachineServiceImplementation(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SinusAdvancedSearchMachineServiceImplementation() {
        super(SINUSADVANCEDSEARCHMACHINESERVICEIMPLEMENTATION_WSDL_LOCATION, new QName("http://sinusadvancedsearchmachineservice/sinusadvancedsearchmachineservicecontracts", "SinusAdvancedSearchMachineServiceImplementation"));
    }

    /**
     * 
     * @return
     *     returns ISinusAdvancedSearchMachineService
     */
    @WebEndpoint(name = "basicHttp")
    public ISinusAdvancedSearchMachineService getBasicHttp() {
        return super.getPort(new QName("http://sinusadvancedsearchmachineservice/sinusadvancedsearchmachineservicecontracts", "basicHttp"), ISinusAdvancedSearchMachineService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ISinusAdvancedSearchMachineService
     */
    @WebEndpoint(name = "basicHttp")
    public ISinusAdvancedSearchMachineService getBasicHttp(WebServiceFeature... features) {
        return super.getPort(new QName("http://sinusadvancedsearchmachineservice/sinusadvancedsearchmachineservicecontracts", "basicHttp"), ISinusAdvancedSearchMachineService.class, features);
    }

}
