
package sinusvirtuallibraryservice.datacontracts;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Century.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Century">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="None"/>
 *     &lt;enumeration value="TheBeginningOf"/>
 *     &lt;enumeration value="TheMiddleOf"/>
 *     &lt;enumeration value="TheEndOf"/>
 *     &lt;enumeration value="TheFirstHalf"/>
 *     &lt;enumeration value="TheSecondHalf"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Century")
@XmlEnum
public enum Century {

    @XmlEnumValue("None")
    NONE("None"),
    @XmlEnumValue("TheBeginningOf")
    THE_BEGINNING_OF("TheBeginningOf"),
    @XmlEnumValue("TheMiddleOf")
    THE_MIDDLE_OF("TheMiddleOf"),
    @XmlEnumValue("TheEndOf")
    THE_END_OF("TheEndOf"),
    @XmlEnumValue("TheFirstHalf")
    THE_FIRST_HALF("TheFirstHalf"),
    @XmlEnumValue("TheSecondHalf")
    THE_SECOND_HALF("TheSecondHalf");
    private final String value;

    Century(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Century fromValue(String v) {
        for (Century c: Century.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
