
package sinusvirtuallibraryservice.datacontracts;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfstring;


/**
 * <p>Java class for Icon complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Icon">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="URI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Identification" type="{http://sinusvirtuallibraryservice/datacontracts}Identification" minOccurs="0"/>
 *         &lt;element name="Description" type="{http://sinusvirtuallibraryservice/datacontracts}Description" minOccurs="0"/>
 *         &lt;element name="Technology" type="{http://sinusvirtuallibraryservice/datacontracts}Technology" minOccurs="0"/>
 *         &lt;element name="Files" type="{http://schemas.microsoft.com/2003/10/Serialization/Arrays}ArrayOfstring" minOccurs="0"/>
 *         &lt;element name="PropertyEmpy" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SmallImage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LargerImage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Icon", propOrder = {
    "uri",
    "identification",
    "description",
    "technology",
    "files",
    "propertyEmpy",
    "smallImage",
    "largerImage"
})
public class Icon {

    @XmlElementRef(name = "URI", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<String> uri;
    @XmlElementRef(name = "Identification", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<Identification> identification;
    @XmlElementRef(name = "Description", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<Description> description;
    @XmlElementRef(name = "Technology", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<Technology> technology;
    @XmlElementRef(name = "Files", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<ArrayOfstring> files;
    @XmlElementRef(name = "PropertyEmpy", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<String> propertyEmpy;
    @XmlElementRef(name = "SmallImage", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<String> smallImage;
    @XmlElementRef(name = "LargerImage", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<String> largerImage;

    /**
     * Gets the value of the uri property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getURI() {
        return uri;
    }

    /**
     * Sets the value of the uri property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setURI(JAXBElement<String> value) {
        this.uri = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the identification property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Identification }{@code >}
     *     
     */
    public JAXBElement<Identification> getIdentification() {
        return identification;
    }

    /**
     * Sets the value of the identification property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Identification }{@code >}
     *     
     */
    public void setIdentification(JAXBElement<Identification> value) {
        this.identification = ((JAXBElement<Identification> ) value);
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Description }{@code >}
     *     
     */
    public JAXBElement<Description> getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Description }{@code >}
     *     
     */
    public void setDescription(JAXBElement<Description> value) {
        this.description = ((JAXBElement<Description> ) value);
    }

    /**
     * Gets the value of the technology property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Technology }{@code >}
     *     
     */
    public JAXBElement<Technology> getTechnology() {
        return technology;
    }

    /**
     * Sets the value of the technology property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Technology }{@code >}
     *     
     */
    public void setTechnology(JAXBElement<Technology> value) {
        this.technology = ((JAXBElement<Technology> ) value);
    }

    /**
     * Gets the value of the files property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}
     *     
     */
    public JAXBElement<ArrayOfstring> getFiles() {
        return files;
    }

    /**
     * Sets the value of the files property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}
     *     
     */
    public void setFiles(JAXBElement<ArrayOfstring> value) {
        this.files = ((JAXBElement<ArrayOfstring> ) value);
    }

    /**
     * Gets the value of the propertyEmpy property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPropertyEmpy() {
        return propertyEmpy;
    }

    /**
     * Sets the value of the propertyEmpy property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPropertyEmpy(JAXBElement<String> value) {
        this.propertyEmpy = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the smallImage property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSmallImage() {
        return smallImage;
    }

    /**
     * Sets the value of the smallImage property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSmallImage(JAXBElement<String> value) {
        this.smallImage = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the largerImage property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLargerImage() {
        return largerImage;
    }

    /**
     * Sets the value of the largerImage property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLargerImage(JAXBElement<String> value) {
        this.largerImage = ((JAXBElement<String> ) value);
    }

}
