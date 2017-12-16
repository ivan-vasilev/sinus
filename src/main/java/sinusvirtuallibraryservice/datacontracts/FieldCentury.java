
package sinusvirtuallibraryservice.datacontracts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FieldCentury complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FieldCentury">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CenturyCompare" type="{http://sinusvirtuallibraryservice/datacontracts}OperationForCompareNumber" minOccurs="0"/>
 *         &lt;element name="TypeCentury" type="{http://sinusvirtuallibraryservice/datacontracts}Century" minOccurs="0"/>
 *         &lt;element name="ValueCentury" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
@XmlType(name = "FieldCentury", propOrder = {
    "centuryCompare",
    "typeCentury",
    "valueCentury",
    "select"
})
public class FieldCentury {

    @XmlElement(name = "CenturyCompare")
    protected OperationForCompareNumber centuryCompare;
    @XmlElement(name = "TypeCentury")
    protected Century typeCentury;
    @XmlElement(name = "ValueCentury")
    protected Integer valueCentury;
    @XmlElement(name = "Select")
    protected Boolean select;

    /**
     * Gets the value of the centuryCompare property.
     * 
     * @return
     *     possible object is
     *     {@link OperationForCompareNumber }
     *     
     */
    public OperationForCompareNumber getCenturyCompare() {
        return centuryCompare;
    }

    /**
     * Sets the value of the centuryCompare property.
     * 
     * @param value
     *     allowed object is
     *     {@link OperationForCompareNumber }
     *     
     */
    public void setCenturyCompare(OperationForCompareNumber value) {
        this.centuryCompare = value;
    }

    /**
     * Gets the value of the typeCentury property.
     * 
     * @return
     *     possible object is
     *     {@link Century }
     *     
     */
    public Century getTypeCentury() {
        return typeCentury;
    }

    /**
     * Sets the value of the typeCentury property.
     * 
     * @param value
     *     allowed object is
     *     {@link Century }
     *     
     */
    public void setTypeCentury(Century value) {
        this.typeCentury = value;
    }

    /**
     * Gets the value of the valueCentury property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getValueCentury() {
        return valueCentury;
    }

    /**
     * Sets the value of the valueCentury property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setValueCentury(Integer value) {
        this.valueCentury = value;
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
