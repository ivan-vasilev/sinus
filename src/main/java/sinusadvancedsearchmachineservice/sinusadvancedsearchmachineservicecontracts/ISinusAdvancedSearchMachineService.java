
package sinusadvancedsearchmachineservice.sinusadvancedsearchmachineservicecontracts;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebService(name = "ISinusAdvancedSearchMachineService", targetNamespace = "http://sinusadvancedsearchmachineservice/sinusadvancedsearchmachineservicecontracts")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({
    com.microsoft.schemas._2003._10.serialization.arrays.ObjectFactory.class,
    sinusadvancedsearchmachineservice.sinusadvancedsearchmachineservicecontracts.ObjectFactory.class,
    com.microsoft.schemas._2003._10.serialization.ObjectFactory.class
})
public interface ISinusAdvancedSearchMachineService {


    /**
     * 
     * @param parameters
     * @return
     *     returns sinusadvancedsearchmachineservice.sinusadvancedsearchmachineservicecontracts.FindIconsResult
     */
    @WebMethod(operationName = "FindIcons", action = "FindIcons")
    @WebResult(name = "FindIconsResult", targetNamespace = "http://sinusadvancedsearchmachineservice/sinusadvancedsearchmachineservicecontracts", partName = "parameters")
    public FindIconsResult findIcons(
        @WebParam(name = "FindIconsQuery", targetNamespace = "http://sinusadvancedsearchmachineservice/sinusadvancedsearchmachineservicecontracts", partName = "parameters")
        FindIconsQuery parameters);

    /**
     * 
     * @param parameters
     * @return
     *     returns sinusadvancedsearchmachineservice.sinusadvancedsearchmachineservicecontracts.AddIconsResult
     */
    @WebMethod(operationName = "AddIcons", action = "AddIcon")
    @WebResult(name = "AddIconsResult", targetNamespace = "http://sinusadvancedsearchmachineservice/sinusadvancedsearchmachineservicecontracts", partName = "parameters")
    public AddIconsResult addIcons(
        @WebParam(name = "AddIconsQuery", targetNamespace = "http://sinusadvancedsearchmachineservice/sinusadvancedsearchmachineservicecontracts", partName = "parameters")
        AddIconsQuery parameters);

}
