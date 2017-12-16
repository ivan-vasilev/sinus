
package sinusadvancedsearchmachineservice.sinusadvancedsearchmachineservicecontracts;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfstring;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the sinusadvancedsearchmachineservice.sinusadvancedsearchmachineservicecontracts package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _FindIconsQuerySparql_QNAME = new QName("http://sinusadvancedsearchmachineservice/sinusadvancedsearchmachineservicecontracts", "Sparql");
    private final static QName _FindIconsResultURIs_QNAME = new QName("http://sinusadvancedsearchmachineservice/sinusadvancedsearchmachineservicecontracts", "URIs");
    private final static QName _AddIconsQueryIcons_QNAME = new QName("http://sinusadvancedsearchmachineservice/sinusadvancedsearchmachineservicecontracts", "Icons");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: sinusadvancedsearchmachineservice.sinusadvancedsearchmachineservicecontracts
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FindIconsQuery }
     * 
     */
    public FindIconsQuery createFindIconsQuery() {
        return new FindIconsQuery();
    }

    /**
     * Create an instance of {@link AddIconsResult }
     * 
     */
    public AddIconsResult createAddIconsResult() {
        return new AddIconsResult();
    }

    /**
     * Create an instance of {@link FindIconsResult }
     * 
     */
    public FindIconsResult createFindIconsResult() {
        return new FindIconsResult();
    }

    /**
     * Create an instance of {@link AddIconsQuery }
     * 
     */
    public AddIconsQuery createAddIconsQuery() {
        return new AddIconsQuery();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sinusadvancedsearchmachineservice/sinusadvancedsearchmachineservicecontracts", name = "Sparql", scope = FindIconsQuery.class)
    public JAXBElement<String> createFindIconsQuerySparql(String value) {
        return new JAXBElement<String>(_FindIconsQuerySparql_QNAME, String.class, FindIconsQuery.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sinusadvancedsearchmachineservice/sinusadvancedsearchmachineservicecontracts", name = "URIs", scope = FindIconsResult.class)
    public JAXBElement<ArrayOfstring> createFindIconsResultURIs(ArrayOfstring value) {
        return new JAXBElement<ArrayOfstring>(_FindIconsResultURIs_QNAME, ArrayOfstring.class, FindIconsResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sinusadvancedsearchmachineservice/sinusadvancedsearchmachineservicecontracts", name = "Icons", scope = AddIconsQuery.class)
    public JAXBElement<String> createAddIconsQueryIcons(String value) {
        return new JAXBElement<String>(_AddIconsQueryIcons_QNAME, String.class, AddIconsQuery.class, value);
    }

}
