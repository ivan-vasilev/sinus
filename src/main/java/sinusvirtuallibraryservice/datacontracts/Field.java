
package sinusvirtuallibraryservice.datacontracts;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Field complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Field">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Operation" type="{http://sinusvirtuallibraryservice/datacontracts}OperationForCompareField" minOccurs="0"/>
 *         &lt;element name="BgValueField" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AnglValueField" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "Field", propOrder = {
    "operation",
    "bgValueField",
    "anglValueField",
    "select"
})
public class Field {

    @XmlElement(name = "Operation")
    protected OperationForCompareField operation;
    @XmlElementRef(name = "BgValueField", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<String> bgValueField;
    @XmlElementRef(name = "AnglValueField", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<String> anglValueField;
    @XmlElement(name = "Select")
    protected Boolean select;

    /**
     * Gets the value of the operation property.
     * 
     * @return
     *     possible object is
     *     {@link OperationForCompareField }
     *     
     */
    public OperationForCompareField getOperation() {
        return operation;
    }

    /**
     * Sets the value of the operation property.
     * 
     * @param value
     *     allowed object is
     *     {@link OperationForCompareField }
     *     
     */
    public void setOperation(OperationForCompareField value) {
        this.operation = value;
    }

    /**
     * Gets the value of the bgValueField property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getBgValueField() {
        return bgValueField;
    }

    /**
     * Sets the value of the bgValueField property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setBgValueField(JAXBElement<String> value) {
        this.bgValueField = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the anglValueField property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAnglValueField() {
        return anglValueField;
    }

    /**
     * Sets the value of the anglValueField property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAnglValueField(JAXBElement<String> value) {
        this.anglValueField = ((JAXBElement<String> ) value);
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
