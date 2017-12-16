
package sinusvirtuallibraryservice.datacontracts;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IconographicalScene complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IconographicalScene">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NameIconographicalScene" type="{http://sinusvirtuallibraryservice/datacontracts}Field" minOccurs="0"/>
 *         &lt;element name="CharacterBeingInScene" type="{http://sinusvirtuallibraryservice/datacontracts}ArrayOfField" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IconographicalScene", propOrder = {
    "nameIconographicalScene",
    "characterBeingInScene"
})
public class IconographicalScene {

    @XmlElementRef(name = "NameIconographicalScene", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<Field> nameIconographicalScene;
    @XmlElementRef(name = "CharacterBeingInScene", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<ArrayOfField> characterBeingInScene;

    /**
     * Gets the value of the nameIconographicalScene property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Field }{@code >}
     *     
     */
    public JAXBElement<Field> getNameIconographicalScene() {
        return nameIconographicalScene;
    }

    /**
     * Sets the value of the nameIconographicalScene property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Field }{@code >}
     *     
     */
    public void setNameIconographicalScene(JAXBElement<Field> value) {
        this.nameIconographicalScene = ((JAXBElement<Field> ) value);
    }

    /**
     * Gets the value of the characterBeingInScene property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfField }{@code >}
     *     
     */
    public JAXBElement<ArrayOfField> getCharacterBeingInScene() {
        return characterBeingInScene;
    }

    /**
     * Sets the value of the characterBeingInScene property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfField }{@code >}
     *     
     */
    public void setCharacterBeingInScene(JAXBElement<ArrayOfField> value) {
        this.characterBeingInScene = ((JAXBElement<ArrayOfField> ) value);
    }

}
