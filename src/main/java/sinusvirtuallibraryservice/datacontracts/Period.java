
package sinusvirtuallibraryservice.datacontracts;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Period complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Period">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="From" type="{http://sinusvirtuallibraryservice/datacontracts}PeriodDate" minOccurs="0"/>
 *         &lt;element name="To" type="{http://sinusvirtuallibraryservice/datacontracts}PeriodDate" minOccurs="0"/>
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
@XmlType(name = "Period", propOrder = {
    "from",
    "to",
    "select"
})
public class Period {

    @XmlElementRef(name = "From", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<PeriodDate> from;
    @XmlElementRef(name = "To", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<PeriodDate> to;
    @XmlElement(name = "Select")
    protected Boolean select;

    /**
     * Gets the value of the from property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link PeriodDate }{@code >}
     *     
     */
    public JAXBElement<PeriodDate> getFrom() {
        return from;
    }

    /**
     * Sets the value of the from property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link PeriodDate }{@code >}
     *     
     */
    public void setFrom(JAXBElement<PeriodDate> value) {
        this.from = ((JAXBElement<PeriodDate> ) value);
    }

    /**
     * Gets the value of the to property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link PeriodDate }{@code >}
     *     
     */
    public JAXBElement<PeriodDate> getTo() {
        return to;
    }

    /**
     * Sets the value of the to property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link PeriodDate }{@code >}
     *     
     */
    public void setTo(JAXBElement<PeriodDate> value) {
        this.to = ((JAXBElement<PeriodDate> ) value);
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
