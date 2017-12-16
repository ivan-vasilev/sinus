
package sinusvirtuallibraryservice.datacontracts;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Description complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Description">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Characters" type="{http://sinusvirtuallibraryservice/datacontracts}ArrayOfCharacter" minOccurs="0"/>
 *         &lt;element name="IconographicalScenes" type="{http://sinusvirtuallibraryservice/datacontracts}ArrayOfIconographicalScene" minOccurs="0"/>
 *         &lt;element name="ValueDescription" type="{http://sinusvirtuallibraryservice/datacontracts}Field" minOccurs="0"/>
 *         &lt;element name="Select" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="SelectCharacters" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="SelectIconographicalScenes" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Description", propOrder = {
    "characters",
    "iconographicalScenes",
    "valueDescription",
    "select",
    "selectCharacters",
    "selectIconographicalScenes"
})
public class Description {

    @XmlElementRef(name = "Characters", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<ArrayOfCharacter> characters;
    @XmlElementRef(name = "IconographicalScenes", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<ArrayOfIconographicalScene> iconographicalScenes;
    @XmlElementRef(name = "ValueDescription", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<Field> valueDescription;
    @XmlElement(name = "Select")
    protected Boolean select;
    @XmlElement(name = "SelectCharacters")
    protected Boolean selectCharacters;
    @XmlElement(name = "SelectIconographicalScenes")
    protected Boolean selectIconographicalScenes;

    /**
     * Gets the value of the characters property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfCharacter }{@code >}
     *     
     */
    public JAXBElement<ArrayOfCharacter> getCharacters() {
        return characters;
    }

    /**
     * Sets the value of the characters property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfCharacter }{@code >}
     *     
     */
    public void setCharacters(JAXBElement<ArrayOfCharacter> value) {
        this.characters = ((JAXBElement<ArrayOfCharacter> ) value);
    }

    /**
     * Gets the value of the iconographicalScenes property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfIconographicalScene }{@code >}
     *     
     */
    public JAXBElement<ArrayOfIconographicalScene> getIconographicalScenes() {
        return iconographicalScenes;
    }

    /**
     * Sets the value of the iconographicalScenes property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfIconographicalScene }{@code >}
     *     
     */
    public void setIconographicalScenes(JAXBElement<ArrayOfIconographicalScene> value) {
        this.iconographicalScenes = ((JAXBElement<ArrayOfIconographicalScene> ) value);
    }

    /**
     * Gets the value of the valueDescription property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Field }{@code >}
     *     
     */
    public JAXBElement<Field> getValueDescription() {
        return valueDescription;
    }

    /**
     * Sets the value of the valueDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Field }{@code >}
     *     
     */
    public void setValueDescription(JAXBElement<Field> value) {
        this.valueDescription = ((JAXBElement<Field> ) value);
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

    /**
     * Gets the value of the selectCharacters property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSelectCharacters() {
        return selectCharacters;
    }

    /**
     * Sets the value of the selectCharacters property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSelectCharacters(Boolean value) {
        this.selectCharacters = value;
    }

    /**
     * Gets the value of the selectIconographicalScenes property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSelectIconographicalScenes() {
        return selectIconographicalScenes;
    }

    /**
     * Sets the value of the selectIconographicalScenes property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSelectIconographicalScenes(Boolean value) {
        this.selectIconographicalScenes = value;
    }

}
