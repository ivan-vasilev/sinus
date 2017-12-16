
package sinusvirtuallibraryservice.datacontracts;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TypeTechnique complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TypeTechnique">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Name" type="{http://sinusvirtuallibraryservice/datacontracts}Field" minOccurs="0"/>
 *         &lt;element name="Description" type="{http://sinusvirtuallibraryservice/datacontracts}Field" minOccurs="0"/>
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
@XmlType(name = "TypeTechnique", propOrder = {
    "name",
    "description",
    "select"
})
public class TypeTechnique {

    @XmlElementRef(name = "Name", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<Field> name;
    @XmlElementRef(name = "Description", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<Field> description;
    @XmlElement(name = "Select")
    protected Boolean select;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Field }{@code >}
     *     
     */
    public JAXBElement<Field> getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Field }{@code >}
     *     
     */
    public void setName(JAXBElement<Field> value) {
        this.name = ((JAXBElement<Field> ) value);
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Field }{@code >}
     *     
     */
    public JAXBElement<Field> getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Field }{@code >}
     *     
     */
    public void setDescription(JAXBElement<Field> value) {
        this.description = ((JAXBElement<Field> ) value);
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
