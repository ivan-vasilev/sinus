
package sinusvirtuallibraryservice.datacontracts;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Dimensions complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Dimensions">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Height" type="{http://sinusvirtuallibraryservice/datacontracts}FieldDouble" minOccurs="0"/>
 *         &lt;element name="Width" type="{http://sinusvirtuallibraryservice/datacontracts}FieldDouble" minOccurs="0"/>
 *         &lt;element name="Thickness" type="{http://sinusvirtuallibraryservice/datacontracts}FieldDouble" minOccurs="0"/>
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
@XmlType(name = "Dimensions", propOrder = {
    "height",
    "width",
    "thickness",
    "select"
})
public class Dimensions {

    @XmlElementRef(name = "Height", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<FieldDouble> height;
    @XmlElementRef(name = "Width", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<FieldDouble> width;
    @XmlElementRef(name = "Thickness", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<FieldDouble> thickness;
    @XmlElement(name = "Select")
    protected Boolean select;

    /**
     * Gets the value of the height property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link FieldDouble }{@code >}
     *     
     */
    public JAXBElement<FieldDouble> getHeight() {
        return height;
    }

    /**
     * Sets the value of the height property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link FieldDouble }{@code >}
     *     
     */
    public void setHeight(JAXBElement<FieldDouble> value) {
        this.height = ((JAXBElement<FieldDouble> ) value);
    }

    /**
     * Gets the value of the width property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link FieldDouble }{@code >}
     *     
     */
    public JAXBElement<FieldDouble> getWidth() {
        return width;
    }

    /**
     * Sets the value of the width property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link FieldDouble }{@code >}
     *     
     */
    public void setWidth(JAXBElement<FieldDouble> value) {
        this.width = ((JAXBElement<FieldDouble> ) value);
    }

    /**
     * Gets the value of the thickness property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link FieldDouble }{@code >}
     *     
     */
    public JAXBElement<FieldDouble> getThickness() {
        return thickness;
    }

    /**
     * Sets the value of the thickness property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link FieldDouble }{@code >}
     *     
     */
    public void setThickness(JAXBElement<FieldDouble> value) {
        this.thickness = ((JAXBElement<FieldDouble> ) value);
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
