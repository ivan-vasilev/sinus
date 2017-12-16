
package sinusvirtuallibraryservice.datacontracts;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OperationForCompareNumber.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OperationForCompareNumber">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Equal"/>
 *     &lt;enumeration value="Diferent"/>
 *     &lt;enumeration value="BiggerOrEqual"/>
 *     &lt;enumeration value="LessOrEqual"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OperationForCompareNumber")
@XmlEnum
public enum OperationForCompareNumber {

    @XmlEnumValue("Equal")
    EQUAL("Equal"),
    @XmlEnumValue("Diferent")
    DIFERENT("Diferent"),
    @XmlEnumValue("BiggerOrEqual")
    BIGGER_OR_EQUAL("BiggerOrEqual"),
    @XmlEnumValue("LessOrEqual")
    LESS_OR_EQUAL("LessOrEqual");
    private final String value;

    OperationForCompareNumber(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static OperationForCompareNumber fromValue(String v) {
        for (OperationForCompareNumber c: OperationForCompareNumber.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
