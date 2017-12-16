
package sinusvirtuallibraryservice.datacontracts;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OperationForCompareField.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OperationForCompareField">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Equal"/>
 *     &lt;enumeration value="Diferent"/>
 *     &lt;enumeration value="Approximately"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OperationForCompareField")
@XmlEnum
public enum OperationForCompareField {

    @XmlEnumValue("Equal")
    EQUAL("Equal"),
    @XmlEnumValue("Diferent")
    DIFERENT("Diferent"),
    @XmlEnumValue("Approximately")
    APPROXIMATELY("Approximately");
    private final String value;

    OperationForCompareField(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static OperationForCompareField fromValue(String v) {
        for (OperationForCompareField c: OperationForCompareField.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
