
package sinusvirtuallibraryservice.datacontracts;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Technology complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Technology">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IconographicalTechnique" type="{http://sinusvirtuallibraryservice/datacontracts}TypeTechnique" minOccurs="0"/>
 *         &lt;element name="BaseMaterial" type="{http://sinusvirtuallibraryservice/datacontracts}TypeTechnique" minOccurs="0"/>
 *         &lt;element name="StateRestorationTraces" type="{http://sinusvirtuallibraryservice/datacontracts}Field" minOccurs="0"/>
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
@XmlType(name = "Technology", propOrder = {
    "iconographicalTechnique",
    "baseMaterial",
    "stateRestorationTraces",
    "select"
})
public class Technology {

    @XmlElementRef(name = "IconographicalTechnique", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<TypeTechnique> iconographicalTechnique;
    @XmlElementRef(name = "BaseMaterial", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<TypeTechnique> baseMaterial;
    @XmlElementRef(name = "StateRestorationTraces", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<Field> stateRestorationTraces;
    @XmlElement(name = "Select")
    protected Boolean select;

    /**
     * Gets the value of the iconographicalTechnique property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link TypeTechnique }{@code >}
     *     
     */
    public JAXBElement<TypeTechnique> getIconographicalTechnique() {
        return iconographicalTechnique;
    }

    /**
     * Sets the value of the iconographicalTechnique property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link TypeTechnique }{@code >}
     *     
     */
    public void setIconographicalTechnique(JAXBElement<TypeTechnique> value) {
        this.iconographicalTechnique = ((JAXBElement<TypeTechnique> ) value);
    }

    /**
     * Gets the value of the baseMaterial property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link TypeTechnique }{@code >}
     *     
     */
    public JAXBElement<TypeTechnique> getBaseMaterial() {
        return baseMaterial;
    }

    /**
     * Sets the value of the baseMaterial property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link TypeTechnique }{@code >}
     *     
     */
    public void setBaseMaterial(JAXBElement<TypeTechnique> value) {
        this.baseMaterial = ((JAXBElement<TypeTechnique> ) value);
    }

    /**
     * Gets the value of the stateRestorationTraces property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Field }{@code >}
     *     
     */
    public JAXBElement<Field> getStateRestorationTraces() {
        return stateRestorationTraces;
    }

    /**
     * Sets the value of the stateRestorationTraces property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Field }{@code >}
     *     
     */
    public void setStateRestorationTraces(JAXBElement<Field> value) {
        this.stateRestorationTraces = ((JAXBElement<Field> ) value);
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
