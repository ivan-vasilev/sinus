
package sinusvirtuallibraryservice.datacontracts;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PlasticType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PlasticType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="None"/>
 *     &lt;enumeration value="Iconostasis"/>
 *     &lt;enumeration value="Altar"/>
 *     &lt;enumeration value="Other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PlasticType")
@XmlEnum
public enum PlasticType {

    @XmlEnumValue("None")
    NONE("None"),
    @XmlEnumValue("Iconostasis")
    ICONOSTASIS("Iconostasis"),
    @XmlEnumValue("Altar")
    ALTAR("Altar"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    PlasticType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PlasticType fromValue(String v) {
        for (PlasticType c: PlasticType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
