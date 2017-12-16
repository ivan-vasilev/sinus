
package sinusvirtuallibraryservice.datacontracts;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;


/**
 * <p>Java class for Author complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Author">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Name" type="{http://sinusvirtuallibraryservice/datacontracts}Field" minOccurs="0"/>
 *         &lt;element name="Biography" type="{http://sinusvirtuallibraryservice/datacontracts}AuthorBiography" minOccurs="0"/>
 *         &lt;element name="IconographicalClan" type="{http://sinusvirtuallibraryservice/datacontracts}Field" minOccurs="0"/>
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
@XmlType(name = "Author", propOrder = {
    "name",
    "bio",
    "iconographicalClan",
    "select"
})
public class Author {

    @XmlElementRef(name = "Name", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<Field> name;
    @XmlElementRef(name = "Biography", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = Author.Bio.class)
    protected Author.Bio bio;
    @XmlElementRef(name = "IconographicalClan", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<Field> iconographicalClan;
    @XmlElement(name = "Select")
    protected Boolean select;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Field }{@code >}
     *     
     */
    public JAXBElement<Field> getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Field }{@code >}
     *     
     */
    public void setName(JAXBElement<Field> value) {
        this.name = ((JAXBElement<Field> ) value);
    }

    /**
     * Gets the value of the bio property.
     * 
     * @return
     *     possible object is
     *     {@link Author.Bio }
     *     
     */
    public Author.Bio getBio() {
        return bio;
    }

    /**
     * Sets the value of the bio property.
     * 
     * @param value
     *     allowed object is
     *     {@link Author.Bio }
     *     
     */
    public void setBio(Author.Bio value) {
        this.bio = value;
    }

    /**
     * Gets the value of the iconographicalClan property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Field }{@code >}
     *     
     */
    public JAXBElement<Field> getIconographicalClan() {
        return iconographicalClan;
    }

    /**
     * Sets the value of the iconographicalClan property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Field }{@code >}
     *     
     */
    public void setIconographicalClan(JAXBElement<Field> value) {
        this.iconographicalClan = ((JAXBElement<Field> ) value);
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

    public static class Bio
        extends JAXBElement<AuthorBiography>
    {

        protected final static QName NAME = new QName("http://sinusvirtuallibraryservice/datacontracts", "Biography");

        public Bio(AuthorBiography value) {
            super(NAME, ((Class) AuthorBiography.class), Author.class, value);
        }

        public Bio() {
            super(NAME, ((Class) AuthorBiography.class), Author.class, null);
        }

    }

}
