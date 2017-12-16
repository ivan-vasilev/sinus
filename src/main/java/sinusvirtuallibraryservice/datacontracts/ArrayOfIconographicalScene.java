
package sinusvirtuallibraryservice.datacontracts;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfIconographicalScene complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfIconographicalScene">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IconographicalScene" type="{http://sinusvirtuallibraryservice/datacontracts}IconographicalScene" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfIconographicalScene", propOrder = {
    "iconographicalScene"
})
public class ArrayOfIconographicalScene {

    @XmlElement(name = "IconographicalScene", nillable = true)
    protected List<IconographicalScene> iconographicalScene;

    /**
     * Gets the value of the iconographicalScene property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the iconographicalScene property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIconographicalScene().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IconographicalScene }
     * 
     * 
     */
    public List<IconographicalScene> getIconographicalScene() {
        if (iconographicalScene == null) {
            iconographicalScene = new ArrayList<IconographicalScene>();
        }
        return this.iconographicalScene;
    }

}
