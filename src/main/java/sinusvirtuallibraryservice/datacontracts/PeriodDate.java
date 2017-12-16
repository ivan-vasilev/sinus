
package sinusvirtuallibraryservice.datacontracts;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PeriodDate complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PeriodDate">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Century" type="{http://sinusvirtuallibraryservice/datacontracts}FieldCentury" minOccurs="0"/>
 *         &lt;element name="Year" type="{http://sinusvirtuallibraryservice/datacontracts}FieldYear" minOccurs="0"/>
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
@XmlType(name = "PeriodDate", propOrder = {
    "century",
    "year",
    "select"
})
public class PeriodDate {

    @XmlElementRef(name = "Century", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<FieldCentury> century;
    @XmlElementRef(name = "Year", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<FieldYear> year;
    @XmlElement(name = "Select")
    protected Boolean select;

    /**
     * Gets the value of the century property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link FieldCentury }{@code >}
     *     
     */
    public JAXBElement<FieldCentury> getCentury() {
        return century;
    }

    /**
     * Sets the value of the century property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link FieldCentury }{@code >}
     *     
     */
    public void setCentury(JAXBElement<FieldCentury> value) {
        this.century = ((JAXBElement<FieldCentury> ) value);
    }

    /**
     * Gets the value of the year property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link FieldYear }{@code >}
     *     
     */
    public JAXBElement<FieldYear> getYear() {
        return year;
    }

    /**
     * Sets the value of the year property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link FieldYear }{@code >}
     *     
     */
    public void setYear(JAXBElement<FieldYear> value) {
        this.year = ((JAXBElement<FieldYear> ) value);
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
