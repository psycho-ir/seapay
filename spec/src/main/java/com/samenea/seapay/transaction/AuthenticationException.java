package com.samenea.seapay.transaction;

import com.samenea.commons.component.utils.exceptions.SamenRuntimeException;

import javax.xml.ws.WebFault;

/**
 * @author: Jalal Ashrafi
 * Date: 1/6/13
 */
//If we don't want to use it for webservice a new exception with translation is needed
@WebFault(name = "AuthenticationException")
public class AuthenticationException extends SamenRuntimeException {
    public AuthenticationException(String message) {
    }

    public AuthenticationException(Throwable cause) {
        super(cause);
    }

    public AuthenticationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
