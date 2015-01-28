package com.samenea.seapay.bank.gateway.model;

import com.samenea.commons.component.utils.exceptions.SamenRuntimeException;

/**
 * @author: Soroosh Sarabadani
 * Date: 1/6/13
 * Time: 4:33 PM
 * This exception throws when gatewayplugin verification has problem.
 */

public class VerifyException extends SamenRuntimeException {
    public VerifyException(String message){
        super(message);
    }

}
