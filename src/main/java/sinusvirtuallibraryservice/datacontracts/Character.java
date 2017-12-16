
package sinusvirtuallibraryservice.datacontracts;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Character complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Character">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CharacterName" type="{http://sinusvirtuallibraryservice/datacontracts}Field" minOccurs="0"/>
 *         &lt;element name="CanonicalCharacter" type="{http://sinusvirtuallibraryservice/datacontracts}Field" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Character", propOrder = {
    "characterName",
    "canonicalCharacter"
})
public class Character {

    @XmlElementRef(name = "CharacterName", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<Field> characterName;
    @XmlElementRef(name = "CanonicalCharacter", namespace = "http://sinusvirtuallibraryservice/datacontracts", type = JAXBElement.class)
    protected JAXBElement<Field> canonicalCharacter;

    /**
     * Gets the value of the characterName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Field }{@code >}
     *     
     */
    public JAXBElement<Field> getCharacterName() {
        return characterName;
    }

    /**
     * Sets the value of the characterName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Field }{@code >}
     *     
     */
    public void setCharacterName(JAXBElement<Field> value) {
        this.characterName = ((JAXBElement<Field> ) value);
    }

    /**
     * Gets the value of the canonicalCharacter property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Field }{@code >}
     *     
     */
    public JAXBElement<Field> getCanonicalCharacter() {
        return canonicalCharacter;
    }

    /**
     * Sets the value of the canonicalCharacter property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Field }{@code >}
     *     
     */
    public void setCanonicalCharacter(JAXBElement<Field> value) {
        this.canonicalCharacter = ((JAXBElement<Field> ) value);
    }

}
