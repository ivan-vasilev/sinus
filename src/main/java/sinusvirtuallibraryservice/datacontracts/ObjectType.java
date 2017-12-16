
package sinusvirtuallibraryservice.datacontracts;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ObjectType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ObjectType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="None"/>
 *     &lt;enumeration value="Icon"/>
 *     &lt;enumeration value="WallPainting"/>
 *     &lt;enumeration value="Miniature"/>
 *     &lt;enumeration value="Vitrage"/>
 *     &lt;enumeration value="Mosaic"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ObjectType")
@XmlEnum
public enum ObjectType {

    @XmlEnumValue("None")
    NONE("None"),
    @XmlEnumValue("Icon")
    ICON("Icon"),
    @XmlEnumValue("WallPainting")
    WALL_PAINTING("WallPainting"),
    @XmlEnumValue("Miniature")
    MINIATURE("Miniature"),
    @XmlEnumValue("Vitrage")
    VITRAGE("Vitrage"),
    @XmlEnumValue("Mosaic")
    MOSAIC("Mosaic");
    private final String value;

    ObjectType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ObjectType fromValue(String v) {
        for (ObjectType c: ObjectType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
