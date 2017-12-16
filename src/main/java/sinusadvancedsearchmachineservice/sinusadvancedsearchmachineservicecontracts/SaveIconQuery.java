
package sinusadvancedsearchmachineservice.sinusadvancedsearchmachineservicecontracts;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import sinusvirtuallibraryservice.datacontracts.Icon;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Icon" type="{http://sinusvirtuallibraryservice/datacontracts}Icon" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "icon"
})
@XmlRootElement(name = "SaveIconQuery")
public class SaveIconQuery {

    @XmlElementRef(name = "Icon", namespace = "http://sinusadvancedsearchmachineservice/sinusadvancedsearchmachineservicecontracts", type = JAXBElement.class)
    protected JAXBElement<Icon> icon;

    /**
     * Gets the value of the icon property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Icon }{@code >}
     *     
     */
    public JAXBElement<Icon> getIcon() {
        return icon;
    }

    /**
     * Sets the value of the icon property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Icon }{@code >}
     *     
     */
    public void setIcon(JAXBElement<Icon> value) {
        this.icon = ((JAXBElement<Icon> ) value);
    }

}
