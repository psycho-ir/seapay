package com.samenea.seapay.transaction.exceptions;

import javax.xml.ws.WebFault;

/**
 * @author: Jalal Ashrafi
 * Date: 1/20/13
 */
//Just is used for translating in web service
@WebFault(name = "IllegalStateException")
public class IllegalStateException extends RuntimeException{
    public IllegalStateException() {
        super();
    }

    public IllegalStateException(String message) {
        super(message);
    }

    public IllegalStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalStateException(Throwable cause) {
        super(cause);
    }

    protected IllegalStateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
