
package sinusvirtuallibraryservice.datacontracts;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Identification complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Identification">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Title" type="{http://sinusvirtuallibraryservice/datacontracts}Field" minOccurs="0"/>
 *         &lt;element name="IconographicalObjectType" type="{http://sinusvirtuallibraryservice/datacontracts}IconographicalObjectType" minOccurs="0"/>
 *         &lt;element name="Author" type="{http://sinusvirtuallibraryservice/datacontracts}Author" minOccurs="0"/>
 *         &lt;element name="IconographicalSchool" type="{http://sinusvirtuallibraryservice/datacontracts}Field" minOccurs="0"/>
 *         &lt;element name="Period" type="{http://sinusvirtuallibraryservice/datacontracts}Period" minOccurs="0"/>
 *         &lt;element name="Dimensions" type="{http://sinusvirtuallibraryservice/datacontracts}Dimensions" minOccurs="0"/>
 *         &lt;element name="Location" type="{http://sinusvirtuallibraryservice/datacontracts}Place" minOccurs="0"/>
 *         &lt;element name="Source" type="{http://sinusvirtuallibraryservice/datacontracts}Place" minOccurs="0"/>
 *         &lt;element name="ObjectIdentificationNotes" type="{http://sinusvirtuallibraryservice/datacontracts}Field" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Identification", propOrder = {
    "title",
    "iconographicalObjectType",
    "author",
    "iconographicalSchool",
    "period",
    "dimensions",
    "location",
    "source",
    "objectIdentificationNotes"
})
public class Identification {

    @XmlElementRef(name = "Title", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<Field> title;
    @XmlElementRef(name = "IconographicalObjectType", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<IconographicalObjectType> iconographicalObjectType;
    @XmlElementRef(name = "Author", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<Author> author;
    @XmlElementRef(name = "IconographicalSchool", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<Field> iconographicalSchool;
    @XmlElementRef(name = "Period", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<Period> period;
    @XmlElementRef(name = "Dimensions", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<Dimensions> dimensions;
    @XmlElementRef(name = "Location", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<Place> location;
    @XmlElementRef(name = "Source", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<Place> source;
    @XmlElementRef(name = "ObjectIdentificationNotes", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<Field> objectIdentificationNotes;

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Field }{@code >}
     *     
     */
    public JAXBElement<Field> getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Field }{@code >}
     *     
     */
    public void setTitle(JAXBElement<Field> value) {
        this.title = ((JAXBElement<Field> ) value);
    }

    /**
     * Gets the value of the iconographicalObjectType property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link IconographicalObjectType }{@code >}
     *     
     */
    public JAXBElement<IconographicalObjectType> getIconographicalObjectType() {
        return iconographicalObjectType;
    }

    /**
     * Sets the value of the iconographicalObjectType property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link IconographicalObjectType }{@code >}
     *     
     */
    public void setIconographicalObjectType(JAXBElement<IconographicalObjectType> value) {
        this.iconographicalObjectType = ((JAXBElement<IconographicalObjectType> ) value);
    }

    /**
     * Gets the value of the author property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Author }{@code >}
     *     
     */
    public JAXBElement<Author> getAuthor() {
        return author;
    }

    /**
     * Sets the value of the author property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Author }{@code >}
     *     
     */
    public void setAuthor(JAXBElement<Author> value) {
        this.author = ((JAXBElement<Author> ) value);
    }

    /**
     * Gets the value of the iconographicalSchool property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Field }{@code >}
     *     
     */
    public JAXBElement<Field> getIconographicalSchool() {
        return iconographicalSchool;
    }

    /**
     * Sets the value of the iconographicalSchool property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Field }{@code >}
     *     
     */
    public void setIconographicalSchool(JAXBElement<Field> value) {
        this.iconographicalSchool = ((JAXBElement<Field> ) value);
    }

    /**
     * Gets the value of the period property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Period }{@code >}
     *     
     */
    public JAXBElement<Period> getPeriod() {
        return period;
    }

    /**
     * Sets the value of the period property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Period }{@code >}
     *     
     */
    public void setPeriod(JAXBElement<Period> value) {
        this.period = ((JAXBElement<Period> ) value);
    }

    /**
     * Gets the value of the dimensions property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Dimensions }{@code >}
     *     
     */
    public JAXBElement<Dimensions> getDimensions() {
        return dimensions;
    }

    /**
     * Sets the value of the dimensions property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Dimensions }{@code >}
     *     
     */
    public void setDimensions(JAXBElement<Dimensions> value) {
        this.dimensions = ((JAXBElement<Dimensions> ) value);
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Place }{@code >}
     *     
     */
    public JAXBElement<Place> getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Place }{@code >}
     *     
     */
    public void setLocation(JAXBElement<Place> value) {
        this.location = ((JAXBElement<Place> ) value);
    }

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Place }{@code >}
     *     
     */
    public JAXBElement<Place> getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Place }{@code >}
     *     
     */
    public void setSource(JAXBElement<Place> value) {
        this.source = ((JAXBElement<Place> ) value);
    }

    /**
     * Gets the value of the objectIdentificationNotes property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Field }{@code >}
     *     
     */
    public JAXBElement<Field> getObjectIdentificationNotes() {
        return objectIdentificationNotes;
    }

    /**
     * Sets the value of the objectIdentificationNotes property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Field }{@code >}
     *     
     */
    public void setObjectIdentificationNotes(JAXBElement<Field> value) {
        this.objectIdentificationNotes = ((JAXBElement<Field> ) value);
    }

}
