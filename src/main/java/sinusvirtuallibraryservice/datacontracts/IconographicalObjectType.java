
package sinusvirtuallibraryservice.datacontracts;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IconographicalObjectType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IconographicalObjectType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ObjectType" type="{http://sinusvirtuallibraryservice/datacontracts}ObjectType" minOccurs="0"/>
 *         &lt;element name="PlIconographicalObject" type="{http://sinusvirtuallibraryservice/datacontracts}PlasticIconographicalObjectType" minOccurs="0"/>
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
@XmlType(name = "IconographicalObjectType", propOrder = {
    "objectType",
    "plIconographicalObject",
    "other",
    "select"
})
public class IconographicalObjectType {

    @XmlElement(name = "ObjectType")
    protected ObjectType objectType;
    @XmlElementRef(name = "PlIconographicalObject", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<PlasticIconographicalObjectType> plIconographicalObject;
    @XmlElementRef(name = "Other", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<Field> other;
    @XmlElement(name = "Select")
    protected Boolean select;

    /**
     * Gets the value of the objectType property.
     * 
     * @return
     *     possible object is
     *     {@link ObjectType }
     *     
     */
    public ObjectType getObjectType() {
        return objectType;
    }

    /**
     * Sets the value of the objectType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectType }
     *     
     */
    public void setObjectType(ObjectType value) {
        this.objectType = value;
    }

    /**
     * Gets the value of the plIconographicalObject property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link PlasticIconographicalObjectType }{@code >}
     *     
     */
    public JAXBElement<PlasticIconographicalObjectType> getPlIconographicalObject() {
        return plIconographicalObject;
    }

    /**
     * Sets the value of the plIconographicalObject property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link PlasticIconographicalObjectType }{@code >}
     *     
     */
    public void setPlIconographicalObject(JAXBElement<PlasticIconographicalObjectType> value) {
        this.plIconographicalObject = ((JAXBElement<PlasticIconographicalObjectType> ) value);
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
