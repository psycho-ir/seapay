package com.samenea.seapay.bank.service.gateway.plugin.mellat;

import com.samenea.commons.component.utils.exceptions.SamenRuntimeException;

/**
 * @author: Soroosh Sarabadani
 * Date: 9/30/13
 * Time: 1:29 PM
 */

public class NoMellatResponseException extends SamenRuntimeException {
    public NoMellatResponseException(String s) {
        super(s);
    }
}
