
package sinusvirtuallibraryservice.datacontracts;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FieldYear complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FieldYear">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Month" type="{http://sinusvirtuallibraryservice/datacontracts}FieldNumber" minOccurs="0"/>
 *         &lt;element name="Day" type="{http://sinusvirtuallibraryservice/datacontracts}FieldNumber" minOccurs="0"/>
 *         &lt;element name="YearValue" type="{http://sinusvirtuallibraryservice/datacontracts}FieldNumber" minOccurs="0"/>
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
@XmlType(name = "FieldYear", propOrder = {
    "month",
    "day",
    "yearValue",
    "select"
})
public class FieldYear {

    @XmlElementRef(name = "Month", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<FieldNumber> month;
    @XmlElementRef(name = "Day", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<FieldNumber> day;
    @XmlElementRef(name = "YearValue", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<FieldNumber> yearValue;
    @XmlElement(name = "Select")
    protected Boolean select;

    /**
     * Gets the value of the month property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link FieldNumber }{@code >}
     *     
     */
    public JAXBElement<FieldNumber> getMonth() {
        return month;
    }

    /**
     * Sets the value of the month property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link FieldNumber }{@code >}
     *     
     */
    public void setMonth(JAXBElement<FieldNumber> value) {
        this.month = ((JAXBElement<FieldNumber> ) value);
    }

    /**
     * Gets the value of the day property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link FieldNumber }{@code >}
     *     
     */
    public JAXBElement<FieldNumber> getDay() {
        return day;
    }

    /**
     * Sets the value of the day property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link FieldNumber }{@code >}
     *     
     */
    public void setDay(JAXBElement<FieldNumber> value) {
        this.day = ((JAXBElement<FieldNumber> ) value);
    }

    /**
     * Gets the value of the yearValue property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link FieldNumber }{@code >}
     *     
     */
    public JAXBElement<FieldNumber> getYearValue() {
        return yearValue;
    }

    /**
     * Sets the value of the yearValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link FieldNumber }{@code >}
     *     
     */
    public void setYearValue(JAXBElement<FieldNumber> value) {
        this.yearValue = ((JAXBElement<FieldNumber> ) value);
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
