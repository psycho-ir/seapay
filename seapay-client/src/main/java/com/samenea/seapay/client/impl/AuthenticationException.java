package com.samenea.seapay.client.impl;

import com.samenea.seapay.client.ws.AuthenticationException_Exception;

/**
 * @author: Jalal Ashrafi
 * Date: 1/20/13
 */
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationException(Throwable cause) {
        super(cause);
    }
}
