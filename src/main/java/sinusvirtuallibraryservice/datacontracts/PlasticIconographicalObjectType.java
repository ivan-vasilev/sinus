
package sinusvirtuallibraryservice.datacontracts;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PlasticIconographicalObjectType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PlasticIconographicalObjectType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PlasticType" type="{http://sinusvirtuallibraryservice/datacontracts}PlasticType" minOccurs="0"/>
 *         &lt;element name="Other" type="{http://sinusvirtuallibraryservice/datacontracts}Field" minOccurs="0"/>
 *         &lt;element name="Select" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PlasticIconographicalObjectType", propOrder = {
    "plasticType",
    "other",
    "select"
})
public class PlasticIconographicalObjectType {

    @XmlElement(name = "PlasticType")
    protected PlasticType plasticType;
    @XmlElementRef(name = "Other", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<Field> other;
    @XmlElement(name = "Select")
    protected Boolean select;

    /**
     * Gets the value of the plasticType property.
     * 
     * @return
     *     possible object is
     *     {@link PlasticType }
     *     
     */
    public PlasticType getPlasticType() {
        return plasticType;
    }

    /**
     * Sets the value of the plasticType property.
     * 
     * @param value
     *     allowed object is
     *     {@link PlasticType }
     *     
     */
    public void setPlasticType(PlasticType value) {
        this.plasticType = value;
    }

    /**
     * Gets the value of the other property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Field }{@code >}
     *     
     */
    public JAXBElement<Field> getOther() {
        return other;
    }

    /**
     * Sets the value of the other property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Field }{@code >}
     *     
     */
    public void setOther(JAXBElement<Field> value) {
        this.other = ((JAXBElement<Field> ) value);
    }

    /**
     * Gets the value of the select property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSelect() {
        return select;
    }

    /**
     * Sets the value of the select property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSelect(Boolean value) {
        this.select = value;
    }

}
