
package com.samenea.seapay.client.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for transactionStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="transactionStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="NEW"/>
 *     &lt;enumeration value="BANK_RESOLVED"/>
 *     &lt;enumeration value="COMMITED"/>
 *     &lt;enumeration value="FAILED"/>
 *     &lt;enumeration value="UNKNOWN"/>
 *     &lt;enumeration value="COMMITED_WITH_DELAY"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "transactionStatus")
@XmlEnum
public enum TransactionStatus {

    NEW,
    BANK_RESOLVED,
    COMMITED,
    FAILED,
    UNKNOWN,
    COMMITED_WITH_DELAY;

    public String value() {
        return name();
    }

    public static TransactionStatus fromValue(String v) {
        return valueOf(v);
    }

}
